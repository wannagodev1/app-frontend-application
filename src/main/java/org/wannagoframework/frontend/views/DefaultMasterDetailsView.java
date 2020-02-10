package org.wannagoframework.frontend.views;

import ch.carnet.kasparscherrer.EmptyFormLayoutItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawer;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawerFooter;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawerHeader;
import org.wannagoframework.frontend.components.navigation.bar.AppBar;
import org.wannagoframework.frontend.components.navigation.bar.AppBar.NaviMode;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.layout.SplitViewFrame;
import org.wannagoframework.frontend.layout.size.Horizontal;
import org.wannagoframework.frontend.layout.size.Top;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.css.BoxSizing;
import org.wannagoframework.frontend.utils.i18n.DateTimeFormatter;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 8/27/19
 */
public abstract class DefaultMasterDetailsView<T extends BaseEntity, F extends DefaultFilter> extends
    SplitViewFrame {

  protected final String I18N_PREFIX;
  protected Grid<T> grid;
  protected DefaultDataProvider<T, F> dataProvider;
  protected DetailsDrawer detailsDrawer;
  protected DetailsDrawerHeader detailsDrawerHeader;
  protected DetailsDrawerFooter detailsDrawerFooter;
  protected Binder<T> binder;
  private T currentEditing;
  private Class<T> entityType;
  private Function<T, T> saveHandler;
  private Consumer<T> deleteHandler;
  private Tabs tabs;

  public DefaultMasterDetailsView(String I18N_PREFIX, Class<T> entityType,
      DefaultDataProvider<T, F> dataProvider) {
    this.I18N_PREFIX = I18N_PREFIX;
    this.entityType = entityType;
    this.binder = new BeanValidationBinder<>(entityType);
    this.dataProvider = dataProvider;
    this.saveHandler = null;
    this.deleteHandler = null;
  }

  public DefaultMasterDetailsView(String I18N_PREFIX, Class<T> entityType,
      DefaultDataProvider<T, F> dataProvider,
      Function<T, T> saveHandler, Consumer<T> deleteHandler) {
    this.I18N_PREFIX = I18N_PREFIX;
    this.entityType = entityType;
    this.binder = new BeanValidationBinder<>(entityType);
    this.dataProvider = dataProvider;
    this.saveHandler = saveHandler;
    this.deleteHandler = deleteHandler;
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    initHeader();
    setViewContent(createContent());
    setViewDetails(createDetailsDrawer());
    filter(null);
  }

  protected void initHeader() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    appBar.setNaviMode(NaviMode.MENU);

    Button searchButton = UIUtils
        .createTertiaryButton(VaadinIcon.SEARCH);
    searchButton.addClickListener(event -> appBar.searchModeOn());
    appBar.addSearchListener(event -> filter((String) event.getValue()));
    appBar.setSearchPlaceholder(getTranslation("element.global.search"));
    appBar.addActionItem(searchButton);

    if (saveHandler != null) {
      Button newPlaceButton = UIUtils
          .createTertiaryButton(VaadinIcon.PLUS);
      newPlaceButton.addClickListener(event -> {
        try {
          showDetails(entityType.getDeclaredConstructor().newInstance());

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
      });
      appBar.addActionItem(newPlaceButton);
    }
  }

  private Component createContent() {
    FlexBoxLayout content = new FlexBoxLayout(createGrid());
    content.setBoxSizing(BoxSizing.BORDER_BOX);
    content.setHeightFull();
    content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
    return content;
  }

  protected abstract Grid createGrid();

  private DetailsDrawer createDetailsDrawer() {
    detailsDrawer = new DetailsDrawer(DetailsDrawer.Position.RIGHT);

    // Header
    Tab details = new Tab(getTranslation("element.title.details"));
    Tab audit = new Tab(getTranslation("element.title.audit"));

    tabs = new Tabs(details, audit);
    tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
    tabs.addSelectedChangeListener(e -> {
      Tab selectedTab = tabs.getSelectedTab();
      if (selectedTab.equals(details)) {
        detailsDrawer
            .setContent(createDetails(currentEditing));
      } else if (selectedTab.equals(audit)) {
        detailsDrawer
            .setContent(createAudit(currentEditing));
      }
    });

    detailsDrawerHeader = new DetailsDrawerHeader(
        getTranslation("element." + I18N_PREFIX + "className"), tabs);
    detailsDrawerHeader.addCloseListener(e -> {
      detailsDrawer.hide();
      currentEditing = null;
    });
    detailsDrawer.setHeader(detailsDrawerHeader);

    // Footer
    detailsDrawerFooter = new DetailsDrawerFooter();
    if (saveHandler == null) {
      detailsDrawerFooter.setSaveButtonVisible(false);
    }
    if (deleteHandler == null) {
      detailsDrawerFooter.setDeleteButtonVisible(false);
    }

    detailsDrawerFooter.addCancelListener(e -> {
      detailsDrawer.hide();
      currentEditing = null;
    });
    if (saveHandler != null) {
      detailsDrawerFooter.addSaveListener(e -> save());
    }
    if (deleteHandler != null) {
      detailsDrawerFooter.addDeleteListener(e -> delete());
    }
    detailsDrawer.setFooter(detailsDrawerFooter);

    return detailsDrawer;
  }

  protected void showDetails(T entity) {
    currentEditing = entity;
    detailsDrawer.setContent(createDetails(entity));
    detailsDrawer.show();
    tabs.setSelectedIndex(0);
  }

  protected abstract Component createDetails(T entity);

  private Component createAudit(T entity) {
    TextField id = new TextField();
    id.setWidth("100%");

    TextField created = new TextField();
    created.setWidth("100%");

    TextField updated = new TextField();
    updated.setWidth("100%");

    TextField createdBy = new TextField();
    createdBy.setWidth("100%");

    TextField updatedBy = new TextField();
    updatedBy.setWidth("100%");

    // Form layout
    FormLayout auditForm = new FormLayout();
    auditForm.addClassNames(LumoStyles.Padding.Bottom.L,
        LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
    auditForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1,
            FormLayout.ResponsiveStep.LabelsPosition.TOP),
        new FormLayout.ResponsiveStep("21em", 2,
            FormLayout.ResponsiveStep.LabelsPosition.TOP));

    auditForm.addFormItem(id, getTranslation("element.baseEntity.id"));
    auditForm.add(new EmptyFormLayoutItem());
    auditForm.addFormItem(created, getTranslation("element.baseEntity.created"));
    auditForm.addFormItem(updated, getTranslation("element.baseEntity.updated"));
    auditForm.addFormItem(createdBy, getTranslation("element.baseEntity.createdBy"));
    auditForm.addFormItem(updatedBy, getTranslation("element.baseEntity.updatedBy"));

    binder.bind(id, entity1 -> entity1.getId() == null ? null :
            entity1.getId().toString(),
        null);
    binder.bind(created, entity1 -> entity1.getCreated() == null ? ""
        : DateTimeFormatter.format(entity1.getCreated()), (a, b) -> {
    });
    binder.bind(createdBy,
        activityDisplay -> entity.getCreatedBy(),
        null);
    binder.bind(updated, entity1 -> entity1.getModified() == null ? ""
        : DateTimeFormatter.format(entity1.getModified()), (a, b) -> {
    });
    binder.bind(updatedBy,
        activityDisplay -> entity.getModifiedBy(),
        null);

    return auditForm;
  }

  protected void afterSave(T entity) {
  }

  ;

  protected void afterDelete() {
  }

  ;

  private void save() {
    Object id = binder.getBean().getId();

    boolean isNew = id == null;

    if (binder.writeBeanIfValid(currentEditing)) {
      currentEditing = saveHandler.apply(currentEditing);
      afterSave(currentEditing);
    } else {
      Notification.show(getTranslation("message.global.validationErrorMessage"), 3000,
          Notification.Position.BOTTOM_CENTER);
    }

    if (!isNew) {
      dataProvider.refreshItem(currentEditing);
    } else {
      dataProvider.refreshAll();
    }

    showDetails(currentEditing);
  }

  public void delete() {
    ConfirmDialog dialog = new ConfirmDialog(getTranslation("message.global.confirmDelete.title"),
        getTranslation("message.global.confirmDelete.message"),
        getTranslation("action.global.deleteButton"), event -> deleteConfirmed(),
        getTranslation("action.global.cancelButton"), event -> {
    });
    dialog.setConfirmButtonTheme("error primary");

    dialog.setOpened(true);
  }

  private void deleteConfirmed() {
    deleteHandler.accept(currentEditing);

    afterDelete();

    detailsDrawer.hide();

    dataProvider.refreshAll();

    currentEditing = null;
  }

  protected void filter(String filter) {
    dataProvider
        .setFilter((F) new DefaultFilter(
            StringUtils.isBlank(filter) ? null : filter,
            Boolean.TRUE));
  }
}

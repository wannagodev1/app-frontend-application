package org.wannagoframework.frontend.views;

import ch.carnet.kasparscherrer.EmptyFormLayoutItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.crud.CrudI18n.Confirmations.Confirmation;
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
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.frontend.components.ConfirmationDialog;
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
    SplitViewFrame implements BeforeLeaveObserver {

  protected final String I18N_PREFIX;
  protected Grid<T> grid;
  protected DefaultDataProvider<T, F> dataProvider;
  protected DetailsDrawer detailsDrawer;
  protected DetailsDrawerHeader detailsDrawerHeader;
  protected DetailsDrawerFooter detailsDrawerFooter;
  protected Binder<T> binder;
  private T currentEditing;
  private Class<T> entityType;
  private Function<T, ServiceResult<T>> saveHandler;
  private Consumer<T> deleteHandler;
  private Tabs tabs;
  private Button newRecordButton;

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
      Function<T, ServiceResult<T>> saveHandler, Consumer<T> deleteHandler) {
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

    if ( currentEditing != null )
      showDetails( currentEditing );
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
    ContinueNavigationAction action = beforeLeaveEvent.postpone();
    checkForDetailsChanges( () -> action.proceed() );
  }

  protected void checkForDetailsChanges( Runnable action ) {
    if ( currentEditing != null &&  this.binder.hasChanges() ) {
      ConfirmDialog.createQuestion()
          .withCaption(getTranslation("element.global.unsavedChanged.title"))
          .withMessage(getTranslation("message.global.unsavedChanged"))
          .withOkButton(() ->action.run(), ButtonOption.focus(), ButtonOption.caption(getTranslation("action.global.yes")))
          .withCancelButton(ButtonOption.caption( getTranslation("action.global.no")))
          .open();
    } else {
      action.run();
    }
  }

  protected T getCurrentEditing() {
    return currentEditing;
  }

  protected void setCurrentEditing(T currentEditing ) { this.currentEditing = currentEditing; }

  protected void initHeader() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    appBar.setNaviMode(NaviMode.MENU);

    initSearchBar();

    if (canCreateRecord() && saveHandler != null) {
      newRecordButton = UIUtils
          .createTertiaryButton(VaadinIcon.PLUS);
      newRecordButton.addClickListener(event -> showDetails());
      appBar.addActionItem(newRecordButton);
    }
  }

  protected void showDetails() {
    try {
      showDetails(entityType.getDeclaredConstructor().newInstance());

    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
    }
  }

  protected boolean canCreateRecord() {
    return true;
  }
  protected boolean canSave() { return true; }

  protected boolean canDelete() { return true; }

  protected void initSearchBar() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    Button searchButton = UIUtils.createTertiaryButton(VaadinIcon.SEARCH);
    searchButton.addClickListener(event -> appBar.searchModeOn());
    appBar.addSearchListener(event -> filter((String) event.getValue()));
    appBar.setSearchPlaceholder(getTranslation("element.global.search"));
    appBar.addActionItem(searchButton);
  }

  public void enableCreateRecord() {
    if (newRecordButton != null) {
      newRecordButton.setVisible(true);
    }
  }

  public void disableCreateRecord() {
    if (newRecordButton != null) {
      newRecordButton.setVisible  (false);
    }
  }

  public void enableSaveButton() {
    detailsDrawerFooter.setSaveButtonVisible(true);
  }

  public void disableSaveButton() {
    detailsDrawerFooter.setSaveButtonVisible(false);
  }

  public void enableDeleteButton() {
    detailsDrawerFooter.setDeleteButtonVisible(true);
  }

  public void disableDeleteButton() {
    detailsDrawerFooter.setDeleteButtonVisible(false);
  }

  private Component createContent() {
    FlexBoxLayout content = new FlexBoxLayout(createGrid());
    content.setBoxSizing(BoxSizing.BORDER_BOX);
    content.setHeightFull();
    content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
    return content;
  }

  protected abstract Grid createGrid();

  protected DetailsDrawer.Position getDetailsDrawerPosition() {
    return DetailsDrawer.Position.RIGHT;
  }

  protected SplitViewFrame.Position getSplitViewFramePosition() {
    return SplitViewFrame.Position.RIGHT;
  }

  protected Tabs buildTabs() {
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

    return tabs;
  }

  private DetailsDrawer createDetailsDrawer() {
    detailsDrawer = new DetailsDrawer(getDetailsDrawerPosition());
    setViewDetailsPosition(getSplitViewFramePosition());
    // Header

    tabs = buildTabs();

    detailsDrawerHeader = new DetailsDrawerHeader(
        getTranslation("element." + I18N_PREFIX + "className"), tabs);
    detailsDrawerHeader.addCloseListener(e -> {
      checkForDetailsChanges( () ->  {
        detailsDrawer.hide();
        currentEditing = null;
      });
    });
    detailsDrawer.setHeader(detailsDrawerHeader);

    // Footer
    detailsDrawerFooter = new DetailsDrawerFooter();
    if (saveHandler == null || ! canSave()) {
      detailsDrawerFooter.setSaveButtonVisible(false);
      detailsDrawerFooter.setSaveAndNewButtonVisible(false);
    }
    if (deleteHandler == null|| ! canDelete()) {
      detailsDrawerFooter.setDeleteButtonVisible(false);
    }

    detailsDrawerFooter.addCancelListener(e -> {
      detailsDrawer.hide();
      currentEditing = null;
    });
    if (saveHandler != null && canSave()) {
      detailsDrawerFooter.addSaveListener(e -> save(false));
      detailsDrawerFooter.addSaveAndNewListener(e -> save(true));
    }
    if (deleteHandler != null && canDelete()) {
      detailsDrawerFooter.addDeleteListener(e -> delete());
    }
    detailsDrawer.setFooter(detailsDrawerFooter);

    return detailsDrawer;
  }

  protected void showDetails(T entity) {
    checkForDetailsChanges( () -> {
      if (entity.getId() != null)
        detailsDrawerFooter.setSaveAndNewButtonVisible(false);
      else {
        if (saveHandler != null && canSave()) {
          detailsDrawerFooter.setSaveAndNewButtonVisible(true);
        }
      }
      this.binder = new BeanValidationBinder<>(entityType);

      currentEditing = entity;
      detailsDrawer.setContent(createDetails(entity));
      detailsDrawer.show();
      tabs.setSelectedIndex(0);
    } );
  }

  protected abstract Component createDetails(T entity);

  protected Component createAudit(T entity) {
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

  protected boolean beforeSave(T entity) {
    return true;
  }

  protected void afterSave(T entity) {
  }

  protected boolean beforeDelete(T entity) {
    return true;
  }

  protected void afterDelete() {
  }

  private void save(boolean saveAndNew) {
    if (binder.writeBeanIfValid(currentEditing)) {
      boolean isNew = currentEditing.getId() == null;

      if (beforeSave(currentEditing)) {
        ServiceResult<T> result = saveHandler.apply(currentEditing);
        if ( result.getIsSuccess() && result.getData() != null )
          currentEditing = result.getData();
        else {
          WannagoMainView.get()
              .displayErrorMessage(getTranslation("message.global.unknownError", result.getMessage()));
          return;
        }
      }
      afterSave(currentEditing);

      WannagoMainView.get()
          .displayInfoMessage(getTranslation("message.global.recordSavedMessage"));

      if (!isNew) {
        dataProvider.refreshItem(currentEditing);
      } else {
        dataProvider.refreshAll();
      }

      if ( saveAndNew ) {
        showDetails();
        return;
      }

      showDetails(currentEditing);
    } else {
      BinderValidationStatus<T> validate = binder.validate();
      String errorText = validate.getFieldValidationStatuses()
          .stream().filter(BindingValidationStatus::isError)
          .map(BindingValidationStatus::getMessage)
          .map(Optional::get).distinct()
          .collect(Collectors.joining(", "));

      Notification
          .show(getTranslation("message.global.validationErrorMessage") + " : " + errorText, 3000,
              Notification.Position.BOTTOM_CENTER);
    }
  }

  public void delete() {
    ConfirmDialog.create()
        .withCaption(getTranslation("message.global.confirmDelete.title"))
        .withMessage(getTranslation("message.global.confirmDelete.message"))
        .withOkButton(() ->deleteConfirmed(), ButtonOption.focus(), ButtonOption.caption("YES"))
        .withCancelButton(ButtonOption.caption("NO"))
        .open();
  }

  private void deleteConfirmed() {
    if (beforeDelete(currentEditing)) {
      deleteHandler.accept(currentEditing);
    }

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

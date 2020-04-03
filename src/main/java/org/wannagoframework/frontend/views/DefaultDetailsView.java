package org.wannagoframework.frontend.views;

import ch.carnet.kasparscherrer.EmptyFormLayoutItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.RouterLayout;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawer;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawerFooter;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawerHeader;
import org.wannagoframework.frontend.components.navigation.bar.AppBar;
import org.wannagoframework.frontend.layout.ViewFrame;
import org.wannagoframework.frontend.layout.size.Horizontal;
import org.wannagoframework.frontend.layout.size.Top;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.css.BoxSizing;
import org.wannagoframework.frontend.utils.i18n.DateTimeFormatter;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 8/27/19
 */
public abstract class DefaultDetailsView<T extends BaseEntity> extends ViewFrame implements
    RouterLayout, HasLogger, HasUrlParameter<String> {

  protected final String I18N_PREFIX;
  protected DetailsDrawer detailsDrawer;
  protected DetailsDrawerHeader detailsDrawerHeader;
  protected DetailsDrawerFooter detailsDrawerFooter;
  protected Binder<T> binder;
  protected T currentEditing;
  private Class<T> entityType;
  private Function<T,ServiceResult<T>> saveHandler;
  private Consumer<T> deleteHandler;
  private Tabs tabs;
  private Class parentViewClassname;

  public DefaultDetailsView(String I18N_PREFIX, Class<T> entityType, Class parentViewClassname) {
    this.I18N_PREFIX = I18N_PREFIX;
    this.entityType = entityType;
    this.binder = new BeanValidationBinder<>(entityType);
    this.saveHandler = null;
    this.deleteHandler = null;
    this.parentViewClassname = parentViewClassname;
  }

  public DefaultDetailsView(String I18N_PREFIX, Class<T> entityType, Class parentViewClassname,
      Function<T, ServiceResult<T>> saveHandler, Consumer<T> deleteHandler) {
    this.I18N_PREFIX = I18N_PREFIX;
    this.entityType = entityType;
    this.binder = new BeanValidationBinder<>(entityType);
    this.saveHandler = saveHandler;
    this.deleteHandler = deleteHandler;
    this.parentViewClassname = parentViewClassname;
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    initHeader();

    setViewContent(createContent());
  }

  protected T getCurrentEditing() {
    return currentEditing;
  }

  protected void setCurrentEditing(T currentEditing ) { this.currentEditing = currentEditing; }

  protected void initHeader() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
    appBar.getContextIcon().addClickListener(event -> goBack());
    appBar.setTitle(getTitle(currentEditing));
  }

  protected abstract String getTitle(T entity);

  protected abstract Component createDetails(T entity);

  private Component createContent() {
    FlexBoxLayout content = new FlexBoxLayout(getContentTab());
    content.setBoxSizing(BoxSizing.BORDER_BOX);
    content.setHeightFull();
    content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
    return content;
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
  private Component getContentTab() {
    detailsDrawer = new DetailsDrawer();
    detailsDrawer.setWidthFull();
    // Header

    tabs = buildTabs();

    detailsDrawer
        .setContent(createDetails(currentEditing));

    detailsDrawerHeader = new DetailsDrawerHeader("", tabs, false, false);
    detailsDrawer.setHeader(detailsDrawerHeader);

    // Footer
    detailsDrawerFooter = new DetailsDrawerFooter();
    if (saveHandler == null || ! canSave() ) {
      detailsDrawerFooter.setSaveButtonVisible(false);
    }
    if (deleteHandler == null || ! canDelete()) {
      detailsDrawerFooter.setDeleteButtonVisible(false);
    }

    detailsDrawerFooter.addCancelListener(e -> {
      detailsDrawer.hide();
      currentEditing = null;
    });
    if (saveHandler != null && canSave()) {
      detailsDrawerFooter.addSaveListener(e -> save());
    }
    if (deleteHandler != null && canDelete()) {
      detailsDrawerFooter.addDeleteListener(e -> delete());
    }
    detailsDrawer.setFooter(detailsDrawerFooter);

    return detailsDrawer;
  }

  protected boolean canSave() { return true; }
  protected boolean canDelete() { return true; }

  protected void showDetails(T entity) {
    this.binder = new BeanValidationBinder<>(entityType);

    currentEditing = entity;
    detailsDrawer.setContent(createDetails(entity));
    tabs.setSelectedIndex(0);
  }

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

  private void save() {
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

      if ( isNew )
        UI.getCurrent()
            .navigate(getClass(), currentEditing.getId().toString());
      else
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
    ConfirmDialog dialog = new ConfirmDialog(getTranslation("message.global.confirmDelete.title"),
        getTranslation("message.global.confirmDelete.message"),
        getTranslation("action.global.deleteButton"), event -> deleteConfirmed(),
        getTranslation("action.global.cancelButton"), event -> {
    });
    dialog.setConfirmButtonTheme("error primary");

    dialog.setOpened(true);
  }

  private void deleteConfirmed() {
    if (beforeDelete(currentEditing)) {
      deleteHandler.accept(currentEditing);
    }

    afterDelete();

    goBack();
  }

  private void goBack() {
    UI.getCurrent().navigate(parentViewClassname);
  }
}

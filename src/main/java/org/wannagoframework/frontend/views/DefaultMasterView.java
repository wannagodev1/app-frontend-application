package org.wannagoframework.frontend.views;

import ch.carnet.kasparscherrer.EmptyFormLayoutItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import org.wannagoframework.frontend.layout.ViewFrame;
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
public abstract class DefaultMasterView<T extends BaseEntity, F extends DefaultFilter> extends ViewFrame {

  protected final String I18N_PREFIX;
  protected Grid<T> grid;
  protected DefaultDataProvider<T, F> dataProvider;
  private Class<T> entityType;
  private Tabs tabs;
  private Button newRecordButton;
  private Class entityViewClass;

  public DefaultMasterView(String I18N_PREFIX, Class<T> entityType,
      DefaultDataProvider<T, F> dataProvider, Class entityViewClass) {
    this.I18N_PREFIX = I18N_PREFIX;
    this.entityType = entityType;
    this.dataProvider = dataProvider;
    this.entityViewClass = entityViewClass;
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    initHeader();
    setViewContent(createContent());

    filter(null);
  }

  protected void initHeader() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    appBar.setNaviMode(NaviMode.MENU);

    initSearchBar();

      newRecordButton = UIUtils
          .createTertiaryButton(VaadinIcon.PLUS);
      newRecordButton.addClickListener(event -> {
        try {
          showDetails(entityType.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
      });
      appBar.addActionItem(newRecordButton);
  }

  protected void initSearchBar() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    Button searchButton = UIUtils.createTertiaryButton(VaadinIcon.SEARCH);
    searchButton.addClickListener(event -> appBar.searchModeOn());
    appBar.addSearchListener(event -> filter((String) event.getValue()));
    appBar.setSearchPlaceholder(getTranslation("element.global.search"));
    appBar.addActionItem(searchButton);
  }

  private Component createContent() {
    FlexBoxLayout content = new FlexBoxLayout(createGrid());
    content.setBoxSizing(BoxSizing.BORDER_BOX);
    content.setHeightFull();
    content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
    return content;
  }

  protected abstract Grid createGrid();

  protected void showDetails(T entity) {
      UI.getCurrent().navigate(entityViewClass, entity.getId() == null ? "-1" : entity.getId().toString());
  }

  protected void filter(String filter) {
    dataProvider
        .setFilter((F) new DefaultFilter(
            StringUtils.isBlank(filter) ? null : filter,
            Boolean.TRUE));
  }
}

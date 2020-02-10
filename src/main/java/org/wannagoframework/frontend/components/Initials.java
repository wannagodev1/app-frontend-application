package org.wannagoframework.frontend.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import org.wannagoframework.frontend.utils.FontSize;
import org.wannagoframework.frontend.utils.FontWeight;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.css.BorderRadius;

public class Initials extends FlexBoxLayout {

  private String CLASS_NAME = "initials";

  public Initials(String initials) {
    setAlignItems(FlexComponent.Alignment.CENTER);
    setBackgroundColor(LumoStyles.Color.Contrast._10);
    setBorderRadius(BorderRadius.L);
    setClassName(CLASS_NAME);
    UIUtils.setFontSize(FontSize.S, this);
    UIUtils.setFontWeight(FontWeight._600, this);
    setHeight(LumoStyles.Size.M);
    setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    setWidth(LumoStyles.Size.M);

    add(initials);
  }

}

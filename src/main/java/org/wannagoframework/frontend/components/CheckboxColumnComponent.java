package org.wannagoframework.frontend.components;

import com.vaadin.flow.component.checkbox.Checkbox;

/**
 * @author Alexandre Clavaud.
 * @version 1.0
 * @since 11/04/2020
 */
public class CheckboxColumnComponent extends Checkbox {

  public CheckboxColumnComponent(boolean initialValue) {
    super(initialValue);
    setReadOnly(false);
  }
}

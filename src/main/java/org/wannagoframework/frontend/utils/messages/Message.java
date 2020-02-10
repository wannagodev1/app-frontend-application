/*
 * This file is part of the WannaGo distribution (https://github.com/wannago).
 * Copyright (c) [2019] - [2020].
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


package org.wannagoframework.frontend.utils.messages;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-06
 */
public class Message {

  public static final String CONFIRM_CAPTION_DELETE = "Confirm Delete";
  public static final String CONFIRM_MESSAGE_DELETE = "Are you sure you want to delete the selected Item? This action cannot be undone.";
  public static final String BUTTON_CAPTION_DELETE = "Delete";
  public static final String BUTTON_CAPTION_CANCEL = "Cancel";

  public static final MessageSupplier UNSAVED_CHANGES = createMessage("Unsaved Changes", "Discard",
      "Continue Editing",
      "There are unsaved modifications to the %s. Discard changes?");

  public static final MessageSupplier CONFIRM_DELETE = createMessage(CONFIRM_CAPTION_DELETE,
      BUTTON_CAPTION_DELETE,
      BUTTON_CAPTION_CANCEL, CONFIRM_MESSAGE_DELETE);

  private final String caption;
  private final String okText;
  private final String cancelText;
  private final String message;

  public Message(String caption, String okText, String cancelText, String message) {
    this.caption = caption;
    this.okText = okText;
    this.cancelText = cancelText;
    this.message = message;
  }

  private static MessageSupplier createMessage(String caption, String okText, String cancelText,
      String message) {
    return (parameters) -> new Message(caption, okText, cancelText,
        String.format(message, parameters));
  }

  public String getCaption() {
    return caption;
  }

  public String getOkText() {
    return okText;
  }

  public String getCancelText() {
    return cancelText;
  }

  public String getMessage() {
    return message;
  }

  @FunctionalInterface
  public interface MessageSupplier {

    Message createMessage(Object... parameters);
  }

}

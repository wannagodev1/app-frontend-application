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


package org.wannagoframework.frontend.components.myOAuth2;

import static java.util.Objects.requireNonNull;

import com.github.scribejava.core.model.Response;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Tag("sign-in-2")
@JsModule("./myOAuth2/signin.js")
public abstract class MyOAuth2Signin extends PolymerTemplate<TemplateModel> {

  private final List<Consumer<String>> loginListeners = new ArrayList<>();
  private String accessToken;
  @Id("signin-button")
  private Button signinButton;

  protected MyOAuth2Signin(String authUrl, String redirectUri) {

    requireNonNull(authUrl);
    requireNonNull(redirectUri);

    addListener(AccessTokenReceivedEvent.class, e -> onAccessTokenReceived(e.getAccessToken()));

    getElement().setAttribute("auth-url", authUrl);
    getElement().setAttribute("redirect-uri", redirectUri);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    configureButton(signinButton);
  }

  protected abstract void configureButton(Button button);

  private void onAccessTokenReceived(String accessToken) {
    requireNonNull(accessToken);
    this.accessToken = accessToken;
    loginListeners.forEach(listener -> listener.accept(accessToken));
  }

  /**
   * An error-response was returned from the auth-provider. The default implementation will silently
   * return.
   *
   * @param response the response
   */
  protected void onResponseError(Response response) {
  }

  public String getAccessToken() {
    return accessToken;
  }

  public Registration addLoginListener(Consumer<String> listener) {
    requireNonNull(listener);

    loginListeners.add(listener);
    return () -> loginListeners.remove(listener);
  }
}

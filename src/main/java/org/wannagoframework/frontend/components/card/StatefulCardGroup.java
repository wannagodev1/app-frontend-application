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


package org.wannagoframework.frontend.components.card;

import com.github.appreciated.card.StatefulCard;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class StatefulCardGroup<T extends StatefulCard> extends Composite<VerticalLayout> {

  private StatefulCard currentFocus;
  private Consumer<T> listener;
  private List<T> cards = new ArrayList<>();

  @SafeVarargs
  public StatefulCardGroup(T... cards) {
    add(cards);
    getContent().setMargin(false);
    getContent().setPadding(false);
    getContent().setSpacing(false);
  }

  @SafeVarargs
  public final void add(T... cards) {
    getContent().add(cards);
    getContent().add(cards);
    Arrays.stream(cards).forEach(card -> card.addClickListener(event -> setState(card)));
    this.cards.addAll(Arrays.asList(cards));
  }

  public void removeAll() {
    getContent().removeAll();
    this.cards.clear();
  }

  public void setState(T nextFocus, boolean notifyListeners) {
    if (nextFocus != currentFocus) {
      nextFocus.setSelected(true);
      if (currentFocus != null) {
        currentFocus.setSelected(false);
      }
      currentFocus = nextFocus;
      if (listener != null && notifyListeners) {
        listener.accept(nextFocus);
      }
    }
  }

  public StatefulCard getState() {
    return currentFocus;
  }

  public void setState(T nextFocus) {
    setState(nextFocus, true);
  }

  public StatefulCardGroup<T> withStateChangedListener(Consumer<T> listener) {
    setStateChangedListener(listener);
    return this;
  }

  public void setStateChangedListener(Consumer<T> listener) {
    this.listener = listener;
  }

  public void setHighlight(boolean enabled) {
    if (enabled) {
      getContent().getStyle().remove("--card-state-highlight");
    } else {
      getContent().getStyle().set("--card-state-highlight", "transparent");
    }
  }

  public List<T> getCards() {
    return cards;
  }
}
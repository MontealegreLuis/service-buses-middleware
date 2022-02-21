package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.error;

import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebuses.ActionException;

final class CommandFailure extends ActionException {
  private final Action action;

  public CommandFailure(Action action, Throwable cause) {
    super("Cannot complete " + action.toWords() + ". " + cause.getMessage(), cause);
    this.action = action;
  }

  @Override
  public String code() {
    return String.format("%s-%s", action.toSlug(), super.code());
  }
}

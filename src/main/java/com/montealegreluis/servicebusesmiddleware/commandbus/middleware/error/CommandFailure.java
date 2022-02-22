package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.error;

import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebuses.ActionException;

final class CommandFailure extends ActionException {
  public CommandFailure(Action action, Throwable cause) {
    super("Cannot complete " + action.toWords() + ". " + cause.getMessage(), action, cause);
  }

  @Override
  public String code() {
    return String.format("%s-%s", action.toSlug(), super.code());
  }
}

package com.montealegreluis.servicebusesmiddleware.querybus.middleware.error;

import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebuses.ActionException;

final class QueryFailure extends ActionException {
  private final Action action;

  public QueryFailure(Action action, Throwable cause) {
    super("Cannot complete " + action.toWords() + ". " + cause.getMessage(), cause);
    this.action = action;
  }

  @Override
  public String code() {
    return String.format("%s-%s", action.toSlug(), super.code());
  }
}

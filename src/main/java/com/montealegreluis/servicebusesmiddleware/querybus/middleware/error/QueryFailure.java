package com.montealegreluis.servicebusesmiddleware.querybus.middleware.error;

import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebuses.ActionException;

final class QueryFailure extends ActionException {
  public QueryFailure(Action action, Throwable cause) {
    super("Cannot complete " + action.toWords() + ". " + cause.getMessage(), cause);
  }
}

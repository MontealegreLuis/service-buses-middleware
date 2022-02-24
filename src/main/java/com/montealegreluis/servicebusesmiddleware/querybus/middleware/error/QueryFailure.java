package com.montealegreluis.servicebusesmiddleware.querybus.middleware.error;

import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebuses.ActionException;

final class QueryFailure extends ActionException {
  public QueryFailure(Action action, Throwable cause) {
    super(cause.getMessage() == null ? "No message provided" : cause.getMessage(), action, cause);
  }

  @Override
  public String code() {
    return String.format("%s-%s", action.toSlug(), super.code());
  }
}

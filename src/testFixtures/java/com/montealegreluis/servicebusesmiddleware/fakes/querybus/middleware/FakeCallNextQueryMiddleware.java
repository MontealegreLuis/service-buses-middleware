package com.montealegreluis.servicebusesmiddleware.fakes.querybus.middleware;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.QueryMiddleware;

public final class FakeCallNextQueryMiddleware implements QueryMiddleware {
  private boolean executed = false;

  @Override
  public Response execute(Query query, QueryHandler<Query, Response> next) throws ActionException {
    executed = true;
    return next.execute(query);
  }

  public boolean wasExecuted() {
    return executed;
  }
}

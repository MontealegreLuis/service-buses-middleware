package com.montealegreluis.servicebusesmiddleware.fakes.querybus.middleware;

import com.montealegreluis.servicebuses.fakes.querybus.FakeResponse;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.QueryMiddleware;

public final class FakeQueryMiddleware implements QueryMiddleware {
  private boolean executed = false;

  @Override
  public Response execute(Query query, QueryHandler<Query, Response> next) {
    executed = true;
    return new FakeResponse();
  }

  public boolean wasExecuted() {
    return executed;
  }
}

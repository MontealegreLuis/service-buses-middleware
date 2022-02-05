package com.montealegreluis.servicebusesmiddleware.querybus;

import com.montealegreluis.assertions.Assert;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryBus;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.QueryMiddleware;
import java.util.ArrayList;
import java.util.List;

public final class MiddlewareQueryBus implements QueryBus, QueryHandler<Query, Response> {
  private final List<QueryMiddleware> middleware;
  private List<QueryMiddleware> runtimeMiddleware;

  public MiddlewareQueryBus(List<QueryMiddleware> middleware) {
    Assert.notEmpty(middleware, "Cannot dispatch queries on an empty query bus");
    this.middleware = middleware;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R extends Response> R dispatch(Query query) throws ActionException {
    runtimeMiddleware = new ArrayList<>(middleware);
    return (R) execute(query);
  }

  @Override
  public Response execute(Query query) throws ActionException {
    QueryMiddleware middleware = runtimeMiddleware.remove(0);
    return middleware.execute(query, this);
  }
}

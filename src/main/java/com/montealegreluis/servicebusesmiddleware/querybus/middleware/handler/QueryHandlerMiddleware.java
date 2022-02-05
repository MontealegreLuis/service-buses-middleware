package com.montealegreluis.servicebusesmiddleware.querybus.middleware.handler;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebuses.querybus.factory.QueryHandlerFactory;
import com.montealegreluis.servicebuses.querybus.locator.QueryHandlerLocator;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.QueryMiddleware;

public final class QueryHandlerMiddleware implements QueryMiddleware {
  private final QueryHandlerLocator locator;
  private final QueryHandlerFactory factory;

  public QueryHandlerMiddleware(QueryHandlerLocator locator, QueryHandlerFactory factory) {
    this.locator = locator;
    this.factory = factory;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Response execute(Query query, QueryHandler<Query, Response> next) throws ActionException {
    final var queryHandlerName = locator.search(query.getClass());
    final var handler = (QueryHandler<Query, Response>) factory.queryFromName(queryHandlerName);

    return handler.execute(query);
  }
}

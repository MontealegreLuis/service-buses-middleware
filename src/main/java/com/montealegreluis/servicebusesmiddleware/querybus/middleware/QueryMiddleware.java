package com.montealegreluis.servicebusesmiddleware.querybus.middleware;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;

public interface QueryMiddleware {
  Response execute(Query query, QueryHandler<Query, Response> next) throws ActionException;
}

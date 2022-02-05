package com.montealegreluis.servicebusesmiddleware.querybus.middleware.error;

import static com.montealegreluis.servicebusesmiddleware.ActionErrorActivity.domainException;
import static com.montealegreluis.servicebusesmiddleware.ActionErrorActivity.queryFailure;

import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.DomainException;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.QueryMiddleware;
import io.vavr.control.Try;

public final class QueryErrorHandlerMiddleware implements QueryMiddleware {
  private final ActivityFeed feed;
  private final ContextSerializer serializer;

  public QueryErrorHandlerMiddleware(ActivityFeed feed, ContextSerializer serializer) {
    this.feed = feed;
    this.serializer = serializer;
  }

  @Override
  public Response execute(Query query, QueryHandler<Query, Response> next) throws ActionException {
    return Try.of(() -> next.execute(query))
        .onFailure((e) -> logException(query, e))
        .getOrElseThrow((e) -> rethrowException(query.action(), e));
  }

  private void logException(Query query, Throwable exception) {
    if (exception instanceof DomainException) {
      feed.record(domainException(query, (DomainException) exception, serializer));
    } else {
      feed.record(queryFailure(query, exception, serializer));
    }
  }

  private ActionException rethrowException(Action action, Throwable cause) {
    return cause instanceof DomainException
        ? (DomainException) cause
        : new QueryFailure(action, cause);
  }
}

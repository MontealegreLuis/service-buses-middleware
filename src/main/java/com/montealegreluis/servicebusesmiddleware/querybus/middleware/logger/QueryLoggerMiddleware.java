package com.montealegreluis.servicebusesmiddleware.querybus.middleware.logger;

import static com.montealegreluis.servicebusesmiddleware.ActionActivity.queryCompleted;

import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.QueryMiddleware;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public final class QueryLoggerMiddleware implements QueryMiddleware {
  private final ActivityFeed feed;
  private final Clock clock;

  public QueryLoggerMiddleware(ActivityFeed feed, Clock clock) {
    this.feed = feed;
    this.clock = clock;
  }

  @Override
  public Response execute(Query query, QueryHandler<Query, Response> next) throws ActionException {
    Instant start = clock.instant();
    Response response = next.execute(query);
    Instant end = clock.instant();

    feed.record(queryCompleted(query.action(), Duration.between(start, end)));

    return response;
  }
}

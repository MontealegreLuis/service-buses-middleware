package com.montealegreluis.servicebusesmiddleware.querybus.middleware.logger;

import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.fakes.querybus.FakeQuery;
import com.montealegreluis.servicebuses.fakes.querybus.FakeResponse;
import com.montealegreluis.servicebuses.fakes.querybus.SpyQueryHandler;
import com.montealegreluis.servicebusesmiddleware.fakes.commandbus.middleware.logger.FixedOffsetInstantClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static com.montealegreluis.servicebusesmiddleware.ActionActivity.queryCompleted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

final class QueryLoggerMiddlewareTest {
  @Test
  void it_logs_the_duration_of_a_query() throws ActionException {
    var query = new FakeQuery();
    var next = new SpyQueryHandler();
    var activity = queryCompleted(query.action(), duration);

    var response = middleware.execute(query, next);

    verify(feed, times(1)).record(activity);
    assertTrue(next.wasCalled());
    assertEquals(new FakeResponse(), response);
  }

  @BeforeEach
  void let() {
    feed = mock(ActivityFeed.class);
    duration = Duration.ofMillis(500);
    var clock = new FixedOffsetInstantClock(Instant.now(), ZoneOffset.UTC, duration);
    middleware = new QueryLoggerMiddleware(feed, clock);
  }

  private ActivityFeed feed;
  private Duration duration;
  private QueryLoggerMiddleware middleware;
}

package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.logger;

import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommand;
import com.montealegreluis.servicebuses.fakes.commandbus.SpyCommandHandler;
import com.montealegreluis.servicebusesmiddleware.fakes.commandbus.middleware.logger.FixedOffsetInstantClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static com.montealegreluis.servicebusesmiddleware.ActionActivity.commandCompleted;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

final class CommandLoggerMiddlewareTest {
  @Test
  void it_logs_the_duration_of_a_command() throws ActionException {
    var command = new FakeCommand();
    var next = new SpyCommandHandler();
    var activity = commandCompleted(command.action(), duration);

    middleware.execute(command, next);

    verify(feed, times(1)).record(activity);
    assertTrue(next.wasCalled());
  }

  @BeforeEach
  void let() {
    feed = mock(ActivityFeed.class);
    duration = Duration.ofMillis(500);
    var clock = new FixedOffsetInstantClock(Instant.now(), ZoneOffset.UTC, duration);
    middleware = new CommandLoggerMiddleware(feed, clock);
  }

  private ActivityFeed feed;
  private Duration duration;
  private CommandLoggerMiddleware middleware;
}

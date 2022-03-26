package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events;

import static com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events.EventsLoggerActivity.domainEventRecorded;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.domainevents.DomainEventsCollector;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommand;
import com.montealegreluis.servicebuses.fakes.commandbus.SpyCommandHandler;
import com.montealegreluis.servicebuses.fakes.domainevents.FakeDomainEvent;
import org.junit.jupiter.api.Test;

final class EventsLoggerMiddlewareTest {
  @Test
  void it_logs_all_collected_events() throws ActionException {
    var collector = new DomainEventsCollector();
    var event = new FakeDomainEvent();
    collector.collect(event);
    collector.collect(new FakeDomainEvent());
    collector.collect(new FakeDomainEvent());
    var serializer = new ContextSerializer(new ObjectMapper());
    var activity = domainEventRecorded(event, serializer);
    var feed = mock(ActivityFeed.class);
    var middleware = new EventsLoggerMiddleware(collector, feed, serializer);
    var handler = new SpyCommandHandler();
    var command = new FakeCommand();

    middleware.execute(command, handler);

    assertTrue(handler.wasCalled());
    verify(feed, times(3)).record(activity);
  }
}

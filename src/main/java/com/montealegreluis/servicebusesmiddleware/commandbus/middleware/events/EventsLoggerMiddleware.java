package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events;

import static com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events.EventsLoggerActivity.domainEventRecorded;

import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebuses.domainevents.DomainEvent;
import com.montealegreluis.servicebuses.domainevents.DomainEventsCollector;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.CommandMiddleware;

public final class EventsLoggerMiddleware implements CommandMiddleware {
  private final DomainEventsCollector collector;
  private final ActivityFeed feed;
  private final ContextSerializer serializer;

  public EventsLoggerMiddleware(
      DomainEventsCollector collector, ActivityFeed feed, ContextSerializer serializer) {
    this.collector = collector;
    this.feed = feed;
    this.serializer = serializer;
  }

  @Override
  public void execute(Command command, CommandHandler<Command> next) throws ActionException {
    next.execute(command);

    Iterable<DomainEvent> events = collector.events().all();

    events.forEach(event -> feed.record(domainEventRecorded(event, serializer)));
  }
}

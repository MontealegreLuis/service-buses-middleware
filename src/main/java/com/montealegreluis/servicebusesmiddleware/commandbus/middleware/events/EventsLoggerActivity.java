package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events;

import static com.montealegreluis.activityfeed.Activity.info;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.domainevents.DomainEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EventsLoggerActivity {
  public static Activity domainEventRecorded(DomainEvent event, ContextSerializer serializer) {
    return info(
        "domain-event",
        event.action().toSentence(),
        (context) -> {
          context.put("name", event.name());
          context.put("aggregateId", event.aggregateId());
          context.put("event", serializer.toContextMap(event));
        });
  }
}

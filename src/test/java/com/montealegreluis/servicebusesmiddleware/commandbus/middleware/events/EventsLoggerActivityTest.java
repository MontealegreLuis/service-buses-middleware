package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events;

import static com.montealegreluis.activityfeed.ContextAssertions.assertContextSize;
import static com.montealegreluis.activityfeed.ContextAssertions.assertContextValueEquals;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.fakes.domainevents.FakeDomainEvent;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import org.junit.jupiter.api.Test;

final class EventsLoggerActivityTest {
  @Test
  void it_records_a_domain_event() {
    var aggregateId = "cd8b09ec-ac62-4e5d-a6cd-b328456fc419";
    var event =
        new FakeDomainEvent(aggregateId, Date.from(Instant.parse("2022-03-25T18:35:24.00Z")));
    var serializer = new ContextSerializer(new ObjectMapper());

    var activity = EventsLoggerActivity.domainEventRecorded(event, serializer);

    assertEquals("Fake domain event", activity.message());
    assertEquals(Level.INFO, activity.level());
    @SuppressWarnings("unchecked")
    Map<String, Object> context = (Map<String, Object>) (activity.context().get("context"));
    assertContextSize(4, context);
    assertContextValueEquals("domain-event", "identifier", context);
    assertContextValueEquals("context.aggregate.eventHasOccurred", "name", context);
    assertContextValueEquals(aggregateId, "aggregateId", context);
    @SuppressWarnings("unchecked")
    var eventMap = (Map<String, Object>) (context.get("event"));
    assertContextValueEquals("context.aggregate.eventHasOccurred", "name", eventMap);
    assertContextValueEquals(1648233324000L, "occurredOn", eventMap);
    assertContextValueEquals(aggregateId, "aggregateId", eventMap);
  }
}

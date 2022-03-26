package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events;

import static org.junit.jupiter.api.Assertions.*;

import com.montealegreluis.servicebuses.builders.Random;
import com.montealegreluis.servicebuses.domainevents.DomainEvent;
import com.montealegreluis.servicebuses.domainevents.DomainEventsCollector;
import com.montealegreluis.servicebuses.fakes.domainevents.FakeDomainEvent;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class DomainEventsCollectorTest {
  @Test
  void it_collects_all_events() {
    collector.collect(new FakeDomainEvent());
    collector.collect(new FakeDomainEvent());
    collector.collect(new FakeDomainEvent());

    var events = collector.events();

    assertEquals(
        3, ((ArrayList<DomainEvent>) events.all()).size(), "There should be 3 collected events");
  }

  @Test
  void it_collects_all_types_of_domain_events() {
    boolean acceptsFakeEvent = collector.accepts(new FakeDomainEvent());
    boolean acceptsAnonymousDomainEvent =
        collector.accepts(
            new DomainEvent() {
              @Override
              public String name() {
                return "context.aggregate.eventHasOccurred";
              }

              @Override
              public String aggregateId() {
                return Random.uuid();
              }
            });

    assertTrue(acceptsFakeEvent, "Event should have been accepted");
    assertTrue(acceptsAnonymousDomainEvent, "Event should have been accepted");
  }

  @Test
  void it_resets_events_after_collecting_them() {
    collector.collect(new FakeDomainEvent());
    collector.collect(new FakeDomainEvent());
    collector.events(); // collect events

    var noEvents = collector.events().all(); // collect them a second time

    assertTrue(((ArrayList<DomainEvent>) noEvents).isEmpty());
  }

  @BeforeEach
  void let() {
    collector = new DomainEventsCollector();
  }

  private DomainEventsCollector collector;
}

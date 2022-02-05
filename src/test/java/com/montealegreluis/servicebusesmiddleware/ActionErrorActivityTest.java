package com.montealegreluis.servicebusesmiddleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.ActionErrorActivity;
import com.montealegreluis.servicebuses.DomainException;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommand;
import com.montealegreluis.servicebuses.fakes.querybus.FakeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.logging.Level;

import static com.montealegreluis.activityfeed.ContextAssertions.assertContextHasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ActionErrorActivityTest {
  @Test
  void it_creates_a_domain_exception_activity() {
    var activity =
        com.montealegreluis.servicebuses.ActionErrorActivity.domainException(
            new FakeCommand(), new DomainException("Action cannot be completed") {}, serializer);

    assertEquals("Cannot fake command. Action cannot be completed", activity.message());
    assertEquals(Level.WARNING, activity.level());
    @SuppressWarnings("unchecked")
    Map<String, Object> context = (Map<String, Object>) (activity.context().get("context"));
    assertContextHasKey("input", context);
    assertContextHasKey("exception", context);
  }

  @Test
  void it_creates_a_command_infrastructure_exception_activity() {
    var activity =
        ActionErrorActivity.commandFailure(
            new FakeCommand(), new RuntimeException("Action cannot be completed") {}, serializer);

    assertEquals("Cannot fake command. Action cannot be completed", activity.message());
    assertEquals(Level.SEVERE, activity.level());
    @SuppressWarnings("unchecked")
    Map<String, Object> context = (Map<String, Object>) (activity.context().get("context"));
    assertContextHasKey("input", context);
    assertContextHasKey("exception", context);
  }

  @Test
  void it_creates_a_query_infrastructure_exception_activity() {
    var activity =
        ActionErrorActivity.queryFailure(
            new FakeQuery(), new RuntimeException("Action cannot be completed") {}, serializer);

    assertEquals("Cannot fake query. Action cannot be completed", activity.message());
    assertEquals(Level.SEVERE, activity.level());
    @SuppressWarnings("unchecked")
    Map<String, Object> context = (Map<String, Object>) (activity.context().get("context"));
    assertContextHasKey("input", context);
    assertContextHasKey("exception", context);
  }

  @BeforeEach
  void let() {
    serializer = new ContextSerializer(new ObjectMapper());
  }

  private ContextSerializer serializer;
}

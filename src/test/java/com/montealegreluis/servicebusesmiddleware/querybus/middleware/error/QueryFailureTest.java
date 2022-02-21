package com.montealegreluis.servicebusesmiddleware.querybus.middleware.error;

import static org.junit.jupiter.api.Assertions.*;

import com.montealegreluis.servicebuses.Action;
import org.junit.jupiter.api.Test;

final class QueryFailureTest {
  @Test
  void it_generates_its_code_from_an_action() {
    var action = Action.withoutSuffix("SearchUpcomingConcerts", "");

    var failure = new QueryFailure(action, new RuntimeException("Cannot find concerts"));

    assertEquals("search-upcoming-concerts-query-failure", failure.code());
  }
}

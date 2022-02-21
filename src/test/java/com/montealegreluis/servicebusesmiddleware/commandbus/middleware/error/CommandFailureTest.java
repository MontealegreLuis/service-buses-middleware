package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.error;

import static org.junit.jupiter.api.Assertions.*;

import com.montealegreluis.servicebuses.Action;
import org.junit.jupiter.api.Test;

final class CommandFailureTest {
  @Test
  void it_generates_its_code_from_an_action() {
    var action = Action.withoutSuffix("PublishUpcomingConcert", "");

    var failure = new CommandFailure(action, new RuntimeException("Cannot publish concert"));

    assertEquals("publish-upcoming-concert-command-failure", failure.code());
  }
}

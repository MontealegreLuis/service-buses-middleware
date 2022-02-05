package com.montealegreluis.servicebusesmiddleware.commandbus;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.MiddlewareCommandBus;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommand;
import com.montealegreluis.servicebusesmiddleware.fakes.commandbus.FakeCommandMiddleware;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class MiddlewareCommandBusTest {
  @Test
  void it_prevents_dispatching_a_command_without_middleware() {
    assertThrows(
        IllegalArgumentException.class, () -> new com.montealegreluis.servicebuses.commandbus.MiddlewareCommandBus(Collections.emptyList()));
  }

  @Test
  void it_dispatches_a_single_middleware() throws ActionException {
    var middleware = new FakeCommandMiddleware();
    var bus = new com.montealegreluis.servicebuses.commandbus.MiddlewareCommandBus(Collections.singletonList(middleware));

    bus.dispatch(new FakeCommand());

    assertTrue(middleware.hasBeenExecuted(), "Middleware has not been executed");
  }

  @Test
  void it_dispatches_multiple_middleware() throws ActionException {
    var middlewareA = new FakeCommandMiddleware();
    var middlewareB = new FakeCommandMiddleware();
    var middlewareC = new FakeCommandMiddleware();
    var bus = new MiddlewareCommandBus(List.of(middlewareA, middlewareB, middlewareC));

    bus.dispatch(new FakeCommand());

    assertTrue(middlewareA.hasBeenExecuted(), "Middleware A has not been executed");
    assertTrue(middlewareB.hasBeenExecuted(), "Middleware B has not been executed");
    assertTrue(middlewareC.hasBeenExecuted(), "Middleware C has not been executed");
  }
}

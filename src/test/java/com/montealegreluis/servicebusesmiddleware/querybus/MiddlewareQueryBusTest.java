package com.montealegreluis.servicebusesmiddleware.querybus;

import com.montealegreluis.assertions.IllegalArgumentException;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.fakes.querybus.FakeQuery;
import com.montealegreluis.servicebuses.fakes.querybus.FakeResponse;
import com.montealegreluis.servicebusesmiddleware.fakes.querybus.middleware.FakeCallNextQueryMiddleware;
import com.montealegreluis.servicebusesmiddleware.fakes.querybus.middleware.FakeQueryMiddleware;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class MiddlewareQueryBusTest {
  @Test
  void it_prevents_dispatching_queries_without_middleware() {
    assertThrows(
        IllegalArgumentException.class, () -> new MiddlewareQueryBus(Collections.emptyList()));
  }

  @Test
  void it_dispatches_a_single_middleware() throws ActionException {
    var middleware = new FakeQueryMiddleware();
    var bus = new MiddlewareQueryBus(Collections.singletonList(middleware));

    var response = bus.dispatch(new FakeQuery());

    assertEquals(new FakeResponse(), response);
    assertTrue(middleware.wasExecuted(), "Middleware has not been executed");
  }

  @Test
  void it_dispatches_multiple_middleware() throws ActionException {
    var middlewareA = new FakeCallNextQueryMiddleware();
    var middlewareB = new FakeCallNextQueryMiddleware();
    var middlewareC = new FakeQueryMiddleware();
    var bus = new MiddlewareQueryBus(List.of(middlewareA, middlewareB, middlewareC));

    var response = bus.dispatch(new FakeQuery());

    assertEquals(new FakeResponse(), response);
    assertTrue(middlewareA.wasExecuted(), "Middleware A was not executed");
    assertTrue(middlewareB.wasExecuted(), "Middleware B was not executed");
    assertTrue(middlewareC.wasExecuted(), "Middleware C was not executed");
  }
}

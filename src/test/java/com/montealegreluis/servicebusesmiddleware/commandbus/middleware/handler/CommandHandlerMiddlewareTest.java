package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.factory.InMemoryCommandHandlerFactory;
import com.montealegreluis.servicebuses.commandbus.locator.ReflectionsCommandHandlerLocator;
import com.montealegreluis.servicebuses.commandbus.locator.UnknownCommandHandler;
import com.montealegreluis.servicebuses.fakes.commandbus.CommandWithoutHandler;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommand;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommandHandler;
import com.montealegreluis.servicebuses.fakes.commandbus.SpyCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class CommandHandlerMiddlewareTest {
  @Test
  void it_locates_and_executes_a_command_handler() throws ActionException {
    var command = new FakeCommand();
    var handler = new FakeCommandHandler();
    factory.add(handler.getClass(), handler);
    var next = new SpyCommandHandler();

    middleware.execute(command, next);

    assertEquals(command, handler.executedCommand(), "Command was not executed");
  }

  @Test
  void it_fails_when_no_handler_is_registered_for_a_command() {
    var command = new CommandWithoutHandler();
    var handler = new SpyCommandHandler();

    assertThrows(UnknownCommandHandler.class, () -> middleware.execute(command, handler));
  }

  @BeforeEach
  void let() {
    factory = new InMemoryCommandHandlerFactory();
    var locator =
        new ReflectionsCommandHandlerLocator("com.montealegreluis.servicebuses.fakes.commandbus");
    middleware = new CommandHandlerMiddleware(locator, factory);
  }

  private CommandHandlerMiddleware middleware;
  private InMemoryCommandHandlerFactory factory;
}

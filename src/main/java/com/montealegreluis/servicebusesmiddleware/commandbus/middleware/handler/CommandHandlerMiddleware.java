package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.handler;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebuses.commandbus.factory.CommandHandlerFactory;
import com.montealegreluis.servicebuses.commandbus.locator.CommandHandlerLocator;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.CommandMiddleware;

public final class CommandHandlerMiddleware implements CommandMiddleware {
  private final CommandHandlerLocator locator;
  private final CommandHandlerFactory factory;

  public CommandHandlerMiddleware(CommandHandlerLocator locator, CommandHandlerFactory factory) {
    this.locator = locator;
    this.factory = factory;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void execute(Command command, CommandHandler<Command> next) throws ActionException {
    final var commandName = locator.search(command.getClass());
    final var handler = (CommandHandler<Command>) factory.commandFromName(commandName);

    handler.execute(command);
  }
}

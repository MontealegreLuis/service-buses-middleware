package com.montealegreluis.servicebusesmiddleware.fakes.commandbus;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebuses.commandbus.middleware.CommandMiddleware;

public final class FakeCommandMiddleware implements CommandMiddleware {
  private boolean executed = false;

  @Override
  public void execute(Command command, CommandHandler<Command> next) throws ActionException {
    executed = true;
    next.execute(command);
  }

  public boolean hasBeenExecuted() {
    return executed;
  }
}

package com.montealegreluis.servicebusesmiddleware.commandbus;

import com.montealegreluis.assertions.Assert;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandBus;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.CommandMiddleware;

import java.util.ArrayList;
import java.util.List;

public final class MiddlewareCommandBus implements CommandBus, CommandHandler<Command> {
  public final List<CommandMiddleware> middleware;
  private List<CommandMiddleware> runtimeMiddleware;

  public MiddlewareCommandBus(List<CommandMiddleware> middleware) {
    Assert.notEmpty(middleware, "Cannot dispatch commands on an empty command bus");
    this.middleware = middleware;
  }

  @Override
  public void dispatch(Command command) throws ActionException {
    runtimeMiddleware = new ArrayList<>(middleware);
    execute(command);
  }

  @Override
  public void execute(Command command) throws ActionException {
    if (runtimeMiddleware.isEmpty()) return;

    CommandMiddleware middleware = runtimeMiddleware.remove(0);
    middleware.execute(command, this);
  }
}

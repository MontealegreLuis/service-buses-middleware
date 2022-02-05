package com.montealegreluis.servicebusesmiddleware.commandbus.middleware;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;

public interface CommandMiddleware {
  void execute(Command command, CommandHandler<Command> next) throws ActionException;
}

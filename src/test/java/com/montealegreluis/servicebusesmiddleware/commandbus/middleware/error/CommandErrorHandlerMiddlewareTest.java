package com.montealegreluis.servicebusesmiddleware.commandbus.middleware.error;

import static com.montealegreluis.servicebusesmiddleware.ActionErrorActivity.commandFailure;
import static com.montealegreluis.servicebusesmiddleware.ActionErrorActivity.domainException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.DomainException;
import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class CommandErrorHandlerMiddlewareTest {
  @Test
  void it_logs_a_domain_exception() {
    var input = new FakeCommand();
    var exception =
        new DomainException("Action cannot be completed", new Exception("Cause exception")) {};
    var activity = domainException(input, exception, serializer);
    var next =
        new CommandHandler<>() {
          @Override
          public void execute(Command command) throws DomainException {
            throw exception;
          }
        };

    var rethrownException =
        assertThrows(DomainException.class, () -> middleware.execute(input, next));

    assertEquals("Action cannot be completed", rethrownException.getMessage());
    verify(feed, times(1)).record(activity);
  }

  @Test
  void it_logs_an_infrastructure_exception() {
    var input = new FakeCommand();
    var exception = new RuntimeException("Action cannot be completed") {};
    var activity = commandFailure(input, exception, serializer);
    var next =
        new CommandHandler<>() {
          @Override
          public void execute(Command command) {
            throw exception;
          }
        };

    var rethrownException =
        assertThrows(CommandFailure.class, () -> middleware.execute(input, next));

    assertEquals(
        "Cannot complete fake command. Action cannot be completed", rethrownException.getMessage());
    verify(feed, times(1)).record(activity);
  }

  @Test
  void it_does_nothing_if_no_exception_is_thrown() throws ActionException {
    var input = new FakeCommand();
    var next =
        new CommandHandler<>() {
          @Override
          public void execute(Command command) {}
        };

    middleware.execute(input, next);

    verify(feed, never()).record(any());
  }

  @BeforeEach
  void let() {
    feed = mock(ActivityFeed.class);
    serializer = new ContextSerializer(new ObjectMapper());
    middleware = new CommandErrorHandlerMiddleware(feed, serializer);
  }

  private ActivityFeed feed;
  private ContextSerializer serializer;
  private CommandErrorHandlerMiddleware middleware;
}

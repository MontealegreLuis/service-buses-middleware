package com.montealegreluis.servicebusesmiddleware.querybus.middleware.error;

import static com.montealegreluis.servicebusesmiddleware.ActionErrorActivity.domainException;
import static com.montealegreluis.servicebusesmiddleware.ActionErrorActivity.queryFailure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.DomainException;
import com.montealegreluis.servicebuses.fakes.querybus.FakeQuery;
import com.montealegreluis.servicebuses.fakes.querybus.FakeResponse;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class QueryErrorHandlerMiddlewareTest {
  @Test
  void it_logs_a_domain_exception() {
    var input = new FakeQuery();
    var exception =
        new DomainException("Action cannot be completed", new Exception("Cause exception")) {};
    var activity = domainException(input, exception, serializer);
    var next =
        new QueryHandler<>() {
          @Override
          public Response execute(Query query) throws DomainException {
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
    var input = new FakeQuery();
    var exception = new RuntimeException("Action cannot be completed") {};
    var activity = queryFailure(input, exception, serializer);
    var next =
        new QueryHandler<>() {
          @Override
          public Response execute(Query query) {
            throw exception;
          }
        };

    var rethrownException = assertThrows(QueryFailure.class, () -> middleware.execute(input, next));

    assertEquals(
        "Cannot complete fake query. Action cannot be completed", rethrownException.getMessage());
    verify(feed, times(1)).record(activity);
  }

  @Test
  void it_does_nothing_if_no_exception_is_thrown() throws ActionException {
    var input = new FakeQuery();
    var expectedResponse = new FakeResponse();
    var next =
        new QueryHandler<>() {
          @Override
          public Response execute(Query query) {
            return expectedResponse;
          }
        };

    var response = middleware.execute(input, next);

    assertEquals(expectedResponse, response);
    verify(feed, never()).record(any());
  }

  @BeforeEach
  void let() {
    feed = mock(ActivityFeed.class);
    serializer = new ContextSerializer(new ObjectMapper());
    middleware = new QueryErrorHandlerMiddleware(feed, serializer);
  }

  private ActivityFeed feed;
  private ContextSerializer serializer;
  private QueryErrorHandlerMiddleware middleware;
}

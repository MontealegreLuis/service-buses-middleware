package com.montealegreluis.servicebusesmiddleware;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebuses.DomainException;
import com.montealegreluis.servicebuses.Input;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.montealegreluis.activityfeed.Activity.error;
import static com.montealegreluis.activityfeed.Activity.warning;
import static com.montealegreluis.activityfeed.ExceptionContextFactory.contextFrom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionErrorActivity {
  public static Activity domainException(
      Input input, DomainException exception, ContextSerializer serializer) {
    Action action = input.action();
    String message = "Cannot " + action.toWords() + ". " + exception.getMessage();
    String identifier = action.toSlug() + "-" + exception.code();

    return warning(
        identifier,
        message,
        (context) -> {
          context.put("exception", contextFrom(exception));
          context.put("input", serializer.toContextMap(input));
        });
  }

  public static Activity commandFailure(
      Input input, Throwable exception, ContextSerializer serializer) {
    return createActivity(input, exception, serializer, "command");
  }

  private static Activity createActivity(
      Input input, Throwable exception, ContextSerializer serializer, String actionType) {
    Action action = input.action();
    String message = "Cannot " + action.toWords() + ". " + exception.getMessage();
    String identifier = action.toSlug() + "-" + actionType + "-failure";

    return error(
        identifier,
        message,
        (context) -> {
          context.put("exception", contextFrom(exception));
          context.put("input", serializer.toContextMap(input));
        });
  }
}

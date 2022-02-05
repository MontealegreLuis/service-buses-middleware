package com.montealegreluis.servicebusesmiddleware;

import static com.montealegreluis.activityfeed.Activity.info;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.servicebuses.Action;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionActivity {
  public static Activity commandCompleted(Action command, Duration duration) {
    return actionCompleted(command, duration, "command");
  }

  public static Activity queryCompleted(Action query, Duration duration) {
    return actionCompleted(query, duration, "query");
  }

  private static Activity actionCompleted(Action action, Duration duration, String identifier) {
    long durationInMilliseconds = duration.toMillis();
    String message =
        String.format(
            "%s has been completed in %d milliseconds",
            action.toSentence(), durationInMilliseconds);
    return info(
        identifier,
        message,
        (context) -> {
          context.put(identifier, action.toSlug());
          context.put("durationInMilliseconds", durationInMilliseconds);
        });
  }
}

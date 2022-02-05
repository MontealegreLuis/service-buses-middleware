package com.montealegreluis.servicebusesmiddleware;

import com.montealegreluis.servicebuses.Action;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Level;

import static com.montealegreluis.activityfeed.ContextAssertions.assertContextSize;
import static com.montealegreluis.activityfeed.ContextAssertions.assertContextValueEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ActionActivityTest {
  @Test
  void it_records_the_duration_of_a_command() {
    var action = Action.withoutSuffix("UpdateCreditCardInformationAction", "Action");
    var duration = Duration.ofMillis(500);

    var activity = ActionActivity.commandCompleted(action, duration);

    assertEquals(
        "Update credit card information has been completed in 500 milliseconds",
        activity.message());
    assertEquals(Level.INFO, activity.level());
    @SuppressWarnings("unchecked")
    Map<String, Object> context = (Map<String, Object>) (activity.context().get("context"));
    assertContextSize(3, context);
    assertContextValueEquals("command", "identifier", context);
    assertContextValueEquals("update-credit-card-information", "command", context);
    assertContextValueEquals(500L, "durationInMilliseconds", context);
  }

  @Test
  void it_records_the_duration_of_a_query() {
    var action = Action.withoutSuffix("SearchProductsAction", "Action");
    var duration = Duration.ofMillis(500);

    var activity = com.montealegreluis.servicebuses.ActionActivity.queryCompleted(action, duration);

    assertEquals("Search products has been completed in 500 milliseconds", activity.message());
    assertEquals(Level.INFO, activity.level());
    @SuppressWarnings("unchecked")
    Map<String, Object> context = (Map<String, Object>) (activity.context().get("context"));
    assertContextSize(3, context);
    assertContextValueEquals("query", "identifier", context);
    assertContextValueEquals("search-products", "query", context);
    assertContextValueEquals(500L, "durationInMilliseconds", context);
  }
}

# Logging domain events

This middleware will log all the domain events produced by a command.

You can add this logger middleware to the bus as shown below.

```java
import java.time.Clock;

public final class Application {
  public static void main(String[] args) {
    // ...
    var collector = new DomainEventsCollector();
    var feed = new ActivityFeed(logger);
    var mapper = new ObjectMapper();
    var serializer = new ContextSerializer(mapper);
    var loggerMiddleware = new EventsLoggerMiddleware(
        collector, feed, serializer);

    var bus = new MiddlewareCommandBus(List.of(
        loggerMiddleware,
        handlerMiddleware));
  }
}
```

## Querying your logs

The logging events generate by the middleware explained in the previous section look as follows.

```json
{
  "identifier": "domain-event",
  "context": {
    "name": "newsletters.subscriptionHasBeenUpdated",
    "aggregateId": "d1c064cc-47c9-4825-8b07-73a9dcb354a3",
    "event": {
      "newsletterId": "a9129274-6105-4d8c-abd3-8c227526cf84",
      "subscriberId": "05e21296-97ee-4753-b942-d1cc532bd865",
      "status": "CANCELLED",
      "occurredOn": 1648233324000
    }
  }
}
```

With the information provided by the logging event described above, you'll be able to:

- Query your logs per domain event
- Look for all the events of a given aggregate

Below is the query to look for the event `subscriptionHasBeenUpdated` in AWS Cloudwatch.

```sql
fields @timestamp, message, context
| filter `context.name` LIKE /subscriptionHasBeenUpdated/
| sort @timestamp desc
| limit 50
```

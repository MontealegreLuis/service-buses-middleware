# Logging Middleware

This package provides a middleware to log how much time queries take.
You can add this logger middleware to the bus as shown below.

```java
public final class Application {
  public static void main(String[] args) {
    // ...
    var logger = LoggerFactory.getLogger(Application.class);
    var feed = new ActivityFeed(logger);
    var clock = Clock.systemUTC();
    var loggerMiddleware = new QueryLoggerMiddleware(feed, clock);

    var bus = new MiddlewareQueryBus(List.of(
      loggerMiddleware,
      handlerMiddleware));
  }
}
```

## Querying your logs

The logging events generate by the middleware explained in the previous section look as follows.

```json
{
  "identifier": "query",
  "context": {
    "command": "search-products",
    "durationInMilliseconds": 500
  }
}
```

With the information provided by the logging event described above, you'll be able to:

- Query your logs per query
- Look for slow queries

Below is the query to look for the query `search-products` in AWS Cloudwatch.

```sql
fields @timestamp, message, `context.durationInMilliseconds`
| filter `context.query` = "search-products"
| sort @timestamp desc
| limit 50
```

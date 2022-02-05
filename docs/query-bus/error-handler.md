# Error Handler Middleware

The snippet below shows the simplest way to configure your error handler.

```java
var logger = LoggerFactory.getLogger(Application.class);
var feed = new ActivityFeed(logger);

var mapper = new ObjectMapper();
var serializer = new ContextSerializer(mapper);

var errorHandler = new QueryErrorHandlerMiddleware(
  feed, 
  serializer
);
```

You can customize the `ContextSerializer` as specified in its [documentation](https://github.com/MontealegreLuis/activity-feed#masking-sensitive-information).

Once you create the error handler, the next step is to add it to the query bus as shown below.

```java
var middleware = List.of(
  errorHandlerMiddleware, 
  queryHandlerMiddleware
);
var queryBus = new MiddlewareQueryBus(middleware);
```

### Log filtering

#### Domain exceptions

Whenever a child of the `DomainException` class is thrown, the error handler will generate an **identifier** using the **query class name** and the **exception class name**.

Suppose your query `SearchProducts` throws a domain exception `UnknownDepartment`, the logging event would have the identifier `search-products-unknown-department` which you could use to filter your logs.

The error handler will add the `exception` [information](https://github.com/MontealegreLuis/activity-feed#logging-an-exception) and the `input` used when the exception was thrown to the logging event automatically.

The snippet below shows how can you look for instances of this specific error in AWS CloudWatch.

```sql
fields @timestamp, message, `context.exception`, `context.input`
| filter `context.identifier` = "search-products-unknown-department"
| sort @timestamp desc
| limit 50
```

#### Infrastructure exceptions

Any exception that doesn't extend `DomainException` is considered an infrastructure exception.
The error handler will generate an **identifier** using the **query class name** and will add the suffix `query-failure`.

Suppose your query `SearchProducts` throws an `SQLException`, the logging event would have the identifier `search-products-query-failure` which you could use to filter your logs.

The error handler will add the `exception` [information](https://github.com/MontealegreLuis/activity-feed#logging-an-exception) and the `input` used when the exception was thrown to the logging event automatically.

The snippet below shows how can you look for instances of this specific error in AWS CloudWatch.

```sql
fields @timestamp, message, `context.exception`, `context.input`
| filter `context.identifier` = "search-products-query-failure"
| filter `context.exception.class` = "java.sql.SQLException"
| sort @timestamp desc
| limit 50
```

### How exceptions are handled

- Child classes of `DomainException` are re-thrown.
- Everything else is wrapped in a `QueryFailure` exception.

If you don't want to log exceptions twice, you could prevent it as shown in the snippet below.

```java
if (exception instanceof ActionException) return;

// rest of your application logging logic...
```

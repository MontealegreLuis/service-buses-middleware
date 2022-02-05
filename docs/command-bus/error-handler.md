# Error Handler Middleware

The snippet below shows the simplest way to configure your error handler.

```java
var logger = LoggerFactory.getLogger(Application.class);
var feed = new ActivityFeed(logger);

var mapper = new ObjectMapper();
var serializer = new ContextSerializer(mapper);

var errorHandler = new CommandErrorHandlerMiddleware(
  feed, 
  serializer
);
```

You can customize the `ContextSerializer` as specified in its [documentation](https://github.com/MontealegreLuis/activity-feed#masking-sensitive-information).

Once you create the error handler, the next step is to add it to the command bus as shown below.

```java
var middleware = List.of(
  errorHandlerMiddleware, 
  commandHandlerMiddleware
);
var commandBus = new MiddlewareCommandBus(middleware);
```

### Log filtering

#### Domain exceptions

Whenever a child of the `DomainException` class is thrown, the error handler will generate an **identifier** using the **command class name** and the **exception class name**.

Suppose your command `ProcessPayment` throws a domain exception `InsufficientFunds`, the logging event would have the identifier `process-payment-insufficient-funds` which you could use to filter your logs.

The error handler will add the `exception` [information](https://github.com/MontealegreLuis/activity-feed#logging-an-exception) and the `input` used when the exception was thrown to the logging event automatically.

The snippet below shows how can you look for instances of this specific error in AWS CloudWatch.

```sql
fields @timestamp, message, `context.exception`, `context.input`
| filter `context.identifier` = "process-payment-insufficient-funds"
| sort @timestamp desc
| limit 50
```

#### Infrastructure exceptions

Any exception that doesn't extend `DomainException` is considered an infrastructure exception.
The error handler will generate an **identifier** using the **command class name** and will add the suffix `command-failure`.

Suppose your command `ProcessPayment` throws an `SQLException`, the logging event would have the identifier `process-payment-command-failure` which you could use to filter your logs.

The error handler will add the `exception` [information](https://github.com/MontealegreLuis/activity-feed#logging-an-exception) and the `input` used when the exception was thrown to the logging event automatically.

The snippet below shows how can you look for instances of this specific error in AWS CloudWatch.

```sql
fields @timestamp, message, `context.exception`, `context.input`
| filter `context.identifier` = "process-payment-command-failure"
| filter `context.exception.class` = "java.sql.SQLException"
| sort @timestamp desc
| limit 50
```

### How exceptions are handled

- Child classes of `DomainException` are re-thrown.
- Everything else is wrapped in a `CommandFailure` exception.

If you don't want to log exceptions twice, you could prevent it as shown in the snippet below.

```java
if (exception instanceof ActionException) return;

// rest of your application logging logic...
```

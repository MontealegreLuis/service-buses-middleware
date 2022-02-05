# Query Bus Middleware

At a minimum you need to add the `QueryHandlerMiddleware` to your Query Bus, in order to locate, create and execute a query handler.

```java
public final class Application {
  public static void main(String[] args) {
    var factory = new InMemoryQueryHandlerFactory();
    var locator = new ReflectionsQueryHandlerLocator("queries.package");
    var handlerMiddleware = new QueryHandlerMiddleware(locator, factory);
    var bus = new MiddlewareQueryBus(List.of(handlerMiddleware));
  }
}
```

Middleware is a very useful concept for lots of things.
You could write middleware for:

- [Logging](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/query-bus/logging.md)
- [Error handling](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/query-bus/error-handler.md)
- Validation
- Permissions

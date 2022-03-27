# Command Bus Middleware

At a minimum, you need to add the `CommandHandlerMiddleware` to your Command Bus in order to locate, create, and execute a command handler.

```java
public final class Application {
  public static void main(String[] args) {
    var factory = new InMemoryCommandHandlerFactory();
    var locator = new ReflectionsCommandHandlerLocator("commands.package");
    var handlerMiddleware = new CommandHandlerMiddleware(locator, factory);
    var bus = new MiddlewareCommandBus(List.of(handlerMiddleware));
  }
}
```

Middleware is a very useful concept for lots of things.
You could write middleware for:

- Logging
  - [Commands](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/logging-commands.md)
  - [Domain events](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/logging-events.md)
- [Error handling](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/error-handler.md)
- [Database transactions](https://github.com/MontealegreLuis/service-buses-spring-boot#transaction-middleware)
- Queuing
- Validation
- Permissions

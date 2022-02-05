# Service buses Middleware

[![CI workflow](https://github.com/montealegreluis/service-buses-middleware/actions/workflows/ci.yml/badge.svg)](https://github.com/montealegreluis/service-buses-middleware/actions/workflows/ci.yml)
[![Release workflow](https://github.com/montealegreluis/service-buses-middleware/actions/workflows/release.yml/badge.svg)](https://github.com/montealegreluis/service-buses-middleware/actions/workflows/release.yml)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventionalcommits-e10079?logo=semantic-release)](https://github.com/semantic-release/semantic-release)

Middleware are a way to **add behavior** to [commands and queries](https://github.com/MontealegreLuis/service-buses).
When you execute a command or a query, it is passed through every Middleware.
Middleware are executed in sequence; the order is configured when you set up the `MiddlewareCommandBus` or `MiddlewareQueryBus` classes and can’t be changed later.

Middleware can control when the next middleware starts.
This allows you to control if your custom behavior will come **before** or **after** command execution, or if you’ll **suppress** the command from being executed at all.

## Usage

- [Command Bus Middleware](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/index.md)
- [Query Bus Middleware](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/query-bus/index.md)

## Installation

1. [Authenticating to GitHub Packages](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/installation/authentication.md)
2. [Maven](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/installation/maven.md)
3. [Gradle](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/installation/gradle.md)

## Contribute

Please refer to [CONTRIBUTING](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/CONTRIBUTING.md) for information on how to contribute to Service Buses.

## License

Released under the [BSD-3-Clause](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/LICENSE).

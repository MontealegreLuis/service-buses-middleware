package com.montealegreluis.servicebusesmiddleware.fakes.commandbus.middleware.logger;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Generates instant values using a given offset.
 *
 * <pre>instant</pre> will return values in fixed intervals given by the following algorithm
 *
 * <pre></code>
 * now = now + offset
 * instant = now
 * </code><pre>
 */
public final class FixedOffsetInstantClock extends Clock {
  private Instant now;
  private final ZoneId zoneId;
  private final Duration offset;

  public FixedOffsetInstantClock(Instant now, ZoneId zoneId, Duration offset) {
    this.now = now;
    this.zoneId = zoneId;
    this.offset = offset;
  }

  @Override
  public ZoneId getZone() {
    return zoneId;
  }

  /** offset defaults to 1 second now defaults to the current time */
  @Override
  public Clock withZone(ZoneId zone) {
    return new FixedOffsetInstantClock(Instant.now(), zoneId, Duration.ofSeconds(1));
  }

  @Override
  public Instant instant() {
    now = now.plus(offset);
    return now;
  }
}

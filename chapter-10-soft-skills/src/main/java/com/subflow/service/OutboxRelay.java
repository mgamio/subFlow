package com.subflow.service;

import com.subflow.domain.events.DomainEvent;

/**
 * Publishes pending events. If publishing fails, the event stays pending
 * and is tried again on the next run: delivery is at least once.
 */
public class OutboxRelay {
  private final Outbox outbox;
  private final EventPublisher publisher;

  public OutboxRelay(Outbox outbox, EventPublisher publisher) {
    this.outbox = outbox;
    this.publisher = publisher;
  }

  public int relayPending() {
    int published = 0;
    for (DomainEvent event : outbox.pending()) {
      publisher.publish(event);
      outbox.markPublished(event.eventId());
      published++;
    }
    return published;
  }
}

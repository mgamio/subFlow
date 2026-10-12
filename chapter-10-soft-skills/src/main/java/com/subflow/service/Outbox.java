package com.subflow.service;

import com.subflow.domain.events.DomainEvent;
import java.util.List;

/**
 * Events stored in the same database, in the same transaction as the
 * change they describe. A relay publishes them afterward.
 */
public interface Outbox {
  void add(DomainEvent event);

  List<DomainEvent> pending();

  void markPublished(String eventId);
}

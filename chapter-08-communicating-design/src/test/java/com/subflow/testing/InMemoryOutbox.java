package com.subflow.testing;

import com.subflow.domain.events.DomainEvent;
import com.subflow.service.Outbox;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryOutbox implements Outbox {
  private final Map<String, DomainEvent> pending = new LinkedHashMap<>();

  @Override
  public void add(DomainEvent event) {
    pending.put(event.eventId(), event);
  }

  @Override
  public List<DomainEvent> pending() {
    return new ArrayList<>(pending.values());
  }

  @Override
  public void markPublished(String eventId) {
    pending.remove(eventId);
  }
}

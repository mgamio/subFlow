package com.subflow.testing;

import com.subflow.service.ProcessedEvents;
import java.util.HashSet;
import java.util.Set;

public class InMemoryProcessedEvents implements ProcessedEvents {
  private final Set<String> ids = new HashSet<>();

  @Override
  public synchronized boolean markProcessed(String eventId) {
    return ids.add(eventId);
  }
}

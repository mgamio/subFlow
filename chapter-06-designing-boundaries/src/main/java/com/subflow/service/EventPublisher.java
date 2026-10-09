package com.subflow.service;

import com.subflow.domain.events.DomainEvent;

public interface EventPublisher {
  void publish(DomainEvent event);
}

package com.subflow.domain.events;

import java.time.LocalDate;

/** Something that happened in SubFlow that other parts may react to. */
public sealed interface DomainEvent
    permits SubscriptionCanceled, InvoiceIssued {

  /** Unique and stable: consumers use it to ignore duplicates. */
  String eventId();

  LocalDate occurredOn();
}

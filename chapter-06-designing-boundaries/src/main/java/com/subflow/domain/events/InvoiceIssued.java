package com.subflow.domain.events;

import com.subflow.domain.Money;
import java.time.LocalDate;

public record InvoiceIssued(long invoiceId, long subscriptionId, Money total,
                            LocalDate occurredOn) implements DomainEvent {
  @Override
  public String eventId() {
    return "invoice-issued-" + invoiceId;
  }
}

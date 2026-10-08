package com.subflow.domain;

import java.time.LocalDate;
import java.util.Objects;

public record Payment(String id, long subscriptionId,
                      PaymentProvider provider, Money amount,
                      LocalDate date) {
  public Payment {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(provider, "provider");
    Objects.requireNonNull(amount, "amount");
    Objects.requireNonNull(date, "date");
  }
}

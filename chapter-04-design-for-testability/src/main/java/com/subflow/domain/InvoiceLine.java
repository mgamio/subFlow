package com.subflow.domain;

import java.util.Objects;

public record InvoiceLine(String description, Money amount) {

  public InvoiceLine {
    Objects.requireNonNull(description, "description");
    Objects.requireNonNull(amount, "amount");
    if (amount.isNegative()) {
      throw new IllegalArgumentException(
          "An invoice line cannot be negative: " + amount);
    }
  }
}

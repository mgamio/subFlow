package com.subflow.domain;

import java.util.Objects;

public record InvoiceLine(String description, Money amount, Kind kind) {

  /** Recorded now because VAT is announced; see ADR 0002. */
  public enum Kind { SUBSCRIPTION, TAX, ADJUSTMENT }

  public InvoiceLine {
    Objects.requireNonNull(description, "description");
    Objects.requireNonNull(amount, "amount");
    Objects.requireNonNull(kind, "kind");
    if (amount.isNegative()) {
      throw new IllegalArgumentException(
          "An invoice line cannot be negative: " + amount);
    }
  }

  /** A subscription line: the only kind SubFlow issues today. */
  public InvoiceLine(String description, Money amount) {
    this(description, amount, Kind.SUBSCRIPTION);
  }
}

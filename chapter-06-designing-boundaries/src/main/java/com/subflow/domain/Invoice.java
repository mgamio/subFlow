package com.subflow.domain;

import java.util.List;

public record Invoice(long subscriptionId,
                      String customerEmail,
                      List<InvoiceLine> lines) {

  public Invoice {
    if (lines.isEmpty()) {
      throw new IllegalArgumentException(
          "An invoice needs at least one line");
    }
    lines = List.copyOf(lines);   // nobody can change them later
  }

  public Money total() {
    return lines.stream()
        .map(InvoiceLine::amount)
        .reduce(Money::plus)
        .orElseThrow();
  }
}

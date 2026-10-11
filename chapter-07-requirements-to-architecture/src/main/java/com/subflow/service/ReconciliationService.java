package com.subflow.service;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import java.time.LocalDate;

/** Used by Finance to check what the provider actually collected. */
public class ReconciliationService {
  private final TransactionHistory history;

  public ReconciliationService(TransactionHistory history) {
    this.history = history;
  }

  public Money collectedBetween(LocalDate from, LocalDate to) {
    return history.paymentsBetween(from, to).stream()
        .map(Payment::amount)
        .reduce(Money::plus)
        .orElse(Money.usd("0"));
  }
}

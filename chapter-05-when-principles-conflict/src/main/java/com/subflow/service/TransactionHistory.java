package com.subflow.service;

import com.subflow.domain.Payment;
import java.time.LocalDate;
import java.util.List;

public interface TransactionHistory {
  List<Payment> paymentsBetween(LocalDate from, LocalDate to);
}

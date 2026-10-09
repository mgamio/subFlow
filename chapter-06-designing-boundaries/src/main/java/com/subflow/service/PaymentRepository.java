package com.subflow.service;

import com.subflow.domain.Payment;
import java.util.Optional;

public interface PaymentRepository {
  void save(Payment payment);
  Optional<Payment> lastPaymentFor(long subscriptionId);
}

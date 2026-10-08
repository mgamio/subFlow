package com.subflow.testing;

import com.subflow.domain.Payment;
import com.subflow.service.PaymentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryPayments implements PaymentRepository {
  private final List<Payment> rows = new ArrayList<>();

  @Override
  public void save(Payment payment) {
    rows.removeIf(p -> p.id().equals(payment.id()));
    rows.add(payment);
  }

  @Override
  public Optional<Payment> lastPaymentFor(long subscriptionId) {
    for (int i = rows.size() - 1; i >= 0; i--) {
      if (rows.get(i).subscriptionId() == subscriptionId) {
        return Optional.of(rows.get(i));
      }
    }
    return Optional.empty();
  }

  public List<Payment> all() {
    return List.copyOf(rows);
  }
}

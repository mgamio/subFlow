package examples.ch03.lsp.before;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;

public interface PaymentGateway {
  Payment charge(long subscriptionId, Money amount);
  void refund(Payment payment);
}

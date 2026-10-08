package examples.ch03.isp.before;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.service.RefundResult;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/** One interface for four different kinds of clients. */
public interface PaymentGateway {
  Payment charge(long subscriptionId, Money amount);       // checkout
  RefundResult refund(Payment payment);                    // cancellation
  List<Payment> paymentsBetween(LocalDate from,
                                LocalDate to);             // finance
  void registerWebhook(URI callbackUrl);                   // operations
}

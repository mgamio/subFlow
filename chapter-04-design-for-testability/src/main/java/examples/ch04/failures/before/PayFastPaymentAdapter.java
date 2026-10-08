package examples.ch04.failures.before;

import com.payfast.sdk.PayFastCharge;
import com.payfast.sdk.PayFastChargeRequest;
import com.payfast.sdk.PayFastClient;
import com.payfast.sdk.PayFastTimeoutException;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.service.PaymentDeclinedException;
import java.time.LocalDate;

/** The retry that charged customers twice. */
public class PayFastPaymentAdapter {
  private static final int MAX_ATTEMPTS = 3;

  private final PayFastClient payFast;

  public PayFastPaymentAdapter(PayFastClient payFast) {
    this.payFast = payFast;
  }

  public Payment charge(long subscriptionId, Money amount) {
    long cents = amount.amount().movePointRight(2).longValueExact();
    for (int attempt = 1; ; attempt++) {
      try {
        PayFastCharge charge = payFast.createCharge(
            new PayFastChargeRequest("subscription-" + subscriptionId,
                cents, "USD", null));
        return new Payment(charge.id(), subscriptionId,
            PaymentProvider.PAYFAST, amount, LocalDate.now());
      } catch (PayFastTimeoutException e) {
        if (attempt == MAX_ATTEMPTS) {
          throw new PaymentDeclinedException("PayFast is not answering");
        }
        sleep(2000);   // give PayFast time to recover
      }
    }
  }

  private static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}

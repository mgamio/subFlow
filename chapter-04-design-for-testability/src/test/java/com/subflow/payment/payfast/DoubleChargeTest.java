package com.subflow.payment.payfast;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.payfast.sdk.PayFastCharge;
import com.payfast.sdk.PayFastChargeRequest;
import com.payfast.sdk.PayFastClient;
import com.payfast.sdk.PayFastTimeoutException;
import com.subflow.domain.Money;
import com.subflow.payment.RetryPolicy;
import com.subflow.payment.RetryingPaymentCharger;
import com.subflow.service.ChargeRequest;
import org.junit.jupiter.api.Test;

/** The weekend incident, reproduced in milliseconds. */
class DoubleChargeTest {

  /** PayFast on a bad day: it charges the card, then the answer is lost. */
  static class SlowPayFastClient extends PayFastClient {
    private int timeouts;

    SlowPayFastClient(int timeouts) {
      super("test-key", clockOn(START));
      this.timeouts = timeouts;
    }

    @Override
    public PayFastCharge createCharge(PayFastChargeRequest request) {
      PayFastCharge charge = super.createCharge(request);
      if (timeouts > 0) {
        timeouts--;
        throw new PayFastTimeoutException("Read timed out after 10 s");
      }
      return charge;
    }
  }

  @Test
  void theOriginalRetryChargesTwice() {
    SlowPayFastClient payFast = new SlowPayFastClient(1);
    var adapter = new examples.ch04.failures.before.PayFastPaymentAdapter(payFast);

    adapter.charge(7, Money.usd("203.90"));

    assertEquals(2, payFast.listCharges(START, START).size());
  }

  @Test
  void theIdempotentRetryChargesOnce() {
    SlowPayFastClient payFast = new SlowPayFastClient(1);
    var charger = new RetryingPaymentCharger(
        new PayFastPaymentAdapter(payFast, clockOn(START)),
        RetryPolicy.DEFAULT, duration -> { });

    charger.charge(ChargeRequest.forBillingPeriod(7, Money.usd("203.90"), START));

    assertEquals(1, payFast.listCharges(START, START).size());
  }
}

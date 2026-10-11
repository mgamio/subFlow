package com.subflow.payment.payfast;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.payfast.sdk.PayFastCharge;
import com.payfast.sdk.PayFastChargeRequest;
import com.payfast.sdk.PayFastClient;
import com.payfast.sdk.PayFastConnectionException;
import com.subflow.domain.Money;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentProviderUnavailableException;
import org.junit.jupiter.api.Test;

class PayFastOutageTest {

  @Test
  void aRefusedConnectionMeansCertainlyNotCharged() {
    PayFastClient down = new PayFastClient("test-key", clockOn(START)) {
      @Override
      public PayFastCharge createCharge(PayFastChargeRequest request) {
        throw new PayFastConnectionException("Connection refused");
      }
    };
    var adapter = new PayFastPaymentAdapter(down, clockOn(START));
    assertThrows(PaymentProviderUnavailableException.class,
        () -> adapter.charge(ChargeRequest.forBillingPeriod(7, Money.usd("19.99"), START)));
  }
}

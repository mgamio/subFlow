package com.subflow.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.subflow.domain.Payment;
import org.junit.jupiter.api.Test;

/** Every PaymentRefunder must pass these tests: the LSP, written down. */
public abstract class PaymentRefunderContract {

  protected abstract PaymentRefunder refunder();

  /** A payment that this provider accepted. */
  protected abstract Payment validPayment();

  @Test
  void refundingAValidPaymentNeverThrows() {
    assertDoesNotThrow(() -> refunder().refund(validPayment()));
  }

  @Test
  void refundAlwaysReturnsAStatusAndAReference() {
    RefundResult result = refunder().refund(validPayment());
    assertNotNull(result.status());
    assertFalse(result.reference().isBlank());
  }
}

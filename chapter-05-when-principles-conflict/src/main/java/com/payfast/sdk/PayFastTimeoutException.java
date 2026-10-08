package com.payfast.sdk;

/** Part of a SIMULATED vendor SDK: no answer within the read timeout. */
public class PayFastTimeoutException extends PayFastException {
  public PayFastTimeoutException(String message) {
    super(message);
  }
}

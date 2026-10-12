package com.subflow.service;

/** The provider did not answer in time: the charge may or may not exist. */
public class PaymentTimeoutException extends RuntimeException {
  public PaymentTimeoutException(String message) {
    super(message);
  }
}

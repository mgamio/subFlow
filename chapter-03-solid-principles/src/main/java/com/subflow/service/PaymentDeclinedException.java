package com.subflow.service;

public class PaymentDeclinedException extends RuntimeException {
  public PaymentDeclinedException(String message) {
    super(message);
  }
}

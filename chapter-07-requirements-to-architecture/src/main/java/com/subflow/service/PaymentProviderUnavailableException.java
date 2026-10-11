package com.subflow.service;

/**
 * The provider could not be reached at all, so the charge certainly
 * did not happen. Unlike a timeout, it is safe to try elsewhere.
 */
public class PaymentProviderUnavailableException extends RuntimeException {
  public PaymentProviderUnavailableException(String message) {
    super(message);
  }
}

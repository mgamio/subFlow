package com.subflow.service;

/** Someone else changed the data since it was read. */
public class ConcurrentUpdateException extends RuntimeException {
  public ConcurrentUpdateException(String message) {
    super(message);
  }
}

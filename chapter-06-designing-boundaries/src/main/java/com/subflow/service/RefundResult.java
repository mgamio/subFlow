package com.subflow.service;

import java.util.Objects;

public record RefundResult(RefundStatus status, String reference) {
  public RefundResult {
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(reference, "reference");
  }
}

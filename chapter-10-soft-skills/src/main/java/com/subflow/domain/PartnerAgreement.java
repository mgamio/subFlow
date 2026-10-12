package com.subflow.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record PartnerAgreement(String company, BigDecimal discount) {

  public PartnerAgreement {
    Objects.requireNonNull(company, "company");
    if (discount.signum() <= 0
        || discount.compareTo(BigDecimal.ONE) >= 0) {
      throw new IllegalArgumentException(
          "A partner discount must be between 0 and 1");
    }
  }
}

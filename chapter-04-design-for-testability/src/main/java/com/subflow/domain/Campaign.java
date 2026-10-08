package com.subflow.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record Campaign(String name, BigDecimal annualDiscount,
                       LocalDate firstDay, LocalDate lastDay) {

  public Campaign {
    Objects.requireNonNull(name, "name");
    if (annualDiscount.signum() <= 0
        || annualDiscount.compareTo(BigDecimal.ONE) >= 0) {
      throw new IllegalArgumentException(
          "A campaign discount must be between 0 and 1");
    }
    if (lastDay.isBefore(firstDay)) {
      throw new IllegalArgumentException(
          "Campaign " + name + " ends before it starts");
    }
  }

  public boolean isRunningOn(LocalDate day) {
    return !day.isBefore(firstDay) && !day.isAfter(lastDay);
  }
}

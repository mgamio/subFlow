package com.subflow.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * An amount of money in one currency, always rounded to the
 * currency's number of decimal places.
 */
public record Money(BigDecimal amount, Currency currency) {

  public Money {
    Objects.requireNonNull(amount, "amount");
    Objects.requireNonNull(currency, "currency");
    amount = amount.setScale(
        currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
  }

  public static Money of(String amount, String currencyCode) {
    return new Money(new BigDecimal(amount),
                     Currency.getInstance(currencyCode));
  }

  public static Money usd(String amount) {
    return of(amount, "USD");
  }

  public Money plus(Money other) {
    requireSameCurrency(other);
    return new Money(amount.add(other.amount), currency);
  }

  public Money times(BigDecimal factor) {
    return new Money(amount.multiply(factor), currency);
  }

  public boolean isNegative() {
    return amount.signum() < 0;
  }

  private void requireSameCurrency(Money other) {
    if (!currency.equals(other.currency)) {
      throw new IllegalArgumentException(
          "Cannot combine " + currency + " and " + other.currency);
    }
  }

  @Override
  public String toString() {
    return currency.getCurrencyCode() + " " + amount.toPlainString();
  }
}

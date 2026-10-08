package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

  @Test
  void isAlwaysRoundedToTheCurrencyScale() {
    assertEquals(Money.usd("203.90"),
        Money.usd("239.88").times(new BigDecimal("0.85")));
    assertEquals(Money.of("1500", "JPY"),
        Money.of("1499.6", "JPY"));
  }

  @Test
  void addsAmountsInTheSameCurrency() {
    assertEquals(Money.usd("29.98"),
        Money.usd("9.99").plus(Money.usd("19.99")));
  }

  @Test
  void refusesToMixCurrencies() {
    assertThrows(IllegalArgumentException.class,
        () -> Money.usd("10").plus(Money.of("10", "EUR")));
  }

  @Test
  void printsCurrencyAndAmount() {
    assertEquals("USD 203.90", Money.usd("203.9").toString());
  }
}

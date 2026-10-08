package examples.ch02.encapsulation.after;

import java.math.BigDecimal;

public class BankAccount {
  private BigDecimal balance;

  public BankAccount(BigDecimal initialBalance) {
    requireNotNegative(initialBalance);
    balance = initialBalance;
  }

  public void deposit(BigDecimal amount) {
    if (amount.signum() <= 0) {
      throw new IllegalArgumentException(
          "A deposit must be positive: " + amount);
    }
    balance = balance.add(amount);
  }

  public BigDecimal balance() {
    return balance;
  }

  private static void requireNotNegative(BigDecimal amount) {
    if (amount.signum() < 0) {
      throw new IllegalArgumentException(
          "A balance cannot start negative: " + amount);
    }
  }
}

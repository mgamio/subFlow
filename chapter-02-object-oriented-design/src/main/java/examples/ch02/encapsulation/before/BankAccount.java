package examples.ch02.encapsulation.before;

/** The balance is private, yet nothing protects it. */
public class BankAccount {
  private double balance;

  public BankAccount(double initialBalance) {
    balance = initialBalance;
  }

  public void deposit(double amount) {
    balance += amount;
  }

  public double getBalance() {
    return balance;
  }
}

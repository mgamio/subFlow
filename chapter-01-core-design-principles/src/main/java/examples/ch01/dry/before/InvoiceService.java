package examples.ch01.dry.before;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvoiceService {

  public BigDecimal amountDue(Subscription subscription) {
    BigDecimal monthly = subscription.getPlan().equals("PRO")
        ? new BigDecimal("19.99")
        : new BigDecimal("9.99");
    if (subscription.isAnnual()) {
      return monthly.multiply(new BigDecimal("12"))
          .multiply(new BigDecimal("0.8"))
          .setScale(2, RoundingMode.HALF_UP);
    }
    return monthly;
  }
}

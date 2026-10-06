package examples.ch01.dry.before;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CheckoutService {

  public BigDecimal priceFor(String plan, boolean annual) {
    BigDecimal monthly = switch (plan) {
      case "BASIC" -> new BigDecimal("9.99");
      case "PRO"   -> new BigDecimal("19.99");
      default -> throw new IllegalArgumentException(
          "Unknown plan: " + plan);
    };
    if (annual) {
      // 12 months with a 20% discount
      return monthly.multiply(BigDecimal.valueOf(12))
          .multiply(new BigDecimal("0.80"))
          .setScale(2, RoundingMode.HALF_UP);
    }
    return monthly;
  }
}

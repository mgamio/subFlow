package examples.ch09.ordering;

import examples.ch09.shared.Money;
import java.util.Objects;

/**
 * A copy of the article as it was when the order was placed. If the supplier
 * changes the price tomorrow, this order does not change.
 */
public record OrderLine(String articleId, String name, int quantity, Money unitPrice) {

  public OrderLine {
    Objects.requireNonNull(articleId, "articleId");
    Objects.requireNonNull(unitPrice, "unitPrice");
    if (quantity < 1) {
      throw new InvalidOrderException("Quantity must be at least 1: " + articleId);
    }
  }

  public Money total() {
    return unitPrice.times(quantity);
  }
}

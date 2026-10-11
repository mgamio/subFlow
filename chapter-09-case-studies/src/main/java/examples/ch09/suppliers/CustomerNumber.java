package examples.ch09.suppliers;

import java.util.Objects;

/**
 * The number a supplier gives a buyer. It belongs to the pair (buyer, supplier),
 * not to either one alone. A pair may have several (one per department) or none.
 */
public record CustomerNumber(long buyerId, long supplierId, String value, String department) {

  public CustomerNumber {
    Objects.requireNonNull(value, "value");
    if (value.isBlank()) {
      throw new IllegalArgumentException("A customer number cannot be blank");
    }
    department = department == null ? "" : department;
  }
}

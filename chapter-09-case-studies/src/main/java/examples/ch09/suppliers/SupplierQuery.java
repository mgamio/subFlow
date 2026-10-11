package examples.ch09.suppliers;

import java.util.Optional;

/** Everything a client can ask about a buyer's suppliers, in one value. */
public record SupplierQuery(
    long buyerId,
    Optional<String> customerNumber,
    Sort<SupplierQuery.Field> sort,
    PageRequest page) {

  public enum Field { NAME, CITY }

  public static SupplierQuery of(long buyerId) {
    return new SupplierQuery(buyerId, Optional.empty(),
        new Sort<>(Field.NAME, Sort.Direction.ASC), PageRequest.first(20));
  }

  public SupplierQuery withCustomerNumber(String number) {
    return new SupplierQuery(buyerId, Optional.of(number), sort, page);
  }
}

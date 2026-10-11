package examples.ch09.suppliers;

import java.util.Optional;

public record CustomerNumberQuery(
    long buyerId,
    long supplierId,
    Optional<String> number,
    Sort<CustomerNumberQuery.Field> sort,
    PageRequest page) {

  public enum Field { NUMBER, DEPARTMENT }
}

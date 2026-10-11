package examples.ch09.suppliers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SupplierQueryTest {

  @Test
  void sortTextFromTheUrlBecomesATypedSort() {
    Sort<SupplierQuery.Field> sort = Sort.parse("city,desc", SupplierQuery.Field.class);

    assertEquals(SupplierQuery.Field.CITY, sort.field());
    assertEquals(Sort.Direction.DESC, sort.direction());
  }

  @Test
  void ascendingIsTheDefaultDirection() {
    assertEquals(Sort.Direction.ASC,
        Sort.parse("name", SupplierQuery.Field.class).direction());
  }

  @Test
  void aFieldTheApiDoesNotOfferIsAClientError() {
    InvalidQueryException e = assertThrows(InvalidQueryException.class,
        () -> Sort.parse("password", SupplierQuery.Field.class));
    assertEquals("Cannot sort by 'password'", e.getMessage());
  }

  @Test
  void theSameSortRulesServeEveryList() {
    assertEquals(CustomerNumberQuery.Field.DEPARTMENT,
        Sort.parse("department", CustomerNumberQuery.Field.class).field());
    assertThrows(InvalidQueryException.class,
        () -> Sort.parse("name,sideways", CustomerNumberQuery.Field.class));
  }

  @Test
  void pageSizeIsLimited() {
    assertThrows(InvalidQueryException.class, () -> new PageRequest(0, 0));
    assertThrows(InvalidQueryException.class, () -> new PageRequest(0, 101));
    assertThrows(InvalidQueryException.class, () -> new PageRequest(-1, 20));
    assertEquals(100, new PageRequest(200, 100).limit());
  }

  @Test
  void aNewFilterDoesNotChangeAnySignature() {
    SupplierQuery query = SupplierQuery.of(7).withCustomerNumber("K-1043");

    assertEquals("K-1043", query.customerNumber().orElseThrow());
    assertEquals(20, query.page().limit());
  }

  @Test
  void aCustomerNumberCannotBeBlank() {
    assertThrows(IllegalArgumentException.class, () -> new CustomerNumber(1, 2, " ", null));
    assertEquals("", new CustomerNumber(1, 2, "K-1043", null).department());
  }
}

package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EmailAddressTest {

  @Test
  void equalValuesAreEqualAddresses() {
    assertEquals(EmailAddress.of("ana@example.com"), EmailAddress.of("  Ana@Example.COM "));
  }

  @Test
  void customersAlwaysHoldANormalizedEmail() {
    assertEquals("ana@example.com", new Customer(1, "Ana", "Ana@Example.com").email());
  }

  @Test
  void textThatIsNotAnEmailIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("ana.example.com"));
    assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("ana@example"));
  }
}

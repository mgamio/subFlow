package com.subflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SrpTest {
  private final List<OverdueAccount> accounts = List.of(
      new OverdueAccount(1, 5), new OverdueAccount(2, 8),
      new OverdueAccount(3, 15));

  @Test
  void eachTeamKeepsItsOwnRule() {
    List<Long> charged = new ArrayList<>();
    List<Long> suspended = new ArrayList<>();

    new LateFeePolicy(charged::add).apply(accounts);
    new AccessSuspensionPolicy(suspended::add).apply(accounts);

    assertEquals(List.of(3L), charged);         // 14 days or more
    assertEquals(List.of(2L, 3L), suspended);   // 7 days or more
  }
}

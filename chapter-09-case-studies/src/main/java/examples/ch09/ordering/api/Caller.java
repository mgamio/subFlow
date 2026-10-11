package examples.ch09.ordering.api;

import java.util.Set;

/** The authenticated user and the buyers (restaurants) they may act for. */
public record Caller(String userId, Set<Long> buyerIds) {

  public boolean mayActFor(long buyerId) {
    return buyerIds.contains(buyerId);
  }
}

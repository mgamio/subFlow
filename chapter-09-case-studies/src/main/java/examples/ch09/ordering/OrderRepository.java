package examples.ch09.ordering;

import java.util.Optional;

/**
 * Production: a table with a UNIQUE (buyer_id, idempotency_key) constraint,
 * so two concurrent retries cannot both insert (see Chapter 6).
 */
public interface OrderRepository {
  long nextId();

  void add(Order order, String idempotencyKey);

  Optional<Order> findByIdempotencyKey(long buyerId, String idempotencyKey);
}

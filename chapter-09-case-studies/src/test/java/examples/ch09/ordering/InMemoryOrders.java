package examples.ch09.ordering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryOrders implements OrderRepository {
  private final Map<String, Order> byKey = new HashMap<>();
  final List<Order> saved = new ArrayList<>();
  private long lastId;

  @Override
  public long nextId() {
    return ++lastId;
  }

  @Override
  public void add(Order order, String idempotencyKey) {
    if (byKey.putIfAbsent(order.buyerId() + "/" + idempotencyKey, order) != null) {
      throw new IllegalStateException("Duplicate idempotency key");  // the UNIQUE constraint
    }
    saved.add(order);
  }

  @Override
  public Optional<Order> findByIdempotencyKey(long buyerId, String idempotencyKey) {
    return Optional.ofNullable(byKey.get(buyerId + "/" + idempotencyKey));
  }
}

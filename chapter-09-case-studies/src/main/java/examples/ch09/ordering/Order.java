package examples.ch09.ordering;

import examples.ch09.shared.Money;
import java.util.List;

/** One buyer, one supplier, at least one line, one currency. */
public final class Order {
  private final long id;
  private final long buyerId;
  private final long supplierId;
  private final List<OrderLine> lines;
  private final OrderStatus status;

  private Order(long id, long buyerId, long supplierId, List<OrderLine> lines,
      OrderStatus status) {
    this.id = id;
    this.buyerId = buyerId;
    this.supplierId = supplierId;
    this.lines = List.copyOf(lines);
    this.status = status;
  }

  public static Order place(long id, long buyerId, long supplierId, List<OrderLine> lines) {
    if (lines.isEmpty()) {
      throw new InvalidOrderException("An order needs at least one line");
    }
    if (lines.stream().map(l -> l.unitPrice().currency()).distinct().count() > 1) {
      throw new InvalidOrderException("All lines must be in one currency");
    }
    return new Order(id, buyerId, supplierId, lines, OrderStatus.SUBMITTED);
  }

  public Money total() {
    return lines.stream()
        .map(OrderLine::total)
        .reduce(Money::plus)
        .orElseThrow();
  }

  public long id() { return id; }
  public long buyerId() { return buyerId; }
  public long supplierId() { return supplierId; }
  public List<OrderLine> lines() { return lines; }
  public OrderStatus status() { return status; }
}

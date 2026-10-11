package examples.ch09.ordering;

import java.util.List;
import java.util.Optional;

/** The use case "a buyer places an order", with its rules in one place. */
public class OrderingService {
  private final Assortments assortments;
  private final OrderRepository orders;

  public OrderingService(Assortments assortments, OrderRepository orders) {
    this.assortments = assortments;
    this.orders = orders;
  }

  public Order placeOrder(String idempotencyKey, PlaceOrder request) {
    Optional<Order> earlier =
        orders.findByIdempotencyKey(request.buyerId(), idempotencyKey);
    if (earlier.isPresent()) {
      return earlier.get();              // a retry: same answer, no new order
    }
    List<OrderLine> lines = request.items().stream()
        .map(item -> lineFor(request, item))
        .toList();
    Order order = Order.place(orders.nextId(), request.buyerId(),
        request.supplierId(), lines);
    orders.add(order, idempotencyKey);
    return order;
  }

  private OrderLine lineFor(PlaceOrder request, PlaceOrder.Item item) {
    ArticleOffer offer = assortments
        .offer(request.buyerId(), request.supplierId(), item.articleId())
        .orElseThrow(() -> new ArticleNotOfferedException(
            request.supplierId(), item.articleId()));
    return new OrderLine(offer.articleId(), offer.name(), item.quantity(),
        offer.unitPrice());
  }
}

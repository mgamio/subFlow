package examples.ch09.ordering.api;

import examples.ch09.ordering.ArticleNotOfferedException;
import examples.ch09.ordering.InvalidOrderException;
import examples.ch09.ordering.Order;
import examples.ch09.ordering.OrderingService;
import examples.ch09.ordering.PlaceOrder;

/** POST /buyers/{buyerId}/orders. HTTP concerns stay here; rules stay in the service. */
public class OrdersApi {
  private final OrderingService ordering;

  public OrdersApi(OrderingService ordering) {
    this.ordering = ordering;
  }

  public ApiResponse placeOrder(Caller caller, long buyerId,
      String idempotencyKey, PlaceOrderRequest body) {
    if (!caller.mayActFor(buyerId)) {
      return ApiResponse.problem(403, "Forbidden",
          "You cannot place orders for buyer " + buyerId);
    }
    if (idempotencyKey == null || idempotencyKey.isBlank()) {
      return ApiResponse.problem(400, "Missing Idempotency-Key",
          "Send a unique Idempotency-Key header so that retries are safe");
    }
    try {
      Order order = ordering.placeOrder(idempotencyKey,
          new PlaceOrder(buyerId, body.supplierId(), body.items()));
      return new ApiResponse(201, order);
    } catch (ArticleNotOfferedException | InvalidOrderException e) {
      return ApiResponse.problem(422, "Order not accepted", e.getMessage());
    }
  }
}

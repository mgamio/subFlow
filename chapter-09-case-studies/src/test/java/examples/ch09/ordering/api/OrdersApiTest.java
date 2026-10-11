package examples.ch09.ordering.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import examples.ch09.ordering.Assortments;
import examples.ch09.ordering.ArticleOffer;
import examples.ch09.ordering.Order;
import examples.ch09.ordering.OrderRepository;
import examples.ch09.ordering.OrderingService;
import examples.ch09.ordering.PlaceOrder;
import examples.ch09.shared.Money;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrdersApiTest {
  static final long LA_CANTINA = 7;
  static final long GREEN_FARM = 21;

  final Assortments assortments = (buyer, supplier, article) -> article.equals("TOM-5KG")
      ? Optional.of(new ArticleOffer(supplier, article, "Tomatoes, 5 kg",
          Money.of("12.50", "USD")))
      : Optional.empty();
  final OrdersApi api = new OrdersApi(new OrderingService(assortments, new Orders()));

  final Caller chef = new Caller("chef@la-cantina", Set.of(LA_CANTINA));
  final PlaceOrderRequest tomatoes = new PlaceOrderRequest(GREEN_FARM,
      List.of(new PlaceOrder.Item("TOM-5KG", 2)));

  @Test
  void createsTheOrder() {
    ApiResponse response = api.placeOrder(chef, LA_CANTINA, "key-1", tomatoes);

    assertEquals(201, response.status());
    assertEquals(Money.of("25.00", "USD"), ((Order) response.body()).total());
  }

  @Test
  void aCallerCannotOrderForAnotherRestaurant() {
    assertEquals(403, api.placeOrder(chef, 99, "key-1", tomatoes).status());
  }

  @Test
  void anIdempotencyKeyIsRequired() {
    assertEquals(400, api.placeOrder(chef, LA_CANTINA, null, tomatoes).status());
    assertEquals(400, api.placeOrder(chef, LA_CANTINA, " ", tomatoes).status());
  }

  @Test
  void businessRulesBecomeAProblemResponse() {
    PlaceOrderRequest saffron = new PlaceOrderRequest(GREEN_FARM,
        List.of(new PlaceOrder.Item("SAFFRON-1G", 1)));

    ApiResponse response = api.placeOrder(chef, LA_CANTINA, "key-1", saffron);

    assertEquals(422, response.status());
    ApiResponse.Problem problem = assertInstanceOf(ApiResponse.Problem.class, response.body());
    assertEquals("Supplier 21 does not offer article SAFFRON-1G to this buyer",
        problem.detail());
  }

  /** The smallest repository that works; the ordering tests use a fuller one. */
  static class Orders implements OrderRepository {
    private final Map<String, Order> byKey = new HashMap<>();
    private long lastId;

    public long nextId() { return ++lastId; }

    public void add(Order order, String key) { byKey.put(order.buyerId() + "/" + key, order); }

    public Optional<Order> findByIdempotencyKey(long buyerId, String key) {
      return Optional.ofNullable(byKey.get(buyerId + "/" + key));
    }
  }
}

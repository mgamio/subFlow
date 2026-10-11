package examples.ch09.ordering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import examples.ch09.shared.Money;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderingServiceTest {
  static final long LA_CANTINA = 7;        // a buyer: one restaurant
  static final long GREEN_FARM = 21;       // a supplier

  final InMemoryAssortments assortments = new InMemoryAssortments()
      .offer(LA_CANTINA, GREEN_FARM, "TOM-5KG", "Tomatoes, 5 kg", "12.50")
      .offer(LA_CANTINA, GREEN_FARM, "ONI-10KG", "Onions, 10 kg", "9.00");
  final InMemoryOrders orders = new InMemoryOrders();
  final OrderingService ordering = new OrderingService(assortments, orders);

  final PlaceOrder request = new PlaceOrder(LA_CANTINA, GREEN_FARM, List.of(
      new PlaceOrder.Item("TOM-5KG", 4), new PlaceOrder.Item("ONI-10KG", 1)));

  @Test
  void pricesComeFromTheBuyersAssortment() {
    Order order = ordering.placeOrder("key-1", request);

    assertEquals(Money.of("59.00", "USD"), order.total());   // 4 x 12.50 + 9.00
    assertEquals(OrderStatus.SUBMITTED, order.status());
  }

  @Test
  void anOrderKeepsThePriceItWasPlacedAt() {
    Order order = ordering.placeOrder("key-1", request);

    assortments.offer(LA_CANTINA, GREEN_FARM, "TOM-5KG", "Tomatoes, 5 kg", "14.00");

    assertEquals(Money.of("12.50", "USD"), order.lines().get(0).unitPrice());
    assertEquals(Money.of("59.00", "USD"), order.total());
  }

  @Test
  void aRetryWithTheSameKeyReturnsTheSameOrder() {
    Order first = ordering.placeOrder("key-1", request);
    Order retry = ordering.placeOrder("key-1", request);   // the kitchen Wi-Fi dropped

    assertSame(first, retry);
    assertEquals(1, orders.saved.size());
  }

  @Test
  void aDifferentKeyIsANewOrder() {
    ordering.placeOrder("key-1", request);
    ordering.placeOrder("key-2", request);

    assertEquals(2, orders.saved.size());
  }

  @Test
  void aBuyerCanOnlyOrderWhatTheSupplierOffersThem() {
    PlaceOrder notOffered = new PlaceOrder(LA_CANTINA, GREEN_FARM,
        List.of(new PlaceOrder.Item("SAFFRON-1G", 1)));

    assertThrows(ArticleNotOfferedException.class,
        () -> ordering.placeOrder("key-1", notOffered));
    assertEquals(0, orders.saved.size());
  }

  @Test
  void anOrderNeedsLinesWithPositiveQuantities() {
    assertThrows(InvalidOrderException.class, () -> ordering.placeOrder("key-1",
        new PlaceOrder(LA_CANTINA, GREEN_FARM, List.of())));
    assertThrows(InvalidOrderException.class, () -> ordering.placeOrder("key-2",
        new PlaceOrder(LA_CANTINA, GREEN_FARM, List.of(new PlaceOrder.Item("TOM-5KG", 0)))));
  }

  @Test
  void anOrderCannotMixCurrencies() {
    List<OrderLine> lines = List.of(
        new OrderLine("A", "A", 1, Money.of("1.00", "USD")),
        new OrderLine("B", "B", 1, Money.of("1.00", "EUR")));

    assertThrows(InvalidOrderException.class, () -> Order.place(1, 2, 3, lines));
  }
}

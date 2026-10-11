package examples.ch09.ordering;

import java.util.List;

/** What a buyer asks for: articles and quantities. Prices come from the assortment. */
public record PlaceOrder(long buyerId, long supplierId, List<PlaceOrder.Item> items) {

  public record Item(String articleId, int quantity) {
  }

  public PlaceOrder {
    items = List.copyOf(items);
  }
}

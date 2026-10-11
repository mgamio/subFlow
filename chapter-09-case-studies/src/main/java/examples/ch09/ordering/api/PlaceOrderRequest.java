package examples.ch09.ordering.api;

import examples.ch09.ordering.PlaceOrder;
import java.util.List;

/** The JSON body of POST /buyers/{buyerId}/orders. */
public record PlaceOrderRequest(long supplierId, List<PlaceOrder.Item> items) {
}

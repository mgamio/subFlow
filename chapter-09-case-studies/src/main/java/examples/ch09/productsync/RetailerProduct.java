package examples.ch09.productsync;

import examples.ch09.shared.Money;
import java.util.List;

/** A product as the retailer describes it, already in our terms: only valid images. */
public record RetailerProduct(String productId, String name, Money price, List<String> imageIds) {
  public RetailerProduct {
    imageIds = List.copyOf(imageIds);
  }
}

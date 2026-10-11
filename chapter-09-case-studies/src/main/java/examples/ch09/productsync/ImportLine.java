package examples.ch09.productsync;

import examples.ch09.shared.Money;

/** One record per image, for the marketplace's internal import process. */
public record ImportLine(String retailerId, String productId, String name, Money price,
    String imagePath) {
}

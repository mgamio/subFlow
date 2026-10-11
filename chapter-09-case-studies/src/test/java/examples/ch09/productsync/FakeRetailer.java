package examples.ch09.productsync;

import examples.ch09.shared.Money;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/** A retailer we control: products, missing images, outages. */
class FakeRetailer implements RetailerCatalog {
  private final Map<String, RetailerProduct> products = new HashMap<>();
  private final Set<String> missingImages = new HashSet<>();
  final List<String> imageDownloads = new ArrayList<>();
  String downAfterProduct;                 // simulates an outage mid-run

  FakeRetailer product(String id, String name, String price, String... imageIds) {
    products.put(id, new RetailerProduct(id, name, Money.of(price, "USD"), List.of(imageIds)));
    return this;
  }

  FakeRetailer missingImage(String imageId) {
    missingImages.add(imageId);
    return this;
  }

  void imageIsBack(String imageId) {
    missingImages.remove(imageId);
  }

  @Override
  public RetailerProduct product(String productId) {
    if (productId.equals(downAfterProduct)) {
      throw new RetailerUnavailableException("503 Service Unavailable");
    }
    RetailerProduct product = products.get(productId);
    if (product == null) {
      throw new NotFoundAtRetailerException("product " + productId + " not found");
    }
    return product;
  }

  @Override
  public byte[] image(String imageId) {
    if (missingImages.contains(imageId)) {
      throw new NotFoundAtRetailerException("image " + imageId + " not found");
    }
    imageDownloads.add(imageId);
    return ("jpeg:" + imageId).getBytes(StandardCharsets.UTF_8);
  }
}

package examples.ch09.productsync;

import java.util.regex.Pattern;

/**
 * Where an image lives: retailer/product/image.jpg. The ids come from another
 * company's system, so they are checked before they become part of a file path.
 */
public record ImageKey(String retailerId, String productId, String imageId) {
  private static final Pattern SAFE_ID = Pattern.compile("[A-Za-z0-9_-]{1,64}");

  public ImageKey {
    requireSafe(retailerId);
    requireSafe(productId);
    requireSafe(imageId);
  }

  public String path() {
    return retailerId + "/" + productId + "/" + imageId + ".jpg";
  }

  private static void requireSafe(String id) {
    if (id == null || !SAFE_ID.matcher(id).matches()) {
      throw new InvalidRetailerDataException("Unsafe id from retailer: " + id);
    }
  }
}

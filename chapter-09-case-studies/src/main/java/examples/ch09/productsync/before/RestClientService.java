package examples.ch09.productsync.before;

import java.util.List;

public interface RestClientService {
  List<ProductData> getProductData();

  byte[] getImage(int productId);
}

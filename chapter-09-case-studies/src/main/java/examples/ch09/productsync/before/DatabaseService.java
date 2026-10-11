package examples.ch09.productsync.before;

import java.util.List;

public interface DatabaseService {
  @SuppressWarnings("rawtypes")
  void saveProducts(List products);
}

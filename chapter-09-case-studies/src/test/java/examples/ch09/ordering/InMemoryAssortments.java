package examples.ch09.ordering;

import examples.ch09.shared.Money;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryAssortments implements Assortments {
  private final Map<String, ArticleOffer> offers = new HashMap<>();

  InMemoryAssortments offer(long buyerId, long supplierId, String articleId, String name,
      String price) {
    offers.put(key(buyerId, supplierId, articleId),
        new ArticleOffer(supplierId, articleId, name, Money.of(price, "USD")));
    return this;
  }

  @Override
  public Optional<ArticleOffer> offer(long buyerId, long supplierId, String articleId) {
    return Optional.ofNullable(offers.get(key(buyerId, supplierId, articleId)));
  }

  private static String key(long buyerId, long supplierId, String articleId) {
    return buyerId + "/" + supplierId + "/" + articleId;
  }
}

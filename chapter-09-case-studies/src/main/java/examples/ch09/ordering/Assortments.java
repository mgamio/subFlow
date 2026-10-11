package examples.ch09.ordering;

import java.util.Optional;

/** What each supplier offers each buyer. Empty if the buyer cannot order it. */
public interface Assortments {
  Optional<ArticleOffer> offer(long buyerId, long supplierId, String articleId);
}

package examples.ch09.ordering;

public class ArticleNotOfferedException extends RuntimeException {
  public ArticleNotOfferedException(long supplierId, String articleId) {
    super("Supplier " + supplierId + " does not offer article " + articleId
        + " to this buyer");
  }
}

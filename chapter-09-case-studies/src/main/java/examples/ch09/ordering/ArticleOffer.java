package examples.ch09.ordering;

import examples.ch09.shared.Money;

/** An article as a supplier offers it to one buyer today: name and price can change. */
public record ArticleOffer(long supplierId, String articleId, String name, Money unitPrice) {
}

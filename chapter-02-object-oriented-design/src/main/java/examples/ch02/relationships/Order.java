package examples.ch02.relationships;

/** Aggregation: the order refers to an address that lives on its own. */
public class Order {
  private final long supplierId;
  private final Address shippingAddress;

  public Order(long supplierId, Address shippingAddress) {
    this.supplierId = supplierId;
    this.shippingAddress = shippingAddress;
  }

  public long supplierId() { return supplierId; }
  public Address shippingAddress() { return shippingAddress; }
}

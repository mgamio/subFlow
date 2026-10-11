package examples.ch09.suppliers;

/** The questions a buyer asks about suppliers. */
public interface SupplierDirectory {
  Page<SupplierSummary> suppliers(SupplierQuery query);

  Page<CustomerNumber> customerNumbers(CustomerNumberQuery query);
}

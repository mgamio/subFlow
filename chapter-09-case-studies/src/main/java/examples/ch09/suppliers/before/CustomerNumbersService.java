package examples.ch09.suppliers.before;

import java.util.List;

public interface CustomerNumbersService {
  public List<CustomerNumber> listCustomerNumbers(
    Integer buyerId,
    Integer supplierId,
    String customerNumber,
    String sortBy,
    String sortOrder,
    Integer offset,
    Integer limit) throws Exception;
}

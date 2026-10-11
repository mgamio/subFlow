package examples.ch09.suppliers.before;

import java.util.List;

/** The first edition's interface: every new filter changes the signature. */
public interface SuppliersService {
  public List<Supplier> listSuppliers(
    Integer buyerId,
    String supplierNumber,
    String sortBy,
    String sortOrder,
    Integer offset,
    Integer limit) throws Exception;
}

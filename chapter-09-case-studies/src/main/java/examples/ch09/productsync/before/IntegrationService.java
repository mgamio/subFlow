package examples.ch09.productsync.before;

import java.util.List;

/** The first edition's design: services named after technologies. */
public class IntegrationService {
  private final DatabaseService databaseService;
  private final RestClientService restClientService;
  private final FileSystemService fileSystemService;

  public IntegrationService(DatabaseService databaseService,
      RestClientService restClientService, FileSystemService fileSystemService) {
    this.databaseService = databaseService;
    this.restClientService = restClientService;
    this.fileSystemService = fileSystemService;
  }

  public void integrationTask() {
    List<ProductData> products = restClientService.getProductData();  // all of them
    fileSystemService.createFolder();
    for (ProductData product : products) {
      byte[] image = restClientService.getImage(product.productId);
      fileSystemService.storeImage(image);
    }
    fileSystemService.createCsvFile(products);
    databaseService.saveProducts(products);
  }
}

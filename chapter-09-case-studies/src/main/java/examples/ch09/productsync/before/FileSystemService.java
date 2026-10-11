package examples.ch09.productsync.before;

import java.util.List;

public interface FileSystemService {
  void createFolder();

  void storeImage(byte[] image);

  @SuppressWarnings("rawtypes")
  void createCsvFile(List products);
}

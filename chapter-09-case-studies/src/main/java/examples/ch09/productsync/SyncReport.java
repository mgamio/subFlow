package examples.ch09.productsync;

import java.util.ArrayList;
import java.util.List;

/** What one run did. Sent to the team after every run. */
public class SyncReport {
  private int products;
  private int imagesStored;
  private int imagesAlreadyStored;
  private final List<String> failures = new ArrayList<>();

  void productSynced() { products++; }
  void imageStored() { imagesStored++; }
  void imageAlreadyStored() { imagesAlreadyStored++; }
  void failed(String reason) { failures.add(reason); }

  public int products() { return products; }
  public int imagesStored() { return imagesStored; }
  public int imagesAlreadyStored() { return imagesAlreadyStored; }
  public List<String> failures() { return List.copyOf(failures); }
}

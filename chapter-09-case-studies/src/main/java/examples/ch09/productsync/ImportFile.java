package examples.ch09.productsync;

/** Lines are invisible to the import process until publish() is called. */
public interface ImportFile {
  void add(ImportLine line);

  void publish();
}

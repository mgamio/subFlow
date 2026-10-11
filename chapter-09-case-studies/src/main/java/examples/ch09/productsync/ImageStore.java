package examples.ch09.productsync;

public interface ImageStore {
  boolean contains(ImageKey key);

  void store(ImageKey key, byte[] image);
}

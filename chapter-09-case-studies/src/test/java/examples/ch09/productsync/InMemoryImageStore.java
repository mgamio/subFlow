package examples.ch09.productsync;

import java.util.HashMap;
import java.util.Map;

class InMemoryImageStore implements ImageStore {
  final Map<String, byte[]> images = new HashMap<>();

  @Override
  public boolean contains(ImageKey key) {
    return images.containsKey(key.path());
  }

  @Override
  public void store(ImageKey key, byte[] image) {
    images.put(key.path(), image);
  }
}

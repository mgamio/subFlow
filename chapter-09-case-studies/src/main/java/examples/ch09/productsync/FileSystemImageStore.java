package examples.ch09.productsync;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Images on the file server, written so that a reader never sees half a file. */
public class FileSystemImageStore implements ImageStore {
  private final Path root;

  public FileSystemImageStore(Path root) {
    this.root = root;
  }

  @Override
  public boolean contains(ImageKey key) {
    return Files.exists(root.resolve(key.path()));
  }

  @Override
  public void store(ImageKey key, byte[] image) {
    Path target = root.resolve(key.path());
    try {
      Files.createDirectories(target.getParent());
      Path partial = target.resolveSibling(target.getFileName() + ".part");
      Files.write(partial, image);
      Files.move(partial, target, StandardCopyOption.ATOMIC_MOVE,
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new UncheckedIOException("Could not store " + key.path(), e);
    }
  }
}

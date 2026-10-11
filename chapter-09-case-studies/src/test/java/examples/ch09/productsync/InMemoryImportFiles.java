package examples.ch09.productsync;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Records what the import process would see: only published files. */
class InMemoryImportFiles implements ImportFiles {
  final List<List<ImportLine>> published = new ArrayList<>();

  @Override
  public ImportFile create(String retailerId, Instant runStartedAt) {
    List<ImportLine> lines = new ArrayList<>();
    return new ImportFile() {
      @Override
      public void add(ImportLine line) {
        lines.add(line);
      }

      @Override
      public void publish() {
        published.add(List.copyOf(lines));
      }
    };
  }
}

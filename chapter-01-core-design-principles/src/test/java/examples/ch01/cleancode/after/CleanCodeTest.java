package examples.ch01.cleancode.after;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CleanCodeTest {

  @Test
  void imageSyncValidatesAndFindsTheFirstId() {
    ImageSync sync = new ImageSync();
    String[] valid = {"id", "x", "Y", "Y", null, "LIX-1", "OIX-1"};
    String[] invalid = {"id", "x", "Y", "N", "AIX-1", null, null};

    assertTrue(sync.isValidImage(valid));
    assertEquals(Optional.of("LIX-1"), sync.firstImageId(valid));
    assertFalse(sync.isValidImage(invalid));
  }

  @Test
  void acmeSessionIsClosedEvenWhenATransferFails() {
    List<String> calls = new ArrayList<>();
    AcmeClient failing = new AcmeClient() {
      public void login() { calls.add("login"); }
      public void logout() { calls.add("logout"); }
      public void transferBuyerCoreData() { calls.add("core"); }
      public void transferBuyerStatusChanges() {
        throw new IllegalStateException("partner is down");
      }
      public void transferEvents() { calls.add("events"); }
    };

    assertThrows(IllegalStateException.class,
        () -> new AcmeSync(failing).sendToAcme(AcmeExport.BUYER_STATUS_CHANGES));
    assertEquals(List.of("login", "logout"), calls);
  }

  @Test
  void guardClausesRejectInvalidOrForbiddenImages() {
    ArticleImages images = new ArticleImages();
    assertNull(images.articleImageUrl(0, false, false));
    assertNull(images.articleImageUrl(5, true, false));
    assertEquals(ArticleImages.IMAGE_URL + 5, images.articleImageUrl(5, true, true));
    assertEquals(ArticleImages.IMAGE_URL + 5, images.articleImageUrl(5, false, false));
  }

  @Test
  void restrictedProvidersAreReadFromAFile(@TempDir Path dir) throws Exception {
    Path file = Files.writeString(dir.resolve("providers.txt"), "530636\n 36507 \n\n");
    RestrictedProviders providers = new FileRestrictedProviders(file);

    assertTrue(providers.contains("530636"));
    assertTrue(providers.contains("36507"));
    assertFalse(providers.contains("100"));
  }

  @Test
  void missingProvidersFileFailsLoudly(@TempDir Path dir) {
    assertThrows(NoSuchFileException.class,
        () -> new FileRestrictedProviders(dir.resolve("missing.txt")));
  }
}

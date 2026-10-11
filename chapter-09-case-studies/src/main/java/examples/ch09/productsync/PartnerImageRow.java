package examples.ch09.productsync;

import java.util.Optional;

/**
 * Chapter 1's image rules, now inside the retailer adapter. The rest of the job
 * never sees array positions or "Y" flags, only valid image ids.
 */
public final class PartnerImageRow {
  // Array positions defined in the partner's API documentation
  private static final int FIRST_VALIDITY_FLAG = 2;
  private static final int SECOND_VALIDITY_FLAG = 3;
  private static final int FIRST_IMAGE_ID = 4;
  private static final int LAST_IMAGE_ID = 6;
  private static final String VALID = "Y";

  private PartnerImageRow() {
  }

  public static Optional<String> validImageId(String[] values) {
    if (!VALID.equals(values[FIRST_VALIDITY_FLAG])
        || !VALID.equals(values[SECOND_VALIDITY_FLAG])) {
      return Optional.empty();
    }
    for (int i = FIRST_IMAGE_ID; i <= LAST_IMAGE_ID; i++) {
      if (values[i] != null) {
        return Optional.of(values[i]);
      }
    }
    return Optional.empty();
  }
}

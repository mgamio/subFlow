package examples.ch01.cleancode.after;

import java.util.Optional;

public class ImageSync {
  // Array positions defined in the partner's API documentation
  private static final int FIRST_VALIDITY_FLAG = 2;
  private static final int SECOND_VALIDITY_FLAG = 3;
  private static final int FIRST_IMAGE_ID = 4;
  private static final int LAST_IMAGE_ID = 6;
  private static final String VALID = "Y";

  public boolean isValidImage(String[] values) {
    return VALID.equals(values[FIRST_VALIDITY_FLAG])
        && VALID.equals(values[SECOND_VALIDITY_FLAG]);
  }

  public Optional<String> firstImageId(String[] values) {
    for (int i = FIRST_IMAGE_ID; i <= LAST_IMAGE_ID; i++) {
      if (values[i] != null) {
        return Optional.of(values[i]);
      }
    }
    return Optional.empty();
  }
}

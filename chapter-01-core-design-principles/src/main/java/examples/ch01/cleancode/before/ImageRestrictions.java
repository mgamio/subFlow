package examples.ch01.cleancode.before;

public class ImageRestrictions {

  /** Hard-coded providers AND a bug: == compares String references. */
  @SuppressWarnings("StringEquality")
  public boolean pictureIsRestricted(String providerId) {
    return providerId == "530636" || providerId == "36507";
  }
}

package examples.ch01.cleancode.before;

public class ImageSync {

  /** Validates AND retrieves: two jobs behind one name. */
  public String retrieveImageId(String[] values) {
    if (!values[2].equals("Y") || !values[3].equals("Y"))
      return null;
    String imageId = null;
    // get the first non-null value as the imageId
    if (values[4] != null) {
      imageId = values[4];        // imageAIXId
    } else if (values[5] != null) {
      imageId = values[5];        // imageLIXId
    } else if (values[6] != null) {
      imageId = values[6];        // imageOIXId
    }
    return imageId;
  }
}

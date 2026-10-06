package examples.ch01.cleancode.after;

public class ArticleImages {
  static final String IMAGE_URL = "https://images.example.com/";

  public String articleImageUrl(int imageId,
      boolean imageIsRestricted, boolean partnerCanSeeImage) {
    if (imageId <= 0) {
      return null;
    }
    if (imageIsRestricted && !partnerCanSeeImage) {
      return null;
    }
    return IMAGE_URL + imageId;
  }
}

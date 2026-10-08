package examples.ch03.lsp.shapes.before;

public class Square extends Rectangle {
  @Override
  public void setWidth(int width) {
    super.setWidth(width);
    super.setHeight(width);   // keep it square
  }

  @Override
  public void setHeight(int height) {
    super.setWidth(height);
    super.setHeight(height);  // keep it square
  }
}

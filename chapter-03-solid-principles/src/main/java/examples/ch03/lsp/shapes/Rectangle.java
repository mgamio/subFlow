package examples.ch03.lsp.shapes;

public record Rectangle(int width, int height) implements Shape {
  @Override
  public int area() {
    return width * height;
  }
}

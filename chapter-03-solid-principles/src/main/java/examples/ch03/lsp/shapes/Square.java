package examples.ch03.lsp.shapes;

public record Square(int side) implements Shape {
  @Override
  public int area() {
    return side * side;
  }
}

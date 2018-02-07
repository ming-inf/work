package pattern.strategy;

public class AddStrategy implements Strategy {
  @Override
  public int algorithm(int x, int y) {
    return x + y;
  }
}

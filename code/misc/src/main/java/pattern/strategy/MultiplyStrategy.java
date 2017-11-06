package pattern.strategy;

public class MultiplyStrategy implements Strategy {
	@Override
	public int algorithm(int x, int y) {
		return x * y;
	}
}

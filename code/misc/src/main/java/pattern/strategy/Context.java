package pattern.strategy;

public class Context {
	public Integer execute(int x, int y, Strategy strategy) {
		return strategy.algorithm(x, y);
	}
}

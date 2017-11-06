package pattern.state;

public class StateA implements State {
	@Override
	public State transition() {
		return new StateB();
	}
}

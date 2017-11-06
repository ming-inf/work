package pattern.state;

public class Context {
	State state;

	public Context() {
		state = new StateA();
	}

	public void execute() {
		state = state.transition();
	}

	public String getState() {
		return state.toString();
	}
}

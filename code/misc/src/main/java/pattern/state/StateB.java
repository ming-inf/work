package pattern.state;

public class StateB implements State {
  @Override
  public State transition() {
    return new StateA();
  }
}

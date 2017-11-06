package pattern.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
	private Integer state;
	private List<Observer<Integer>> observers = new ArrayList<>();

	public void attach(Observer<Integer> o) {
		observers.add(o);
	}

	public void detach(Observer<Integer> o) {
		observers.remove(o);
	}

	public void update(Integer state) {
		observers.forEach(listener -> listener.update(state));
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
		update(this.state);
	}
}

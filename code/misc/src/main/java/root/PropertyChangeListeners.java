package root;

import java.util.ArrayList;
import java.util.List;

public class PropertyChangeListeners<T> {
  List<PropertyChangeListener<T>> listeners = new ArrayList<>();

  public void add(PropertyChangeListener<T> listener) {
    listeners.add(listener);
  }

  public void remove(PropertyChangeListener<T> listener) {
    listeners.remove(listener);
  }

  public void firePropertyChange(T oldValue, T newValue) {
    PropertyChangeEvent<T> event = new PropertyChangeEvent<T>(oldValue, newValue);
    fire(event);
  }

  private void fire(PropertyChangeEvent<T> event) {
    for (PropertyChangeListener<T> listener : listeners) {
      listener.propertyChange(event);
    }
  }
}

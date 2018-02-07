package root;

public class SimpleObjectProperty<T> {
  T value;
  PropertyChangeListeners<T> listeners = new PropertyChangeListeners<>();

  public SimpleObjectProperty() {
  }

  public SimpleObjectProperty(T init) {
    value = init;
  }

  public T get() {
    return value;
  }

  public void set(T value) {
    T oldValue = this.value;
    this.value = value;
    listeners.firePropertyChange(oldValue, value);
  }

  public void addListener(PropertyChangeListener<T> listener) {
    listeners.add(listener);
  }

  public void removeListener(PropertyChangeListener<T> listener) {
    listeners.remove(listener);
  }
}

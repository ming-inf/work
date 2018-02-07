package root;

public class PropertyChangeEvent<T> {
  T oldValue;
  T newValue;

  public PropertyChangeEvent(T oldValue, T newValue) {
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public T getOldValue() {
    return oldValue;
  }

  public T getNewValue() {
    return newValue;
  }
}

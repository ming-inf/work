package root;

public interface PropertyChangeListener<T> {
  void propertyChange(PropertyChangeEvent<T> event);
}

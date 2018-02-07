package structure.api;

import java.util.List;

public interface Tree<T> extends Collection<T> {
  int height();

  List<T> preorder();

  List<T> postorder();

  List<T> inorder();

  List<T> breadthFirst();
}

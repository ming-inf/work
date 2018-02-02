package structure.api;

import java.util.List;

public interface Tree<T> extends Collection<T> {
	public int height();

	public List<T> preorder();

	public List<T> postorder();

	public List<T> inorder();

	public List<T> breadthFirst();
}

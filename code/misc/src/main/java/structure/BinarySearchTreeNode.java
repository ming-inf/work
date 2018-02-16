package structure;

public class BinarySearchTreeNode<T extends Comparable<T>>
    extends BinaryTreeNode<T>
    implements Comparable<BinaryTreeNode<T>> {
  BinarySearchTreeNode<T> left;
  BinarySearchTreeNode<T> right;
  int height;

  public BinarySearchTreeNode(T value) {
    super(value);
    this.height = 1;
  }

  @Override
  public int compareTo(BinaryTreeNode<T> o) {
    return value.compareTo(o.value);
  }
}

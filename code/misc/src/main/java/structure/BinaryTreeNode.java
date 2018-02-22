package structure;

import java.util.Objects;

import static java.util.Objects.isNull;

class BinaryTreeNode<T> {
  T value;
  BinaryTreeNode<T> parent;
  BinaryTreeNode<T> left;
  BinaryTreeNode<T> right;

  public BinaryTreeNode(T value) {
    this(value, null, null, null);
  }

  public BinaryTreeNode(T value, BinaryTreeNode<T> parent, BinaryTreeNode<T> left, BinaryTreeNode<T> right) {
    this.value = value;
    this.parent = parent;
    this.left = left;
    this.right = right;
  }

  public static int leavesSize(BinaryTreeNode<?> current) {
    if (isNull(current.left) && isNull(current.right)) {
      return 1;
    }

    return leavesSize(current.left) + leavesSize(current.right);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BinaryTreeNode<?> that = (BinaryTreeNode<?>) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {

    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "BinaryTreeNode [value=" + value + ", parent=" + parent + ", left=" + left + ", right=" + right + "]";
  }
}

package structure;

import static java.util.Objects.isNull;

class BinaryTreeNode<S> {
  S value;
  BinaryTreeNode<S> parent;
  BinaryTreeNode<S> left;
  BinaryTreeNode<S> right;

  public BinaryTreeNode(S value) {
    this(value, null, null, null);
  }

  public BinaryTreeNode(S value, BinaryTreeNode<S> parent, BinaryTreeNode<S> left, BinaryTreeNode<S> right) {
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
  public String toString() {
    return "BinaryTreeNode [value=" + value + ", parent=" + parent + ", left=" + left + ", right=" + right + "]";
  }
}

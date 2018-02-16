package structure;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class IntervalTree extends AVLTree<IntervalNode> {
  void leftRotation(BinarySearchTreeNode<IntervalNode> current) {
    BinarySearchTreeNode<IntervalNode> rightNode = current.right;
    super.leftRotation(current);

    updateMax(current);
    updateMax(rightNode);
  }

  void rightRotation(BinarySearchTreeNode<IntervalNode> current) {
    BinarySearchTreeNode<IntervalNode> leftNode = current.left;
    super.rightRotation(current);

    updateMax(current);
    updateMax(leftNode);
  }

  void insertNode(BinarySearchTreeNode<IntervalNode> current, BinarySearchTreeNode<IntervalNode> value) {
    super.insertNode(current, value);
  }

  private void updateMax(BinarySearchTreeNode<IntervalNode> current) {
    if (nonNull(current.left)) {
      current.value.max = Math.max(current.value.high, current.left.value.max);
    }
    if (nonNull(current.right)) {
      current.value.max = Math.max(current.value.max, current.right.value.max);
    }
  }

  @Override
  public boolean remove(IntervalNode value) {
    if (isNull(value) || isNull(root)) {
      return false;
    }

    Tuple3<BinarySearchTreeNode<IntervalNode>, BinarySearchTreeNode<IntervalNode>, List<BinarySearchTreeNode<IntervalNode>>> parentCurrentPath = searchNodeWithPath(null, root, new BinarySearchTreeNode<>(value), new ArrayList<>());
    BinarySearchTreeNode<IntervalNode> parent = parentCurrentPath.parent;
    BinarySearchTreeNode<IntervalNode> target = parentCurrentPath.target;
    List<BinarySearchTreeNode<IntervalNode>> path = parentCurrentPath.path;
    if (isNull(target)) {
      return false;
    }

    BinarySearchTreeNode<IntervalNode> left = target.left;
    BinarySearchTreeNode<IntervalNode> right = target.right;

    boolean isParentNull = isNull(parent);
    boolean isBothNull = isNull(left) && isNull(right);
    boolean isLeftOnly = nonNull(left) && isNull(right);
    boolean isRightOnly = isNull(left) && nonNull(right);
    boolean isBothNonNull = nonNull(left) && nonNull(right);

    if (!isParentNull) {
      boolean isLeftChild = parent.left == target;
      BinarySearchTreeNode<IntervalNode> newChild;
      if (isBothNull) {
        newChild = null;
      } else if (isLeftOnly) {
        newChild = left;
      } else if (isRightOnly) {
        newChild = right;
      } else {
        newChild = isLeftChild ? parent.left : parent.right;
      }

      if (isLeftChild) {
        parent.left = newChild;
      } else {
        parent.right = newChild;
      }
      updateMax(parent);
    } else {
      if (isBothNull) {
        root = null;
      } else if (isLeftOnly) {
        root.value = left.value;
        left = null;
      } else if (isRightOnly) {
        root.value = right.value;
        right = null;
      }
    }

    if (isBothNonNull) {
      BinarySearchTreeNode<IntervalNode> largest = left;
      while (nonNull(largest.right)) {
        largest = largest.right;
      }
      BinarySearchTreeNode<IntervalNode> largestParent = searchNode(target, target.left, largest).parent;
      if (largestParent != target) {
        largestParent.right = null;
      }
      target.value = largest.value;
    }

    while (!path.isEmpty()) {
      BinarySearchTreeNode<IntervalNode> last = path.remove(path.size() - 1);
      updateHeight(last);
      rebalance(last);
      updateMax(last);
    }

    return true;
  }

  public boolean intersect(IntervalNode i) {
    BinarySearchTreeNode<IntervalNode> x = root;
    while (nonNull(x) && (x.value.high < i.low || i.high < x.value.low)) {
      if (nonNull(x.left) && i.low <= x.left.value.max) {
        x = x.left;
      } else {
        x = x.right;
      }
    }
    return nonNull(x);
  }
}

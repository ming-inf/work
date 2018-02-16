package structure;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class IntervalTree extends AVLTree<Interval> {
  void leftRotation(BinarySearchTreeNode<Interval> current) {
    BinarySearchTreeNode<Interval> rightNode = current.right;
    super.leftRotation(current);

    updateMax(current);
    updateMax(rightNode);
  }

  void rightRotation(BinarySearchTreeNode<Interval> current) {
    BinarySearchTreeNode<Interval> leftNode = current.left;
    super.rightRotation(current);

    updateMax(current);
    updateMax(leftNode);
  }

  void insertNode(BinarySearchTreeNode<Interval> current, BinarySearchTreeNode<Interval> value) {
    super.insertNode(current, value);
  }

  private void updateMax(BinarySearchTreeNode<Interval> current) {
    if (nonNull(current.left)) {
      current.value.max = Math.max(current.value.high, current.left.value.max);
    }
    if (nonNull(current.right)) {
      current.value.max = Math.max(current.value.max, current.right.value.max);
    }
  }

  @Override
  public boolean remove(Interval value) {
    if (isNull(value) || isNull(root)) {
      return false;
    }

    Tuple3<BinarySearchTreeNode<Interval>, BinarySearchTreeNode<Interval>, List<BinarySearchTreeNode<Interval>>> parentCurrentPath = searchNodeWithPath(null, root, new BinarySearchTreeNode<>(value), new ArrayList<>());
    BinarySearchTreeNode<Interval> parent = parentCurrentPath.parent;
    BinarySearchTreeNode<Interval> target = parentCurrentPath.target;
    List<BinarySearchTreeNode<Interval>> path = parentCurrentPath.path;
    if (isNull(target)) {
      return false;
    }

    BinarySearchTreeNode<Interval> left = target.left;
    BinarySearchTreeNode<Interval> right = target.right;

    boolean isParentNull = isNull(parent);
    boolean isBothNull = isNull(left) && isNull(right);
    boolean isLeftOnly = nonNull(left) && isNull(right);
    boolean isRightOnly = isNull(left) && nonNull(right);
    boolean isBothNonNull = nonNull(left) && nonNull(right);

    if (!isParentNull) {
      boolean isLeftChild = parent.left == target;
      BinarySearchTreeNode<Interval> newChild;
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
      BinarySearchTreeNode<Interval> largest = left;
      while (nonNull(largest.right)) {
        largest = largest.right;
      }
      BinarySearchTreeNode<Interval> largestParent = searchNode(target, target.left, largest).parent;
      if (largestParent != target) {
        largestParent.right = null;
      }
      target.value = largest.value;
    }

    while (!path.isEmpty()) {
      BinarySearchTreeNode<Interval> last = path.remove(path.size() - 1);
      updateHeight(last);
      rebalance(last);
      updateMax(last);
    }

    return true;
  }

  public boolean intersect(Interval i) {
    BinarySearchTreeNode<Interval> x = root;
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

class Interval implements Comparable<Interval> {
  int low;
  int high;
  int max;

  public Interval(int low, int high) {
    this.low = low;
    this.high = high;
    this.max = high;
  }

  @Override
  public int compareTo(Interval o) {
    if (low != o.low) {
      return low - o.low;
    } else {
      return high - o.high;
    }
  }
}
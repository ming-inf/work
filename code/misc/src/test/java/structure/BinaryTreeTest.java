package structure;

import com.google.common.base.Functions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import structure.api.Tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BinaryTreeTest {
  private Tree<Integer> objectUnderTest;

  @Before
  public void setup() {
    objectUnderTest = new BinaryTree<>();
  }

  @Test
  public void testCustomTree() {
    TreeNode<Integer> root = new TreeNode<>(0);
    TreeNode<Integer> left = new TreeNode<>(1);
    TreeNode<Integer> right = new TreeNode<>(2);
    TreeNode<Integer> rightRight = new TreeNode<>(3);
    right.right = rightRight;
    root.left = left;
    root.right = right;

    objectUnderTest = new BinaryTree<>(root);

    assertEquals(Arrays.asList(0, 1, 2, 3), objectUnderTest.preorder());
    assertEquals(Arrays.asList(1, 3, 2, 0), objectUnderTest.postorder());
    assertEquals(Arrays.asList(1, 0, 2, 3), objectUnderTest.inorder());
    assertEquals(Arrays.asList(0, 1, 2, 3), objectUnderTest.breadthFirst());
  }

  @Test
  public void testSearchNull() {
    assertFalse(objectUnderTest.contains(null));
  }

  @Test
  public void testSearchNotFoundEmpty() {
    assertFalse(objectUnderTest.contains(0));
  }

  @Test
  public void testSearchNotFound() {
    objectUnderTest.add(0);

    assertFalse(objectUnderTest.contains(1));
  }

  @Test
  public void testSearchFound() {
    objectUnderTest.add(0);

    assertTrue(objectUnderTest.contains(0));
  }

  @Test
  public void testSearchNotFoundLeft() {
    objectUnderTest.add(0);
    objectUnderTest.add(-1);

    assertFalse(objectUnderTest.contains(-2));
  }

  @Test
  public void testSearchNotFoundRight() {
    objectUnderTest.add(0);
    objectUnderTest.add(1);

    assertFalse(objectUnderTest.contains(2));
  }

  @Test
  public void testSearchNotFoundBoth() {
    objectUnderTest.add(0);
    objectUnderTest.add(-1);
    objectUnderTest.add(1);

    assertFalse(objectUnderTest.contains(-2));
  }

  @Test
  public void testInsertNull() {
    objectUnderTest.add(null);
  }

  @Test
  public void testInsert() {
    objectUnderTest.add(0);

    assertFalse(objectUnderTest.contains(-1));
    assertTrue(objectUnderTest.contains(0));
    assertFalse(objectUnderTest.contains(1));
  }

  @Test
  public void testInsertFull() {
    Integer[] intArray = {0, 1, 2, 3, 4, 5, 6};
    List<Integer> ints = Arrays.asList(intArray);

    for (int i : ints) {
      objectUnderTest.add(i);
    }

    assertEquals(Arrays.asList(0, 1, 3, 4, 2, 5, 6), objectUnderTest.preorder());
    assertEquals(Arrays.asList(3, 4, 1, 5, 6, 2, 0), objectUnderTest.postorder());
    assertEquals(Arrays.asList(3, 1, 4, 0, 5, 2, 6), objectUnderTest.inorder());
    assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6), objectUnderTest.breadthFirst());
  }

  @Test
  public void testDeleteNull() {
    assertFalse(objectUnderTest.remove(null));
  }

  @Test
  public void testDeleteEmpty() {
    assertFalse(objectUnderTest.remove(0));
  }

  @Test
  public void testDeleteNotFound() {
    objectUnderTest.add(0);

    assertFalse(objectUnderTest.remove(1));
  }

  @Test
  public void testDeleteRootLeaf() {
    objectUnderTest.add(0);

    assertTrue(objectUnderTest.remove(0));
    assertFalse(objectUnderTest.contains(0));
  }

  @Test
  public void testDeleteRootWithLeft() {
    objectUnderTest.add(0);
    objectUnderTest.add(-1);

    assertTrue(objectUnderTest.remove(0));
    assertFalse(objectUnderTest.contains(0));
  }

  @Test
  public void testDeleteRootWithRight() {
    objectUnderTest.add(0);
    objectUnderTest.add(1);

    assertTrue(objectUnderTest.remove(0));
    assertFalse(objectUnderTest.contains(0));
  }

  @Test
  public void testDeleteRootWithBoth() {
    objectUnderTest.add(0);
    objectUnderTest.add(1);
    objectUnderTest.add(-1);

    assertTrue(objectUnderTest.remove(0));
    assertFalse(objectUnderTest.contains(0));
    assertTrue(objectUnderTest.contains(1));
    assertTrue(objectUnderTest.contains(-1));
  }

  @Test
  public void testDeleteNonRootLeftLeaf() {
    objectUnderTest.add(0);
    objectUnderTest.add(-1);

    assertTrue(objectUnderTest.remove(-1));
    assertFalse(objectUnderTest.contains(-1));
  }

  @Test
  public void testDeleteNonRootLeftWithLeft() {
    objectUnderTest.add(0);
    objectUnderTest.add(-1);
    objectUnderTest.add(-2);

    assertTrue(objectUnderTest.remove(-1));
    assertFalse(objectUnderTest.contains(-1));
    assertTrue(objectUnderTest.contains(-2));
  }

  @Test
  public void testDeleteNonRootLeftWithRight() {
    objectUnderTest.add(0);
    objectUnderTest.add(-2);
    objectUnderTest.add(-1);

    assertTrue(objectUnderTest.remove(-2));
    assertFalse(objectUnderTest.contains(-2));
    assertTrue(objectUnderTest.contains(-1));
  }

  @Test
  public void testDeleteNonRootLeftWithBoth() {
    objectUnderTest.add(0);
    objectUnderTest.add(-2);
    objectUnderTest.add(-3);
    objectUnderTest.add(-1);

    assertTrue(objectUnderTest.remove(-2));
    assertFalse(objectUnderTest.contains(-2));
    assertTrue(objectUnderTest.contains(-1));
    assertTrue(objectUnderTest.contains(-3));
  }

  @Test
  public void testDeleteNonRootRightLeaf() {
    objectUnderTest.add(0);
    objectUnderTest.add(1);

    assertTrue(objectUnderTest.remove(1));
    assertFalse(objectUnderTest.contains(1));
  }

  @Test
  public void testDeleteNonRootRightWithLeft() {
    objectUnderTest.add(0);
    objectUnderTest.add(2);
    objectUnderTest.add(1);

    assertTrue(objectUnderTest.remove(2));
    assertFalse(objectUnderTest.contains(2));
    assertTrue(objectUnderTest.contains(1));
  }

  @Test
  public void testDeleteNonRootRightWithRight() {
    objectUnderTest.add(0);
    objectUnderTest.add(1);
    objectUnderTest.add(2);

    assertTrue(objectUnderTest.remove(1));
    assertFalse(objectUnderTest.contains(1));
    assertTrue(objectUnderTest.contains(2));
  }

  @Test
  public void testDeleteNonRootRightWithBoth() {
    objectUnderTest.add(0);
    objectUnderTest.add(2);
    objectUnderTest.add(1);
    objectUnderTest.add(3);

    assertTrue(objectUnderTest.remove(2));
    assertFalse(objectUnderTest.contains(2));
    assertTrue(objectUnderTest.contains(1));
    assertTrue(objectUnderTest.contains(3));
  }

  @Test
  public void testDeleteNonRootLeft() {
    objectUnderTest.add(0);
    objectUnderTest.add(-5);
    objectUnderTest.add(-8);
    objectUnderTest.add(-7);
    objectUnderTest.add(-4);

    assertTrue(objectUnderTest.remove(-5));
    assertFalse(objectUnderTest.contains(-5));
    assertTrue(objectUnderTest.contains(-8));
    assertTrue(objectUnderTest.contains(-7));
  }

  @Test
  public void testSerialize() {
    TreeNode<Character> a = new TreeNode<>('a');
    TreeNode<Character> b = new TreeNode<>('b');
    TreeNode<Character> c = new TreeNode<>('c');
    TreeNode<Character> d = new TreeNode<>('d');
    TreeNode<Character> e = new TreeNode<>('e');
    TreeNode<Character> f = new TreeNode<>('f');
    TreeNode<Character> g = new TreeNode<>('g');
    a.left = b;
    a.right = c;
    b.left = d;
    b.right = e;
    e.left = g;
    c.right = f;
    BinaryTree<Character> objectUnderTest = new BinaryTree<>(a);

    String expected = "abd!!eg!!!c!f!!";
    Assert.assertEquals(expected, objectUnderTest.serialize(Functions.identity()));
  }

  @Test
  public void testDeserialize() {
    BinaryTree<Character> objectUnderTest = new BinaryTree<>();
    objectUnderTest.deserialize("abd!!eg!!!c!f!!", Functions.identity());

    List<Character> expected = Arrays.asList('a', 'b', 'd', 'e', 'g', 'c', 'f');
    Assert.assertEquals(expected, objectUnderTest.preorder());
  }

  @Test
  public void testTraversePreorder() {
    Integer[] intArray = {23, 14, 7, 9, 17, 31};
    List<Integer> ints = Arrays.asList(intArray);

    for (int i : ints) {
      objectUnderTest.add(i);
    }

    List<Integer> expected = Arrays.asList(23, 14, 9, 17, 7, 31);
    List<Integer> actualIntArray = objectUnderTest.preorder();

    assertEquals(expected, actualIntArray);
  }

  @Test
  public void testTraversePostorder() {
    Integer[] intArray = {23, 14, 7, 9, 17, 31};
    List<Integer> ints = Arrays.asList(intArray);

    for (int i : ints) {
      objectUnderTest.add(i);
    }

    List<Integer> expected = Arrays.asList(9, 17, 14, 31, 7, 23);
    List<Integer> actualIntArray = objectUnderTest.postorder();

    assertEquals(expected, actualIntArray);
  }

  @Test
  public void testTraverseInorder() {
    Integer[] intArray = {23, 14, 7, 9, 17, 31};
    List<Integer> ints = Arrays.asList(intArray);

    for (int i : ints) {
      objectUnderTest.add(i);
    }

    List<Integer> expected = Arrays.asList(9, 14, 17, 23, 31, 7);
    List<Integer> actualIntArray = objectUnderTest.inorder();

    assertEquals(expected, actualIntArray);
  }

  @Test
  public void testTraverseBreadthFirstNull() {
    List<Integer> expected = Collections.emptyList();
    List<Integer> actualIntArray = objectUnderTest.breadthFirst();

    assertEquals(expected, actualIntArray);
  }

  @Test
  public void testTraverseBreadthFirst() {
    Integer[] intArray = {23, 14, 7, 9, 17, 31};
    List<Integer> ints = Arrays.asList(intArray);

    for (int i : ints) {
      objectUnderTest.add(i);
    }

    List<Integer> expected = Arrays.asList(23, 14, 7, 9, 17, 31);
    List<Integer> actualIntArray = objectUnderTest.breadthFirst();

    assertEquals(expected, actualIntArray);
  }

  @Test
  public void testHeight() {
    objectUnderTest.add(0);
    objectUnderTest.add(-2);
    objectUnderTest.add(-1);
    objectUnderTest.add(-3);
    objectUnderTest.add(2);
    objectUnderTest.add(1);
    objectUnderTest.add(3);

    assertEquals(3, objectUnderTest.height());
  }

  @Test
  public void testToUI1Null() {
    Assert.assertEquals("", ((BinaryTree) objectUnderTest).toUI1());
  }

  @Test
  public void testToUI1NoChildren() {
    objectUnderTest.add(1);

    Assert.assertEquals("1", ((BinaryTree) objectUnderTest).toUI1());
  }

  @Test
  public void testToUI1BothChildren() {
    objectUnderTest.add(1);
    objectUnderTest.add(0);
    objectUnderTest.add(2);

    Assert.assertEquals("1┬0\n └2", ((BinaryTree) objectUnderTest).toUI1());
  }

  @Test
  public void testToUI1MultipleLevels() {
    objectUnderTest.add(1);
    objectUnderTest.add(2);
    objectUnderTest.add(3);
    objectUnderTest.add(4);
    objectUnderTest.add(5);
    objectUnderTest.add(6);
    objectUnderTest.add(7);

    Assert.assertEquals("1┬2┬4\n │ └5\n └3┬6\n   └7", ((BinaryTree) objectUnderTest).toUI1());
  }

  @Test
  public void testToUI1MultipleLevelsNonFull() {
    objectUnderTest.add(1);
    objectUnderTest.add(2);
    objectUnderTest.add(3);
    objectUnderTest.add(4);
    objectUnderTest.add(5);

    Assert.assertEquals("1┬2┬4\n │ └5\n └3", ((BinaryTree) objectUnderTest).toUI1());
  }

  @Test
  public void testToUI2Null() {
    Assert.assertEquals("", ((BinaryTree) objectUnderTest).toUI2());
  }

  @Test
  public void testToUI2NoChildren() {
    objectUnderTest.add(1);

    Assert.assertEquals("1", ((BinaryTree) objectUnderTest).toUI2());
  }

  @Test
  public void testToUI2BothChildren() {
    objectUnderTest.add(1);
    objectUnderTest.add(0);
    objectUnderTest.add(2);

    Assert.assertEquals("1┐\n ├0\n └2", ((BinaryTree) objectUnderTest).toUI2());
  }

  @Test
  public void testToUI2MultipleLevels() {
    objectUnderTest.add(1);
    objectUnderTest.add(2);
    objectUnderTest.add(3);
    objectUnderTest.add(4);
    objectUnderTest.add(5);
    objectUnderTest.add(6);
    objectUnderTest.add(7);

    Assert.assertEquals("1┐\n ├2┐\n │ ├4\n │ └5\n └3┐\n   ├6\n   └7", ((BinaryTree) objectUnderTest).toUI2());
  }

  @Test
  public void testToUI2MultipleLevelsNonFull() {
    objectUnderTest.add(1);
    objectUnderTest.add(2);
    objectUnderTest.add(3);
    objectUnderTest.add(4);
    objectUnderTest.add(5);

    Assert.assertEquals("1┐\n ├2┐\n │ ├4\n │ └5\n └3", ((BinaryTree) objectUnderTest).toUI2());
  }

  @Test
  public void testToUI3Null() {
    Assert.assertEquals("", ((BinaryTree) objectUnderTest).toUI3(1));
  }

  @Test
  public void testToUI3NoChildren() {
    objectUnderTest.add(1);

    Assert.assertEquals("1", ((BinaryTree) objectUnderTest).toUI3(1));
  }

  @Test
  public void testToUI3BothChildren() {
    objectUnderTest.add(1);
    objectUnderTest.add(0);
    objectUnderTest.add(2);

    Assert.assertEquals("1\n├0\n└2", ((BinaryTree) objectUnderTest).toUI3(1));
  }

  @Test
  public void testToUI3MultipleLevels() {
    objectUnderTest.add(1);
    objectUnderTest.add(2);
    objectUnderTest.add(3);
    objectUnderTest.add(4);
    objectUnderTest.add(5);
    objectUnderTest.add(6);
    objectUnderTest.add(7);

    Assert.assertEquals("1\n├2\n│├4\n│└5\n└3\n ├6\n └7", ((BinaryTree) objectUnderTest).toUI3(1));
  }

  @Test
  public void testToUI3MultipleLevelsNonFull() {
    objectUnderTest.add(1);
    objectUnderTest.add(2);
    objectUnderTest.add(3);
    objectUnderTest.add(4);
    objectUnderTest.add(5);

    Assert.assertEquals("1\n├2\n│├4\n│└5\n└3", ((BinaryTree) objectUnderTest).toUI3(1));
  }

  @Test
  public void testToUI3Padding() {
    for (int i = 1; i <= 7; i++) {
      objectUnderTest.add(i);
    }

    Assert.assertEquals(" 1\n├ 2\n│├ 4\n│└ 5\n└ 3\n ├ 6\n └ 7", ((BinaryTree) objectUnderTest).toUI3(2));
  }

  @Test
  public void testFromUI3Null() {
    Assert.assertNull(BinaryTree.fromUI3((String) null, Integer::parseInt));
  }

  @Test
  public void testFromUI3Empty() {
    Assert.assertNull(BinaryTree.fromUI3("", Integer::parseInt));
  }

  @Test
  public void testFromUI3NoChildren() {
    Assert.assertTrue(BinaryTree.fromUI3("1", Integer::parseInt).contains(1));
  }

  @Test
  public void testFromUI3BothChildren() {
    BinaryTree<Integer> tree = BinaryTree.fromUI3("0\n├1\n└2", Integer::parseInt);
    Assert.assertTrue(tree.contains(0));
    Assert.assertTrue(tree.contains(1));
    Assert.assertTrue(tree.contains(2));
  }

  @Test
  public void testFromUI3MultipleLevels() {
    BinaryTree<Integer> tree = BinaryTree.fromUI3("1\n├2\n│├4\n│└5\n└3\n ├6\n └7", Integer::parseInt);
    for (int i = 1; i <= 7; i++) {
      Assert.assertTrue(tree.contains(i));
    }
  }

  @Test
  public void testFromUI3MultipleLevelsNonFull() {
    BinaryTree<Integer> tree = BinaryTree.fromUI3("1\n├2\n│├4\n│└5\n└3", Integer::parseInt);
    for (int i = 1; i <= 5; i++) {
      Assert.assertTrue(tree.contains(i));
    }
  }

  @Test
  public void testFromUI3OneChild() {
    BinaryTree<Integer> tree = BinaryTree.fromUI3("1\n└2", Integer::parseInt);
    for (int i = 1; i <= 2; i++) {
      Assert.assertTrue(tree.contains(i));
    }
  }

  @Test
  public void testFromUI3Padding() {
    BinaryTree<Integer> tree = BinaryTree.fromUI3("  0\n├  1\n│└  3\n│ └  6\n└  2\n ├  4\n │└  7\n └  5\n  ├  8\n  └  9", s -> Integer.parseInt(s.trim()));
    for (int i = 1; i <= 9; i++) {
      Assert.assertTrue(tree.contains(i));
    }
  }
}

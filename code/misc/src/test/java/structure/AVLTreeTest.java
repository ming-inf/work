package structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AVLTreeTest extends TreeTest {
	@Before
	public void setup() {
		objectUnderTest = new AVLTree<>();
	}

	@Test
	public void testInsertAvlLeftRotate() {
		objectUnderTest.add(1);
		objectUnderTest.add(2);
		objectUnderTest.add(3);
		objectUnderTest.add(4);
		objectUnderTest.add(5);
		objectUnderTest.add(6);

		assertEquals(3, objectUnderTest.height());
	}

	@Test
	public void testInsertAvlRightRotate() {
		objectUnderTest.add(6);
		objectUnderTest.add(5);
		objectUnderTest.add(4);
		objectUnderTest.add(3);
		objectUnderTest.add(2);
		objectUnderTest.add(1);

		assertEquals(3, objectUnderTest.height());
	}

	@Test
	public void testDeleteNonRootLeftWithLeft() {
		objectUnderTest.add(0);
		objectUnderTest.add(1);
		objectUnderTest.add(-1);
		objectUnderTest.add(-2);

		assertTrue(objectUnderTest.remove(-1));
		assertFalse(objectUnderTest.contains(-1));
		assertTrue(objectUnderTest.contains(-2));
	}

	@Test
	public void testDeleteNonRootLeftWithRight() {
		objectUnderTest.add(0);
		objectUnderTest.add(1);
		objectUnderTest.add(-2);
		objectUnderTest.add(-1);

		assertTrue(objectUnderTest.remove(-2));
		assertFalse(objectUnderTest.contains(-2));
		assertTrue(objectUnderTest.contains(-1));
	}

	@Test
	public void testDeleteNonRootLeftWithBoth() {
		objectUnderTest.add(0);
		objectUnderTest.add(1);
		objectUnderTest.add(-2);
		objectUnderTest.add(-1);
		objectUnderTest.add(-3);

		assertTrue(objectUnderTest.remove(-2));
		assertFalse(objectUnderTest.contains(-2));
		assertTrue(objectUnderTest.contains(-1));
		assertTrue(objectUnderTest.contains(-3));
	}

	@Test
	public void testDeleteNonRootRightWithLeft() {
		objectUnderTest.add(0);
		objectUnderTest.add(-1);
		objectUnderTest.add(2);
		objectUnderTest.add(1);

		assertTrue(objectUnderTest.remove(2));
		assertFalse(objectUnderTest.contains(2));
		assertTrue(objectUnderTest.contains(1));
	}

	@Test
	public void testDeleteNonRootRightWithRight() {
		objectUnderTest.add(0);
		objectUnderTest.add(-1);
		objectUnderTest.add(1);
		objectUnderTest.add(2);

		assertTrue(objectUnderTest.remove(1));
		assertFalse(objectUnderTest.contains(1));
		assertTrue(objectUnderTest.contains(2));
	}

	@Test
	public void testDeleteNonRootRightWithBoth() {
		objectUnderTest.add(0);
		objectUnderTest.add(-1);
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
		objectUnderTest.add(1);
		objectUnderTest.add(-2);
		objectUnderTest.add(2);
		objectUnderTest.add(-1);
		objectUnderTest.add(-4);
		objectUnderTest.add(-3);

		assertTrue(objectUnderTest.remove(-2));
		assertFalse(objectUnderTest.contains(-2));
		assertTrue(objectUnderTest.contains(-4));
		assertTrue(objectUnderTest.contains(-3));
	}

	@Test
	public void testLeftRightRotation() {
		objectUnderTest.add(0);
		objectUnderTest.add(1);
		objectUnderTest.add(-2);
		objectUnderTest.add(-1);

		assertTrue(objectUnderTest.remove(1));
		assertFalse(objectUnderTest.contains(1));
	}

	@Test
	public void testRightLeftRotation() {
		objectUnderTest.add(0);
		objectUnderTest.add(-1);
		objectUnderTest.add(2);
		objectUnderTest.add(1);

		assertTrue(objectUnderTest.remove(-1));
		assertFalse(objectUnderTest.contains(-1));
	}

	@Test
	public void testTraversePreorder() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> expected = Arrays.asList(14, 7, 9, 23, 17, 31);
		List<Integer> actualIntArray = objectUnderTest.preorder();

		assertEquals(expected, actualIntArray);
	}

	@Test
	public void testTraversePostorder() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> expected = Arrays.asList(9, 7, 17, 31, 23, 14);
		List<Integer> actualIntArray = objectUnderTest.postorder();

		assertEquals(expected, actualIntArray);
	}

	@Test
	public void testTraverseInorder() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> expected = Arrays.asList(7, 9, 14, 17, 23, 31);
		List<Integer> actualIntArray = objectUnderTest.inorder();

		assertEquals(expected, actualIntArray);
	}

	@Test
	public void testTraverseBreadthFirst() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> expected = Arrays.asList(14, 7, 23, 9, 17, 31);
		List<Integer> actualIntArray = objectUnderTest.breadthFirst();

		assertEquals(expected, actualIntArray);
	}

	@Test
	public void testHeight() {
		objectUnderTest.add(0);
		objectUnderTest.add(-1);
		objectUnderTest.add(1);
		objectUnderTest.add(-3);
		objectUnderTest.add(-2);
		objectUnderTest.add(1);
		objectUnderTest.add(3);

		assertEquals(3, objectUnderTest.height());
	}
}

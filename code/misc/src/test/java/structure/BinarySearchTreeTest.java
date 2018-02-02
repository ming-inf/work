package structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import structure.api.Tree;

public class BinarySearchTreeTest {
	private Tree<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new BinarySearchTree<>();
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
	public void testTraversePreorder() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> actualIntArray = objectUnderTest.preorder();

		assertEquals(ints, actualIntArray);
	}

	@Test
	public void testTraversePostorder() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> expected = Arrays.asList(9, 7, 17, 14, 31, 23);
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
	public void testTraverseBreadthFirstNull() {
		List<Integer> expected = Collections.emptyList();
		List<Integer> actualIntArray = objectUnderTest.breadthFirst();

		assertEquals(expected, actualIntArray);
	}

	@Test
	public void testTraverseBreadthFirst() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> expected = Arrays.asList(23, 14, 31, 7, 17, 9);
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
}

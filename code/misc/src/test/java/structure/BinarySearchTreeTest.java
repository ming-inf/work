package structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BinarySearchTreeTest {
	Tree<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new BinarySearchTree<>();
	}

	@Test
	public void testSearchNull() {
		assertFalse(objectUnderTest.search(null));
	}

	@Test
	public void testSearchNotFoundEmpty() {
		assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testSearchNotFound() {
		objectUnderTest.insert(0);

		assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testSearchFound() {
		objectUnderTest.insert(0);

		assertTrue(objectUnderTest.search(0));
	}

	@Test
	public void testSearchNotFoundLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);

		assertFalse(objectUnderTest.search(-2));
	}

	@Test
	public void testSearchNotFoundRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		assertFalse(objectUnderTest.search(2));
	}

	@Test
	public void testSearchNotFoundBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(1);

		assertFalse(objectUnderTest.search(-2));
	}

	@Test
	public void testInsertNull() {
		objectUnderTest.insert(null);
	}

	@Test
	public void testInsert() {
		objectUnderTest.insert(0);

		assertFalse(objectUnderTest.search(-1));
		assertTrue(objectUnderTest.search(0));
		assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteNull() {
		assertFalse(objectUnderTest.delete(null));
	}

	@Test
	public void testDeleteEmpty() {
		assertFalse(objectUnderTest.delete(0));
	}

	@Test
	public void testDeleteNotFound() {
		objectUnderTest.insert(0);

		assertFalse(objectUnderTest.delete(1));
	}

	@Test
	public void testDeleteRootLeaf() {
		objectUnderTest.insert(0);

		assertTrue(objectUnderTest.delete(0));
		assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteRootWithLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);

		assertTrue(objectUnderTest.delete(0));
		assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteRootWithRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		assertTrue(objectUnderTest.delete(0));
		assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteRootWithBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(-1);

		assertTrue(objectUnderTest.delete(0));
		assertFalse(objectUnderTest.search(0));
		assertTrue(objectUnderTest.search(1));
		assertTrue(objectUnderTest.search(-1));
	}

	@Test
	public void testDeleteNonRootLeftLeaf() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);

		assertTrue(objectUnderTest.delete(-1));
		assertFalse(objectUnderTest.search(-1));
	}

	@Test
	public void testDeleteNonRootLeftWithLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(-2);

		assertTrue(objectUnderTest.delete(-1));
		assertFalse(objectUnderTest.search(-1));
		assertTrue(objectUnderTest.search(-2));
	}

	@Test
	public void testDeleteNonRootLeftWithRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-2);
		objectUnderTest.insert(-1);

		assertTrue(objectUnderTest.delete(-2));
		assertFalse(objectUnderTest.search(-2));
		assertTrue(objectUnderTest.search(-1));
	}

	@Test
	public void testDeleteNonRootLeftWithBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-2);
		objectUnderTest.insert(-3);
		objectUnderTest.insert(-1);

		assertTrue(objectUnderTest.delete(-2));
		assertFalse(objectUnderTest.search(-2));
		assertTrue(objectUnderTest.search(-1));
		assertTrue(objectUnderTest.search(-3));
	}

	@Test
	public void testDeleteNonRootRightLeaf() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		assertTrue(objectUnderTest.delete(1));
		assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteNonRootRightWithLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(2);
		objectUnderTest.insert(1);

		assertTrue(objectUnderTest.delete(2));
		assertFalse(objectUnderTest.search(2));
		assertTrue(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteNonRootRightWithRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(2);

		assertTrue(objectUnderTest.delete(1));
		assertFalse(objectUnderTest.search(1));
		assertTrue(objectUnderTest.search(2));
	}

	@Test
	public void testDeleteNonRootRightWithBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(2);
		objectUnderTest.insert(1);
		objectUnderTest.insert(3);

		assertTrue(objectUnderTest.delete(2));
		assertFalse(objectUnderTest.search(2));
		assertTrue(objectUnderTest.search(1));
		assertTrue(objectUnderTest.search(3));
	}

	@Test
	public void testDeleteNonRootLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-5);
		objectUnderTest.insert(-8);
		objectUnderTest.insert(-7);
		objectUnderTest.insert(-4);

		assertTrue(objectUnderTest.delete(-5));
		assertFalse(objectUnderTest.search(-5));
		assertTrue(objectUnderTest.search(-8));
		assertTrue(objectUnderTest.search(-7));
	}

	@Test
	public void testTraversePreorder() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		List<Integer> actualIntArray = objectUnderTest.preorder();

		assertEquals(ints, actualIntArray);
	}

	@Test
	public void testTraversePostorder() {
		Integer[] intArray = { 23, 14, 7, 9, 17, 31 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
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
			objectUnderTest.insert(i);
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
			objectUnderTest.insert(i);
		}

		List<Integer> expected = Arrays.asList(23, 14, 31, 7, 17, 9);
		List<Integer> actualIntArray = objectUnderTest.breadthFirst();

		assertEquals(expected, actualIntArray);
	}

	@Test
	public void testHeight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-2);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(-3);
		objectUnderTest.insert(2);
		objectUnderTest.insert(1);
		objectUnderTest.insert(3);

		assertEquals(3, objectUnderTest.height());
	}
}

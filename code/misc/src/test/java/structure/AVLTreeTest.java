package structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AVLTreeTest {
	Tree<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new AVLTree<>();
	}

	@Test
	public void testSearchNull() {
		Assert.assertFalse(objectUnderTest.search(null));
	}

	@Test
	public void testSearchNotFoundEmpty() {
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testSearchNotFound() {
		objectUnderTest.insert(0);

		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testSearchFound() {
		objectUnderTest.insert(0);

		Assert.assertTrue(objectUnderTest.search(0));
	}

	@Test
	public void testSearchNotFoundLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);

		Assert.assertFalse(objectUnderTest.search(-2));
	}

	@Test
	public void testSearchNotFoundRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		Assert.assertFalse(objectUnderTest.search(2));
	}

	@Test
	public void testSearchNotFoundBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(1);

		Assert.assertFalse(objectUnderTest.search(-2));
	}

	@Test
	public void testInsertNull() {
		objectUnderTest.insert(null);
	}

	@Test
	public void testInsert() {
		objectUnderTest.insert(0);

		Assert.assertFalse(objectUnderTest.search(-1));
		Assert.assertTrue(objectUnderTest.search(0));
		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testInsertAvlLeftRotate() {
		objectUnderTest.insert(1);
		objectUnderTest.insert(2);
		objectUnderTest.insert(3);
		objectUnderTest.insert(4);
		objectUnderTest.insert(5);
		objectUnderTest.insert(6);

		Assert.assertEquals(3, objectUnderTest.height());
	}

	@Test
	public void testInsertAvlRightRotate() {
		objectUnderTest.insert(6);
		objectUnderTest.insert(5);
		objectUnderTest.insert(4);
		objectUnderTest.insert(3);
		objectUnderTest.insert(2);
		objectUnderTest.insert(1);

		Assert.assertEquals(3, objectUnderTest.height());
	}

	@Test
	public void testDeleteNull() {
		Assert.assertFalse(objectUnderTest.delete(null));
	}

	@Test
	public void testDeleteEmpty() {
		Assert.assertFalse(objectUnderTest.delete(0));
	}

	@Test
	public void testDeleteNotFound() {
		objectUnderTest.insert(0);

		Assert.assertFalse(objectUnderTest.delete(1));
	}

	@Test
	public void testDeleteRootLeaf() {
		objectUnderTest.insert(0);

		Assert.assertTrue(objectUnderTest.delete(0));
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteRootWithLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);

		Assert.assertTrue(objectUnderTest.delete(0));
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteRootWithRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		Assert.assertTrue(objectUnderTest.delete(0));
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteRootWithBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(-1);

		Assert.assertTrue(objectUnderTest.delete(0));
		Assert.assertFalse(objectUnderTest.search(0));
		Assert.assertTrue(objectUnderTest.search(1));
		Assert.assertTrue(objectUnderTest.search(-1));
	}

	@Test
	public void testDeleteNonRootLeftLeaf() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);

		Assert.assertTrue(objectUnderTest.delete(-1));
		Assert.assertFalse(objectUnderTest.search(-1));
	}

	@Test
	public void testDeleteNonRootLeftWithLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(-2);

		Assert.assertTrue(objectUnderTest.delete(-1));
		Assert.assertFalse(objectUnderTest.search(-1));
		Assert.assertTrue(objectUnderTest.search(-2));
	}

	@Test
	public void testDeleteNonRootLeftWithRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(-2);
		objectUnderTest.insert(-1);

		Assert.assertTrue(objectUnderTest.delete(-2));
		Assert.assertFalse(objectUnderTest.search(-2));
		Assert.assertTrue(objectUnderTest.search(-1));
	}

	@Test
	public void testDeleteNonRootLeftWithBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(-2);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(-3);

		Assert.assertTrue(objectUnderTest.delete(-2));
		Assert.assertFalse(objectUnderTest.search(-2));
		Assert.assertTrue(objectUnderTest.search(-1));
		Assert.assertTrue(objectUnderTest.search(-3));
	}

	@Test
	public void testDeleteNonRootRightLeaf() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		Assert.assertTrue(objectUnderTest.delete(1));
		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteNonRootRightWithLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(2);
		objectUnderTest.insert(1);

		Assert.assertTrue(objectUnderTest.delete(2));
		Assert.assertFalse(objectUnderTest.search(2));
		Assert.assertTrue(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteNonRootRightWithRight() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(1);
		objectUnderTest.insert(2);

		Assert.assertTrue(objectUnderTest.delete(1));
		Assert.assertFalse(objectUnderTest.search(1));
		Assert.assertTrue(objectUnderTest.search(2));
	}

	@Test
	public void testDeleteNonRootRightWithBoth() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(2);
		objectUnderTest.insert(1);
		objectUnderTest.insert(3);

		Assert.assertTrue(objectUnderTest.delete(2));
		Assert.assertFalse(objectUnderTest.search(2));
		Assert.assertTrue(objectUnderTest.search(1));
		Assert.assertTrue(objectUnderTest.search(3));
	}

	@Test
	public void testDeleteNonRootLeft() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(-2);
		objectUnderTest.insert(2);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(-4);
		objectUnderTest.insert(-3);

		Assert.assertTrue(objectUnderTest.delete(-2));
		Assert.assertFalse(objectUnderTest.search(-2));
		Assert.assertTrue(objectUnderTest.search(-4));
		Assert.assertTrue(objectUnderTest.search(-3));
	}

	@Test
	public void testLeftRightRotation() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);
		objectUnderTest.insert(-2);
		objectUnderTest.insert(-1);

		Assert.assertTrue(objectUnderTest.delete(1));
		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testRightLeftRotation() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(-1);
		objectUnderTest.insert(2);
		objectUnderTest.insert(1);

		Assert.assertTrue(objectUnderTest.delete(-1));
		Assert.assertFalse(objectUnderTest.search(-1));
	}
}

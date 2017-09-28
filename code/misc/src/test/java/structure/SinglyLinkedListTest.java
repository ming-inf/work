package structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SinglyLinkedListTest {
	List<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new SinglyLinkedList<>();
	}

	@Test
	public void testSearchNull() {
		Assert.assertFalse(objectUnderTest.search(null));
	}

	@Test
	public void testSearchEmpty() {
		Assert.assertFalse(objectUnderTest.search(-1));
		Assert.assertFalse(objectUnderTest.search(0));
		Assert.assertFalse(objectUnderTest.search(1));
		Assert.assertFalse(objectUnderTest.search(5742));
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
		Assert.assertFalse(objectUnderTest.search(5742));
	}

	@Test
	public void testDeleteNull() {
		Assert.assertFalse(objectUnderTest.delete(null));
	}

	@Test
	public void testDeleteEmpty() {
		Assert.assertFalse(objectUnderTest.search(1));
		Assert.assertFalse(objectUnderTest.delete(1));
		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteNotFound() {
		objectUnderTest.insert(0);

		Assert.assertFalse(objectUnderTest.search(1));
		Assert.assertFalse(objectUnderTest.delete(1));
		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteFound() {
		objectUnderTest.insert(0);

		Assert.assertTrue(objectUnderTest.search(0));
		Assert.assertTrue(objectUnderTest.delete(0));
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteFoundHead() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		Assert.assertTrue(objectUnderTest.search(0));
		Assert.assertTrue(objectUnderTest.delete(0));
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteFoundLast() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		Assert.assertTrue(objectUnderTest.search(1));
		Assert.assertTrue(objectUnderTest.delete(1));
		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testDeleteThenInsert() {
		objectUnderTest.insert(0);
		objectUnderTest.insert(1);

		Assert.assertTrue(objectUnderTest.delete(1));

		objectUnderTest.insert(2);

		Assert.assertTrue(objectUnderTest.search(0));
		Assert.assertFalse(objectUnderTest.search(1));
		Assert.assertTrue(objectUnderTest.search(2));
	}

	@Test
	public void testTraverse() {
		Integer[] intArray = { 7, 34, 57, 775, 9679, 78, 677, 27 };

		for (int i : intArray) {
			objectUnderTest.insert(i);
		}

		Integer[] actualIntArray = objectUnderTest.traverse(Integer.class);

		Assert.assertArrayEquals(intArray, actualIntArray);
	}

	@Test
	public void testReverseTraverse() {
		Integer[] intArray = { 7, 34, 57, 775, 9679, 78, 677, 27 };

		for (int i : intArray) {
			objectUnderTest.insert(i);
		}

		Integer[] actualIntArray = objectUnderTest.reverseTraverse(Integer.class);

		Assert.assertArrayEquals(reverse(intArray), actualIntArray);
	}

	private Integer[] reverse(Integer[] array) {
		for (int i = 0; i < array.length / 2; i++) {
			int iPrime = array.length - 1 - i;
			array[i] = array[iPrime] - array[i];
			array[iPrime] = array[iPrime] - array[i];
			array[i] = array[iPrime] + array[i];
		}
		return array;
	}
}

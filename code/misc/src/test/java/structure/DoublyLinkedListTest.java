package structure;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DoublyLinkedListTest {
	List<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new DoublyLinkedList<>();
	}

	@Test
	public void testSearchNull() {
		assertFalse(objectUnderTest.contains(null));
	}

	@Test
	public void testSearchEmpty() {
		assertFalse(objectUnderTest.contains(-1));
		assertFalse(objectUnderTest.contains(0));
		assertFalse(objectUnderTest.contains(1));
		assertFalse(objectUnderTest.contains(5742));
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
		assertFalse(objectUnderTest.contains(5742));
	}

	@Test
	public void testDeleteNull() {
		assertFalse(objectUnderTest.remove(null));
	}

	@Test
	public void testDeleteEmpty() {
		assertFalse(objectUnderTest.contains(1));
		assertFalse(objectUnderTest.remove(1));
		assertFalse(objectUnderTest.contains(1));
	}

	@Test
	public void testDeleteNotFound() {
		objectUnderTest.add(0);

		assertFalse(objectUnderTest.contains(1));
		assertFalse(objectUnderTest.remove(1));
		assertFalse(objectUnderTest.contains(1));
	}

	@Test
	public void testDeleteFound() {
		objectUnderTest.add(0);

		assertTrue(objectUnderTest.contains(0));
		assertTrue(objectUnderTest.remove(0));
		assertFalse(objectUnderTest.contains(0));
	}

	@Test
	public void testDeleteFoundHead() {
		objectUnderTest.add(0);
		objectUnderTest.add(1);

		assertTrue(objectUnderTest.contains(0));
		assertTrue(objectUnderTest.remove(0));
		assertFalse(objectUnderTest.contains(0));
	}

	@Test
	public void testDeleteFoundLast() {
		objectUnderTest.add(0);
		objectUnderTest.add(1);

		assertTrue(objectUnderTest.contains(1));
		assertTrue(objectUnderTest.remove(1));
		assertFalse(objectUnderTest.contains(1));
	}

	@Test
	public void testDeleteThenInsert() {
		objectUnderTest.add(0);
		objectUnderTest.add(1);

		assertTrue(objectUnderTest.remove(1));

		objectUnderTest.add(2);

		assertTrue(objectUnderTest.contains(0));
		assertFalse(objectUnderTest.contains(1));
		assertTrue(objectUnderTest.contains(2));
	}

	@Test
	public void testTraverse() {
		Integer[] intArray = { 7, 34, 57, 775, 9679, 78, 677, 27 };

		for (int i : intArray) {
			objectUnderTest.add(i);
		}

		Integer[] actualIntArray = objectUnderTest.traverse(Integer.class);

		assertArrayEquals(intArray, actualIntArray);
	}

	@Test
	public void testReverseTraverse() {
		Integer[] intArray = { 7, 34, 57, 775, 9679, 78, 677, 27 };

		for (int i : intArray) {
			objectUnderTest.add(i);
		}

		Integer[] actualIntArray = objectUnderTest.reverseTraverse(Integer.class);

		assertArrayEquals(reverse(intArray), actualIntArray);
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

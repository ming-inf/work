package structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class HeapTest {
	Heap<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new Heap<>();
	}

	@Test
	public void testSearchNull() {
		assertFalse(objectUnderTest.contains(null));
		assertFalse(objectUnderTest.contains(0));
	}

	@Test
	public void testSearchNotFound() {
		Integer[] intArray = { 0, 2, 3, 4, 5, 6 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		assertFalse(objectUnderTest.contains(1));
	}

	@Test
	public void testInsertNull() {
		objectUnderTest.add(null);
	}

	@Test
	public void testInsert() {
		Integer[] intArray = { 8, 19, 12, 1 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		assertTrue(objectUnderTest.contains(8));
		assertTrue(objectUnderTest.contains(19));
		assertTrue(objectUnderTest.contains(12));
		assertTrue(objectUnderTest.contains(1));
		assertFalse(objectUnderTest.contains(0));
	}

	@Test
	public void testDeleteEmpty() {
		assertFalse(objectUnderTest.delete());
	}

	@Test
	public void testDeleteOnly() {
		objectUnderTest.add(0);

		assertTrue(objectUnderTest.delete());
	}

	@Test
	public void testDelete() {
		Integer[] intArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		assertTrue(objectUnderTest.delete());
		assertFalse(objectUnderTest.contains(0));
		assertTrue(objectUnderTest.delete());
		assertFalse(objectUnderTest.contains(1));
		assertTrue(objectUnderTest.delete());
		assertFalse(objectUnderTest.contains(2));
		assertTrue(objectUnderTest.delete());
		assertFalse(objectUnderTest.contains(3));
		assertTrue(objectUnderTest.contains(4));
	}

	@Test
	public void testPeek() {
		assertNull(objectUnderTest.peek());

		objectUnderTest.add(0);

		assertEquals((Integer) 0, objectUnderTest.peek());
	}

	@Test
	public void testTraverse() {
		Integer[] intArray = { 3, 9, 12, 7, 1 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.add(i);
		}

		List<Integer> expected = Arrays.asList(1, 3, 12, 9, 7);
		List<Integer> actualIntArray = objectUnderTest.traverse();

		assertEquals(expected, actualIntArray);
	}
}

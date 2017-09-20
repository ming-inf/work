package structure;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
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
		Assert.assertFalse(objectUnderTest.search(null));
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testSearchNotFound() {
		Integer[] intArray = { 0, 2, 3, 4, 5, 6 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		Assert.assertFalse(objectUnderTest.search(1));
	}

	@Test
	public void testInsertNull() {
		objectUnderTest.insert(null);
	}

	@Test
	public void testInsert() {
		Integer[] intArray = { 8, 19, 12, 1 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		Assert.assertTrue(objectUnderTest.search(8));
		Assert.assertTrue(objectUnderTest.search(19));
		Assert.assertTrue(objectUnderTest.search(12));
		Assert.assertTrue(objectUnderTest.search(1));
		Assert.assertFalse(objectUnderTest.search(0));
	}

	@Test
	public void testDeleteEmpty() {
		Assert.assertFalse(objectUnderTest.delete());
	}

	@Test
	public void testDeleteOnly() {
		objectUnderTest.insert(0);

		Assert.assertTrue(objectUnderTest.delete());
	}

	@Test
	public void testDelete() {
		Integer[] intArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		Assert.assertTrue(objectUnderTest.delete());
		Assert.assertFalse(objectUnderTest.search(0));
		Assert.assertTrue(objectUnderTest.delete());
		Assert.assertFalse(objectUnderTest.search(1));
		Assert.assertTrue(objectUnderTest.delete());
		Assert.assertFalse(objectUnderTest.search(2));
		Assert.assertTrue(objectUnderTest.delete());
		Assert.assertFalse(objectUnderTest.search(3));
		Assert.assertTrue(objectUnderTest.search(4));
	}

	@Test
	public void testPeek() {
		Assert.assertNull(objectUnderTest.peek());

		objectUnderTest.insert(0);

		Assert.assertEquals((Integer) 0, objectUnderTest.peek());
	}

	@Test
	public void testTraverse() {
		Integer[] intArray = { 3, 9, 12, 7, 1 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		List<Integer> expected = Arrays.asList(1, 3, 12, 9, 7);
		List<Integer> actualIntArray = objectUnderTest.traverse();

		Assert.assertEquals(expected, actualIntArray);
	}
}

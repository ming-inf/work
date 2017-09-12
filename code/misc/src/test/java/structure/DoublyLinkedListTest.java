package structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DoublyLinkedListTest {
	DoublyLinkedList<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new DoublyLinkedList<>();
	}

	@Test
	public void testSearchNotFound() {
		Assert.assertFalse(objectUnderTest.search(-1));
		Assert.assertFalse(objectUnderTest.search(0));
		Assert.assertFalse(objectUnderTest.search(1));
		Assert.assertFalse(objectUnderTest.search(5742));
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
	public void testDeleteNotFound() {
		objectUnderTest.insert(0);

		Assert.assertFalse(objectUnderTest.delete(1));
	}

	@Test
	public void testDeleteFound() {
		objectUnderTest.insert(0);

		Assert.assertTrue(objectUnderTest.delete(0));
	}

	@Test
	public void testIterator() {
		Integer[] intArray = { 7, 34, 57, 775, 9679, 78, 677, 27 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		List<Integer> actualIntArray = objectUnderTest.traverse();

		Collections.reverse(actualIntArray);
		Assert.assertEquals(ints, actualIntArray);
	}

	@Test
	public void testReverseIterator() {
		Integer[] intArray = { 7, 34, 57, 775, 9679, 78, 677, 27 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		List<Integer> actualIntArray = objectUnderTest.reverseTraverse();

		Assert.assertEquals(ints, actualIntArray);
	}
}

package structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SinglyLinkedListTest {
	SinglyLinkedList<Integer> objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new SinglyLinkedList<>();
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
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		List<Integer> actualIntArray = objectUnderTest.traverse();

		Assert.assertEquals(ints, actualIntArray);
	}

	@Test
	public void testReverseTraverse() {
		Integer[] intArray = { 7, 34, 57, 775, 9679, 78, 677, 27 };
		List<Integer> ints = Arrays.asList(intArray);

		for (int i : ints) {
			objectUnderTest.insert(i);
		}

		List<Integer> actualIntArray = objectUnderTest.reverseTraverse();

		Collections.reverse(actualIntArray);
		Assert.assertEquals(ints, actualIntArray);
	}
}

package algorithm.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SortTest {
	Sort<Integer> objectUnderTest;
	Map<List<Integer>, List<Integer>> testData;

	@Before
	public void initTestData() {
		testData = new HashMap<>();

		testData.put(Arrays.asList(4, 75, 74, 2, 54), Arrays.asList(2, 4, 54, 74, 75));
		testData.put(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		testData.put(Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		testData.put(Arrays.asList(4, 3, 2, 1, 4, 3, 2, 1), Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4));
	}

	@Test
	public void testBubble() {
		objectUnderTest = new Bubble<>();

		for (Entry<List<Integer>, List<Integer>> test : testData.entrySet()) {
			List<Integer> data = test.getKey();
			objectUnderTest.sort(data);
			Assert.assertThat(data, CoreMatchers.is(test.getValue()));
		}
	}

	@Test
	public void testBubbleQuickExit() {
		objectUnderTest = new BubbleQuickExit<>();

		for (Entry<List<Integer>, List<Integer>> test : testData.entrySet()) {
			List<Integer> data = test.getKey();
			objectUnderTest.sort(data);
			Assert.assertThat(data, CoreMatchers.is(test.getValue()));
		}
	}

	@Test
	public void testInsertion() {
		objectUnderTest = new Insertion<>();

		for (Entry<List<Integer>, List<Integer>> test : testData.entrySet()) {
			List<Integer> data = test.getKey();
			objectUnderTest.sort(data);
			Assert.assertThat(data, CoreMatchers.is(test.getValue()));
		}
	}

	@Test
	public void testMerge() {
		objectUnderTest = new Merge<>();

		for (Entry<List<Integer>, List<Integer>> test : testData.entrySet()) {
			List<Integer> data = test.getKey();
			objectUnderTest.sort(data);
			Assert.assertThat(data, CoreMatchers.is(test.getValue()));
		}
	}

	@Test
	public void testQuick() {
		objectUnderTest = new Quick<>();

		for (Entry<List<Integer>, List<Integer>> test : testData.entrySet()) {
			List<Integer> data = test.getKey();
			objectUnderTest.sort(data);
			Assert.assertThat(data, CoreMatchers.is(test.getValue()));
		}
	}

	@Test
	public void testShell() {
		objectUnderTest = new Shell<>();

		for (Entry<List<Integer>, List<Integer>> test : testData.entrySet()) {
			List<Integer> data = test.getKey();
			objectUnderTest.sort(data);
			Assert.assertThat(data, CoreMatchers.is(test.getValue()));
		}
	}
}

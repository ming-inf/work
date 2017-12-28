package structure;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

public class GraphTest {
	Graph objectUnderTest;

	@Test
	public void testAllTopologicalSort() {
		objectUnderTest = new Graph();

		String wordsString = "0\n1\n2\n3\n4\n5\n\n5 2\n5 0\n4 0\n4 1\n2 3\n3 1";
		objectUnderTest.fromString(wordsString);
		java.util.List<String> all = objectUnderTest.allTopologicalSort();
		Collections.sort(all);

		String expected = "4 5 0 2 3 1\n" +
				"4 5 2 0 3 1\n" +
				"4 5 2 3 0 1\n" +
				"4 5 2 3 1 0\n" +
				"5 2 3 4 0 1\n" +
				"5 2 3 4 1 0\n" +
				"5 2 4 0 3 1\n" +
				"5 2 4 3 0 1\n" +
				"5 2 4 3 1 0\n" +
				"5 4 0 2 3 1\n" +
				"5 4 2 0 3 1\n" +
				"5 4 2 3 0 1\n" +
				"5 4 2 3 1 0";
		Assert.assertEquals(expected, String.join("\n", all));
	}

	@Test
	public void testAllTopologicalSort2() {
		objectUnderTest = new Graph();

		String wordsString = "0\n1\n2\n\n0 2\n1 2";
		objectUnderTest.fromString(wordsString);
		java.util.List<String> all = objectUnderTest.allTopologicalSort();
		Collections.sort(all);

		String expected = "0 1 2\n" +
				"1 0 2";
		Assert.assertEquals(expected, String.join("\n", all));
	}

	@Test
	public void testAllTopologicalSortCycle() {
		objectUnderTest = new Graph();

		String wordsString = "0\n1\n2\n\n0 1\n1 2\n2 0";
		objectUnderTest.fromString(wordsString);
		Assert.assertTrue(objectUnderTest.allTopologicalSort().isEmpty());
	}

	@Test
	public void testTopologicalSort() {
		objectUnderTest = new Graph();

		String wordsString = "0\n1\n2\n3\n4\n5\n\n5 2\n5 0\n4 0\n4 1\n2 3\n3 1";
		objectUnderTest.fromString(wordsString);
		java.util.List<String> all = objectUnderTest.allTopologicalSort();
		Assert.assertTrue(all.contains(objectUnderTest.topologicalSort()));
	}

	@Test
	public void testNotCyclic() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n\n";
		objectUnderTest.fromString(wordsString);
		Assert.assertFalse(objectUnderTest.isCyclic());
	}

	@Test
	public void testNotCyclicTwoNodes() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n1 b\n\n0 1";
		objectUnderTest.fromString(wordsString);
		Assert.assertFalse(objectUnderTest.isCyclic());
	}

	@Test
	public void testNotCyclicMultiRoot() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n1 b\n2 c\n\n0 2\n1 2";
		objectUnderTest.fromString(wordsString);
		Assert.assertFalse(objectUnderTest.isCyclic());
	}

	@Test
	public void testCyclic() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n\n0 0";
		objectUnderTest.fromString(wordsString);
		Assert.assertTrue(objectUnderTest.isCyclic());
	}

	@Test
	public void testCyclicTwoNodes() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n1 b\n\n0 1\n1 0";
		objectUnderTest.fromString(wordsString);
		Assert.assertTrue(objectUnderTest.isCyclic());
	}

	@Test
	public void testCyclicMultiNode() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n1 b\n2 c\n3 d\n\n0 1\n1 2\n2 3\n3 0";
		objectUnderTest.fromString(wordsString);
		Assert.assertTrue(objectUnderTest.isCyclic());
	}

	@Test
	public void testMultipleRoot() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n1 b\n2 c\n\n0 2\n1 2";
		objectUnderTest.fromString(wordsString);
	}

	@Test
	public void testDepthFirst() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n1 b\n2 c\n3 d\n4 e\n5 f\n6 g\n\n0 1\n0 2\n1 3\n4 1\n6 4\n5 6\n5 2\n6 0";
		objectUnderTest.fromString(wordsString);
	}

	@Test
	public void testGraph() {
		objectUnderTest = new Graph();

		String wordsString = "0 foo\n1 bar\n2 bat\n\n0 1\n0 2\n1 2";
		objectUnderTest.fromString(wordsString);
	}
}

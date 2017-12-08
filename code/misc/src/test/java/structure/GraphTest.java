package structure;

import org.junit.Test;

public class GraphTest {
	Graph objectUnderTest;

	@Test
	public void testDepthFirst() {
		objectUnderTest = new Graph();

		String wordsString = "0 a\n1 b\n2 c\n3 d\n4 e\n5 f\n6 g\n\n0 1\n0 2\n1 3\n4 1\n6 4\n5 6\n5 2\n6 0";
		objectUnderTest.fromString(wordsString);
		System.out.println(objectUnderTest.toString());
	}

	@Test
	public void testGraph() {
		objectUnderTest = new Graph();

		String wordsString = "0 foo\n1 bar\n2 bat\n\n0 1\n0 2\n1 2";
		objectUnderTest.fromString(wordsString);
		System.out.println(objectUnderTest.toString());
	}
}

package structure;

import org.junit.Test;

public class GraphTest {
	Graph objectUnderTest;
	
	@Test
	public void testGraph() {
		objectUnderTest = new Graph();

		String wordsString = "0 foo\n1 bar\n2 bat\n\n0 1\n0 2\n1 2";
		objectUnderTest.fromString(wordsString);
		System.out.println(objectUnderTest.toString());
	}
}

package algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class MiscTest {
	Misc<String> objectUnderTest;

	@Test
	public void testPowerSet() {
		objectUnderTest = new Misc<String>();

		String elements = "a b c d";
		Set<String> set = new HashSet<>(Arrays.asList(elements.split(" ")));
		Assert.assertEquals(16, objectUnderTest.powerset(set).size());
	}
}

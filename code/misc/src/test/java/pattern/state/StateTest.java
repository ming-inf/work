package pattern.state;

import org.junit.Assert;
import org.junit.Test;

public class StateTest {
	Context objectUnderTest;

	@Test
	public void testState() {
		objectUnderTest = new Context();
		Assert.assertTrue(objectUnderTest.getState().contains("StateA"));

		objectUnderTest.execute();
		Assert.assertTrue(objectUnderTest.getState().contains("StateB"));
	}
}

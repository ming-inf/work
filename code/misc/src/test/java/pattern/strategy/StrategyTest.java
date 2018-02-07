package pattern.strategy;

import org.junit.Assert;
import org.junit.Test;

public class StrategyTest {
  Context objectUnderTest;

  @Test
  public void testStrategy() {
    objectUnderTest = new Context();
    Assert.assertEquals((Integer) 5, objectUnderTest.execute(2, 3, new AddStrategy()));
    Assert.assertEquals((Integer) 6, objectUnderTest.execute(2, 3, new MultiplyStrategy()));
  }
}

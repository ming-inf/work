package algorithm;

import org.junit.Assert;
import org.junit.Test;

public class TreeNaturalNumberTest {
  TreeNaturalNumber objectUnderTest = new TreeNaturalNumber();

  @Test
  public void testP() {
    Assert.assertEquals(-1, objectUnderTest.pInv(4));
    Assert.assertEquals(-1, objectUnderTest.pInv(6));
    Assert.assertEquals(-1, objectUnderTest.pInv(9));

    Assert.assertEquals(1, objectUnderTest.pInv(objectUnderTest.p(1)));
    Assert.assertEquals(2, objectUnderTest.pInv(objectUnderTest.p(2)));
    Assert.assertEquals(9999, objectUnderTest.pInv(objectUnderTest.p(9999)));
    Assert.assertEquals(10000, objectUnderTest.pInv(objectUnderTest.p(10000)));

    Assert.assertEquals(2, objectUnderTest.p(objectUnderTest.pInv(2)));
    Assert.assertEquals(3, objectUnderTest.p(objectUnderTest.pInv(3)));
    Assert.assertEquals(5, objectUnderTest.p(objectUnderTest.pInv(5)));
    Assert.assertEquals(7, objectUnderTest.p(objectUnderTest.pInv(7)));
    Assert.assertEquals(11, objectUnderTest.p(objectUnderTest.pInv(11)));
  }

  @Test
  public void testT() {
    Assert.assertEquals(10000, objectUnderTest.tInv(objectUnderTest.t(10000)));
    Assert.assertEquals(399, objectUnderTest.tInv(objectUnderTest.t(399)));
    Assert.assertEquals(1, objectUnderTest.tInv(objectUnderTest.t(1)));
  }

  @Test
  public void testInit() {
    objectUnderTest.init();
  }
}

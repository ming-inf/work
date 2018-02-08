package math.prime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class First10000Test {
  First10000 objectUnderTest;

  @Before
  public void setup() {
    objectUnderTest = new First10000();
  }

  @Test
  public void testPrimeFactors() {
    Map<Integer, List<Integer>> numberToPrimeFactors = new HashMap<>();
    numberToPrimeFactors.put(44, Arrays.asList(2, 2, 11));
    numberToPrimeFactors.put(3, Arrays.asList(3));
    numberToPrimeFactors.put(32, Arrays.asList(2, 2, 2, 2, 2));

    for (Map.Entry e : numberToPrimeFactors.entrySet()) {
      Assert.assertArrayEquals(((List<Integer>) e.getValue()).toArray(), First10000.primeFactors((Integer) e.getKey()).toArray());
    }
  }

  @Test
  public void testLookup() throws IOException, ClassNotFoundException {
    objectUnderTest.deserialize();
    Assert.assertEquals(2, objectUnderTest.lookup(1));
    Assert.assertEquals(104729, objectUnderTest.lookup(10000));
  }
}

package structure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IntervalTreeTest {
  IntervalTree objectUnderTest;

  @Before
  public void setup() {
    objectUnderTest = new IntervalTree();
  }

  @Test
  public void testIntersectEmpty() {
    Assert.assertFalse(objectUnderTest.intersect(new Interval(0, 0)));
  }

  @Test
  public void testIntersectOneInterval() {
    objectUnderTest.add(new Interval(0, 1));

    Assert.assertTrue(objectUnderTest.intersect(new Interval(0, 0)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(1, 1)));

    Assert.assertTrue(objectUnderTest.intersect(new Interval(-1, 2)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(0, 1)));

    Assert.assertFalse(objectUnderTest.intersect(new Interval(-1, -1)));
    Assert.assertFalse(objectUnderTest.intersect(new Interval(2, 2)));
  }

  @Test
  public void testIntersectOverlapping() {
    objectUnderTest.add(new Interval(0, 1));
    objectUnderTest.add(new Interval(1, 2));

    Assert.assertTrue(objectUnderTest.intersect(new Interval(0, 0)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(2, 2)));

    Assert.assertTrue(objectUnderTest.intersect(new Interval(-1, 3)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(0, 2)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(1, 1)));

    Assert.assertFalse(objectUnderTest.intersect(new Interval(-1, -1)));
    Assert.assertFalse(objectUnderTest.intersect(new Interval(3, 3)));
  }

  @Test
  public void testIntersectNonOverlapping() {
    objectUnderTest.add(new Interval(0, 2));
    objectUnderTest.add(new Interval(5, 7));

    Assert.assertTrue(objectUnderTest.intersect(new Interval(0, 0)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(2, 2)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(5, 5)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(7, 7)));

    Assert.assertTrue(objectUnderTest.intersect(new Interval(-1, 8)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(0, 7)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(2, 5)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(1, 6)));

    Assert.assertFalse(objectUnderTest.intersect(new Interval(-1, -1)));
    Assert.assertFalse(objectUnderTest.intersect(new Interval(3, 4)));
    Assert.assertFalse(objectUnderTest.intersect(new Interval(8, 8)));
  }

  @Test
  public void testIntersectMultiple() {
    List<Interval> intervals = new ArrayList<>();
    intervals.add(new Interval(0, 3));
    intervals.add(new Interval(5, 8));
    intervals.add(new Interval(6, 10));
    intervals.add(new Interval(8, 9));
    intervals.add(new Interval(15, 23));
    intervals.add(new Interval(16, 21));
    intervals.add(new Interval(17, 19));
    intervals.add(new Interval(19, 20));
    intervals.add(new Interval(25, 30));
    intervals.add(new Interval(26, 26));

    for (Interval i : intervals) {
      objectUnderTest.add(i);
    }

    Assert.assertTrue(objectUnderTest.intersect(new Interval(10, 11)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(10, 15)));

    Assert.assertFalse(objectUnderTest.intersect(new Interval(11, 14)));
    Assert.assertFalse(objectUnderTest.intersect(new Interval(24, 24)));
  }

  @Test
  public void testRemoval() {
    List<Interval> intervals = new ArrayList<>();
    intervals.add(new Interval(0, 3));
    intervals.add(new Interval(5, 8));
    intervals.add(new Interval(6, 10));
    intervals.add(new Interval(8, 9));
    intervals.add(new Interval(15, 23));
    intervals.add(new Interval(16, 21));
    intervals.add(new Interval(17, 19));
    intervals.add(new Interval(19, 20));
    intervals.add(new Interval(25, 30));
    intervals.add(new Interval(26, 26));

    for (Interval i : intervals) {
      objectUnderTest.add(i);
    }

    Assert.assertTrue(objectUnderTest.intersect(new Interval(26, 26)));
    Assert.assertTrue(objectUnderTest.intersect(new Interval(30, 30)));

    objectUnderTest.remove(new Interval(25, 30));
    Assert.assertFalse(objectUnderTest.intersect(new Interval(30, 30)));
    Assert.assertFalse(objectUnderTest.intersect(new Interval(24, 25)));

    Assert.assertTrue(objectUnderTest.intersect(new Interval(26, 26)));
  }
}

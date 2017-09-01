package structure;

import org.junit.Assert;
import org.junit.Test;

public class LinkedListTest {
	@Test
	public void test() {
		LinkedList<Integer> list = new LinkedList<>();

		Assert.assertFalse(list.search(0));

		list.insert(0);

		Assert.assertTrue(list.search(0));
		Assert.assertFalse(list.search(1));

		list.insert(1);

		Assert.assertTrue(list.search(0));
		Assert.assertTrue(list.search(1));

		list.delete(0);

		Assert.assertFalse(list.search(0));
		Assert.assertTrue(list.search(1));

		Assert.assertFalse(list.search(5742));
	}
}

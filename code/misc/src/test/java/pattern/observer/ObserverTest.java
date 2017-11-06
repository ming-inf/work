package pattern.observer;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class ObserverTest {
	Subject objectUnderTest;

	@Test
	public void testObserver() {
		objectUnderTest = new Subject();
		AtomicInteger count = new AtomicInteger(0);

		Observer<Integer> o = i -> System.out.println(count.incrementAndGet());
		objectUnderTest.attach(o);
		objectUnderTest.attach(o);

		objectUnderTest.setState(1);
		objectUnderTest.setState(2);

		Assert.assertEquals(4, count.intValue());
	}
}

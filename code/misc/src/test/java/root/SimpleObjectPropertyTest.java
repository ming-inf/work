package root;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import root.SimpleObjectProperty;

public class SimpleObjectPropertyTest {
	@Test
	public void testPropertyChange() {
		List<Integer> testValues = Arrays.asList(1, 2, 4, 6);

		SimpleObjectProperty<Integer> m = new SimpleObjectProperty<>();

		for (int i : testValues) {
			PropertyChangeListener<Integer> listener = e -> {
				Assert.assertEquals((Integer)i, e.getNewValue());
			};
			m.addListener(listener);
			m.set(i);
			m.removeListener(listener);
		}
	}
}

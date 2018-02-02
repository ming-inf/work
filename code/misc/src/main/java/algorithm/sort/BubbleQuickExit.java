package algorithm.sort;

import java.util.Collections;
import java.util.List;

public class BubbleQuickExit<T extends Comparable<T>> implements Sort<T> {
	public void sort(List<T> list) {
		boolean swapped = false;
		do {
			swapped = false;
			for (int i = 1; i < list.size(); i++) {
				if (-1 == list.get(i).compareTo(list.get(i - 1))) {
					Collections.swap(list, i - 1, i);
					swapped = true;
				}
			}
		} while (swapped);
	}
}

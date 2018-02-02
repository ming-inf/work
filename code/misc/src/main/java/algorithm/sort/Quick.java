package algorithm.sort;

import java.util.Collections;
import java.util.List;

public class Quick<T extends Comparable<T>> implements Sort<T> {
	public void sort(List<T> list) {
		quickSort(list, 0, list.size() - 1);
	}

	private void quickSort(List<T> list, int start, int pivot) {
		if (pivot - start <= 0) {
			return;
		}

		int i = start;
		int p = pivot;
		int direction = 1;
		while (i != p) {
			if (direction == list.get(i).compareTo(list.get(p))) {
				Collections.swap(list, i, p);
				int temp = p;
				p = i;
				i = temp - direction;
				direction = -direction;
			} else {
				i += direction;
			}
		}

		quickSort(list, 0, p - 1);
		quickSort(list, p, pivot);
	}
}

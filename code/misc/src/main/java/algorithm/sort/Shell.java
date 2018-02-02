package algorithm.sort;

import java.util.List;

public class Shell<T extends Comparable<T>> implements Sort<T> {
	public void sort(List<T> list) {
		int size = list.size();
		for (int gap = size / 2; 0 < gap; gap /= 2) {
			for (int i = gap; i < size; i++) {
				T temp = list.get(i);
				int j;
				for (j = i; gap <= j && 1 == list.get(j - gap).compareTo(temp); j -= gap) {
					list.set(j, list.get(j - gap));
				}
				list.set(j, temp);
			}
		}
	}
}

package algorithm.sort;

import java.util.List;

public class Insertion<T extends Comparable<T>> implements Sort<T> {
	public void sort(List<T> list) {
		int sorted = 0;
		for (int j = 1; j < list.size(); j++) {
			for (int i = 0; i <= sorted; i++) {
				if (1 == list.get(i).compareTo(list.get(j))) {
					Util.rotate(list, i, j);
					break;
				}
			}
			sorted++;
		}
	}
}

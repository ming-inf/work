package algorithm.sort;

import java.util.List;

public class Merge<T extends Comparable<T>> implements Sort<T> {
	public void sort(List<T> list) {
		mergeSortSplit(list, 0, list.size() - 1);
	}

	private void mergeSortSplit(List<T> list, int i, int j) {
		if (i == j) {
			return;
		}

		int split = (i + j) / 2;
		mergeSortSplit(list, i, split);
		mergeSortSplit(list, split + 1, j);
		mergeSortMerge(list, i, split + 1, j);
	}

	private void mergeSortMerge(List<T> list, int i, int split, int j) {
		int left = i;
		int right = split;
		int mid = split;
		while (left <= mid && right <= j) {
			if (list.get(left).compareTo(list.get(right)) <= 0) {
				left++;
			} else {
				Util.rotate(list, left, right);
				left++;
				right++;
				mid++;
			}
		}
	}
}

package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Sort {
	static Random rand = new Random();

	public static void bubbleSort(java.util.List<Integer> list) {
		for (int j = 0; j < list.size(); j++) {
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i - 1) > list.get(i)) {
					Collections.swap(list, i - 1, i);
				}
			}
		}
	}

	public static void bubbleSortOpt(java.util.List<Integer> list) {
		boolean swapped = false;
		do {
			swapped = false;
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i - 1) > list.get(i)) {
					Collections.swap(list, i - 1, i);
					swapped = true;
				}
			}
		} while (swapped);
	}

	public static void insertionSort(java.util.List<Integer> list) {
		int sorted = 0;
		for (int j = 1; j < list.size(); j++) {
			for (int i = 0; i <= sorted; i++) {
				if (1 == list.get(i).compareTo(list.get(j))) {
					rotate(list, i, j);
					break;
				}
			}
			sorted++;
		}
	}

	public static void mergeSort(java.util.List<Integer> list) {
		mergeSortSplit(list, 0, list.size() - 1);
	}

	private static void mergeSortSplit(java.util.List<Integer> list, int i, int j) {
		if (i == j) {
			return;
		}

		int split = (i + j) / 2;
		mergeSortSplit(list, i, split);
		mergeSortSplit(list, split + 1, j);
		mergeSortMerge(list, i, split + 1, j);
	}

	private static void mergeSortMerge(java.util.List<Integer> list, int i, int split, int j) {
		int left = i;
		int right = split;
		int mid = split;
		while (left <= mid && right <= j) {
			if (list.get(left) <= list.get(right)) {
				left++;
			} else {
				rotate(list, left, right);
				left++;
				right++;
				mid++;
			}
		}
	}

	private static void rotate(java.util.List<Integer> list, int left, int right) {
		for (int i = right; left < i; i--) {
			Collections.swap(list, i, i - 1);
		}
	}

	public static void quickSort(java.util.List<Integer> list) {
		quickSort(list, 0, list.size() - 1);
	}

	private static void quickSort(java.util.List<Integer> list, int start, int pivot) {
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

	public static void shellSort(java.util.List<Integer> list) {
		int size = list.size();
		for (int gap = size / 2; 0 < gap; gap /= 2) {
			for (int i = gap; i < size; i++) {
				for (int j = i; gap <= j && list.get(i) < list.get(j - gap); j -= gap) {
					Collections.swap(list, j, j - gap);
				}
			}
		}
	}

	public static void main(String... args) {
// java.util.List<Integer> array = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));
		java.util.List<Integer> array = new ArrayList<>(Arrays.asList(4, 75, 74, 2, 54));

// Collections.shuffle(array);
		System.out.println(array);

		bubbleSort(array);
		System.out.println(array);
	}
}

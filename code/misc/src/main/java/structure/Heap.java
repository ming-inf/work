package structure;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Heap<T extends Comparable<T>> {
	List<T> heap = new ArrayList<>();

	public boolean search(T value) {
		if (isNull(value) || heap.isEmpty()) {
			return false;
		}

		int start = 0;
		int nodes = 1;
		while (start < heap.size()) {
			start = nodes - 1;
			int end = nodes + start;
			int count = 0;
			while ((start < heap.size()) && (start < end)) {
				if (value.equals(heap.get(start))) {
					return true;
				} else if (1 == value.compareTo(heap.get(parent(start))) && -1 == value.compareTo(heap.get(start))) {
					count++;
				}
				start++;
			}

			if (count == nodes) {
				return false;
			}
			nodes = nodes * 2;
		}
		return false;
	}

	public void insert(T value) {
		if (isNull(value)) {
			return;
		}

		heap.add(value);
		moveUp();
	}

	private void moveUp() {
		int i = heap.size() - 1;
		while (0 < i && -1 == heap.get(i).compareTo(heap.get(parent(i)))) {
			Collections.swap(heap, i, parent(i));
			i = parent(i);
		}
	}

	private int parent(int i) {
		return (i - 1) / 2;
	}

	private int left(int i) {
		return i * 2 + 1;
	}

	private int right(int i) {
		return i * 2 + 2;
	}

	public boolean delete() {
		if (heap.isEmpty()) {
			return false;
		}

		Collections.swap(heap, 0, heap.size() - 1);
		heap.remove(heap.size() - 1);
		moveDown();

		return true;
	}

	private void moveDown() {
		int i = 0;
		while (left(i) < heap.size() && 1 == heap.get(i).compareTo(heap.get(left(i)))) {
			if ((right(i) >= heap.size() || -1 == heap.get(left(i)).compareTo(heap.get(right(i))))) {
				Collections.swap(heap, i, left(i));
				i = left(i);
			} else {
				Collections.swap(heap, i, right(i));
				i = right(i);
			}
		}
	}

	public T peek() {
		if (heap.isEmpty()) {
			return null;
		}

		return heap.get(0);
	}

	public List<T> traverse() {
		return heap;
	}
}

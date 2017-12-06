package algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Misc<T> {
	public Set<Set<T>> powerset(Set<T> set) {
		List<T> list = new ArrayList<>(set);
		int size = (int) Math.pow(2, set.size());
		Set<Set<T>> result = IntStream.rangeClosed(0, size).mapToObj(i -> {
			Set<T> s = new HashSet<>();
			for (int j = 0; j < set.size(); j++) {
				if (0 < (i & (1 << j))) {
					s.add(list.get(j));
				}
			}
			return s;
		}).collect(Collectors.toSet());

		return result;
	}
}

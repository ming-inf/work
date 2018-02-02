package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Levenshtein {
	public int levenshtein(String word1, String word2) {
		int columns = word1.length() + 1;
		int rows = word2.length() + 1;

		List<Integer> currentRow = IntStream.range(0, columns).boxed().collect(Collectors.toList());
		for (int row = 1; row < rows; row++) {
			List<Integer> previousRow = currentRow;
			currentRow = new ArrayList<>();
			currentRow.add(previousRow.get(0) + 1);

			for (int column = 1; column < columns; column++) {
				int insertCost = currentRow.get(column - 1) + 1;
				int deleteCost = previousRow.get(column) + 1;
				int replaceCost = previousRow.get(column - 1) + ((word1.charAt(column - 1) != word2.charAt(row - 1)) ? 1 : 0);
				currentRow.add(Math.min(insertCost, Math.min(deleteCost, replaceCost)));
			}
		}
		return currentRow.get(currentRow.size() - 1);
	}

	public Map<String, Integer> search(String targetWord, int maxCost, List<String> words) {
		Map<String, Integer> results = new HashMap<>();
		for (String word : words) {
			int cost = levenshtein(targetWord, word);
			if (cost <= maxCost) {
				results.put(word, cost);
			}
		}
		return results;
	}
}

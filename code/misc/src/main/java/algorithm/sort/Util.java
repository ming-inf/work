package algorithm.sort;

import java.util.Collections;
import java.util.List;

public class Util {
  public static <T> void rotate(List<T> list, int left, int right) {
    for (int i = right; left < i; i--) {
      Collections.swap(list, i, i - 1);
    }
  }
}

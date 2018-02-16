package structure;

class Interval implements Comparable<Interval> {
  int low;
  int high;
  int max;

  public Interval(int low, int high) {
    this.low = low;
    this.high = high;
    this.max = high;
  }

  @Override
  public int compareTo(Interval o) {
    if (low != o.low) {
      return low - o.low;
    } else {
      return high - o.high;
    }
  }
}
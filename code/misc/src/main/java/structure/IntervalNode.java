package structure;

class IntervalNode implements Comparable<IntervalNode> {
  int low;
  int high;
  int max;

  public IntervalNode(int low, int high) {
    this.low = low;
    this.high = high;
    this.max = high;
  }

  @Override
  public int compareTo(IntervalNode o) {
    if (low != o.low) {
      return low - o.low;
    } else {
      return high - o.high;
    }
  }
}
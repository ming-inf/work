package math.prime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class First10000 implements Log {
  int[] primes;

  public void deserialize() throws IOException, ClassNotFoundException {
    String filename = "/10000primes.ser";

    try (ObjectInputStream in = new ObjectInputStream(getClass().getResourceAsStream(filename))) {
      primes = (int[]) in.readObject();
    }
  }

  public static List<Integer> primeFactors(int number) {
    int n = number;
    List<Integer> factors = new ArrayList<>();
    for (int i = 2; i <= n / i; i++) {
      while (n % i == 0) {
        factors.add(i);
        n /= i;
      }
    }
    if (n > 1) {
      factors.add(n);
    }
    return factors;
  }

  public int lookup(int nth) {
    if (nth < 1 || 10000 < nth) {
      throw new IllegalArgumentException();
    }

    return primes[nth - 1];
  }

  @Override
  public Logger getLogger() {
    return LogManager.getLogger();
  }
}

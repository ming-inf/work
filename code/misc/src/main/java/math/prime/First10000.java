package math.prime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class First10000 implements Log {
  static int[] primes;
  static String filename = "/10000primes.ser";

  static {
    try (ObjectInputStream in = new ObjectInputStream(First10000.class.getResourceAsStream(filename))) {
      primes = (int[]) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static List<Integer> primeFactors(int number) {
    int n = number;
    List<Integer> factors = new ArrayList<>();
    for (int i = 0; primes[i] <= n; i++) {
      while (n % primes[i] == 0) {
        factors.add(primes[i]);
        n /= primes[i];
      }
    }
    return factors;
  }

  public int isPrime(int i) {
    for (int j = 0; j < primes.length; j++) {
      if (i == primes[j]) {
        return j;
      } else if (i < primes[j]) {
        break;
      }
    }
    return -1;
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

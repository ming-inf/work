package math.prime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.Log;

public class First10000 implements Log {
  public int[] deserialize() throws IOException, ClassNotFoundException {
    String filename = "/10000primes.ser";

    try (ObjectInputStream in = new ObjectInputStream(getClass().getResourceAsStream(filename))) {
      int[] primes = (int[]) in.readObject();
      return primes;
    }
  }

  public void print(int[] primes) {
    for (int p : primes) {
      i(p);
    }
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    First10000 o = new First10000();
    int[] primes = o.deserialize();
    o.print(primes);
  }

  @Override
  public Logger getLogger() {
    return LogManager.getLogger();
  }
}

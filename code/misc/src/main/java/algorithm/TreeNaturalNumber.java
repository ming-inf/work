package algorithm;

import math.prime.First10000;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class TreeNaturalNumber {
  private TreeNode[] trees;
  private First10000 primes;

  TreeNaturalNumber() {
    trees = new TreeNode[10000];
    primes = new First10000();
  }

  public void init() {
    trees[0] = new TreeNode("2");
    for (int i = 1; i < trees.length; i++) {
      int j = primes.isPrime(i + 1);
      if (-1 < j) {
        TreeNode n = new TreeNode("" + (i + 1));
        n.addEdge(trees[j]);
        trees[i] = n;
      } else {
        List<Integer> factors = First10000.primeFactors(i + 1);
        TreeNode n = new TreeNode((i + 1) + "");
        for (int factor : factors) {
          n.addEdge(trees[factor - 2]);
        }
        trees[i] = n;
      }
    }
  }

  public int p(int nth) {
    return primes.lookup(nth);
  }

  public int pInv(int p) {
    int isPrime = primes.isPrime(p);
    return -1 < isPrime ? 1 + isPrime : -1;
  }

  public TreeNode t(int n) {
    if (1 == n) {
      return null;
    } else if (2 == n) {
      if (isNull(trees[n - 2])) {
        trees[n - 2] = new TreeNode("2");
      }
      return trees[n - 2];
    }

    TreeNode node = new TreeNode("" + n);
    int isPrime = primes.isPrime(n);
    if (0 < isPrime) {
      TreeNode prime = trees[n];
      node.addEdge(isNull(prime) ? t(pInv(n)) : prime);
      trees[n - 2] = node;
      return node;
    } else {
      List<Integer> factors = First10000.primeFactors(n);
      for (int factor : factors) {
        TreeNode t = trees[factor - 2];
        if (isNull(t)) {
          t = t(factor);
        }
        node.addEdge(t);
      }
    }

    return node;
  }

  public int tInv(TreeNode t) {
    if (isNull(t)) {
      return 1;
    } else if (t.connectedTo.isEmpty()) {
      return 2;
    } else if (1 == t.connectedTo.size()) {
      return p(tInv(t.connectedTo.get(0)));
    }

    int i = 1;
    for (TreeNode child : t.connectedTo) {
      i *= tInv(child);
    }
    return i;
  }

  public static class TreeNode {
    String id;
    List<TreeNode> connectedTo = new ArrayList<>();

    TreeNode(String id) {
      this.id = id;
    }

    void addEdge(TreeNode target) {
      connectedTo.add(target);
    }

    public String toString() {
      return id;
    }
  }

  public static void main(String... args) {
    TreeNaturalNumber o = new TreeNaturalNumber();

    o.init();
  }
}

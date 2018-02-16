package structure;

import java.util.Arrays;

class Hash {
  byte[] hash;

  Hash(byte[] hash) {
    this.hash = hash;
  }

  int size() {
    return hash.length;
  }

  static Hash hash(byte[] hash) {
    return new Hash(hash);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(hash);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Hash other = (Hash) obj;
    return Arrays.equals(hash, other.hash);
  }
}

package structure;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

public class MerkleTree extends BinaryTree<Hash> {
  public int SIZE = 1024;
  public Digest digest = new SHA256Digest();

  void digest(InputStream is) throws IOException {
    LinkedList<Hash> leafHashes = new LinkedList<>();
    byte[] data = new byte[SIZE];
    int available;
    while (0 < (available = is.available())) {
      if (available < SIZE) {
        data = new byte[available];
      }

      int bytesRead = 0;
      do {
        bytesRead = is.read(data, bytesRead, data.length - bytesRead);
      } while (0 < bytesRead);
      leafHashes.add(lh(data));
    }

    for (Hash h : leafHashes) {
      if (isNull(root)) {
        root = new TreeNode<>(h);
      } else {
        root = addLeaf(root, h);
      }
    }
  }

  Hash ih(Hash left, Hash right) {
    byte[] hash = new byte[digest.getDigestSize()];
    digest.update((byte) 1);
    digest.update(left.hash, 0, digest.getDigestSize());
    digest.update(right.hash, 0, digest.getDigestSize());
    digest.doFinal(hash, 0);
    return Hash.hash(hash);
  }

  Hash lh(byte[] data) {
    byte[] hash = new byte[digest.getDigestSize()];
    digest.update((byte) 0);
    digest.update(data, 0, data.length);
    digest.doFinal(hash, 0);
    return Hash.hash(hash);
  }

  private TreeNode<Hash> addLeaf(TreeNode<Hash> root, Hash h) {
    if (powerOf2(TreeNode.leavesSize(root))) {
      TreeNode<Hash> left = root;
      TreeNode<Hash> right = new TreeNode<>(h);
      Hash internalHash = ih(left.value, right.value);
      TreeNode<Hash> newRoot = new TreeNode<>(internalHash, null, left, right);
      left.parent = newRoot;
      right.parent = newRoot;
      return newRoot;
    } else {
      TreeNode<Hash> newRight = addLeaf(root.right, h);
      newRight.parent = root;
      root.right = newRight;
      root.value = ih(root.left.value, root.right.value);
      return root;
    }
  }

  private boolean powerOf2(int number) {
    return number > 0 && ((number & (number - 1)) == 0);
  }
}

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

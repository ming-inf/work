package structure;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import static java.util.Objects.isNull;

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
        root = new BinaryTreeNode<>(h);
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

  private BinaryTreeNode<Hash> addLeaf(BinaryTreeNode<Hash> root, Hash h) {
    if (powerOf2(BinaryTreeNode.leavesSize(root))) {
      BinaryTreeNode<Hash> left = root;
      BinaryTreeNode<Hash> right = new BinaryTreeNode<>(h);
      Hash internalHash = ih(left.value, right.value);
      BinaryTreeNode<Hash> newRoot = new BinaryTreeNode<>(internalHash, null, left, right);
      left.parent = newRoot;
      right.parent = newRoot;
      return newRoot;
    } else {
      BinaryTreeNode<Hash> newRight = addLeaf(root.right, h);
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

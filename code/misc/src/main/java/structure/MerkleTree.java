package structure;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

public class MerkleTree extends BinaryTreeWithParentLink<Hash> {
	public int SIZE = 1024;
	public Digest digest = new SHA256Digest();

	void digest(InputStream is) throws IOException {
		LinkedList<Hash> leafHashes = new LinkedList<>();
		byte[] data = new byte[SIZE];
		int bytesRead = 0;
		while (0 < is.available()) {
			while (bytesRead < SIZE) {
				bytesRead = is.read(data, bytesRead, SIZE - bytesRead);
			}
			leafHashes.add(lh(data));
			bytesRead = 0;
		}

		for (Hash h : leafHashes) {
			if (isNull(root)) {
				root = new Node<>(h);
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
		digest.update(data, 0, SIZE);
		digest.doFinal(hash, 0);
		return Hash.hash(hash);
	}

	private Node<Hash> addLeaf(Node<Hash> root, Hash h) {
		if (powerOf2(root.leavesSize())) {
			Node<Hash> left = root;
			Node<Hash> right = new Node<Hash>(h);
			Hash internalHash = ih(left.value, right.value);
			Node<Hash> newRoot = new Node<Hash>(internalHash, null, left, right);
			left.parent = newRoot;
			right.parent = newRoot;
			return newRoot;
		} else {
			Node<Hash> newRight = addLeaf(root.right, h);
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
}
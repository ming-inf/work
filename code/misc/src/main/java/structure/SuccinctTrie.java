package structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class SuccinctTrie {
	// Configure the bit writing and reading functions to work natively in BASE-64
	// encoding. That way, we don't have to convert back and forth to bytes.
	String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

	/**
	 * The width of each unit of the encoding, in bits. Here we use 6, for base-64 encoding.
	 */
	int W = 6;

	/**
	 * Returns the character unit that represents the given value. If this were binary data, we would simply return id.
	 */
	public char CHR(int id) {
		return BASE64.charAt(id);
	}

	/**
	 * Returns the decimal value of the given character unit.
	 */
	Map<Character, Integer> BASE64_CACHE = new HashMap<>(64);
	{
		BASE64_CACHE.put('A', 0);
		BASE64_CACHE.put('B', 1);
		BASE64_CACHE.put('C', 2);
		BASE64_CACHE.put('D', 3);
		BASE64_CACHE.put('E', 4);
		BASE64_CACHE.put('F', 5);
		BASE64_CACHE.put('G', 6);
		BASE64_CACHE.put('H', 7);
		BASE64_CACHE.put('I', 8);
		BASE64_CACHE.put('J', 9);
		BASE64_CACHE.put('K', 10);
		BASE64_CACHE.put('L', 11);
		BASE64_CACHE.put('M', 12);
		BASE64_CACHE.put('N', 13);
		BASE64_CACHE.put('O', 14);
		BASE64_CACHE.put('P', 15);
		BASE64_CACHE.put('Q', 16);
		BASE64_CACHE.put('R', 17);
		BASE64_CACHE.put('S', 18);
		BASE64_CACHE.put('T', 19);
		BASE64_CACHE.put('U', 20);
		BASE64_CACHE.put('V', 21);
		BASE64_CACHE.put('W', 22);
		BASE64_CACHE.put('X', 23);
		BASE64_CACHE.put('Y', 24);
		BASE64_CACHE.put('Z', 25);
		BASE64_CACHE.put('a', 26);
		BASE64_CACHE.put('b', 27);
		BASE64_CACHE.put('c', 28);
		BASE64_CACHE.put('d', 29);
		BASE64_CACHE.put('e', 30);
		BASE64_CACHE.put('f', 31);
		BASE64_CACHE.put('g', 32);
		BASE64_CACHE.put('h', 33);
		BASE64_CACHE.put('i', 34);
		BASE64_CACHE.put('j', 35);
		BASE64_CACHE.put('k', 36);
		BASE64_CACHE.put('l', 37);
		BASE64_CACHE.put('m', 38);
		BASE64_CACHE.put('n', 39);
		BASE64_CACHE.put('o', 40);
		BASE64_CACHE.put('p', 41);
		BASE64_CACHE.put('q', 42);
		BASE64_CACHE.put('r', 43);
		BASE64_CACHE.put('s', 44);
		BASE64_CACHE.put('t', 45);
		BASE64_CACHE.put('u', 46);
		BASE64_CACHE.put('v', 47);
		BASE64_CACHE.put('w', 48);
		BASE64_CACHE.put('x', 49);
		BASE64_CACHE.put('y', 50);
		BASE64_CACHE.put('z', 51);
		BASE64_CACHE.put('0', 52);
		BASE64_CACHE.put('1', 53);
		BASE64_CACHE.put('2', 54);
		BASE64_CACHE.put('3', 55);
		BASE64_CACHE.put('4', 56);
		BASE64_CACHE.put('5', 57);
		BASE64_CACHE.put('6', 58);
		BASE64_CACHE.put('7', 59);
		BASE64_CACHE.put('8', 60);
		BASE64_CACHE.put('9', 61);
		BASE64_CACHE.put('-', 62);
		BASE64_CACHE.put('_', 63);
	};

	public int ORD(char ch) {
		return BASE64_CACHE.get(ch);
	}

	/**
	 * Fixed values for the L1 and L2 table sizes in the Rank Directory
	 */
	int L2 = 32;
	int L1 = L2 * L2;

	/**
	 * The BitWriter will create a stream of bytes, letting you write a certain number of bits at a time. This is part of the encoder, so it is not optimized
	 * for memory or speed.
	 */
	class BitWriter {
		java.util.List<Integer> bits = new ArrayList<>();

		/**
		 * Write some data to the bit string. The number of bits must be 32 or fewer.
		 */
		public void write(int data, int numBits) {
			for (int i = numBits - 1; i >= 0; i--) {
				if (0 < (data & (1 << i))) {
					bits.add(1);
				} else {
					bits.add(0);
				}
			}
		}

		/**
		 * Get the bitstring represented as a string of bytes
		 */
		public String getData() {
			StringBuffer chars = new StringBuffer();
			int b = 0;
			int i = 0;

			for (int j = 0; j < bits.size(); j++) {
				b = (b << 1) | bits.get(j);
				i += 1;
				if (i == W) {
					chars.append(CHR(b));
					i = b = 0;
				}
			}

			if (0 < i) {
				chars.append(CHR(b << (W - i)));
			}

			return chars.toString();
		}

		/**
		 * Returns the bits as a human readable binary string for debugging
		 */
		public String getDebugString(int group) {
			StringBuffer chars = new StringBuffer();
			int i = 0;

			for (int j = 0; j < bits.size(); j++) {
				chars.append(bits.get(j));
				i++;
				if (i == group) {
					chars.append(" ");
					i = 0;
				}
			}

			return chars.toString();
		}
	}

	/**
	 * Given a string of data (eg, in BASE-64), the BitString class supports reading or counting a number of bits from an arbitrary position in the string.
	 */
	class BitString {
		String bytes;
		int length;

		public BitString(String str) {
			bytes = str;
			length = bytes.length() * W;
		}

		public int maskTop(int i) {
			return ~(~0<<6-i);
		}

		/**
		 * Returns the internal string of bytes
		 */
		public String getData() {
			return bytes;
		}

		/**
		 * Returns a decimal number, consisting of a certain number, n, of bits starting at a certain position, p.
		 */
		public int get(int p, int n) {
			int mask = maskTop(p % W);
			// case 1: bits lie within the given byte
			if ((p % W) + n <= W) {
				return (ORD(bytes.charAt(p / W | 0)) & mask) >> (W - p % W - n);

				// case 2: bits lie incompletely in the given byte
			} else {
				int result = (ORD(bytes.charAt(p / W | 0)) & mask);

				int l = W - p % W;
				p += l;
				n -= l;

				while (n >= W) {
					result = (result << W) | ORD(bytes.charAt(p / W | 0));
					p += W;
					n -= W;
				}

				if (n > 0) {
					result = (result << n) | (ORD(bytes.charAt(p / W | 0)) >> (W - n));
				}

				return result;
			}
		}

		/**
		 * Counts the number of bits set to 1 starting at position p and ending at position p + n
		 */
		public int count(int p, int n) {
			int count = 0;
			while (n >= 8) {
				count += Integer.bitCount(get(p, 8));
				p += 8;
				n -= 8;
			}

			return count + Integer.bitCount(get(p, n));
		}

		/**
		 * Returns the number of bits set to 1 up to and including position x. This is the slow implementation used for testing.
		 */
		public int rank(int x) {
			int rank = 0;
			for (int i = 0; i <= x; i++) {
				if (0 < get(i, 1)) {
					rank++;
				}
			}

			return rank;
		}
	}

	/**
	 * Used to build a rank directory from the given input string.
	 * @param data A string containing the data, as readable using the BitString object.
	 * @param numBits The number of bits to index.
	 * @param l1Size The number of bits that each entry in the Level 1 table summarizes. This should be a multiple of l2Size.
	 * @param l2Size The number of bits that each entry in the Level 2 table summarizes.
	 */
	public RankDirectory create(String data, int numBits, int l1Size, int l2Size) {
		BitString bits = new BitString(data);
		int p = 0;
		int i = 0;
		int count1 = 0, count2 = 0;
		int l1bits = (int) Math.ceil(Math.log(numBits) / Math.log(2));
		int l2bits = (int) Math.ceil(Math.log(l1Size) / Math.log(2));

		BitWriter directory = new BitWriter();

		while (p + l2Size <= numBits) {
			count2 += bits.count(p, l2Size);
			i += l2Size;
			p += l2Size;
			if (i == l1Size) {
				count1 += count2;
				directory.write(count1, l1bits);
				count2 = 0;
				i = 0;
			} else {
				directory.write(count2, l2bits);
			}
		}

		return new RankDirectory(directory.getData(), data, numBits, l1Size, l2Size);
	}

	/**
	 * The rank directory allows you to build an index to quickly compute the rank() and select() functions. The index can itself be encoded as a binary string.
	 */
	class RankDirectory {
		BitString directory;
		BitString data;
		int l1Size;
		int l2Size;
		int l1Bits;
		int l2Bits;
		int sectionBits;
		int numBits;

		public RankDirectory(String directoryData, String bitData, int numBits, int l1Size, int l2Size) {
			this.directory = new BitString(directoryData);
			this.data = new BitString(bitData);
			this.l1Size = l1Size;
			this.l2Size = l2Size;
			this.l1Bits = (int) Math.ceil(Math.log(numBits) / Math.log(2));
			this.l2Bits = (int) Math.ceil(Math.log(l1Size) / Math.log(2));
			this.sectionBits = (l1Size / l2Size - 1) * l2Bits + l1Bits;
			this.numBits = numBits;
		}

		/**
		 * Returns the string representation of the directory.
		 */
		public String getData() {
			return directory.getData();
		}

		/**
		 * Returns the number of 1 or 0 bits (depending on the "which" parameter) to to and including position x.
		 */
		public int rank(int which, int x) {
			if (which == 0) {
				return x - rank(1, x) + 1;
			}

			int rank = 0;
			int o = x;
			int sectionPos = 0;

			if (o >= l1Size) {
				sectionPos = (int) ((o / l1Size | 0) * sectionBits);
				rank = directory.get(sectionPos - l1Bits, l1Bits);
				o = o % l1Size;
			}

			if (o >= l2Size) {
				sectionPos += (o / l2Size | 0) * l2Bits;
				rank += directory.get(sectionPos - l2Bits, l2Bits);
			}

			rank += data.count(x - x % l2Size, x % l2Size + 1);

			return rank;
		}

		/**
		 * Returns the position of the y'th 0 or 1 bit, depending on the "which" parameter.
		 */
		public int select(int which, int y) {
			int high = numBits;
			int low = -1;
			int val = -1;

			while (high - low > 1) {
				int probe = (high + low) / 2 | 0;
				int r = rank(which, probe);

				if (r == y) {
					// We have to continue searching after we have found it,
					// because we want the _first_ occurrence.
					val = probe;
					high = probe;
				} else if (r < y) {
					low = probe;
				} else {
					high = probe;
				}
			}

			return val;
		}
	}

	/**
	 * A Trie node, for use in building the encoding trie. This is not needed for the decoder.
	 */
	class TrieNode {
		char letter;
		boolean final1 = false;
		java.util.List<TrieNode> children = new ArrayList<>();

		public TrieNode(char letter) {
			this.letter = letter;
		}
	}

	class Trie {
		String previousWord = "";
		TrieNode root = new TrieNode(' ');
		java.util.List<TrieNode> cache = new ArrayList<>();
		int nodeCount = 1;

		public Trie() {
			cache.add(root);
		}

		/**
		 * Returns the number of nodes in the trie
		 */
		public int getNodeCount() {
			return nodeCount;
		}

		/**
		 * Inserts a word into the trie. This function is fastest if the words are inserted in alphabetical order.
		 */
		public void insert(String word) {
			int commonPrefix = 0;
			for (int i = 0; i < Math.min(word.length(), previousWord.length()); i++) {
				if (word.charAt(i) != previousWord.charAt(i)) {
					break;
				}
				commonPrefix += 1;
			}

			TrieNode node = cache.get(commonPrefix);

			for (int i = commonPrefix; i < word.length(); i++) {
				TrieNode next = new TrieNode(word.charAt(i));
				nodeCount++;
				node.children.add(next);
				cache.add(next);
				node = next;
			}

			node.final1 = true;
			previousWord = word;
		}

		/**
		 * Apply a function to each node, traversing the trie in level order.
		 */
		public void apply(Consumer<TrieNode> fn) {
			java.util.Queue<TrieNode> level = new LinkedBlockingQueue<>();
			level.add(root);
			while (level.size() > 0) {
				TrieNode node = level.remove();
				for (int i = 0; i < node.children.size(); i++) {
					level.add(node.children.get(i));
				}
				fn.accept(node);
			}
		}

		/**
		 * Encode the trie and all of its nodes. Returns a string representing the encoded data.
		 */
		public String encode() {
			// Write the unary encoding of the tree in level order.
			BitWriter bits = new BitWriter();
			bits.write(0x02, 2);
			apply(node -> {
				for (int i = 0; i < node.children.size(); i++) {
					bits.write(1, 1);
				}
				bits.write(0, 1);
			});

			// Write the data for each node, using 6 bits for node. 1 bit stores
			// the "final" indicator. The other 5 bits store one of the 26 letters
			// of the alphabet.
			int a = 'a';
			apply(node -> {
				int value = node.letter - a;
				if (node.final1) {
					value |= 0x20;
				}

				bits.write(value, 6);
			});

			return bits.getData();
		}
	}

	/**
	 * This class is used for traversing the succinctly encoded trie.
	 */
	class FrozenTrieNode {
		FrozenTrie trie;
		int index;
		char letter;
		boolean final1;
		int firstChild;
		int childCount;

		public FrozenTrieNode(FrozenTrie trie, int index, char letter, boolean final1, int firstChild, int childCount) {
			this.trie = trie;
			this.index = index;
			this.letter = letter;
			this.final1 = final1;
			this.firstChild = firstChild;
			this.childCount = childCount;
		}

		/**
		 * Returns the number of children.
		 */
		public int getChildCount() {
			return childCount;
		}

		/**
		 * Returns the FrozenTrieNode for the given child.
		 * @param index The 0-based index of the child of this node. For example, if the node has 5 children, and you wanted the 0th one, pass in 0.
		 */
		public FrozenTrieNode getChild(int index) {
			return trie.getNodeByIndex(firstChild + index);
		}
	}

	/**
	 * The FrozenTrie is used for looking up words in the encoded trie.
	 * @param data A string representing the encoded trie.
	 * @param directoryData A string representing the RankDirectory. The global L1 and L2 constants are used to determine the L1Size and L2size.
	 * @param nodeCount The number of nodes in the trie.
	 */
	class FrozenTrie {
		BitString data;
		RankDirectory directory;
		int letterStart;

		public FrozenTrie(String data, String directoryData, int nodeCount) {
			this.data = new BitString(data);
			this.directory = new RankDirectory(directoryData, data, nodeCount * 2 + 1, L1, L2);

			// The position of the first bit of the data in 0th node. In non-root
			// nodes, this would contain 6-bit letters.
			this.letterStart = nodeCount * 2 + 1;
		}

		/**
		 * Retrieve the FrozenTrieNode of the trie, given its index in level-order. This is a private function that you don't have to use.
		 */
		public FrozenTrieNode getNodeByIndex(int index) {
			// retrieve the 6-bit letter.
			boolean final1 = data.get(letterStart + index * 6, 1) == 1;
			char letter = (char) (data.get(letterStart + index * 6 + 1, 5) + 'a');
			int firstChild = directory.select(0, index + 1) - index;

			// Since the nodes are in level order, this nodes children must go up
			// until the next node's children start.
			int childOfNextNode = directory.select(0, index + 2) - index - 1;

			return new FrozenTrieNode(this, index, letter, final1, firstChild, childOfNextNode - firstChild);
		}

		/**
		 * Retrieve the root node. You can use this node to obtain all of the other nodes in the trie.
		 */
		public FrozenTrieNode getRoot() {
			return getNodeByIndex(0);
		}

		/**
		 * Look-up a word in the trie. Returns true if and only if the word exists in the trie.
		 */
		public boolean lookup(String word) {
			FrozenTrieNode node = getRoot();
			for (int i = 0; i < word.length(); i++) {
				FrozenTrieNode child = null;
				int j = 0;
				for (; j < node.getChildCount(); j++) {
					child = node.getChild(j);
					if (child.letter == word.charAt(i)) {
						break;
					}
				}

				if (j == node.getChildCount()) {
					return false;
				}
				node = child;
			}

			return node.final1;
		}
	}

	/**************************************************************************************************
	 * DEMONSTATION APPLICATION FUNCTIONS
	 *************************************************************************************************/

	Trie trie;
	RankDirectory directory;
	String trieData;

	/**
	 * Encode the trie.
	 */
	public void go(java.util.List<String> words) {
		// create a trie
		Trie trie = new Trie();

		// split the words of the input up. Sort them for faster trie insertion.
		Collections.sort(words);
		for (String word : words) {
			trie.insert(word);
		}

		// Encode the trie.
		String trieData = trie.encode();

		// Encode the rank directory
		RankDirectory directory = create(trieData, trie.getNodeCount() * 2 + 1, L1, L2);

		this.trie = trie;
		this.directory = directory;
		this.trieData = trieData;
	}

	public boolean lookup(String word) {
		FrozenTrie ftrie = new FrozenTrie(trieData, directory.getData(), trie.getNodeCount());
		return ftrie.lookup(word);
	}

	public String lookupStatus(String word) {
		String status = String.format("\"%s\" %s in the dictionary", word, lookup(word) ? "is" : "is NOT");
		return status;
	}

	@Override
	public String toString() {
		return "SuccinctTrie [trie.nodeCount=" + trie.getNodeCount() + ", directory=" + directory.getData() + ", trieData=" + trieData + "]";
	}
}

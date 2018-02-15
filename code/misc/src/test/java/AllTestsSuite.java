

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import algorithm.LevenshteinTest;
import algorithm.LevenshteinWithTrieTest;
import algorithm.sort.SortTest;
import javafx.AppTest;
import practice.KeyPlaintextRunningKeyEncryptionTest;
import structure.AVLTreeTest;
import structure.BinarySearchTreeTest;
import structure.BinaryTreeTest;
import structure.DAFSATest;
import structure.DoublyLinkedListTest;
import structure.GraphTest;
import structure.HeapTest;
import structure.MerkleTreeTest;
import structure.SinglyLinkedListTest;
import structure.SuccinctTrieTest;
import structure.TrieTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    AppTest.class
    , SinglyLinkedListTest.class
    , DoublyLinkedListTest.class
    , BinarySearchTreeTest.class
    , HeapTest.class
    , AVLTreeTest.class
    , SortTest.class
    , LevenshteinTest.class
    , LevenshteinWithTrieTest.class
    , KeyPlaintextRunningKeyEncryptionTest.class
    , BinaryTreeTest.class
    , DAFSATest.class
    , GraphTest.class
    , MerkleTreeTest.class
    , SuccinctTrieTest.class
    , TrieTest.class
})

public class AllTestsSuite {
}

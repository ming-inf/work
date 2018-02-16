

import algorithm.TreeNaturalNumberTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import algorithm.LevenshteinTest;
import algorithm.LevenshteinWithTrieTest;
import algorithm.sort.SortTest;
import javafx.AppTest;
import practice.KeyPlaintextRunningKeyEncryptionTest;
import structure.*;

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
    , TreeNaturalNumberTest.class
    , IntervalTreeTest.class
})

public class AllTestsSuite {
}

package structure;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	SinglyLinkedListTest.class
	, DoublyLinkedListTest.class
	, BinarySearchTreeTest.class
	, HeapTest.class
	, AVLTreeTest.class
})

public class AllTestsSuite {
}

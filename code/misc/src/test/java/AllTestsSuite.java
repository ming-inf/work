

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import javafx.AppTest;
import structure.AVLTreeTest;
import structure.BinarySearchTreeTest;
import structure.DoublyLinkedListTest;
import structure.HeapTest;
import structure.SinglyLinkedListTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	AppTest.class
	, SinglyLinkedListTest.class
	, DoublyLinkedListTest.class
	, BinarySearchTreeTest.class
	, HeapTest.class
	, AVLTreeTest.class
})

public class AllTestsSuite {
}

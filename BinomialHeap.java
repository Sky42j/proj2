/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap {
	public int size;
	public HeapNode last;
	public HeapNode min;

	/**
	 *
	 * Construct an empty Binomial Heap
	 *
	 */
	public BinomialHeap() {
		this.size = 0;
		this.last = null;
		this.min = null;
	}

	/**
	 *
	 * Construct new BinomialHeap with HeapNode as only node
	 *
	 */
	public BinomialHeap(HeapNode node) {
		this.size = 1;
		this.last = node;
		this.min = node;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) {
		HeapNode node = new HeapNode(key, info);
		BinomialHeap inserted = new BinomialHeap(node);
		meld(inserted);
		return node.item;
	}

	/**
	 * 
	 * pre: x and y are the top nodes in heaps of same rank
	 *
	 * Link nodes (x,y) with minimal one at top of heap. return top of linked heap.
	 *
	 */
	public HeapNode link(HeapNode x, HeapNode y) {
		if (x.item.key > y.item.key) {
			HeapItem temp = x.item;
			x.item = y.item;
			y.item = temp;
		}
		if (x.child == null)
			y.next = y;
		else {
			y.next = x.child.next;
			x.child.next = y;
		}
		x.setChild(y);
		x.rank += 1;
		return x;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin() {
		BinomialHeap childHeap = new BinomialHeap();
		HeapNode node;

		if (this.min.child != null) {
			// create new heap from deleted node's children
			childHeap = new BinomialHeap(this.min.child);
			childHeap.size = (int) Math.pow(2, this.min.rank) - 1;
			// turn parent of new children in heap to null
			node = childHeap.last;
			while (node.parent != null) {
				node.parent = null;
				node = node.next;
			}
		}

		if (this.min.next == this.min) {
			this.size = childHeap.size;
			this.last = childHeap.last;
			this.min = childHeap.min;
		} else {
			// remove deleted node from list
			node = this.min.next;
			while (node.next != this.min) {
				node = node.next;
			}
			node.next = this.min.next;
			min.next = null;
			min.child = null;

			this.min = this.last;

			meld(childHeap);
		}

		// find new min
		node = this.last.next;
		boolean iterationDone = false;
		while (!iterationDone) {
			// check if final tree has been reached
			if (node == this.last)
				iterationDone = true;
			if (node.item.key < this.min.item.key)
				this.min = node;
			node = node.next;
		}

		return;

	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin() {
		return this.min.item;
	}

	/**
	 * 
	 * pre: 0 < diff < item.key
	 * 
	 * Decrease the key of item by diff and fix the heap.
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) {
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) {
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2) {
		// MAKE Y'S PREV SKIP Y BEFORE LINKING

		// connect tree lists
		if (this.empty()) {
			this.size = heap2.size;
			this.last = heap2.last;
			this.min = heap2.min;
			heap2 = new BinomialHeap();
		}

		if (!heap2.empty()) {
			HeapNode temp = heap2.last.next;
			heap2.last.next = this.last.next;
			this.last.next = temp;

			// update size
			this.size = this.size + heap2.size;

			// update min
			if (heap2.min.item.key < this.min.item.key)
				this.min = heap2.min;
		}

		int maxRank = (int) Math.floor(Math.log(this.size) / Math.log(2));
		HeapNode[] treesByRank = new HeapNode[maxRank + 1];
		boolean iterationDone = false;
		HeapNode current = this.last.next;
		while (!iterationDone) {
			// check if final tree has been reached
			if (current == this.last)
				iterationDone = true;

			int currRank = current.rank;
			// link until no tree of same rank is in heap
			while (treesByRank[currRank] != null) {
				HeapNode other = treesByRank[currRank];

				// to maintain order by rank, prioritize linking to later tree
				if (other.next.rank == currRank && other.next != current)
					other = other.next;

				treesByRank[currRank] = null;
				link(current, other);
				currRank++;
			}
			// node has been linked as much as possible, put it in relevant array index
			treesByRank[currRank] = current;
			// advance current
			current = current.next;
		}
		return;
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 * 
	 */
	public int size() {
		return this.size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 * 
	 */
	public boolean empty() {
		return this.size == 0; // should be replaced by student code
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees() {
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 * 
	 */
	public class HeapNode {
		public HeapNode(int key, String info) {
			this.item = new HeapItem(key, info, this);
			this.child = null;
			this.next = this;
			this.parent = null;
			this.rank = 0;
		}

		public void setChild(HeapNode child) {
			this.child = child;
			this.child.parent = this;
		}

		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 * 
	 */
	public class HeapItem {
		public HeapItem(int key, String info, HeapNode node) {
			this.key = key;
			this.info = info;
			this.node = node;
		}

		public HeapNode node;
		public int key;
		public String info;
	}

}

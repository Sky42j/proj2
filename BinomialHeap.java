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
		this.size = (int) Math.pow(2, node.rank);
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
			HeapItem tempItem = x.item;
			x.setItem(y.item);
			y.setItem(tempItem);
			HeapNode tempChild = x.child;
			x.setChild(y.child);
			y.setChild(tempChild);
			if (this.min == y)
				this.min = x;
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
			removeTree(node);

			this.min = this.last;

			meld(childHeap);
		}

		if (!this.empty()) {
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
		item.key -= diff;
		while (item.node.parent != null && item.key <= item.node.parent.item.key) {
			HeapItem tmp = item.node.parent.item;
			item.node.parent.item = item;
			item = tmp;
		}
		if (item.key <= this.min.item.key) {
			this.min = item.node;
		}
		return;
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) {
		int diff = item.key - this.min.item.key;
		decreaseKey(item, diff);
		deleteMin();
		return;
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2) {
		if (heap2.empty())
			return;
		if (this.empty()) {
			this.size = heap2.size;
			this.last = heap2.last;
			this.min = heap2.min;
			return;
		}

		// update size
		this.size = this.size + heap2.size;
		// update min
		if (heap2.min.item.key < this.min.item.key)
			this.min = heap2.min;

		HeapNode xPrev = this.last;
		HeapNode yPrev = heap2.last;

		HeapNode y;
		HeapNode x;
		while (!heap2.empty()) {
			if (xPrev.next.rank == yPrev.next.rank) {
				y = heap2.removeTree(yPrev);
				x = link(xPrev.next, y);
				if (x.next.rank == x.rank && x.next != x) {
					this.removeTree(xPrev);
					BinomialHeap xHeap = new BinomialHeap(x);
					meld(xHeap);
				}
			} else if (xPrev.next.rank > yPrev.next.rank) {
				y = heap2.removeTree(yPrev);
				y.next = xPrev.next;
				xPrev.next = y;
			} else {
				xPrev = xPrev.next;
			}
		}
		return;
	}

	public HeapNode removeTree(HeapNode nodePrev) {
		HeapNode node = nodePrev.next;
		if (node == this.last)
			this.last = nodePrev;
		nodePrev.next = nodePrev.next.next;
		node.next = node;
		this.size -= Math.pow(2, node.rank);
		return node;
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
		return this.size == 0;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees() {
		int counter = 0;
		int num = this.size;
		while (num > 0) {
			if (num % 2 == 1)
				counter++;
			num = num / 2;
		}
		return counter;
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
			if (child != null)
				this.child.parent = this;
		}

		public void setItem(HeapItem item) {
			this.item = item;
			this.item.node = this;
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

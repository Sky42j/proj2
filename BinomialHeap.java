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
	// complexity: O(log(n))
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
	// complexity: O(1)
	public HeapNode link(HeapNode x, HeapNode y) {
		// make sure x always has the smaller key
		if (x.item.key > y.item.key) {
			HeapNode temp = x;
			x = y;
			y = temp;
		}
		if (x.child == null)
			y.next = y;
		else {
			y.next = x.child.next;
			y.parent = x;
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
	// complexity: O(log(n))
	public void deleteMin() {
		if (this.empty())
			return;

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

		// if deleted node has no siblings, heap is now child heap
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
			detachTree(node);
			meld(childHeap);
		}

		if (!this.empty()) {
			// find new min
			node = this.last.next;
			this.min = node;
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
	// complexity: O(1)
	public HeapItem findMin() {
		if (this.min == null)
			return null;
		return this.min.item;
	}

	/**
	 * 
	 * pre: 0 < diff < item.key
	 * 
	 * Decrease the key of item by diff and fix the heap.
	 * 
	 */
	// complexity: O(log(n))
	public void decreaseKey(HeapItem item, int diff) {
		item.key -= diff;
		HeapNode node = item.node;
		// heapify up
		while (node.parent != null && item.key <= node.parent.item.key) {
			HeapItem tmp = node.parent.item;
			node.parent.setItem(item);
			node.setItem(tmp);
			node = item.node;
		}
		// update min
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
	// complexity: O(log(n))
	public void delete(HeapItem item) {
		// find the diff needed to make the item the new min
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
	// complexity: O(log(n))
	public void meld(BinomialHeap heap2) {
		if (heap2.empty())
			return;
		if (this.empty()) {
			this.size = heap2.size;
			this.last = heap2.last;
			this.min = heap2.min;
			return;
		}

		HeapNode xPrev = this.last;
		HeapNode yPrev = heap2.last;

		HeapNode y;
		HeapNode x;

		while (!heap2.empty() && heap2.last.next.rank <= this.last.rank) {
			if (xPrev.next.rank == yPrev.next.rank) {
				y = heap2.detachTree(yPrev);
				x = this.detachTree(xPrev);
				if (this.last == null && xPrev.item.key > y.item.key)
					xPrev = y;
				x = link(x, y);
				this.reattachTree(xPrev, x);
				while (x.next.rank == x.rank && x.next != x) {
					y = this.detachTree(x);
					if (xPrev == y)
						x = this.detachTree(x);
					else
						x = this.detachTree(xPrev);
					x = link(x, y);
					if (xPrev == y)
						xPrev = x;
					this.reattachTree(xPrev, x);
				}
			} else if (xPrev.next.rank > yPrev.next.rank) {
				y = heap2.detachTree(yPrev);
				this.reattachTree(xPrev, y);
			} else {
				xPrev = xPrev.next;
			}
		}

		while (!heap2.empty()) {
			y = heap2.detachTree(yPrev);
			this.reattachTree(this.last, y);
		}
		return;
	}

	/**
	 * 
	 * receives the node whose 'next' is the top of the tree we want to remove
	 * removes that tree from the heap
	 *
	 */
	// complexity: O(1)
	public HeapNode detachTree(HeapNode nodePrev) {
		HeapNode node = nodePrev.next;
		if (node == this.last)
			this.last = nodePrev;
		if (node == nodePrev) {
			this.last = null;
			this.min = null;
		}
		nodePrev.next = nodePrev.next.next;
		node.next = node;
		this.size -= Math.pow(2, node.rank);
		return node;
	}

	/**
	 * 
	 * receives the node whose 'next' is the top of the tree we want to add
	 * adds that tree to the heap
	 *
	 */
	// complexity: O(1)
	public HeapNode reattachTree(HeapNode nodePrev, HeapNode node) {
		if (this.empty()) {
			this.last = node;
			this.min = node;
		} else {
			node.next = nodePrev.next;
			nodePrev.next = node;
		}
		if (node.rank > this.last.rank)
			this.last = node;
		if (node.item.key <= this.min.item.key)
			this.min = node;
		this.size += Math.pow(2, node.rank);
		return node;
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 * 
	 */
	// complexity: O(1)
	public int size() {
		return this.size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 * 
	 */
	// complexity: O(1)
	public boolean empty() {
		return this.size == 0;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	// complexity: O(log(n))
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

		// complexity: O(log(n))
		public void setChild(HeapNode child) {
			this.child = child;
			if (child != null) {
				this.child.parent = this;
				HeapNode sibling = child.next;
				while (sibling != child) {
					sibling.parent = this;
					sibling = sibling.next;
				}
			}
		}

		// complexity: O(log(n))
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

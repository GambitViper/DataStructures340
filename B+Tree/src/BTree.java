
/**
 * @author Zach
 *
 */
import java.io.*;
import java.util.*;

public class BTree {

	RandomAccessFile f;
	int order;
	int blockSize;
	long root;
	long free;
	Stack<BTreeNode> paths;
	int mid;
	// add instance variables as needed.

	private class BTreeNode {
		private int count;
		private int keys[];
		private long children[];
		private long addr;

		// constructors and other method
		private BTreeNode(int count, int keys[], long children[], long addr) {
			this.count = count;
			this.keys = keys;
			this.children = children;
			this.addr = addr;
		}

		private BTreeNode(long addr) throws IOException {
			if (addr != 0) {
				// Allocate space for the keys and children
				this.keys = new int[order - 1];
				this.children = new long[order];
				// Seek to the memory address addr
				f.seek(addr);
				// Set the internal address property and read the count
				this.addr = addr;
				this.count = f.readInt();
				// Loop to read the keys in the node
				for (int i = 0; i < order - 1; i++) {
					keys[i] = f.readInt();
				}
				// Loop to read the children node pointers
				for (int j = 0; j < order; j++) {
					children[j] = f.readLong();
				}
			}
		}

		private void writeBTreeNode() throws IOException {
			f.seek(addr);

			f.writeInt(count);
			// Write the keys
			for (int i = 0; i < order - 1; i++) {
				f.writeInt(keys[i]);
			}

			// Write the child addresses
			for (int j = 0; j < order; j++) {
				f.writeLong(children[j]);
			}
		}

		private boolean isLeaf() {
			return this.count < 0;
		}

		private boolean notFull() {
			return Math.abs(count) < (order - 1);
		}

		private void insertNode(int k, long addr) {
			if (isLeaf()) {
				// Add to the count
				count--;
				// n will be inserted count
				int n = Math.abs(count);

				// New key added at end of the keys
				keys[n - 1] = k;
				// Put address at the second to last place in children
				children[n - 1] = addr;
				
				//Calculate max
				int max = 0;
				if(keys[n - 2] > k){
					max = keys[n - 2];
				}else {
					max = k;
				}
				
				//Intermediate save array
				long[] intermediate = new long[++max];
				
				// Is leaf possibly new smallest key
				for (int i = 0; i < n; i++) {
					intermediate[keys[i]] = children[i];
				}
				
				for (int i = 1; i < n; ++i) {
					int key = keys[i];
					int j = i - 1;

					// Shift values in array
					while (j >= 0 && keys[j] > key) {
						keys[j + 1] = keys[j];
						j--;
					}

					keys[j + 1] = key;
				}
				
				// Save an in-between table of all the associated children addresses
				for (int i = 0; i < n; i++) {
					children[i] = intermediate[keys[i]];
				}

			} else {//Non-Leaf node
				// Add to the count
				count++;
				// n will be inserted count
				int n = Math.abs(count);

				// New key added at end of the keys
				keys[n - 1] = k;
				// If not a leaf, possibly overwrote the last value
				children[n] = addr;
				
				//Calculate the max
				int max = 0;
				if(keys[n - 2] > k){
					max = keys[n - 2];
				}else {
					max = k;
				}
				
				//intermediate save array
				long[] intermediate = new long[++max];
				
				// Not a leaf no change to children[0]
				for (int i = 0; i < n; i++) {
					intermediate[keys[i]] = children[i + 1];
				}
				
				for (int i = 1; i < n; ++i) {
					int key = keys[i];
					int j = i - 1;

					// Shift values in array
					while (j >= 0 && keys[j] > key) {
						keys[j + 1] = keys[j];
						j--;
					}

					keys[j + 1] = key;
				}
				
				// Children array to match the key array
				for (int i = 0; i < n; i++) {
					children[i + 1] = intermediate[keys[i]];
				}
			}
		}

		private BTreeNode split(int key, long addr) {
			//Copy arrays with one more space
			int[] keyCopyOneMore = Arrays.copyOf(keys, order);
			long[] childrenCopyOneMore = Arrays.copyOf(children, order + 1);
			
			//New node with 1 extra space for the insert
			BTreeNode splitNode = new BTreeNode(count, keyCopyOneMore, childrenCopyOneMore,this.addr);

			if (key != -1 && addr != -1) {
				//Add the new inserted node into the tree
				splitNode.insertNode(key, addr);
			}
			
			//Calculation for the new count of the node
			int newCount = (int) Math.ceil((double) splitNode.keys.length / 2);
			
			//Split the values between node and the new node and update old node's count
			int oldNodeNewCount = Math.abs(count) + 1 - newCount;
			if (isLeaf()) {
				count = oldNodeNewCount * -1;
			} else {
				count = oldNodeNewCount;
			}

			// Link to next node to copy into the last child address to keep link to next node
			long nextLeaf = children[order - 1];

			// Get the new values for the old node
			int[] origKeys = Arrays.copyOfRange(splitNode.keys, 0, Math.abs(count));
			long[] origChilds = Arrays.copyOfRange(splitNode.children, 0, Math.abs(count) + 1);
			
			//Copy into original nodes keys and children node properties
			keys = Arrays.copyOf(origKeys, order - 1);
			children = Arrays.copyOf(origChilds, order);

			// Get the new values for the new node. Starting at the middle until the end.
			int[] splitKeys = Arrays.copyOfRange(splitNode.keys, newCount - 1, splitNode.keys.length);
			long[] splitChilds = Arrays.copyOfRange(splitNode.children, newCount - 1, splitNode.children.length);
			
			//Copy into
			int[] keyArr = Arrays.copyOf(splitKeys, order - 1);
			long[] childrenArr = Arrays.copyOf(splitChilds, order);

			if (order % 2 == 0 || !splitNode.isLeaf()) {
				//Set the middle value for later loops
				mid = keyArr[0];
				
				// This will copy the array starting 1 place over to the right
				splitKeys = Arrays.copyOfRange(splitNode.keys, newCount, splitNode.keys.length);
				splitChilds = Arrays.copyOfRange(splitNode.children, newCount, splitNode.children.length);
				
				//Copy into
				keyArr = Arrays.copyOf(splitKeys, order - 1);
				childrenArr = Arrays.copyOf(splitChilds,order);
				
				//Odd order tree rectify
				if (order % 2 == 1) {
					newCount--;
				}
			}
			
			//Rectify negative count index
			if (splitNode.isLeaf()) {
				newCount *= -1;
			}

			//Keep listed link to the next node and add the new node
			childrenArr[order - 1] = nextLeaf;
			BTreeNode newnode = new BTreeNode(newCount, keyArr, childrenArr, findFree());
			children[order - 1] = newnode.addr;

			return newnode;
		}
	}

	public BTree(String filename, int bsize) throws IOException {
		// bsize is the block size. This value is used to calculate the order of
		// the B+Tree
		// all B+Tree nodes will use bsize bytes
		// make a new B+tree
		File path = new File(filename);
		if (path.exists()) {
			path.delete();
		}
		f = new RandomAccessFile(path, "rw");

		root = 0;
		free = 0;
		blockSize = bsize;
		order = bsize / 12;
		paths = new Stack<>();
		// Write initialized BTree
		f.seek(0);
		f.writeLong(root);
		f.writeLong(free);
		f.writeInt(blockSize);

	}

	public BTree(String filename) throws IOException {
		// open an existing B+Tree
		File path = new File(filename);
		f = new RandomAccessFile(path, "rw");
		f.seek(0);
		root = f.readLong();
		free = f.readLong();
		blockSize = f.readInt();
		order = blockSize / 12;
		paths = new Stack<>();
	}

	public boolean insert(int key, long addr) throws IOException {
		/*
		 * If key is not a duplicate add, key to the B+tree addr (in DBTable) is
		 * the address of the row that contains the key return true if the key
		 * is added return false if the key is a duplicate
		 */
		if (root == 0) {
			int[] keys = new int[order - 1];
			long[] children = new long[order];
			keys[0] = key;
			children[0] = addr;
			BTreeNode r = new BTreeNode(-1, keys, children, findFree());

			r.writeBTreeNode();

			root = r.addr;
			return true;
		}
		boolean alreadyPresent = 0 != search(key);
		boolean split = false;
		if (!alreadyPresent) {
			long location = 0;
			int value = 0;
			// Perform Insertion
			BTreeNode r = paths.pop();
			//Node r has room to insert the key
			if (r.notFull()) {
				//Perform the insert and write the new node out to the random Access file
				r.insertNode(key, addr);
				r.writeBTreeNode();
			} else {
				//Node r is full , which requires a split
				BTreeNode newNode = r.split(key, addr);
				//Keep track of the value to update parent node's keys
				value = newNode.keys[0];
				//Write the nodes out
				r.writeBTreeNode();
				newNode.writeBTreeNode();
				//Update the new node's location
				location = newNode.addr;
				split = true;
			}
			
			//Paths aren't empty and you have split
			while (!paths.empty() && split) {
				//Node is the next node on the path's stack
				BTreeNode node = paths.pop();
				if (node.notFull()) {
					//Node node is not full so you can insert as normal
					node.insertNode(value, location);
					//Write the node out and change the loop's condition
					node.writeBTreeNode();
					split = false;
				} else {
					//Node node was full so you have to perform a split
					BTreeNode newNode = node.split(value, location);
					//Update the value and location
					value = mid;
					location = newNode.addr;
					
					//Write the nodes out and keep loop condition to update further up in the path's stack
					node.writeBTreeNode();
					newNode.writeBTreeNode();
					
					//Continue the loop condition
					split = true;
				}
			}
			
			//Case for the split root
			if (split) {
				//Setup space for the keys and children values
				int[] keys = new int[order - 1];
				long[] children = new long[order];
				//Utilize the value and location data for the parent of the root
				keys[0] = value;
				children[0] = root;
				children[1] = location;
				
				//Find a new location for the split of the root node
				BTreeNode newNode = new BTreeNode(1, keys, children, findFree());
				root = newNode.addr;

				//Write the node out
				newNode.writeBTreeNode();
			}
		}
		
		//Return the boolean value of whether or not the given insert was performed
		return !alreadyPresent;

	}	

	private long findFree() {
		//Find new location to insert new node
		long addr = 0;
		try {
			//If free is empty return the length
			if (free == 0) {
				addr = f.length();
			} else {
				//Retain linked list in the free list
				addr = free;
				f.seek(free);

				free = f.readLong();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Return free address
		return addr;
	}

	public long remove(int key) {
		/*
		 * If the key is in the Btree, remove the key and return the address of
		 * the row return 0 if the key is not found in the B+tree
		 */
		return key;
	}

	public long search(int k) throws IOException {
		/*
		 * This is an equality search
		 * 
		 * If the key is found return the address of the row with the key
		 * otherwise return 0
		 */

		//Create a new path's stack
		int i = 0;
		paths = new Stack<>();
		//Start at the root
		BTreeNode r = new BTreeNode(root);
		paths.push(r);
		if (root == 0) {
			return 0;
		}

		//Get to a leaf node
		while (!r.isLeaf()) {
			for (i = 0; i <= Math.abs(r.count); i++) {
				if (i == Math.abs(r.count) || k < r.keys[i]) {
					r = new BTreeNode(r.children[i]);
					paths.push(r);
					break;
				}
			}
		}
		
		//Finding the address of the key equality search
		long addr = 0;
		int idx = -1;
		for (int j = 0; j < Math.abs(r.count); j++) {
			if (k == r.keys[j]) {
				idx = i;
			}
		}

		if (idx != -1) {
			addr = r.children[idx];
		}

		return addr;
	}

	public LinkedList<Long> rangeSearch(int low, int high) throws IOException {
		// PRE:low <= high
		/*
		 * return a list of row addresses for all keys in the range low to high
		 * inclusive return an empty list when no keys are in the range
		 */
		LinkedList<Long> list = new LinkedList<>();
		int i = 0;
		paths = new Stack<>();
		BTreeNode r = new BTreeNode(root);
		BTreeNode look = null;
		paths.push(r);
		if (root == 0) {
			look = r;
		}

		while (!r.isLeaf()) {
			for (i = 0; i < Math.abs(r.count); i++) {
				if (i == Math.abs(r.count) || low < r.keys[i]) {
					r = new BTreeNode(r.children[i]);
					paths.push(r);
					break;
				}
			}
		}

		if (look == null) {
			look = r;
		}

		while (low < look.keys[i] && look.keys[i] < high) {
			list.add(look.children[i]);
			i++;
			if (i == Math.abs(look.count)) {
				if (look.children[order - 1] == 0) {
					// End of linked list of leaf nodes
					break;
				}
				// Look at next leaf in the linked list of leaf nodes
				look = new BTreeNode(look.children[order - 1]);
				i = 0;
			}
		}

		return list;
	}

	public void print() throws IOException {
		// print the B+Tree to standard output
		// print one node per line
		System.out.printf("%10s: \n", "Address");
		System.out.printf("%10s: %2d\n", "Root", root);
		System.out.printf("%10s: %2d\n", "Free", free);
		f.seek(20);
		while(f.getFilePointer() < f.length()){
			printAddr(f.getFilePointer());
		}
	}

	private void printAddr(long addr) throws IOException {
		f.seek(addr);
		BTreeNode curr = new BTreeNode(addr);
		String addrStr = Long.toString(curr.addr);
		String countStr = " " + curr.count + " ";
		String keyStr = "";
		for (int key : curr.keys) {
			keyStr += key + " ";
		}
		String childStr = "";
		for (long child : curr.children) {
			childStr += child + " ";
		}
		System.out.printf("%10s: Count: %4s | Keys: %10s | Children: %4s\n", addrStr, countStr, keyStr, childStr);
	}

	public void close() throws IOException {
		// close the B+tree. The tree should not be accessed after close is
		// called
		f.seek(0);
		f.writeLong(root);
		f.writeLong(free);
		f.writeInt(blockSize);
		f.close();
	}

}

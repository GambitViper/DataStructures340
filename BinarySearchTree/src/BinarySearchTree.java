/**
 * Implements a binary search tree of ints stored in a random access file.
 * Duplicates are recorded by a count field associated with the int
 * @author Zach
 *
 */
import java.io.*;
import java.util.*;

public class BinarySearchTree {

	final int CREATE = 0;
	final int REUSE = 1;

	private RandomAccessFile f;
	long root;//the address of the root node in the file
	long free;//the address in the file of the first node in the free list

	private class Node{
		private long left;
		private int data;
		private int count;
		private long right;

		private Node(long l, int d, long r){
			//constructor for a new node
			left = l;
			data = d;
			right = r;
			count = 1;
		}

		private Node(long addr) throws IOException{
			//constructor for a node that exists and is stored in the file
			f.seek(addr);
			data = f.readInt();
			count = f.readInt();
			left = f.readLong();
			right= f.readLong();
		}

		private void writeNode(long addr) throws IOException{
			//constructor for a node that exists and is stored in the file
			f.seek(addr);
			f.writeInt(data);
			f.writeInt(count);
			f.writeLong(left);
			f.writeLong(right);
		}
	}

	public BinarySearchTree(String fname,int mode) throws IOException{
		/* if mode is CREATE a new empty file is created
		 * if mode is CREATE and a file name fname exists the file with fname must be
		 * deleted before the new empty file is created
		 * if mode is REUSE an existing file is used if it exists otherwise a new empty file
		 * is created
		 */
		File path = new File(fname);
		if(path.exists() && mode == CREATE){
			path.delete();
		}

		f = new RandomAccessFile(path, "rw");
		if(mode == CREATE){
			root = 0;
			free = 0;
			f.writeLong(root);
			f.writeLong(free);
		}else {
			f.seek(0);
			root = f.readLong();
			free = f.readLong();
		}
	}

	public void insert(int d) throws IOException{
		/* insert d into the tree
		 * if d is in the tree increment the count field associated with d
		 */
		root = insert(root, d);
	}

	/**
	 * Insert method for Binary Search Tree
	 * @param addr Address of place you're inserting at
	 * @param d Data to be inserted
	 * @return long of new Address in edge cases of root deletion
	 * @throws IOException uses Random Access File
	 */
	private long insert(long addr, int d) throws IOException{
		if(addr == 0){
			Node newNode = new Node(0, d, 0);
			long writeAddr = getFree();
			newNode.writeNode(writeAddr);
			return writeAddr;
		}
		Node curr = new Node(addr);
		if(d < curr.data){
			curr.left = insert(curr.left, d);
			curr.writeNode(addr);
		}else if(d > curr.data){
			curr.right = insert(curr.right, d);
			curr.writeNode(addr);
		}else {
			curr.count++;
			curr.writeNode(addr);
		}
		return addr;
	}

	/**
	 * Fetches the free address and does some book keeping behind the scenes
	 * @return the address of the free node
	 * @throws IOException
	 */
	private long getFree() throws IOException{
		long addr = 0;
		if(free == 0){
			addr = f.length();
		}else {
			addr = free;
			Node freeNode = new Node(free);
			free = (long) freeNode.data;
		}
		return addr;
	}

	/**
	 * Adds the given addr to the free list
	 * @param addr addr of the Node to be given to the free list
	 * @throws IOException
	 */
	private void addFree(long addr) throws IOException{
		Node toFree = new Node(addr);
		toFree.data = (int) free;
		toFree.count = 0;
		toFree.left = 0;
		toFree.right = 0;
		toFree.writeNode(addr);
		free = addr;
	}

	public int find(int d) throws IOException{
		/* if d is in the tree return the value of count associated with d
		 * otherwise return 0
		 */
		return find(root, d);
	}

	/**
	 * Recursive find method given the data to look for returns the count
	 * @param addr initial look address
	 * @param d Data to find the Count for
	 * @return count of the Data d
	 * @throws IOException
	 */
	private int find(long addr, int d) throws IOException{
		if(addr == 0){
			return 0;
		}
		Node temp = new Node(addr);
		if(d > temp.data){
			return find(temp.right, d);
		}else if(d < temp.data){
			return find(temp.left, d);
		}
		return temp.count;
	}

	public void removeOne(int d) throws IOException{
		/* remove one copy of d from the tree
		 * if the copy is the last copy remove d from the tree
		 * if d is not in the tree the method has no effect
		 */
		root = delete(root, d, false);
	}

	public void removeAll(int d) throws IOException{
		/* remove d from the tree
		 * if d is not in the tree the method has no effect
		 */
		root = delete(root, d, true);
	}

	/**
	 * Remove data from the Binary Search Tree
	 * @param r address to begin removing at
	 * @param d Data to be removed from the tree
	 * @param all removeOne versus removeAll toggle
	 * @return address to recursively remove with
	 * @throws IOException
	 */
	private long delete(long r, int d, boolean all) throws IOException{
		if(r == 0) return 0;
		long retVal = r;
		Node curr = new Node(r);
		if(curr.data == d){
			if(all){
				curr.count = 0;
			}else {
				curr.count--;
			}
			if(curr.count == 0){
				if(curr.left == 0){
					retVal = curr.right;
					addFree(r);
				}else if(curr.right == 0){
					retVal = curr.left;
					addFree(r);
				}else {
					curr.left = replace(curr.left, r);
				}
			}
		}else if(curr.data > d){
			curr.left = delete(curr.left, d, false);
			curr.writeNode(retVal);
		}else{
			curr.right = delete(curr.right, d, false);
			curr.writeNode(retVal);
		}
		return retVal;
	}

	/**
	 * Replace a given Node with addr r to addr repHere
	 * @param r addr of given Node
	 * @param repHere addr of replacement Node
	 * @return
	 * @throws IOException
	 */
	private long replace(long r, long repHere) throws IOException{
		Node curr = new Node(r);
		Node rep = new Node(repHere);
		if(curr.right != 0){
			curr.right = replace(curr.right, repHere);
			curr.writeNode(r);
			return r;
		}else {
			rep.data = curr.data;
			rep.count = curr.count;
			rep.writeNode(repHere);
			addFree(r);
			return curr.left;
		}
	}

	public void close() throws IOException{
		/* close the random access file
		 * before closing update the values of root and free if necessary
		 */
		f.seek(0);
		f.writeLong(root);
		f.writeLong(free);
		f.close();
	}

	/**Main method with test code to check random access file system and implementation
	 * of the binary search tree data structure
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BinarySearchTree bst = new BinarySearchTree("tree.bin", 0);
		bst.insert(42);
		bst.insert(42);
		bst.insert(45);
		bst.insert(89);
		bst.insert(34);
		bst.insert(98);
		bst.insert(90);
		bst.insert(55);
		bst.insert(55);
		bst.insert(32);
		bst.insert(56);
		bst.insert(75);
		bst.printBST(bst);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		bst.removeOne(98);
		bst.printBST(bst);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		bst.removeOne(32);
		bst.printBST(bst);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		bst.insert(32);
		bst.printBST(bst);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		bst.removeOne(89);
		bst.printBST(bst);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		bst.removeAll(42);
		bst.printBST(bst);
		bst.close();

	}
	/**
	 * Print Method to locate issues in code while debugging
	 * @param bst Object Binary Search Tree instance
	 * @throws IOException
	 */
	private void printBST(BinarySearchTree bst) throws IOException{
		System.out.printf("%7s %7s %7s %7s %7s\n", "Address", "Data", "Count", "Left", "Right");
		System.out.printf("%7s %7d\n", "Root", bst.root);
		System.out.printf("%7s %7d\n", "Free", bst.free);
		for (int i = 16; i < bst.f.length(); i+=24){
			bst.printAddr(i);
		}
	}
	
	/**
	 * Helper method for printBST
	 * @param addr
	 * @throws IOException
	 */
	private void printAddr(long addr) throws IOException{
		f.seek(addr);
		Node curr = new Node(addr);
		System.out.printf("%7d %7d %7d %7d %7d\n",addr, curr.data, curr.count, curr.left, curr.right);
	}

}

/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.util.*;

public class HuffmanTree {

	private class Node {
		private Node left;
		private char data;
		private Node right;

		private Node(Node l, char d, Node r) {
			left = l;
			data = d;
			right = r;
		}
	}

	private Node root;
	private Node current;

	public HuffmanTree() {
		root = null;
		current = null;
	}

	/**
	 * Makes a one node Huffman tree
	 * 
	 * @param d
	 *            character to place in a single node tree
	 */
	public HuffmanTree(char d) {
		root = new Node(null, d, null);
		current = root;
	}

	/**
	 * A tree where a node is either a leaf or has two children
	 * 
	 * @param t
	 *            represents a post order traversal of the tree
	 * @param nonLeaf
	 *            is the char value of the data in the non-leaf nodes
	 */
	public HuffmanTree(String t, char nonLeaf) {

	}

	/**
	 * Merges b1 with b2
	 * 
	 * @param b1
	 *            Right leaf
	 * @param b2
	 *            Left leaf
	 * @param d
	 *            value of the non-leaf data node to merge with
	 */
	public HuffmanTree(HuffmanTree b1, HuffmanTree b2, char d) {
		root = new Node(b1.root, d, b2.root);
		current = root;
		
	}

	/*
	 * The following methods allow a user object to follow a path in the tree.
	 * Each method except atLeaf and current changes the value of current
	 */

	public void moveRoot() {

	}

	public void moveLeft() {

	}

	public void moveRight() {

	}

	/**
	 * atLeaf returns true if the current position is a leaf, otherwise it
	 * returns false
	 * 
	 * @return atLeaf boolean operation
	 */
	public boolean atLeaf() {
		if (current.right == null && current.left == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * current returns the data value in the current Node
	 * 
	 * @return current.data
	 */
	public char current() {
		return current.data;
	}

	public class PathIterator implements Iterator<String> {
		private LinkedList<String> paths;

		public PathIterator() {
			paths = new LinkedList<>();
			makePath(root, "");
		}

		private void makePath(Node r, String path) {
			if (atLeaf()) {
				paths.add(r.data + path);
			} else {
				makePath(r.left, path + "0");
				makePath(r.right, path + "1");
			}
		}

		public boolean hasNext() {
			return paths.size() < 1;
		}

		public String next() {
			return paths.poll();
		}
	}

	public Iterator<String> iterator() {
		return new PathIterator();

	}

	/**
	 * Returns a post order representation of the tree in the format we
	 * discussed in class
	 */
	public String toString() {
		return toString(root);
	}
	
	private String toString(Node r){
		if(r == null){
			return "";
		}else {
			return toString(r.left) + toString(r.right) + r.data;
		}
	}
}

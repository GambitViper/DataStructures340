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
	private Stack<HuffmanTree> stack;

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
		stack = new Stack<>();
		for (int i = 0; i < t.length(); ++i) {
			if (t.charAt(i) == nonLeaf){
				HuffmanTree right = stack.pop();
				HuffmanTree left = stack.pop();

				stack.push(new HuffmanTree(left, right, nonLeaf));
			}else {
				stack.push(new HuffmanTree(t.charAt(i)));
			}
		}
		root = stack.pop().current;
		current = root;
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

	//Moves to the position of the root
	public void moveRoot() {
		current = root;
	}

	//Moves to the left node of the current node
	public void moveLeft() {
		current = current.left;
	}

	//Moves to the right node of the current node
	public void moveRight() {
		current = current.right;
	}

	/**
	 * atLeaf returns true if the current position is a leaf, otherwise it
	 * returns false
	 * 
	 * @return atLeaf boolean operation
	 */
	public boolean atLeaf() {
		return current.right == null && current.left == null;
	}

	/**
	 * current returns the data value / character in the current Node
	 * @return current.data 
	 */
	public char current() {
		return current.data;
	}
	
	public class PathIterator implements Iterator<String> {
		private LinkedList<String> paths;

		//Build a linked list of encoding paths and pull from that for encoding data
		public PathIterator() {
			paths = new LinkedList<>();
			makePath(root, "");
		}

		//Builds the string representation paths for the encoded characters
		private void makePath(Node r, String path) {
			if (r.left == null) {
				paths.add(r.data + path);
			} else {
				makePath(r.left, path + "0");
				makePath(r.right, path + "1");
			}
		}

		public boolean hasNext() {
			return !paths.isEmpty();
		}

		//Returns the string representation of the encoded path
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
	
	//To String method for the Huffman Tree
	private String toString(Node r){
		if(r == null){
			return "";
		}else {
			return toString(r.left) + toString(r.right) + r.data;
		}
	}
}

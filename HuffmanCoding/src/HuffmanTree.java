/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.util.*;
public class HuffmanTree {
	
	private class Node{
		private Node left;
		private char data;
		private Node right;
		
		private Node(Node l, char d, Node r){
			left = l;
			data = d;
			right = r;
		}
	}
	
	private Node root;
	private Node current;
	
	public HuffmanTree(){
		root = null;
		current = null;
	}
	
	/**
	 * Makes a one node Huffman tree
	 * @param d character to place in a single node tree
	 */
	public HuffmanTree(char d){
		
	}
	
	/**
	 * A tree where a node is either a leaf or has two children
	 * @param t represents a post order traversal of the tree
	 * @param nonLeaf is the char value of the data in the non-leaf nodes
	 */
	public HuffmanTree(String t, char nonLeaf){
		
	}
	
	/**
	 * Merges b1 with b2
	 * @param b1 Right leaf
	 * @param b2 Left leaf
	 * @param d value of the non-leaf data node to merge with
	 */
	public HuffmanTree(HuffmanTree b1, HuffmanTree b2, char d){
		
	}
	
	/* The following methods allow a user object to follow a path in the tree.
	 * Each method except atLeaf and current changes the value of current
	 */
	
	public void moveRoot(){
		
	}
	
	public void moveLeft(){
		
	}
	
	public void moveRight(){
		
	}
	
	/**
	 * atLeaf returns true if the current position is a leaf, 
	 * otherwise it returns false
	 * @return atLeaf boolean operation
	 */
	public boolean atLeaf(){
		if(current.right == null && current.left == null){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * current returns the data value in the current Node
	 * @return current.data
	 */
	public char current(){
		return current.data;
	}
	
	public class PathIterator implements Iterator<String>{
		public PathIterator(){
			
		}
		
		public boolean hasNext(){
			return false;
			
		}
		
		public String next(){
			return null;
			
		}
	}
	
	public Iterator<String> iterator(){
		return null;
		
	}
	
	/**
	 * Returns a post order representation of the tree
	 * in the format we discussed in class
	 */
	public String toString(){
		return null;
		
	}
}



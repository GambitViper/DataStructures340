/**
 * @author Zach
 *
 */
import java.util.*;

public class SymbolTable implements Iterable<String>{

	/**
	 * A node used to build linked lists not utilizing the Java LinkedList class
	 */
	private class Node {

		private String key;
		private Object data;
		private Node next;

		private Node(String k, Object d, Node x) {
			key = k;
			data = d;
			next = x;
		}
		
		public String toString(){
			return data.toString();
		}
	}

	private Node table[];

	/**
	 * Constructs a Symbol Table of size s Does not handle resizing
	 * 
	 * @param s
	 */
	public SymbolTable(int s) {
		table = new Node[s];
	}

	/**
	 * Key k returns the hash function value for k
	 * 
	 * @param k
	 * @return the hash function value for k
	 */
	private int hash(String k) {
		int hash = 0;
		int len = k.length();

		for (int c = 0; c < len; c++) {
			hash += k.charAt(c);
		}

		return hash % table.length;
	}

	public boolean insert(String k) {
		// if k is not in the table create a new entry (with a null data value)
		// and return true if k is in the table return false
		if (!find(k)) {
			if (table[hash(k)] == null) {
				table[hash(k)] = new Node(k, null, null);
			} else {
				// Add to existing list
				addLast(k);
			}
			return true;
		}
		return false;

	}

	private void addLast(String k) {
		Node curr = table[hash(k)];
		while (curr.next != null) {
			curr = curr.next;
		}
		curr.next = new Node(k, null, null);
	}

	public boolean find(String k) {
		// return true if k is in the table otherwise return false
		if (table[hash(k)] == null) {
			return false;
		} else {
			Node check = find(table[hash(k)], k);
			if (check == null) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Node find takes a starting node and a lookup key Starting find Node
	 * 
	 * @param find
	 * @param lookup
	 * @return node of lookup key or null
	 */
	private Node find(Node find, String lookup) {
		while (find != null && !(find.key.equals(lookup))) {
			find = find.next;
		}
		return find;
	}

	public Object getData(String k) {
		// if k is in the table return the data
		// (which could be null) associated with k
		// if k is not in the table return null
		if (find(k)) {
			return find(table[hash(k)], k);
		} else {
			return null;
		}
	}

	public void setValue(String k, Object d) {
		// PRE: k is in the table
		// make d the data value associated with k
		Node set = find(table[hash(k)], k);

		if (set != null) {
			set.data = d;
		}
	}
	
	public boolean remove(String k) {
		// if k is in the table, return the entry
		// for k and return true //if k is not
		// in the table, return false
		if (find(k)){
			Node remove = table[hash(k)];
			if (remove.key.equals(k)){
				table[hash(k)] = remove.next;
			}else {
				//Initial position was not key == k
				while(!remove.next.key.equals(k)){
					remove = remove.next;
				}
				remove = remove.next;
			}
//			Node remove = find(table[hash(k)], k);
//			System.out.println(remove + "\n" + "\\/");
//			remove = remove.next;
//			System.out.println(remove + "\n");
			return true;
		}else {
			return false;
		}
	}

	public class STIterator implements Iterator<String> {
		// An iterator that
		// iterates through
		// the keys in the
		// table
		private Node curNode;
		private int idx;

		public STIterator() {
			idx = 0;
			curNode = table[idx];
		}

		public boolean hasNext() {
			if(idx < table.length || curNode != null){
				return true;
			}
			return false;
		}

		public String next() {
			// PRE: hasNext() 
			//The format of the string should be key:data where key is a key in the
			//symbol table and data is the String representation of the data associated 
			//with the key
			while(curNode == null && hasNext()){
				curNode = table[idx];
				idx++;
			}
			
			if(curNode == null){
				return "";
			}
			
			Node nextNode = curNode;
			curNode = curNode.next;
			
			return nextNode.key + " : " + nextNode.data.toString();
		}

		public void remove() { 
			// optional method not implemented
		}
	}

	public Iterator<String> iterator() {
		// return a new STIterator object
		return new STIterator();
	}

	public static void main(String[] args) {
//		// code to test SymbolTable
//		int tableSize = 26;
//		
//		SymbolTable table = new SymbolTable(tableSize);
//		table.insert("x");
//		table.setValue("x", new Integer("3"));
//		
//		table.insert("y");
//		table.setValue("y", new Integer("42"));
//		
//		table.insert("z");
//		table.setValue("z", new Integer("1703"));
//		
//		Iterator<String> iter = table.iterator();
//		while(iter.hasNext()){
//			System.out.println(iter.next());
//		}
//		
//		table.setValue("z", "Haha!");
//		table.remove("y");
//		
//		iter = table.iterator();
//		while(iter.hasNext()){
//			System.out.println(iter.next());
//		}

	}

}

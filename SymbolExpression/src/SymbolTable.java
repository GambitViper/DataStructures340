/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.util.*;

public class SymbolTable {

	/**
	 * A node used to build linked lists
	 * not utilizing the Java LinkedList class
	 */
	private class Node{
		
		private String key;
		private Object data;
		private Node next;
		
		private Node(String k, Object d, Node x){
			key = k;
			data = d;
			next = x;
		}
	}
	
	private Node table[];
	
	/**
	 * @param s is the size of the table. Does not handle resizing the table
	 */
	public SymbolTable(int s){
		
	}
	
	/**
	 * @param k - key
	 * @return the hash function value for k
	 */
	private int hash(String k){
		return -1;
	}
	
	public boolean insert(String k){
	//if k is not in the table create a new entry (with a null data value)
	//and return true if k is in the table return false
		return false;
	}
	
	public	boolean	find(String	k)	{	
	//return	true	if	k	is	in	the	table	otherwise	return	false	
		return false;
	}	
	public	Object	getData(String	k)	{	
	//if	k	is	in	the	table	return	the	data	(which	could	be	null)	associated	with	k	
	//if	k	is	not	in	the	table	return	null	
		return null;
	}	
	public	void	setValue(String	k,	Object	d)	{	
	//PRE:	k	is	in	the	table	
	//make	d	the	data	value	associated	with	k		
	}	
	
	public static void main(String[] args) {
		//code to test SymbolTable

	}

}

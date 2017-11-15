
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
	//add instance variables as needed.
	
	private class BTreeNode{
		private int count;
		private int keys[];
		private long children[];
		
		//constructors and other method
	}
	
	public BTree(String filename, int bsize){
		//bsize is the block size. This value is used to calculate the order of the B+Tree
		//all B+Tree nodes will use bsize bytes
		//make a new B+tree
	}
	
	public BTree(String filename){
		//open an existing B+Tree
	}
	
	public boolean insert(int key, long addr){
		/*
		 * If key is not a duplicate add, key to the B+tree
		 * addr (in DBTable) is the address of the row that contains the key
		 * return true if the key is added
		 * return false if the key is a duplicate
		 */
		return false;
	}
	
	public long remove(int key){
		/*
		 * If the key is in the Btree, remove the key and return the address of the row
		 * return 0 if the key is not found in the B+tree
		 */
		return key;
	}
	
	public long search(int k){
		/*
		 * This is an equality search
		 * 
		 * If the key is found return the address of the row with the key
		 * otherwise return 0
		 */
		return 0;
	}
	
	public LinkedList<Long> rangeSearch(int low, int high){
		//PRE:low <= high
		/*
		 * return a list of row addresses for all keys in the range low to high inclusive
		 * return an empty list when no keys are in the range
		 */
		return null;
	}
	
	public void print(){
		//print the B+Tree to standard output
		//print one node per line
	}
	
	public void close(){
		//close the B+tree. The tree should not be accessed after close is called
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

import java.io.*;
import java.util.*;

public class DBTable {

	// The file that stores the row in the table
	RandomAccessFile rows;
	// Head of the free list space for rows
	long free;
	int numOtherFields;
	int otherFieldLengths[];
	// Add other instance variables as needed
	BTree tree;

	private class Row {
		private int keyField;
		private char otherFields[][];
		private long addr;

		/*
		 * Each row consists of unique key and one or more character array
		 * fields.
		 * 
		 * Each character array field is a fixed length field (for example 10
		 * characters).
		 * 
		 * Each field can have a different length
		 * 
		 * Fields are padded with null characters so a field with a length of x
		 * characters always uses space for x character
		 */
		private Row(int keyField, char otherFields[][], long addr) {
			this.keyField = keyField;
			this.otherFields = otherFields;
			this.addr = addr;
		}

		private Row(long addr) throws IOException {
			rows.seek(addr);
			this.addr = addr;
			this.keyField = rows.readInt();
			this.otherFields = new char[numOtherFields][];
			for (int i = 0; i < numOtherFields; i++) {
				otherFields[i] = new char[otherFieldLengths[i]];
				for (int j = 0; j < otherFields[i].length; j++) {
					otherFields[i][j] = rows.readChar();
				}
			}
		}

		private void writeRow() throws IOException {
			rows.seek(addr);
			rows.writeInt(keyField);
			for (int i = 0; i < numOtherFields; i++) {
				for (int j = 0; j < otherFieldLengths[i]; j++) {
					rows.writeChar(otherFields[i][j]);
				}
			}
		}

		// Constructors and other Row methods
	}

	public DBTable(String filename, int fl[], int bsize) throws IOException {
		/*
		 * Use this constructor to create a new DBTable.
		 * 
		 * filename is the name of the file used to store the table fl is the
		 * lengths of the otherFields fl.length indicates how many other fields
		 * are part of the row bsize is the block size. It is used to calculate
		 * the order of the B+Tree
		 * 
		 * A B+Tree must be created for the key field in the table
		 * 
		 * If a file with name filename exists, the file should be deleted
		 * before the new file is created.
		 */
		File path = new File(filename);
		File treePath = new File(filename + "_tree");
		if (path.exists()) {
			path.delete();
			treePath.delete();
		}
		rows = new RandomAccessFile(path, "rw");
		rows.seek(0);
		numOtherFields = fl.length;

		rows.writeInt(numOtherFields);
		otherFieldLengths = new int[numOtherFields];
		for (int i = 0; i < numOtherFields; i++) {
			otherFieldLengths[i] = fl[i];
			rows.writeInt(fl[i]);
		}
		tree = new BTree(filename + "_ofTree", bsize);
		free = 0;
	}

	public DBTable(String filename) throws IOException {
		// Use this constructor to open an existing DBTable
		rows = new RandomAccessFile(filename, "rw");
		rows.seek(0);

		numOtherFields = rows.readInt();
		otherFieldLengths = new int[numOtherFields];

		for (int i = 0; i < numOtherFields; i++) {
			otherFieldLengths[i] = rows.readInt();
		}
		free = rows.readLong();
		tree = new BTree(filename + "_ofTree");
	}

	public boolean insert(int key, char fields[][]) throws IOException {
		// PRE:the length of each row is fields matches the expected length
		/*
		 * If a row with the key is not in the table, the row is added and the
		 * method returns true otherwise the row is not added and the method
		 * returns false.
		 * 
		 * The method must use the B+tree to determine if a row with the key
		 * exists.
		 * 
		 * If the row is added the key is also added into the B+tree
		 */
		if (tree.search(key) != 0) {
			return false;
		}

		long newAddr = findFree();
		tree.insert(key, newAddr);

		Row r = new Row(key, fields, newAddr);
		r.writeRow();

		return true;
	}

	public boolean remove(int key) throws IOException {
		/*
		 * If a row with the key is in the table it is removed and true is
		 * returned otherwise false is returned
		 * 
		 * The method must use the B+Tree to determine if a row with the key
		 * exists.
		 * 
		 * If the row is deleted the key must be deleted from the B+Tree
		 */

		return true;
	}

	public LinkedList<String> search(int key) throws IOException {
		/*
		 * If a row with the key is found in the table return a list of the
		 * other fields in the row. The string values in the list should not
		 * include the null characters.
		 * 
		 * If a row with the key is not found return an empty list
		 * 
		 * The method must use the equality search in B+Tree
		 */
		long addr = tree.search(key);
		if (addr == 0) {
			return new LinkedList<String>();
		}

		LinkedList<String> list = new LinkedList<>();
		Row r = new Row(addr);
		String str = Integer.toString(r.keyField);
		list.add(str);
		for (int i = 0; i < numOtherFields; i++) {
			str = "";
			for (int j = 0; j < otherFieldLengths[i] && r.otherFields[i][j] != '\0'; j++) {
				str += r.otherFields[i][j];
			}
			list.add(str);
		}
		
		return list;
	}

	public LinkedList<LinkedList<String>> rangeSearch(int low, int high) throws IOException {
		// PRE:low <= high
		/*
		 * For each row with a key that is in the range low to high inclusive a
		 * list of the fields in the row is added to the list returned by the
		 * call.
		 * 
		 * If there are no rows with a key in the range return an empty list
		 * 
		 * This method must use the range search in B+Tree
		 */
		LinkedList<LinkedList<String>> list = new LinkedList<LinkedList<String>>();

		for (long addr : tree.rangeSearch(low, high)) {
			LinkedList<String> subList = new LinkedList<>();
			Row r = new Row(addr);
			String str = Integer.toString(r.keyField);
			subList.add(str);
			for (int i = 0; i < numOtherFields; i++) {
				str = "";
				for (int j = 0; j < otherFieldLengths[i] && r.otherFields[i][j] != '\0'; j++) {
					str += r.otherFields[i][j];
				}
				subList.add(str);
			}
			list.add(subList);
		}

		return list;
	}
	
	private long findFree() throws IOException{
		long addr = 0;
		if(free == 0){
			addr = rows.length();
		}else {
			addr = free;
			rows.seek(free);
			free = rows.readLong();
		}
		return addr;
	}

	public void print() throws IOException {
		// Print the rows to standard output in ascending order (based on the
		// keys)
		// One row per line
		tree.print();
	}

	public void close() throws IOException {
		// close the DBTable. The table should not be used after it is closed
		rows.seek(4 * (numOtherFields + 1));
		rows.writeLong(free);
		tree.close();
	}

}
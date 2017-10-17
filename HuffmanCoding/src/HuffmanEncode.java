/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.io.*;
import java.util.*;

public class HuffmanEncode {

	private PriorityQueue<Item> queue;
	private String[] encodings;
	private int totalChars;

	private class Item implements Comparable<Item> {
		private int freq;
		private HuffmanTree data;

		public Item(HuffmanTree d, int f) {
			freq = f;
			data = d;
		}

		public int compareTo(Item o) {
			return this.freq - o.freq;
		}

		public String toString() {
			return data.toString();
		}

	}

	private void buildTree() {
		while (queue.size() > 1) {
			Item left = queue.poll();
			Item right = queue.poll();

			HuffmanTree merge = new HuffmanTree(left.data, right.data, (char) 128);

			Item putBack = new Item(merge, left.freq + right.freq);
			queue.add(putBack);
		}
	}

	private void readFile(String filename) {
		int[] frequencies = new int[128];
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int currChar = br.read();
			while (currChar != -1) {
				if (currChar < 128) {
					frequencies[currChar]++;
					totalChars++;
					currChar = br.read();
				} else {
					currChar = br.read();
				}
			}
			for (int i = 0; i < frequencies.length; i++) {
				if (frequencies[i] != 0) {
					queue.add(new Item(new HuffmanTree((char) i), frequencies[i]));
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void encodeOut(String fout, String tree, String fin) {
		try {
			HuffmanOutputStream bitOut = new HuffmanOutputStream(fout, tree, totalChars);
			BufferedReader br = new BufferedReader(new FileReader(fin));
			int toEncode = br.read();
			while (toEncode != -1) {
				for (int i = 0; i < encodings[toEncode].length(); ++i) {
					bitOut.writeBit((int) (encodings[toEncode].charAt(i) - '0'));
				}
				toEncode = br.read();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new HuffmanEncode(args[0], args[1]);
	}

	/**
	 * Implements the huffman encoding algorithm private methods as needed
	 * 
	 * @param in
	 *            filename of input
	 * @param out
	 *            filename of output
	 */
	public HuffmanEncode(String in, String out) {
		queue = new PriorityQueue<Item>();
		readFile(in);
		buildTree();
		HuffmanTree tree = queue.poll().data;
		String treeStr = tree.toString();
		Iterator<String> iter = tree.iterator();
		encodings = new String[128];
		while (iter.hasNext()) {
			String pathEncode = iter.next();
			System.out.println("path encode: " + pathEncode);
			encodings[pathEncode.charAt(0)] = pathEncode.substring(1);
		}
		encodeOut(out, treeStr, in);
	}
}

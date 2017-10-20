import java.io.*;
import java.util.*;

public class HuffmanDecode {

	private Stack<HuffmanTree> stack;
	private int totalChars;

	/**
	 * Implements the huffman decoding algorithm private methods as needed
	 * 
	 * @param in
	 * @param out
	 */
	public HuffmanDecode(String in, String out) {
		HuffmanInputStream decode = new HuffmanInputStream(in);
		String treeToBuild = decode.getTree();
		totalChars = decode.totalChars();
		System.out.println("Initial: " + treeToBuild);
		HuffmanTree rebuilt = new HuffmanTree(treeToBuild, (char) 128);//rebuildTree(treeToBuild);
		System.out.println("Rebuilt: " + rebuilt);
		decodeOut(decode, rebuilt);

	}
	
	private void decodeOut(HuffmanInputStream stream, HuffmanTree tree){
		while(totalChars > 0){
			
			totalChars--;
		}
	}

	public static void main(String args[]) {
		new HuffmanDecode(args[0], args[1]);
	}

}

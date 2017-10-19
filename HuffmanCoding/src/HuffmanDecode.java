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
		HuffmanTree rebuilt = rebuildTree(treeToBuild);
		System.out.println("Rebuilt: " + rebuilt);
		decodeOut(decode, rebuilt);

	}

	private HuffmanTree rebuildTree(String tree) {
		stack = new Stack<>();
		for (int i = 0; i < tree.length(); ++i) {
			//System.out.println("Character " + tree.charAt(i) + " ");
			if (tree.charAt(i) == (char) 128){//(list.size() > 1){ //|| tree.charAt(i) == (char) 128) {
				HuffmanTree right = stack.pop();
				HuffmanTree left = stack.pop();

				//System.out.println("Merging: " + left + " /merge\\ " + right);
				HuffmanTree merge = new HuffmanTree(left, right, (char) 128);

				stack.push(new HuffmanTree(left, right, (char) 128));
			}else {
			//if (tree.charAt(i) != (char) 128) {
				// System.out.println("char added: " + tree.charAt(i));
				stack.push(new HuffmanTree(tree.charAt(i)));
			}
		}
		return stack.pop();
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

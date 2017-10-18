import java.io.*;
import java.util.*;

public class HuffmanDecode {
	
	private LinkedList<HuffmanTree> list;
	private int totalChars;
	
	/**
	 * Implements the huffman decoding algorithm
	 * private methods as needed
	 * @param in
	 * @param out
	 */
	public HuffmanDecode(String in, String out){
		HuffmanInputStream decode = new HuffmanInputStream(in);
		String treeToBuild = decode.getTree();
		totalChars = decode.totalChars();
		System.out.println("Initial: " + treeToBuild);
		HuffmanTree rebuilt = rebuildTree(treeToBuild);
		System.out.println("Rebuilt: " + rebuilt);
		
	}
	
	private HuffmanTree rebuildTree(String tree){
		list = new LinkedList<>();
		for(int i = 0; i < tree.length(); ++i){
			if(tree.charAt(i) != (char) 128){
				//System.out.println("char added: " + tree.charAt(i));
				list.add(new HuffmanTree(tree.charAt(i)));
			}else {
				if(list.size() > 1){
					HuffmanTree left = list.poll();
					HuffmanTree right = list.poll();
					
					HuffmanTree merge = new HuffmanTree(left, right, (char) 128);
					
					list.add(merge);
				}
			}
		}
		return list.poll();
	}
	
	/*
	 * 		Item left = queue.poll();
			Item right = queue.poll();

			HuffmanTree merge = new HuffmanTree(left.data, right.data, (char) 128);

			Item putBack = new Item(merge, left.freq + right.freq);
			queue.add(putBack);
	 */
	
	public static void main(String args[]){
		new HuffmanDecode(args[0], args[1]);
	}

}

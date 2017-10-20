import java.io.*;

public class HuffmanDecode {

	private int totalChars;

	/**
	 * Implements the Huffman decoding algorithm private methods as needed
	 * 
	 * @param in
	 * @param out
	 */
	public HuffmanDecode(String in, String out) {
		HuffmanInputStream decode = new HuffmanInputStream(in);
		String treeToBuild = decode.getTree();
		totalChars = decode.totalChars();
		HuffmanTree rebuilt = new HuffmanTree(treeToBuild, (char) 128);
		decodeOut(decode, rebuilt, out);

	}

	private void decodeOut(HuffmanInputStream stream, HuffmanTree tree, String outFile) {
		try {
			PrintWriter fout = new PrintWriter(outFile);
			while (totalChars > 0) {
				if (!(tree.atLeaf())) {
					int cmp = stream.readBit();
					if (cmp == 1) {
						tree.moveRight();
					} else {
						tree.moveLeft();
					}
				} else {
					fout.write(tree.current());
					tree.moveRoot();
					totalChars--;
				}
			}
			fout.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new HuffmanDecode(args[0], args[1]);
	}

}

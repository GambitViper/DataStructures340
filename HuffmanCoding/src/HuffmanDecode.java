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
		//Gets the post-order representation of the tree from the input stream
		String treeToBuild = decode.getTree();
		//Stores the total number of characters it has to look to construct from the binary / compressed file
		totalChars = decode.totalChars();
		//Rebuilds the tree using the post-order representation
		HuffmanTree rebuilt = new HuffmanTree(treeToBuild, (char) 128);
		//Takes the input stream, the rebuilt representation and the desired destination to decompress to
		decodeOut(decode, rebuilt, out);

	}

	/**
	 * Work horse of the decode program, this decodes a compressed file to a desired location
	 * @param stream - input stream used to read bits at a time
	 * @param tree - rebuilt tree used to traverse and find character encodings
	 * @param outFile - desired output location for the new decompressed file
	 */
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

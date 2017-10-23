/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.io.*;

public class HuffmanOutputStream extends BitOutputStream {

	private int currentByte;
	private int bitCount;

	public HuffmanOutputStream(String filename, String tree, int totalChars) {
		super(filename);
		try {
			d.writeUTF(tree);
			d.writeInt(totalChars);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Writes bit at a time, keeps a counter for when to write out as a byte
	public void writeBit(int bit) {
		currentByte += bit << bitCount;
		bitCount++;
		if (bitCount == 8) {
			writeByte();
		}
	}

	//Writes the bit stream out as a full byte regardless of how many full characters were encoded into the byte
	private void writeByte() {
		try {
			d.write(currentByte);
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentByte = 0;
		bitCount = 0;
	}

	//Closes the data to avoid leaks and writes out the final bits if they didn't fill a full byte
	public void close() {
		if(bitCount > 0){
			writeByte();
		}
		try {
			d.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

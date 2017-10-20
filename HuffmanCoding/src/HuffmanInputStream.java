/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.io.*;

public class HuffmanInputStream extends BitInputStream {

	private String tree;
	private int totalChars;
	private int currentByte;
	private int bitsread;
	private int[] bite;

	public HuffmanInputStream(String filename) {
		super(filename);
		try {
			tree = d.readUTF();
			totalChars = d.readInt();
		} catch (IOException e) {

		}
	}

	public int readBit() {
		if(bitsread >= 0){
			bitsread--;
			int store = bite[7 - bitsread];
			bite[7 - bitsread] = 0;
			return store;
		}else {
			readByte();
			bitsread--;
			int store = bite[7 - bitsread];
			bite[7 - bitsread] = 0;
			return store;
		}
	}

	private void readByte() {
		bite = new int[8];
		bitsread = 0;
		try {
			while (bitsread < 7) {
				currentByte = d.read();
				bite[7 - bitsread] = currentByte % 2;
				currentByte %= 2;
				currentByte /= 2;
				bitsread++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getTree() {
		return tree;
	}

	public int totalChars() {
		return totalChars;
	}

	public void close() {

	}

}

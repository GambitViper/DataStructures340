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

	public HuffmanInputStream(String filename) {
		super(filename);
		try {
			tree = d.readUTF();
			totalChars = d.readInt();
			readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int readBit() {
		if(currentByte == -1){
			close();
			return -1;
		}
		int bit = currentByte % 2;
		currentByte /= 2;
		
		bitsread++;
		if(bitsread == 8){
			readByte();
		}
		return bit;
	}

	private void readByte() {
		bitsread = 0;
		try {
			currentByte = d.readUnsignedByte();			
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
		try{
			d.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

}

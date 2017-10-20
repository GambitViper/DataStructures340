/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.io.*;
public class HuffmanOutputStream extends BitOutputStream{
	
	private int currentByte;
	private int bitCount;
	
	public HuffmanOutputStream(String filename, String tree, int totalChars){
		super(filename);
		try{
			d.writeUTF(tree);
			d.writeInt(totalChars);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void writeBit(int bit){
		currentByte += bit << bitCount;
		bitCount++;
		
		if(bitCount == 8){
			writeByte();
		}
	}
	
	public void close(){
		
	}
	
	private void writeByte(){
		try{
			d.write(currentByte);
		} catch(IOException e){
			e.printStackTrace();
		}
		currentByte = 0;
		bitCount = 0;
	}
}

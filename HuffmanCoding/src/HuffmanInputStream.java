/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.io.*;
public class HuffmanInputStream extends BitInputStream{
	
		private String tree;
		private int totalChars;
		
		public HuffmanInputStream(String filename){
			super(filename);
			try{
				tree = d.readUTF();
				totalChars = d.readInt();
			}
			catch (IOException e){
				
			}
		}
		
		public int readBit(){
			return -1;
		}
		
		public String getTree(){
			return tree;
		}
		
		public int totalChars(){
			return totalChars;
		}
		
		public void close(){
			
		}
		
		
}

/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.io.*;
import java.util.*;

public class Assignments {

    public static void main(String[] args) throws IOException {
        BufferedReader fin = new BufferedReader(new FileReader(args[0]));
        SymbolTable symbolTable = new SymbolTable(10);
        String line = "";
        
        //Loop through the lines
        while ((line = fin.readLine()) != null) {
            String[] expression = line.split(" = ");
            symbolTable.insert(expression[0]);
            //Convert right side of '=' to expression tree
            ExpressionTree expTree = new ExpressionTree(expression[1]);
            //Evaluate and add as value for the assigning key
            int evaluation = expTree.evaluate(symbolTable);
            symbolTable.setValue(expression[0], evaluation);
        }
        
        //Output results
        Iterator<String> iter = symbolTable.iterator();
        while(iter.hasNext()){
        	System.out.println(iter.next());
        }
        
        fin.close();
    }
}

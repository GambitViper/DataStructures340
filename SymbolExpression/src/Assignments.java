/**
 * 
 */

/**
 * @author Zach
 *
 */
import java.io.*;

public class Assignments {

    public static void main(String[] args) throws IOException {
        BufferedReader fin = new BufferedReader(new FileReader(args[0]));
        SymbolTable symbolTable = new SymbolTable(10);
        String line = "";
        
        //Loop through the lines
        while ((line = fin.readLine()) != null) {
            String[] expression = line.split(" = ");
            symbolTable.insert(expression[0].trim());
            //Convert right side of '=' to expression tree
            ExpressionTree expTree = new ExpressionTree(expression[1].trim());
            //Evaluate and add as value for the assigning key
            int evaluation = expTree.evaluate(symbolTable);
            symbolTable.setValue(expression[0], evaluation);
        }
        
        //Output results
        for (String string : symbolTable) {
            System.out.println(string);
        }
        
        fin.close();
    }
}

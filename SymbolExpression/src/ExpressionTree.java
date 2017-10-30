import java.io.IOException;

/**
 * 
 */

/**
 * @author Zach
 *
 */
public class ExpressionTree {

	private class Node {
		private Node left;
		private String data;
		private Node right;

		private Node(Node l, String d, Node r) {
			left = l;
			data = d;
			right = r;
		}
	}

	private Node root;

	public ExpressionTree(String exp) {
		// PRE: exp is a legal infix expression
		// Build an expression tree from the expression exp

	}

	public int evaluate(SymbolTable t) {
		// return the int value of the expression tree 
		//t is used to lookup values of variables 
		
		return evaluate(t, root);
	}

	private int evaluate(SymbolTable t, Node r) {
		// return the int value of the expression tree with root r
		// t is used to lookup values of variables
		
		return -1;
	}

	public String toPostfix() {
		// return the postfix representation of the expression tree
		
		return toPostfix(root);
	}

	private String toPostfix(Node r) {
		// return the postfix representation of the tree with root r
		
		return null;
	}

	public String toInfix() {
		// return the fully parenthesized infix
		// representation of the expression tree

		return toInfix(root);
	}

	private String toInfix(Node r) {
		// return the fully parenthesized infix representation of the tree with
		// root r
		
		return null;
	}
	
	public static void main(String args[]) throws IOException {
		//used to test expression tree
	}

}

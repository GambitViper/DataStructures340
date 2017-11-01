import java.io.*;
import java.util.*;

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
		Stack<String> operators = new Stack<>();
		Stack<String> operands = new Stack<>();
		String[] tokens = exp.trim().split(" ");
		for(int i = 0; i < tokens.length; i++){
			if(isOperand(tokens[i].charAt(0))){
				operands.push(tokens[i]);
			}else {
				operators.push(tokens[i]);
			}
		}
		System.out.println("Operators: " + operators);
		System.out.println("Operands : " + operands);
		
		

	}
	
	private boolean isOperand(char o){
		return precedence(o) == 0;
	}
	
	private int precedence(char c){
		if(c == '+'|| c == '-') return 1;
		if(c == '*' || c == '/' || c == '%') return 2;
		if(c == '^') return 3;
		if(c == '!') return 4;
		if(c == '(') return 4;
		if(c == ')') return -1;
		return 0;
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
		if (r == null){
			return "";
		}
		return toPostfix(r.left) + toPostfix(r.right) + r.data;
	}

	public String toInfix() {
		// return the fully parenthesized infix
		// representation of the expression tree

		return toInfix(root);
	}

	private String toInfix(Node r) {
		// return the fully parenthesized infix representation of the tree with
		// root r
		if (r == null){
			return "";
		}
		return "(" + toInfix(r.left) + r.data + toInfix(r.right) + ")";
	}
	
	public static void main(String args[]) throws IOException {
		//used to test expression tree
		String exp = "A + B * C ^ ( D - E ) ^ F + H / I";
		ExpressionTree tree = new ExpressionTree(exp);
	}

}

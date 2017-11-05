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
		
		public String toString(){//TODO Remove me
			return data;
		}
	}

	private Node root;

	private ExpressionTree(ExpressionTree operand1, String operator, ExpressionTree operand2) {
		root = new Node(operand1.root, operator, operand2.root);
	}

	private ExpressionTree(Node r) {
		root = r;
	}
	
	private Stack<String> operators;
	private Stack<ExpressionTree> operands;

	public ExpressionTree(String exp) {
		// PRE: exp is a legal infix expression
		// Build an expression tree from the expression exp

		// Operators and Operands stacks
		operators = new Stack<>();
		operands = new Stack<>();

		// ~~~~~~~~~~~~~~~~~~~~Psuedo Code for Reverse Polish~~~~~~~~~~~~~~~~
		// for each token in the reversed postfix expression:
		// if token is an operator:
		// push token onto the operator stack
		// pending_operand <- False
		// else if token is an operand:
		// operand <- token
		// if pending_operand is True:
		// while the operand stack is not empty:
		// operand_1 <- pop from the operand stack
		// operator <- pop from the operator stack
		// operand <- evaluate operator with operand_1 and operand
		// push operand onto the operand stack
		// pending_operand <- True
		// result <- pop from the operand stack
		
		for(String token : exp.split(" ")) {
			System.out.println("Operators" + operators);//TODO remove print statement
			System.out.print("Operands");//TODO remove print statement
			printOperands();
			if (isOperand(token)) {
				System.out.println("Pushing to Operands: " + token);//TODO remove print statement
				operands.push(new ExpressionTree(new Node(null, token, null)));
			} else if(token.equals("(")){
				System.out.println("Pushing to Operators: " + token);//TODO remove print statement
				operators.push(token);
			} else if(token.equals(")")){
				while(!operators.isEmpty() && !operators.peek().equals("(")){
					operands.push(operate());
				}
				System.out.println("Popping Open: " + operators.peek());//TODO remove print statement
				operators.pop();
			}else if(token.equals("^") && operators.peek().equals("^")){
				operators.push(token);
			}else {
				while(!operators.isEmpty() && precedence(token) <= precedence(operators.peek())){
					operands.push(operate());
				}
				System.out.println("Pushing to Operators: " + token);//TODO remove print statement
				operators.push(token);
			}
		}

		// Check if operators stack is empty if not perform remaining operations
		while (!operators.isEmpty()) {
			operands.push(operate());
		}

		// Return the built Expression Tree
		root = operands.pop().root;

	}
	
	private ExpressionTree operate(){
		ExpressionTree right = operands.pop();
		ExpressionTree left = operands.pop();
		String op = operators.pop();
		ExpressionTree merge = new ExpressionTree(left, op, right);
		return merge;
	}
	
	private void printOperands(){//TODO Remove me
		System.out.print("[");
		for (ExpressionTree t : operands){
			System.out.print(t.root + ", ");
		}
		System.out.print("]");
		System.out.println();
	}

	private boolean isOperand(String o) {
		return precedence(o) == 0;
	}

	private int precedence(String str) {
		char c = str.charAt(0);
		if (c == '+' || c == '-') {
			// + or -
			return 1;
		}
		if (c == '*' || c == '/' || c == '%') {
			// * or / or %
			return 2;
		}
		if (c == '^') {
			// ^
			return 3;
		}
		if (c == '!') {
			// !
			return 4;
		}
		if (c == '(' || c == ')'){
			return -1;
		}
		// Operand
		return 0;
	}

	public int evaluate(SymbolTable t) {
		// return the int value of the expression tree
		// t is used to lookup values of variables

		return evaluate(t, root);
	}

	private int evaluate(SymbolTable t, Node r) {// TODO write evaluate
		// return the int value of the expression tree with root r
		// t is used to lookup values of variables
		//if(isDigit(r.data))
		return -1;
	}

	public String toPostfix() {
		// return the postfix representation of the expression tree

		return toPostfix(root);
	}

	private String toPostfix(Node r) {
		// return the postfix representation of the tree with root r
		if (r == null) {
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
		if (r == null) {
			return "";
		}
		if (r.left == null && r.right == null) {
			return r.data;
		} else {
			return "(" + toInfix(r.left) + r.data + toInfix(r.right) + ")";
		}
	}

	public static void main(String args[]) throws IOException {
		// used to test expression tree
		String exp = "A + B * C ^ ( D - E ) ^ F + H / I";
		ExpressionTree tree = new ExpressionTree(exp);
		System.out.println("Infix: ");
		System.out.println(tree.toInfix());
		System.out.println("Postfix: ");
		System.out.println(tree.toPostfix());
	}

}

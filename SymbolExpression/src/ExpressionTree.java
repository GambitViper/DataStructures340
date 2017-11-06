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

		// public String toString(){
		// return data;
		// }
	}

	private Node root;

	private ExpressionTree(Node operand1, String operator, Node operand2) {
		root = new Node(operand1, operator, operand2);
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

		for (String token : exp.split(" ")) {
			// System.out.println("Operators" + operators);
			// System.out.print("Operands");
			// printOperands(); Debugger print methods
			if (isOperand(token)) {
				// System.out.println("Pushing to Operands: " + token);
				operands.push(new ExpressionTree(new Node(null, token, null)));
			} else if (token.equals("(")) {
				// System.out.println("Pushing to Operators: " + token);
				operators.push(token);
			} else if (token.equals(")")) {
				while (!operators.isEmpty() && !operators.peek().equals("(")) {
					operands.push(operate());
				}
				// System.out.println("Popping Open: " + operators.peek());
				operators.pop();
			} else if (!operators.isEmpty() && token.equals("^") && operators.peek().equals("^")) {
				operators.push(token);
			} else {
				while (!operators.isEmpty() && precedence(token) <= precedence(operators.peek())) {
					operands.push(operate());
				}
				// System.out.println("Pushing to Operators: " + token);
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

	private ExpressionTree operate() {
		ExpressionTree right = operands.pop();
		ExpressionTree left;
		String op = operators.pop();
		ExpressionTree merge;
		if (op.equals("!")) {
			merge = new ExpressionTree(null, op, right.root);
		} else {
			left = operands.pop();
			merge = new ExpressionTree(left.root, op, right.root);
		}
		return merge;
	}

	// private void printOperands(){
	// System.out.print("[");
	// for (ExpressionTree t : operands){
	// System.out.print(t.root + ", ");
	// }
	// System.out.print("]");
	// System.out.println();
	// }

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
		if (c == '(' || c == ')') {
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

	private int evaluate(SymbolTable t, Node r) {
		// return the int value of the expression tree with root r
		// t is used to lookup values of variables
		// if(isDigit(r.data))
		if (isNum(r.data)) {
			return Integer.parseInt(r.data);
		} else if (t.find(r.data)) {
			// Value located in symbol table
			return Integer.parseInt(t.getData(r.data).toString());
		} else if (!isOperand(r.data)) {
			// r.data is an operator
			if (r.data.equals("!")) {
				return evaluate(0, r.data, evaluate(t, r.right));
			} else {
				return evaluate(evaluate(t, r.left), r.data, evaluate(t, r.right));
			}
		} else {
			// Uninitialized variable
			return 0;
		}
	}

	private int evaluate(int left, String operator, int right) {
		switch (operator.charAt(0)) {
		case '+':
			return left + right;
		case '-':
			return left - right;
		case '*':
			return left * right;
		case '/':
			return left / right;
		case '%':
			return left % right;
		case '^':
			return (int) Math.pow(left, right);
		case '!':
			return right * -1;
		}
		return 0;
	}

	public static boolean isNum(String strNum) {
		boolean isNum = true;
		try {

			Double.parseDouble(strNum);

		} catch (NumberFormatException e) {
			isNum = false;
		}
		return isNum;
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
		// String exp = "A + B * C ^ ( D - E ) ^ F + H / I";
		// exp = "! ( A + B )";
		// ExpressionTree tree = new ExpressionTree(exp);
		// System.out.println("Infix: ");
		// System.out.println(tree.toInfix());
		// System.out.println("Postfix: ");
		// System.out.println(tree.toPostfix());
	}

}

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

	private ExpressionTree(ExpressionTree operand1, String operator, ExpressionTree operand2) {
		root = new Node(operand1.root, operator, operand2.root);
	}

	private ExpressionTree(Node r) {
		root = r;
	}

	public ExpressionTree(String exp) {
		// PRE: exp is a legal infix expression
		// Build an expression tree from the expression exp

		// The expression split up into tokens into a stack
		Stack<String> expStack = generateTokens(exp);

		// Operators and Operands stacks
		Stack<String> operators = new Stack<>();
		Stack<ExpressionTree> operands = new Stack<>();

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
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Attempt 1~~~~~~~~~~~~~~~~~~~~~~~~~~
		// while (!expStack.isEmpty()) {
		// boolean pendingOperand = false;
		// String token = expStack.pop();
		// if(!isOperand(token.charAt(0))){//Token is operator
		// operators.push(token);
		// pendingOperand = true;
		// }else if(isOperand(token.charAt(0))){//Token is operand
		// ExpressionTree curNode = new ExpressionTree(new Node(null, token,
		// null));
		// if(pendingOperand){
		// while(!operands.isEmpty()){//operand stack is not empty
		// ExpressionTree operand1 = operands.pop();
		// String operator = operators.pop();
		// ExpressionTree operand2 = curNode;
		// operands.push( new ExpressionTree(operand1, operator, operand2));
		// }
		// pendingOperand = false;
		// }
		// }
		//
		// }
		while (!expStack.isEmpty()) {
			String token = expStack.pop();
			if (isOperand(token.charAt(0))) {
				operands.push(new ExpressionTree(new Node(null, token, null)));
				System.out.println(operands);
			} else if (!isOperand(token.charAt(0))) {
				// token is an operator push if not 2 operands
				// OR higher precedence operator
				if (operands.size() < 2) {
					operators.push(token);
					System.out.println(operators);
				} else if (precedence(token.charAt(0)) > precedence(operators.peek().charAt(0))) {
					operators.push(token);
					System.out.println(operators);
				} else {
					// Not higher precedence perform calculation first
					String operator = operators.pop();
					System.out.println(operators);
					ExpressionTree operand1 = operands.pop();
					ExpressionTree operand2 = operands.pop();
					ExpressionTree operation = new ExpressionTree(operand1, operator, operand2);
					operands.push(operation);
					System.out.println(operands);
				}
			}
		}

		root = operands.pop().root;

	}

	private Stack<String> generateTokens(String exp) {
		Stack<String> stack = new Stack<>();
		String[] tokens = exp.trim().split(" ");
		for (String t : tokens) {
			stack.push(t);
		}
		return stack;
	}

	private boolean isOperand(char o) {
		return precedence(o) == 0;
	}

	private int precedence(char c) {
		if (c == '+' || c == '-')
			return 1;
		if (c == '*' || c == '/' || c == '%')
			return 2;
		if (c == '^')
			return 3;
		if (c == '!')
			return 4;
		if (c == '(')
			return 4;
		if (c == ')')
			return -1;
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
		return "(" + toInfix(r.left) + r.data + toInfix(r.right) + ")";
	}

	public static void main(String args[]) throws IOException {
		// used to test expression tree
		String exp = "A + B * C ^ ( D - E ) ^ F + H / I";
		ExpressionTree tree = new ExpressionTree(exp);
	}

}

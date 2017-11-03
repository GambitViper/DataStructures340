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
		
		public String toString(){
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

//		// The expression split up into tokens into a stack
//		LinkedList<String> expStack = generateTokens(exp);

		// Operators and Operands stacks
		operators = new Stack<>();
		operands = new Stack<>();

		// Open parenthesis checker
//		boolean isParenthesis = false;

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
//			System.out.println("Token I'm looking at: " + token);
//			isParenthesis = false;
			System.out.println("Operators" + operators);
			System.out.print("Operands");
			printOperands();
			if (isOperand(token)) {
				System.out.println("Pushing to Operands: " + token);
				operands.push(new ExpressionTree(new Node(null, token, null)));
			} else if(token.equals("(")){
				System.out.println("Pushing to Operators: " + token);
				operators.push(token);
			} else if(token.equals(")")){
				while(!operators.isEmpty() && !operators.peek().equals("(")){
					operands.push(operate());
				}
				System.out.println("Popping Open: " + operators.peek());
				operators.pop();
			}else {
				while(!operators.isEmpty() && precedence(token) <= precedence(operators.peek())){
					operands.push(operate());
				}
				System.out.println("Pushing to Operators: " + token);
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
	
	private void printOperands(){
		System.out.print("[");
		for (ExpressionTree t : operands){
			System.out.print(t.root + ", ");
		}
		System.out.print("]");
		System.out.println();
	}

//	private boolean pushPriority(String token, String peek) {
//		if (comparePrecedence(token, peek) > 0) {
//			return true;
//		} else if (comparePrecedence(token, peek) < 0 && precedence(token) >= 3) {
//			return true;
//		} else {
//			return false;
//		}
//	}

//	private int comparePrecedence(String token, String peek) {
//		// If token is higher precedence than peeked return > 1
//		// Else if token is lower precedence than peeked or equal return < 1
//		int compare = precedence(token);
//		int compareTo = precedence(peek);
//		return compare > compareTo ? 1 : -1;
//	}

//	private boolean operationsPending(Stack<String> operators, Stack<ExpressionTree> operands) {
//		if (operands.isEmpty() || operators.isEmpty()) {
//			return false;
//		}
//		if (precedence(operators.peek()) == 4) {
//			return operands.size() >= 1;
//		} else {
//			return operands.size() >= 2;
//		}
//	}

	private void performOperation(ExpressionTree operand1, String operator, ExpressionTree operand2) {
//		String operator = operators.pop();

//		// Stop loop if open parenthesis for forced precedence case
//		if (precedence(operator) >= 5) {
//			return;
//		}

		ExpressionTree right = operand2;
		ExpressionTree left;
		if (precedence(operator) == 4) {
			left = null;
			ExpressionTree merged = new ExpressionTree(left, operator, right);
			operands.push(merged);
		} else {
			// Not unary minus
			left = operands.pop();
			ExpressionTree merged = new ExpressionTree(left, operator, right);
			operands.push(merged);
		}
		System.out.println("Merging: " + left.root.data + " -> " + operator + " <- " + right.root.data);

	}

//	private LinkedList<String> generateTokens(String exp) {
//		LinkedList<String> list = new LinkedList<>();
//		String[] tokens = exp.trim().split(" ");
//		for (String t : tokens) {
//			System.out.println("Adding " + t + " to expStack");
//			list.push(t);
//		}
//		return list;
//	}
	
// Expression Tree fail
//	if (operators.isEmpty() || pushPriority(token, operators.peek())) {
//	operators.push(token);
//	System.out.println("Pushing to Operators: " + token);
//} else {
//	// token has lesser precedence than current operator in
//	// operators stack
//	while (operationsPending(operators, operands) && !isParenthesis
//			&& !pushPriority(token, operators.peek())) {
//
//		if (precedence(token) < 0) {
//			// token is closing parenthesis
//			isParenthesis = true;
//		}
//
//		performOperation(operators, operands);
//	}
//	// Don't push for closing parenthesis
//	if (!isParenthesis) {
//		operators.push(token);
//	}
//}
//}

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

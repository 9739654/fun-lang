package mati.expression;

import mati.Token;
import mati.TokenKind;
import mati.Tokenizer;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ExpressionParser {

	static Tokenizer tokenizer = new Tokenizer().standard();

	private String source;
	private List<Token> tokens;
	private ExprNode tree;

	public ExpressionParser expression(String source) {
		this.source = source;
		return this;
	}

	public ExprNode parse() {
		Queue<Token> tokens = new ArrayDeque<>(tokenizer.process(source).getTokens());

		Stack<Token> operatorStack = new Stack<>();
		Stack<ExprNode> exprStack = new Stack<>();

		while (!tokens.isEmpty()) {
			Token t = tokens.poll();
			if (t.kind == TokenKind.Whitespace) {
				continue;
			}
			if (t.kind == TokenKind.OpenParenthesis) {
				operatorStack.push(t);
			} else if (t.kind.isLiteral()) {
				exprStack.push(new Literal(t));
			} else if (t.kind.isOperator()) {
				while (!operatorStack.isEmpty() &&
						operatorStack.peek().kind.precedence() >= t.kind.precedence()) {
					Token op = operatorStack.pop();
					ExprNode e2 = exprStack.pop();
					if (!op.kind.isUnary()) {
						ExprNode e1 = exprStack.pop();
						exprStack.push(new BinaryOperator(op, e1, e2));
					} else {
						exprStack.push(new UnaryOperator(op, e2));
					}
				}
				operatorStack.push(t);
			} else if (t.kind == TokenKind.CloseParenthesis) {
				while (operatorStack.peek().kind != TokenKind.OpenParenthesis) {
					Token op = operatorStack.pop();
					ExprNode e2 = exprStack.pop();

					if (operatorStack.peek().kind != TokenKind.OpenParenthesis) {
						if (op.kind.isUnary()) {
							System.err.println("ERROR: unary operator but no open parenthesis");
						}
						ExprNode e1 = exprStack.pop();
						exprStack.push(new BinaryOperator(op, e1, e2));
					} else {
						exprStack.push(new UnaryOperator(op, e2));
					}
				}
				operatorStack.pop();
			} else {
				System.err.println("ERROR: unexpected token");
			}
		}

		while (!operatorStack.isEmpty()) {
			Token op = operatorStack.pop();
			ExprNode e2 = exprStack.pop();
			if (op.kind.isUnary()) {
				exprStack.push(new UnaryOperator(op, e2));
			} else {
				ExprNode e1 = exprStack.pop();
				exprStack.push(new BinaryOperator(op, e1, e2));
			}
		}

		tree = exprStack.pop();
		return tree;
	}

}

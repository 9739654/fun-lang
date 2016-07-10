package mati.expression;

import mati.Token;

public class UnaryOperator extends ExprNode {
	public UnaryOperator(Token operator, ExprNode expr) {
		super(operator, expr);
	}
}

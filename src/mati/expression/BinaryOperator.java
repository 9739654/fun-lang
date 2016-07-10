package mati.expression;

import mati.Token;

public class BinaryOperator extends ExprNode {
	public BinaryOperator(Token operator, ExprNode left, ExprNode right) {
		super(operator, left, right);
	}
}

package mati.expression;

import mati.Token;

public abstract class ExprNode {
	Token operator;
	ExprNode left;
	ExprNode right;

	protected ExprNode() {
		this(null, null, null);
	}

	public ExprNode(Token operator) {
		this(operator, null, null);
	}

	public ExprNode(Token operator, ExprNode expr) {
		this(operator, expr, null);
	}

	public ExprNode(Token operator, ExprNode left, ExprNode right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	protected String toString(String indent) {
		String out = indent + operator.sequence + '\n';

		if (left != null) {
			out += indent + '\t' + left.toString(indent + '\t');
		}
		if (right != null) {
			out += indent + '\t' + right.toString(indent + '\t');
		}
		return out;
	}

	@Override
	public String toString() {
		return toString("");
	}
}

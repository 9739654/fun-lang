package mati.expression;

import java.util.ArrayList;
import java.util.List;

public class Block extends ExprNode {
	public final List<ExprNode> children = new ArrayList<>();

	public void add(ExprNode node) {
		children.add(node);
	}

	@Override
	protected String toString(String indent) {
		String out = indent + operator.sequence + '\n';

		for (ExprNode node : children) {
			if (node != null) {
				out += indent + '\t' + node.toString(indent + '\t');
			}
		}

		return out;
	}
}

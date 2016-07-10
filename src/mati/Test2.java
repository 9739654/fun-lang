package mati;

import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Test2 {

	class Node {
		Node root;
		Node previous;
		Map<Node, Predicate<Token>> branches = new TreeMap<>();
		Map<String, String> groups = new TreeMap<>();

		Node branch(Predicate<Token> predicate) {
			Node n = new Node();
			n.root = this;
			branches.put(n, predicate);
			return n;
		}

		Node and(Predicate<Token> predicate) {
			Node n = new Node();
			n.root = this.root;
			branches.put(n, predicate);
			return n;
		}

		Node and(Predicate<Token> predicate, String name) {
			Node n = new Node();
			n.root = this.root;
			branches.put(n, predicate);
			return n;
		}

		Node accept(Consumer<String> consumer) {
			//TODO
			return this;
		}

		Node fi() {
			return root;
		}

		void test(Deque<Token> tokens) {
			Token current = tokens.peek();
			for (Map.Entry<Node, Predicate<Token>> entry : branches.entrySet()) {
				Predicate<Token> test = entry.getValue();
				if (test.test(current)) {
					// go!!!

				}
			}
		}
	}

	Node module;

	{
		module = new Node();
		module.branch(Token.isKeyword("import")).and(Token.isWhiteSpace());
		module.branch(Token.isSymbol('#'));
	}

	void start() {

	}

	public static void main(String[] args) {
		new Test2().start();
	}
}

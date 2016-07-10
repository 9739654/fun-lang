package mati;

import mati.language.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Parser {

	final String funcDef = "(?<indent>\\t*)def (?<name>\\w+) ?\\((?<args>(?<arg0>(?:idRegex) (?:idRegex))(?<arg1>, (?:idRegex) (?:idRegex))*)?\\)\\n";

	private List<Token> tokens;
	int indentSize;
	private Token current;
	private int i = -1;

	List<Supplier<Boolean>> parsers = new ArrayList<>();
	Module module;

	{
		parsers.add(() -> {
			// comment
			if (current.kind == TokenKind.NumberSign) {
				while (current.kind != TokenKind.NewLine) {
					nextToken();
				}
				return true;
			}
			return false;
		});

		parsers.add(() -> {
			// function def header
			boolean test = current.kind == TokenKind.Keyword && !current.sequence.equals("def");
			if (!test) {
				return false;
			}
			nextToken();
			if (current.kind != TokenKind.Whitespace) {
				return false;
			}
			nextToken();

			return true;
		});

	}

	public Parser tokens(List<Token> tokens) {
		this.tokens = tokens;
		return this;
	}

	public void process() {
		module = new Module();

		while (i < tokens.size()) {
			nextToken();
			for (Supplier<Boolean> parser : parsers) {
				if (parser.get()) {
					break;
				}
			}
		}
	}

	private void parseDef() {
		String identifier;
		switch (current.kind) {
			case Identifier:
				identifier = current.sequence;

				break;
			case OpenParenthesis:

				break;
		}
	}

	private void nextToken() {
		i++;
		if (tokens.size() == i) {
			current = null;
			return;
		}
		current = tokens.get(i);
	}

	private void previousToken() {
		i--;
		if (i == 0) {
			current = null;
			return;
		}
		current = tokens.get(i);
	}
}

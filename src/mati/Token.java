package mati;

import java.util.function.Predicate;

public class Token {
	public final TokenKind kind;
	public final String sequence;
	public final int line;
	public final int pos;

	public Token(TokenKind kind, String sequence) {
		this(kind, sequence, -1);
	}

	public Token(TokenKind kind, String sequence, int line) {
		this(kind, sequence, line, -1);
	}

	public Token(TokenKind kind, String sequence, int line, int pos) {
		this.kind = kind;
		this.sequence = sequence;
		this.line = line;
		this.pos = pos;
	}

	static Predicate<Token> isWhiteSpace() {
		return t -> t.kind == TokenKind.Whitespace;
	}

	static Predicate<Token> isKeyword(String name) {
		return t -> t.kind == TokenKind.Keyword && t.sequence.equals(name);
	}

	static Predicate<Token> isHash() {
		return t -> t.kind == TokenKind.NumberSign;
	}

	static Predicate<Token> isSymbol(char c) {
		return t -> t.kind == TokenKind.Symbol && t.sequence.equals(c);
	}

	@Override
	public String toString() {
		return kind + ": " + sequence;
	}
}

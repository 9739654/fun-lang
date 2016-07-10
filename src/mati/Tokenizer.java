package mati;

import jdk.nashorn.internal.runtime.ParserException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

	private ArrayList<TokenInfo> tokenInfos;
	private ArrayList<Token> tokens;

	public Tokenizer() {
		tokenInfos = new ArrayList<>();
	}

	public void add(String regex, TokenKind kind) {
		tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), kind));
	}

	public Tokenizer process(String input) {
		tokens = new ArrayList<>();
		if (input.trim().equals("")) {
			return this;
		}
		while (!input.equals("")) {
			boolean match = false;
			for (TokenInfo info : tokenInfos) {
				Matcher m = info.regex.matcher(input);
				if (m.find()) {
					match = true;

					String token;
					if (m.groupCount() > 1) {
						token = m.group("data");
					} else {
						token = m.group();
					}
					tokens.add(new Token(info.token, token));
					input = m.replaceFirst("");
					break;
				}
			}
			if (!match) throw new ParserException("Unexpected character: " + input);
		}
		return this;
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public Tokenizer standard() {
		add("\\s", TokenKind.Whitespace);
		add("def|int|void|return", TokenKind.Keyword);

		add("[0-9]*\\.[0-9]+[fF]", TokenKind.Float);
		add("[0-9]*\\.[0-9]+", TokenKind.Double);
		add("[0-9]+L", TokenKind.Long);
		add("[0-9]+", TokenKind.Integer);
		add("\"(?<data>(?:[^\"\\\\]|\\\\.)*)\"", TokenKind.String);
		add("\\pL[\\pL0-9_]*", TokenKind.Identifier);

		// operators
		add("is", TokenKind.Is);
		add("in", TokenKind.In);
		add("or", TokenKind.Or);
		add("and", TokenKind.And);
		add("\\|\\|", TokenKind.Or);
		add("&&", TokenKind.And);
		add("==", TokenKind.EqualsSign);


		//add(" ", TokenKind.Space);
		add("\\!", TokenKind.Symbol);
		add("\"", TokenKind.Symbol);
		add("#", TokenKind.Symbol);
		add("\\$", TokenKind.Symbol);
		add("%", TokenKind.Symbol);
		add("&", TokenKind.Symbol);
		add("'", TokenKind.Symbol);
		add("\\(", TokenKind.Symbol);
		add("\\)", TokenKind.Symbol);
		add("\\*", TokenKind.Star);
		add("\\+", TokenKind.Plus);
		add(",", TokenKind.Comma);
		add("-", TokenKind.Minus);
		add("\\.", TokenKind.FullStop);
		add("\\/", TokenKind.Slash);

		add(":", TokenKind.Colon);
		add(";", TokenKind.Semicolon);
		add("<", TokenKind.LessThanSign);
		add("=", TokenKind.AssignSign);
		add(">", TokenKind.GreaterThanSign);
		add("\\?", TokenKind.QuestionMark);
		add("@", TokenKind.AtSign);

		add("\\[", TokenKind.OpenBracket);
		add("\\\\", TokenKind.Backslash);
		add("\\]", TokenKind.CloseBracket);
		add("\\^", TokenKind.Caret);
		add("_", TokenKind.Underscore);

		add("\\{", TokenKind.OpenBrace);
		add("\\|", TokenKind.VerticalBar);
		add("\\}", TokenKind.CloseBrace);
		add("~", TokenKind.Tilde);

		add("\\.", TokenKind.Other);
		add("", TokenKind.Dedent);

		return this;
	}
}

package mati;

import jdk.nashorn.internal.runtime.ParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class App {

	private Tokenizer tokenizer;
	private String source;

	public App config() {
		tokenizer = new Tokenizer();
		/*
		"bool|byte|int|long|float|double|char|def|void|null|" +
				"case|class|else|enum|for|if|import|return|switch|this|while",
		 */
		tokenizer.add("^#\\s+\\-\\*\\-\\s+coding:\\s+(?<coding>.*)\\s+\\-\\*\\-\\s*$", TokenKind.Coding);
		tokenizer.add("\\n|\\r|(?:\\r\\n)", TokenKind.NewLine);
		tokenizer.add("^[\\t ]+", TokenKind.Indent);
		tokenizer.add("\\s", TokenKind.Whitespace);
		tokenizer.add(
				"def|" +
				"int|void|" +
				"return",
				TokenKind.Keyword);

		tokenizer.add("[0-9]*\\.[0-9]+[fF]", TokenKind.Float);
		tokenizer.add("[0-9]*\\.[0-9]+", TokenKind.Double);
		tokenizer.add("[0-9]+L", TokenKind.Long);
		tokenizer.add("[0-9]+", TokenKind.Integer);
		tokenizer.add("\"(?<data>(?:[^\"\\\\]|\\\\.)*)\"", TokenKind.String);
		tokenizer.add("\\pL[\\pL0-9_]*", TokenKind.Identifier);

		tokenizer.add(" ", TokenKind.Space);
		tokenizer.add("\\!", TokenKind.ExclamationMark);
		tokenizer.add("\"", TokenKind.QuotationMark);
		tokenizer.add("#", TokenKind.NumberSign);
		tokenizer.add("\\$", TokenKind.DollarSign);
		tokenizer.add("%", TokenKind.PercentSign);
		tokenizer.add("&", TokenKind.Ampersand);
		tokenizer.add("'", TokenKind.Apostrophe);
		tokenizer.add("\\(", TokenKind.OpenParenthesis);
		tokenizer.add("\\)", TokenKind.CloseParenthesis);
		tokenizer.add("\\*", TokenKind.Star);
		tokenizer.add("\\+", TokenKind.Plus);
		tokenizer.add(",", TokenKind.Comma);
		tokenizer.add("-", TokenKind.Minus);
		tokenizer.add("\\.", TokenKind.FullStop);
		tokenizer.add("\\/", TokenKind.Slash);

		tokenizer.add(":", TokenKind.Colon);
		tokenizer.add(";", TokenKind.Semicolon);
		tokenizer.add("<", TokenKind.LessThanSign);
		tokenizer.add("=", TokenKind.AssignSign);
		tokenizer.add(">", TokenKind.GreaterThanSign);
		tokenizer.add("\\?", TokenKind.QuestionMark);
		tokenizer.add("@", TokenKind.AtSign);

		tokenizer.add("\\[", TokenKind.OpenBracket);
		tokenizer.add("\\\\", TokenKind.Backslash);
		tokenizer.add("\\]", TokenKind.CloseBracket);
		tokenizer.add("\\^", TokenKind.Caret);
		tokenizer.add("_", TokenKind.Underscore);

		tokenizer.add("\\{", TokenKind.OpenBrace);
		tokenizer.add("\\|", TokenKind.VerticalBar);
		tokenizer.add("\\}", TokenKind.CloseBrace);
		tokenizer.add("~", TokenKind.Tilde);

		tokenizer.add("\\.", TokenKind.Other);
		tokenizer.add("", TokenKind.Dedent);
		return this;
	}

	private void configParser() {

	}

	public void start(String source) {
		this.source = source;
		start();
	}

	public void start(InputStream stream) {
		Scanner s = new Scanner(stream).useDelimiter("\\A");
		start(s.hasNext() ? s.next() : "");
	}

	public void start(File file) throws IOException {
		byte[] bytes = Files.readAllBytes(file.toPath());
		source = new String(bytes, StandardCharsets.UTF_8);
		start();
	}

	public void start() {
		try {
			List<Token> tokens = tokenizer.process(source).getTokens();

			for (Token token : tokens) {
				String text;
				switch (token.kind) {
					case NewLine:
						text = "\\n";
						break;
					case Whitespace:
						text = token.sequence.replace(" ", "_").replace("\t", "\\t");
					case Indent:
						text = "\\t";
						break;
					case Space:
						text = "_";
						break;
					case String:
						text = '"' + token.sequence + '"';
						break;
					default:
						text = token.sequence;
				}
				System.out.println(token.kind + " " + text);
			}
		} catch (ParserException e) {
			System.out.println(e.getMessage());
		}


	}

	public static void main(String[] args) throws IOException {
		new App().config().start(new File("examples/01.m"));
	}
}

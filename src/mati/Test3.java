package mati;

import mati.expression.ExprNode;
import mati.expression.ExpressionParser;
import mati.language.Class;
import mati.language.Function;
import mati.language.Method;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test3 {

	final int FIRST_LINE = 0;
	final int SECOND_LINE = 1;
	final int THIRD_LINE = 2;

	enum State {Module, Class, Method, Function }

	int tabSize = 8;
	String tab = updateTab();
	String source;
	String output = "";
	String[] lines;

	Map<String, Consumer<String>> rules;

	private String indentRegex = "^(?<indent>[\\t ]*)";
	private Pattern indentPattern = Pattern.compile(indentRegex, Pattern.MULTILINE);

	private String codingRegex = "^# \\-\\*\\- coding: (?<coding>[a-zA-Z0-9-]+) \\-\\*\\-\\s*";
	private Pattern codingPattern = Pattern.compile(codingRegex);

	private String funcDefRegex = "^def\\s+(?<name>\\w+)\\((?<args>(?:\\w+\\s+\\w+)(?:,\\s+\\w+\\s+\\w+)*)?\\)\\s*:\\s*(?<returnType>\\w+)$";
	private Pattern funcDefPattern = Pattern.compile(funcDefRegex, Pattern.MULTILINE);

	private String importRegex = "^import\\s+(?<import>.*)$";
	private Pattern importPattern = Pattern.compile(importRegex, Pattern.MULTILINE);

	private String classDefRegex = "def\\s+(?<name>\\w+)(?:\\s*:\\s*(?<superclass>\\w+))?";
	private Pattern classDefPattern = Pattern.compile(classDefRegex, Pattern.MULTILINE);

	private String updateTab() {
		tab = new String(new char[tabSize]).replace('\0', ' ');
		return tab;
	}

	public Test3 config(String fileName) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(fileName));
		source = new String(encoded, StandardCharsets.UTF_8);
		return this;
	}

	public void start() throws IOException {
		lines = source.split("\\n", -1);

		Deque<Integer> indents = new ArrayDeque<>();
		indents.push(0);
		Deque<State> state = new ArrayDeque<>();
		state.push(State.Module);

		Function function = null;
		Class clazz = null;
		Method method;

		int index = -1;
		while (index < lines.length - 1) {
			index++;
			String line = lines[index];

			// join logical lines
			{
				while (line.matches("\\\\\\s*$")) {
					index += 1;
					if (index == lines.length) {
						break;
					}
					line = line + lines[index];
				}
			}

			// check empty line
			{
				if (line.trim().isEmpty()) {
					continue;
				}
			}

			//check # directives
			{
				if (index == FIRST_LINE || index == SECOND_LINE) {
					Matcher matcher = codingPattern.matcher(line);
					if (matcher.find()) {
						String coding = matcher.group("coding");
						//TODO do sth with coding
					}
				}
				if (index == FIRST_LINE) {
					if (line.startsWith("#")) {
						break;
						//TODO do sth with comment
					}
				}
			}

			// check indent
			{
				Matcher matcher = indentPattern.matcher(line);
				String indent = "";
				if (matcher.find()) {
					indent = matcher.group("indent");
				}
				line = matcher.replaceFirst("");
				if (indent == null) {
					//TODO: no indent
					indent = "";
				}
				indent = indent.replace("\t", tab);

				int prevIndentSize = indents.peek();
				int indentSize = indent.length();

				if (indentSize > prevIndentSize) {
					indents.push(indentSize);
					output += "{\n";
				} else if (indentSize < prevIndentSize) {
					while (indents.poll() > indentSize) {
						State last = state.poll();
						if (last == State.Class) {
							for (Function aMethod : clazz.methods) {
								output += aMethod.toCAsMethod(clazz.name);
							}
						}
						output += "\n}";
					}
				}
			}
			// line contains no indent

			// check import
			{
				Matcher matcher = importPattern.matcher(line);
				String importTarget;
				if (matcher.find()) {
					if (state.peek() != State.Module) {
						System.err.println("ERROR: import only in module allowed");
					}
					importTarget = matcher.group("import");
					if (importTarget.charAt(0) == '"' && importTarget.charAt(importTarget.length() - 1) == '"') {
						//TODO
					}
					output += "#include <" + importTarget + ">\n";
					continue;
				}
			}

			// check function
			{
				Matcher matcher = funcDefPattern.matcher(line);
				if (matcher.find()) {
					if (state.peek() == State.Module) {
						state.push(State.Function);
					} else if(state.peek() == State.Class) {
						state.push(State.Method);
					} else {
						System.err.println("ERROR unfction only in module or class allowed");
						break;
					}
					function = new Function();
					function.name = matcher.group("name");
					function.setReturnType(matcher.group("returnType"));
					function.parseArgs(matcher.group("args"));
					if (state.peek() == State.Module) {
						output += function.getReturnType() + ' ' + function.name + '(' + function.getCArgs() + ')';
					} else if (state.peek() == State.Class) {
						if (clazz == null) {
							System.err.println("ERROR: indented method outside of a class def");
						} else {
							clazz.methods.add(function);
						}
					}
					continue;
				}
			}

			// check class
			{
				Matcher matcher = classDefPattern.matcher(line);
				String name, superclass;
				if (matcher.find()) {
					if (state.peek() != State.Module) {
						System.err.println("ERROR: Class only in module allowed");
						break;
					}
					state.push(State.Class);
					clazz = new Class();
					clazz.name = matcher.group("name");
					clazz.superClass = matcher.group("superclass");

					//TODO
					continue;
				}
			}

			// check expression
			{
				boolean ret = false;
				if (line.startsWith("return")) {
					line = line.substring(6);
					ret = true;
				}
				ExprNode tree = new ExpressionParser().expression(line).parse();
				switch (state.peek()) {
					case Function:
						function.block.add(tree);
						break;
				}

			}

			output += times("\t", indents.peekLast()) + line;
		}
		if (indents.size() != 0 || indents.peek() != 0) {
			System.err.println("ERROR:\tIndentation error.");
		}
		if (state.size() != 0 || state.peek() != State.Module) {
			System.err.println("ERROR:\tState error.");
		}

		System.out.println("--- OUTPUT BEGIN ---\n" + output + "\n--- OUTPUT END ---");
	}

	public static void main(String[] args) throws IOException {
		new Test3().config("examples/02.m").start();
	}

	static String times(String text, int n) {
		return new String(new char[n]).replace("\0", text);
	}
}

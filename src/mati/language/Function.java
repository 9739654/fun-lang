package mati.language;

import mati.expression.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
	static String argsRegex = "^(?:(\\w+\\s+\\w+)(?:\\s*,\\s*(\\w+\\s+\\w+))*)?$";
	static Pattern argsPattern = Pattern.compile(argsRegex);

	public String name;
	List<Argument> arguments = new ArrayList<>();
	private String returnType;
	public final List<String> lines = new ArrayList<>();

	public final Block block = new Block();

	public void setReturnType(String returnType) {
		this.returnType = returnType == null || returnType.isEmpty() ? "void" : returnType;
	}

	public void parseArgs(String args) {
		Matcher matcher = argsPattern.matcher(args);
		if (matcher.find()) {
			int index = 1;
			while (index < matcher.groupCount()) {
				Argument argument = new Argument();
				String[] arg = matcher.group(index).split("\\s+");
				argument.name = arg[0];
				argument.type = arg[1];
				arguments.add(argument);
				index += 1;
			}
		}
	}

	public String getCArgs() {
		String out = "";
		for (Argument argument : arguments) {
			out += argument.type + ' ' + argument.name + ", ";
		}
		out = out.substring(0, out.length() - 1);
		return out;
	}

	public String toCAsMethod(String className) {
		return toC(className);
	}

	public String toC() {
		return toC("");
	}

	private String toC(String className) {
		String out = returnType + " " + className + "___" + name + '(';
		for (Argument argument : arguments) {
			out += argument.type + ' ' + argument.name + ',';
		}
		out = out.substring(0, out.length() - 1);
		out += "){\n";
		for (String line : lines) {
			out += line + '\n';
		}
		out += "}\n";
		return out;
	}

	public String getReturnType() {
		return returnType;
	}
}

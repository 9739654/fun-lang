package mati;

import mati.language.Function;
import mati.language.Module;
import mati.language.Path;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
func decl:
(?<func>(?<func_header>(?<indent>(?<indent_char>\t)*)def (?<name>\w+) ?\((?<args>((args)))?\)\n)(?<func_body>(?<lines>(?<line>\k<indent>\t(?<expression>.*)\n)*))\n)

class decl:
(?<indent>(?<indent_char>[\t ])*)def (?<name>\w+)\n(?:(?<body_indent>\k<indent>(?<body_indent_char>[\t ])*).*\n)*\k<indent>\n

 */

public class Test {

	private String commentRegex = "#.*$";
	private String idRegex = "\\p{L}+\\w*";
	private String intRegex = "\\d+";
	private String longRegex = "\\d+L";
	private String floatRegex = "\\d*\\.\\d+[fF]";
	private String doubleRegex = "\\d*\\.\\d+[dD]?";
	private String binaryRegex = "0b[01]+";
	private String octalRegex = "0o[01234567]+";
	private String hexRegex = "0x[0123456789ABCDEF]+|[0123456789abcdef]+";
	private String stringRegex = "(?<text>\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\")";
	private String argsRegex = "^(?<args>(?:(((id)))\\s(((id))))(?:\\s?,\\s?(((id)))\\s(((id))))*)*\\s?$";
	private String indentRegex = "";
	private String functionRegex =
			"(?<func>" +
				"(?<funcheader>(?<indent>(?<indentchar>\\t)*)def (?<name>\\w+) ?\\((?:((args)))?\\)\\n)" +
				"(?<funcbody>(?<lines>" +
					"(?<line>\\k<indent>\\t(?<expression>.*)\\n)*" +
				"))\\n" +
			")";
	private String importRegex = "^import\\s*(?<target>.*).*$";

	private String funcHeaderRegex = "^(?<funcheader>(?<indent>(?<indentchar>\\t)*)def\\s+(?<name>((id))\\s*\\((?:((args)))?\\)))$";

	private Map<String, Pattern> patterns = new TreeMap<>();
	private String source;

	{
		argsRegex = argsRegex.replace("((id))", idRegex);
		functionRegex = functionRegex.replace("((args))", argsRegex);
		funcHeaderRegex = funcHeaderRegex.replace("((args))", argsRegex).replace("((id))", idRegex);

		System.out.println("idRegex: " + idRegex);
		System.out.println("strRegex:" + stringRegex);
		System.out.println("argRegex: " + argsRegex);
		System.out.println("fncRegex: " + functionRegex);
	}

	void start() throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get("examples/01.m"));
		source = new String(encoded, StandardCharsets.UTF_8);


		Pattern.compile(stringRegex);
		Pattern.compile(idRegex);
		Pattern.compile(argsRegex);

		//patterns.put("function", Pattern.compile(functionRegex, Pattern.MULTILINE));
		patterns.put("import", Pattern.compile(importRegex, Pattern.MULTILINE));
		patterns.put("comment", Pattern.compile(commentRegex, Pattern.MULTILINE));
		patterns.put("func-header", Pattern.compile(funcHeaderRegex, Pattern.MULTILINE));
		patterns.put("args", Pattern.compile("^" + argsRegex + "$", Pattern.MULTILINE));

		try (BufferedReader br = new BufferedReader(new FileReader("examples/01.m"))) {
			Path level = Path.ROOT.dive("module");
			String classPath = "";
			Path path = Path.ROOT;
			String line;
			Module module = new Module();
			String indent;
			int index = 0;
			while ((line = br.readLine()) != null) {
				index++;
				for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
					Matcher matcher = entry.getValue().matcher(line);
					while (matcher.find()) {
						System.out.print("L: " + index);
						System.out.print(", " + matcher.start() + "-" + matcher.end() + ": " + entry.getKey());
						switch (entry.getKey()) {
							case "func-header":
								indent = matcher.group("indent").replace("\t", "\\t").replace(" ", "_");
								Function func = new Function();
								func.name = matcher.group("name");



								System.out.print(", indent=" + indent);
								path = path.dive(func.name);
								level = level.dive("func-header");

								parseArgs(func, matcher);
								break;
						}
						System.out.println();
					}
				}
			}
		}
	}



	private void parseArgs(Function func, Matcher matcher) {
		String args = matcher.group("args");
		if (args == null) {
			return;
		}
		matcher = patterns.get("args").matcher(args);
		int groupCount = matcher.groupCount();
	}

	public static void main(String[] args) throws IOException {
		new Test().start();
	}
}

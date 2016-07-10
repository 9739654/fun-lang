package mati.language;

import java.util.List;

public class Module {
	List<Function> functions;
	List<Class> classes;

	public Function getFunction(String name) {
		for (Function function : functions) {
			if (function.name.equals(name)) {
				return function;
			}
		}
		return null;
	}

	public void addFunction(Function function) {
		functions.add(function);
	}
}

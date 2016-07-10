package mati.language;

import java.util.ArrayList;
import java.util.List;

public class Class {
	public String name;
	public String superClass;

	public final List<Function> methods = new ArrayList<>();
	public final List<Field> fields = new ArrayList<>();
}

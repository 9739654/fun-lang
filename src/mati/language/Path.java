package mati.language;

import java.util.ArrayList;

public class Path {
	private final String name;
	private final Path parent;
	private final ArrayList<Path> children;

	public static final Path ROOT = new Path(null);

	private Path(String name) {
		this(name, null);
	}

	public Path(String name, Path parent) {
		this.name = name;
		this.parent = parent;
		this.children = new ArrayList<>();
	}

	public Path dive(String name) {
		Path child = new Path(name, this);
		children.add(child);
		return child;
	}

	public Path up() {
		if (parent == null) {
			return this;
		}
		return parent;
	}

	@Override
	public String toString() {
		if (parent == null) {
			return "/";
		}
		return parent.toString() + name + "/";
	}
}

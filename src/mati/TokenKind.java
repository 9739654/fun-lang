package mati;

public enum TokenKind {
	/** ( */ OpenParenthesis, /** ) */ CloseParenthesis, Caret,
	Integer {

		@Override
		public boolean isLiteral() {
			return true;
		}
	},
	Function, Indent, Keyword, /** [ */ OpenBracket, /** ] */ CloseBracket, /** { */ OpenBrace, /** } */CloseBrace, Colon,
	Float {

		@Override
		public boolean isLiteral() {
			return true;
		}
	},
	Double {

		@Override
		public boolean isLiteral() {
			return true;
		}
	},
	Long {

		@Override
		public boolean isLiteral() {
			return true;
		}
	},
	Comment, NewLine, Whitespace, Space, Backslash,
	Slash {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 21;
		}
	},
	NumberSign,
	Minus {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 20;
		}
	},
	Plus {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 20;
		}
	},
	Star {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 21;
		}
	},
	PercentSign {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 21;
		}
	},
	Underscore, Semicolon,
	ExclamationMark {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 22;
		}

		@Override
		public boolean isUnary() {
			return true;
		}
	},
	QuotationMark, DollarSign,
	Ampersand {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 16;
		}
	},
	Apostrophe, Comma, FullStop,
	LessThanSign {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 18;
		}
	},
	AssignSign {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 10;
		}
	},
	GreaterThanSign {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 18;
		}
	},
	QuestionMark, AtSign, VerticalBar,
	Tilde {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 22;
		}
	},
	String {

		@Override
		public boolean isLiteral() {
			return true;
		}
	},
	Other, Symbol, Dedent, Coding,
	Or {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 12;
		}
	},
	And {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 13;
		}
	},
	EqualsSign {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 17;
		}
	},
	InequalSign {
		@Override
		public boolean isOperator() {
			return true;
		}

		@Override
		public int precedence() {
			return 17;
		}
	},
	Identifier {
		@Override
		public boolean isLiteral() {
			return true;
		}
	}, In, Is;

	public boolean isLiteral() {
		return false;
	}

	public boolean isOperator() {
		return false;
	}

	public int precedence() {
		return java.lang.Integer.MAX_VALUE;
	}

	public boolean isUnary() {
		return false;
	}
}

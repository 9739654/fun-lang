package mati;

import java.util.regex.Pattern;

public class TokenInfo {
	public final Pattern regex;
	public final TokenKind token;

	public TokenInfo(Pattern regex, TokenKind token) {
		this.regex = regex;
		this.token = token;
	}
}

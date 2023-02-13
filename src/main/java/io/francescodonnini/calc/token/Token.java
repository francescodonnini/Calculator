package io.francescodonnini.calc.token;

public class Token {
    private final TokenType tokenType;
    private final String text;

    public Token(TokenType tokenType) {
        this(tokenType, "");
    }

    public Token(TokenType tokenType, String text) {
        this.tokenType = tokenType;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public TokenType getTokenType() {
        return tokenType;
    }
}

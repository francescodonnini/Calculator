package io.francescodonnini.calc.scanner;

import io.francescodonnini.calc.exceptions.InvalidSymbol;
import io.francescodonnini.calc.token.Token;
import io.francescodonnini.calc.token.TokenType;

import java.util.Optional;

public class Scanner {
    private final String source;
    private int readPos;

    public Scanner(String source) {
        this.source = source;
    }

    public Optional<Token> nextToken() throws InvalidSymbol {
        Optional<Character> optional = getCurrentChar();
        while (optional.isPresent() && isSpace(optional.get())) {
            optional = nextChar();
        }
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        if (Character.isDigit(optional.get())) {
            StringBuilder s = new StringBuilder().append(optional.get());
            return parseNumber(s);
        } else if (Character.isAlphabetic(optional.get())) {
            StringBuilder s = new StringBuilder();
            s.append(optional.get());
            optional = nextChar();
            while (optional.isPresent() && (Character.isAlphabetic(optional.get()) || Character.isDigit(optional.get()))) {
                s.append(optional.get());
                optional = nextChar();
            }
            return Optional.of(makeIdent(s.toString()));
        } else {
            switch (optional.get()) {
                case '+' -> {
                    nextChar();
                    return Optional.of(new Token(TokenType.ADD));
                }
                case '-' -> {
                    nextChar();
                    return Optional.of(new Token(TokenType.SUB));
                }
                case '*' -> {
                    nextChar();
                    return Optional.of(new Token(TokenType.MUL));
                }
                case '/' -> {
                    nextChar();
                    return Optional.of(new Token(TokenType.DIV));
                }
                case '(' -> {
                    nextChar();
                    return Optional.of(new Token(TokenType.LEFT_PAREN));
                }
                case ')' -> {
                    nextChar();
                    return Optional.of(new Token(TokenType.RIGHT_PAREN));
                }
                default -> throw new InvalidSymbol(optional.get());
            }
        }
    }

    private Optional<Token> parseNumber(StringBuilder s) {
        Optional<Character> optional = nextChar();
        while (optional.isPresent() && Character.isDigit(optional.get())) {
            s.append(optional.get());
            optional = nextChar();
        }
        if (optional.isPresent() && optional.get() == '.') {
            s.append('.');
            optional = nextChar();
            while (optional.isPresent() && Character.isDigit(optional.get())) {
                s.append(optional.get());
                optional = nextChar();
            }
        }
        return Optional.of(makeNumber(s.toString()));
    }

    private boolean isSpace(Character c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    private Token makeIdent(String ident) {
        return new Token(TokenType.IDENT, ident);
    }

    private Token makeNumber(String number) {
        return new Token(TokenType.NUM, number);
    }

    private Optional<Character> nextChar() {
        if (readPos < source.length()) {
            readPos++;
        }
        return getCurrentChar();
    }

    private Optional<Character> getCurrentChar() {
        if (readPos < source.length()) {
            return Optional.of(source.charAt(readPos));
        }
        return Optional.empty();
    }
}

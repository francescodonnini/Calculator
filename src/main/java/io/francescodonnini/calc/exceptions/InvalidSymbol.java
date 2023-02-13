package io.francescodonnini.calc.exceptions;

public class InvalidSymbol extends Exception {
    private final char invalidChar;

    public InvalidSymbol(char invalidChar) {
        this.invalidChar = invalidChar;
    }

    public char getInvalidChar() {
        return invalidChar;
    }
}

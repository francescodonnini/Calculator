package io.francescodonnini.calc.exceptions;

public class InvalidFunName extends Exception {
    private final String funName;
    public InvalidFunName(String text) {
        funName = text;
    }

    public String getFunName() {
        return funName;
    }
}

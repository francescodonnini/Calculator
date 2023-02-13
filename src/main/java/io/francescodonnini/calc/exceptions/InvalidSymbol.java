package io.francescodonnini.calc.exceptions;

public class InvalidSymbol extends Exception {
    private final String symbol;

    public InvalidSymbol(char symbol) {
        this.symbol = String.valueOf(symbol);
    }

    public InvalidSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

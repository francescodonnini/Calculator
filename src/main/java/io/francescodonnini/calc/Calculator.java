package io.francescodonnini.calc;

import io.francescodonnini.calc.exceptions.InvalidInput;
import io.francescodonnini.calc.exceptions.MismatchedParenthesis;
import io.francescodonnini.calc.exceptions.ZeroDivisionError;
import io.francescodonnini.calc.exceptions.InvalidSymbol;
import io.francescodonnini.calc.scanner.Scanner;
import io.francescodonnini.calc.token.*;

import java.util.*;
import java.util.function.Function;

public class Calculator {
    private static final Map<TokenType, Integer> PRECEDENCE_MAP = Map.of(
            TokenType.ADD, 2,
            TokenType.SUB, 2,
            TokenType.MUL, 3,
            TokenType.DIV, 3
    );
    private static final Map<String, Function<Double, Double>> FUNCTIONS = Map.of(
            "cos", Math::cos,
            "sin", Math::sin,
            "exp", Math::exp,
            "log", Math::log,
            "log10", Math::log10
    );
    private static final Map<String, Double> CONSTANTS = Map.of(
            "e", Math.E,
            "pi", Math.PI
    );
    private final Scanner scanner;
    private final Deque<Token> output;
    private final Deque<Token> operatorStack;
    private final Deque<Double> stack;
    
    public Calculator(String source) {
        this.scanner = new Scanner(source);
        output = new ArrayDeque<>();
        operatorStack = new ArrayDeque<>();
        stack = new ArrayDeque<>();
    }

    public double getResult() throws InvalidSymbol, MismatchedParenthesis, InvalidInput, ZeroDivisionError {
        Optional<Token> optional = scanner.nextToken();
        while (optional.isPresent()) {
            Token token = optional.get();
            if (match(token, TokenType.NUM)) {
                output.add(token);
            } else if (match(token, TokenType.IDENT)) {
                if (CONSTANTS.containsKey(token.getText())) {
                    output.add(token);
                } else {
                    operatorStack.add(token);
                }
            } else if (match(token, TokenType.LEFT_PAREN)) {
                operatorStack.add(token);
            } else if (match(token, TokenType.RIGHT_PAREN)) {
                handleRightParen();
            } else {
                handleOperator(optional.get());
            }
            optional = scanner.nextToken();
        }
        while (!operatorStack.isEmpty()) {
            Token token = operatorStack.pollLast();
            if (match(token, TokenType.LEFT_PAREN)) {
                throw new MismatchedParenthesis();
            }
            output.add(token);
        }
        try {
            return evaluate(output);
        } catch (NullPointerException ignored) {
            throw new InvalidInput();
        }
    }

    private void handleOperator(Token operator) {
        while (!operatorStack.isEmpty()
                && !match(operatorStack.getLast(), TokenType.LEFT_PAREN)
                && (hasGreaterPrecedenceThan(operatorStack.getLast(), operator) || (hasSamePrecedence(operatorStack.getLast(), operator) && isLeftAssociative(operator)))) {
            output.add(operatorStack.pollLast());
        }
        operatorStack.add(operator);
    }

    private void handleRightParen() throws MismatchedParenthesis {
        while (!operatorStack.isEmpty() && !match(operatorStack.getLast(), TokenType.LEFT_PAREN)) {
            output.add(operatorStack.pollLast());
        }
        if (operatorStack.isEmpty()) {
            throw new MismatchedParenthesis();
        } else {
            operatorStack.pollLast();
            if (!operatorStack.isEmpty() && match(operatorStack.getLast(), TokenType.IDENT)) {
                output.add(operatorStack.pollLast());
            }
        }
    }

    private double evaluate(Deque<Token> outputStack) throws ZeroDivisionError, InvalidInput, InvalidSymbol {
        for (Token token : outputStack) {
            if (match(token, TokenType.NUM)) {
                stack.add(Double.parseDouble(token.getText()));
            } else {
                switch (token.getTokenType()) {
                    case IDENT -> resolveIdentifier(token.getText());
                    case ADD -> stack.add(stack.pollLast() + stack.pollLast());
                    case DIV -> {
                        double right = stack.pollLast();
                        double left = stack.pollLast();
                        if (right == 0) {
                            throw new ZeroDivisionError();
                        }
                        stack.add(left / right);
                    }
                    case MUL -> stack.add(stack.pollLast() * stack.pollLast());
                    case SUB -> {
                        double right = stack.pollLast();
                        double left = stack.pollLast();
                        stack.add(left - right);
                    }
                    default -> throw new InvalidInput();
                }
            }
        }
        return stack.pollLast();
    }

    private void resolveIdentifier(String text) throws InvalidSymbol {
        if (CONSTANTS.containsKey(text)) {
            stack.add(CONSTANTS.get(text));
        } else if (FUNCTIONS.containsKey(text)){
            stack.add(FUNCTIONS.get(text).apply(stack.pollLast()));
        } else {
            throw new InvalidSymbol(text);
        }
    }

    private boolean isLeftAssociative(Token token) {
        return switch (token.getTokenType()) {
            case ADD, SUB, DIV, MUL -> true;
            default -> false;
        };
    }

    private boolean hasSamePrecedence(Token left, Token right) {
        return Objects.equals(PRECEDENCE_MAP.get(left.getTokenType()), PRECEDENCE_MAP.get(right.getTokenType()));
    }

    private boolean hasGreaterPrecedenceThan(Token left, Token right) {
        return PRECEDENCE_MAP.get(left.getTokenType()) >= PRECEDENCE_MAP.get(right.getTokenType());
    }

    private boolean match(Token token, TokenType tokenType) {
        return token.getTokenType().equals(tokenType);
    }
}

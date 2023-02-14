package io.francescodonnini.calc.scanner;

import io.francescodonnini.calc.Calculator;
import io.francescodonnini.calc.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class EvaluationTest {
    private Map<String, Double> testCases;

    @BeforeEach
    void setup() {
        testCases = Map.of(
                "11-4/2+log10(100)", 11.0,
                "12*3+6", 42.0
        );
    }

    @Test
    void testEvaluation() throws MismatchedParenthesis, InvalidSymbol, InvalidInput, ZeroDivisionError {
        for (String e : testCases.keySet()) {
            Calculator calculator = new Calculator(e);
            Assertions.assertEquals(testCases.get(e), calculator.getResult());
        }
    }
}

package io.francescodonnini.calc.scanner;

import io.francescodonnini.calc.exceptions.InvalidSymbol;
import io.francescodonnini.calc.token.Token;
import io.francescodonnini.calc.token.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class ScannerTest {
    private Scanner scanner;

    @BeforeEach
    void setup() {
        scanner = new Scanner("123+45-21");
    }

    @Test
    void testScanner() throws InvalidSymbol {
        Optional<Token> optional = scanner.nextToken();
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(TokenType.NUM, optional.get().getTokenType());
        Assertions.assertEquals("123", optional.get().getText());
        optional = scanner.nextToken();
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(TokenType.ADD, optional.get().getTokenType());
        optional = scanner.nextToken();
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(TokenType.NUM, optional.get().getTokenType());
        Assertions.assertEquals("45", optional.get().getText());
        optional = scanner.nextToken();
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(TokenType.SUB, optional.get().getTokenType());
        optional = scanner.nextToken();
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(TokenType.NUM, optional.get().getTokenType());
        Assertions.assertEquals("21", optional.get().getText());
    }
}

module io.francescodonnini.calc {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.francescodonnini.calc to javafx.fxml;
    exports io.francescodonnini.calc;
    exports io.francescodonnini.calc.exceptions;
    opens io.francescodonnini.calc.exceptions to javafx.fxml;
}
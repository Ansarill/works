package expression.exceptions;

public class DivisionByZeroException extends CalculateException{
    public DivisionByZeroException(String message) {
        super(message);
    }

    public DivisionByZeroException(String message, Throwable cause) {
        super(message, cause);
    }
    public DivisionByZeroException(Number a, Number b) { super("Division by zero: " + a + " / " + b); }
}

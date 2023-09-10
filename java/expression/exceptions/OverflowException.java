package expression.exceptions;

public class OverflowException extends CalculateException{
    public OverflowException(String message) {
        super(message);
    }
    public OverflowException(Number a, Number b, String op) {
        super("Overflow: " + a + " " + op + " " + b);
    }
    public OverflowException(String message, Throwable cause) {
        super(message, cause);
    }
}

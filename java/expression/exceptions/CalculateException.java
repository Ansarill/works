package expression.exceptions;

public class CalculateException extends RuntimeException {

    public CalculateException(String message) {
        super(message);
    }

    public CalculateException(String message, Throwable cause) {
        super(message, cause);
    }
}

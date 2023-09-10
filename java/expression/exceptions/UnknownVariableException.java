package expression.exceptions;

public class UnknownVariableException extends ParseException{
    public UnknownVariableException(String message) {
        super(message);
    }

    public UnknownVariableException(String message, Throwable cause) {
        super(message, cause);
    }
}

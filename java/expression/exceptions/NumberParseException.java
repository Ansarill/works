package expression.exceptions;

public class NumberParseException extends ParseException{
    public NumberParseException(String message) {
        super(message);
    }

    public NumberParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

package expression.exceptions;

public class PrimaryParseException extends ParseException{
    public PrimaryParseException(String message) {
        super(message);
    }

    public PrimaryParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

package expression.exceptions;

public class MatchBracketParseException extends ParseException{
    public MatchBracketParseException(String message) {
        super(message);
    }

    public MatchBracketParseException(String message, Throwable cause) {
        super(message, cause);
    }
}


package expression.exceptions;

import expression.generic.*;
import expression.parser.BaseParser;
import expression.parser.StringSource;

import java.util.Map;

import static expression.exceptions.ExpressionParse.TokenValue.*;

public class ExpressionParse<T> extends BaseParser {
    @FunctionalInterface
    private interface TripleBinOperation<T> {
        GenericTriple<T> apply(GenericTriple<T> a, GenericTriple<T> b, OpList<T> c);
    }
    @FunctionalInterface
    private interface TripleUnaryOperation<T> {
        GenericTriple<T> apply(GenericTriple<T> a, OpList<T> c);
    }
    public enum TokenValue {
        CLEAR, SET,
        PLUS, MINUS,
        MULTIPLY, DIVIDE, MOD,
        UNARYMINUS, COUNT, ABS, SQUARE,
        VARIABLE, NUMBER,
        LB, DEFAULT, NEGATE
    }
    private final OpList<T> op;
    private final Map<TokenValue, TripleBinOperation<T>> binaryFactory;
    private final Map<TokenValue, TripleUnaryOperation<T>> unaryFactory;

    protected ExpressionParse(final String expr, final OpList<T> operationList) {
        super(new StringSource(expr));
        this.op = operationList;
        binaryFactory = Map.of( PLUS, GenericAdd<T>::new,
                                MINUS, GenericSubtract<T>::new,
                                CLEAR, GenericClear<T>::new,
                                SET, GenericSet<T>::new,
                                MULTIPLY, GenericMultiply<T>::new,
                                DIVIDE, GenericDivide<T>::new,
                                MOD, GenericMod<T>::new);
        unaryFactory = Map.of(  ABS, GenericAbs<T>::new,
                                SQUARE, GenericSquare<T>::new,
                                COUNT, GenericSquare<T>::new,
                                NEGATE, GenericNegate<T>::new);
    }
    protected GenericTriple<T> parse() throws ParseException {
        var temp = expr();
        if (!eof()) {
            throw new ParseException("Expected the end of the expression, but found '" + take() + "'");
        }
        return temp;
    }
    private GenericTriple<T> expr() throws ParseException {
        skipWhitespace();
        GenericTriple<T> left = low();
        while (true) {
            var token = getExpr();
            switch (token) {
                case CLEAR, SET -> {
                    if (!testDigit() && !testVariable()) {
                        left = binaryFactory.get(token).apply(left, low(), op);
                    } else {
                        throw new ParseException("Expected a whitespace, but found: '" + take() + "'");
                    }
                }
                default -> {
                    return left;
                }
            }
        }
    }
    private TokenValue getExpr() {
        skipWhitespace();
        if (take('c')) {
            expect("lear");
            return CLEAR;
        }
        if (take('s')) {
            expect("et");
            return SET;
        }
        return DEFAULT;
    }
    private GenericTriple<T> low() throws ParseException {
        skipWhitespace();
        GenericTriple<T> left = term();
        while (true) {
            var token = getLow();
            if (token == DEFAULT) {
                return left;
            }
            left = binaryFactory.get(token).apply(left, term(), op);
        }
    }
    private TokenValue getLow() {
        skipWhitespace();
        if (take('+')) {
            return PLUS;
        }
        if (take('-')) {
            return MINUS;
        }
        return DEFAULT;
    }
    private GenericTriple<T> term() throws ParseException {
        skipWhitespace();
        GenericTriple<T> left = unary();
        while (true) {
            var token = getTerm();
            if (token == DEFAULT) {
                return left;
            }
            left = binaryFactory.get(token).apply(left, unary(), op);
        }
    }

    private TokenValue getTerm() {
        skipWhitespace();
        if (take('*')) {
            return MULTIPLY;
        }
        if (take('/')) {
            return DIVIDE;
        }
        if (take('m')) {
            expect("od");
            return MOD;
        }
        return DEFAULT;
    }
    private GenericTriple<T> unary() throws ParseException {
        skipWhitespace();
        var token = getUnary();
        return switch (token) {
            case UNARYMINUS -> testDigit() && !testVariable()? prim(true): unaryFactory.get(NEGATE).apply(unary(), op);
            case COUNT, ABS, SQUARE -> {
                if (testVariable() || testDigit()) {
                    throw new ParseException("Expected a whitespace before " + token + ", but found: '" + take() + "'");
                }
                yield unaryFactory.get(token).apply(unary(), op);
            }
            default -> prim(false);
        };
    }
    private TokenValue getUnary() {
        skipWhitespace();
        if (take('c')) {
            expect("ount");
            return COUNT;
        }
        if (take('-')) {
            return UNARYMINUS;
        }
        if (take('a')) {
            expect("bs");
            return ABS;
        }
        if (take('s')) {
            expect("quare");
            return SQUARE;
        }
        return DEFAULT;
    }

    private GenericTriple<T> prim(boolean negnumber) throws ParseException {
        skipWhitespace();
        return switch (getPrim(negnumber)) {
            case LB -> {
                var temp = expr();
                if (!take(')')) {
                    throw new MatchBracketParseException("Expected ')', but found '" + take() + "'");
                }
                yield temp;
            }
            case VARIABLE -> new GenericVariable<T>(take() + "");
            case NUMBER -> {
                String digits = (negnumber? "-": "") + parsePosNumber();
                try{
                    op.cast().apply(digits);
                    yield new GenericConst<>(digits, op);
                } catch (final NumberFormatException e) {
                    throw new NumberParseException("Number format isn't supported: '" + digits + "'", e);
                }
            }
            default -> throw new PrimaryParseException("Couldn't resolve primary expression, found: '" + take() + "'");
        };
    }
    private TokenValue getPrim(boolean negnumber) throws ParseException {
        skipWhitespace();
        if (negnumber || testDigit()) {
            return NUMBER;
        }
        if (take('(')) {
            return LB;
        }
        if (testVariable()) {
            return VARIABLE;
        }
        if (testLetter()) {
            throw new UnknownVariableException("Unknown variable name: '" + take() + "'");
        }
        return DEFAULT;
    }



    private boolean testDigit() {
        return test(Character::isDigit);
    }

    private boolean testVariable() {
        return test(c -> c == 'x' || c == 'y' || c == 'z');
    }

    private boolean testLetter() {
        return test(Character::isLetter);
    }
    private String parsePosNumber() {
        StringBuilder digits = new StringBuilder();
        while (testDigit()) {
            digits.append(take());
        }
        if (test('.')) {
            digits.append(take());
        }
        while(testDigit()) {
            digits.append(take());
        }
        return digits.toString();
    }
}

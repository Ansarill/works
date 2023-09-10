package expression.generic;

import java.util.function.Function;

public class Lmode implements OpList<Long> {
    private static final GenericBinOp<Long> addOp = new GenericBinOp<>(Long::sum, "+");
    private static final GenericBinOp<Long> subOp = new GenericBinOp<>((a, b) -> a - b, "-");
    private static final GenericBinOp<Long> mulOp = new GenericBinOp<>((a, b) -> a * b, "*");
    private static final GenericBinOp<Long> divOp = new GenericBinOp<>((a, b) -> a / b, "/");
    private static final GenericBinOp<Long> modOp = new GenericBinOp<>((a, b) -> a % b, "%");
    private static final GenericUnaryOp<Long> negateOp = new GenericUnaryOp<>(a -> -a, "negate");
    private static final GenericUnaryOp<Long> absOp = new GenericUnaryOp<>(Math::abs, "abs");
    private static final GenericUnaryOp<Long> squareOp = new GenericUnaryOp<>(a -> a * a, "square");
    Function<String, Long> castOp = Long::parseLong;
    @Override
    public GenericBinOp<Long> add() { return addOp; }

    @Override
    public GenericBinOp<Long> sub() {
        return subOp;
    }

    @Override
    public GenericBinOp<Long> mul() {
        return mulOp;
    }

    @Override
    public GenericBinOp<Long> div() {
        return divOp;
    }

    @Override
    public GenericBinOp<Long> mod() {
        return modOp;
    }


    @Override
    public GenericUnaryOp<Long> negate() {
        return negateOp;
    }

    @Override
    public GenericUnaryOp<Long> square() {
        return squareOp;
    }

    @Override
    public GenericUnaryOp<Long> abs() {
        return absOp;
    }

    @Override
    public Function<String, Long> cast() {
        return castOp;
    }
    @Override
    public String implName() {
        return "l mode";
    }
    @Override
    public String toString() {
        return "operation list implementation for <long> calc without checking";
    }
}

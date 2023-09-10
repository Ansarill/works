package expression.generic;

import java.util.function.Function;

public class Dmode implements OpList<Double> {
    private static final GenericBinOp<Double> addOp = new GenericBinOp<>(Double::sum, "+");
    private static final GenericBinOp<Double> subOp = new GenericBinOp<>((a, b) -> a - b, "-");
    private static final GenericBinOp<Double> mulOp = new GenericBinOp<>((a, b) -> a * b, "*");
    private static final GenericBinOp<Double> divOp = new GenericBinOp<>((a, b) -> a / b, "/");
    private static final GenericBinOp<Double> modOp = new GenericBinOp<>((a, b) -> a % b, "%");
    private static final GenericUnaryOp<Double> negateOp = new GenericUnaryOp<>(a -> -a, "negate");
    private static final GenericUnaryOp<Double> absOp = new GenericUnaryOp<>(Math::abs, "abs");
    private static final GenericUnaryOp<Double> squareOp = new GenericUnaryOp<>(a -> a * a, "square");
    Function<String, Double> castOp = Double::parseDouble;

    @Override
    public GenericBinOp<Double> add() { return addOp; }

    @Override
    public GenericBinOp<Double> sub() {
        return subOp;
    }

    @Override
    public GenericBinOp<Double> mul() {
        return mulOp;
    }

    @Override
    public GenericBinOp<Double> div() {
        return divOp;
    }

    @Override
    public GenericBinOp<Double> mod() {
        return modOp;
    }


    @Override
    public GenericUnaryOp<Double> negate() {
        return negateOp;
    }

    @Override
    public GenericUnaryOp<Double> square() {
        return squareOp;
    }

    @Override
    public GenericUnaryOp<Double> abs() {
        return absOp;
    }

    @Override
    public Function<String, Double> cast() {
        return castOp;
    }

    @Override
    public String toString() {
        return "operation list implementation for <double> calc without checking";
    }
    @Override
    public String implName() {
        return "d mode";
    }
}

package expression.generic;

import java.util.function.Function;

public class Smode implements OpList<Short> {
    private static final GenericBinOp<Short> addOp = new GenericBinOp<>((a, b) -> (short) (a + b), "+");
    private static final GenericBinOp<Short> subOp = new GenericBinOp<>((a, b) -> (short)(a - b), "-");
    private static final GenericBinOp<Short> mulOp = new GenericBinOp<>((a, b) -> (short)(a * b), "*");
    private static final GenericBinOp<Short> divOp = new GenericBinOp<>((a, b) -> (short)(a / b), "/");
    private static final GenericBinOp<Short> modOp = new GenericBinOp<>((a, b) -> (short)(a % b), "%");
    private static final GenericUnaryOp<Short> negateOp = new GenericUnaryOp<>(a -> (short)(-a), "negate");
    private static final GenericUnaryOp<Short> absOp = new GenericUnaryOp<>(a -> a >= 0? a: (short) (-a), "abs");
    private static final GenericUnaryOp<Short> squareOp = new GenericUnaryOp<>(a -> (short)(a * a), "square");
    Function<String, Short> castOp = a -> (short) Integer.parseInt(a);

    @Override
    public GenericBinOp<Short> add() { return addOp; }

    @Override
    public GenericBinOp<Short> sub() {
        return subOp;
    }

    @Override
    public GenericBinOp<Short> mul() {
        return mulOp;
    }

    @Override
    public GenericBinOp<Short> div() {
        return divOp;
    }

    @Override
    public GenericBinOp<Short> mod() {
        return modOp;
    }


    @Override
    public GenericUnaryOp<Short> negate() {
        return negateOp;
    }

    @Override
    public GenericUnaryOp<Short> square() {
        return squareOp;
    }

    @Override
    public GenericUnaryOp<Short> abs() {
        return absOp;
    }
    @Override
    public Function<String, Short> cast() {
        return castOp;
    }
    
    @Override
    public String implName() {
        return "s mode";
    }

    @Override
    public String toString() {
        return "operation list implementation for <short> calc without checking";
    }
}


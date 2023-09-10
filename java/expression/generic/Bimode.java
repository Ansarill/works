package expression.generic;

import java.math.BigInteger;
import java.util.function.Function;

public class Bimode implements OpList<BigInteger> {
    private static final GenericBinOp<BigInteger> addOp = new GenericBinOp<>(BigInteger::add, "+");
    private static final GenericBinOp<BigInteger> subOp = new GenericBinOp<>(BigInteger::subtract, "-");
    private static final GenericBinOp<BigInteger> mulOp = new GenericBinOp<>(BigInteger::multiply, "*");
    private static final GenericBinOp<BigInteger> divOp = new GenericBinOp<>(BigInteger::divide, "/");
    private static final GenericBinOp<BigInteger> modOp = new GenericBinOp<>(BigInteger::mod, "%");
    private static final GenericUnaryOp<BigInteger> negateOp = new GenericUnaryOp<>(BigInteger::negate, "negate");
    private static final GenericUnaryOp<BigInteger> absOp = new GenericUnaryOp<>(a -> {
        if (a.compareTo(BigInteger.ZERO) >= 0) {
            return a;
        } else {
            return a.negate();
        }
    }, "abs");
    private static final GenericUnaryOp<BigInteger> squareOp = new GenericUnaryOp<>(a -> a.multiply(a), "square");
    Function<String, BigInteger> castOp = a -> BigInteger.valueOf(Long.parseLong(a));;

    @Override
    public GenericBinOp<BigInteger> add() { return addOp; }

    @Override
    public GenericBinOp<BigInteger> sub() {
        return subOp;
    }

    @Override
    public GenericBinOp<BigInteger> mul() {
        return mulOp;
    }

    @Override
    public GenericBinOp<BigInteger> div() {
        return divOp;
    }

    @Override
    public GenericBinOp<BigInteger> mod() {
        return modOp;
    }


    @Override
    public GenericUnaryOp<BigInteger> negate() {
        return negateOp;
    }

    @Override
    public GenericUnaryOp<BigInteger> square() {
        return squareOp;
    }

    @Override
    public GenericUnaryOp<BigInteger> abs() {
        return absOp;
    }

    @Override
    public Function<String, BigInteger> cast() {
        return castOp;
    }
    @Override
    public String implName() {
        return "bi mode";
    }
    
    @Override
    public String toString() {
        return "operation list implementation for <biginteger> calc without checking";
    }
}

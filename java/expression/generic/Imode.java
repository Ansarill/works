package expression.generic;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

import java.util.function.Function;

public class Imode implements OpList<Integer> {

    private static final GenericBinOp<Integer> addOp = new GenericBinOp<>((a, b) -> {
        if (addCheck(a, b)) {
            return a + b;
        } else {
            throw new OverflowException(a, b, "+");
        }
    }, "+");
    private static final GenericBinOp<Integer> subOp = new GenericBinOp<>((a, b) -> {
        if (subCheck(a, b)) {
            return a - b;
        } else {
            throw new OverflowException(a, b, "-");
        }
    }, "-");
    private static final GenericBinOp<Integer> mulOp = new GenericBinOp<>((a, b) -> {
        if (mulCheck(a, b)) {
            return a * b;
        } else {
            throw new OverflowException(a, b, "*");
        }
    }, "*");
    private static final GenericBinOp<Integer> divOp = new GenericBinOp<>((a, b) -> {
        if (!divDivisionByZeroCheck(a, b)) {
            throw new DivisionByZeroException(a + " / 0 is forbidden");
        } else if (!divOverflowCheck(a, b)) {
            throw new OverflowException(a, b, "/");
        } else {
            return a / b;
        }
    }, "/");
    private static final GenericBinOp<Integer> modOp = new GenericBinOp<>((a, b) -> {
        if (modCheck(a, b)) {
            return a % b;
        } else {
            throw new DivisionByZeroException(a + " % 0 is forbidden");
        }
    }, "%");
    private static final GenericUnaryOp<Integer> negateOp = new GenericUnaryOp<>(a -> {
        if (negateCheck(a)) {
            return -a;
        } else {
            throw new OverflowException("Overflow: - " + a);
        }
    }, "negate");
    private static final GenericUnaryOp<Integer> absOp = new GenericUnaryOp<>(a -> {
        if (a < 0) {
            if (negateCheck(a)) {
                return -a;
            } else {
                throw new OverflowException("Overflow: abs " + a);
            }
        } else {
            return a;
        }
    }, "abs");
    private static final GenericUnaryOp<Integer> squareOp = new GenericUnaryOp<>(a -> {
        if (mulCheck(a, a)) {
            return a * a;
        } else {
            throw new OverflowException("Overflow: square " + a);
        }
    }, "square");
    private static final GenericUnaryOp<Integer> countOp = new GenericUnaryOp<>(Integer::bitCount, "count");
    private static final GenericBinOp<Integer> setOp = new GenericBinOp<>((a, b) -> a | (1 << (b % 32)), "set");
    private static final GenericBinOp<Integer> clearOp = new GenericBinOp<>((a, b) -> a & ~(1 << (b % 32)), "clear");
    Function<String, Integer> castOp = Integer::parseInt;

    @Override
    public GenericBinOp<Integer> add() { return addOp; }

    @Override
    public GenericBinOp<Integer> sub() {
        return subOp;
    }

    @Override
    public GenericBinOp<Integer> mul() {
        return mulOp;
    }

    @Override
    public GenericBinOp<Integer> div() {
        return divOp;
    }

    @Override
    public GenericBinOp<Integer> mod() {
        return modOp;
    }


    @Override
    public GenericUnaryOp<Integer> negate() {
        return negateOp;
    }

    @Override
    public GenericUnaryOp<Integer> square() {
        return squareOp;
    }

    @Override
    public GenericUnaryOp<Integer> abs() {
        return absOp;
    }

    @Override
    public Function<String, Integer> cast() {
        return castOp;
    }
    @Override
    public GenericUnaryOp<Integer> count() {
        return countOp;
    }

    @Override
    public GenericBinOp<Integer> set() {
        return setOp;
    }

    @Override
    public GenericBinOp<Integer> clear() {
        return clearOp;
    }

    protected static boolean mulCheck(int a, int b) {
        if (b == 0) {
            return true;
        }
        int mx = Integer.MAX_VALUE/b;
        int mn = b == -1? Integer.MAX_VALUE: Integer.MIN_VALUE/b;
        int mxx = Math.max(mx, mn);
        int mnn = Math.min(mx, mn);
        return (mnn <= a && mxx >= a);
    }
    protected static boolean negateCheck(int a) {
        return a != Integer.MIN_VALUE;
    }
    protected static boolean addCheck(int a, int b) {
        return !((b > 0) && (Integer.MAX_VALUE - b < a) || (b < 0) && (Integer.MIN_VALUE - b > a));
    }
    protected static boolean subCheck(int a, int b) {
        return !((b < 0) && (Integer.MAX_VALUE + b < a) || (b > 0) && (Integer.MIN_VALUE + b > a));
    }
    protected static boolean modCheck(int a, int b) {
        return b != 0;
    }
    protected static boolean divOverflowCheck(int a, int b) {
        return (a != Integer.MIN_VALUE || b != -1);
    }
    protected static boolean divDivisionByZeroCheck(int a, int b) {
        return b != 0;
    }

    @Override
    public String toString() {
        return "operation list implementation for <int> calc with checking";
    }
    @Override
    public String implName() {
        return "i mode";
    }
}

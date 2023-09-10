package expression.generic;

import java.util.function.BiFunction;

public class GenericBinOp<T> {
    private final String opString;
    private final BiFunction <T, T, T> op;
    public GenericBinOp (BiFunction<T, T, T> op, String opString) {
        this.op = op;
        this.opString = opString;
    }
    public T apply(T a, T b) {
        return op.apply(a, b);
    }

    public String toString() {
        return opString;
    }
}


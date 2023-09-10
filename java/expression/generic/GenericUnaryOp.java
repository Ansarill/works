package expression.generic;

import java.util.function.Function;

public class GenericUnaryOp<T> {
    private final String opString;
    private final Function<T, T> op;
    public GenericUnaryOp (Function<T, T> op, String opString) {
        this.op = op;
        this.opString = opString;
    }
    public T apply(T a) {
        return op.apply(a);
    }

    public String toString() {
        return opString;
    }
}


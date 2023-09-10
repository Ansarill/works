package expression.generic;

public class GenericCount<T> extends GenericUnary<T> {
    public GenericCount(GenericTriple<T> expr, OpList<T> op) {
        super(expr, op.count());
    }
}

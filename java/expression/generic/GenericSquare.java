package expression.generic;

public class GenericSquare<T> extends GenericUnary<T> {
    public GenericSquare(GenericTriple<T> expr, OpList<T> op) {
        super(expr, op.square());
    }
}

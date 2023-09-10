package expression.generic;

public class GenericNegate<T> extends GenericUnary<T>{
    public GenericNegate(GenericTriple<T> expr, OpList<T> op) {
        super(expr, op.negate());
    }
}

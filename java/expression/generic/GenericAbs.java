package expression.generic;


public class GenericAbs<T> extends GenericUnary<T> {
    public GenericAbs(GenericTriple<T> expr, OpList<T> opList) {
        super(expr, opList.abs());
    }
}

package expression.generic;


public class GenericUnary<T> implements GenericTriple<T> {
    protected final GenericTriple<T> expr;
    protected GenericUnaryOp<T> op;
    public GenericUnary(GenericTriple<T> expr, OpList<T> opList) {
        this.expr = expr;
    }
    protected GenericUnary(GenericTriple<T> expr, GenericUnaryOp<T> op) {
        this.expr = expr;
        this.op = op;
    }

    @Override
    public String toString() {
        return op + "(" + expr + ")";
    }
    @Override
    public String toMiniString() {
        if (expr instanceof GenericConst<?> || expr instanceof GenericVariable<?> || expr instanceof GenericUnary<?>) {
            return op + " " + expr.toMiniString();
        } else {
            return op + "(" + expr.toMiniString() + ")";
        }
    }
    @Override
    public int priority() {
        return 5000;
    }

    @Override
    public T eval(T x, T y, T z) {
        return op.apply(expr.eval(x, y, z));
    }
}

package expression;



public abstract class Unary implements TripleExpression {
    protected final TripleExpression expr;
    protected UnaryOperation op;
    public Unary(final TripleExpression x) {
        this.expr = x;
    }
    protected Unary(final TripleExpression x, UnaryOperation op, String stringOperation) {
        this(x);
        this.op = op;
        this.stringOperation = stringOperation;
    }
    protected String stringOperation;

    @Override
    public int evaluate(int x, int y, int z) {
        return op.apply(expr.evaluate(x, y, z));
    }
    @Override
    public String toString() {
        return stringOperation + "(" + expr + ")";
    }
    @Override
    public String toMiniString() {
        if (expr instanceof Const || expr instanceof Variable || expr instanceof Unary) {
            return stringOperation + " " + expr.toMiniString();
        } else {
            return stringOperation + "(" + expr.toMiniString() + ")";
        }
    }

    @Override
    public int priority() {
        return 5000;
    }
}

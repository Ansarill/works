package expression;

public class Multiply extends BinaryOp {
    public Multiply(TripleExpression left, TripleExpression right) {
        super(left, right, (a, b) -> a * b, "*");
    }

    @Override
    protected boolean bracketLeft() {
        return left.priority() < priority();
    }
    @Override
    protected boolean bracketRight() {
        return right.priority() < priority() || right instanceof Divide;
    }

    @Override
    public int priority() {
        return 2000;
    }
}

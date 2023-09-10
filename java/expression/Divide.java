package expression;

public class Divide extends BinaryOp {
    public Divide(TripleExpression left, TripleExpression right) {
        super(left, right, (a, b) -> a / b, "/");
    }

    @Override
    protected boolean bracketLeft() {
        return left.priority() + 3 < priority();
    }

    @Override
    protected boolean bracketRight() {
        return right.priority() <= priority();
    }

    @Override
    public int priority() {
        return 2002;
    }
}

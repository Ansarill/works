package expression;

public class Add extends BinaryOp {
    public Add(TripleExpression left, TripleExpression right) {
        super(left, right, (a, b) -> a + b, "+");
    }

    @Override
    protected boolean bracketLeft() {
        return left.priority() < priority();
    }

    @Override
    protected boolean bracketRight() {
        return right.priority() < priority();
    }

    @Override
    public int priority() {
        return 1600;
    }
}

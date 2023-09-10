package expression;


public class Clear extends BinaryOp {

    public Clear(TripleExpression left, TripleExpression right) {
        super(left, right, (a, b) -> a & ~(1 << (b % 32)), "clear");
    }

    @Override
    protected boolean bracketLeft() {
        return false;
    }

    @Override
    protected boolean bracketRight() {
        return right.priority() <= priority();
    }

    @Override
    public int priority() {
        return 202;
    }
}

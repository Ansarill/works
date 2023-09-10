package expression.generic;

public class GenericSubtract<T> extends GenericBin<T> {
    public GenericSubtract(GenericTriple<T> left, GenericTriple<T> right, OpList<T> op) {
        super(left, right, op.sub());
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
        return 1602;
    }
}

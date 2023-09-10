package expression.generic;

public class GenericDivide<T> extends GenericBin<T> {
    public GenericDivide(GenericTriple<T> left, GenericTriple<T> right, OpList<T> op) {
        super(left, right, op.div());
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

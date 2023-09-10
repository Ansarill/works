package expression.generic;

public class GenericSet<T> extends GenericBin<T> {
    public GenericSet(GenericTriple<T> left, GenericTriple<T> right, OpList<T> op) {
        super(left, right, op.set());
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

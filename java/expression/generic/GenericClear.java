package expression.generic;

public class GenericClear<T> extends GenericBin<T> {
    public GenericClear(GenericTriple<T> left, GenericTriple<T> right, OpList<T> op) {
        super(left, right, op.clear());
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

package expression.generic;

public class GenericAdd<T> extends GenericBin<T> {
    public GenericAdd(GenericTriple<T> left, GenericTriple<T> right, OpList<T> opList) {
        super(left, right, opList.add());
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

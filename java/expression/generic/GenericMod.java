package expression.generic;

public class GenericMod<T> extends GenericBin<T>{
    public GenericMod(GenericTriple<T> left, GenericTriple<T> right, OpList<T> op) {
        super(left, right, op.mod());
    }

    @Override
    protected boolean bracketLeft() {
        return left.priority() < priority();
    }
    @Override
    protected boolean bracketRight() {
        return right.priority() < priority() || right instanceof GenericDivide<?>;
    }

    @Override
    public int priority() {
        return 2000;
    }
}

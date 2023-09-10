package expression.generic;

import java.util.Objects;

public abstract class GenericBin<T> implements GenericTriple<T> {
    protected final GenericTriple<T> left;
    protected final GenericTriple<T> right;
    protected final GenericBinOp<T> op;
    protected GenericBin(GenericTriple<T> left, GenericTriple<T> right, GenericBinOp<T> op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o.getClass() == this.getClass()) {
            var obj = (GenericBin<?>) o;
            return Objects.equals(this.left, obj.left) &&
                    Objects.equals(this.right, obj.right);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return left.hashCode() * 31 + right.hashCode() * 19 + getClass().hashCode();
    }

    protected abstract boolean bracketLeft();

    protected abstract boolean bracketRight();

    @Override
    public String toMiniString() {
        return (bracketLeft() ? "(" + left.toMiniString() + ")" : left.toMiniString()) +
                " " + op + " " +
                (bracketRight() ? "(" + right.toMiniString() + ")" : right.toMiniString());
    }

    @Override
    public T eval(T x, T y, T z) {
        return op.apply(left.eval(x, y, z), right.eval(x, y, z));
    }
}

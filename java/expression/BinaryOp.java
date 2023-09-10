package expression;

import java.util.Objects;

public abstract class BinaryOp implements TripleExpression {
    protected final TripleExpression left;
    protected final TripleExpression right;
    protected BinOperation op;
    protected String opString;
    public BinaryOp(TripleExpression left, TripleExpression right) {
        this.left = left;
        this.right = right;
    }
    protected BinaryOp(TripleExpression left, TripleExpression right, BinOperation op, String opString) {
        this(left, right);
        this.op = op;
        this.opString = opString;
    }
    protected TripleExpression getLeft() {
        return left;
    }

    protected TripleExpression getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + " " + opString + " " + right + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o.getClass() == this.getClass()) {
            var obj = (BinaryOp) o;
            return Objects.equals(this.left, obj.getLeft()) &&
                    Objects.equals(this.right, obj.getRight());
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
                " " + opString + " " +
                (bracketRight() ? "(" + right.toMiniString() + ")" : right.toMiniString());
    }

    @Override
    public int evaluate(int x, int y, int z) {
            return op.apply(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}

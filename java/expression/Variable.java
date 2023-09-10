package expression;

import java.util.Objects;

public class Variable implements TripleExpression {
    private final String val;

    public Variable(final String val) {
        this.val = val;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if ("x".equals(val)) {
            return x;
        }
        if ("y".equals(val)) {
            return y;
        }
        if ("z".equals(val)) {
            return z;
        }
        throw new AssertionError("Unknown variable '" + val + "'");
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o.getClass() == Variable.class) {
            return Objects.equals(((Variable) o).val, val);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return val.hashCode() + this.getClass().hashCode();
    }

    @Override
    public String toMiniString() {
        return val;
    }

    @Override
    public int priority() {
        return 10000;
    }
}

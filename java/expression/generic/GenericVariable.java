package expression.generic;

import java.util.Objects;

public class GenericVariable<T> implements GenericTriple<T> {
    private final String val;

    public GenericVariable(final String val) {
        this.val = val;
    }

    @Override
    public T eval(T x, T y, T z) {
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
        if (o != null && o.getClass() == GenericVariable.class) {
            return Objects.equals(((GenericVariable<?>) o).val, val);
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

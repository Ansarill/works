package expression;

import java.util.Objects;

public class Const implements TripleExpression {
    private final Number c;
    public Const(final int c) {
        this.c = c;
    }


    @Override
    public boolean equals(final Object o) {
        if (o != null && o.getClass() == Const.class) {
            return Objects.equals(((Const) o).c, c);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return String.valueOf(c).hashCode() + this.getClass().hashCode();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public String toString() {
        return c.toString();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return c.intValue();
    }

    @Override
    public int priority() {
        return 10000;
    }
}

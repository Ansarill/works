package expression.generic;

import java.util.Objects;
import java.util.function.Function;

public class GenericConst<T> implements GenericTriple<T> {
    private final String c;
    private final Function<String, T> cast;
    public GenericConst(final String c, final OpList<T> opList) {
        this.c = c;
        this.cast = opList.cast();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof GenericConst<?>) {
            return Objects.equals(((GenericConst<?>) o).c, c);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return c.hashCode() + this.getClass().hashCode();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public String toString() {
        return c;
    }

    @Override
    public T eval(T x, T y, T z) {
        return cast.apply(c);
    }

    @Override
    public int priority() {
        return 10000;
    }
}

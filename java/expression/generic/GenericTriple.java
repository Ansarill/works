package expression.generic;

import expression.ToMiniString;

public interface GenericTriple<T> extends ToMiniString {
    T eval(T x, T y, T z);
}

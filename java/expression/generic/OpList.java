package expression.generic;

import java.util.function.Function;


public interface OpList<T> {
    default AssertionError notImplemented(String op) {
        return new AssertionError(op + " not implemented for " + implName());
    }
    default String implName() {
        return "this unnamed operation list";
    }
    default GenericBinOp<T> add() {
        throw notImplemented("add");
    }
    default GenericBinOp<T> sub() {
        throw notImplemented("sub");
    }
    default GenericBinOp<T> mul() {
        throw notImplemented("mul");
    }
    default GenericBinOp<T> div() {
        throw notImplemented("div");
    }
    default GenericBinOp<T> set() {
        throw notImplemented("set");
    }
    default GenericBinOp<T> clear() {
        throw notImplemented("clear");
    }
    default GenericBinOp<T> mod() {
        throw notImplemented("set");
    }
    default GenericUnaryOp<T> negate() {
        throw notImplemented("negate");
    }
    default GenericUnaryOp<T> count() {
        throw notImplemented("count");
    }
    default GenericUnaryOp<T> square() {
        throw notImplemented("square");
    }
    default GenericUnaryOp<T> abs() {
        throw notImplemented("abs");
    }
    default Function<String, T> cast() {
        throw notImplemented("cast");
    }
}

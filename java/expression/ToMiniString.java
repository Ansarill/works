package expression;

public interface ToMiniString {
    default String toMiniString() {
        return toString();
    }
    default int priority() { return 0; }
}

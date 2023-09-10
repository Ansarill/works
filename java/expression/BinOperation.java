package expression;

@FunctionalInterface
public interface BinOperation {
    int apply(int a, int b);
}

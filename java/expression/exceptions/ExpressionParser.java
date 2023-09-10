package expression.exceptions;

import expression.TripleExpression;
import expression.generic.GenericTriple;
import expression.generic.Imode;
import expression.generic.OpList;

public final class ExpressionParser<T> implements TripleParser {
    @Override
    public TripleExpression parse(final String expr) throws ParseException {
        return castToTripleExpression(new ExpressionParse<>(expr, new Imode()).parse());
    }
    private TripleExpression castToTripleExpression(GenericTriple<Integer> expr) {
        return new TripleExpression() {
            @Override
            public int priority() {
                return expr.priority();
            }
            @Override
            public int evaluate(int x, int y, int z) {
                return expr.eval(x, y, z);
            }
            @Override
            public String toMiniString() {
                return expr.toMiniString();
            }
            @Override
            public String toString() {
                return expr.toString();
            }
            @Override
            public boolean equals(Object o) {
                return expr.equals(o);
            }
            @Override
            public int hashCode() {
                return expr.hashCode();
            }
        };
    }
    public GenericTriple<T> parse(final String expr, final OpList<T> opList) throws ParseException {
        return new ExpressionParse<>(expr, opList).parse();

    }
}

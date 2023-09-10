package expression;


public class Count extends Unary {
    public Count(TripleExpression c) {
        super(c, Integer::bitCount, "count");
    }
}

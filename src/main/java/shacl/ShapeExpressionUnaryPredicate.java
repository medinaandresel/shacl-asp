package shacl;

public class ShapeExpressionUnaryPredicate extends ShapeExpression {

    private Predicate pred;

    static public ShapeExpressionUnaryPredicate constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

        input = input.trim();
        if (!input.matches("[a-zA-Z0-9]+"))
            throw new ParseAttemptFailException("not a valid predicate symbol, should be alphanumeretic");

        ShapeExpressionUnaryPredicate ret = new ShapeExpressionUnaryPredicate();
        ret.pred = Predicate.predicateFromString(input,1);

        return ret;

    }

    @Override
    public String toString() {
        return pred.toString();
    }

    public String getPredicateName()  {
        return pred.getName();
    }

    @Override
    public String toRules(String head_predicate_name) {
        return head_predicate_name+"(Y) :- "+pred.getName()+ "(Y).\n";
    }

	@Override
	public String getName() {
		return pred.getName();
	}
}

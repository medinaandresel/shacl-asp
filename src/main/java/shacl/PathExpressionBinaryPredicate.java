package shacl;


public class PathExpressionBinaryPredicate extends PathExpression {

    private Predicate pred;

    static public PathExpressionBinaryPredicate constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

        input = input.trim();
        if (!input.matches("[a-zA-Z0-9]+"))
            throw new ParseAttemptFailException("not a valid predicate symbol, should be alphanumeretic");

        PathExpressionBinaryPredicate ret = new PathExpressionBinaryPredicate();
        ret.pred = Predicate.predicateFromString(input,2);

        return ret;

    }

    @Override
    public String toString() {
        return pred.toString();
    }

    public String toRules(String head_pred_name)  {
        return head_pred_name+"(X,Y) :- "+pred.getName()+ "(X,Y).\n";
    }

	@Override
	public String getName() {
		return pred.getName();
	}

}

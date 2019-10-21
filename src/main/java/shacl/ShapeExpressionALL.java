package shacl;

public class ShapeExpressionALL extends ShapeExpression {

    private ShapeExpression innerExpr;
    private PathExpression pathExpression;

    static public ShapeExpressionALL constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

        int posGets = input.indexOf("ONLY");

        if (posGets == -1)   {

            throw new ParseAttemptFailException("failed to see the keyword ALL");

        } else {

            PathExpression lhs = PathExpression.constructFromText(input.substring(0, posGets));
            ShapeExpression rhs = ShapeExpression.constructFromText(input.substring(posGets + 4));

            ShapeExpressionALL ret = new ShapeExpressionALL();
            ret.pathExpression = lhs;
            ret.innerExpr = rhs;

            return ret;
        }


    }


    @Override
    public String toString() {
        return "(ALL "+ pathExpression.toString() + " ARE " +  innerExpr.toString() + ")";
    }


    @Override
    public String toRules(String head_predicate_name) {


        String new_bin_predicate_name = Context.getNewBinaryPredicateName();
        String new_loc_name = Context.getNewUnaryPredicateName();
        String new_follow_name = Context.getNewUnaryPredicateName();


        return head_predicate_name+ "(X) :-ADom(X), not " + new_loc_name +"(X).\n " +
                new_loc_name +"(X) :- " + new_bin_predicate_name + "(X,Y), not " + new_follow_name + "(Y).\n" +
                pathExpression.toRules(new_bin_predicate_name) + innerExpr.toRules(new_follow_name);


    }


	@Override
	public String getName() {
		return "TO BE IMPLEMENTED";
	}
}
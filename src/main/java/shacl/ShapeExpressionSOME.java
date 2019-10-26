package shacl;

public class ShapeExpressionSOME extends ShapeExpression {

    private ShapeExpression innerExpr;
    private PathExpression pathExpression;

    static public ShapeExpressionSOME constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

        int posGets = input.indexOf("SOME");

        if (posGets == -1)   {

            throw new ParseAttemptFailException("failed to see the keyword SOME");

        } else {

            PathExpression lhs = PathExpression.constructFromText(input.substring(0, posGets));
            ShapeExpression rhs = ShapeExpression.constructFromText(input.substring(posGets + 4));

            ShapeExpressionSOME ret = new ShapeExpressionSOME();
            ret.pathExpression = lhs;
            ret.innerExpr = rhs;

            return ret;
        }


    }


    @Override
    public String toString() {
        return "(SOME "+ pathExpression.toString() + " IS " +  innerExpr.toString() + ")";
    }

    @Override
    public String toRules(String head_predicate_name) {

        String new_bin_predicate_name = Context.getNewBinaryPredicateName();
        String new_unary_predicate_name = Context.getNewUnaryPredicateName();

        return head_predicate_name+ "(X) :- " + new_bin_predicate_name +"(X,Y)," + new_unary_predicate_name + "(Y).\n" +
               pathExpression.toRules(new_bin_predicate_name) + innerExpr.toRules(new_unary_predicate_name);
    }


	@Override
	public String getName() {
		return pathExpression.getName()+"_"+innerExpr.getName();
	}
}
package shacl;

public class PathExpressionSTAR extends PathExpression {

        private PathExpression innerExpr;

        static public PathExpressionSTAR constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

            input = input.trim();
            if (input.charAt(input.length()-1)!='*') {

                throw new ParseAttemptFailException("failed to see *");

            } else {

                PathExpression innerExpr = PathExpression.constructFromText(input.substring(0, input.length()-1));

                PathExpressionSTAR ret = new PathExpressionSTAR();
                ret.innerExpr = innerExpr;
                return ret;
            }

        }


    @Override
    public String toString() {
        return "("+ this.innerExpr.toString() + ")^*";

    }

    @Override
    public String toRules(String head_predicate_name) {

        String new_name_inner = Context.getNewBinaryPredicateName();

        return  head_predicate_name+"(X,Z) :- " + head_predicate_name+"(X,Y)," + new_name_inner + "(Y,Z).\n" +
                head_predicate_name+"(X,X) :- ADom(X).\n"
                + innerExpr.toRules(new_name_inner);

    }


	@Override
	public String getName() {
		return innerExpr.getName();
	}

}

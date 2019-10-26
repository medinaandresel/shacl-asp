package shacl;

public class PathExpressionUNION extends PathExpression {

        private PathExpression lhs, rhs;

        static public PathExpressionUNION constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

            int posGets = input.indexOf("UNION");

            if (posGets == -1)   {

                throw new ParseAttemptFailException("failed to see the keyword union");

            } else {

                PathExpression lhs = PathExpression.constructFromText(input.substring(0, posGets));
                PathExpression rhs = PathExpression.constructFromText(input.substring(posGets + 5));

                PathExpressionUNION ret = new PathExpressionUNION();
                ret.lhs = lhs;
                ret.rhs = rhs;

                return ret;
            }

        }

    @Override
    public String toString() {
        return "("+ lhs.toString() + " UNION " + rhs.toString() + ")";
    }

    @Override
    public String toRules(String head_predicate_name) {

        String new_name_left = Context.getNewBinaryPredicateName();
        String new_name_right = Context.getNewBinaryPredicateName();

        return  head_predicate_name+"(X,Y) :- " + new_name_left + "(X,Y).\n" +
                head_predicate_name+"(X,Y) :- " + new_name_right + "(X,Y).\n" +
                lhs.toRules(new_name_left) + rhs.toRules(new_name_right);

    }

	@Override
	public String getName() {
		return lhs.getName()+"_U_"+rhs.getName();
	}
}
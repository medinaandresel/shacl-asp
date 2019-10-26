package shacl;

public class PathExpressionCONC extends PathExpression {

        private PathExpression lhs, rhs;

        static public PathExpressionCONC constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

            int posGets = input.indexOf("CONC");

            if (posGets == -1)   {

                throw new ParseAttemptFailException("failed to see the keyword CONC");

            } else {

                PathExpression lhs = PathExpression.constructFromText(input.substring(0, posGets));
                PathExpression rhs = PathExpression.constructFromText(input.substring(posGets + 4));

                PathExpressionCONC ret = new PathExpressionCONC();
                ret.lhs = lhs;
                ret.rhs = rhs;

                return ret;
            }

        }


    @Override
    public String toString() {
        return "("+ lhs.toString() + " CONC " + rhs.toString() + ")";
    }

    @Override
    public String toRules(String head_predicate_name) {

        String new_name_left = Context.getNewBinaryPredicateName();
        String new_name_right = Context.getNewBinaryPredicateName();

        return  head_predicate_name+"(X,Z) :- " + new_name_left + "(X,Y),"+ new_name_right + "(Y,Z).\n" +
                lhs.toRules(new_name_left) + rhs.toRules(new_name_right);

    }


	@Override
	public String getName() {
		return lhs.getName()+"_star_"+rhs.getName();
	}

}
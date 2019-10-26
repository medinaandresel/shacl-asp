package shacl;

public class ShapeExpressionAND extends ShapeExpression {

        private ShapeExpression lhs, rhs;

        static public ShapeExpressionAND constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

            int posGets = input.indexOf("AND");

            if (posGets == -1)   {

                throw new ParseAttemptFailException("failed to see the keyword AND");

            } else {

                ShapeExpression lhs = ShapeExpression.constructFromText(input.substring(0, posGets));
                ShapeExpression rhs = ShapeExpression.constructFromText(input.substring(posGets + 3));

                ShapeExpressionAND ret = new ShapeExpressionAND();
                ret.lhs = lhs;
                ret.rhs = rhs;

                return ret;
            }

        }


    @Override
    public String toString() {
        return "(AND "+ lhs.toString() + " " + rhs.toString() + ")";
    }


    @Override
    public String toRules(String head_predicate_name) {

        String new_name_left = Context.getNewUnaryPredicateName();
        String new_name_right = Context.getNewUnaryPredicateName();

        return  head_predicate_name+"(X) :- " + new_name_left + "(X),"+ new_name_right + "(X).\n" +
                lhs.toRules(new_name_left) + rhs.toRules(new_name_right);

    }


	@Override
	public String getName() {
		return lhs.getName()+"_and_"+rhs.getName();
	}

}
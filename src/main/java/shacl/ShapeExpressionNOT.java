package shacl;

public class ShapeExpressionNOT extends ShapeExpression {

    private ShapeExpression innerExpr;

    static public ShapeExpressionNOT constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

        int posGets = input.indexOf("NOT");

        if ((posGets == -1) || (!input.substring(0,posGets).trim().isEmpty())) {

            throw new ParseAttemptFailException("failed to see NOT, or there is something to the left of the keyword");

        } else
            {


            ShapeExpression innerExpr = ShapeExpression.constructFromText(input.substring(posGets + 3));

            ShapeExpressionNOT ret = new ShapeExpressionNOT();
            ret.innerExpr = innerExpr;

            return ret;
        }

    }


    @Override
    public String toString() {
        return "(NOT "+ innerExpr.toString() + ")";
    }


    @Override
    public String toRules(String head_predicate_name) {


        String new_name_inner = Context.getNewUnaryPredicateName();

        return  head_predicate_name+"(X) :-ADom(X), not " + new_name_inner + "(X).\n" +
                innerExpr.toRules(new_name_inner);

    }


	@Override
	public String getName() {
		return "Not_"+innerExpr.getName();
	}



}
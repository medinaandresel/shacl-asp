package shacl;

public class ShapeExpressionATMOST extends ShapeExpression {

    private ShapeExpression innerExpr;
    private PathExpression pathExpression;
    private int number;

    static public ShapeExpressionATMOST constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

        int posGets = input.indexOf("MAX");

        if ((posGets == -1) || (!input.substring(0,posGets).trim().isEmpty())) {

            throw new ParseAttemptFailException("failed to see MAX keyword, or there is something to the left of the keyword");

        } else
        {

            String substring = input.substring(posGets + 3).trim();

            int spacePos     = substring.indexOf(" ");

            if (spacePos == -1) {

                throw new ParseAttemptFailException("parts of the atleast expression are missing");

            }

            String numberStr = substring.substring(0,spacePos);
            int number = Integer.decode(numberStr);


            String path_constraint_str = substring.substring(spacePos+1, substring.length());

             spacePos = path_constraint_str.indexOf(" ");

            if (spacePos == -1) {

                throw new ParseAttemptFailException("parts of the atleast expression are missing");

            }


            PathExpression  pathExpression = PathExpression.constructFromText(path_constraint_str.substring(0,spacePos));
            ShapeExpression innerExpr =     ShapeExpression.constructFromText(path_constraint_str.substring(spacePos));

            ShapeExpressionATMOST ret = new ShapeExpressionATMOST();
            ret.innerExpr = innerExpr;
            ret.pathExpression = pathExpression;
            ret.number = number;


            return ret;
        }

    }


    @Override
    public String toString() {
        return "(ATMOST "+this.number+ " OF " + pathExpression.toString() + " ARE " +  innerExpr.toString() + ")";

    }


    @Override
    public String toRules(String head_predicate_name) {
        return "PLEASE USE not ATLEAST "+(number+1);
    }


	@Override
	public String getName() {
		return null;
	}

}
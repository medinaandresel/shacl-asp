package shacl;

public class ShapeExpressionATLEAST extends ShapeExpression {

    private ShapeExpression innerExpr;
    private PathExpression pathExpression;
    private int number;

    static public ShapeExpressionATLEAST constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

        int posGets = input.indexOf("MIN");

        if ((posGets == -1) || (!input.substring(0,posGets).trim().isEmpty())) {

            throw new ParseAttemptFailException("failed to see ATLEAST keyword, or there is something to the left of the keyword");

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

            ShapeExpressionATLEAST ret = new ShapeExpressionATLEAST();
            ret.innerExpr = innerExpr;
            ret.pathExpression = pathExpression;
            ret.number = number;


            return ret;
        }

    }


    @Override
    public String toString() {
        return "(ATLEAST "+this.number+ " OF " + pathExpression.toString() + " ARE " +  innerExpr.toString() + ")";

    }


    @Override
    public String toRules(String head_predicate_name) {
    	String rules = "";
    	String atLeastNeS = this.getName();
    	rules += head_predicate_name+"(X) :- "+atLeastNeS+"(X,Y).\n"+
    	"AtLeast_0"+"_"+pathExpression.getName()+"_"+innerExpr.getName()+"(X,X) :- ADom(X). \n"+
    	"AtLeast_1"+"_"+pathExpression.getName()+"_"+innerExpr.getName()
    	+"(X,Y) :- First_"+pathExpression.getName()+"(X,Y),"+innerExpr.getName()+"(Y).\n";
    	
    	for (int i=2; i<=number;i++)
    	{
    		rules += "AtLeast_"+i+"_"+pathExpression.getName()+"_"+innerExpr.getName()+"(X,Z) :- "+
    				"Next_"+pathExpression.getName()+"(X,Y,Z), AtLeast_"+i+"_"+pathExpression.getName()+"_"+innerExpr.getName()+"(X,Y). \n";
    		rules += "AtLeast_"+i+"_"+pathExpression.getName()+"_"+innerExpr.getName()+"(X,Z) :- "+
    				"Next_"+pathExpression.getName()+"(X,Y,Z), AtLeast_"+(i-1)+"_"+pathExpression.getName()+"_"+innerExpr.getName()+"(X,Y),"+
    				innerExpr.getName()+"(Z), Y!=Z. \n";
    				
    	}
    	
    	
    	return "\n"+rules+"\n"+Context.getBuiltIns(pathExpression.getName())+"\n";
    }


	@Override
	public String getName() {
		return "AtLeast_"+number+"_"+pathExpression.getName()+"_"+innerExpr.getName();
	}

}
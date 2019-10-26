package shacl;

public class Constraint  {

    private ShapeExpressionUnaryPredicate head;
    private ShapeExpression body;


    public static Constraint constructFromText(String input) throws SyntaxErrorException {

        /*
        * if ":-" is not found, then we report an error
        * otherwise, the lhs is a unary predicate and the rhs is a shape expression
        *
        * */

        int posGets = input.indexOf(":-");

        if (posGets == -1) {

            throw new SyntaxErrorException(
                    "Syntax error: couldn't identify a a contraint, ':-' expected. Offending text:"
                            + input);

        } else {

            String lhs = input.substring(0, posGets);
            String rhs = input.substring(posGets + 2);

            try {

                ShapeExpressionUnaryPredicate head = ShapeExpressionUnaryPredicate.constructFromText(lhs);
                ShapeExpression body = ShapeExpression.constructFromText(rhs);


                Constraint ret = new Constraint();
                ret.head = head;
                ret.body = body;

                return ret;

            } catch (ParseAttemptFailException e) {
                throw new SyntaxErrorException("unrecoverable");
            }

        }


    }

    @Override
    public String toString() {
        return this.head.toString() + " <= "+this.body.toString();
    }


    public String toRules() {
        return "\n % " +this.toString() + "\n" + body.toRules(head.getPredicateName());
    }
    
    public ShapeExpressionUnaryPredicate getHead()
    {
    	return head;
    }
    
    public ShapeExpression getBody()
    {
    	return body;
    }
    
}

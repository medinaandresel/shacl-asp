package shacl;

public abstract class PathExpression implements ConvertsToProgram {

    static PathExpression constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

//        System.out.println("TryingPathExpression: "+input);

        input = input.trim();
        int input_length = input.length();

        if (input_length == 0) {
            throw new SyntaxErrorException("Syntax error: empty (sub)expression!");
        }

        try {
            if (input.charAt(0) == '(') {

                int pos = 1;
                int count = 1;
                while ((count != 0) && pos!= input_length ) {
                    if (input.charAt(pos) == ')') count = count - 1;
                    if (input.charAt(pos) == '(') count = count + 1;
                    if (count != 0) pos++;
                }

                if ((pos == input_length-1) && (count ==0)) {
                    return PathExpression.constructFromText(input.substring(1,input_length-1));
                } else {
                    throw new ParseAttemptFailException("Brackets do not match");
                }

            } } catch (ParseAttemptFailException e) {};

        try {
            PathExpressionSTAR peSTAR = PathExpressionSTAR.constructFromText(input);
            return peSTAR;
        } catch (ParseAttemptFailException e) {}


        try {
            PathExpressionUNION peUNION = PathExpressionUNION.constructFromText(input);
            return peUNION;
        } catch (ParseAttemptFailException e) { }

        try {
            PathExpressionCONC peCONC = PathExpressionCONC.constructFromText(input);
            return peCONC;
        } catch (ParseAttemptFailException e) { }

        try {
            PathExpressionBinaryPredicate peBinaryPredicate = PathExpressionBinaryPredicate.constructFromText(input);
            return peBinaryPredicate;
        }
       catch (ParseAttemptFailException e) {

            throw new ParseAttemptFailException("Failed to parse SHACL expression!");

        }


    }

}
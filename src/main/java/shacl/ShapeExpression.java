package shacl;

public abstract class ShapeExpression implements ConvertsToProgram {

    static ShapeExpression constructFromText(String input) throws ParseAttemptFailException,SyntaxErrorException {

       // System.out.println("Trying: "+input);

        input = input.trim();
        int input_length = input.length();

        if (input_length == 0) {
            throw new SyntaxErrorException("Syntax error: empty shape (sub)expression!");
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
                return ShapeExpression.constructFromText(input.substring(1,input_length-1));
            } else {
                throw new ParseAttemptFailException("Brackets do not match");
            }

        } } catch (ParseAttemptFailException e) {};



        try {
            ShapeExpressionUnaryPredicate seUnaryPredicate = ShapeExpressionUnaryPredicate.constructFromText(input);
            return seUnaryPredicate;
        } catch (ParseAttemptFailException e) {}


        try {
            ShapeExpressionOR seOR = ShapeExpressionOR.constructFromText(input);
            return seOR;
        } catch (ParseAttemptFailException e) { }

        try {
            ShapeExpressionAND seAND = ShapeExpressionAND.constructFromText(input);
            return seAND;
        } catch (ParseAttemptFailException e) { }

        try {
            ShapeExpressionSOME seSOME = ShapeExpressionSOME.constructFromText(input);
            return seSOME;
        } catch (ParseAttemptFailException e) { }


        try {
            ShapeExpressionALL seALL = ShapeExpressionALL.constructFromText(input);
            return seALL;
        } catch (ParseAttemptFailException e) { }


        try {
            ShapeExpressionATLEAST seATLEAST = ShapeExpressionATLEAST.constructFromText(input);
            return seATLEAST;
        } catch (ParseAttemptFailException e) { }

        try {
            ShapeExpressionATMOST seATMOST = ShapeExpressionATMOST.constructFromText(input);
            return seATMOST;
        } catch (ParseAttemptFailException e) { }

        try {
            ShapeExpressionNOT seNOT = ShapeExpressionNOT.constructFromText(input);
            return seNOT;
        } catch (ParseAttemptFailException e)
        {

            throw new ParseAttemptFailException("Failed to parse SHACL expression!");

        }
        
        
        

    }

}
package shacl;

public class Context {

public static int bin_counter = 0;
    public static int unary_counter = 0;

public static boolean builtIns = false;

public static String getNewBinaryPredicateName() {

    Context.bin_counter++;
    return "Rel"+ Context.bin_counter;
}



    public static String getNewUnaryPredicateName() {

        Context.unary_counter++;
        return "Aux"+ Context.unary_counter;
    }
    
    public static String getBuiltIns()
    {
    	if (builtIns==true)
    		return "";
    	else
    	{
    		builtIns = true;
    		return "LEQ(X,X) :- ADom(X). \n"
    				+ "LEQ(X,Y) :- ADom(X), ADom(Y), not LEQ(Y,X). \n"
    				+ "LEQ(X,Z) :- LEQ(X,Y), LEQ(Y,Z).\n";
    	}
    }
    
    public static String getBuiltIns(String predicate)
    {
    	return 
    			"LEQ_"+predicate+"(X,X) :- "+predicate+"(Y,X). \n"
				+ "LEQ_"+predicate+"(Y,Z) :-"+predicate+"(X,Y),"+predicate+"(X,Z)"+", not LEQ_"+predicate+"(Z,Y). \n"
				+ "LEQ_"+predicate+"(X,Z) :- LEQ_"+predicate+"(X,Y), LEQ_"+predicate+"(Y,Z).\n"+
    		   "NonEmpty_"+predicate+"(X) :- "+predicate+"(X,Y). \n"+
    		   "Empty_"+predicate+"(X) :- ADom(X), not NonEmpty_"+predicate+"(X). \n"+
    		   "Next_"+predicate+"(X,Y,Z) :- "+predicate+"(X,Y),"+predicate+"(X,Z),"+"LEQ_"+predicate+"(Y,Z). \n"+
    		   "NonFirst_"+predicate+"(X,Z) :- "+ predicate+"(X,Y),"+predicate+"(X,Z),LEQ_"+predicate+"(Y,Z), Y!=Z. \n"+
    		   "First_"+predicate+"(X,Y) :- "+ predicate+"(X,Y), not "+"NonFirst_"+predicate+"(X,Y) .\n"+
    		   "NonLast_"+predicate+"(X,Y) :- "+ predicate+"(X,Y),"+ predicate+"(X,Z),LEQ_"+predicate+"(Y,Z), Y!=Z.  \n"+
    		   "Last_"+predicate+"(X,Y) :- "+ predicate+"(X,Y), not NonLast_"+predicate+"(X,Y). \n";
    
    }


}

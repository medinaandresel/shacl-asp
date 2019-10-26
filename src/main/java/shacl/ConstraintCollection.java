package shacl;

import java.util.ArrayList;
import java.util.List;

public class ConstraintCollection  {

    private List<Constraint> constraints = null;

  //  private ConstraintCollection() { }

    public static ConstraintCollection constructFromText(String input) throws SyntaxErrorException {

        /*
        * if input is empty, then construct empty list
        * if input non-empty, then
        *   if input contains ";", then LHS should parse as a constraint and the RHS as a list of containts
        *   if input does not contain ";", then ret part as a contraint and return a singleton list
        *
        * */

        if (input.trim().isEmpty()) {
            ConstraintCollection cc = new ConstraintCollection();
            cc.constraints = new ArrayList<Constraint>();
            return cc;
        }

        int posSemiCol = input.indexOf(";");

        if (posSemiCol == -1) {
            Constraint constraint = Constraint.constructFromText(input);
            ConstraintCollection cc = new ConstraintCollection();
            cc.constraints = new ArrayList<Constraint>();
            cc.constraints.add(constraint);
            return cc;


        } else {

            String lhs = input.substring(0, posSemiCol);
            String rhs = input.substring(posSemiCol + 1);


            ConstraintCollection cc = ConstraintCollection.constructFromText(rhs);
            Constraint constraint = Constraint.constructFromText(lhs);

            cc.constraints.add(constraint);
            return cc;

        }

    }

    @Override
    public String toString() {
        //return this.constraints.stream().map(Constraint::toString).collect(Collectors.joining(" \n"));

        String out = "";
        for (Constraint con: this.constraints) {
            out += con.toString()+" \n";
        }
        return out;
    }

    public String toRules() {

        String out = Predicate.toADomRules();
        for (Constraint con: this.constraints) {
            out += con.toRules()+" \n";
        }
        return out;

    }
    
    public List<Constraint> getConstraints ()
    {
    	return constraints;
    }
}
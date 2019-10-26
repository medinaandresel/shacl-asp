package shacl;

import java.util.HashMap;
import java.util.Map;

public class Predicate {

    private String relationName;
    private int arity;

    final static private Map<String, Predicate> pool = new HashMap<String, Predicate>();


    public Predicate(String relationName, int arity) {
        this.relationName = relationName;
        this.arity = arity;
    }

    public String getName() {
        return relationName;
    }

    public int getArity() {
        return arity;
    }


    public String toString() {

        return "<"+this.relationName+"/" + this.arity + ">";
    }


    static Predicate predicateFromString(String predicateStr, int arity) throws SyntaxErrorException {

        predicateStr = predicateStr.trim();

        if (predicateStr.equals("or") || predicateStr.equals("and") || predicateStr.equals("not")  ) {
            throw new SyntaxErrorException("Syntax error: keyword used as relation symbol");

        }

        if (Predicate.pool.containsKey(predicateStr)) {

            Predicate pred = Predicate.pool.get(predicateStr);

            if (pred.getArity() != arity) {
                throw new SyntaxErrorException("Syntax error: a single relation name cannot be used with different arities.  Offending relation name: " + predicateStr);
            } else {
                return pred;
            }

        } else {
            Predicate ret = new Predicate(predicateStr, arity);

            pool.put(predicateStr, ret);

            return ret;
        }

    }

    public static String toADomRules() {
        String ret = "";
        for (Predicate pred: Predicate.pool.values()) {

            if (pred.arity ==1) {
                ret += "ADom(X):-" + pred.getName() + "(X).\n";
            }

            if (pred.arity ==2) {
                ret += "ADom(X):-" + pred.getName() + "(X,Y).\n";
                ret += "ADom(Y):-" + pred.getName() + "(X,Y).\n";
            }

        }
        return ret;
    }
}

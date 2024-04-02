package ConMerge;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//QuickXPlain Algorithm	"Ulrich Junker"
//--------------------
//QuickXPlain(B, C={c1,c2,…, cm})
//IF consistent(B∪C) return "No conflict"; 
//IF isEmpty(C) return Φ;
//ELSE return QX(B, B, C);

//func QX(B,Δ, C={c1,c2, …, cq}): conflictSet Δ
//IF (Δ != Φ AND inconsistent(B)) return Φ; /* pruning of C */
//IF (C = {cα}) return({cα}); /* conflict element cα detected */
//k=n/2;
//C1 <-- {c1, …, ck}; C2 <-- {ck+1, …, cq}; /* B still consistent */
//D2 <-- QX(B ∪ C1, C1, C2);
//D1 <-- QX(B ∪ D2, D2, C1);
//return (D2 ∪ D1)

public class QuickXplain {

    public static List<Constraint> quickXPlain(List<Constraint> b, List<Constraint> c, Model model)
    {
        List<Constraint> ac = new ArrayList<Constraint>(); ac.addAll(b); ac.addAll(c);
        //IF (is empty(C) or consistent(B ∪ C)) return Φ
        if (c.isEmpty() || isConsistent(ac,model))
            return Collections.<Constraint>emptyList();
        else{ //ELSE return QX(B, B, C)
            return qx(b,b, c, model);}
    }
    // func QX(B,Δ, C={c1,c2, …, cq}): conflictSet Δ
    private static List<Constraint> qx(List<Constraint> b, List<Constraint> d, List<Constraint> c, Model model){
        List<Constraint> conflictSet = new ArrayList<Constraint>();
        int cSize=c.size();
        // IF (Δ != Φ AND inconsistent(B)) return Φ;
        if( !d.isEmpty() && !isConsistent(b, model))
            return Collections.<Constraint>emptyList();
        // if singleton(C) return C;
        if(cSize==1)
            return c;
        int k = cSize/2;  // k = q/2;
        // C1 = {c1..ck}; C2 = {ck+1..cq};	
        List<Constraint> c1 = new ArrayList<Constraint>();
        c1.addAll(c.subList(0, k));
        List<Constraint> c2 = new ArrayList<Constraint>();
        c2.addAll(c.subList(k, cSize));
        //Saving B of the parent node
        List<Constraint> prevB=new ArrayList<Constraint>();
        prevB.addAll(b);
        // D2 = QX(B ∪ C1, C1, C2);  // D1 = QX(B ∪ D2, D2, C1);
        List<Constraint> d2 = qx(constrsUnion(b,c1), c1, c2,model);
        conflictSet.addAll(d2);
        List<Constraint> d1 = qx(constrsUnion(prevB,d2), d2, c1,model);
        for (int i=0; i<d1.size(); i++)
            if (!conflictSet.contains(d1.get(i)))
                conflictSet.add(d1.get(i));
        return conflictSet;
    }
    // Check if set of constraint is consistent
    private static boolean isConsistent(List<Constraint> constrs, Model model)
    {
        model.getSolver().reset();
        model.unpost(model.getCstrs());
        for (int i=0; i<constrs.size(); i++)
        {
            model.post(constrs.get(i));
        }
        boolean consistent=model.getSolver().solve();
        return consistent;
    }

    //Calculate c1 ∪ c2
    public static List<Constraint> constrsUnion(List<Constraint> c1, List<Constraint> c2)
    {
        c1.addAll(c2);
        return c1;
    }
}
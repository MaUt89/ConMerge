package ConMerge;

import org.chocosolver.solver.constraints.Constraint;

import java.util.ArrayList;
import java.util.List;

public class ConstraintUtils {

    public static List<Constraint> arrayToList(Constraint[] cstrs)
    {
        List<Constraint> l = new ArrayList<Constraint>();
        for (int i=0; i<cstrs.length; i++)
            l.add(cstrs[i]);
        return l;
    }
}

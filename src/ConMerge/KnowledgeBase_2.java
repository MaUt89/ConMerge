package ConMerge;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;

public class KnowledgeBase_2 {

    public static String[][] getKnowledgeBase2_Variables() {
        String[][] VarArray = new String[][]
                {
                        {"intVar", "CPU", "1,2"},
                        {"intVar", "MB", "1,2,3"}

                };
        return VarArray;
    }

    public static String[][] getKnowledgeBase2_Constraints(){
        String[][] ConstraintArray = new String[][]{
                {"CPU", "CPUs", "assign","MB","MBgold"},
                {"CPU", "CPUg", "allowed", "MB", "MBsilver", "MBdiamond"},
                {"CPU", "CPUs", "incompatible", "MB", "MBsilver"},
                {"CPU", "CPUs", "incompatible", "MB", "MBdiamond"},
                {"CPU", "CPUg", "incompatible", "MB", "MBgold"}
        };
        return ConstraintArray;
    }
    /*
    public static void getKnowledgeBase2_Constraints(Model ModelKB2, ArrayList<IntVar> IntVariables){
        ModelKB2.ifThen(
                ModelKB2.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(0)),
                ModelKB2.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(1))
        );
        ModelKB2.ifThen(
                ModelKB2.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(1)),
                ModelKB2.or((ModelKB2.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(0))), (ModelKB2.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(2))))
        );
        ModelKB2.not(ModelKB2.and(ModelKB2.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(0)), ModelKB2.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(0)))).post();
        ModelKB2.not(ModelKB2.and(ModelKB2.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(0)), ModelKB2.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(2)))).post();
        ModelKB2.not(ModelKB2.and(ModelKB2.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(1)), ModelKB2.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(1)))).post();
    }
    */
}

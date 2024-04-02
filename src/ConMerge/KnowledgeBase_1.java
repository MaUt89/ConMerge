package ConMerge;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;

public class KnowledgeBase_1 {

    public static String[][] getKnowledgeBase1_Variables() {
        String[][] VarArray = new String[][]
                {
                        {"intVar", "CPU", "1,3"},
                        {"intVar", "MB", "1,3"}

                };
        return VarArray;
    }

    public static String[][] getKnowledgeBase1_Constraints(){
        String[][] ConstraintArray = new String[][]{
                {"CPU", "CPUs", "assign","MB","MBdiamond"},
                {"CPU", "CPUd", "assign", "MB", "MBsilver"},
                {"CPU", "CPUs", "incompatible", "MB", "MBsilver"},
                {"CPU", "CPUd", "incompatible", "MB", "MBdiamond"}
        };
        return ConstraintArray;
    /*
    public static void getKnowledgeBase1_Constraints(Model ModelKB1, ArrayList<IntVar> IntVariables){

        ModelKB1.ifThen(
                ModelKB1.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(0)),
                ModelKB1.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(1))
        );
        ModelKB1.ifThen(
                ModelKB1.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(1)),
                ModelKB1.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(0))
        );
        ModelKB1.not(ModelKB1.and(ModelKB1.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(0)), ModelKB1.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(0)))).post();
        ModelKB1.not(ModelKB1.and(ModelKB1.arithm(IntVariables.get(0), "=", IntVariables.get(0).nextValue(1)), ModelKB1.arithm(IntVariables.get(1), "=", IntVariables.get(1).nextValue(1)))).post();
        */

    }
}

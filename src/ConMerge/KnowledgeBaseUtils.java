package ConMerge;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KnowledgeBaseUtils {



    public static ArrayList<MyVariable> getVariables(String[][] VarArray) {

        ArrayList<MyVariable> Vars = new ArrayList<>();

        for (String[] item : VarArray) {
            String type = item[0];
            String name = item[1];
            String[] vals = item[2].split(",");
            int[] ivals = new int[vals.length];

            for (int i = 0; i < vals.length; i++) {
                ivals[i] = Integer.parseInt(vals[i]);
            }

            Vars.add(new MyVariable(type, name, ivals));
        }
        return Vars;
    }

    public static void createConstraints(String[][] Constraints, Model model, ArrayList<IntVar> IntVariables){

        ArrayList<String> parameter = new ArrayList<>();
        String trigger = "";
        String triggerVal = "";
        String type = "";

        for (int i = 0; i < Constraints.length; i++) {
            String[] Input = Constraints[i];
            for (int j = 0; j < Input.length; j++) {
                // split constraint
                switch (j) {
                    case (0):
                        trigger = Input[j];
                    continue;
                    case (1):
                        triggerVal = Input[j];
                        continue;
                    case(2):
                        type = Input[j];
                        continue;
                    default:
                        parameter.add(Input[j]);
                        continue;
                }
            }
            // categorize constraint
            int t = getVarAsInt(trigger);
            int tv = getVarValue(trigger, triggerVal);
            int IntVarPosT = getIntVarPos(IntVariables.get(t), tv);
            int p = getVarAsInt(parameter.get(0));
            int pv = getVarValue(parameter.get(0), parameter.get(1));
            int IntVarPosP = getIntVarPos(IntVariables.get(p), pv);

            switch (type){
                case("assign"):
                    model.ifThen(
                            model.arithm(IntVariables.get(t), "=", IntVariables.get(t).nextValue(IntVarPosT)),
                            model.arithm(IntVariables.get(p), "=", IntVariables.get(p).nextValue(IntVarPosP))
                    );
                    parameter.clear();
                    continue;
                case("incompatible"):
                    model.not(model.and(model.arithm(IntVariables.get(t), "=", IntVariables.get(t).nextValue(IntVarPosT)), model.arithm(IntVariables.get(p), "=", IntVariables.get(p).nextValue(IntVarPosP)))).post();
                    parameter.clear();
                    continue;
                case("allowed"):
                    int p2 = getVarAsInt(parameter.get(0));
                    int pv2 = getVarValue(parameter.get(0), parameter.get(2));
                    int IntVarPosP2 = getIntVarPos(IntVariables.get(p2), pv2);
                    model.ifThen(
                            model.arithm(IntVariables.get(t), "=", IntVariables.get(t).nextValue(IntVarPosT)),
                            model.or((model.arithm(IntVariables.get(p), "=", IntVariables.get(p).nextValue(IntVarPosP))), (model.arithm(IntVariables.get(p2), "=", IntVariables.get(p2).nextValue(IntVarPosP2))))
                    );
                    parameter.clear();
                    continue;
            }
        }
    }

    public static String[][] createIncompatibleConstraints(String[][] Constraints, ArrayList<IntVar> IntVariables){

        // Get all parameters of IntVariables
        String[][] varPar = new String[IntVariables.size()][];

        for (int i = 0; i < IntVariables.size(); i++) {
            varPar[i] = new String[IntVariables.get(i).getDomainSize() + 1];
            for (int j = 0; j < varPar[i].length; j++) {
                if (j == varPar[i].length - 1) {
                    varPar[i][j] = IntVariables.get(i).getName();
                } else {
                    varPar[i][j] = getVarValueName(IntVariables.get(i).getName(), IntVariables.get(i).nextValue(j));
                }
            }
        }

        // Check if constraints between all parameters exists and create new constraint if not
       ArrayList<String[]> ConstraintsList = new ArrayList<>();
        for (String[] item: Constraints){
            ConstraintsList.add(item);
        }
        ArrayList<String[]> newConstraints = new ArrayList<>();
        for (String[] item: varPar) {
            ArrayList<String[]> relevantConstraints = new ArrayList<>();
            for (int i = 0; i < item.length-1; i++) {
                relevantConstraints.clear();
                String startPar = item[i];
                for (String[] Constraint : ConstraintsList) {
                    if (Arrays.asList(Constraint).contains(startPar)) {
                        relevantConstraints.add(Constraint);
                    }
                }

                for (String[] item2 : varPar) {
                    for (int j = 0; j < item2.length-1; j++) {
                        if (item2.equals(item)) {
                            break;
                        } else {
                            String targetPar = item2[j];
                            boolean check = false;
                            for (String[] Constraint : relevantConstraints) {
                                if (Arrays.asList(Constraint).contains(targetPar)) {
                                    check = true;
                                    break;
                                } else {
                                    check = false;

                                }
                            }
                            if (!check) {
                                String[] newConstraint = new String[]{item[item.length-1], startPar, "incompatible", item2[item2.length-1], targetPar};
                                newConstraints.add(newConstraint);
                            }
                        }
                    }
                }
            }
            for (String[] newConstraint: newConstraints) {
                ConstraintsList.add(newConstraint);
            }
        }

        // Add new constraints to existing knowledge base constraints
        String[][] resultConstraints = new String[Constraints.length+newConstraints.size()][];
        for (int i = 0; i < Constraints.length; ++i) {
            resultConstraints[i] = new String[Constraints[i].length];
            for (int j = 0; j < Constraints[i].length; ++j) {
                resultConstraints[i][j] = Constraints[i][j];
            }
        }

        for (int i = 0; i < newConstraints.size(); i++) {
            resultConstraints[i+Constraints.length] = newConstraints.get(i);
            String[] row = newConstraints.get(i);
            for (int j = 0; j < row.length; j++) {
                resultConstraints[i+Constraints.length][j] = row[j];
            }
        }
        return resultConstraints;
    }


    public static String getVarValueName(String varName, int varValue) {
        if (varName == "CPU") {
            if (varValue == 1)
                return "CPUs";
            else if (varValue == 2)
                return "CPUg";
            else
                return "CPUd";
        } else {
            if (varValue == 1)
                return "MBsilver";
            else if (varValue == 2)
                return "MBgold";
            else
                return "MBdiamond";
        }
    }

    public static int getVarValue(String varName, String varValueName) {
        if (varName == "CPU") {
            if (varValueName == "CPUs")
                return 1;
            else if (varValueName == "CPUg")
                return 2;
            else
                return 3;
        } else {
            if (varValueName == "MBsilver")
                return 1;
            else if (varValueName == "MBgold")
                return 2;
            else
                return 3;
        }
    }

    public static int getVarAsInt(String varName){
        if (varName == "CPU"){
            return 0;
        }
        else{
            return 1;
        }
    }

    public static int getIntVarPos(IntVar IntVariable, int VarNumber){
        int result = 0;
        for (int i = 0; i < IntVariable.getDomainSize(); i++) {
            if (VarNumber == IntVariable.nextValue(i)){
                result = i;
                break;
            }
            else{
                continue;
            }
        }
        return result;
    }
}

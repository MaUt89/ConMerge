package ConMerge;

import org.chocosolver.solver.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KnowledgeBaseMergeUtils {

    public static ArrayList<MyVariable> mergeVariables(ArrayList<MyVariable> VariablesKB1, ArrayList<MyVariable> VariablesKB2){

        ArrayList<MyVariable> VariablesKBMerge = new ArrayList<>();
        ArrayList<Integer> ValuesMerge = new ArrayList<>();

        for (int i = 0; i < VariablesKB1.size() ; i++) {
            for (int j = 0; j < VariablesKB2.size() ; j++) {
                if (VariablesKB1.get(i).Name == VariablesKB2.get(j).Name){
                    int[] Values1 = VariablesKB1.get(i).Values;
                    int[] Values2 = VariablesKB2.get(j).Values;
                    for (int k = 0; k < Values1.length; k++) {
                        if (ValuesMerge.contains(Values1[k])) {

                        } else {
                            ValuesMerge.add(Values1[k]);
                        }
                        for (int l = 0; l < Values2.length; l++) {
                            if (Values1[k] == Values2[l]) {
                                continue;
                            } else {
                                if (ValuesMerge.contains(Values2[l])) {
                                } else {
                                    ValuesMerge.add(Values2[l]);
                                }
                            }
                        }
                    }
                    Collections.sort(ValuesMerge);
                }
            }
            String type = VariablesKB1.get(i).Type;
            String name = VariablesKB2.get(i).Name;
            int[] values = new int[ValuesMerge.size()];
            for (int j = 0; j < values.length; j++) {
                values[j] = ValuesMerge.get(j).intValue();
            }

            VariablesKBMerge.add(new MyVariable(type, name, values));
            ValuesMerge.clear();
        }
        return VariablesKBMerge;
    }

    public static String[][] mergeConstraints(String[][] Constraints1, String[][] Constraints2){

        String[][] ConstraintsMerge = new String[Constraints1.length+Constraints2.length][];

        for (int i = 0; i < Constraints1.length; i++) {
            ConstraintsMerge[i] = new String[Constraints1[i].length];
            for (int j = 0; j < Constraints1[i].length; j++) {
                ConstraintsMerge[i][j] = Constraints1[i][j];
            }
        }

        for (int i = 0; i < Constraints2.length; i++) {
            for (int j = 0; j < ConstraintsMerge.length; j++) {
                if (Arrays.deepEquals(Constraints2[i], ConstraintsMerge[j])){
                    continue;
            }
                else{
                    ConstraintsMerge[i+Constraints1.length] = new String[Constraints2[i].length];
                    for (int k = 0; k < Constraints2[i].length; k++) {
                        ConstraintsMerge[i+Constraints1.length][k] = Constraints2[i][k];
                        }
                    }
            }

        }
        return ConstraintsMerge;
    }
}

package ConMerge;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;


public class ConMerge {

    public static void main(String[] args) {

        // Create first model
        String[][] VarArray = KnowledgeBase_1.getKnowledgeBase1_Variables();
        ArrayList<MyVariable> VariablesKB1 = KnowledgeBaseUtils.getVariables(VarArray);
        ArrayList<IntVar> IntVariables = new ArrayList<>();
        Model ModelKB1 = new Model();

        // Add variables
        for (MyVariable item : VariablesKB1) {
            IntVar ivars = ModelKB1.intVar(item.Name, item.Values);
            IntVariables.add(ivars);
        }

        // Add constraints
        String[][] ConstraintArray = KnowledgeBase_1.getKnowledgeBase1_Constraints();
        KnowledgeBaseUtils.createConstraints(ConstraintArray, ModelKB1, IntVariables);

        System.out.println("First model:");

        // Print variables
        for (MyVariable item : VariablesKB1) {
            for (Integer item2 : item.Values) {
                String VarValue = KnowledgeBaseUtils.getVarValueName(item.Name, item2);
                String VarName = item.Name;
                System.out.println(VarName + " = " + VarValue);
            }
        }

        System.out.println("...................................................................");

        // Print constraints
        for (Constraint item : ModelKB1.getCstrs()) {
            System.out.println((item.getName()) + " --> " + item.toString());
        }

        System.out.println("...................................................................");

        // Print solutions

        Solver s = ModelKB1.getSolver();

        while (s.solve()) {
            System.out.println(s.getMeasures().getSolutionCount());
            for (IntVar item : IntVariables) {

                String VarValue = KnowledgeBaseUtils.getVarValueName(item.getName(), item.getValue());
                String VarName = item.getName();
                System.out.println(VarName + " = " + VarValue);

            }
        }

        System.out.println("...................................................................");
        System.out.println("Second model:");

        // Create second model
        String[][] VarArray2 = KnowledgeBase_2.getKnowledgeBase2_Variables();
        ArrayList<MyVariable> VariablesKB2 = KnowledgeBaseUtils.getVariables(VarArray2);
        ArrayList<IntVar> IntVariables2 = new ArrayList<>();
        Model ModelKB2 = new Model();
        // Add variables
        for (MyVariable item : VariablesKB2) {

            IntVar ivars = ModelKB2.intVar(item.Name, item.Values);
            IntVariables2.add(ivars);

        }
        // Add constraints
        String[][] ConstraintArray2 = KnowledgeBase_2.getKnowledgeBase2_Constraints();
        KnowledgeBaseUtils.createConstraints(ConstraintArray2, ModelKB2, IntVariables2);

        // Print variables
        for (MyVariable item : VariablesKB2) {
            for (Integer item2 : item.Values
            ) {
                String VarValue = KnowledgeBaseUtils.getVarValueName(item.Name, item2);
                String VarName = item.Name;
                System.out.println(VarName + " = " + VarValue);
            }
        }

        System.out.println("...................................................................");

        // Print constraints
        for (Constraint item : ModelKB2.getCstrs()) {

            System.out.println((item.getName()) + " --> " + item.toString());
        }

        System.out.println("...................................................................");

        // Print solutions

        Solver s2 = ModelKB2.getSolver();

        while (s2.solve()) {
            System.out.println(s2.getMeasures().getSolutionCount());
            for (IntVar item : IntVariables2) {

                String VarValue = KnowledgeBaseUtils.getVarValueName(item.getName(), item.getValue());
                String VarName = item.getName();
                System.out.println(VarName + " = " + VarValue);

            }
        }

        System.out.println("...................................................................");
        System.out.println("Merge knowledge bases:");

        // Merge variables
        ArrayList<MyVariable> VariablesKBMerge = KnowledgeBaseMergeUtils.mergeVariables(VariablesKB1, VariablesKB2);

        ArrayList<IntVar> IntVariablesMerge = new ArrayList<>();
        Model ModelKBMerge = new Model();

        // Add variables
        for (MyVariable item : VariablesKBMerge) {
            IntVar ivars = ModelKBMerge.intVar(item.Name, item.Values);
            IntVariablesMerge.add(ivars);
        }

        // Create resulting incompatible constraints for each knowledge base
        ConstraintArray = KnowledgeBaseUtils.createIncompatibleConstraints(ConstraintArray, IntVariablesMerge);
        ConstraintArray2 = KnowledgeBaseUtils.createIncompatibleConstraints(ConstraintArray2, IntVariablesMerge);

        // Merge constraints
        String[][] ConstraintArrayMerge = KnowledgeBaseMergeUtils.mergeConstraints(ConstraintArray, ConstraintArray2);
        KnowledgeBaseUtils.createConstraints(ConstraintArrayMerge, ModelKBMerge, IntVariablesMerge);

        // Print variables
        for (MyVariable item : VariablesKBMerge) {
            for (Integer item2 : item.Values) {
                String VarValue = KnowledgeBaseUtils.getVarValueName(item.Name, item2);
                String VarName = item.Name;
                System.out.println(VarName + " = " + VarValue);
            }
        }
        System.out.println("...................................................................");

        // Print constraints
        for (Constraint item : ModelKBMerge.getCstrs()) {

            System.out.println((item.getName()) + " --> " + item.toString());
        }

        System.out.println("...................................................................");

        // Print solutions

        Solver sMerge = ModelKBMerge.getSolver();

        if (sMerge.solve()) {
            while (sMerge.solve()) {
                System.out.println(sMerge.getMeasures().getSolutionCount());
                for (IntVar item : IntVariablesMerge) {

                    String VarValue = KnowledgeBaseUtils.getVarValueName(item.getName(), item.getValue());
                    String VarName = item.getName();
                    System.out.println(VarName + " = " + VarValue);
                }
            }
        } else {
            System.out.println("No solution has been found!");
        }

        System.out.println("...................................................................");
        System.out.println("Find conflict set:");

        // Remove constraints from model
        for (Constraint item : ModelKBMerge.getCstrs()) {
            ModelKBMerge.unpost(item);
        }

        // Convert constraints to list as input for QuickXPlain
        KnowledgeBaseUtils.createConstraints(ConstraintArray, ModelKBMerge, IntVariablesMerge);
        Constraint[] cKB1 = ModelKBMerge.getCstrs();
        List<Constraint> cKB1List = new ArrayList<>();
        cKB1List = ConstraintUtils.arrayToList(cKB1);
        for (Constraint item : ModelKBMerge.getCstrs()) {
            ModelKBMerge.unpost(item);
        }

        KnowledgeBaseUtils.createConstraints(ConstraintArray2, ModelKBMerge, IntVariablesMerge);
        Constraint[] cKB2 = ModelKBMerge.getCstrs();
        List<Constraint> cKB2List = new ArrayList<>();
        cKB2List = ConstraintUtils.arrayToList(cKB2);
        for (Constraint item : ModelKBMerge.getCstrs()) {
            ModelKBMerge.unpost(item);
        }

        // Use QuickXplain to find conflict set
        Model QuickXPlainModel = new Model();

        // Add variables
        for (MyVariable item : VariablesKBMerge) {
            IntVar ivars = QuickXPlainModel.intVar(item.Name, item.Values);
        }

        // KnowledgeBaseUtils.createConstraints(ConstraintArrayMerge, QuickXPlainModel, IntVariablesMerge);
        for (Constraint item : cKB1List) {
            QuickXPlainModel.post(item);
        }
        for (Constraint item : cKB2List) {
            QuickXPlainModel.post(item);
        }
        /*
        for (Constraint item : QuickXPlainModel.getCstrs()) {

            System.out.println((item.getName()) + " --> " + item.toString());
        }
        */
        List<Constraint> conflictSet = new ArrayList<Constraint>();
        conflictSet = QuickXplain.quickXPlain(cKB1List, cKB2List, QuickXPlainModel);


        if (conflictSet.isEmpty()) {
            System.out.println("No conflict set could be found...");
            /*
            Solver QuickSolver = QuickXPlainModel.getSolver();
            while (QuickSolver.solve()) {
                System.out.println(QuickSolver.getMeasures().getSolutionCount());
                for (IntVar item : IntVariablesMerge) {

                    String VarValue = KnowledgeBaseUtils.getVarValueName(item.getName(), item.getValue());
                    String VarName = item.getName();
                    System.out.println(VarName + " = " + VarValue);
                }
            }
            */
        }
    else
            {
                System.out.println("> One minimal conflict set could be found using QuickXplain: CS1= " + conflictSet);
            }

    }
}

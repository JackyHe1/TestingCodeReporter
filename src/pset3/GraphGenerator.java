package pset3;

import org.apache.bcel.*;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.lang.invoke.MethodHandle;
import java.util.*;

public class GraphGenerator {
    public CFG createCFG(String className) throws ClassNotFoundException {
        CFG cfg = new CFG();
        JavaClass jc = Repository.lookupClass(className);
        ClassGen cg = new ClassGen(jc);
        ConstantPoolGen cpg = cg.getConstantPool();

        int exitPos = -1;
        for (Method m : cg.getMethods()) {
            LineNumberTable lineNumberTable = m.getLineNumberTable();
            MethodGen mg = new MethodGen(m, cg.getClassName(), cpg);
            InstructionList il = mg.getInstructionList();
            InstructionHandle[] handles = il.getInstructionHandles();

            for(InstructionHandle ih : handles) {
                int position = ih.getPosition();
                int sourcePosition = lineNumberTable.getSourceLine(position);

                cfg.addNode(position, m, jc);
                Instruction inst = ih.getInstruction();
                // your code goes here

                //if branch must add next and also target next; while go to only add target instruction
                // call cfg.addEdge(int p1, Method m1, JavaClass c1, int p2, Method m2, JavaClass c2)
                InstructionHandle nextIH = ih.getNext();

                if(inst instanceof ReturnInstruction) {
                    cfg.addEdge(position, exitPos, m, jc);  //-1 is exitPos

                    cfg.addIntEdge(sourcePosition, exitPos);
                }
                else if (inst instanceof BranchInstruction) {
                    cfg.addEdge(position, nextIH.getPosition(), m, jc);
                    cfg.addEdge(position, ((BranchInstruction) inst).getTarget().getPosition(), m, jc);
                    cfg.addIntEdge(sourcePosition, lineNumberTable.getSourceLine(nextIH.getPosition()));
                    cfg.addIntEdge(sourcePosition, lineNumberTable.getSourceLine(((BranchInstruction) inst).getTarget().getPosition()));
                }
                else if(inst instanceof GotoInstruction) {
                    cfg.addEdge(position, ((BranchInstruction) inst).getTarget().getPosition(), m, jc);
                    cfg.addIntEdge(sourcePosition, lineNumberTable.getSourceLine(((BranchInstruction) inst).getTarget().getPosition()));
                }
                else {
                    cfg.addEdge(position, nextIH.getPosition(), m, jc);
                    cfg.addIntEdge(sourcePosition, lineNumberTable.getSourceLine(nextIH.getPosition()));
                }
            }
        }

        return cfg;
    }

    public CFG createCFGWithMethodInvocation(String className) throws ClassNotFoundException {
        // your code goes here
        CFG cfg = new CFG();
        JavaClass jc = Repository.lookupClass(className);
        ClassGen cg = new ClassGen(jc);
        ConstantPoolGen cpg = cg.getConstantPool();

        int exitPos = -1;
        for (Method m : cg.getMethods()) {
            MethodGen mg = new MethodGen(m, cg.getClassName(), cpg);
            InstructionList il = mg.getInstructionList();
            InstructionHandle[] handles = il.getInstructionHandles();
            for (InstructionHandle ih : handles) {
                int position = ih.getPosition();
                cfg.addNode(position, m, jc);
                Instruction inst = ih.getInstruction();

                //if branch must add next and also target next; while go to only add target instruction
                // call cfg.addEdge(int p1, Method m1, JavaClass c1, int p2, Method m2, JavaClass c2)
                InstructionHandle nextIH = ih.getNext();
                if(inst instanceof ReturnInstruction) {
                    cfg.addEdge(position, exitPos, m, jc);  //-1 is exitPos
                }
                else if (inst instanceof BranchInstruction) {
                    cfg.addEdge(position, nextIH.getPosition(), m, jc);
                    cfg.addEdge(position, ((BranchInstruction) inst).getTarget().getPosition(), m, jc);
                }
                else if(inst instanceof GotoInstruction) {
                    cfg.addEdge(position, ((BranchInstruction) inst).getTarget().getPosition(), m, jc);
                }
                else if(inst instanceof InvokeInstruction) {
                    //System.out.println("this is a invoke method = " + inst);
                    //still need to connect with invoked method tail with
                    //System.out.println("caller= " + m.getName() + " , called func=" + ((InvokeInstruction) inst).getMethodName(cpg));

                    // we should also add edges between caller and callee
                    Method callee = cg.getMethodAt(((InvokeInstruction) inst).getIndex());
                    MethodGen calleeGen = new MethodGen(callee, cg.getClassName(), cpg);
                    InstructionList ilee = calleeGen.getInstructionList();
                    InstructionHandle[] handlesCallee = ilee.getInstructionHandles();
                    cfg.addEdge(position, m, jc, handlesCallee[0].getPosition(), callee, jc);  //connect current caller position with the first position of callee
                    for(InstructionHandle handleCallee : handlesCallee) {
                        if(handleCallee.getInstruction() instanceof ReturnInstruction) {
                            cfg.addEdge(handleCallee.getPosition(), exitPos, callee, jc);  //-1 is exitPos
                            cfg.addEdge(exitPos, callee, jc, ih.getNext().getPosition(), m, jc);  //connect current caller position with the first position of callee
                        }
                    }
                }
//                else if(nextIH == null) {
//                    cfg.addEdge(position, exitPos, m, jc);
//                }
                else {
                    cfg.addEdge(position, nextIH.getPosition(), m, jc);
                }
            }
        }
//        System.out.println(cfg);
        return cfg;
    }

//    public static void main(String[] a) throws ClassNotFoundException {
//        GraphGenerator gg = new GraphGenerator();
//
//        System.out.println("------------------------  cfg  ------------------------");
//        CFG cfg = gg.createCFG("pset3.C"); // example invocation of createCFG
//        System.out.println(cfg);
//
//        System.out.println("------------------------  cfgWithMethodInvocation  ------------------------");
//        CFG cfgWithMethodInvocation = gg.createCFGWithMethodInvocation("pset3.D"); // example invocation of createCFGWithMethodInovcation
//        System.out.println(cfgWithMethodInvocation);
//
//        System.out.println("------------------------  Control-flow Graph Reachability  ------------------------");
//        //add some tests for Control-flow Graph Reachability
//        System.out.println("isReachable(\"main\", \"pset3.D\", \"foo\", \"pset3.D\") == "
//                + cfgWithMethodInvocation.isReachable("main", "pset3.D", "foo", "pset3.D"));
//        System.out.println("isReachable(\"main\", \"pset3.D\", \"bar\", \"pset3.D\") == "
//                + cfgWithMethodInvocation.isReachable("main", "pset3.D", "bar", "pset3.D"));
//
//        // we do not have barrrrrrrrrrrr method, so this should be false
//        System.out.println("isReachable(\"main\", \"pset3.D\", \"barrrrrrrrrrrr\", \"pset3.D\") == "
//                + cfgWithMethodInvocation.isReachable("main", "pset3.D", "barrrrrrrrrrrr", "pset3.D"));
//
//        // even though we do have bar method, but we do not have pset3.XXXXXX, so this should be false
//        System.out.println("isReachable(\"main\", \"pset3.D\", \"barrrrrrrrrrrr\", \"pset3.D\") == "
//                + cfgWithMethodInvocation.isReachable("main", "pset3.D", "bar", "pset3.XXXXXX"));
//    }

    public static void main(String[] a) throws ClassNotFoundException {
        GraphGenerator gg = new GraphGenerator();

        System.out.println("------------------------  cfg  ------------------------");
        CFG cfg = gg.createCFG("pset3.C"); // example invocation of createCFG

        //EdgeReporter edgeReporter = new EdgeReporter(cfg.edges);
        cfg.addIntEdge(4, 5);
        cfg.addIntEdge(4, 8);
        cfg.addIntEdge(8, -1);
        System.out.println("edge= " + cfg.edges);
        System.out.println("edge= " + cfg.intEdges);

        cfg.generateLineNumbersList();
        cfg.generateAllCompletePaths();
        cfg.generateDifferentEdges();
    }
}
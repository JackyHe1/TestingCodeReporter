package pset3;
import java.util.*;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

public class CFG {
    Set<Node> nodes = new HashSet<Node>();
    Map<Node, Set<Node>> edges = new HashMap<Node, Set<Node>>();
    Map<Integer, Set<Integer>> intEdges = new HashMap<>();

    public static class Node {
        int position;
        Method method;
        JavaClass clazz;
        Node(int p, Method m, JavaClass c) {
            position = p;
            method = m;
            clazz = c;
        }
        public Method getMethod() {
            return method;
        }
        public JavaClass getClazz() {
            return clazz;
        }
        public boolean equals(Object o) {
            if (!(o instanceof Node)) return false;
            Node n = (Node)o;
            return (position == n.position) && method.equals(n.method) && clazz.equals(n.clazz);
        }
        public int hashCode() {
            return position + method.hashCode() + clazz.hashCode();
        }
        public String toString() {
            return clazz.getClassName() + '.' + method.getName() + method.getSignature() + ": " + position;
        }
    }

    public void addNode(int p, Method m, JavaClass c) {
        addNode(new Node(p, m, c));
    }

    private void addNode(Node n) {
        nodes.add(n);
        Set<Node> nbrs = edges.get(n);
        if (nbrs == null) {
            nbrs = new HashSet<Node>();
            edges.put(n, nbrs);
        }
    }

    public void addIntEdge(int from, int to) {
        if(from == to) {
            return;
        }
        intEdges.putIfAbsent(from, new HashSet<>());
        intEdges.get(from).add(to);
    }

    public void addEdge(int p1, Method m1, JavaClass c1, int p2, Method m2, JavaClass c2) {
        Node n1 = new Node(p1, m1, c1);
        Node n2 = new Node(p2, m2, c2);
        addNode(n1);
        addNode(n2);
        Set<Node> nbrs = edges.get(n1);
        nbrs.add(n2);
        edges.put(n1, nbrs);
    }
    public void addEdge(int p1, int p2, Method m, JavaClass c) {
        addEdge(p1, m, c, p2, m, c);
    }
    public String toString() {
        return nodes.size() + " nodes\n" + "nodes: " + nodes + '\n' + "edges: " + edges;
    }
    public boolean isReachable(String methodFrom, String clazzFrom,
                               String methodTo, String clazzTo) {
        // you will implement this method in Question 2.2
        JavaClass jcFrom = null;
        try {
            jcFrom = Repository.lookupClass(clazzFrom);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ClassGen cgFrom = new ClassGen(jcFrom);
        ConstantPoolGen cpgFrom = cgFrom.getConstantPool();
        // find method from
        Method mFrom = null;
        for(Method method : cgFrom.getMethods()) {
            if(method.getName().equals(methodFrom)) {
                mFrom = method;
                break;
            }
        }

        // find the first instruction of method from
        MethodGen mgFrom = new MethodGen(mFrom, cgFrom.getClassName(), cpgFrom);
        InstructionHandle firstMFromIH = mgFrom.getInstructionList().getInstructionHandles()[0];

        // find the first position of methodFrom
        int firstPosMFrom = firstMFromIH.getPosition();

        // find dfs start node
        Node startNode = new Node(firstPosMFrom, mFrom, jcFrom);
        return helper(edges, startNode, new HashSet<Node>(), clazzTo, methodTo);
    }

    public boolean helper(Map<Node, Set<Node>> edges, Node curNode, Set<Node> visited, String clazzTo, String mTo) {
        if(visited.contains(curNode)) return false;
        if(curNode.clazz.getClassName().equals(clazzTo) && curNode.method.getName().equals(mTo)) {
            return true;
        }

        visited.add(curNode);

        for(Node next : edges.get(curNode)) {
            if(helper(edges, next, visited, clazzTo, mTo)) {
                return true;
            }
        }

        return false;
    }

}
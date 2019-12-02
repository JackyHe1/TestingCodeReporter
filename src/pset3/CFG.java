package pset3;
import java.io.PrintWriter;
import java.util.*;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

public class CFG {
    Set<Node> nodes = new HashSet<Node>();
    Map<Node, Set<Node>> edgesMap = new HashMap<Node, Set<Node>>();

    //For testing project
    Map<Integer, Set<Integer>> intEdgesMap = new HashMap<>();
    List<Integer> codeLineNumbersList = new ArrayList<>();


    List<String> completePaths = new ArrayList<>();
    List<String> edges = new ArrayList<>();  //len 1, 2 nodes
    List<String> edgePairs = new ArrayList<>(); //len 2, 3 nodes
    List<String> simplePaths = new ArrayList<>();
    List<String> primePaths = new ArrayList<>();

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
        Set<Node> nbrs = edgesMap.get(n);
        if (nbrs == null) {
            nbrs = new HashSet<Node>();
            edgesMap.put(n, nbrs);
        }
    }

    public void addIntEdge(int from, int to) {
        if(from == to) {
            return;
        }
        intEdgesMap.putIfAbsent(from, new HashSet<>());
        intEdgesMap.get(from).add(to);
    }

    public void addEdge(int p1, Method m1, JavaClass c1, int p2, Method m2, JavaClass c2) {
        Node n1 = new Node(p1, m1, c1);
        Node n2 = new Node(p2, m2, c2);
        addNode(n1);
        addNode(n2);
        Set<Node> nbrs = edgesMap.get(n1);
        nbrs.add(n2);
        edgesMap.put(n1, nbrs);
    }
    public void addEdge(int p1, int p2, Method m, JavaClass c) {
        addEdge(p1, m, c, p2, m, c);
    }
    public String toString() {
        return nodes.size() + " nodes\n" + "nodes: " + nodes + '\n' + "edges: " + edgesMap;
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
        return helper(edgesMap, startNode, new HashSet<Node>(), clazzTo, methodTo);
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

    public void generateLineNumbersList() {
        Set<Integer> codeLineNumbersSet = new HashSet<>();
        for(int lineNumber : intEdgesMap.keySet()) {
            codeLineNumbersSet.add(lineNumber);
            codeLineNumbersSet.addAll(intEdgesMap.get(lineNumber));
        }
        codeLineNumbersList.addAll(codeLineNumbersSet);
        Collections.sort(codeLineNumbersList);

        System.out.println("------------------------  code number list  ------------------------");
        System.out.println(codeLineNumbersList.toString());
    }

    public void generateAllCompletePaths() {
        if(codeLineNumbersList == null || codeLineNumbersList.size() <= 1) {
            return;
        }

        Queue<String> paths = new LinkedList<>();
        Queue<Integer> nodes = new LinkedList<>();

        // find firstNode to start
        int firstNode = -1;
        for(int node : codeLineNumbersList) {
            if(node == -1 || intEdgesMap.get(node).size() == 1 && intEdgesMap.get(node).iterator().next() == -1) {
                continue;
            }
            firstNode = node;
            break;
        }

        paths.offer(firstNode + "");
        nodes.offer(firstNode);

        while(!nodes.isEmpty()) {
            int curNode = nodes.poll();
            String curPath = paths.poll();
            if(curNode == -1) {
                completePaths.add(curPath);
                continue;
            }

            for(int next : intEdgesMap.get(curNode)) {
                nodes.offer(next);
                paths.offer(curPath + "," + next);
            }
        }

        System.out.println("------------------------  all complete paths  ------------------------");
        for(String path : completePaths) {
            System.out.println(path);
        }
    }

    public void generateDifferentEdges() {
        //  edge
        edges = generateEdgesWithEdgeLen(2);
        System.out.println("------------------------  edges  ------------------------");
        for(String path : edges) {
            System.out.println(path);
        }

        //  edge pairs
        edgePairs = generateEdgesWithEdgeLen(3);
        System.out.println("------------------------  edges pairs  ------------------------");
        for(String path : edgePairs) {
            System.out.println(path);
        }

        // simple paths
        int maxPathLen = 0;
        for(String completePath : completePaths) {
            maxPathLen = Math.max(maxPathLen, completePath.length());
        }

        for(int len = 2; len <= maxPathLen; len++) {
            simplePaths.addAll(generateEdgesWithEdgeLen(len));
        }

        System.out.println("------------------------  simple paths  ------------------------");
        for(String path : simplePaths) {
            System.out.println(path);
        }

        // prime paths
        primePaths = generatePrimePaths();
        System.out.println("------------------------  prime paths  ------------------------");
        for(String path : primePaths) {
            System.out.println(path);
        }
    }

    public List<String> generateEdgesWithEdgeLen(int edgeLen) {
        List<String> paths = new ArrayList<>();
        Set<String> pathSet = new HashSet<>();
        for(String completePath : completePaths) {
            for(int i = 0; i + 2 * edgeLen - 1 <= completePath.length() - 3; i += 2) { //last one is -1, jump
                String path = completePath.substring(i, i + 2 * edgeLen - 1);
                if(pathSet.add(path)) {
                    paths.add(path);
                }
            }
        }
        return  paths;
    }

    public List<String> generatePrimePaths() {
        List<String> primePaths = new ArrayList<>();
        for(String simplePath : simplePaths) {
            boolean notSubPathOfOtherPaths = true;
            for(String simplePathComp : simplePaths) {
                if(simplePath.length() < simplePathComp.length() && simplePathComp.indexOf(simplePath) != -1) {
                    notSubPathOfOtherPaths = false;
                    break;
                }
            }
            if(notSubPathOfOtherPaths) {
                primePaths.add(simplePath);
            }
        }
        return primePaths;
    }

    public void writeAllPathsToFiles() {
        writePathsToFile(edges, "edges.txt");
        writePathsToFile(edgePairs, "edgePairs.txt");
        writePathsToFile(simplePaths, "simplePaths.txt");
        writePathsToFile(primePaths, "primePaths.txt");
        writePathsToFile(completePaths, "completePaths.txt");
    }

    public void writePathsToFile(List<String> paths, String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            for(String path : paths) {
                out.println(path);
            }
        }
        catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
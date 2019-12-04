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

    boolean noCompletePaths = false;
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
        codeLineNumbersList.remove(0);  //remove -1
        codeLineNumbersList.remove(0);  //remove node which call class init

        System.out.println("------------------------  code number list  ------------------------");
        System.out.println(codeLineNumbersList.toString());
    }

    public List<String> convertIntListToStringListAndRemoveLastOne() {
        String nodes = "";
        for(int node : codeLineNumbersList) {
            nodes += "," + node;
        }
        nodes = nodes.substring(1);

        System.out.println("------------------------  nodes  ------------------------");
        System.out.println(nodes);

        List<String> ret = new ArrayList<>();
        ret.add(nodes);
        return ret;
    }

    public void generateAllCompletePaths() {
        if(codeLineNumbersList == null || codeLineNumbersList.size() <= 1) {
            return;
        }

        Queue<String> paths = new LinkedList<>();
        Queue<Integer> nodes = new LinkedList<>();
        Map<Integer, Integer> countMap = new HashMap<>();

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
        countMap.put(firstNode, 1);

        while(!nodes.isEmpty()) {
            int curNode = nodes.poll();
            String curPath = paths.poll();
            if(curNode == -1) {
                completePaths.add(curPath.substring(0, curPath.length() - 3));
                continue;
            }

            for(int next : intEdgesMap.get(curNode)) {
                countMap.put(next, countMap.getOrDefault(next, 0) + 1);
                if(countMap.get(next) == 3) {  //handle for loop, we can have 4,6 or 454, but not 45454 in complete path to generate other paths
                    noCompletePaths = true;
                    continue;
                }

                nodes.offer(next);
                paths.offer(curPath + "," + next);
            }
        }

        System.out.println("------------------------  all complete paths  ------------------------");
        if(!noCompletePaths) {
            for (String path : completePaths) {
                System.out.println(path);
            }
        }
    }

    public void generateDifferentEdges() {
        //  edge
        edges = generateEdgesWithEdgeLen(1);
        System.out.println("------------------------  edges  ------------------------");
        for(String path : edges) {
            System.out.println(path);
        }

        //  edge pairs
        edgePairs = generateEdgesWithEdgeLen(2);
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
            String[] nums = completePath.split(",");
            for(int i = 0; i < nums.length - edgeLen; i ++) { //last one is -1, jump
                String path = "";
                int count = edgeLen + 1;
                int idx = i;
                while(count > 0) {
                    path += nums[idx++] + ",";
                    count--;
                }
                if(pathSet.add(path)) {
                    paths.add(path.substring(0, path.length() - 1));
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
        writePathsToFile(convertIntListToStringListAndRemoveLastOne(), "out/nodes.txt");
        writePathsToFile(edges, "out/edges.txt");
        writePathsToFile(edgePairs, "out/edgePairs.txt");
        writePathsToFile(simplePaths, "out/simplePaths.txt");
        writePathsToFile(primePaths, "out/primePaths.txt");
        writePathsToFile(noCompletePaths ? new ArrayList<>() : completePaths, "out/completePaths.txt");
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
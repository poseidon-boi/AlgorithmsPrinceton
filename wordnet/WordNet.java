import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

public class WordNet {

    private TreeMap<String, Integer> synsetsST;
    private String[] synsetsList;
    private Digraph hypernymsWordNet;

    /**
     * @param synsets   File containing the synsets
     * @param hypernyms File containing the hypernyms
     */
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Null argument");

        In hypernymsInputCounter = new In(hypernyms);
        In hypernymsInput = new In(hypernyms);
        In synsetsInput = new In(synsets);
        int vertices = 0;
        // System.out.println("Reached counter");
        while (!hypernymsInputCounter.isEmpty()) {
            hypernymsInputCounter.readLine();
            vertices++;
        }
        hypernymsWordNet = new Digraph(vertices);
        synsetsList = new String[vertices];
        // vertices--;

        // System.out.println("Vertices: " + vertices);

        boolean hasOrphan = false;
        int rootVertex = 0;

        for (int i = 0; i < vertices; i++) {
            String inputLine = hypernymsInput.readLine();
            String[] parts = inputLine.split(",");
            if (parts.length < 1) {
                // if (parts[0].equals(""))
                //     break;
                throw new IllegalArgumentException(
                        "Hypernyms line " + i + " has no parts");
            }
            if (parts.length == 1) {
                if (!hasOrphan) {
                    hasOrphan = true;
                    // System.out.println("Orphan: " + i);
                    rootVertex = i;
                }
                else
                    throw new IllegalArgumentException("More than one root, not a rooted DAG");
            }
            int v1 = Integer.parseInt(parts[0]);
            if (v1 != i)
                throw new IllegalArgumentException("Invalid line number");
            for (int j = 1; j < parts.length; j++)
                hypernymsWordNet.addEdge(v1, Integer.parseInt(parts[j]));
        }

        boolean hasCycle = checkCycles(hypernymsWordNet.reverse(), hypernymsWordNet.V(),
                                       new boolean[hypernymsWordNet.V()],
                                       new Stack<Integer>(), rootVertex);

        if (hasCycle)
            throw new IllegalArgumentException("Cycle detected, not a DAG");

        synsetsST = new TreeMap<>();

        for (int i = 0; i < vertices; i++) {
            String inputLine = synsetsInput.readLine();
            String[] parts = inputLine.split(",");
            if (parts.length < 2)
                throw new IllegalArgumentException(
                        "Synsets line " + i + " has " + parts.length + " parts");
            int index;
            try {
                index = Integer.parseInt(parts[0]);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("First element should be a number");
            }
            if (index != i)
                throw new IllegalArgumentException("Invalid line number");
            synsetsList[i] = parts[1];
            synsetsST.put(parts[1], i);
        }
    }

    private boolean checkCycles(Digraph digraph, int vertices, boolean[] visited,
                                Stack<Integer> ancestors, int currentVertex) {

        if (currentVertex >= vertices)
            return true;
        visited[currentVertex] = true;
        ancestors.push(currentVertex);
        boolean foundCycle;
        for (int adj : digraph.adj(currentVertex)) {
            if (visited[adj]) {
                for (int ancestor : ancestors) {
                    if (ancestor == adj)
                        return true;
                }
            }
            foundCycle = checkCycles(digraph, vertices, visited, ancestors, adj);
            if (foundCycle)
                return true;
        }
        int poppedVertex = ancestors.pop();
        assert poppedVertex == currentVertex;
        return false;
    }

    /**
     * @return All WordNet nouns
     */
    public Iterable<String> nouns() {
        ArrayList<String> nouns = new ArrayList<>();
        Collections.addAll(nouns, synsetsList);
        return nouns;
    }

    /**
     * Checks whether the given word is a WordNet noun
     *
     * @param word The word to check
     * @return true if a WordNet noun, false otherwise
     */
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Null argument");
        return synsetsST.containsKey(word);
    }

    private class Node implements Comparable<Node> {
        int key;
        int distance;

        Node(int k, int d) {
            key = k;
            distance = d;
        }

        /**
         * @param obj The object to check against
         * @return true if the key is the same
         */
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (this == obj)
                return true;
            if (obj.getClass() != this.getClass())
                return false;
            Node that = (Node) obj;
            return this.key == that.key;
        }

        /**
         * @param that The Node to compare to
         * @return Comparison of the key of the nodes
         */
        public int compareTo(Node that) {
            if (that == null)
                throw new NullPointerException("Null argument");
            return Integer.compare(this.key, that.key);
        }

        public int hashCode() {
            return key;
        }
    }

    /**
     * Gets the SAP and distance between two nouns
     *
     * @param nounA
     * @param nounB
     * @return A Node with the common ancestor id and the distance between nounA and nounB
     */
    private Node getSAPAndDistance(String nounA, String nounB) {
        assert nounA != null && nounB != null;
        assert isNoun(nounA) && isNoun(nounB);
        LinkedList<Node> keysA = new LinkedList<>(), keysB = new LinkedList<>();
        keysA.addLast(new Node(synsetsST.get(nounA), 0));
        keysB.addLast(new Node(synsetsST.get(nounB), 0));
        TreeSet<Node> processedKeysA = new TreeSet<>(Node::compareTo),
                processedKeysB = new TreeSet<>(Node::compareTo);
        while (true) {
            assert !(keysA.isEmpty() && keysB.isEmpty());
            if (!keysA.isEmpty()) {
                Node currentNode = keysA.getFirst();
                int keyA = currentNode.key;
                processedKeysA.add(currentNode);
                for (int adjA : hypernymsWordNet.adj(keyA)) {
                    Node nodeA = new Node(adjA, currentNode.distance + 1);
                    if (keysA.contains(nodeA)) {
                        continue;
                    }
                    if (processedKeysB.contains(nodeA)) {
                        int distanceOther = 0;
                        for (Node node : processedKeysB) {
                            if (node.equals(nodeA)) {
                                distanceOther = node.distance;
                                break;
                            }
                        }
                        nodeA.distance += distanceOther;
                        return nodeA;
                    }
                    keysA.addLast(new Node(adjA, currentNode.distance + 1));
                    processedKeysA.add(nodeA);
                }
                keysA.removeFirst();
            }
            if (!keysB.isEmpty()) {
                Node currentNode = keysB.getFirst();
                int keyB = currentNode.key;
                processedKeysB.add(currentNode);
                for (int adjB : hypernymsWordNet.adj(keyB)) {
                    Node nodeB = new Node(adjB, currentNode.distance + 1);
                    if (keysB.contains(nodeB)) {
                        continue;
                    }
                    if (processedKeysA.contains(nodeB)) {
                        int distanceOther = 0;
                        for (Node node : processedKeysA) {
                            if (node.equals(nodeB)) {
                                distanceOther = node.distance;
                                break;
                            }
                        }
                        nodeB.distance += distanceOther;
                        return nodeB;
                    }
                    keysB.addLast(new Node(adjB, currentNode.distance + 1));
                    processedKeysB.add(nodeB);
                }
                keysB.removeFirst();
            }
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Null argument");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not a wordnet noun");
        Node sapNode = getSAPAndDistance(nounA, nounB);
        assert sapNode != null;
        return sapNode.distance;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Null argument");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not a wordnet noun");
        Node sapNode = getSAPAndDistance(nounA, nounB);
        assert sapNode != null;
        return synsetsList[sapNode.key];
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.isNoun("22-karat_gold"));
        System.out.println(wordNet.isNoun("h"));
        System.out.println(
                wordNet.sap("1750s", "1900s"));
        System.out.println(
                wordNet.distance("1750s", "1900s"));
    }
}

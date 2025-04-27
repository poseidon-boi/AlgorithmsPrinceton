import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.TreeSet;

public class SAP {

    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Argument is null");
        int size = G.V();
        digraph = new Digraph(size);
        for (int i = 0; i < size; i++) {
            for (int adjacentVertex : G.adj(i))
                digraph.addEdge(i, adjacentVertex);
        }
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

    private Node getSAPAndDistance(int v, int w) {
        LinkedList<Node> keysV = new LinkedList<>(), keysW = new LinkedList<>();
        keysV.addLast(new Node(v, 0));
        keysW.addLast(new Node(w, 0));
        TreeSet<Node> processedKeysV = new TreeSet<>(Node::compareTo),
                processedKeysW = new TreeSet<>(Node::compareTo);
        while (true) {
            assert !(keysV.isEmpty() && keysW.isEmpty());
            if (!keysV.isEmpty()) {
                Node currentNode = keysV.getFirst();
                int keyV = currentNode.key;
                processedKeysV.add(currentNode);
                for (int adjV : digraph.adj(keyV)) {
                    Node nodeV = new Node(adjV, currentNode.distance + 1);
                    if (keysV.contains(nodeV)) {
                        continue;
                    }
                    if (processedKeysW.contains(nodeV)) {
                        int distanceOther = 0;
                        for (Node node : processedKeysW) {
                            if (node.equals(nodeV)) {
                                distanceOther = node.distance;
                                break;
                            }
                        }
                        nodeV.distance += distanceOther;
                        return nodeV;
                    }
                    keysV.addLast(new Node(adjV, currentNode.distance + 1));
                    processedKeysV.add(nodeV);
                }
                keysV.removeFirst();
            }
            if (!keysW.isEmpty()) {
                Node currentNode = keysW.getFirst();
                int keyW = currentNode.key;
                processedKeysW.add(currentNode);
                for (int adjW : digraph.adj(keyW)) {
                    Node nodeW = new Node(adjW, currentNode.distance + 1);
                    if (keysW.contains(nodeW)) {
                        continue;
                    }
                    if (processedKeysV.contains(nodeW)) {
                        int distanceOther = 0;
                        for (Node node : processedKeysV) {
                            if (node.equals(nodeW)) {
                                distanceOther = node.distance;
                                break;
                            }
                        }
                        nodeW.distance += distanceOther;
                        return nodeW;
                    }
                    keysW.addLast(new Node(adjW, currentNode.distance + 1));
                    processedKeysW.add(nodeW);
                }
                keysW.removeFirst();
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int size = digraph.V();
        if (v >= size || w >= size || v < 0 || w < 0)
            throw new IllegalArgumentException("Vertex not in graph");

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int size = digraph.V();
        if (v >= size || w >= size || v < 0 || w < 0)
            throw new IllegalArgumentException("Vertex not in graph");
        LinkedList<Integer> keysV = new LinkedList<>(), keysW = new LinkedList<>();

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Argument is null");

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Argument is null");
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

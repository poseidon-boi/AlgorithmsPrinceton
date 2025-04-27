import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int moves = 0;
    private boolean solvable = true;
    private Stack<Board> solution = new Stack<Board>();

    public Solver(Board initial) {

        if (initial == null)
            throw new IllegalArgumentException("No board passed");

        boolean initialSolved = false, twinSolved = false;
        Board twinBoard = initial.twin();
        MinPQ<Node> initialQueue = new MinPQ<>(), twinQueue = new MinPQ<>();
        Node initial1 = new Node(initial, null, 0);
        Node twin1 = new Node(twinBoard, null, 0);
        initialQueue.insert(initial1);
        twinQueue.insert(twin1);

        if (initial.isGoal()) {
            initialSolved = true;
            solution.push(initial);
        }

        while (!initialSolved && !twinSolved) {

            Node currentTwin = twinQueue.delMin();
            if (currentTwin.current.isGoal()) {
                twinSolved = true;
                continue;
            }
            Node currentNode = initialQueue.delMin();
            if (currentNode.current.isGoal()) {
                initialSolved = true;
                solution.push(currentNode.current);
                moves = currentNode.nodeMoves;
                while (currentNode.previousNode != null) {
                    solution.push(currentNode.previousNode.current);
                    currentNode = currentNode.previousNode;
                }
                continue;
            }
            for (Board board : currentNode.current.neighbors()) {
                Node node = new Node(board, currentNode, currentNode.nodeMoves + 1);
                if (currentNode.previousNode == null) {
                    initialQueue.insert(node);
                    continue;
                }
                if (!node.current.equals(currentNode.previousNode.current))
                    initialQueue.insert(node);
            }
            for (Board board : currentTwin.current.neighbors()) {
                Node node = new Node(board, currentTwin, currentTwin.nodeMoves + 1);
                if (currentTwin.previousNode == null) {
                    twinQueue.insert(node);
                    continue;
                }
                if (!node.current.equals(currentTwin.previousNode.current))
                    twinQueue.insert(node);
            }
        }
        if (twinSolved) {
            solvable = false;
            moves = -1;
            solution = null;
        }
    }

    private class Node implements Comparable<Node> {
        Node previousNode;
        Board current;
        int priority;
        int nodeMoves;

        public Node(Board currentBoard, Node previousNode, int moves) {
            current = currentBoard;
            this.previousNode = previousNode;
            priority = currentBoard.manhattan() + moves;
            nodeMoves = moves;
        }

        public int compareTo(Node node) {
            return this.priority - node.priority;
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

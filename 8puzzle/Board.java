import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {

    private int[][] currentBoard;
    private int tile1, tile2;
    private int n;

    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException("Empty board");
        n = tiles.length;
        currentBoard = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                currentBoard[i][j] = tiles[i][j];
        do {
            tile1 = StdRandom.uniformInt(n * n);
        } while (currentBoard[tile1 / n][tile1 % n] == 0);
        do {
            tile2 = StdRandom.uniformInt(n * n);
        } while ((tile2 == tile1) || (currentBoard[tile2 / n][tile2 % n] == 0));
    }

    public String toString() {
        String stringRepresentation = n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                stringRepresentation = stringRepresentation.concat(currentBoard[i][j] + " ");
            stringRepresentation = stringRepresentation.concat("\n");
        }
        return stringRepresentation;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hammingDistance = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (currentBoard[i][j] == 0)
                    continue;
                if (currentBoard[i][j] != n * i + j + 1)
                    hammingDistance++;
            }
        return hammingDistance;
    }

    public int manhattan() {
        int manhattanDistance = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (currentBoard[i][j] == 0)
                    continue;
                if (currentBoard[i][j] != n * i + j + 1) {
                    manhattanDistance += Math.abs((currentBoard[i][j] - 1) / n - i) + Math.abs(
                            ((currentBoard[i][j] - 1) % n) - j);
                }
            }
        return manhattanDistance;
    }

    public boolean isGoal() {
        if (currentBoard[n - 1][n - 1] != 0)
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1)
                    break;
                if (currentBoard[i][j] != n * i + j + 1)
                    return false;
            }
        }
        return true;
    }

    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (this.n != that.n)
            return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.currentBoard[i][j] != that.currentBoard[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborBoards = new ArrayList<Board>();
        int[][][] neighboringBoard = new int[4][n][n];
        int x = 0, y = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (currentBoard[i][j] == 0) {
                    x = i;
                    y = j;
                }
        for (int q = 0; q < 4; q++)
            for (int r = 0; r < n; r++)
                for (int s = 0; s < n; s++)
                    neighboringBoard[q][r][s] = currentBoard[r][s];
        if (x < n - 1) {
            int temp = neighboringBoard[0][x + 1][y];
            neighboringBoard[0][x + 1][y] = 0;
            neighboringBoard[0][x][y] = temp;
            neighborBoards.add(new Board(neighboringBoard[0]));
        }
        if (y < n - 1) {
            int temp = neighboringBoard[1][x][y + 1];
            neighboringBoard[1][x][y + 1] = 0;
            neighboringBoard[1][x][y] = temp;
            neighborBoards.add(new Board(neighboringBoard[1]));
        }
        if (x > 0) {
            int temp = neighboringBoard[2][x - 1][y];
            neighboringBoard[2][x - 1][y] = 0;
            neighboringBoard[2][x][y] = temp;
            neighborBoards.add(new Board(neighboringBoard[2]));
        }
        if (y > 0) {
            int temp = neighboringBoard[3][x][y - 1];
            neighboringBoard[3][x][y - 1] = 0;
            neighboringBoard[3][x][y] = temp;
            neighborBoards.add(new Board(neighboringBoard[3]));
        }
        return neighborBoards;
    }

    public Board twin() {
        int[][] twinBoard = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                twinBoard[i][j] = currentBoard[i][j];
        int temp = twinBoard[tile1 / n][tile1 % n];
        twinBoard[tile1 / n][tile1 % n] = twinBoard[tile2 / n][tile2 % n];
        twinBoard[tile2 / n][tile2 % n] = temp;
        Board twinBoardObject = new Board(twinBoard);
        return twinBoardObject;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Board initial1 = new Board(tiles);
        StdOut.println(initial1.equals(initial));
        Board twinBoard = initial.twin();

        StdOut.println(initial);
        StdOut.println("Twin: ");
        StdOut.println(twinBoard);
        StdOut.println("Hamming = " + initial.hamming());
        StdOut.println("Manhattan = " + initial.manhattan());
        StdOut.println("Neighbors:");
        for (Board board : initial.neighbors())
            StdOut.println(board);
    }
}

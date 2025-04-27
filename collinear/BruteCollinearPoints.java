import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private int numberOfLineSegments = 0;
    private LineSegment[] lines;

    public BruteCollinearPoints(Point[] pointsinput) {


        // Throwing exceptions
        if (pointsinput == null)
            throw new IllegalArgumentException("No point entered");
        int pointslength = pointsinput.length;
        for (int i = 0; i < pointslength; i++) {
            if (pointsinput[i] == null)
                throw new IllegalArgumentException("Null point found");
        }
        Point[] points = new Point[pointslength];
        for (int i = 0; i < pointslength; i++)
            points[i] = pointsinput[i];
        Arrays.sort(points);
        for (int i = 1; i < pointslength; i++) {
            if (points[i].compareTo(points[i - 1]) == 0)
                throw new IllegalArgumentException("Duplicate point found");
        }

        lines = new LineSegment[pointslength];

        for (int p = 0; p < pointslength; p++) {
            for (int q = 0; q < pointslength; q++) {
                if (p == q)
                    continue;
                if (points[p].compareTo(points[q]) < 0)
                    continue;
                double pslopeq = points[p].slopeTo(points[q]);
                for (int r = 0; r < pointslength; r++) {
                    if (q == r)
                        continue;
                    if (points[q].compareTo(points[r]) < 0)
                        continue;
                    if (pslopeq != points[p].slopeTo(points[r]))
                        continue;
                    for (int s = 0; s < pointslength; s++) {
                        if (r == s)
                            continue;
                        if (pslopeq != points[p].slopeTo(points[s]))
                            continue;
                        if (points[r].compareTo(points[s]) > 0)
                            lines[numberOfLineSegments++] = new LineSegment(points[p], points[s]);
                    }

                }
            }
        }
        resize();
    }

    private void resize() {
        if (numberOfLineSegments == 0) {
            lines = null;
            return;
        }
        LineSegment[] temp = new LineSegment[numberOfLineSegments];
        for (int i = 0; i < numberOfLineSegments; i++) {
            temp[i] = lines[i];
        }
        lines = temp;
    }

    public int numberOfSegments() {
        return numberOfLineSegments;
    }

    // Function that returns a defensive copy of the array of line segments
    public LineSegment[] segments() {
        LineSegment[] linesDefensiveCopy = new LineSegment[numberOfLineSegments];
        for (int i = 0; i < numberOfLineSegments; i++)
            linesDefensiveCopy[i] = lines[i];
        return linesDefensiveCopy;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.setPenRadius(0.004);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

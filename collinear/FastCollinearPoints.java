import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private int numberOfLineSegments = 0;
    private LineSegment[] lines;

    public FastCollinearPoints(Point[] pointsinput) {

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

        /* Creating the line segment array, length equal to the number of points
         *  It will be shortened later
         */
        lines = new LineSegment[pointslength / 4];
        int lineslength = lines.length;

        // Finding the line segments for the points
        for (int i = 0; i < pointslength; i++) {

            // The origin is the point with respect to which the rest of the points are sorted
            Point origin = pointsinput[i];
            int numberOfCollinearPoints = 1, current = 1, greatest;

            // Sorting points according to slopes with the origin
            Arrays.sort(points, origin.slopeOrder());

            /*  Inner loop that finds line segments
                q is set to 2 because the first point will always be the same as the origin,
                having slope Double.NEGATIVE_INFINITY, and so is not required, and the second point
                will always be collinear with the first point
             */
            for (int q = 2; q < pointslength; q++) {

                /*  Checks if the slope to the new point is the same as the slope to the
                    previous point, making them collinear
                 */
                if (points[0].slopeTo(points[q]) == points[0].slopeTo(points[current])) {
                    numberOfCollinearPoints++;

                    /*  Handles the edge case of reaching the end of the inner loop,
                        where previously it would skip any line segments discovered at
                        the very end
                     */
                    if (q != pointslength - 1)
                        continue;
                    ++q; /* If not done, number of horizontal line segments is doubled since the
                            penultimate point calculates out to be the smallest */
                }

                /*  Condition entered if the number of collinear points to the origin is more
                    than 3, which means the potential detection of a new line segment
                 */
                if (numberOfCollinearPoints >= 3) {

                    /*  Makes sure the origin is the smallest in the set of collinear points.
                        Being the smallest means being on the bottom or left of the other points.
                        This removes any duplicates
                     */
                    boolean smallest = true;
                    for (int j = current; j < q; j++) {
                        if (points[0].compareTo(points[j]) > 0)
                            smallest = false;
                    }

                    /*  If the origin is the smallest, a new line segment has been found.
                        Now a line segment from the origin to the greatest point is created.
                        Being greatest means that the point is to the top or right of the others.
                     */
                    if (smallest) {
                        greatest = current;
                        for (int j = current + 1; j < q; j++) {
                            if (points[greatest].compareTo(points[j]) < 0)
                                greatest = j;
                        }
                        // Doubles in size if the lines array is too small
                        if (numberOfLineSegments == lineslength) {
                            doubleSize();
                            lineslength = lines.length;
                        }
                        lines[numberOfLineSegments++] = new LineSegment(points[0],
                                                                        points[greatest]);
                    }
                }
                // Resets the current point against which the slopes are measured
                numberOfCollinearPoints = 1;
                current = q;
            }
        }
        // Resizes the line segment array to the correct length, to save memory
        resize();
    }

    // Function that resizes doubles the size of the line segment array if there are too many lines
    private void doubleSize() {
        LineSegment[] temp = new LineSegment[2 * lines.length];
        for (int i = 0; i < numberOfLineSegments; i++) {
            temp[i] = lines[i];
        }
        lines = temp;
    }

    // Function that resizes the line segment array to the required length to save memory
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

    // Function that returns the number of line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println("The number of line segments is " + collinear.numberOfSegments());
        StdDraw.show();
    }
}

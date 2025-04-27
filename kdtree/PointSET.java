import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    /** The red black BST containing the set of points */
    private TreeSet<Point2D> points = new TreeSet<>();

    /**
     * Checks whether the set of points is empty
     *
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /** Returns the number of points in the set */
    public int size() {
        return points.size();
    }

    /**
     * Inserts the given point into the set of points
     *
     * @param p The point to be inserted
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument point is null");
        points.add(p);
    }

    /**
     * Checks whether the given point is contained in the set
     *
     * @param p The point to check for
     * @return true if the set contains the point
     */
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument point is null");
        return points.contains(p);
    }

    /** Draws the set of points to standard draw */
    public void draw() {
        for (Point2D p : points)
            p.draw();
    }

    /**
     * Finds all the points in the given rectangle (including boundary)
     *
     * @param rect The rectangle to search in
     * @return An ArrayList containing the set of points
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Argument rectangle is null");
        ArrayList<Point2D> containedPoints = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p))
                containedPoints.add(p);
        }
        return containedPoints;
    }

    /**
     * Finds the nearest point in the set to the given point
     *
     * @param p The given point
     * @return The nearest point. null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument point is null");
        if (points.isEmpty())
            return null;
        Point2D champion = points.first();
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < champion.distanceSquaredTo(p))
                champion = point;
        }
        return champion;
    }

    public static void main(String[] args) {

    }
}

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    /** The root node of the tree */
    private Node root;
    /** The number of nodes in the tree */
    private int size;

    /** Node of the KdTree */
    private class Node {

        /** The point stored within the node */
        public Point2D point;
        /** The left child of the node */
        public Node left;
        /** The right child of the node */
        public Node right;
        /** Is the node compared x wise or y wise? */
        public boolean xComp;

        /**
         * Constructs a node with point p
         *
         * @param p The given point
         */
        public Node(Point2D p) {
            point = p;
        }

        /**
         * Compares two nodes
         *
         * @param that The node to compare to
         * @return 1 if this > that, 0 if equal, -1 if less
         */
        public int compareTo(Node that) {
            if (xComp)
                return Double.compare(this.point.x(), that.point.x());
            return Double.compare(this.point.y(), that.point.y());
        }
    }

    /**
     * Checks whether the KdTree is empty
     *
     * @return true if the KdTree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Finds the number of points in the KdTree
     *
     * @return The number of points in the KdTree
     */
    public int size() {
        return size;
    }

    /**
     * Inserts p into the KdTree
     *
     * @param p The point to insert
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument point is null");
        Node temp = new Node(p);
        if (root == null) {
            root = temp;
            temp.xComp = true;
            size++;
            return;
        }
        Node current = root;
        for (int i = 0; ; i++) {

            if (current.point.equals(p))
                return;
            if (current.compareTo(temp) > 0) {
                if (current.left == null) {
                    current.left = temp;
                    temp.xComp = !current.xComp;
                    size++;
                    return;
                }
                current = current.left;
                continue;
            }

            if (current.right == null) {
                current.right = temp;
                temp.xComp = !current.xComp;
                size++;
                return;
            }
            current = current.right;
        }
    }

    /**
     * Checks whether p is in the KdTree
     *
     * @param p The point to check for
     * @return true if the point is in the KdTree
     */
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument point is null");
        if (root == null)
            return false;
        Node current = root, temp = new Node(p);
        for (int i = 0; ; i++) {

            if (current.point.equals(p))
                return true;

            if (current.compareTo(temp) > 0) {
                if (current.left == null)
                    return false;
                current = current.left;
                continue;
            }

            if (current.right == null)
                return false;
            current = current.right;
        }
    }

    /** Draws all the points and corresponding lines in the tree to StdDraw */
    public void draw() {
        draw(root, 0, 1, 0, 1);
    }

    /**
     * Draws the given point and the vertical/horizontal line
     *
     * @param node   The node containing the point to draw
     * @param lowerX The lower x limit to draw the line segment from
     * @param upperX The upper x limit to draw the line segment to
     * @param lowerY The lower y limit to draw the line segment from
     * @param upperY The upper y limit to draw the line segment to
     */
    private void draw(Node node, double lowerX, double upperX,
                      double lowerY, double upperY) {

        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        double radius = StdDraw.getPenRadius();
        StdDraw.setPenRadius(radius * 5);
        double x = node.point.x();
        double y = node.point.y();
        StdDraw.point(x, y);
        StdDraw.setPenRadius(radius);
        if (node.xComp) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x, lowerY, x, upperY);
            draw(node.left, lowerX, x, lowerY, upperY);
            draw(node.right, x, upperX, lowerY, upperY);
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(lowerX, y, upperX, y);
            draw(node.left, lowerX, upperX, lowerY, y);
            draw(node.right, lowerX, upperX, y, upperY);
        }
    }

    /**
     * Finds all the points in rect
     *
     * @param rect The rectangle to search in
     * @return An ArrayList containing all the points in the given rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Argument rectangle is null");
        ArrayList<Point2D> containedPoints = new ArrayList<>();
        if (root == null)
            return containedPoints;
        find(rect, root, containedPoints);
        return containedPoints;
    }

    /**
     * Finds and adds all the points in rect to containedPoints recursively
     *
     * @param rect            The rectangle to search in
     * @param current         The current node
     * @param containedPoints The ArrayList to store the points in rect
     */
    private void find(RectHV rect, Node current,
                      ArrayList<Point2D> containedPoints) {

        if (current == null)
            return;
        double x = current.point.x(), y = current.point.y();
        if (rect.contains(current.point))
            containedPoints.add(current.point);
        if (current.xComp) {
            if (x <= rect.xmax())
                find(rect, current.right, containedPoints);
            if (x >= rect.xmin())
                find(rect, current.left, containedPoints);
        }
        else {
            if (y <= rect.ymax())
                find(rect, current.right, containedPoints);
            if (y >= rect.ymin())
                find(rect, current.left, containedPoints);
        }
    }

    /**
     * Finds the nearest point to p
     *
     * @param p The given point
     * @return The nearest point to p, null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument point is null");
        if (root == null)
            return null;
        RectHV rect = new RectHV(0, 0, 1, 1);
        return nearest(new Node(p), root, rect, root.point);
    }

    /**
     * Recursively finds nearest point to p
     *
     * @param p               The given point
     * @param current         The current node
     * @param rect            Rectangle containing the point in the node
     * @param currentChampion The current closest point to the point
     * @return The closest of the current point and its children
     */
    private Point2D nearest(Node p, Node current, RectHV rect, Point2D currentChampion) {

        Point2D otherChampion = current.point;
        double rectangularDistance;
        RectHV thisRect, otherRect;
        double currentDistance = currentChampion.distanceSquaredTo(p.point);

        if (otherChampion.distanceSquaredTo(p.point) < currentChampion.distanceSquaredTo(p.point)) {
            currentChampion = otherChampion;
            currentDistance = otherChampion.distanceSquaredTo(p.point);
        }

        if (current.compareTo(p) >= 0) {

            if (current.xComp) {
                thisRect = new RectHV(rect.xmin(), rect.ymin(), current.point.x(), rect.ymax());
                otherRect = new RectHV(current.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            else {
                thisRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), current.point.y());
                otherRect = new RectHV(rect.xmin(), current.point.y(), rect.xmax(), rect.ymax());
            }
            rectangularDistance = otherRect.distanceSquaredTo(p.point);

            if (current.left != null)
                otherChampion = nearest(p, current.left, thisRect, currentChampion);
            if (otherChampion.distanceSquaredTo(p.point) < currentDistance) {
                currentChampion = otherChampion;
                currentDistance = otherChampion.distanceSquaredTo(p.point);
            }
            if (current.right != null && currentDistance > rectangularDistance) {
                otherChampion = nearest(p, current.right, otherRect, currentChampion);
            }
            if (otherChampion.distanceSquaredTo(p.point) < currentDistance)
                currentChampion = otherChampion;
            return currentChampion;
        }
        if (current.xComp) {
            thisRect = new RectHV(current.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            otherRect = new RectHV(rect.xmin(), rect.ymin(), current.point.x(), rect.ymax());
        }
        else {
            thisRect = new RectHV(rect.xmin(), current.point.y(), rect.xmax(), rect.ymax());
            otherRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), current.point.y());
        }
        rectangularDistance = otherRect.distanceSquaredTo(p.point);

        if (current.right != null)
            otherChampion = nearest(p, current.right, thisRect, currentChampion);
        if (otherChampion.distanceSquaredTo(p.point) < currentDistance) {
            currentChampion = otherChampion;
            currentDistance = otherChampion.distanceSquaredTo(p.point);
        }
        if (current.left != null && currentDistance > rectangularDistance) {
            otherChampion = nearest(p, current.left, otherRect, currentChampion);
        }
        if (otherChampion.distanceSquaredTo(p.point) < currentDistance)
            currentChampion = otherChampion;
        return currentChampion;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        StdDraw.show();
    }
}

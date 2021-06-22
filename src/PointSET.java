import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.size() == 0;
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        validate(p);

        set.add(p);
    }

    public boolean contains(Point2D p) {
        validate(p);

        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rectangle is null.");
        }

        LinkedList<Point2D> pointsList = new LinkedList<>();
        for (Point2D p : set) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax()
             && p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
                pointsList.add(p);
            }
        }

        return pointsList;
    }

    public Point2D nearest(Point2D p) {
        validate(p);
        if (set.size() == 0) {
            return null;
        }

        Point2D nearest = set.first();
        for (Point2D curr : set) {
            if (curr.distanceTo(p) < nearest.distanceTo(p)) {
                nearest = curr;
            }
        }

        return nearest;
    }

    private void validate(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null.");
        }
    }

    public static void main(String[] args) {
        PointSET brute = new PointSET();
        In in = new In(args[0]);

        for (int i = 0; i < 10; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        Point2D c = new Point2D(0.5, 0.5);
        System.out.println(brute.nearest(c));
    }
}

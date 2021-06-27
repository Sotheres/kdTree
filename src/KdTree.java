import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb, rt;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public KdTree() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        validate(p);
        root = insert(root, p, true, null);
    }

    private Node insert(Node cur, Point2D p, boolean vertical, RectHV rect) {
        if (root == null) {
            size++;
            return new Node(p, new RectHV(0, 0, 1, 1));
        }
        if (cur == null) {
            size++;
            return new Node(p, rect);
        }

        if (p.equals(cur.p)) {
            return cur;
        }

        if (vertical) {
            if (cur.p.x() > p.x()) {
                if (cur.lb == null) {
                    rect = new RectHV(0, cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
                }
                cur.lb = insert(cur.lb, p, !vertical, rect);
            } else {
                if (cur.rt == null) {
                    rect = new RectHV(cur.p.x(), cur.rect.ymin(), 1, cur.rect.ymax());
                }
                cur.rt = insert(cur.rt, p, !vertical, rect);
            }
        } else {
            if (cur.p.y() > p.y()) {
                if (cur.lb == null) {
                    rect = new RectHV(cur.rect.xmin(), 0, cur.rect.xmax(), cur.p.y());
                }
                cur.lb = insert(cur.lb, p, !vertical, rect);
            } else {
                if (cur.rt == null) {
                    rect = new RectHV(cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), 1);
                }
                cur.rt = insert(cur.rt, p, !vertical, rect);
            }
        }

        return cur;
    }

    public boolean contains(Point2D p) {
        validate(p);
        return contains(root, p, true);
    }

    private boolean contains(Node cur, Point2D p, boolean vertical) {
        if (cur == null) {
            return false;
        }
        if (cur.p.equals(p)) {
            return true;
        }

        if (vertical) {
            if (cur.p.x() > p.x()) {
                return contains(cur.lb, p, !vertical);
            } else {
                return contains(cur.rt, p, !vertical);
            }
        } else {
            if (cur.p.y() > p.y()) {
                return contains(cur.lb, p, !vertical);
            } else {
                return contains(cur.rt, p, !vertical);
            }
        }
    }

    public void draw() {
        boolean vertical = true;
        draw(root, vertical);
    }

    private void draw(Node x, boolean vertical) {
        if (x == null) {
            return;
        }

        draw(x.lb, !vertical);

        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        StdDraw.point(x.p.x(), x.p.y());
        StdDraw.setPenRadius();

        draw(x.rt, !vertical);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rectangle is null.");
        }

        LinkedList<Point2D> points = new LinkedList<>();
        range(root, rect, points);

        return points;
    }

    private void range(Node cur, RectHV rect, LinkedList<Point2D> points) {
        if (cur == null) {
            return;
        }

        if (cur.lb != null && rect.intersects(cur.lb.rect)) {
            range(cur.lb, rect, points);
        }
        if (rect.contains(cur.p)) {
            points.add(cur.p);
        }
        if (cur.rt != null && rect.intersects(cur.rt.rect)) {
            range(cur.rt, rect, points);
        }
    }

    public Point2D nearest(Point2D p) {
        validate(p);
        if (root == null) {
            return null;
        }
        boolean vertical = true;

        return nearest(root, p, root.p, vertical);
    }

    private Point2D nearest(Node cur, Point2D query, Point2D nearest, boolean vertical) {
        if (cur == null) {
            return nearest;
        }

        if (cur.p.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
            nearest = cur.p;
        }
        if (vertical) {
            if (query.x() < cur.p.x()) {
                if (cur.lb != null
                        && cur.lb.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.lb, query, nearest, vertical);
                }
                if (cur.rt != null
                        && cur.rt.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.rt, query, nearest, vertical);
                }
            } else {
                if (cur.rt != null
                        && cur.rt.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.rt, query, nearest, vertical);
                }
                if (cur.lb != null
                        && cur.lb.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.lb, query, nearest, vertical);
                }
            }
        } else {
            if (query.y() < cur.p.y()) {
                if (cur.lb != null
                        && cur.lb.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.lb, query, nearest, vertical);
                }
                if (cur.rt != null
                        && cur.rt.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.rt, query, nearest, vertical);
                }
            } else {
                if (cur.rt != null
                        && cur.rt.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.rt, query, nearest, vertical);
                }
                if (cur.lb != null
                        && cur.lb.rect.distanceSquaredTo(query) < nearest.distanceSquaredTo(query)) {
                    nearest = nearest(cur.lb, query, nearest, vertical);
                }
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
        KdTree tree = new KdTree();
        In in = new In(args[0]);

        for (int i = 0; i < 10; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }

        tree.draw();
    }
}
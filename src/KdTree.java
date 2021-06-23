import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb, rt;

        public Node(Point2D p) {
            this.p = p;
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
        root = insert(root, p, true);
        size++;
    }

    private Node insert(Node cur, Point2D p, boolean vertical) {
        if (cur == null) {
            return new Node(p);
        }

        if (vertical) {
            if (cur.p.x() > p.x()) {
                cur.lb = insert(cur.lb, p, !vertical);
            } else {
                cur.rt = insert(cur.rt, p, !vertical);
            }
        } else {
            if (cur.p.y() > p.y()) {
                cur.lb = insert(cur.lb, p, !vertical);
            } else {
                cur.rt = insert(cur.rt, p, !vertical);
            }
        }

        return cur;
    }

    public boolean contains(Point2D p) {
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

    }

//    public Iterable<Point2D> range(RectHV rect) {
//
//    }
//
//    public Point2D nearest(Point2D p) {
//
//    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        In in = new In(args[0]);

        for (int i = 0; i < 10; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }

        System.out.println(tree.contains(new Point2D(0.372, 0.497)));
    }
}
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private Point2D p;
        private RectHV rect;
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
        RectHV unitSquare;
        if (root == null) {
            unitSquare = new RectHV(0, 0, 1, 1);
        } else {
            unitSquare = null;
        }
        root = insert(root, p, true, unitSquare);
        size++;
    }

    private Node insert(Node cur, Point2D p, boolean vertical, RectHV rect) {
        if (cur == null) {
            return new Node(p, rect);
        }

        if (vertical) {
            if (cur.p.x() > p.x()) {
                RectHV innerRect = new RectHV(0, cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
                cur.lb = insert(cur.lb, p, !vertical, innerRect);
            } else {
                RectHV innerRect = new RectHV(cur.p.x(), cur.rect.ymin(), 1, cur.rect.ymax());
                cur.rt = insert(cur.rt, p, !vertical, innerRect);
            }
        } else {
            if (cur.p.y() > p.y()) {
                RectHV innerRect = new RectHV(cur.rect.xmin(), 0, cur.rect.xmax(), cur.p.y());
                cur.lb = insert(cur.lb, p, !vertical, innerRect);
            } else {
                RectHV innerRect = new RectHV(cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), 1);
                cur.rt = insert(cur.rt, p, !vertical, innerRect);
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
        boolean vertical = true;
        visit(root, vertical);
    }

    private void visit(Node x, boolean vertical) {
        if (x == null) {
            return;
        }

        visit(x.lb, !vertical);
        draw(x, vertical);
        visit(x.rt, !vertical);
    }

    private void draw(Node x, boolean vertical) {
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
    }

    public Iterable<Point2D> range(RectHV rect) {

    }

    private boolean isIntersect(RectHV a, RectHV b) {
        double e = 0.000001;
        if (a.xmax() > b.xmin()
                && (a.ymax() > b.ymin() || a.ymin() < b.ymax()
                    || (a.ymax() > b.ymax() && a.ymin() < b.ymin()))) {
            return true;
        }
        if (a.xmax() - b.xmin() < e
                && (a.ymax() - b.ymin() < e || a.ymin() - b.ymax() < e
                || (a.ymax() - b.ymax() < e && a.ymin() - b.ymin() < e))) {
            return true;
        }
        if (a.xmax() > b.xmax() && a.xmin() < b.xmin()
                && (a.ymax() > b.ymin() || a.ymin() < b.ymax()
                    || (a.ymax() > b.ymax() && a.ymin() < b.ymin()))) {
            return true;
        }
        if (a.xmax() - b.xmax() < e && a.xmin() - b.xmin() < e
                && (a.ymax() - b.ymin() < e || a.ymin() - b.ymax() < e
                || (a.ymax() - b.ymax() < e && a.ymin() - b.ymin() < e))) {
            return true;
        }
        if (a.xmin() < b.xmax()
                && (a.ymax() > b.ymin() || a.ymin() < b.ymax()
                || (a.ymax() > b.ymax() && a.ymin() < b.ymin()))) {
            return true;
        }
        return a.xmin() - b.xmax() < e
                && (a.ymax() - b.ymin() < e || a.ymin() - b.ymax() < e
                || (a.ymax() - b.ymax() < e && a.ymin() - b.ymin() < e));
    }

//    public Point2D nearest(Point2D p) {
//
//    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        In in = new In(args[0]);

        for (int i = 0; i < 5; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }

        tree.draw();
    }
}
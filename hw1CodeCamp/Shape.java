import java.util.*;

/*
 Shape data for ShapeClient:
 "0 0  0 1  1 1  1 0"
 "10 10  10 11  11 11  11 10"
 "0.5 0.5  0.5 -10  1.5 0"
 "0.5 0.5  0.75 0.75  0.75 0.2"
*/

public class Shape {
    private final ArrayList<Point> points;

    public Shape(String points) {
        this.points = new ArrayList<>();
        LinkedList<Double> buffer = new LinkedList<>();
        Scanner scanner = new Scanner(points);
        while (scanner.hasNextDouble()) {
            buffer.add(scanner.nextDouble());
        }
        scanner.close();
        while (buffer.size() > 1) {
            Point p = new Point(buffer.remove(), buffer.remove());
            this.points.add(p);
        }
    }

    private boolean pointInside(Point p) {
        for (Point point : this.points) {
            if (p.equals(point)) {
                return true;
            }
        }
        int count = 0;
        for (int i = 0; i < points.size(); i++) {
            Point from = points.get(i);
            Point to;
            if (i == points.size() - 1) {
                to = points.get(0);
            }
            else {
                to = points.get(i + 1);
            }
            if (intersectYRay(from, to, p)) {
                count += 1;
            }
        }
        if (count % 2 == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean intersectYRay(Point from, Point to, Point p) {
        // the Ray start from point p, horizontal, positive direction
        double y = p.getY();
        if ((from.getY() - y) * (to.getY() - y) < 0) {
            if (from.getX() == to.getX()) {
                if (p.getX() < from.getX()) {
                    return true;
                }
                else {
                    return false;
                }
            }
            double k = (from.getY() - to.getY()) / (from.getX() - to.getX());
            double b = from.getY() - k * from.getX();
            double x = (y - b) / k;
            if (p.getX() < x) {
                return true;
            }
        }
        return false;
    }

    public boolean cross(Shape other) {
        int in = 0;
        int out = 0;
        for (Point p : other.points) {
            if (pointInside(p)) {
                in += 1;
            }
            else {
                out += 1;
            }
        }
        if (in != 0 && out != 0) {
            return true;
        }
        return false;
    }

    public int encircles(Shape other) {
        int in = 0;
        int out = 0;
        for (Point p : other.points) {
            if (pointInside(p)) {
                in += 1;
            }
            else {
                out += 1;
            }
        }
        if (in != 0) {
            if (out != 0) {
                return 1;
            }
            else {
                return 2;
            }
        }
        for (Point p : this.points) {
            if (!other.pointInside(p)) {
                return 0;
            }
        }
        return 2;
    }

}


package com.jamma.math.geometry;

import com.jamma.math.Vector3f;

public record Line(Vector3f point, Vector3f direction) {

    public Line(float px, float py, float dx, float dy) {
        this(new Vector3f(px, py, 0.0f), new Vector3f(dx, dy, 0.0f));
    }

    public double distance(Vector3f p) {
        double dx = p.x() - point.x();
        double dy = p.y() - point.y();
        double ddx = direction.x();
        double ddy = direction.y();
        double t = (dx * ddx + dy * ddy) / (ddx * ddx + ddy * ddy);
        double cx = point.x() + t * ddx;
        double cy = point.y() + t * ddy;
        return Math.sqrt((p.x() - cx) * (p.x() - cx) + (p.y() - cy) * (p.y() - cy));
    }

    public Vector3f closestPoint(Vector3f p) {
        double dx = p.x() - point.x();
        double dy = p.y() - point.y();
        double ddx = direction.x();
        double ddy = direction.y();
        double t = (dx * ddx + dy * ddy) / (ddx * ddx + ddy * ddy);
        return new Vector3f(
            (float) (point.x() + t * ddx),
            (float) (point.y() + t * ddy),
            0.0f
        );
    }
}

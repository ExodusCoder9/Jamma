package com.jamma.math.geometry;

import com.jamma.math.Vector3f;

public record Circle(Vector3f center, float radius) {

    public Circle(float cx, float cy, float r) {
        this(new Vector3f(cx, cy, 0.0f), r);
    }

    public boolean contains(Vector3f p) {
        return center.distanceSquared(p) <= radius * radius;
    }

    public boolean contains(float px, float py) {
        float dx = px - center.x();
        float dy = py - center.y();
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean intersects(Circle other) {
        float dx = center.x() - other.center.x();
        float dy = center.y() - other.center.y();
        float rSum = radius + other.radius;
        return dx * dx + dy * dy <= rSum * rSum;
    }

    public float area() {
        return (float) (Math.PI * radius * radius);
    }
}

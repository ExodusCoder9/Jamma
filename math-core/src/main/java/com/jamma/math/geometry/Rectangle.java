package com.jamma.math.geometry;

import com.jamma.math.Vector3f;

public record Rectangle(float minX, float minY, float maxX, float maxY) {

    public static Rectangle ofSize(float cx, float cy, float w, float h) {
        float hw = w / 2.0f;
        float hh = h / 2.0f;
        return new Rectangle(cx - hw, cy - hh, cx + hw, cy + hh);
    }

    public boolean contains(float px, float py) {
        return px >= minX && px <= maxX && py >= minY && py <= maxY;
    }

    public boolean contains(Vector3f p) {
        return contains(p.x(), p.y());
    }

    public boolean intersects(Rectangle other) {
        return maxX >= other.minX && minX <= other.maxX &&
               maxY >= other.minY && minY <= other.maxY;
    }

    public boolean intersects(Circle c) {
        float closestX = Math.clamp(c.center().x(), minX, maxX);
        float closestY = Math.clamp(c.center().y(), minY, maxY);
        float dx = closestX - c.center().x();
        float dy = closestY - c.center().y();
        return dx * dx + dy * dy <= c.radius() * c.radius();
    }

    public float width() {
        return maxX - minX;
    }

    public float height() {
        return maxY - minY;
    }

    public float area() {
        return width() * height();
    }

    public Vector3f center() {
        return new Vector3f((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, 0.0f);
    }
}

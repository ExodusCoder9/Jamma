package com.jamma.math.geometry;

import com.jamma.math.Vector3f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LineCircleRectTest {
    static final float F = 1e-5f;

    @Test void lineClosestPoint() {
        Line line = new Line(0, 0, 1, 0);
        Vector3f closest = line.closestPoint(new Vector3f(0, 5, 0));
        assertEquals(0, closest.x(), F);
        assertEquals(0, closest.y(), F);
    }

    @Test void lineDistance() {
        Line line = new Line(0, 0, 1, 0);
        assertEquals(5.0, line.distance(new Vector3f(0, 5, 0)), 1e-12);
    }

    @Test void circleContains() {
        Circle c = new Circle(0, 0, 5);
        assertTrue(c.contains(3, 4));
        assertFalse(c.contains(10, 0));
    }

    @Test void circleArea() {
        Circle c = new Circle(0, 0, 5);
        assertEquals(78.53981634f, c.area(), 1e-4f);
    }

    @Test void circleIntersects() {
        Circle c = new Circle(0, 0, 5);
        assertTrue(c.intersects(new Circle(3, 0, 5)));
    }

    @Test void rectContains() {
        Rectangle r = new Rectangle(-5, -5, 5, 5);
        assertTrue(r.contains(0, 0));
        assertFalse(r.contains(10, 10));
    }

    @Test void rectIntersects() {
        Rectangle r = new Rectangle(-5, -5, 5, 5);
        assertTrue(r.intersects(new Rectangle(0, 0, 10, 10)));
    }

    @Test void rectIntersectsCircle() {
        Rectangle r = new Rectangle(-5, -5, 5, 5);
        assertTrue(r.intersects(new Circle(0, 0, 10)));
    }

    @Test void rectDimensions() {
        Rectangle r = new Rectangle(-5, -5, 5, 5);
        assertEquals(10, r.width(), F);
        assertEquals(10, r.height(), F);
        assertEquals(100, r.area(), F);
    }

    @Test void rectCenter() {
        Rectangle r = new Rectangle(-5, -5, 5, 5);
        Vector3f center = r.center();
        assertEquals(0, center.x(), F);
        assertEquals(0, center.y(), F);
    }
}

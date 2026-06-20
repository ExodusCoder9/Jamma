package com.jamma.math.geometry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.jamma.math.Vector3D;
import com.jamma.math.matrix.Matrix4d;

class AABBTest {

    @Test
    void constructorAndContains() {
        AABB box = new AABB(0, 0, 0, 1, 1, 1);
        Vector3D center = new Vector3D(0.5, 0.5, 0.5);
        assertTrue(box.contains(center));
    }

    @Test
    void unionPoint() {
        AABB box = new AABB();
        box.union(new Vector3D(1, 2, 3));
        box.union(new Vector3D(4, 5, 6));
        assertEquals(1.0, box.minX);
        assertEquals(2.0, box.minY);
        assertEquals(3.0, box.minZ);
        assertEquals(4.0, box.maxX);
        assertEquals(5.0, box.maxY);
        assertEquals(6.0, box.maxZ);
    }

    @Test
    void unionAABB() {
        AABB a = new AABB(0, 0, 0, 2, 2, 2);
        AABB b = new AABB(1, 1, 1, 3, 3, 3);
        a.union(b);
        assertEquals(0.0, a.minX);
        assertEquals(0.0, a.minY);
        assertEquals(0.0, a.minZ);
        assertEquals(3.0, a.maxX);
        assertEquals(3.0, a.maxY);
        assertEquals(3.0, a.maxZ);
    }

    @Test
    void intersection() {
        AABB a = new AABB(0, 0, 0, 2, 2, 2);
        AABB b = new AABB(1, 1, 1, 3, 3, 3);
        a.intersection(b);
        assertEquals(1.0, a.minX);
        assertEquals(1.0, a.minY);
        assertEquals(1.0, a.minZ);
        assertEquals(2.0, a.maxX);
        assertEquals(2.0, a.maxY);
        assertEquals(2.0, a.maxZ);
    }

    @Test
    void intersects() {
        AABB a = new AABB(0, 0, 0, 2, 2, 2);
        AABB b = new AABB(1, 1, 1, 3, 3, 3);
        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

    @Test
    void notIntersects() {
        AABB a = new AABB(0, 0, 0, 1, 1, 1);
        AABB b = new AABB(2, 2, 2, 3, 3, 3);
        assertFalse(a.intersects(b));
    }

    @Test
    void isEmpty() {
        AABB box = new AABB();
        assertTrue(box.isEmpty());
    }

    @Test
    void notEmpty() {
        AABB box = new AABB(0, 0, 0, 1, 1, 1);
        assertFalse(box.isEmpty());
    }

    @Test
    void clear() {
        AABB box = new AABB(0, 0, 0, 1, 1, 1);
        assertFalse(box.isEmpty());
        box.clear();
        assertTrue(box.isEmpty());
    }

    @Test
    void getCenterAndExtentAndSize() {
        AABB box = new AABB(0, 0, 0, 2, 4, 6);
        Vector3D center = box.getCenter(new Vector3D(0, 0, 0));
        assertEquals(1.0, center.x());
        assertEquals(2.0, center.y());
        assertEquals(3.0, center.z());
        Vector3D extent = box.getExtent(new Vector3D(0, 0, 0));
        assertEquals(1.0, extent.x());
        assertEquals(2.0, extent.y());
        assertEquals(3.0, extent.z());
        Vector3D size = box.getSize(new Vector3D(0, 0, 0));
        assertEquals(2.0, size.x());
        assertEquals(4.0, size.y());
        assertEquals(6.0, size.z());
    }

    @Test
    void getMinMax() {
        AABB box = new AABB(1, 2, 3, 4, 5, 6);
        Vector3D min = box.getMin(new Vector3D(0, 0, 0));
        assertEquals(1.0, min.x());
        assertEquals(2.0, min.y());
        assertEquals(3.0, min.z());
        Vector3D max = box.getMax(new Vector3D(0, 0, 0));
        assertEquals(4.0, max.x());
        assertEquals(5.0, max.y());
        assertEquals(6.0, max.z());
    }

    @Test
    void containsPoint() {
        AABB box = new AABB(0, 0, 0, 2, 2, 2);
        assertTrue(box.contains(new Vector3D(1, 1, 1)));
        assertTrue(box.contains(new Vector3D(0, 0, 0)));
        assertTrue(box.contains(new Vector3D(2, 2, 2)));
        assertFalse(box.contains(new Vector3D(3, 1, 1)));
        assertFalse(box.contains(new Vector3D(1, -1, 1)));
    }

    @Test
    void containsAABB() {
        AABB outer = new AABB(0, 0, 0, 5, 5, 5);
        AABB inner = new AABB(1, 1, 1, 2, 2, 2);
        assertTrue(outer.contains(inner));
        assertFalse(inner.contains(outer));
    }

    @Test
    void intersectsSphere() {
        AABB box = new AABB(0, 0, 0, 2, 2, 2);
        assertTrue(box.intersectsSphere(new Vector3D(1, 1, 1), 1.0));
        assertTrue(box.intersectsSphere(new Vector3D(-1, 1, 1), 1.0));
        assertFalse(box.intersectsSphere(new Vector3D(10, 0, 0), 1.0));
    }

    @Test
    void transform() {
        AABB box = new AABB(-1, -1, -1, 1, 1, 1);
        Matrix4d m = new Matrix4d().scale(2, 3, 4);
        box.transform(m);
        assertEquals(-2.0, box.minX, 1e-15);
        assertEquals(2.0, box.maxX, 1e-15);
        assertEquals(-3.0, box.minY, 1e-15);
        assertEquals(3.0, box.maxY, 1e-15);
        assertEquals(-4.0, box.minZ, 1e-15);
        assertEquals(4.0, box.maxZ, 1e-15);
    }

    @Test
    void transformTranslate() {
        AABB box = new AABB(0, 0, 0, 1, 1, 1);
        Matrix4d m = new Matrix4d().translate(5, 10, 15);
        box.transform(m);
        assertEquals(5.0, box.minX, 1e-15);
        assertEquals(6.0, box.maxX, 1e-15);
        assertEquals(10.0, box.minY, 1e-15);
        assertEquals(11.0, box.maxY, 1e-15);
        assertEquals(15.0, box.minZ, 1e-15);
        assertEquals(16.0, box.maxZ, 1e-15);
    }

    @Test
    void equalsAndHashCode() {
        AABB a = new AABB(0, 0, 0, 1, 1, 1);
        AABB b = new AABB(0, 0, 0, 1, 1, 1);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new AABB());
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void testToString() {
        AABB box = new AABB(0, 1, 2, 3, 4, 5);
        String s = box.toString();
        assertNotNull(s);
        assertTrue(s.length() > 0);
    }

    @Test
    void copyConstructor() {
        AABB a = new AABB(1, 2, 3, 4, 5, 6);
        AABB b = new AABB(a);
        assertEquals(a, b);
        a.minX = 99;
        assertNotEquals(a, b);
    }

    @Test
    void vectorConstructor() {
        AABB box = new AABB(new Vector3D(1, 2, 3), new Vector3D(4, 5, 6));
        assertEquals(1.0, box.minX);
        assertEquals(2.0, box.minY);
        assertEquals(3.0, box.minZ);
        assertEquals(4.0, box.maxX);
        assertEquals(5.0, box.maxY);
        assertEquals(6.0, box.maxZ);
    }

    @Test
    void intersectionNonOverlapping() {
        AABB a = new AABB(0, 0, 0, 1, 1, 1);
        AABB b = new AABB(2, 2, 2, 3, 3, 3);
        a.intersection(b);
        assertTrue(a.isEmpty());
    }

    @Test
    void intersectsRay() {
        AABB box = new AABB(0, 0, 0, 1, 1, 1);
        double t = box.intersectsRay(new Vector3D(-5, 0.5, 0.5), new Vector3D(1, 0, 0));
        assertTrue(t >= 0.0);
        assertEquals(5.0, t, 1e-15);
    }
}

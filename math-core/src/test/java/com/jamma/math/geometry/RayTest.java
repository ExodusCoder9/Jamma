package com.jamma.math.geometry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.jamma.math.Vector3D;
import com.jamma.math.matrix.Matrix4d;

class RayTest {

    @Test
    void constructorAndGetPoint() {
        Ray ray = new Ray(new Vector3D(1, 2, 3), new Vector3D(1, 0, 0));
        Vector3D point = ray.getPoint(2.0, new Vector3D(0, 0, 0));
        assertEquals(3.0, point.x(), 1e-15);
        assertEquals(2.0, point.y(), 1e-15);
        assertEquals(3.0, point.z(), 1e-15);
    }

    @Test
    void constructorWithDoubles() {
        Ray ray = new Ray(1, 2, 3, 4, 5, 6);
        assertEquals(1.0, ray.origin.x());
        assertEquals(2.0, ray.origin.y());
        assertEquals(3.0, ray.origin.z());
        assertEquals(4.0, ray.direction.x());
        assertEquals(5.0, ray.direction.y());
        assertEquals(6.0, ray.direction.z());
    }

    @Test
    void distanceSquared() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0));
        double distSq = ray.distanceSquared(new Vector3D(1, 5, 0));
        assertEquals(1.0, distSq, 1e-15);
    }

    @Test
    void distanceSquaredBehindRay() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));
        double distSq = ray.distanceSquared(new Vector3D(-5, 0, 0));
        assertEquals(25.0, distSq, 1e-15);
    }

    @Test
    void closestPoint() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));
        Vector3D closest = ray.closestPoint(new Vector3D(2, 3, 0), new Vector3D(0, 0, 0));
        assertEquals(2.0, closest.x(), 1e-15);
        assertEquals(0.0, closest.y(), 1e-15);
        assertEquals(0.0, closest.z(), 1e-15);
    }

    @Test
    void closestPointBehindRay() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));
        Vector3D closest = ray.closestPoint(new Vector3D(-5, 3, 0), new Vector3D(0, 0, 0));
        assertEquals(0.0, closest.x(), 1e-15);
        assertEquals(0.0, closest.y(), 1e-15);
        assertEquals(0.0, closest.z(), 1e-15);
    }

    @Test
    void intersectsAABB() {
        Ray ray = new Ray(new Vector3D(-5, 0.5, 0.5), new Vector3D(1, 0, 0));
        AABB box = new AABB(0, 0, 0, 1, 1, 1);
        double t = ray.intersectsAABB(box);
        assertTrue(t >= 0.0);
        assertEquals(5.0, t, 1e-15);
    }

    @Test
    void intersectsAABBMiss() {
        Ray ray = new Ray(new Vector3D(-5, 10, 0.5), new Vector3D(1, 0, 0));
        AABB box = new AABB(0, 0, 0, 1, 1, 1);
        double t = ray.intersectsAABB(box);
        assertTrue(Double.isNaN(t) || t < 0.0 || t == -1.0);
    }

    @Test
    void intersectsPlane() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0));
        double t = ray.intersectsPlane(0, 1, 0, -5, new Vector3D(0, 0, 0));
        assertEquals(5.0, t, 1e-15);
    }

    @Test
    void intersectsPlaneNoIntersection() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));
        double t = ray.intersectsPlane(0, 1, 0, -5, new Vector3D(0, 0, 0));
        assertTrue(Double.isNaN(t));
    }

    @Test
    void intersectsPlaneBehindRay() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0));
        double t = ray.intersectsPlane(0, 1, 0, 5, new Vector3D(0, 0, 0));
        assertTrue(Double.isNaN(t));
    }

    @Test
    void intersectsSphere() {
        Ray ray = new Ray(new Vector3D(0, 0, -5), new Vector3D(0, 0, 1));
        double t = ray.intersectsSphere(new Vector3D(0, 0, 0), 1.0);
        assertTrue(t >= 0.0);
        assertEquals(4.0, t, 1e-15);
    }

    @Test
    void intersectsSphereMiss() {
        Ray ray = new Ray(new Vector3D(0, 0, -5), new Vector3D(1, 0, 0));
        double t = ray.intersectsSphere(new Vector3D(0, 0, 0), 1.0);
        assertTrue(t < 0.0 || Double.isNaN(t));
    }

    @Test
    void transformByTranslation() {
        Ray ray = new Ray(new Vector3D(1, 2, 3), new Vector3D(1, 0, 0));
        Matrix4d m = new Matrix4d().translate(10, 20, 30);
        ray.transform(m);
        assertEquals(11.0, ray.origin.x(), 1e-15);
        assertEquals(22.0, ray.origin.y(), 1e-15);
        assertEquals(33.0, ray.origin.z(), 1e-15);
        assertEquals(1.0, ray.direction.x(), 1e-15);
        assertEquals(0.0, ray.direction.y(), 1e-15);
        assertEquals(0.0, ray.direction.z(), 1e-15);
    }

    @Test
    void transformByRotation() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0));
        Matrix4d m = new Matrix4d().rotateX(Math.PI / 2.0);
        ray.transform(m);
        assertEquals(0.0, ray.direction.x(), 1e-15);
        assertEquals(0.0, ray.direction.y(), 1e-15);
        assertEquals(1.0, ray.direction.z(), 1e-15);
    }

    @Test
    void normalize() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 5, 0));
        ray.normalize();
        assertEquals(0.0, ray.direction.x(), 1e-15);
        assertEquals(1.0, ray.direction.y(), 1e-15);
        assertEquals(0.0, ray.direction.z(), 1e-15);
    }

    @Test
    void normalizeAlreadyUnit() {
        Ray ray = new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0));
        ray.normalize();
        assertEquals(0.0, ray.direction.x(), 1e-15);
        assertEquals(1.0, ray.direction.y(), 1e-15);
    }

    @Test
    void testToString() {
        Ray ray = new Ray(new Vector3D(1, 2, 3), new Vector3D(0, 1, 0));
        String s = ray.toString();
        assertNotNull(s);
        assertTrue(s.contains("origin") || s.contains("direction"));
    }
}

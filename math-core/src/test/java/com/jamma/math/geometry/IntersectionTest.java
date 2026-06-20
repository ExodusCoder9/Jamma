package com.jamma.math.geometry;

import com.jamma.math.Vector3D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntersectionTest {

    @Test
    public void testRayTriangle() {
        Ray ray = new Ray(new Vector3D(0.0, 0.0, 5.0), new Vector3D(0.0, 0.0, -1.0));
        Vector3D v0 = new Vector3D(-1.0, -1.0, 0.0);
        Vector3D v1 = new Vector3D(1.0, -1.0, 0.0);
        Vector3D v2 = new Vector3D(0.0, 1.0, 0.0);

        Vector3D[] intersectionPoint = new Vector3D[1];
        boolean hit = Intersection.testRayTriangle(ray, v0, v1, v2, intersectionPoint);
        assertTrue(hit);
        assertNotNull(intersectionPoint[0]);
        assertEquals(0.0, intersectionPoint[0].x(), 1e-6);
        assertEquals(0.0, intersectionPoint[0].y(), 1e-6);
        assertEquals(0.0, intersectionPoint[0].z(), 1e-6);

        // Test ray pointing away
        Ray rayAway = new Ray(new Vector3D(0.0, 0.0, 5.0), new Vector3D(0.0, 0.0, 1.0));
        boolean hitAway = Intersection.testRayTriangle(rayAway, v0, v1, v2, intersectionPoint);
        assertFalse(hitAway);

        // Test ray parallel to triangle
        Ray rayParallel = new Ray(new Vector3D(0.0, 0.0, 5.0), new Vector3D(1.0, 0.0, 0.0));
        boolean hitParallel = Intersection.testRayTriangle(rayParallel, v0, v1, v2, intersectionPoint);
        assertFalse(hitParallel);
    }

    @Test
    public void testRayAABB() {
        Ray ray = new Ray(new Vector3D(0.0, 0.0, 5.0), new Vector3D(0.0, 0.0, -1.0));
        AABB box = new AABB(new Vector3D(-1.0, -1.0, -1.0), new Vector3D(1.0, 1.0, 1.0));

        double[] resultNearFar = new double[2];
        boolean hit = Intersection.testRayAABB(ray, box, resultNearFar);
        assertTrue(hit);
        assertEquals(4.0, resultNearFar[0], 1e-6); // distance to near plane (z = 1.0 from z = 5.0)
        assertEquals(6.0, resultNearFar[1], 1e-6); // distance to far plane (z = -1.0 from z = 5.0)

        // Test ray missing box
        Ray rayMiss = new Ray(new Vector3D(5.0, 5.0, 5.0), new Vector3D(0.0, 0.0, -1.0));
        boolean hitMiss = Intersection.testRayAABB(rayMiss, box, resultNearFar);
        assertFalse(hitMiss);
    }

    @Test
    public void testAABBAABB() {
        AABB box1 = new AABB(new Vector3D(-1.0, -1.0, -1.0), new Vector3D(1.0, 1.0, 1.0));
        AABB box2 = new AABB(new Vector3D(0.5, 0.5, 0.5), new Vector3D(2.0, 2.0, 2.0));
        AABB box3 = new AABB(new Vector3D(2.5, 2.5, 2.5), new Vector3D(4.0, 4.0, 4.0));

        assertTrue(Intersection.testAABBAABB(box1, box2));
        assertFalse(Intersection.testAABBAABB(box1, box3));
    }

    @Test
    public void testSphereSphere() {
        Vector3D c0 = new Vector3D(0.0, 0.0, 0.0);
        double r0 = 1.0;
        Vector3D c1 = new Vector3D(1.5, 0.0, 0.0);
        double r1 = 1.0;
        Vector3D c2 = new Vector3D(3.0, 0.0, 0.0);
        double r2 = 0.5;

        assertTrue(Intersection.testSphereSphere(c0, r0, c1, r1));
        assertFalse(Intersection.testSphereSphere(c0, r0, c2, r2));
    }
}

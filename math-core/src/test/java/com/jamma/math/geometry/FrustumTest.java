package com.jamma.math.geometry;

import com.jamma.math.Vector3d;
import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix4d;
import com.jamma.math.matrix.Matrix4f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrustumTest {

    @Test
    public void testOpenGLFrustumContainment() {
        // Identity view-projection matrix means NDC clip space itself
        Matrix4d m = new Matrix4d().identity();
        FrustumIntersection frustum = new FrustumIntersection(m, false);

        // Point inside (-1 to 1)
        assertEquals(FrustumIntersection.INSIDE, frustum.testPoint(new Vector3d(0.0, 0.0, 0.0)));
        assertEquals(FrustumIntersection.INSIDE, frustum.testPoint(0.0, 0.0, 0.0));

        // Point outside
        assertEquals(FrustumIntersection.OUTSIDE, frustum.testPoint(new Vector3d(2.0, 0.0, 0.0)));
        assertEquals(FrustumIntersection.OUTSIDE, frustum.testPoint(0.0, -2.0, 0.0));

        // Sphere culling
        assertEquals(FrustumIntersection.INSIDE, frustum.testSphere(new Vector3d(0.0, 0.0, 0.0), 0.5));
        assertEquals(FrustumIntersection.INTERSECT, frustum.testSphere(new Vector3d(0.9, 0.0, 0.0), 0.2));
        assertEquals(FrustumIntersection.OUTSIDE, frustum.testSphere(new Vector3d(1.5, 0.0, 0.0), 0.2));

        // AABB culling
        AABB boxInside = new AABB(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0.5, 0.5, 0.5));
        assertEquals(FrustumIntersection.INSIDE, frustum.testAABB(boxInside));

        AABB boxIntersect = new AABB(new Vector3d(0.5, 0.5, 0.5), new Vector3d(1.5, 1.5, 1.5));
        assertEquals(FrustumIntersection.INTERSECT, frustum.testAABB(boxIntersect));

        AABB boxOutside = new AABB(new Vector3d(2.0, 2.0, 2.0), new Vector3d(3.0, 3.0, 3.0));
        assertEquals(FrustumIntersection.OUTSIDE, frustum.testAABB(boxOutside));
    }

    @Test
    public void testVulkanFrustumContainment() {
        // Identity clip space for Vulkan:
        // X in [-1, 1], Y in [-1, 1], Z in [0, 1]
        Matrix4f m = new Matrix4f().identity();
        FrustumIntersection frustum = new FrustumIntersection(m, true);

        // Z = -0.5 is outside Vulkan clip space
        assertEquals(FrustumIntersection.OUTSIDE, frustum.testPoint(new Vector3f(0.0f, 0.0f, -0.5f)));

        // Z = 0.5 is inside Vulkan clip space
        assertEquals(FrustumIntersection.INSIDE, frustum.testPoint(new Vector3f(0.0f, 0.0f, 0.5f)));

        // Sphere crossing Z near plane (z = 0.0)
        assertEquals(FrustumIntersection.INTERSECT, frustum.testSphere(0.0, 0.0, 0.0, 0.1));
    }

    @Test
    public void testDegenerateMatrixDoesNotNaN() {
        FrustumIntersection frustum = new FrustumIntersection(new Matrix4d().zero(), false);
        assertEquals(FrustumIntersection.OUTSIDE, frustum.testAABB(new AABB(new Vector3d(-1, -1, -1), new Vector3d(1, 1, 1))));
    }
}

package com.jamma.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorMathTest {
    static final float F = 1e-5f;
    static final double D = 1e-12;

    // ── VectorMathf ───────────────────────────────────────────────────

    @Test void vmfAdd() {
        assertEquals(new Vector3f(4, 6, 8), VectorMathf.add(new Vector3f(1, 2, 3), new Vector3f(3, 4, 5)));
    }
    @Test void vmfAddDest() {
        float[] dest = new float[4];
        VectorMathf.add(new Vector3f(1, 2, 3), new Vector3f(3, 4, 5), dest, 0);
        assertArrayEquals(new float[]{4, 6, 8, 0}, dest, F);
    }
    @Test void vmfSubDest() {
        float[] dest = new float[3];
        VectorMathf.sub(new Vector3f(5, 6, 7), new Vector3f(3, 4, 5), dest, 0);
        assertArrayEquals(new float[]{2, 2, 2}, dest, F);
    }
    @Test void vmfMulDest() {
        float[] dest = new float[3];
        VectorMathf.mul(new Vector3f(1, 2, 3), new Vector3f(3, 4, 5), dest, 0);
        assertArrayEquals(new float[]{3, 8, 15}, dest, F);
    }
    @Test void vmfScaleDest() {
        float[] dest = new float[3];
        VectorMathf.scale(new Vector3f(1, 2, 3), 3, dest, 0);
        assertArrayEquals(new float[]{3, 6, 9}, dest, F);
    }
    @Test void vmfCrossDest() {
        float[] dest = new float[3];
        VectorMathf.cross(new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), dest, 0);
        assertArrayEquals(new float[]{0, 0, 1}, dest, F);
    }
    @Test void vmfNormalizeDest() {
        float[] dest = new float[3];
        VectorMathf.normalize(new Vector3f(3, 0, 0), dest, 0);
        assertArrayEquals(new float[]{1, 0, 0}, dest, F);
    }
    @Test void vmfLerpDest() {
        float[] dest = new float[3];
        VectorMathf.lerp(new Vector3f(0, 0, 0), new Vector3f(10, 20, 30), 0.5f, dest, 0);
        assertArrayEquals(new float[]{5, 10, 15}, dest, F);
    }
    @Test void vmfDistance() {
        assertEquals(5, VectorMathf.distance(new Vector3f(0, 0, 0), new Vector3f(3, 4, 0)), F);
        assertEquals(25, VectorMathf.distanceSquared(new Vector3f(0, 0, 0), new Vector3f(3, 4, 0)), F);
    }
    @Test void vmfProjectZeroOntoZero() {
        assertEquals(new Vector2f(0.0f, 0.0f), VectorMathf.project(new Vector2f(2.0f, 3.0f), new Vector2f(0.0f, 0.0f)));
        assertEquals(new Vector2f(2.0f, 3.0f), VectorMathf.reject(new Vector2f(2.0f, 3.0f), new Vector2f(0.0f, 0.0f)));
    }
    @Test void vmfClosestPointDegenerateSegment() {
        Vector2f p = new Vector2f(5.0f, -2.0f);
        Vector2f a = new Vector2f(1.0f, 1.0f);
        assertEquals(a, VectorMathf.closestPointOnSegment(p, a, a));
    }
    @Test void vmfRotateAroundZeroAxis() {
        Vector3f v = new Vector3f(1.0f, 2.0f, 3.0f);
        assertEquals(v, VectorMathf.rotateAroundAxis(v, new Vector3f(0.0f, 0.0f, 0.0f), 1.0f));
    }

    // ── VectorMath ────────────────────────────────────────────────────

    @Test void vmAdd() {
        assertEquals(new Vector3d(4, 6, 8), VectorMath.add(new Vector3d(1, 2, 3), new Vector3d(3, 4, 5)));
    }
    @Test void vmAddDest() {
        double[] dest = new double[3];
        VectorMath.add(new Vector3d(1, 2, 3), new Vector3d(3, 4, 5), dest, 0);
        assertArrayEquals(new double[]{4, 6, 8}, dest, D);
    }
    @Test void vmSubDest() {
        double[] dest = new double[3];
        VectorMath.sub(new Vector3d(5, 6, 7), new Vector3d(3, 4, 5), dest, 0);
        assertArrayEquals(new double[]{2, 2, 2}, dest, D);
    }
    @Test void vmMulDest() {
        double[] dest = new double[3];
        VectorMath.mul(new Vector3d(1, 2, 3), new Vector3d(3, 4, 5), dest, 0);
        assertArrayEquals(new double[]{3, 8, 15}, dest, D);
    }
    @Test void vmScaleDest() {
        double[] dest = new double[3];
        VectorMath.scale(new Vector3d(1, 2, 3), 3, dest, 0);
        assertArrayEquals(new double[]{3, 6, 9}, dest, D);
    }
    @Test void vmCrossDest() {
        double[] dest = new double[3];
        VectorMath.cross(new Vector3d(1, 0, 0), new Vector3d(0, 1, 0), dest, 0);
        assertArrayEquals(new double[]{0, 0, 1}, dest, D);
    }
    @Test void vmNormalizeDest() {
        double[] dest = new double[3];
        VectorMath.normalize(new Vector3d(3, 0, 0), dest, 0);
        assertArrayEquals(new double[]{1, 0, 0}, dest, D);
    }
    @Test void vmLerpDest() {
        double[] dest = new double[3];
        VectorMath.lerp(new Vector3d(0, 0, 0), new Vector3d(10, 20, 30), 0.5, dest, 0);
        assertArrayEquals(new double[]{5, 10, 15}, dest, D);
    }
    @Test void vmDistance() {
        assertEquals(5, VectorMath.distance(new Vector3d(0, 0, 0), new Vector3d(3, 4, 0)), D);
        assertEquals(25, VectorMath.distanceSquared(new Vector3d(0, 0, 0), new Vector3d(3, 4, 0)), D);
    }
    @Test void vmProjectZeroOntoZero() {
        assertEquals(new Vector2d(0.0, 0.0), VectorMath.project(new Vector2d(2.0, 3.0), new Vector2d(0.0, 0.0)));
        assertEquals(new Vector2d(2.0, 3.0), VectorMath.reject(new Vector2d(2.0, 3.0), new Vector2d(0.0, 0.0)));
    }
    @Test void vmClosestPointDegenerateSegment() {
        Vector3d p = new Vector3d(5.0, -2.0, 9.0);
        Vector3d a = new Vector3d(1.0, 1.0, 1.0);
        assertEquals(a, VectorMath.closestPointOnSegment(p, a, a));
    }
    @Test void vmRotateAroundZeroAxis() {
        Vector3d v = new Vector3d(1.0, 2.0, 3.0);
        assertEquals(v, VectorMath.rotateAroundAxis(v, new Vector3d(0.0, 0.0, 0.0), 1.0));
    }
    @Test void vmDot() {
        assertEquals(32.0, VectorMath.dot(new Vector3d(1, 2, 3), new Vector3d(4, 5, 6)), D);
    }
    @Test void vmCross() {
        assertEquals(new Vector3d(0, 0, 1), VectorMath.cross(new Vector3d(1, 0, 0), new Vector3d(0, 1, 0)));
    }
}

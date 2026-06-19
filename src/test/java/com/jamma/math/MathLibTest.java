package com.jamma.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MathLibTest {

    @Test
    void sin() {
        assertEquals(0.0, MathLib.sin(0.0), 1e-15);
        assertEquals(1.0, MathLib.sin(Math.PI / 2.0), 1e-15);
    }

    @Test
    void cos() {
        assertEquals(1.0, MathLib.cos(0.0), 1e-15);
        assertEquals(0.0, MathLib.cos(Math.PI / 2.0), 1e-15);
    }

    @Test
    void sqrt() {
        assertEquals(2.0, MathLib.sqrt(4.0), 1e-15);
        assertEquals(0.0, MathLib.sqrt(0.0), 1e-15);
    }

    @Test
    void invSqrt() {
        assertEquals(0.5, MathLib.invSqrt(4.0), 1e-15);
    }

    @Test
    void clamp() {
        assertEquals(5.0, MathLib.clamp(10.0, 0.0, 5.0), 1e-15);
        assertEquals(0.0, MathLib.clamp(-1.0, 0.0, 5.0), 1e-15);
        assertEquals(3.0, MathLib.clamp(3.0, 0.0, 5.0), 1e-15);
    }

    @Test
    void lerp() {
        assertEquals(5.0, MathLib.lerp(0.0, 10.0, 0.5), 1e-15);
        assertEquals(0.0, MathLib.lerp(0.0, 10.0, 0.0), 1e-15);
        assertEquals(10.0, MathLib.lerp(0.0, 10.0, 1.0), 1e-15);
    }

    @Test
    void vectorAdd() {
        Vector3D a = new Vector3D(1.0, 2.0, 3.0);
        Vector3D b = new Vector3D(4.0, 5.0, 6.0);
        Vector3D c = MathLib.add(a, b);
        assertEquals(new Vector3D(5.0, 7.0, 9.0), c);
    }

    @Test
    void vectorSub() {
        Vector3D a = new Vector3D(5.0, 7.0, 9.0);
        Vector3D b = new Vector3D(1.0, 2.0, 3.0);
        Vector3D c = MathLib.sub(a, b);
        assertEquals(new Vector3D(4.0, 5.0, 6.0), c);
    }

    @Test
    void vectorDot() {
        Vector3D a = new Vector3D(1.0, 2.0, 3.0);
        Vector3D b = new Vector3D(4.0, 5.0, 6.0);
        assertEquals(32.0, MathLib.dot(a, b), 1e-15);
    }

    @Test
    void vectorCross() {
        Vector3D a = new Vector3D(1.0, 0.0, 0.0);
        Vector3D b = new Vector3D(0.0, 1.0, 0.0);
        assertEquals(new Vector3D(0.0, 0.0, 1.0), MathLib.cross(a, b));
    }

    @Test
    void vectorLength() {
        Vector3D v = new Vector3D(3.0, 4.0, 0.0);
        assertEquals(5.0, MathLib.length(v), 1e-15);
    }

    @Test
    void vectorNormalize() {
        Vector3D v = new Vector3D(3.0, 4.0, 0.0);
        Vector3D n = MathLib.normalize(v);
        assertEquals(1.0, MathLib.length(n), 1e-15);
    }

    @Test
    void vectorScale() {
        Vector2D v = new Vector2D(2.0, 3.0);
        Vector2D r = MathLib.scale(v, 2.0);
        assertEquals(new Vector2D(4.0, 6.0), r);
    }

    @Test
    void vectorReflect() {
        Vector2D dir = new Vector2D(1.0, -1.0);
        Vector2D normal = new Vector2D(0.0, 1.0);
        Vector2D r = MathLib.reflect(dir, normal);
        assertEquals(new Vector2D(1.0, 1.0), r);
    }

    @Test
    void vectorPerpendicular() {
        Vector2D v = new Vector2D(1.0, 2.0);
        Vector2D p = MathLib.perpendicular(v);
        assertEquals(new Vector2D(-2.0, 1.0), p);
    }

    @Test
    void smoothstep() {
        assertEquals(0.0, MathLib.smoothstep(0.0, 1.0, -1.0), 1e-15);
        assertEquals(1.0, MathLib.smoothstep(0.0, 1.0, 2.0), 1e-15);
    }

    @Test
    void batchAdd() {
        int n = 10;
        Vector3D[] a = new Vector3D[n];
        Vector3D[] b = new Vector3D[n];
        for (int i = 0; i < n; i++) {
            a[i] = new Vector3D(i, i * 2, i * 3);
            b[i] = new Vector3D(n - i, (n - i) * 2, (n - i) * 3);
        }
        Vector3D[] result = MathLib.batchAdd(a, b);
        for (int i = 0; i < n; i++) {
            assertEquals(n, result[i].x() + result[i].y() + result[i].z(), (n * 6));
        }
    }

    @Test
    void vector2D() {
        Vector2D a = new Vector2D(1.0, 2.0);
        Vector2D b = new Vector2D(3.0, 4.0);
        assertEquals(new Vector2D(4.0, 6.0), MathLib.add(a, b));
        assertEquals(11.0, MathLib.dot(a, b), 1e-15);
    }

    @Test
    void vector4D() {
        Vector4D a = new Vector4D(1.0, 2.0, 3.0, 4.0);
        Vector4D b = new Vector4D(5.0, 6.0, 7.0, 8.0);
        assertEquals(new Vector4D(6.0, 8.0, 10.0, 12.0), MathLib.add(a, b));
    }

    @Test
    void constants() {
        assertEquals(Math.PI, MathLib.PI, 1e-15);
        assertEquals(Math.E, MathLib.E, 1e-15);
    }
}

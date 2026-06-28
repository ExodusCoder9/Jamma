package com.jamma.math.matrix;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.jamma.math.Vector2d;

class Matrix2dTest {

    @Test
    void identity() {
        Matrix2d m = new Matrix2d();
        assertEquals(1.0, m.m00());
        assertEquals(0.0, m.m01());
        assertEquals(0.0, m.m10());
        assertEquals(1.0, m.m11());
    }

    @Test
    void zero() {
        Matrix2d m = new Matrix2d().zero();
        assertEquals(0.0, m.m00());
        assertEquals(0.0, m.m01());
        assertEquals(0.0, m.m10());
        assertEquals(0.0, m.m11());
    }

    @Test
    void rotate() {
        Matrix2d m = new Matrix2d();
        m.rotate(Math.PI / 2.0);
        Vector2d result = m.transform(new Vector2d(1, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(1.0, result.y(), 1e-15);
    }

    @Test
    void scaleXY() {
        Matrix2d m = new Matrix2d();
        m.scale(2.0, 3.0);
        Vector2d result = m.transform(new Vector2d(1, 1));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
    }

    @Test
    void scaleUniform() {
        Matrix2d m = new Matrix2d();
        m.scale(5.0);
        Vector2d result = m.transform(new Vector2d(1, 1));
        assertEquals(5.0, result.x(), 1e-15);
        assertEquals(5.0, result.y(), 1e-15);
    }

    @Test
    void multiply() {
        Matrix2d rot = new Matrix2d();
        rot.rotate(Math.PI / 2.0);
        Matrix2d scale = new Matrix2d();
        scale.scale(2.0, 3.0);
        rot.multiply(scale);
        Vector2d result = rot.transform(new Vector2d(1, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(2.0, result.y(), 1e-15);
    }

    @Test
    void determinant() {
        Matrix2d m = new Matrix2d();
        assertEquals(1.0, m.determinant(), 1e-15);
    }

    @Test
    void determinantScale() {
        Matrix2d m = new Matrix2d();
        m.scale(2.0, 3.0);
        assertEquals(6.0, m.determinant(), 1e-15);
    }

    @Test
    void invert() {
        Matrix2d m = new Matrix2d();
        m.rotate(Math.PI / 4.0);
        Matrix2d inv = new Matrix2d(m);
        inv.invert();
        Matrix2d result = m.multiply(inv);
        assertEquals(1.0, result.m00(), 1e-15);
        assertEquals(0.0, result.m01(), 1e-15);
        assertEquals(0.0, result.m10(), 1e-15);
        assertEquals(1.0, result.m11(), 1e-15);
    }

    @Test
    void transposeTwiceIdentity() {
        Matrix2d m = new Matrix2d(new double[]{1, 2, 3, 4});
        Matrix2d original = new Matrix2d(m);
        m.transpose().transpose();
        assertEquals(original, m);
    }

    @Test
    void transpose() {
        Matrix2d m = new Matrix2d(new double[]{1, 2, 3, 4});
        m.transpose();
        assertEquals(3.0, m.m01());
        assertEquals(2.0, m.m10());
    }

    @Test
    void trace() {
        Matrix2d m = new Matrix2d();
        assertEquals(2.0, m.trace(), 1e-15);
    }

    @Test
    void add() {
        Matrix2d a = new Matrix2d(new double[]{1, 2, 3, 4});
        Matrix2d b = new Matrix2d(new double[]{4, 3, 2, 1});
        a.add(b);
        assertEquals(5.0, a.m00());
        assertEquals(5.0, a.m01());
        assertEquals(5.0, a.m10());
        assertEquals(5.0, a.m11());
    }

    @Test
    void sub() {
        Matrix2d a = new Matrix2d(new double[]{1, 2, 3, 4});
        Matrix2d b = new Matrix2d(new double[]{1, 2, 3, 4});
        a.sub(b);
        assertEquals(0.0, a.m00());
        assertEquals(0.0, a.m01());
        assertEquals(0.0, a.m10());
        assertEquals(0.0, a.m11());
    }

    @Test
    void transformVector() {
        Matrix2d m = new Matrix2d();
        m.scale(2.0, 3.0);
        Vector2d result = m.transform(new Vector2d(1, 1));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
    }

    @Test
    void transformDest() {
        Matrix2d m = new Matrix2d();
        m.scale(2.0, 3.0);
        Vector2d result = m.transform(new Vector2d(1, 1));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
    }

    @Test
    void gettersSetters() {
        Matrix2d m = new Matrix2d();
        m.m00(1).m01(2).m10(3).m11(4);
        assertEquals(1.0, m.m00());
        assertEquals(2.0, m.m01());
        assertEquals(3.0, m.m10());
        assertEquals(4.0, m.m11());
    }

    @Test
    void getArray() {
        Matrix2d m = new Matrix2d(new double[]{1, 2, 3, 4});
        double[] arr = new double[4];
        m.get(arr);
        assertArrayEquals(new double[]{1, 2, 3, 4}, arr, 1e-15);
    }

    @Test
    void getArrayWithOffset() {
        Matrix2d m = new Matrix2d(new double[]{1, 2, 3, 4});
        double[] arr = new double[5];
        m.get(arr, 1);
        assertEquals(1.0, arr[1], 1e-15);
        assertEquals(4.0, arr[4], 1e-15);
    }

    @Test
    void copyConstructor() {
        Matrix2d a = new Matrix2d(new double[]{1, 2, 3, 4});
        Matrix2d b = new Matrix2d(a);
        assertEquals(a, b);
        a.m00(99);
        assertNotEquals(a, b);
    }

    @Test
    void arrayConstructor() {
        Matrix2d m = new Matrix2d(new double[]{1, 2, 3, 4});
        assertEquals(1.0, m.m00());
        assertEquals(2.0, m.m01());
        assertEquals(3.0, m.m10());
        assertEquals(4.0, m.m11());
    }

    @Test
    void equalsAndHashCode() {
        Matrix2d a = new Matrix2d(new double[]{1, 2, 3, 4});
        Matrix2d b = new Matrix2d(new double[]{1, 2, 3, 4});
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Matrix2d());
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void testToString() {
        Matrix2d m = new Matrix2d();
        String s = m.toString();
        assertNotNull(s);
        assertTrue(s.length() > 0);
    }

    @Test
    void scaleByVector2d() {
        Matrix2d m = new Matrix2d();
        m.scale(new Vector2d(2, 3));
        Vector2d result = m.transform(new Vector2d(1, 1));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
    }

    @Test
    void invertSingular() {
        Matrix2d m = new Matrix2d();
        m.zero();
        assertThrows(ArithmeticException.class, m::invert);
    }

    @Test
    void rotateNegativeAngle() {
        Matrix2d m = new Matrix2d();
        m.rotate(-Math.PI / 2.0);
        Vector2d result = m.transform(new Vector2d(1, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(-1.0, result.y(), 1e-15);
    }
}

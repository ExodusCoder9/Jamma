package com.jamma.math.matrix;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.PI;
import org.junit.jupiter.api.Test;
import com.jamma.math.Vector2f;
import com.jamma.math.Vector2d;

class Matrix3x2Test {

    static final float  ef = 1e-5f;
    static final double ed = 1e-12;

    /* ─── float ─── */

    @Test void f_identity() { assertTrue(new Matrix3x2f().isFinite()); }

    @Test void f_zero() {
        Matrix3x2f m = new Matrix3x2f().zero();
        assertEquals(0.0f, m.m00 + m.m01 + m.m02 + m.m10 + m.m11 + m.m12, ef);
    }

    @Test void f_set_array() {
        Matrix3x2f m = new Matrix3x2f().set(new float[]{2, 0, 5, 0, 3, 7});
        assertEquals(2.0f, m.m00, ef); assertEquals(3.0f, m.m11, ef);
        assertEquals(5.0f, m.m02, ef); assertEquals(7.0f, m.m12, ef);
    }

    @Test void f_set_individual() {
        Matrix3x2f m = new Matrix3x2f().set(1, 2, 3, 4, 5, 6);
        assertEquals(1.0f, m.m00, ef); assertEquals(2.0f, m.m01, ef); assertEquals(3.0f, m.m02, ef);
        assertEquals(4.0f, m.m10, ef); assertEquals(5.0f, m.m11, ef); assertEquals(6.0f, m.m12, ef);
    }

    @Test void f_translate() {
        Vector2f r = new Matrix3x2f().translate(10, 20).transformPosition(new Vector2f(0, 0));
        assertEquals(10.0f, r.x(), ef); assertEquals(20.0f, r.y(), ef);
    }

    @Test void f_scale() {
        Vector2f r = new Matrix3x2f().scale(2, 3).transformPosition(new Vector2f(1, 1));
        assertEquals(2.0f, r.x(), ef); assertEquals(3.0f, r.y(), ef);
    }

    @Test void f_rotate() {
        Vector2f r = new Matrix3x2f().rotate((float) (PI / 2)).transformPosition(new Vector2f(1, 0));
        assertEquals(1.0f, r.y(), ef);
    }

    @Test void f_mul_identity() {
        Matrix3x2f a = new Matrix3x2f().translate(5, 7);
        Matrix3x2f b = new Matrix3x2f();
        a.mul(b);
        Vector2f r = a.transformPosition(new Vector2f(0, 0));
        assertEquals(5.0f, r.x(), ef); assertEquals(7.0f, r.y(), ef);
    }

    @Test void f_mul_scale() {
        Matrix3x2f a = new Matrix3x2f().translate(1, 2);
        Matrix3x2f b = new Matrix3x2f().scale(3, 4);
        a.mul(b);
        Vector2f r = a.transformPosition(new Vector2f(0, 0));
        assertEquals(1.0f, r.x(), ef); assertEquals(2.0f, r.y(), ef);
        Vector2f d = a.transformDirection(new Vector2f(1, 1));
        assertEquals(3.0f, d.x(), ef); assertEquals(4.0f, d.y(), ef);
    }

    @Test void f_determinant() {
        assertEquals(1.0f, new Matrix3x2f().determinant(), ef);
        assertEquals(6.0f, new Matrix3x2f().scale(2, 3).determinant(), ef);
    }

    @Test void f_invert_identity() {
        Matrix3x2f m = new Matrix3x2f().invert();
        Vector2f r = m.transformPosition(new Vector2f(5, 7));
        assertEquals(5.0f, r.x(), ef); assertEquals(7.0f, r.y(), ef);
    }

    @Test void f_invert_translate() {
        Vector2f r = new Matrix3x2f().translate(10, 0).invert().transformPosition(new Vector2f(10, 0));
        assertEquals(0.0f, r.x(), ef); assertEquals(0.0f, r.y(), ef);
    }

    @Test void f_invert_singular() {
        assertThrows(ArithmeticException.class, () -> new Matrix3x2f().zero().invert());
    }

    @Test void f_transformPosition() {
        Vector2f r = new Matrix3x2f().translate(3, 4).transformPosition(new Vector2f(1, 2));
        assertEquals(4.0f, r.x(), ef); assertEquals(6.0f, r.y(), ef);
    }

    @Test void f_transformDirection() {
        Vector2f r = new Matrix3x2f().translate(3, 4).transformDirection(new Vector2f(1, 0));
        assertEquals(1.0f, r.x(), ef); assertEquals(0.0f, r.y(), ef);
    }

    @Test void f_toString() { assertNotNull(new Matrix3x2f().toString()); }

    @Test void f_equals() { assertEquals(new Matrix3x2f(), new Matrix3x2f()); }

    @Test void f_get_array() {
        float[] arr = new float[6];
        new Matrix3x2f().identity().get(arr, 0);
        assertEquals(1.0f, arr[0], ef); assertEquals(1.0f, arr[4], ef);
    }

    @Test void f_get_arrayOffset() {
        float[] dst = new float[10];
        new Matrix3x2f().identity().get(dst, 2);
        assertEquals(1.0f, dst[2], ef); assertEquals(1.0f, dst[6], ef);
    }

    /* ─── double ─── */

    @Test void d_identity() { assertTrue(new Matrix3x2d().isFinite()); }

    @Test void d_zero() {
        Matrix3x2d m = new Matrix3x2d().zero();
        assertEquals(0.0, m.m00 + m.m01 + m.m02 + m.m10 + m.m11 + m.m12, ed);
    }

    @Test void d_set_array() {
        Matrix3x2d m = new Matrix3x2d().set(new double[]{2, 0, 5, 0, 3, 7});
        assertEquals(2.0, m.m00, ed); assertEquals(3.0, m.m11, ed);
        assertEquals(5.0, m.m02, ed); assertEquals(7.0, m.m12, ed);
    }

    @Test void d_set_individual() {
        Matrix3x2d m = new Matrix3x2d().set(1, 2, 3, 4, 5, 6);
        assertEquals(1.0, m.m00, ed); assertEquals(2.0, m.m01, ed); assertEquals(3.0, m.m02, ed);
        assertEquals(4.0, m.m10, ed); assertEquals(5.0, m.m11, ed); assertEquals(6.0, m.m12, ed);
    }

    @Test void d_translate() {
        Vector2d r = new Matrix3x2d().translate(10, 20).transformPosition(new Vector2d(0, 0));
        assertEquals(10.0, r.x(), ed); assertEquals(20.0, r.y(), ed);
    }

    @Test void d_scale() {
        Vector2d r = new Matrix3x2d().scale(2, 3).transformPosition(new Vector2d(1, 1));
        assertEquals(2.0, r.x(), ed); assertEquals(3.0, r.y(), ed);
    }

    @Test void d_rotate() {
        Vector2d r = new Matrix3x2d().rotate(PI / 2).transformPosition(new Vector2d(1, 0));
        assertEquals(1.0, r.y(), ed);
    }

    @Test void d_mul_identity() {
        Matrix3x2d a = new Matrix3x2d().translate(5, 7);
        Matrix3x2d b = new Matrix3x2d();
        a.mul(b);
        Vector2d r = a.transformPosition(new Vector2d(0, 0));
        assertEquals(5.0, r.x(), ed); assertEquals(7.0, r.y(), ed);
    }

    @Test void d_mul_scale() {
        Matrix3x2d a = new Matrix3x2d().translate(1, 2);
        Matrix3x2d b = new Matrix3x2d().scale(3, 4);
        a.mul(b);
        Vector2d r = a.transformPosition(new Vector2d(0, 0));
        assertEquals(1.0, r.x(), ed); assertEquals(2.0, r.y(), ed);
        Vector2d d = a.transformDirection(new Vector2d(1, 1));
        assertEquals(3.0, d.x(), ed); assertEquals(4.0, d.y(), ed);
    }

    @Test void d_determinant() {
        assertEquals(1.0, new Matrix3x2d().determinant(), ed);
        assertEquals(6.0, new Matrix3x2d().scale(2, 3).determinant(), ed);
    }

    @Test void d_invert_identity() {
        Matrix3x2d m = new Matrix3x2d().invert();
        Vector2d r = m.transformPosition(new Vector2d(5, 7));
        assertEquals(5.0, r.x(), ed); assertEquals(7.0, r.y(), ed);
    }

    @Test void d_invert_translate() {
        Vector2d r = new Matrix3x2d().translate(10, 0).invert().transformPosition(new Vector2d(10, 0));
        assertEquals(0.0, r.x(), ed); assertEquals(0.0, r.y(), ed);
    }

    @Test void d_invert_singular() {
        assertThrows(ArithmeticException.class, () -> new Matrix3x2d().zero().invert());
    }

    @Test void d_transformPosition() {
        Vector2d r = new Matrix3x2d().translate(3, 4).transformPosition(new Vector2d(1, 2));
        assertEquals(4.0, r.x(), ed); assertEquals(6.0, r.y(), ed);
    }

    @Test void d_transformDirection() {
        Vector2d r = new Matrix3x2d().translate(3, 4).transformDirection(new Vector2d(1, 0));
        assertEquals(1.0, r.x(), ed); assertEquals(0.0, r.y(), ed);
    }

    @Test void d_toString() { assertNotNull(new Matrix3x2d().toString()); }

    @Test void d_equals() { assertEquals(new Matrix3x2d(), new Matrix3x2d()); }

    @Test void d_get_array() {
        double[] arr = new double[6];
        new Matrix3x2d().identity().get(arr, 0);
        assertEquals(1.0, arr[0], ed); assertEquals(1.0, arr[4], ed);
    }

    @Test void d_get_arrayOffset() {
        double[] dst = new double[10];
        new Matrix3x2d().identity().get(dst, 2);
        assertEquals(1.0, dst[2], ed); assertEquals(1.0, dst[6], ed);
    }
}

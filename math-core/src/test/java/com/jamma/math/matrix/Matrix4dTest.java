package com.jamma.math.matrix;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.PI;
import org.junit.jupiter.api.Test;
import com.jamma.math.Vector3d;
import com.jamma.math.Vector4d;
import com.jamma.math.quaternion.Quaterniond;

class Matrix4dTest {

    @Test void identity() { assertTrue(new Matrix4d().isIdentity()); }

    @Test void zero() {
        Matrix4d m = new Matrix4d().zero();
        assertEquals(0.0, m.m00 + m.m01 + m.m02 + m.m03 + m.m10 + m.m11 + m.m12 + m.m13 + m.m20 + m.m21 + m.m22 + m.m23 + m.m30 + m.m31 + m.m32 + m.m33, 1e-12);
    }

    @Test void set_array() {
        Matrix4d m = new Matrix4d().set(new double[]{1,0,0,0, 0,2,0,0, 0,0,3,0, 0,0,0,1});
        assertEquals(1.0, m.m00, 1e-12); assertEquals(2.0, m.m11, 1e-12); assertEquals(3.0, m.m22, 1e-12); assertEquals(1.0, m.m33, 1e-12);
    }

    @Test void translation() {
        Matrix4d m = new Matrix4d().translate(3, 4, 5);
        assertEquals(3.0, m.m30, 1e-12); assertEquals(4.0, m.m31, 1e-12); assertEquals(5.0, m.m32, 1e-12);
    }

    @Test void translation_vector() {
        Matrix4d m = new Matrix4d().translate(new Vector3d(3, 4, 5));
        assertEquals(3.0, m.m30, 1e-12); assertEquals(4.0, m.m31, 1e-12); assertEquals(5.0, m.m32, 1e-12);
    }

    @Test void rotateX() {
        Vector3d r = new Matrix4d().rotateX(PI / 2).transformPosition(new Vector3d(0, 1, 0));
        assertEquals(0.0, r.y(), 1e-12); assertEquals(1.0, r.z(), 1e-12);
    }

    @Test void rotateY() {
        Vector3d r = new Matrix4d().rotateY(PI / 2).transformPosition(new Vector3d(1, 0, 0));
        assertEquals(0.0, r.x(), 1e-12); assertEquals(-1.0, r.z(), 1e-12);
    }

    @Test void rotateZ() {
        Vector3d r = new Matrix4d().rotateZ(PI / 2).transformPosition(new Vector3d(1, 0, 0));
        assertEquals(1.0, r.y(), 1e-12);
    }

    @Test void rotate_axisAngle() {
        Vector3d r = new Matrix4d().rotate(PI / 2, 0, 1, 0).transformPosition(new Vector3d(1, 0, 0));
        assertEquals(0.0, r.x(), 1e-12); assertEquals(-1.0, r.z(), 1e-12);
    }

    @Test void rotate_quaternion() {
        double s = Math.sin(PI / 4), c = Math.cos(PI / 4);
        Vector3d r = new Matrix4d().rotate(new Quaterniond(0, s, 0, c)).transformPosition(new Vector3d(1, 0, 0));
        assertEquals(-1.0, r.z(), 1e-12);
    }

    @Test void scale() {
        Vector3d r = new Matrix4d().scale(2, 3, 4).transformPosition(new Vector3d(1, 1, 1));
        assertEquals(2.0, r.x(), 1e-12); assertEquals(3.0, r.y(), 1e-12); assertEquals(4.0, r.z(), 1e-12);
    }

    @Test void scale_uniform() {
        Vector3d r = new Matrix4d().scale(5).transformPosition(new Vector3d(1, 2, 3));
        assertEquals(5.0, r.x(), 1e-12); assertEquals(10.0, r.y(), 1e-12); assertEquals(15.0, r.z(), 1e-12);
    }

    @Test void scale_vector() {
        Vector3d r = new Matrix4d().scale(new Vector3d(2, 3, 4)).transformPosition(new Vector3d(1, 1, 1));
        assertEquals(2.0, r.x(), 1e-12); assertEquals(3.0, r.y(), 1e-12); assertEquals(4.0, r.z(), 1e-12);
    }

    @Test void perspective() {
        Matrix4d m = new Matrix4d().perspective(PI / 2, 1, 0.1, 100);
        double f = 1.0 / Math.tan(PI / 4);
        assertEquals(f / 1.0, m.m00, 1e-12); assertEquals(f, m.m11, 1e-12); assertEquals(-1.0, m.m23, 1e-12); assertEquals(0.0, m.m33, 1e-12);
    }

    @Test void ortho() {
        Matrix4d m = new Matrix4d().ortho(-1, 1, -1, 1, 0, 10);
        Vector3d p = m.transformPosition(new Vector3d(-1, -1, 0));
        assertEquals(-1.0, p.x(), 1e-12); assertEquals(-1.0, p.y(), 1e-12);
    }

    @Test void lookAt() {
        Vector3d p = new Matrix4d().lookAt(0, 0, 5, 0, 0, 0, 0, 1, 0).transformPosition(new Vector3d(0, 0, 0));
        assertEquals(-5.0, p.z(), 1e-12);
    }

    @Test void lookAt_right() {
        Vector3d p = new Matrix4d().lookAt(0, 0, 5, 0, 0, 0, 0, 1, 0).transformPosition(new Vector3d(1, 0, 5));
        assertEquals(1.0, p.x(), 1e-12);
    }

    @Test void multiply() {
        Vector3d p = new Matrix4d().translate(1, 0, 0).multiply(new Matrix4d().scale(2, 2, 2)).transformPosition(new Vector3d(0, 0, 0));
        assertEquals(1.0, p.x(), 1e-12); assertEquals(0.0, p.y(), 1e-12); assertEquals(0.0, p.z(), 1e-12);
    }

    @Test void mulAffine() {
        Vector3d p = new Matrix4d().translate(1, 0, 0).mulAffine(new Matrix4d().scale(2, 2, 2)).transformPosition(new Vector3d(0, 0, 0));
        assertEquals(1.0, p.x(), 1e-12);
    }

    @Test void determinant() {
        assertEquals(1.0, new Matrix4d().determinant(), 1e-12);
        assertEquals(2.0, new Matrix4d().scale(2, 1, 1).determinant(), 1e-12);
    }

    @Test void invert() {
        Vector3d p = new Matrix4d().translate(5, 0, 0).invert().transformPosition(new Vector3d(5, 0, 0));
        assertEquals(0.0, p.x(), 1e-12); assertEquals(0.0, p.y(), 1e-12); assertEquals(0.0, p.z(), 1e-12);
    }

    @Test void invertAffine() {
        Vector3d p = new Matrix4d().translate(5, 0, 0).invertAffine().transformPosition(new Vector3d(5, 0, 0));
        assertEquals(0.0, p.x(), 1e-12); assertEquals(0.0, p.y(), 1e-12); assertEquals(0.0, p.z(), 1e-12);
    }

    @Test void invert_roundTrip() {
        Matrix4d m = new Matrix4d().translate(2, 3, 4).rotateY(PI / 3).scale(2, 3, 4);
        Matrix4d inv = new Matrix4d(m).invert();
        Matrix4d result = new Matrix4d(m).multiply(inv);
        assertTrue(result.isIdentity(1e-12));
    }

    @Test void invert_singular() { assertThrows(ArithmeticException.class, () -> new Matrix4d().zero().invert()); }

    @Test void transpose() {
        Matrix4d m = new Matrix4d().set(new double[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16}).transpose();
        assertEquals(2.0, m.m10, 1e-12); assertEquals(5.0, m.m01, 1e-12);
    }

    @Test void transpose3x3() {
        Matrix4d m = new Matrix4d().set(new double[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16}).transpose3x3();
        assertEquals(2.0, m.m10, 1e-12); assertEquals(5.0, m.m01, 1e-12);
        assertEquals(16.0, m.m33, 1e-12);
    }

    @Test void transposeTwiceIdentity() {
        Matrix4d m = new Matrix4d().set(new double[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
        Matrix4d original = new Matrix4d(m);
        m.transpose().transpose();
        assertEquals(original, m);
    }

    @Test void adjugate() {
        Matrix4d m = new Matrix4d().set(new double[]{1,0,0,0,0,2,0,0,0,0,3,0,0,0,0,4}).adjugate();
        assertEquals(24.0, m.m00, 1e-10); assertEquals(12.0, m.m11, 1e-10); assertEquals(8.0, m.m22, 1e-10); assertEquals(6.0, m.m33, 1e-10);
    }

    @Test void trace() { assertEquals(4.0, new Matrix4d().trace(), 1e-12); }

    @Test void transform_vector4() {
        Vector4d r = new Matrix4d().transform(new Vector4d(1, 2, 3, 4));
        assertEquals(1.0, r.x(), 1e-12); assertEquals(2.0, r.y(), 1e-12); assertEquals(3.0, r.z(), 1e-12); assertEquals(4.0, r.w(), 1e-12);
    }

    @Test void transformPosition() {
        Vector3d r = new Matrix4d().translate(5, 0, 0).transformPosition(new Vector3d(1, 2, 3));
        assertEquals(6.0, r.x(), 1e-12); assertEquals(2.0, r.y(), 1e-12); assertEquals(3.0, r.z(), 1e-12);
    }

    @Test void transformDirection() {
        Vector3d r = new Matrix4d().translate(5, 0, 0).transformDirection(new Vector3d(1, 0, 0));
        assertEquals(1.0, r.x(), 1e-12); assertEquals(0.0, r.y(), 1e-12); assertEquals(0.0, r.z(), 1e-12);
    }

    @Test void transformProject() {
        Vector3d r = new Matrix4d().perspective(PI / 2, 1, 0.1, 100).transformProject(new Vector3d(0, 0, -2));
        assertEquals(0.0, r.x(), 1e-12); assertEquals(0.0, r.y(), 1e-12);
    }

    @Test void isAffine() {
        assertTrue(new Matrix4d().isAffine());
        assertFalse(new Matrix4d().perspective(PI / 2, 1, 0.1, 100).isAffine());
    }

    @Test void isIdentity() {
        assertTrue(new Matrix4d().isIdentity());
        assertFalse(new Matrix4d().translate(1, 0, 0).isIdentity());
    }

    @Test void isIdentity_epsilon() {
        Matrix4d m = new Matrix4d();
        m.m00(1.0 + 1e-14);
        assertTrue(m.isIdentity(1e-12));
        assertFalse(m.isIdentity(1e-15));
    }

    @Test void rotateZeroAxisNoop() {
        Matrix4d m = new Matrix4d();
        Matrix4d original = new Matrix4d(m);
        m.rotate(PI / 2, 0.0, 0.0, 0.0);
        assertEquals(original, m);
    }

    @Test void lerp() {
        Matrix4d a = new Matrix4d();
        Matrix4d b = new Matrix4d().translate(3, 0, 0);
        Matrix4d r = a.lerp(b, 0.5);
        assertEquals(1.0, r.m00, 1e-12); assertEquals(1.5, r.m30, 1e-12);
    }

    @Test void add() {
        Matrix4d r = new Matrix4d().add(new Matrix4d());
        assertEquals(2.0, r.m00, 1e-12); assertEquals(2.0, r.m11, 1e-12); assertEquals(2.0, r.m22, 1e-12); assertEquals(2.0, r.m33, 1e-12);
    }

    @Test void sub() {
        Matrix4d r = new Matrix4d().scale(2, 2, 2).sub(new Matrix4d().scale(1, 1, 1));
        assertEquals(1.0, r.m00, 1e-12); assertEquals(1.0, r.m11, 1e-12); assertEquals(1.0, r.m22, 1e-12);
    }

    @Test void mulComponentWise() {
        Matrix4d r = new Matrix4d().set(new double[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16}).mulComponentWise(new Matrix4d().set(new double[]{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}));
        assertEquals(2.0, r.m00, 1e-12); assertEquals(4.0, r.m01, 1e-12); assertEquals(6.0, r.m02, 1e-12); assertEquals(8.0, r.m03, 1e-12);
    }

    @Test void get_set() {
        Matrix4d m = new Matrix4d();
        assertEquals(1.0, m.m00(), 1e-12);
        m.m00(42.0);
        assertEquals(42.0, m.m00(), 1e-12);
    }

    @Test void get_array() {
        double[] arr = new Matrix4d().identity().get(new double[16]);
        assertEquals(1.0, arr[0], 1e-12); assertEquals(1.0, arr[5], 1e-12); assertEquals(1.0, arr[10], 1e-12); assertEquals(1.0, arr[15], 1e-12);
    }

    @Test void get3x3() {
        double[] arr = new Matrix4d().identity().get3x3(new double[9], 0);
        assertEquals(1.0, arr[0], 1e-12); assertEquals(1.0, arr[4], 1e-12); assertEquals(1.0, arr[8], 1e-12);
    }

    @Test void row() {
        assertEquals(1.0, new Matrix4d().row(0).x(), 1e-12);
        assertEquals(0.0, new Matrix4d().row(1).x(), 1e-12);
    }

    @Test void column() {
        assertEquals(1.0, new Matrix4d().column(0).x(), 1e-12);
        assertEquals(0.0, new Matrix4d().column(1).x(), 1e-12);
    }

    @Test void hashCode_eq() {
        Matrix4d a = new Matrix4d(), b = new Matrix4d();
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test void equals() { assertEquals(new Matrix4d(), new Matrix4d()); }

    @Test void toString_notNull() { assertNotNull(new Matrix4d().toString()); }

    @Test void reflect() {
        Vector3d r = new Matrix4d().reflect(0, 1, 0).transformPosition(new Vector3d(0, 1, 0));
        assertEquals(0.0, r.x(), 1e-12); assertEquals(-1.0, r.y(), 1e-12); assertEquals(0.0, r.z(), 1e-12);
    }

    @Test void reflect_vector() {
        Vector3d r = new Matrix4d().reflect(new Vector3d(0, 1, 0)).transformPosition(new Vector3d(0, 1, 0));
        assertEquals(0.0, r.x(), 1e-12); assertEquals(-1.0, r.y(), 1e-12); assertEquals(0.0, r.z(), 1e-12);
    }

    @Test void rotateXYZ() {
        Vector3d r = new Matrix4d().rotateXYZ(0.5, 0.3, 0.2).transformPosition(new Vector3d(1, 0, 0));
        assertNotEquals(0.0, r.x() + r.y() + r.z(), 1e-12);
    }

    @Test void rotateZYX() {
        Vector3d r = new Matrix4d().rotateZYX(0.2, 0.3, 0.5).transformPosition(new Vector3d(1, 0, 0));
        assertNotEquals(0.0, r.x() + r.y() + r.z(), 1e-12);
    }

    @Test void rotateYZ() {
        Matrix4d m1 = new Matrix4d().rotateZ(0.3).rotateY(0.5);
        Matrix4d m2 = new Matrix4d().rotateZYX(0.3, 0.5, 0);
        Vector3d p1 = m1.transformPosition(new Vector3d(1, 0, 0));
        Vector3d p2 = m2.transformPosition(new Vector3d(1, 0, 0));
        assertEquals(p1.x(), p2.x(), 1e-12); assertEquals(p1.y(), p2.y(), 1e-12); assertEquals(p1.z(), p2.z(), 1e-12);
    }

    @Test void lookAlong() {
        Vector3d p = new Matrix4d().lookAlong(0, 0, -1, 0, 1, 0).transformPosition(new Vector3d(0, 0, 1));
        assertEquals(0.0, p.x(), 1e-12); assertEquals(0.0, p.y(), 1e-12);
    }

    @Test void normal() {
        Matrix4d n = new Matrix4d().scale(2, 3, 4).normal();
        assertEquals(0.5, n.m00, 1e-12); assertEquals(1.0 / 3.0, n.m11, 1e-12); assertEquals(0.25, n.m22, 1e-12);
    }

    @Test void billboard() {
        Matrix4d m = new Matrix4d().billboard(new Vector3d(0, 0, 0), new Vector3d(0, 0, -1), new Vector3d(0, 1, 0));
        Vector3d p = m.transformPosition(new Vector3d(0, 0, 0));
        assertEquals(0.0, p.x(), 1e-12); assertEquals(0.0, p.y(), 1e-12);
    }

    @Test void frustum() {
        Matrix4d m = new Matrix4d().frustum(-1, 1, -1, 1, 0.1, 100);
        assertEquals(-1.0, m.m23, 1e-12); assertEquals(0.0, m.m33, 1e-12);
    }

    @Test void ortho2D() {
        Matrix4d m = new Matrix4d().ortho2D(0, 800, 600, 0);
        Vector3d p = m.transformPosition(new Vector3d(0, 0, 0));
        assertEquals(-1.0, p.x(), 1e-12); assertEquals(1.0, p.y(), 1e-12);
    }

    @Test void copy_constructor() {
        Matrix4d a = new Matrix4d().translate(2, 3, 4);
        assertEquals(a, new Matrix4d(a));
    }

    @Test void array_constructor() {
        double[] arr = {1,0,0,0, 0,2,0,0, 0,0,3,0, 0,0,0,1};
        Matrix4d m = new Matrix4d(arr);
        double[] out = m.get(new double[16]);
        assertArrayEquals(arr, out, 1e-12);
    }

    @Test void individual_constructor() {
        Matrix4d m = new Matrix4d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        assertEquals(1.0, m.m00, 1e-12); assertEquals(6.0, m.m11, 1e-12); assertEquals(11.0, m.m22, 1e-12); assertEquals(16.0, m.m33, 1e-12);
    }

    @Test void shadow() {
        Matrix4d m = new Matrix4d().shadow(new double[]{0, 1, 0, 0}, 0, 1, 0, 0);
        Vector3d p = m.transformPosition(new Vector3d(1, 1, 0));
        assertEquals(1.0, p.x(), 1e-12); assertEquals(0.0, p.y(), 1e-12);
    }

    @Test void reflection() {
        Vector3d p = new Matrix4d().reflection(0, 1, 0, 0).transformPosition(new Vector3d(0, 1, 0));
        assertEquals(0.0, p.x(), 1e-12); assertEquals(-1.0, p.y(), 1e-12); assertEquals(0.0, p.z(), 1e-12);
    }

    @Test void getTranslation_setTranslation() {
        Matrix4d m = new Matrix4d().setTranslation(3, 4, 5);
        assertEquals(3.0, m.m30, 1e-12); assertEquals(4.0, m.m31, 1e-12); assertEquals(5.0, m.m32, 1e-12);
        assertEquals(3.0, m.getTranslation().x(), 1e-12);
        assertEquals(4.0, m.getTranslation().y(), 1e-12);
        assertEquals(5.0, m.getTranslation().z(), 1e-12);
    }

    @Test void getScale() {
        Vector3d s = new Matrix4d().scale(2, 3, 4).getScale();
        assertEquals(2.0, s.x(), 1e-12); assertEquals(3.0, s.y(), 1e-12); assertEquals(4.0, s.z(), 1e-12);
    }

    @Test void set_other() {
        Matrix4d a = new Matrix4d().translate(5, 6, 7);
        Matrix4d b = new Matrix4d().set(a);
        assertEquals(a, b);
    }

    @Test void set_individual() {
        Matrix4d m = new Matrix4d().set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        assertEquals(1.0, m.m00, 1e-12); assertEquals(2.0, m.m01, 1e-12); assertEquals(3.0, m.m02, 1e-12); assertEquals(4.0, m.m03, 1e-12);
        assertEquals(5.0, m.m10, 1e-12); assertEquals(6.0, m.m11, 1e-12); assertEquals(7.0, m.m12, 1e-12); assertEquals(8.0, m.m13, 1e-12);
        assertEquals(9.0, m.m20, 1e-12); assertEquals(10.0, m.m21, 1e-12); assertEquals(11.0, m.m22, 1e-12); assertEquals(12.0, m.m23, 1e-12);
        assertEquals(13.0, m.m30, 1e-12); assertEquals(14.0, m.m31, 1e-12); assertEquals(15.0, m.m32, 1e-12); assertEquals(16.0, m.m33, 1e-12);
    }

    @Test void setTranslation_vector() {
        Matrix4d m = new Matrix4d().setTranslation(new Vector3d(7, 8, 9));
        assertEquals(7.0, m.m30, 1e-12); assertEquals(8.0, m.m31, 1e-12); assertEquals(9.0, m.m32, 1e-12);
    }

    @Test void get_arrayOffset() {
        double[] dst = new double[20];
        new Matrix4d().get(dst, 2);
        assertEquals(1.0, dst[2], 1e-12); assertEquals(1.0, dst[7], 1e-12); assertEquals(1.0, dst[12], 1e-12); assertEquals(1.0, dst[17], 1e-12);
    }
}

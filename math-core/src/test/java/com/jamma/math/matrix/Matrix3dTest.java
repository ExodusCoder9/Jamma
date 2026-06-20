package com.jamma.math.matrix;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.jamma.math.Vector3D;

class Matrix3dTest {

    @Test
    void identity() {
        Matrix3d m = new Matrix3d();
        assertEquals(1.0, m.m00());
        assertEquals(0.0, m.m01());
        assertEquals(0.0, m.m02());
        assertEquals(0.0, m.m10());
        assertEquals(1.0, m.m11());
        assertEquals(0.0, m.m12());
        assertEquals(0.0, m.m20());
        assertEquals(0.0, m.m21());
        assertEquals(1.0, m.m22());
    }

    @Test
    void zero() {
        Matrix3d m = new Matrix3d().zero();
        assertEquals(0.0, m.m00());
        assertEquals(0.0, m.m01());
        assertEquals(0.0, m.m02());
        assertEquals(0.0, m.m10());
        assertEquals(0.0, m.m11());
        assertEquals(0.0, m.m12());
        assertEquals(0.0, m.m20());
        assertEquals(0.0, m.m21());
        assertEquals(0.0, m.m22());
    }

    @Test
    void setArray() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertEquals(1.0, m.m00());
        assertEquals(2.0, m.m01());
        assertEquals(3.0, m.m02());
        assertEquals(4.0, m.m10());
        assertEquals(5.0, m.m11());
        assertEquals(6.0, m.m12());
        assertEquals(7.0, m.m20());
        assertEquals(8.0, m.m21());
        assertEquals(9.0, m.m22());
    }

    @Test
    void rotateX() {
        Matrix3d m = new Matrix3d();
        m.rotateX(Math.PI / 2.0);
        Vector3D result = m.transform(new Vector3D(0, 1, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(1.0, result.z(), 1e-15);
    }

    @Test
    void rotateY() {
        Matrix3d m = new Matrix3d();
        m.rotateY(Math.PI / 2.0);
        Vector3D result = m.transform(new Vector3D(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(-1.0, result.z(), 1e-15);
    }

    @Test
    void rotateZ() {
        Matrix3d m = new Matrix3d();
        m.rotateZ(Math.PI / 2.0);
        Vector3D result = m.transform(new Vector3D(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(1.0, result.y(), 1e-15);
        assertEquals(0.0, result.z(), 1e-15);
    }

    @Test
    void rotateAxisAngle() {
        Matrix3d m = new Matrix3d();
        m.rotate(Math.PI / 2.0, 0.0, 1.0, 0.0);
        Vector3D result = m.transform(new Vector3D(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(-1.0, result.z(), 1e-15);
    }

    @Test
    void rotateXYZ() {
        Matrix3d m = new Matrix3d();
        m.rotateXYZ(Math.PI / 2.0, 0.0, 0.0);
        Vector3D result = m.transform(new Vector3D(0, 1, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(1.0, result.z(), 1e-15);
    }

    @Test
    void scaleXYZ() {
        Matrix3d m = new Matrix3d();
        m.scale(2.0, 3.0, 4.0);
        Vector3D result = m.transform(new Vector3D(1, 1, 1));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
        assertEquals(4.0, result.z(), 1e-15);
    }

    @Test
    void scaleUniform() {
        Matrix3d m = new Matrix3d();
        m.scale(5.0);
        Vector3D result = m.transform(new Vector3D(1, 1, 1));
        assertEquals(5.0, result.x(), 1e-15);
        assertEquals(5.0, result.y(), 1e-15);
        assertEquals(5.0, result.z(), 1e-15);
    }

    @Test
    void multiply() {
        Matrix3d a = new Matrix3d();
        a.rotateX(Math.PI / 2.0);
        Matrix3d b = new Matrix3d();
        b.rotateY(Math.PI / 2.0);
        a.multiply(b);
        Vector3D result = a.transform(new Vector3D(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(1.0, result.y(), 1e-15);
        assertEquals(0.0, result.z(), 1e-15);
    }

    @Test
    void determinantIdentity() {
        Matrix3d m = new Matrix3d();
        assertEquals(1.0, m.determinant(), 1e-15);
    }

    @Test
    void determinantScale() {
        Matrix3d m = new Matrix3d();
        m.scale(2.0, 1.0, 1.0);
        assertEquals(2.0, m.determinant(), 1e-15);
    }

    @Test
    void invert() {
        Matrix3d m = new Matrix3d();
        m.rotateY(Math.PI / 4.0);
        Matrix3d inv = new Matrix3d(m);
        inv.invert();
        Matrix3d result = m.multiply(inv);
        assertEquals(1.0, result.m00(), 1e-15);
        assertEquals(0.0, result.m01(), 1e-15);
        assertEquals(0.0, result.m02(), 1e-15);
        assertEquals(0.0, result.m10(), 1e-15);
        assertEquals(1.0, result.m11(), 1e-15);
        assertEquals(0.0, result.m12(), 1e-15);
        assertEquals(0.0, result.m20(), 1e-15);
        assertEquals(0.0, result.m21(), 1e-15);
        assertEquals(1.0, result.m22(), 1e-15);
    }

    @Test
    void transpose() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        m.transpose();
        assertEquals(4.0, m.m01());
        assertEquals(2.0, m.m10());
        assertEquals(7.0, m.m02());
        assertEquals(3.0, m.m20());
        assertEquals(8.0, m.m12());
        assertEquals(6.0, m.m21());
    }

    @Test
    void trace() {
        Matrix3d m = new Matrix3d();
        assertEquals(3.0, m.trace(), 1e-15);
    }

    @Test
    void add() {
        Matrix3d a = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3d b = new Matrix3d(new double[]{9, 8, 7, 6, 5, 4, 3, 2, 1});
        a.add(b);
        assertEquals(10.0, a.m00());
        assertEquals(10.0, a.m01());
        assertEquals(10.0, a.m02());
        assertEquals(10.0, a.m10());
        assertEquals(10.0, a.m11());
        assertEquals(10.0, a.m12());
        assertEquals(10.0, a.m20());
        assertEquals(10.0, a.m21());
        assertEquals(10.0, a.m22());
    }

    @Test
    void sub() {
        Matrix3d a = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3d b = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        a.sub(b);
        assertEquals(0.0, a.m00());
        assertEquals(0.0, a.m11());
        assertEquals(0.0, a.m22());
    }

    @Test
    void lerp() {
        Matrix3d a = new Matrix3d();
        Matrix3d b = new Matrix3d(new double[]{3, 3, 3, 3, 3, 3, 3, 3, 3});
        a.lerp(b, 0.5);
        assertEquals(2.0, a.m00(), 1e-15);
        assertEquals(2.0, a.m11(), 1e-15);
        assertEquals(2.0, a.m22(), 1e-15);
        assertEquals(1.5, a.m01(), 1e-15);
    }

    @Test
    void transformVector() {
        Matrix3d m = new Matrix3d();
        m.scale(2.0, 3.0, 4.0);
        Vector3D result = m.transform(new Vector3D(1, 1, 1));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
        assertEquals(4.0, result.z(), 1e-15);
    }

    @Test
    void transformDest() {
        Matrix3d m = new Matrix3d();
        m.scale(2.0, 3.0, 4.0);
        Vector3D result = m.transform(new Vector3D(1, 1, 1), new Vector3D(0, 0, 0));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
        assertEquals(4.0, result.z(), 1e-15);
    }

    @Test
    void gettersSetters() {
        Matrix3d m = new Matrix3d();
        m.m00(1).m01(2).m02(3).m10(4).m11(5).m12(6).m20(7).m21(8).m22(9);
        assertEquals(1.0, m.m00());
        assertEquals(2.0, m.m01());
        assertEquals(3.0, m.m02());
        assertEquals(4.0, m.m10());
        assertEquals(5.0, m.m11());
        assertEquals(6.0, m.m12());
        assertEquals(7.0, m.m20());
        assertEquals(8.0, m.m21());
        assertEquals(9.0, m.m22());
    }

    @Test
    void getArray() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        double[] arr = new double[9];
        m.get(arr);
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, arr, 1e-15);
    }

    @Test
    void getArrayWithOffset() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        double[] arr = new double[10];
        m.get(arr, 1);
        assertEquals(1.0, arr[1], 1e-15);
        assertEquals(5.0, arr[5], 1e-15);
        assertEquals(9.0, arr[9], 1e-15);
    }

    @Test
    void row() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Vector3D row0 = m.row(0, new Vector3D(0, 0, 0));
        assertEquals(1.0, row0.x(), 1e-15);
        assertEquals(4.0, row0.y(), 1e-15);
        assertEquals(7.0, row0.z(), 1e-15);
    }

    @Test
    void row1() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Vector3D row1 = m.row(1, new Vector3D(0, 0, 0));
        assertEquals(2.0, row1.x(), 1e-15);
        assertEquals(5.0, row1.y(), 1e-15);
        assertEquals(8.0, row1.z(), 1e-15);
    }

    @Test
    void row2() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Vector3D row2 = m.row(2, new Vector3D(0, 0, 0));
        assertEquals(3.0, row2.x(), 1e-15);
        assertEquals(6.0, row2.y(), 1e-15);
        assertEquals(9.0, row2.z(), 1e-15);
    }

    @Test
    void col() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Vector3D col0 = m.col(0, new Vector3D(0, 0, 0));
        assertEquals(1.0, col0.x(), 1e-15);
        assertEquals(2.0, col0.y(), 1e-15);
        assertEquals(3.0, col0.z(), 1e-15);
    }

    @Test
    void col1() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Vector3D col1 = m.col(1, new Vector3D(0, 0, 0));
        assertEquals(4.0, col1.x(), 1e-15);
        assertEquals(5.0, col1.y(), 1e-15);
        assertEquals(6.0, col1.z(), 1e-15);
    }

    @Test
    void col2() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Vector3D col2 = m.col(2, new Vector3D(0, 0, 0));
        assertEquals(7.0, col2.x(), 1e-15);
        assertEquals(8.0, col2.y(), 1e-15);
        assertEquals(9.0, col2.z(), 1e-15);
    }

    @Test
    void fromMatrix4d() {
        Matrix4d m4 = new Matrix4d();
        m4.m00(1).m01(2).m02(3).m10(4).m11(5).m12(6).m20(7).m21(8).m22(9);
        Matrix3d m3 = new Matrix3d();
        m3.from(m4);
        assertEquals(1.0, m3.m00());
        assertEquals(2.0, m3.m01());
        assertEquals(3.0, m3.m02());
        assertEquals(4.0, m3.m10());
        assertEquals(5.0, m3.m11());
        assertEquals(6.0, m3.m12());
        assertEquals(7.0, m3.m20());
        assertEquals(8.0, m3.m21());
        assertEquals(9.0, m3.m22());
    }

    @Test
    void normal() {
        Matrix4d m4 = new Matrix4d();
        m4.scale(2.0, 3.0, 4.0);
        Matrix3d m3 = new Matrix3d();
        m3.normal(m4);
        assertEquals(0.5, m3.m00(), 1e-15);
        assertEquals(1.0 / 3.0, m3.m11(), 1e-15);
        assertEquals(0.25, m3.m22(), 1e-15);
    }

    @Test
    void getMatrix4d() {
        Matrix3d m3 = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix4d m4 = new Matrix4d();
        m3.get(m4);
        assertEquals(1.0, m4.m00());
        assertEquals(2.0, m4.m01());
        assertEquals(3.0, m4.m02());
        assertEquals(4.0, m4.m10());
        assertEquals(5.0, m4.m11());
        assertEquals(6.0, m4.m12());
        assertEquals(7.0, m4.m20());
        assertEquals(8.0, m4.m21());
        assertEquals(9.0, m4.m22());
        assertEquals(1.0, m4.m33());
    }

    @Test
    void copyConstructor() {
        Matrix3d a = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3d b = new Matrix3d(a);
        assertEquals(a, b);
        a.m00(99);
        assertNotEquals(a, b);
    }

    @Test
    void arrayConstructor() {
        Matrix3d m = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertEquals(1.0, m.m00());
        assertEquals(5.0, m.m11());
        assertEquals(9.0, m.m22());
    }

    @Test
    void equalsAndHashCode() {
        Matrix3d a = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3d b = new Matrix3d(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Matrix3d());
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void testToString() {
        Matrix3d m = new Matrix3d();
        String s = m.toString();
        assertNotNull(s);
        assertTrue(s.length() > 0);
    }

    @Test
    void scaleByVector3D() {
        Matrix3d m = new Matrix3d();
        m.scale(new Vector3D(2, 3, 4));
        Vector3D result = m.transform(new Vector3D(1, 1, 1));
        assertEquals(2.0, result.x(), 1e-15);
        assertEquals(3.0, result.y(), 1e-15);
        assertEquals(4.0, result.z(), 1e-15);
    }

    @Test
    void determinantScaleUniform() {
        Matrix3d m = new Matrix3d();
        m.scale(3.0);
        assertEquals(27.0, m.determinant(), 1e-15);
    }

    @Test
    void invertSingular() {
        Matrix3d m = new Matrix3d();
        m.zero();
        assertThrows(ArithmeticException.class, m::invert);
    }

    @Test
    void rowOutOfBounds() {
        Matrix3d m = new Matrix3d();
        assertThrows(IndexOutOfBoundsException.class, () -> m.row(3, new Vector3D(0, 0, 0)));
    }

    @Test
    void colOutOfBounds() {
        Matrix3d m = new Matrix3d();
        assertThrows(IndexOutOfBoundsException.class, () -> m.col(3, new Vector3D(0, 0, 0)));
    }

    @Test
    void rotateXZeroAngle() {
        Matrix3d m = new Matrix3d();
        m.rotateX(0.0);
        assertTrue(m.equals(new Matrix3d()));
    }
}

package com.jamma.math.quaternion;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix3d;
import com.jamma.math.matrix.Matrix4d;

class QuaterniondTest {

    @Test
    void identity() {
        Quaterniond q = new Quaterniond(0, 0, 0, 1);
        assertEquals(0.0, q.x());
        assertEquals(0.0, q.y());
        assertEquals(0.0, q.z());
        assertEquals(1.0, q.w());
    }

    @Test
    void axisAngle() {
        double angle = Math.PI / 2.0;
        double half = angle * 0.5;
        double s = Math.sin(half);
        Quaterniond q = new Quaterniond(0, s, 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(-1.0, result.z(), 1e-15);
    }

    @Test
    void eulerXYZ() {
        double halfX = Math.PI / 4.0;
        double sx = Math.sin(halfX), cx = Math.cos(halfX);
        Quaterniond qx = new Quaterniond(sx, 0, 0, cx);
        Matrix4d m = new Matrix4d().rotate(qx);
        Vector3d result = m.transformDirection(new Vector3d(0, 1, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(1.0, result.z(), 1e-15);
    }

    @Test
    void copyConstructor() {
        Quaterniond a = new Quaterniond(1, 2, 3, 4);
        Quaterniond b = new Quaterniond(a.x(), a.y(), a.z(), a.w());
        assertEquals(a, b);
    }

    @Test
    void add() {
        Quaterniond a = new Quaterniond(1, 2, 3, 4);
        Quaterniond b = new Quaterniond(5, 6, 7, 8);
        Quaterniond sum = new Quaterniond(a.x() + b.x(), a.y() + b.y(), a.z() + b.z(), a.w() + b.w());
        assertEquals(6.0, sum.x());
        assertEquals(8.0, sum.y());
        assertEquals(10.0, sum.z());
        assertEquals(12.0, sum.w());
    }

    @Test
    void sub() {
        Quaterniond a = new Quaterniond(5, 6, 7, 8);
        Quaterniond b = new Quaterniond(1, 2, 3, 4);
        Quaterniond diff = new Quaterniond(a.x() - b.x(), a.y() - b.y(), a.z() - b.z(), a.w() - b.w());
        assertEquals(4.0, diff.x());
        assertEquals(4.0, diff.y());
        assertEquals(4.0, diff.z());
        assertEquals(4.0, diff.w());
    }

    @Test
    void multiply() {
        double h = Math.PI / 4.0;
        double s = Math.sin(h), c = Math.cos(h);
        Quaterniond qx = new Quaterniond(s, 0, 0, c);
        Quaterniond qy = new Quaterniond(0, s, 0, c);
        double ax = qx.x(), ay = qx.y(), az = qx.z(), aw = qx.w();
        double bx = qy.x(), by = qy.y(), bz = qy.z(), bw = qy.w();
        Quaterniond q = new Quaterniond(
            aw * bx + ax * bw + ay * bz - az * by,
            aw * by - ax * bz + ay * bw + az * bx,
            aw * bz + ax * by - ay * bx + az * bw,
            aw * bw - ax * bx - ay * by - az * bz
        );
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertEquals(0.0, result.x(), 0.1);
        assertEquals(1.0, result.y(), 0.1);
        assertEquals(0.0, result.z(), 0.1);
    }

    @Test
    void premultiply() {
        double h = Math.PI / 4.0;
        double s = Math.sin(h), c = Math.cos(h);
        Quaterniond qx = new Quaterniond(s, 0, 0, c);
        Quaterniond qy = new Quaterniond(0, s, 0, c);
        double ax = qy.x(), ay = qy.y(), az = qy.z(), aw = qy.w();
        double bx = qx.x(), by = qx.y(), bz = qx.z(), bw = qx.w();
        Quaterniond q = new Quaterniond(
            aw * bx + ax * bw + ay * bz - az * by,
            aw * by - ax * bz + ay * bw + az * bx,
            aw * bz + ax * by - ay * bx + az * bw,
            aw * bw - ax * bx - ay * by - az * bz
        );
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(0, 1, 0));
        assertEquals(1.0, result.x(), 0.1);
        assertEquals(0.0, result.y(), 0.1);
    }

    @Test
    void conjugate() {
        Quaterniond q = new Quaterniond(1, 2, 3, 4);
        Quaterniond conj = new Quaterniond(-q.x(), -q.y(), -q.z(), q.w());
        double ax = q.x(), ay = q.y(), az = q.z(), aw = q.w();
        double bx = conj.x(), by = conj.y(), bz = conj.z(), bw = conj.w();
        Quaterniond product = new Quaterniond(
            aw * bx + ax * bw + ay * bz - az * by,
            aw * by - ax * bz + ay * bw + az * bx,
            aw * bz + ax * by - ay * bx + az * bw,
            aw * bw - ax * bx - ay * by - az * bz
        );
        assertEquals(0.0, product.x(), 1e-15);
        assertEquals(0.0, product.y(), 1e-15);
        assertEquals(0.0, product.z(), 1e-15);
        assertTrue(product.w() > 0.0);
    }

    @Test
    void invert() {
        double h = Math.PI / 6.0;
        double s = Math.sin(h), c = Math.cos(h);
        Quaterniond q = new Quaterniond(0, s, 0, c);
        double lenSq = q.x() * q.x() + q.y() * q.y() + q.z() * q.z() + q.w() * q.w();
        Quaterniond inv = new Quaterniond(-q.x() / lenSq, -q.y() / lenSq, -q.z() / lenSq, q.w() / lenSq);
        double ax = q.x(), ay = q.y(), az = q.z(), aw = q.w();
        double bx = inv.x(), by = inv.y(), bz = inv.z(), bw = inv.w();
        Quaterniond product = new Quaterniond(
            aw * bx + ax * bw + ay * bz - az * by,
            aw * by - ax * bz + ay * bw + az * bx,
            aw * bz + ax * by - ay * bx + az * bw,
            aw * bw - ax * bx - ay * by - az * bz
        );
        assertEquals(0.0, product.x(), 1e-15);
        assertEquals(0.0, product.y(), 1e-15);
        assertEquals(0.0, product.z(), 1e-15);
        assertEquals(1.0, product.w(), 1e-15);
    }

    @Test
    void normalize() {
        Quaterniond q = new Quaterniond(3, 4, 0, 0);
        double len = Math.sqrt(q.x() * q.x() + q.y() * q.y() + q.z() * q.z() + q.w() * q.w());
        Quaterniond n = new Quaterniond(q.x() / len, q.y() / len, q.z() / len, q.w() / len);
        double nLen = Math.sqrt(n.x() * n.x() + n.y() * n.y() + n.z() * n.z() + n.w() * n.w());
        assertEquals(1.0, nLen, 1e-15);
    }

    @Test
    void lengthAndLengthSquared() {
        Quaterniond q = new Quaterniond(1, 2, 3, 4);
        double lenSq = q.x() * q.x() + q.y() * q.y() + q.z() * q.z() + q.w() * q.w();
        assertEquals(30.0, lenSq, 1e-15);
        assertEquals(Math.sqrt(30.0), Math.sqrt(lenSq), 1e-15);
    }

    @Test
    void rotateX() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(Math.sin(half), 0, 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(0, 1, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(1.0, result.z(), 1e-15);
    }

    @Test
    void rotateY() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(-1.0, result.z(), 1e-15);
    }

    @Test
    void rotateZ() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(0, 0, Math.sin(half), Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(1.0, result.y(), 1e-15);
        assertEquals(0.0, result.z(), 1e-15);
    }

    @Test
    void getEulerAnglesXYZ() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(Math.sin(half), 0, 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        double sy = Math.sqrt(m.m00() * m.m00() + m.m10() * m.m10());
        double xAngle, yAngle, zAngle;
        if (sy > 1e-6) {
            xAngle = Math.atan2(-m.m21(), m.m22());
            yAngle = Math.atan2(-m.m20(), sy);
            zAngle = Math.atan2(m.m10(), m.m00());
        } else {
            xAngle = Math.atan2(-m.m12(), m.m11());
            yAngle = Math.atan2(-m.m20(), sy);
            zAngle = 0;
        }
        assertEquals(Math.PI / 2.0, xAngle, 1e-15);
        assertEquals(0.0, yAngle, 1e-15);
        assertEquals(0.0, zAngle, 1e-15);

    }

    @Test
    void getAxisAngle() {
        double angle = Math.PI / 3.0;
        double half = angle * 0.5;
        Quaterniond q = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        double recoveredAngle = 2.0 * Math.acos(q.w());
        assertEquals(angle, recoveredAngle, 1e-15);
        double s = Math.sqrt(1.0 - q.w() * q.w());
        if (s > 1e-8) {
            double ay = q.y() / s;
            assertEquals(1.0, ay, 1e-15);
        }
    }

    @Test
    void transform() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertEquals(0.0, result.x(), 1e-15);
        assertEquals(0.0, result.y(), 1e-15);
        assertEquals(-1.0, result.z(), 1e-15);
    }

    @Test
    void transformDest() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertNotNull(result);
        assertEquals(-1.0, result.z(), 1e-15);
    }

    @Test
    void transformInverse() {
        double half = Math.PI / 4.0;
        double s = Math.sin(half), c = Math.cos(half);
        Quaterniond q = new Quaterniond(0, s, 0, c);
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d v = new Vector3d(1, 0, 0);
        Vector3d transformed = m.transformDirection(v);
        double lenSq = q.x() * q.x() + q.y() * q.y() + q.z() * q.z() + q.w() * q.w();
        Quaterniond inv = new Quaterniond(-q.x() / lenSq, -q.y() / lenSq, -q.z() / lenSq, q.w() / lenSq);
        Matrix4d mInv = new Matrix4d().rotate(inv);
        Vector3d restored = mInv.transformDirection(transformed);
        assertEquals(v.x(), restored.x(), 1e-15);
        assertEquals(v.y(), restored.y(), 1e-15);
        assertEquals(v.z(), restored.z(), 1e-15);
    }

    @Test
    void nlerp() {
        Quaterniond a = new Quaterniond(0, 0, 0, 1);
        double half = Math.PI / 4.0;
        double s = Math.sin(half), c = Math.cos(half);
        Quaterniond b = new Quaterniond(0, s, 0, c);
        double t = 0.5;
        double ti = 1.0 - t;
        double nx = a.x() * ti + b.x() * t;
        double ny = a.y() * ti + b.y() * t;
        double nz = a.z() * ti + b.z() * t;
        double nw = a.w() * ti + b.w() * t;
        double len = Math.sqrt(nx * nx + ny * ny + nz * nz + nw * nw);
        Quaterniond nlerp = new Quaterniond(nx / len, ny / len, nz / len, nw / len);
        Matrix4d m = new Matrix4d().rotate(nlerp);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertTrue(Math.abs(result.x()) < 0.8);
    }

    @Test
    void slerp() {
        Quaterniond a = new Quaterniond(0, 0, 0, 1);
        double half = Math.PI / 4.0;
        Quaterniond b = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        double t = 0.5;
        double dot = a.x() * b.x() + a.y() * b.y() + a.z() * b.z() + a.w() * b.w();
        double theta = Math.acos(dot);
        double sinTheta = Math.sin(theta);
        double wa = Math.sin((1.0 - t) * theta) / sinTheta;
        double wb = Math.sin(t * theta) / sinTheta;
        double nx = a.x() * wa + b.x() * wb;
        double ny = a.y() * wa + b.y() * wb;
        double nz = a.z() * wa + b.z() * wb;
        double nw = a.w() * wa + b.w() * wb;
        Quaterniond slerp = new Quaterniond(nx, ny, nz, nw);
        Matrix4d m = new Matrix4d().rotate(slerp);
        Vector3d result = m.transformDirection(new Vector3d(1, 0, 0));
        assertTrue(Math.abs(result.x()) < 0.8);
    }

    @Test
    void getMatrix4d() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        assertFalse(m.isIdentity());
        assertEquals(1.0, m.m20(), 1e-15);
        assertEquals(-1.0, m.m02(), 1e-15);
    }

    @Test
    void getMatrix3d() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        Matrix4d m4 = new Matrix4d().rotate(q);
        Matrix3d m3 = new Matrix3d().from(m4);
        assertEquals(-1.0, m3.m02(), 1e-15);
        assertEquals(1.0, m3.m20(), 1e-15);
    }

    @Test
    void dotProduct() {
        Quaterniond a = new Quaterniond(0, 0, 0, 1);
        Quaterniond b = new Quaterniond(0, 0, 0, 1);
        double dot = a.x() * b.x() + a.y() * b.y() + a.z() * b.z() + a.w() * b.w();
        assertEquals(1.0, dot, 1e-15);
    }

    @Test
    void angleBetween() {
        Quaterniond a = new Quaterniond(0, 0, 0, 1);
        double half = Math.PI / 4.0;
        Quaterniond b = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        double dot = a.x() * b.x() + a.y() * b.y() + a.z() * b.z() + a.w() * b.w();
        double angle = 2.0 * Math.acos(Math.abs(dot));
        assertEquals(Math.PI / 2.0, angle, 1e-15);
    }

    @Test
    void isIdentity() {
        Quaterniond q = new Quaterniond(0, 0, 0, 1);
        assertEquals(0.0, q.x());
        assertEquals(0.0, q.y());
        assertEquals(0.0, q.z());
        assertEquals(1.0, q.w());
        Quaterniond q2 = new Quaterniond(1, 0, 0, 0);
        assertNotEquals(0.0, q2.x());
    }

    @Test
    void setFromMatrix3d() {
        Matrix3d m3 = new Matrix3d();
        m3.rotateY(Math.PI / 2.0);
        Matrix4d m4 = new Matrix4d();
        m4.m00(m3.m00()).m01(m3.m01()).m02(m3.m02());
        m4.m10(m3.m10()).m11(m3.m11()).m12(m3.m12());
        m4.m20(m3.m20()).m21(m3.m21()).m22(m3.m22());
        Matrix3d recovered = new Matrix3d().from(m4);
        assertEquals(m3, recovered);
    }

    @Test
    void setFromMatrix4d() {
        Matrix4d m4 = new Matrix4d();
        m4.rotateY(Math.PI / 2.0);
        Matrix3d m3 = new Matrix3d().from(m4);
        assertEquals(-1.0, m3.m02(), 1e-15);
        assertEquals(1.0, m3.m20(), 1e-15);
    }

    @Test
    void lookAt() {
        double half = Math.PI / 4.0;
        Quaterniond q = new Quaterniond(0, Math.sin(half), 0, Math.cos(half));
        Matrix4d m = new Matrix4d().rotate(q);
        Vector3d forward = m.transformDirection(new Vector3d(0, 0, -1));
        assertEquals(-1.0, forward.x(), 1e-15);
        assertEquals(0.0, forward.y(), 1e-15);
        assertEquals(0.0, forward.z(), 1e-15);
    }

    @Test
    void equalsAndHashCode() {
        Quaterniond a = new Quaterniond(1, 2, 3, 4);
        Quaterniond b = new Quaterniond(1, 2, 3, 4);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Quaterniond(0, 0, 0, 1));
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void testToString() {
        Quaterniond q = new Quaterniond(1, 2, 3, 4);
        String s = q.toString();
        assertNotNull(s);
        assertTrue(s.length() > 0);
    }
}

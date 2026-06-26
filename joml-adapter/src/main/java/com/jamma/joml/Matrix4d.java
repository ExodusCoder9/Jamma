package com.jamma.joml;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix4d {

    public double m00, m01, m02, m03;
    public double m10, m11, m12, m13;
    public double m20, m21, m22, m23;
    public double m30, m31, m32, m33;

    public Matrix4d() {
        identity();
    }

    public Matrix4d(Matrix4d other) {
        set(other);
    }

    public Matrix4d(com.jamma.math.matrix.Matrix4d jamma) {
        m00 = jamma.m00; m01 = jamma.m01; m02 = jamma.m02; m03 = jamma.m03;
        m10 = jamma.m10; m11 = jamma.m11; m12 = jamma.m12; m13 = jamma.m13;
        m20 = jamma.m20; m21 = jamma.m21; m22 = jamma.m22; m23 = jamma.m23;
        m30 = jamma.m30; m31 = jamma.m31; m32 = jamma.m32; m33 = jamma.m33;
    }

    public com.jamma.math.matrix.Matrix4d toJamma() {
        return new com.jamma.math.matrix.Matrix4d(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public static Matrix4d fromJamma(com.jamma.math.matrix.Matrix4d jamma) {
        return new Matrix4d(jamma);
    }

    public Matrix4d set(Matrix4d other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02; m03 = other.m03;
        m10 = other.m10; m11 = other.m11; m12 = other.m12; m13 = other.m13;
        m20 = other.m20; m21 = other.m21; m22 = other.m22; m23 = other.m23;
        m30 = other.m30; m31 = other.m31; m32 = other.m32; m33 = other.m33;
        return this;
    }

    public Matrix4d identity() {
        m00 = 1.0; m01 = 0.0; m02 = 0.0; m03 = 0.0;
        m10 = 0.0; m11 = 1.0; m12 = 0.0; m13 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 1.0; m23 = 0.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0; m33 = 1.0;
        return this;
    }

    public Matrix4d zero() {
        m00 = 0; m01 = 0; m02 = 0; m03 = 0;
        m10 = 0; m11 = 0; m12 = 0; m13 = 0;
        m20 = 0; m21 = 0; m22 = 0; m23 = 0;
        m30 = 0; m31 = 0; m32 = 0; m33 = 0;
        return this;
    }

    public Matrix4d mul(Matrix4d r) {
        double nm00 = Math.fma(m00, r.m00, Math.fma(m01, r.m10, Math.fma(m02, r.m20, m03 * r.m30)));
        double nm01 = Math.fma(m00, r.m01, Math.fma(m01, r.m11, Math.fma(m02, r.m21, m03 * r.m31)));
        double nm02 = Math.fma(m00, r.m02, Math.fma(m01, r.m12, Math.fma(m02, r.m22, m03 * r.m32)));
        double nm03 = Math.fma(m00, r.m03, Math.fma(m01, r.m13, Math.fma(m02, r.m23, m03 * r.m33)));
        double nm10 = Math.fma(m10, r.m00, Math.fma(m11, r.m10, Math.fma(m12, r.m20, m13 * r.m30)));
        double nm11 = Math.fma(m10, r.m01, Math.fma(m11, r.m11, Math.fma(m12, r.m21, m13 * r.m31)));
        double nm12 = Math.fma(m10, r.m02, Math.fma(m11, r.m12, Math.fma(m12, r.m22, m13 * r.m32)));
        double nm13 = Math.fma(m10, r.m03, Math.fma(m11, r.m13, Math.fma(m12, r.m23, m13 * r.m33)));
        double nm20 = Math.fma(m20, r.m00, Math.fma(m21, r.m10, Math.fma(m22, r.m20, m23 * r.m30)));
        double nm21 = Math.fma(m20, r.m01, Math.fma(m21, r.m11, Math.fma(m22, r.m21, m23 * r.m31)));
        double nm22 = Math.fma(m20, r.m02, Math.fma(m21, r.m12, Math.fma(m22, r.m22, m23 * r.m32)));
        double nm23 = Math.fma(m20, r.m03, Math.fma(m21, r.m13, Math.fma(m22, r.m23, m23 * r.m33)));
        double nm30 = Math.fma(m30, r.m00, Math.fma(m31, r.m10, Math.fma(m32, r.m20, m33 * r.m30)));
        double nm31 = Math.fma(m30, r.m01, Math.fma(m31, r.m11, Math.fma(m32, r.m21, m33 * r.m31)));
        double nm32 = Math.fma(m30, r.m02, Math.fma(m31, r.m12, Math.fma(m32, r.m22, m33 * r.m32)));
        double nm33 = Math.fma(m30, r.m03, Math.fma(m31, r.m13, Math.fma(m32, r.m23, m33 * r.m33)));
        m00 = nm00; m01 = nm01; m02 = nm02; m03 = nm03;
        m10 = nm10; m11 = nm11; m12 = nm12; m13 = nm13;
        m20 = nm20; m21 = nm21; m22 = nm22; m23 = nm23;
        m30 = nm30; m31 = nm31; m32 = nm32; m33 = nm33;
        return this;
    }

    public Matrix4d translate(double x, double y, double z) {
        m03 = Math.fma(m00, x, Math.fma(m01, y, Math.fma(m02, z, m03)));
        m13 = Math.fma(m10, x, Math.fma(m11, y, Math.fma(m12, z, m13)));
        m23 = Math.fma(m20, x, Math.fma(m21, y, Math.fma(m22, z, m23)));
        m33 = Math.fma(m30, x, Math.fma(m31, y, Math.fma(m32, z, m33)));
        return this;
    }

    public Matrix4d translate(Vector3d v) {
        return translate(v.x, v.y, v.z);
    }

    public Matrix4d scale(double x, double y, double z) {
        m00 *= x; m01 *= y; m02 *= z;
        m10 *= x; m11 *= y; m12 *= z;
        m20 *= x; m21 *= y; m22 *= z;
        m30 *= x; m31 *= y; m32 *= z;
        return this;
    }

    public Matrix4d scale(double s) {
        return scale(s, s, s);
    }

    public Matrix4d transpose() {
        double nm00 = m00; double nm01 = m10; double nm02 = m20; double nm03 = m30;
        double nm10 = m01; double nm11 = m11; double nm12 = m21; double nm13 = m31;
        double nm20 = m02; double nm21 = m12; double nm22 = m22; double nm23 = m32;
        double nm30 = m03; double nm31 = m13; double nm32 = m23; double nm33 = m33;
        m00 = nm00; m01 = nm01; m02 = nm02; m03 = nm03;
        m10 = nm10; m11 = nm11; m12 = nm12; m13 = nm13;
        m20 = nm20; m21 = nm21; m22 = nm22; m23 = nm23;
        m30 = nm30; m31 = nm31; m32 = nm32; m33 = nm33;
        return this;
    }

    public double determinant() {
        return (m00 * m11 - m01 * m10) * (m22 * m33 - m23 * m32)
             - (m00 * m12 - m02 * m10) * (m21 * m33 - m23 * m31)
             + (m00 * m13 - m03 * m10) * (m21 * m32 - m22 * m31)
             + (m01 * m12 - m02 * m11) * (m20 * m33 - m23 * m30)
             - (m01 * m13 - m03 * m11) * (m20 * m32 - m22 * m30)
             + (m02 * m13 - m03 * m12) * (m20 * m31 - m21 * m30);
    }

    public Vector3d transformPosition(Vector3d v) {
        double rx = Math.fma(m00, v.x, Math.fma(m01, v.y, Math.fma(m02, v.z, m03)));
        double ry = Math.fma(m10, v.x, Math.fma(m11, v.y, Math.fma(m12, v.z, m13)));
        double rz = Math.fma(m20, v.x, Math.fma(m21, v.y, Math.fma(m22, v.z, m23)));
        double rw = Math.fma(m30, v.x, Math.fma(m31, v.y, Math.fma(m32, v.z, m33)));
        double invW = 1.0 / rw;
        v.x = rx * invW; v.y = ry * invW; v.z = rz * invW;
        return v;
    }

    public Vector3d transformDirection(Vector3d v) {
        double rx = Math.fma(m00, v.x, Math.fma(m01, v.y, m02 * v.z));
        double ry = Math.fma(m10, v.x, Math.fma(m11, v.y, m12 * v.z));
        double rz = Math.fma(m20, v.x, Math.fma(m21, v.y, m22 * v.z));
        v.x = rx; v.y = ry; v.z = rz;
        return v;
    }

    public double[] get(double[] dest) {
        dest[0] = m00; dest[1] = m10; dest[2] = m20; dest[3] = m30;
        dest[4] = m01; dest[5] = m11; dest[6] = m21; dest[7] = m31;
        dest[8] = m02; dest[9] = m12; dest[10] = m22; dest[11] = m32;
        dest[12] = m03; dest[13] = m13; dest[14] = m23; dest[15] = m33;
        return dest;
    }
}

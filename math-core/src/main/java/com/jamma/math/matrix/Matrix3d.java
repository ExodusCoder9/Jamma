package com.jamma.math.matrix;

import com.jamma.math.Vector3D;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Objects;

public class Matrix3d {

    private double m00;
    private double m01;
    private double m02;
    private double m10;
    private double m11;
    private double m12;
    private double m20;
    private double m21;
    private double m22;

    public Matrix3d() {
        identity();
    }

    public Matrix3d(Matrix3d other) {
        set(other);
    }

    public Matrix3d(double[] m) {
        set(m);
    }

    public Matrix3d set(Matrix3d other) {
        this.m00 = other.m00;
        this.m01 = other.m01;
        this.m02 = other.m02;
        this.m10 = other.m10;
        this.m11 = other.m11;
        this.m12 = other.m12;
        this.m20 = other.m20;
        this.m21 = other.m21;
        this.m22 = other.m22;
        return this;
    }

    public Matrix3d set(double[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m02 = m[2];
        this.m10 = m[3];
        this.m11 = m[4];
        this.m12 = m[5];
        this.m20 = m[6];
        this.m21 = m[7];
        this.m22 = m[8];
        return this;
    }

    public Matrix3d identity() {
        this.m00 = 1.0; this.m01 = 0.0; this.m02 = 0.0;
        this.m10 = 0.0; this.m11 = 1.0; this.m12 = 0.0;
        this.m20 = 0.0; this.m21 = 0.0; this.m22 = 1.0;
        return this;
    }

    public Matrix3d zero() {
        this.m00 = 0.0; this.m01 = 0.0; this.m02 = 0.0;
        this.m10 = 0.0; this.m11 = 0.0; this.m12 = 0.0;
        this.m20 = 0.0; this.m21 = 0.0; this.m22 = 0.0;
        return this;
    }

    public Matrix3d rotate(double angle, double x, double y, double z) {
        double invLen = 1.0 / Math.sqrt(x * x + y * y + z * z);
        double nx = x * invLen;
        double ny = y * invLen;
        double nz = z * invLen;
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double t = 1.0 - c;
        double rm00 = t * nx * nx + c;
        double rm01 = t * nx * ny + s * nz;
        double rm02 = t * nx * nz - s * ny;
        double rm10 = t * nx * ny - s * nz;
        double rm11 = t * ny * ny + c;
        double rm12 = t * ny * nz + s * nx;
        double rm20 = t * nx * nz + s * ny;
        double rm21 = t * ny * nz - s * nx;
        double rm22 = t * nz * nz + c;
        double r00 = Math.fma(m00, rm00, Math.fma(m10, rm01, m20 * rm02));
        double r01 = Math.fma(m01, rm00, Math.fma(m11, rm01, m21 * rm02));
        double r02 = Math.fma(m02, rm00, Math.fma(m12, rm01, m22 * rm02));
        double r10 = Math.fma(m00, rm10, Math.fma(m10, rm11, m20 * rm12));
        double r11 = Math.fma(m01, rm10, Math.fma(m11, rm11, m21 * rm12));
        double r12 = Math.fma(m02, rm10, Math.fma(m12, rm11, m22 * rm12));
        double r20 = Math.fma(m00, rm20, Math.fma(m10, rm21, m20 * rm22));
        double r21 = Math.fma(m01, rm20, Math.fma(m11, rm21, m21 * rm22));
        double r22 = Math.fma(m02, rm20, Math.fma(m12, rm21, m22 * rm22));
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3d rotateX(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double r10 = Math.fma(m10, c, m20 * s);
        double r11 = Math.fma(m11, c, m21 * s);
        double r12 = Math.fma(m12, c, m22 * s);
        double r20 = Math.fma(m20, c, -m10 * s);
        double r21 = Math.fma(m21, c, -m11 * s);
        double r22 = Math.fma(m22, c, -m12 * s);
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3d rotateY(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double r00 = Math.fma(m00, c, -m20 * s);
        double r01 = Math.fma(m01, c, -m21 * s);
        double r02 = Math.fma(m02, c, -m22 * s);
        double r20 = Math.fma(m00, s, m20 * c);
        double r21 = Math.fma(m01, s, m21 * c);
        double r22 = Math.fma(m02, s, m22 * c);
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3d rotateZ(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double r00 = Math.fma(m00, c, m10 * s);
        double r01 = Math.fma(m01, c, m11 * s);
        double r02 = Math.fma(m02, c, m12 * s);
        double r10 = Math.fma(m10, c, -m00 * s);
        double r11 = Math.fma(m11, c, -m01 * s);
        double r12 = Math.fma(m12, c, -m02 * s);
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        return this;
    }

    public Matrix3d rotateXYZ(double angleX, double angleY, double angleZ) {
        return rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
    }

    public Matrix3d scale(double x, double y, double z) {
        this.m00 *= x; this.m01 *= x; this.m02 *= x;
        this.m10 *= y; this.m11 *= y; this.m12 *= y;
        this.m20 *= z; this.m21 *= z; this.m22 *= z;
        return this;
    }

    public Matrix3d scale(double factor) {
        return scale(factor, factor, factor);
    }

    public Matrix3d scale(Vector3D xyz) {
        return scale(xyz.x(), xyz.y(), xyz.z());
    }

    public Matrix3d multiply(Matrix3d other) {
        double r00 = Math.fma(m00, other.m00, Math.fma(m10, other.m01, m20 * other.m02));
        double r01 = Math.fma(m01, other.m00, Math.fma(m11, other.m01, m21 * other.m02));
        double r02 = Math.fma(m02, other.m00, Math.fma(m12, other.m01, m22 * other.m02));
        double r10 = Math.fma(m00, other.m10, Math.fma(m10, other.m11, m20 * other.m12));
        double r11 = Math.fma(m01, other.m10, Math.fma(m11, other.m11, m21 * other.m12));
        double r12 = Math.fma(m02, other.m10, Math.fma(m12, other.m11, m22 * other.m12));
        double r20 = Math.fma(m00, other.m20, Math.fma(m10, other.m21, m20 * other.m22));
        double r21 = Math.fma(m01, other.m20, Math.fma(m11, other.m21, m21 * other.m22));
        double r22 = Math.fma(m02, other.m20, Math.fma(m12, other.m21, m22 * other.m22));
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3d add(Matrix3d other) {
        this.m00 += other.m00; this.m01 += other.m01; this.m02 += other.m02;
        this.m10 += other.m10; this.m11 += other.m11; this.m12 += other.m12;
        this.m20 += other.m20; this.m21 += other.m21; this.m22 += other.m22;
        return this;
    }

    public Matrix3d sub(Matrix3d other) {
        this.m00 -= other.m00; this.m01 -= other.m01; this.m02 -= other.m02;
        this.m10 -= other.m10; this.m11 -= other.m11; this.m12 -= other.m12;
        this.m20 -= other.m20; this.m21 -= other.m21; this.m22 -= other.m22;
        return this;
    }

    public Matrix3d lerp(Matrix3d other, double t) {
        double tInv = 1.0 - t;
        this.m00 = this.m00 * tInv + other.m00 * t;
        this.m01 = this.m01 * tInv + other.m01 * t;
        this.m02 = this.m02 * tInv + other.m02 * t;
        this.m10 = this.m10 * tInv + other.m10 * t;
        this.m11 = this.m11 * tInv + other.m11 * t;
        this.m12 = this.m12 * tInv + other.m12 * t;
        this.m20 = this.m20 * tInv + other.m20 * t;
        this.m21 = this.m21 * tInv + other.m21 * t;
        this.m22 = this.m22 * tInv + other.m22 * t;
        return this;
    }

    public double determinant() {
        return m00 * (m11 * m22 - m21 * m12)
             - m10 * (m01 * m22 - m21 * m02)
             + m20 * (m01 * m12 - m11 * m02);
    }

    public Matrix3d invert() {
        double det = determinant();
        if (det == 0.0) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        double invDet = 1.0 / det;
        double r00 = (m11 * m22 - m21 * m12) * invDet;
        double r01 = (m21 * m02 - m01 * m22) * invDet;
        double r02 = (m01 * m12 - m11 * m02) * invDet;
        double r10 = (m20 * m12 - m10 * m22) * invDet;
        double r11 = (m00 * m22 - m20 * m02) * invDet;
        double r12 = (m20 * m01 - m00 * m21) * invDet;
        double r20 = (m10 * m21 - m20 * m11) * invDet;
        double r21 = (m10 * m02 - m00 * m12) * invDet;
        double r22 = (m00 * m11 - m10 * m01) * invDet;
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3d transpose() {
        double t = m01; m01 = m10; m10 = t;
        t = m02; m02 = m20; m20 = t;
        t = m12; m12 = m21; m21 = t;
        return this;
    }

    public double trace() {
        return m00 + m11 + m22;
    }

    public Vector3D transform(Vector3D v) {
        return new Vector3D(
            Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z())),
            Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z())),
            Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()))
        );
    }

    public Vector3D transform(Vector3D v, Vector3D dest) {
        dest = new Vector3D(
            Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z())),
            Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z())),
            Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()))
        );
        return dest;
    }

    public double m00() { return m00; }
    public double m01() { return m01; }
    public double m02() { return m02; }
    public double m10() { return m10; }
    public double m11() { return m11; }
    public double m12() { return m12; }
    public double m20() { return m20; }
    public double m21() { return m21; }
    public double m22() { return m22; }

    public Matrix3d m00(double m00) { this.m00 = m00; return this; }
    public Matrix3d m01(double m01) { this.m01 = m01; return this; }
    public Matrix3d m02(double m02) { this.m02 = m02; return this; }
    public Matrix3d m10(double m10) { this.m10 = m10; return this; }
    public Matrix3d m11(double m11) { this.m11 = m11; return this; }
    public Matrix3d m12(double m12) { this.m12 = m12; return this; }
    public Matrix3d m20(double m20) { this.m20 = m20; return this; }
    public Matrix3d m21(double m21) { this.m21 = m21; return this; }
    public Matrix3d m22(double m22) { this.m22 = m22; return this; }

    public double[] get(double[] dest, int offset) {
        dest[offset]     = m00;
        dest[offset + 1] = m01;
        dest[offset + 2] = m02;
        dest[offset + 3] = m10;
        dest[offset + 4] = m11;
        dest[offset + 5] = m12;
        dest[offset + 6] = m20;
        dest[offset + 7] = m21;
        dest[offset + 8] = m22;
        return dest;
    }

    public double[] get(double[] dest) {
        return get(dest, 0);
    }

    public Vector3D row(int index, Vector3D dest) {
        switch (index) {
            case 0: dest = new Vector3D(m00, m10, m20); break;
            case 1: dest = new Vector3D(m01, m11, m21); break;
            case 2: dest = new Vector3D(m02, m12, m22); break;
            default: throw new IndexOutOfBoundsException("Row index: " + index);
        }
        return dest;
    }

    public Vector3D col(int index, Vector3D dest) {
        switch (index) {
            case 0: dest = new Vector3D(m00, m01, m02); break;
            case 1: dest = new Vector3D(m10, m11, m12); break;
            case 2: dest = new Vector3D(m20, m21, m22); break;
            default: throw new IndexOutOfBoundsException("Column index: " + index);
        }
        return dest;
    }

    public Matrix4d get(Matrix4d dest) {
        dest.m00(m00).m01(m01).m02(m02);
        dest.m10(m10).m11(m11).m12(m12);
        dest.m20(m20).m21(m21).m22(m22);
        return dest;
    }

    public Matrix3d from(Matrix4d src) {
        this.m00 = src.m00(); this.m01 = src.m01(); this.m02 = src.m02();
        this.m10 = src.m10(); this.m11 = src.m11(); this.m12 = src.m12();
        this.m20 = src.m20(); this.m21 = src.m21(); this.m22 = src.m22();
        return this;
    }

    public Matrix3d normal(Matrix4d src) {
        from(src);
        invert();
        transpose();
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix3d other)) return false;
        return m00 == other.m00 && m01 == other.m01 && m02 == other.m02
            && m10 == other.m10 && m11 == other.m11 && m12 == other.m12
            && m20 == other.m20 && m21 == other.m21 && m22 == other.m22;
    }

    @Override
    public String toString() {
        return "[["
            + m00 + ", " + m10 + ", " + m20 + "], ["
            + m01 + ", " + m11 + ", " + m21 + "], ["
            + m02 + ", " + m12 + ", " + m22 + "]]";
    }

    public Matrix3d get(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, m00);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, m01);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, m02);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 24, m10);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 32, m11);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 40, m12);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 48, m20);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 56, m21);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 64, m22);
        return this;
    }

    public Matrix3d set(MemorySegment src, long byteOffset) {
        m00 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset);
        m01 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8);
        m02 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16);
        m10 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 24);
        m11 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 32);
        m12 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 40);
        m20 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 48);
        m21 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 56);
        m22 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 64);
        return this;
    }

    public Matrix3d getVulkan(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, m00);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, m01);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, m02);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 32, m10);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 40, m11);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 48, m12);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 64, m20);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 72, m21);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 80, m22);
        return this;
    }

    public Matrix3d setVulkan(MemorySegment src, long byteOffset) {
        m00 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset);
        m01 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8);
        m02 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16);
        m10 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 32);
        m11 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 40);
        m12 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 48);
        m20 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 64);
        m21 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 72);
        m22 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 80);
        return this;
    }
}

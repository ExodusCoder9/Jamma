package com.jamma.math.matrix;

import com.jamma.math.Vector3f;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;
import java.util.Objects;

public class Matrix3f implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
    public float m20;
    public float m21;
    public float m22;

    public Matrix3f() {
        identity();
    }

    public Matrix3f(Matrix3f other) {
        set(other);
    }

    public Matrix3f(float[] m) {
        set(m);
    }

    public Matrix3f set(Matrix3f other) {
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

    public Matrix3f set(float[] m) {
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

    public Matrix3f identity() {
        this.m00 = 1.0f; this.m01 = 0.0f; this.m02 = 0.0f;
        this.m10 = 0.0f; this.m11 = 1.0f; this.m12 = 0.0f;
        this.m20 = 0.0f; this.m21 = 0.0f; this.m22 = 1.0f;
        return this;
    }

    public Matrix3f zero() {
        this.m00 = 0.0f; this.m01 = 0.0f; this.m02 = 0.0f;
        this.m10 = 0.0f; this.m11 = 0.0f; this.m12 = 0.0f;
        this.m20 = 0.0f; this.m21 = 0.0f; this.m22 = 0.0f;
        return this;
    }

    public Matrix3f rotate(float angle, float x, float y, float z) {
        float invLen = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);
        float nx = x * invLen;
        float ny = y * invLen;
        float nz = z * invLen;
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float t = 1.0f - c;
        float rm00 = t * nx * nx + c;
        float rm01 = t * nx * ny + s * nz;
        float rm02 = t * nx * nz - s * ny;
        float rm10 = t * nx * ny - s * nz;
        float rm11 = t * ny * ny + c;
        float rm12 = t * ny * nz + s * nx;
        float rm20 = t * nx * nz + s * ny;
        float rm21 = t * ny * nz - s * nx;
        float rm22 = t * nz * nz + c;
        float r00 = Math.fma(m00, rm00, Math.fma(m10, rm01, m20 * rm02));
        float r01 = Math.fma(m01, rm00, Math.fma(m11, rm01, m21 * rm02));
        float r02 = Math.fma(m02, rm00, Math.fma(m12, rm01, m22 * rm02));
        float r10 = Math.fma(m00, rm10, Math.fma(m10, rm11, m20 * rm12));
        float r11 = Math.fma(m01, rm10, Math.fma(m11, rm11, m21 * rm12));
        float r12 = Math.fma(m02, rm10, Math.fma(m12, rm11, m22 * rm12));
        float r20 = Math.fma(m00, rm20, Math.fma(m10, rm21, m20 * rm22));
        float r21 = Math.fma(m01, rm20, Math.fma(m11, rm21, m21 * rm22));
        float r22 = Math.fma(m02, rm20, Math.fma(m12, rm21, m22 * rm22));
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3f rotateX(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float r10 = Math.fma(m10, c, m20 * s);
        float r11 = Math.fma(m11, c, m21 * s);
        float r12 = Math.fma(m12, c, m22 * s);
        float r20 = Math.fma(m20, c, -m10 * s);
        float r21 = Math.fma(m21, c, -m11 * s);
        float r22 = Math.fma(m22, c, -m12 * s);
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3f rotateY(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float r00 = Math.fma(m00, c, -m20 * s);
        float r01 = Math.fma(m01, c, -m21 * s);
        float r02 = Math.fma(m02, c, -m22 * s);
        float r20 = Math.fma(m00, s, m20 * c);
        float r21 = Math.fma(m01, s, m21 * c);
        float r22 = Math.fma(m02, s, m22 * c);
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3f rotateZ(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float r00 = Math.fma(m00, c, m10 * s);
        float r01 = Math.fma(m01, c, m11 * s);
        float r02 = Math.fma(m02, c, m12 * s);
        float r10 = Math.fma(m10, c, -m00 * s);
        float r11 = Math.fma(m11, c, -m01 * s);
        float r12 = Math.fma(m12, c, -m02 * s);
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        return this;
    }

    public Matrix3f rotateXYZ(float angleX, float angleY, float angleZ) {
        return rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
    }

    public Matrix3f scale(float x, float y, float z) {
        this.m00 *= x; this.m01 *= x; this.m02 *= x;
        this.m10 *= y; this.m11 *= y; this.m12 *= y;
        this.m20 *= z; this.m21 *= z; this.m22 *= z;
        return this;
    }

    public Matrix3f scale(float factor) {
        return scale(factor, factor, factor);
    }

    public Matrix3f scale(Vector3f xyz) {
        return scale(xyz.x(), xyz.y(), xyz.z());
    }

    public Matrix3f multiply(Matrix3f other) {
        float r00 = Math.fma(m00, other.m00, Math.fma(m10, other.m01, m20 * other.m02));
        float r01 = Math.fma(m01, other.m00, Math.fma(m11, other.m01, m21 * other.m02));
        float r02 = Math.fma(m02, other.m00, Math.fma(m12, other.m01, m22 * other.m02));
        float r10 = Math.fma(m00, other.m10, Math.fma(m10, other.m11, m20 * other.m12));
        float r11 = Math.fma(m01, other.m10, Math.fma(m11, other.m11, m21 * other.m12));
        float r12 = Math.fma(m02, other.m10, Math.fma(m12, other.m11, m22 * other.m12));
        float r20 = Math.fma(m00, other.m20, Math.fma(m10, other.m21, m20 * other.m22));
        float r21 = Math.fma(m01, other.m20, Math.fma(m11, other.m21, m21 * other.m22));
        float r22 = Math.fma(m02, other.m20, Math.fma(m12, other.m21, m22 * other.m22));
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3f add(Matrix3f other) {
        this.m00 += other.m00; this.m01 += other.m01; this.m02 += other.m02;
        this.m10 += other.m10; this.m11 += other.m11; this.m12 += other.m12;
        this.m20 += other.m20; this.m21 += other.m21; this.m22 += other.m22;
        return this;
    }

    public Matrix3f sub(Matrix3f other) {
        this.m00 -= other.m00; this.m01 -= other.m01; this.m02 -= other.m02;
        this.m10 -= other.m10; this.m11 -= other.m11; this.m12 -= other.m12;
        this.m20 -= other.m20; this.m21 -= other.m21; this.m22 -= other.m22;
        return this;
    }

    public Matrix3f lerp(Matrix3f other, float t) {
        float tInv = 1.0f - t;
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

    public float determinant() {
        return m00 * (m11 * m22 - m21 * m12)
             - m10 * (m01 * m22 - m21 * m02)
             + m20 * (m01 * m12 - m11 * m02);
    }

    public Matrix3f invert() {
        float det = determinant();
        if (det == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        float invDet = 1.0f / det;
        float r00 = (m11 * m22 - m21 * m12) * invDet;
        float r01 = (m21 * m02 - m01 * m22) * invDet;
        float r02 = (m01 * m12 - m11 * m02) * invDet;
        float r10 = (m20 * m12 - m10 * m22) * invDet;
        float r11 = (m00 * m22 - m20 * m02) * invDet;
        float r12 = (m20 * m01 - m00 * m21) * invDet;
        float r20 = (m10 * m21 - m20 * m11) * invDet;
        float r21 = (m10 * m02 - m00 * m12) * invDet;
        float r22 = (m00 * m11 - m10 * m01) * invDet;
        this.m00 = r00; this.m01 = r01; this.m02 = r02;
        this.m10 = r10; this.m11 = r11; this.m12 = r12;
        this.m20 = r20; this.m21 = r21; this.m22 = r22;
        return this;
    }

    public Matrix3f transpose() {
        float t = m01; m01 = m10; m10 = t;
        t = m02; m02 = m20; m20 = t;
        t = m12; m12 = m21; m21 = t;
        return this;
    }

    public float trace() {
        return m00 + m11 + m22;
    }

    public Vector3f transform(Vector3f v) {
        return new Vector3f(
            Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z())),
            Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z())),
            Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()))
        );
    }



    public float m00() { return m00; }
    public float m01() { return m01; }
    public float m02() { return m02; }
    public float m10() { return m10; }
    public float m11() { return m11; }
    public float m12() { return m12; }
    public float m20() { return m20; }
    public float m21() { return m21; }
    public float m22() { return m22; }

    public Matrix3f m00(float m00) { this.m00 = m00; return this; }
    public Matrix3f m01(float m01) { this.m01 = m01; return this; }
    public Matrix3f m02(float m02) { this.m02 = m02; return this; }
    public Matrix3f m10(float m10) { this.m10 = m10; return this; }
    public Matrix3f m11(float m11) { this.m11 = m11; return this; }
    public Matrix3f m12(float m12) { this.m12 = m12; return this; }
    public Matrix3f m20(float m20) { this.m20 = m20; return this; }
    public Matrix3f m21(float m21) { this.m21 = m21; return this; }
    public Matrix3f m22(float m22) { this.m22 = m22; return this; }

    public float get(int col, int row) {
        return switch (row * 3 + col) {
            case 0 -> m00; case 1 -> m01; case 2 -> m02;
            case 3 -> m10; case 4 -> m11; case 5 -> m12;
            case 6 -> m20; case 7 -> m21; case 8 -> m22;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix3f set(int col, int row, float value) {
        switch (row * 3 + col) {
            case 0: m00 = value; break; case 1: m01 = value; break; case 2: m02 = value; break;
            case 3: m10 = value; break; case 4: m11 = value; break; case 5: m12 = value; break;
            case 6: m20 = value; break; case 7: m21 = value; break; case 8: m22 = value; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public float[] get(float[] dest, int offset) {
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

    public float[] get(float[] dest) {
        return get(dest, 0);
    }

    public Vector3f row(int index) {
        return switch (index) {
            case 0 -> new Vector3f(m00, m10, m20);
            case 1 -> new Vector3f(m01, m11, m21);
            case 2 -> new Vector3f(m02, m12, m22);
            default -> throw new IndexOutOfBoundsException("Row index: " + index);
        };
    }

    public Vector3f col(int index) {
        return switch (index) {
            case 0 -> new Vector3f(m00, m01, m02);
            case 1 -> new Vector3f(m10, m11, m12);
            case 2 -> new Vector3f(m20, m21, m22);
            default -> throw new IndexOutOfBoundsException("Column index: " + index);
        };
    }

    public Matrix4f get(Matrix4f dest) {
        dest.m00(m00).m01(m01).m02(m02);
        dest.m10(m10).m11(m11).m12(m12);
        dest.m20(m20).m21(m21).m22(m22);
        return dest;
    }

    public Matrix3f from(Matrix4f src) {
        this.m00 = src.m00(); this.m01 = src.m01(); this.m02 = src.m02();
        this.m10 = src.m10(); this.m11 = src.m11(); this.m12 = src.m12();
        this.m20 = src.m20(); this.m21 = src.m21(); this.m22 = src.m22();
        return this;
    }

    public Matrix3f normal(Matrix4f src) {
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
        if (!(obj instanceof Matrix3f other)) return false;
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

    public Matrix3f get(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, m00);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, m01);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, m02);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 12, m10);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 16, m11);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 20, m12);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 24, m20);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 28, m21);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 32, m22);
        return this;
    }

    public Matrix3f set(MemorySegment src, long byteOffset) {
        m00 = src.get(ValueLayout.JAVA_FLOAT, byteOffset);
        m01 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4);
        m02 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8);
        m10 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 12);
        m11 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 16);
        m12 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 20);
        m20 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 24);
        m21 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 28);
        m22 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 32);
        return this;
    }

    public Matrix3f getVulkan(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, m00);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, m01);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, m02);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 16, m10);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 20, m11);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 24, m12);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 32, m20);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 36, m21);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 40, m22);
        return this;
    }

    public Matrix3f setVulkan(MemorySegment src, long byteOffset) {
        m00 = src.get(ValueLayout.JAVA_FLOAT, byteOffset);
        m01 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4);
        m02 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8);
        m10 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 16);
        m11 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 20);
        m12 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 24);
        m20 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 32);
        m21 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 36);
        m22 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 40);
        return this;
    }

    public static Matrix3f fromBuffer(FloatBuffer src) {
        Matrix3f m = new Matrix3f();
        m.m00 = src.get(); m.m01 = src.get(); m.m02 = src.get();
        m.m10 = src.get(); m.m11 = src.get(); m.m12 = src.get();
        m.m20 = src.get(); m.m21 = src.get(); m.m22 = src.get();
        return m;
    }

    public static Matrix3f fromBuffer(int index, FloatBuffer src) {
        Matrix3f m = new Matrix3f();
        m.m00 = src.get(index);     m.m01 = src.get(index + 1); m.m02 = src.get(index + 2);
        m.m10 = src.get(index + 3); m.m11 = src.get(index + 4); m.m12 = src.get(index + 5);
        m.m20 = src.get(index + 6); m.m21 = src.get(index + 7); m.m22 = src.get(index + 8);
        return m;
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(m00); dest.put(m01); dest.put(m02);
        dest.put(m10); dest.put(m11); dest.put(m12);
        dest.put(m20); dest.put(m21); dest.put(m22);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, m00);     dest.put(index + 1, m01); dest.put(index + 2, m02);
        dest.put(index + 3, m10); dest.put(index + 4, m11); dest.put(index + 5, m12);
        dest.put(index + 6, m20); dest.put(index + 7, m21); dest.put(index + 8, m22);
        return dest;
    }
}

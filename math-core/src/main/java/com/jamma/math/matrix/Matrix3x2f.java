package com.jamma.math.matrix;

import com.jamma.math.Vector2f;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix3x2f implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public float m00, m01, m02;
    public float m10, m11, m12;

    public Matrix3x2f() {
        identity();
    }

    public Matrix3x2f(Matrix3x2f other) {
        set(other);
    }

    public Matrix3x2f(Matrix3x2d other) {
        m00 = (float) other.m00; m01 = (float) other.m01; m02 = (float) other.m02;
        m10 = (float) other.m10; m11 = (float) other.m11; m12 = (float) other.m12;
    }

    public Matrix3x2f(Matrix4f other) {
        set(other);
    }

    public Matrix3x2f(float m00, float m01, float m02, float m10, float m11, float m12) {
        set(m00, m01, m02, m10, m11, m12);
    }

    public Matrix3x2f identity() {
        m00 = 1.0f; m01 = 0.0f; m02 = 0.0f;
        m10 = 0.0f; m11 = 1.0f; m12 = 0.0f;
        return this;
    }

    public Matrix3x2f zero() {
        m00 = 0.0f; m01 = 0.0f; m02 = 0.0f;
        m10 = 0.0f; m11 = 0.0f; m12 = 0.0f;
        return this;
    }

    public Matrix3x2f set(Matrix3x2f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
        return this;
    }

    public Matrix3x2f set(Matrix3x2d other) {
        m00 = (float) other.m00; m01 = (float) other.m01; m02 = (float) other.m02;
        m10 = (float) other.m10; m11 = (float) other.m11; m12 = (float) other.m12;
        return this;
    }

    public Matrix3x2f set(Matrix4f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m30;
        m10 = other.m10; m11 = other.m11; m12 = other.m31;
        return this;
    }

    public Matrix3x2f set(float[] m) {
        m00 = m[0]; m01 = m[1]; m02 = m[2];
        m10 = m[3]; m11 = m[4]; m12 = m[5];
        return this;
    }

    public Matrix3x2f set(float m00, float m01, float m02, float m10, float m11, float m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        return this;
    }

    public float get(int col, int row) {
        return switch (row * 3 + col) {
            case 0 -> m00; case 1 -> m01; case 2 -> m02;
            case 3 -> m10; case 4 -> m11; case 5 -> m12;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix3x2f set(int col, int row, float val) {
        switch (row * 3 + col) {
            case 0: m00 = val; break; case 1: m01 = val; break; case 2: m02 = val; break;
            case 3: m10 = val; break; case 4: m11 = val; break; case 5: m12 = val; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public Vector2f getRow(int row) {
        return switch (row) {
            case 0 -> new Vector2f(m00, m01);
            case 1 -> new Vector2f(m10, m11);
            default -> throw new IndexOutOfBoundsException("Row index: " + row);
        };
    }

    public Matrix3x2f setRow(int row, float x, float y) {
        switch (row) {
            case 0: m00 = x; m01 = y; break;
            case 1: m10 = x; m11 = y; break;
            default: throw new IndexOutOfBoundsException("Row index: " + row);
        }
        return this;
    }

    public Matrix3x2f setRow(int row, Vector2f v) {
        return setRow(row, v.x(), v.y());
    }

    public Vector2f getColumn(int col) {
        return switch (col) {
            case 0 -> new Vector2f(m00, m10);
            case 1 -> new Vector2f(m01, m11);
            case 2 -> new Vector2f(m02, m12);
            default -> throw new IndexOutOfBoundsException("Column index: " + col);
        };
    }

    public Matrix3x2f setColumn(int col, float x, float y) {
        switch (col) {
            case 0: m00 = x; m10 = y; break;
            case 1: m01 = x; m11 = y; break;
            case 2: m02 = x; m12 = y; break;
            default: throw new IndexOutOfBoundsException("Column index: " + col);
        }
        return this;
    }

    public Matrix3x2f setColumn(int col, Vector2f v) {
        return setColumn(col, v.x(), v.y());
    }

    public float determinant() {
        return m00 * m11 - m01 * m10;
    }

    public Matrix3x2f invert() {
        float det = m00 * m11 - m01 * m10;
        if (det == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        float invDet = 1.0f / det;
        float n00 = m11 * invDet;
        float n01 = -m01 * invDet;
        float n02 = (m01 * m12 - m11 * m02) * invDet;
        float n10 = -m10 * invDet;
        float n11 = m00 * invDet;
        float n12 = (m10 * m02 - m00 * m12) * invDet;
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        return this;
    }

    public Matrix3x2f mul(Matrix3x2f right) {
        float n00 = Math.fma(m00, right.m00, m01 * right.m10);
        float n10 = Math.fma(m10, right.m00, m11 * right.m10);
        float n01 = Math.fma(m00, right.m01, m01 * right.m11);
        float n11 = Math.fma(m10, right.m01, m11 * right.m11);
        float n02 = Math.fma(m00, right.m02, Math.fma(m01, right.m12, m02));
        float n12 = Math.fma(m10, right.m02, Math.fma(m11, right.m12, m12));
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        return this;
    }

    public Matrix3x2f mul(Matrix3x2f right, Matrix3x2f dest) {
        float n00 = Math.fma(m00, right.m00, m01 * right.m10);
        float n10 = Math.fma(m10, right.m00, m11 * right.m10);
        float n01 = Math.fma(m00, right.m01, m01 * right.m11);
        float n11 = Math.fma(m10, right.m01, m11 * right.m11);
        float n02 = Math.fma(m00, right.m02, Math.fma(m01, right.m12, m02));
        float n12 = Math.fma(m10, right.m02, Math.fma(m11, right.m12, m12));
        dest.m00 = n00; dest.m01 = n01; dest.m02 = n02;
        dest.m10 = n10; dest.m11 = n11; dest.m12 = n12;
        return this;
    }

    public Matrix3x2f scale(float x, float y) {
        m00 *= x; m01 *= x; m02 *= x;
        m10 *= y; m11 *= y; m12 *= y;
        return this;
    }

    public Matrix3x2f scale(float factor) {
        return scale(factor, factor);
    }

    public Matrix3x2f scale(Vector2f xy) {
        return scale(xy.x(), xy.y());
    }

    public Matrix3x2f translate(float x, float y) {
        float t00 = m00; float t01 = m01;
        float t10 = m10; float t11 = m11;
        m02 = Math.fma(t00, x, Math.fma(t01, y, m02));
        m12 = Math.fma(t10, x, Math.fma(t11, y, m12));
        return this;
    }

    public Matrix3x2f translate(Vector2f offset) {
        return translate(offset.x(), offset.y());
    }

    public Matrix3x2f rotate(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t00 = m00; float t01 = m01;
        float t10 = m10; float t11 = m11;
        m00 = t00 * c + t01 * s;
        m10 = t10 * c + t11 * s;
        m01 = t01 * c - t00 * s;
        m11 = t11 * c - t10 * s;
        return this;
    }

    public Vector2f transform(Vector2f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m01, v.y(), m02));
        float y = Math.fma(m10, v.x(), Math.fma(m11, v.y(), m12));
        return new Vector2f(x, y);
    }

    public Vector2f transformPosition(Vector2f v) {
        return transform(v);
    }

    public Vector2f transformDirection(Vector2f v) {
        float x = Math.fma(m00, v.x(), m01 * v.y());
        float y = Math.fma(m10, v.x(), m11 * v.y());
        return new Vector2f(x, y);
    }

    public Matrix3x2f transpose() {
        float t = m01;
        m01 = m10;
        m10 = t;
        return this;
    }

    public Matrix3x2f add(Matrix3x2f other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02;
        m10 += other.m10; m11 += other.m11; m12 += other.m12;
        return this;
    }

    public Matrix3x2f sub(Matrix3x2f other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12;
        return this;
    }

    public Matrix3x2f mulComponentWise(Matrix3x2f other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12;
        return this;
    }

    public Matrix3x2f negate() {
        m00 = -m00; m01 = -m01; m02 = -m02;
        m10 = -m10; m11 = -m11; m12 = -m12;
        return this;
    }

    public boolean isFinite() {
        return Float.isFinite(m00) && Float.isFinite(m01) && Float.isFinite(m02)
            && Float.isFinite(m10) && Float.isFinite(m11) && Float.isFinite(m12);
    }

    public Matrix3x2f get(float[] dest, int offset) {
        dest[offset] = m00; dest[offset + 1] = m01; dest[offset + 2] = m02;
        dest[offset + 3] = m10; dest[offset + 4] = m11; dest[offset + 5] = m12;
        return this;
    }

    public Matrix4f get(Matrix4f dest) {
        dest.m00 = m00; dest.m01 = m01; dest.m02 = 0.0f; dest.m03 = 0.0f;
        dest.m10 = m10; dest.m11 = m11; dest.m12 = 0.0f; dest.m13 = 0.0f;
        dest.m20 = 0.0f; dest.m21 = 0.0f; dest.m22 = 1.0f; dest.m23 = 0.0f;
        dest.m30 = m02; dest.m31 = m12; dest.m32 = 0.0f; dest.m33 = 1.0f;
        return dest;
    }

    public Matrix4f toMatrix4f() {
        return get(new Matrix4f());
    }

    public Matrix3x2f swap(Matrix3x2f other) {
        float t;
        t = m00; m00 = other.m00; other.m00 = t;
        t = m01; m01 = other.m01; other.m01 = t;
        t = m02; m02 = other.m02; other.m02 = t;
        t = m10; m10 = other.m10; other.m10 = t;
        t = m11; m11 = other.m11; other.m11 = t;
        t = m12; m12 = other.m12; other.m12 = t;
        return this;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(m00, m01, m02, m10, m11, m12);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix3x2f)) return false;
        Matrix3x2f other = (Matrix3x2f) obj;
        return Float.floatToIntBits(m00) == Float.floatToIntBits(other.m00)
            && Float.floatToIntBits(m01) == Float.floatToIntBits(other.m01)
            && Float.floatToIntBits(m02) == Float.floatToIntBits(other.m02)
            && Float.floatToIntBits(m10) == Float.floatToIntBits(other.m10)
            && Float.floatToIntBits(m11) == Float.floatToIntBits(other.m11)
            && Float.floatToIntBits(m12) == Float.floatToIntBits(other.m12);
    }

    @Override
    public String toString() {
        return String.format("Matrix3x2f[[%f, %f, %f], [%f, %f, %f]]",
            m00, m01, m02,
            m10, m11, m12);
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, m00);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, m01);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, m02);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 12, m10);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 16, m11);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 20, m12);
    }

    public static Matrix3x2f read(MemorySegment segment, long offset) {
        return new Matrix3x2f(
            segment.get(ValueLayout.JAVA_FLOAT, offset),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 4),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 8),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 12),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 16),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 20)
        );
    }
}

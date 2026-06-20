package com.jamma.math.matrix;

import com.jamma.math.Vector2f;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;
import java.util.Objects;

public class Matrix2f implements Serializable {

    private static final long serialVersionUID = 1L;

    public float m00;
    public float m01;
    public float m10;
    public float m11;

    public Matrix2f() {
        identity();
    }

    public Matrix2f(Matrix2f other) {
        set(other);
    }

    public Matrix2f(float[] m) {
        set(m);
    }

    public Matrix2f set(Matrix2f other) {
        this.m00 = other.m00;
        this.m01 = other.m01;
        this.m10 = other.m10;
        this.m11 = other.m11;
        return this;
    }

    public Matrix2f set(float[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m10 = m[2];
        this.m11 = m[3];
        return this;
    }

    public Matrix2f identity() {
        this.m00 = 1.0f; this.m01 = 0.0f;
        this.m10 = 0.0f; this.m11 = 1.0f;
        return this;
    }

    public Matrix2f zero() {
        this.m00 = 0.0f; this.m01 = 0.0f;
        this.m10 = 0.0f; this.m11 = 0.0f;
        return this;
    }

    public Matrix2f rotate(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float r00 = m00 * c + m10 * s;
        float r01 = m01 * c + m11 * s;
        float r10 = m10 * c - m00 * s;
        float r11 = m11 * c - m01 * s;
        this.m00 = r00; this.m01 = r01;
        this.m10 = r10; this.m11 = r11;
        return this;
    }

    public Matrix2f scale(float x, float y) {
        this.m00 *= x; this.m01 *= x;
        this.m10 *= y; this.m11 *= y;
        return this;
    }

    public Matrix2f scale(float factor) {
        return scale(factor, factor);
    }

    public Matrix2f scale(Vector2f xy) {
        return scale(xy.x(), xy.y());
    }

    public Matrix2f multiply(Matrix2f other) {
        float r00 = Math.fma(m00, other.m00, m10 * other.m01);
        float r01 = Math.fma(m01, other.m00, m11 * other.m01);
        float r10 = Math.fma(m00, other.m10, m10 * other.m11);
        float r11 = Math.fma(m01, other.m10, m11 * other.m11);
        this.m00 = r00; this.m01 = r01;
        this.m10 = r10; this.m11 = r11;
        return this;
    }

    public Matrix2f add(Matrix2f other) {
        this.m00 += other.m00; this.m01 += other.m01;
        this.m10 += other.m10; this.m11 += other.m11;
        return this;
    }

    public Matrix2f sub(Matrix2f other) {
        this.m00 -= other.m00; this.m01 -= other.m01;
        this.m10 -= other.m10; this.m11 -= other.m11;
        return this;
    }

    public float determinant() {
        return m00 * m11 - m10 * m01;
    }

    public Matrix2f invert() {
        float det = determinant();
        if (det == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        float invDet = 1.0f / det;
        float r00 = m11 * invDet;
        float r01 = -m01 * invDet;
        float r10 = -m10 * invDet;
        float r11 = m00 * invDet;
        this.m00 = r00; this.m01 = r01;
        this.m10 = r10; this.m11 = r11;
        return this;
    }

    public Matrix2f transpose() {
        float t = m01; m01 = m10; m10 = t;
        return this;
    }

    public float trace() {
        return m00 + m11;
    }

    public Vector2f transform(Vector2f v) {
        return new Vector2f(
            Math.fma(m00, v.x(), m10 * v.y()),
            Math.fma(m01, v.x(), m11 * v.y())
        );
    }



    public float m00() { return m00; }
    public float m01() { return m01; }
    public float m10() { return m10; }
    public float m11() { return m11; }

    public Matrix2f m00(float m00) { this.m00 = m00; return this; }
    public Matrix2f m01(float m01) { this.m01 = m01; return this; }
    public Matrix2f m10(float m10) { this.m10 = m10; return this; }
    public Matrix2f m11(float m11) { this.m11 = m11; return this; }

    public float get(int col, int row) {
        return switch (row * 2 + col) {
            case 0 -> m00; case 1 -> m01;
            case 2 -> m10; case 3 -> m11;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix2f set(int col, int row, float value) {
        switch (row * 2 + col) {
            case 0: m00 = value; break; case 1: m01 = value; break;
            case 2: m10 = value; break; case 3: m11 = value; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public float[] get(float[] dest, int offset) {
        dest[offset]     = m00;
        dest[offset + 1] = m01;
        dest[offset + 2] = m10;
        dest[offset + 3] = m11;
        return dest;
    }

    public float[] get(float[] dest) {
        return get(dest, 0);
    }

    public Matrix2f get(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, m00);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, m01);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, m10);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 12, m11);
        return this;
    }

    public Matrix2f set(MemorySegment src, long byteOffset) {
        m00 = src.get(ValueLayout.JAVA_FLOAT, byteOffset);
        m01 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4);
        m10 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8);
        m11 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 12);
        return this;
    }

    public static Matrix2f fromBuffer(FloatBuffer src) {
        Matrix2f m = new Matrix2f();
        m.m00 = src.get(); m.m01 = src.get();
        m.m10 = src.get(); m.m11 = src.get();
        return m;
    }

    public static Matrix2f fromBuffer(int index, FloatBuffer src) {
        Matrix2f m = new Matrix2f();
        m.m00 = src.get(index);     m.m01 = src.get(index + 1);
        m.m10 = src.get(index + 2); m.m11 = src.get(index + 3);
        return m;
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(m00); dest.put(m01);
        dest.put(m10); dest.put(m11);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, m00);     dest.put(index + 1, m01);
        dest.put(index + 2, m10); dest.put(index + 3, m11);
        return dest;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m00, m01, m10, m11);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix2f other)) return false;
        return m00 == other.m00 && m01 == other.m01
            && m10 == other.m10 && m11 == other.m11;
    }

    @Override
    public String toString() {
        return "[["
            + m00 + ", " + m10 + "], ["
            + m01 + ", " + m11 + "]]";
    }
}

package com.jamma.math.matrix;

import com.jamma.math.Vector2d;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix3x2d implements Serializable {

    private static final long serialVersionUID = 1L;

    public double m00, m01, m02;
    public double m10, m11, m12;

    public Matrix3x2d() {
        identity();
    }

    public Matrix3x2d(Matrix3x2d other) {
        set(other);
    }

    public Matrix3x2d(Matrix3x2f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
    }

    public Matrix3x2d(Matrix4d other) {
        set(other);
    }

    public Matrix3x2d(double m00, double m01, double m02, double m10, double m11, double m12) {
        set(m00, m01, m02, m10, m11, m12);
    }

    public Matrix3x2d identity() {
        m00 = 1.0; m01 = 0.0; m02 = 0.0;
        m10 = 0.0; m11 = 1.0; m12 = 0.0;
        return this;
    }

    public Matrix3x2d zero() {
        m00 = 0.0; m01 = 0.0; m02 = 0.0;
        m10 = 0.0; m11 = 0.0; m12 = 0.0;
        return this;
    }

    public Matrix3x2d set(Matrix3x2d other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
        return this;
    }

    public Matrix3x2d set(Matrix3x2f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
        return this;
    }

    public Matrix3x2d set(Matrix4d other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m30;
        m10 = other.m10; m11 = other.m11; m12 = other.m31;
        return this;
    }

    public Matrix3x2d set(double[] m) {
        m00 = m[0]; m01 = m[1]; m02 = m[2];
        m10 = m[3]; m11 = m[4]; m12 = m[5];
        return this;
    }

    public Matrix3x2d set(double m00, double m01, double m02, double m10, double m11, double m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        return this;
    }

    public double get(int col, int row) {
        return switch (row * 3 + col) {
            case 0 -> m00; case 1 -> m01; case 2 -> m02;
            case 3 -> m10; case 4 -> m11; case 5 -> m12;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix3x2d set(int col, int row, double val) {
        switch (row * 3 + col) {
            case 0: m00 = val; break; case 1: m01 = val; break; case 2: m02 = val; break;
            case 3: m10 = val; break; case 4: m11 = val; break; case 5: m12 = val; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public Vector2d getRow(int row) {
        return switch (row) {
            case 0 -> new Vector2d(m00, m01);
            case 1 -> new Vector2d(m10, m11);
            default -> throw new IndexOutOfBoundsException("Row index: " + row);
        };
    }

    public Matrix3x2d setRow(int row, double x, double y) {
        switch (row) {
            case 0: m00 = x; m01 = y; break;
            case 1: m10 = x; m11 = y; break;
            default: throw new IndexOutOfBoundsException("Row index: " + row);
        }
        return this;
    }

    public Matrix3x2d setRow(int row, Vector2d v) {
        return setRow(row, v.x(), v.y());
    }

    public Vector2d getColumn(int col) {
        return switch (col) {
            case 0 -> new Vector2d(m00, m10);
            case 1 -> new Vector2d(m01, m11);
            case 2 -> new Vector2d(m02, m12);
            default -> throw new IndexOutOfBoundsException("Column index: " + col);
        };
    }

    public Matrix3x2d setColumn(int col, double x, double y) {
        switch (col) {
            case 0: m00 = x; m10 = y; break;
            case 1: m01 = x; m11 = y; break;
            case 2: m02 = x; m12 = y; break;
            default: throw new IndexOutOfBoundsException("Column index: " + col);
        }
        return this;
    }

    public Matrix3x2d setColumn(int col, Vector2d v) {
        return setColumn(col, v.x(), v.y());
    }

    public double determinant() {
        return m00 * m11 - m01 * m10;
    }

    public Matrix3x2d invert() {
        double det = m00 * m11 - m01 * m10;
        if (det == 0.0) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        double invDet = 1.0 / det;
        double n00 = m11 * invDet;
        double n01 = -m01 * invDet;
        double n02 = (m01 * m12 - m11 * m02) * invDet;
        double n10 = -m10 * invDet;
        double n11 = m00 * invDet;
        double n12 = (m10 * m02 - m00 * m12) * invDet;
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        return this;
    }

    public Matrix3x2d mul(Matrix3x2d right) {
        double n00 = Math.fma(m00, right.m00, m01 * right.m10);
        double n10 = Math.fma(m10, right.m00, m11 * right.m10);
        double n01 = Math.fma(m00, right.m01, m01 * right.m11);
        double n11 = Math.fma(m10, right.m01, m11 * right.m11);
        double n02 = Math.fma(m00, right.m02, Math.fma(m01, right.m12, m02));
        double n12 = Math.fma(m10, right.m02, Math.fma(m11, right.m12, m12));
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        return this;
    }

    public Matrix3x2d mul(Matrix3x2d right, Matrix3x2d dest) {
        double n00 = Math.fma(m00, right.m00, m01 * right.m10);
        double n10 = Math.fma(m10, right.m00, m11 * right.m10);
        double n01 = Math.fma(m00, right.m01, m01 * right.m11);
        double n11 = Math.fma(m10, right.m01, m11 * right.m11);
        double n02 = Math.fma(m00, right.m02, Math.fma(m01, right.m12, m02));
        double n12 = Math.fma(m10, right.m02, Math.fma(m11, right.m12, m12));
        dest.m00 = n00; dest.m01 = n01; dest.m02 = n02;
        dest.m10 = n10; dest.m11 = n11; dest.m12 = n12;
        return this;
    }

    public Matrix3x2d scale(double x, double y) {
        m00 *= x; m01 *= x; m02 *= x;
        m10 *= y; m11 *= y; m12 *= y;
        return this;
    }

    public Matrix3x2d scale(double factor) {
        return scale(factor, factor);
    }

    public Matrix3x2d scale(Vector2d xy) {
        return scale(xy.x(), xy.y());
    }

    public Matrix3x2d translate(double x, double y) {
        double t00 = m00; double t01 = m01;
        double t10 = m10; double t11 = m11;
        m02 = Math.fma(t00, x, Math.fma(t01, y, m02));
        m12 = Math.fma(t10, x, Math.fma(t11, y, m12));
        return this;
    }

    public Matrix3x2d translate(Vector2d offset) {
        return translate(offset.x(), offset.y());
    }

    public Matrix3x2d rotate(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t00 = m00; double t01 = m01;
        double t10 = m10; double t11 = m11;
        m00 = t00 * c + t01 * s;
        m10 = t10 * c + t11 * s;
        m01 = t01 * c - t00 * s;
        m11 = t11 * c - t10 * s;
        return this;
    }

    public Vector2d transform(Vector2d v) {
        double x = Math.fma(m00, v.x(), Math.fma(m01, v.y(), m02));
        double y = Math.fma(m10, v.x(), Math.fma(m11, v.y(), m12));
        return new Vector2d(x, y);
    }

    public Vector2d transformPosition(Vector2d v) {
        return transform(v);
    }

    public Vector2d transformDirection(Vector2d v) {
        double x = Math.fma(m00, v.x(), m01 * v.y());
        double y = Math.fma(m10, v.x(), m11 * v.y());
        return new Vector2d(x, y);
    }

    public Matrix3x2d transpose() {
        double t = m01;
        m01 = m10;
        m10 = t;
        return this;
    }

    public Matrix3x2d add(Matrix3x2d other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02;
        m10 += other.m10; m11 += other.m11; m12 += other.m12;
        return this;
    }

    public Matrix3x2d sub(Matrix3x2d other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12;
        return this;
    }

    public Matrix3x2d mulComponentWise(Matrix3x2d other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12;
        return this;
    }

    public Matrix3x2d negate() {
        m00 = -m00; m01 = -m01; m02 = -m02;
        m10 = -m10; m11 = -m11; m12 = -m12;
        return this;
    }

    public boolean isFinite() {
        return Double.isFinite(m00) && Double.isFinite(m01) && Double.isFinite(m02)
            && Double.isFinite(m10) && Double.isFinite(m11) && Double.isFinite(m12);
    }

    public Matrix3x2d get(double[] dest, int offset) {
        dest[offset] = m00; dest[offset + 1] = m01; dest[offset + 2] = m02;
        dest[offset + 3] = m10; dest[offset + 4] = m11; dest[offset + 5] = m12;
        return this;
    }

    public Matrix4d get(Matrix4d dest) {
        dest.m00 = m00; dest.m01 = m01; dest.m02 = 0.0; dest.m03 = 0.0;
        dest.m10 = m10; dest.m11 = m11; dest.m12 = 0.0; dest.m13 = 0.0;
        dest.m20 = 0.0; dest.m21 = 0.0; dest.m22 = 1.0; dest.m23 = 0.0;
        dest.m30 = m02; dest.m31 = m12; dest.m32 = 0.0; dest.m33 = 1.0;
        return dest;
    }

    public Matrix4d toMatrix4d() {
        return get(new Matrix4d());
    }

    public Matrix3x2d swap(Matrix3x2d other) {
        double t;
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
        if (!(obj instanceof Matrix3x2d)) return false;
        Matrix3x2d other = (Matrix3x2d) obj;
        return Double.doubleToLongBits(m00) == Double.doubleToLongBits(other.m00)
            && Double.doubleToLongBits(m01) == Double.doubleToLongBits(other.m01)
            && Double.doubleToLongBits(m02) == Double.doubleToLongBits(other.m02)
            && Double.doubleToLongBits(m10) == Double.doubleToLongBits(other.m10)
            && Double.doubleToLongBits(m11) == Double.doubleToLongBits(other.m11)
            && Double.doubleToLongBits(m12) == Double.doubleToLongBits(other.m12);
    }

    @Override
    public String toString() {
        return String.format("Matrix3x2d[[%f, %f, %f], [%f, %f, %f]]",
            m00, m01, m02,
            m10, m11, m12);
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_DOUBLE, offset, m00);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 8, m01);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 16, m02);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 24, m10);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 32, m11);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 40, m12);
    }

    public static Matrix3x2d read(MemorySegment segment, long offset) {
        return new Matrix3x2d(
            segment.get(ValueLayout.JAVA_DOUBLE, offset),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 8),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 16),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 24),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 32),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 40)
        );
    }
}

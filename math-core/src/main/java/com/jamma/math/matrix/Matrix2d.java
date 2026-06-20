package com.jamma.math.matrix;

import com.jamma.math.Vector2d;

import java.io.Serializable;
import java.util.Objects;

public class Matrix2d implements Serializable {

    private static final long serialVersionUID = 1L;

    public double m00;
    public double m01;
    public double m10;
    public double m11;

    public Matrix2d() {
        identity();
    }

    public Matrix2d(Matrix2d other) {
        set(other);
    }

    public Matrix2d(double[] m) {
        set(m);
    }

    public Matrix2d set(Matrix2d other) {
        this.m00 = other.m00;
        this.m01 = other.m01;
        this.m10 = other.m10;
        this.m11 = other.m11;
        return this;
    }

    public Matrix2d set(double[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m10 = m[2];
        this.m11 = m[3];
        return this;
    }

    public Matrix2d identity() {
        this.m00 = 1.0; this.m01 = 0.0;
        this.m10 = 0.0; this.m11 = 1.0;
        return this;
    }

    public Matrix2d zero() {
        this.m00 = 0.0; this.m01 = 0.0;
        this.m10 = 0.0; this.m11 = 0.0;
        return this;
    }

    public Matrix2d rotate(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double r00 = m00 * c + m10 * s;
        double r01 = m01 * c + m11 * s;
        double r10 = m10 * c - m00 * s;
        double r11 = m11 * c - m01 * s;
        this.m00 = r00; this.m01 = r01;
        this.m10 = r10; this.m11 = r11;
        return this;
    }

    public Matrix2d scale(double x, double y) {
        this.m00 *= x; this.m01 *= x;
        this.m10 *= y; this.m11 *= y;
        return this;
    }

    public Matrix2d scale(double factor) {
        return scale(factor, factor);
    }

    public Matrix2d scale(Vector2d xy) {
        return scale(xy.x(), xy.y());
    }

    public Matrix2d multiply(Matrix2d other) {
        double r00 = m00 * other.m00 + m10 * other.m01;
        double r01 = m01 * other.m00 + m11 * other.m01;
        double r10 = m00 * other.m10 + m10 * other.m11;
        double r11 = m01 * other.m10 + m11 * other.m11;
        this.m00 = r00; this.m01 = r01;
        this.m10 = r10; this.m11 = r11;
        return this;
    }

    public Matrix2d add(Matrix2d other) {
        this.m00 += other.m00; this.m01 += other.m01;
        this.m10 += other.m10; this.m11 += other.m11;
        return this;
    }

    public Matrix2d sub(Matrix2d other) {
        this.m00 -= other.m00; this.m01 -= other.m01;
        this.m10 -= other.m10; this.m11 -= other.m11;
        return this;
    }

    public double determinant() {
        return m00 * m11 - m10 * m01;
    }

    public Matrix2d invert() {
        double det = determinant();
        if (det == 0.0) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        double invDet = 1.0 / det;
        double r00 = m11 * invDet;
        double r01 = -m01 * invDet;
        double r10 = -m10 * invDet;
        double r11 = m00 * invDet;
        this.m00 = r00; this.m01 = r01;
        this.m10 = r10; this.m11 = r11;
        return this;
    }

    public Matrix2d transpose() {
        double t = m01; m01 = m10; m10 = t;
        return this;
    }

    public double trace() {
        return m00 + m11;
    }

    public Vector2d transform(Vector2d v) {
        return new Vector2d(
            m00 * v.x() + m10 * v.y(),
            m01 * v.x() + m11 * v.y()
        );
    }



    public double m00() { return m00; }
    public double m01() { return m01; }
    public double m10() { return m10; }
    public double m11() { return m11; }

    public Matrix2d m00(double m00) { this.m00 = m00; return this; }
    public Matrix2d m01(double m01) { this.m01 = m01; return this; }
    public Matrix2d m10(double m10) { this.m10 = m10; return this; }
    public Matrix2d m11(double m11) { this.m11 = m11; return this; }

    public double get(int col, int row) {
        return switch (row * 2 + col) {
            case 0 -> m00; case 1 -> m01;
            case 2 -> m10; case 3 -> m11;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix2d set(int col, int row, double value) {
        switch (row * 2 + col) {
            case 0: m00 = value; break; case 1: m01 = value; break;
            case 2: m10 = value; break; case 3: m11 = value; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public double[] get(double[] dest, int offset) {
        dest[offset]     = m00;
        dest[offset + 1] = m01;
        dest[offset + 2] = m10;
        dest[offset + 3] = m11;
        return dest;
    }

    public double[] get(double[] dest) {
        return get(dest, 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m00, m01, m10, m11);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix2d other)) return false;
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

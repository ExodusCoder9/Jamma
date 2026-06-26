package com.jamma.joml;

import com.jamma.math.matrix.Matrix4d;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vector4d {

    public double x;
    public double y;
    public double z;
    public double w;

    public Vector4d() {
    }

    public Vector4d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4d(Vector4d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    public Vector4d(double[] d) {
        this.x = d[0];
        this.y = d[1];
        this.z = d[2];
        this.w = d[3];
    }

    public Vector4d(com.jamma.math.Vector4d jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
        this.w = jamma.w();
    }

    public com.jamma.math.Vector4d toJamma() {
        return new com.jamma.math.Vector4d(x, y, z, w);
    }

    public static Vector4d fromJamma(com.jamma.math.Vector4d jamma) {
        return new Vector4d(jamma.x(), jamma.y(), jamma.z(), jamma.w());
    }

    public Vector4d set(Vector4d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    public Vector4d set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4d set(double[] d) {
        this.x = d[0];
        this.y = d[1];
        this.z = d[2];
        this.w = d[3];
        return this;
    }

    public Vector4d add(Vector4d v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += v.w;
        return this;
    }

    public Vector4d add(double x, double y, double z, double w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    public Vector4d sub(Vector4d v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= v.w;
        return this;
    }

    public Vector4d mul(Vector4d v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        w *= v.w;
        return this;
    }

    public Vector4d mul(double scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        w *= scalar;
        return this;
    }

    public Vector4d div(Vector4d v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
        w /= v.w;
        return this;
    }

    public Vector4d div(double scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        w /= scalar;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public Vector4d normalize() {
        double inv = 1.0 / length();
        x *= inv;
        y *= inv;
        z *= inv;
        w *= inv;
        return this;
    }

    public Vector4d negate() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
        return this;
    }

    public double dot(Vector4d v) {
        return Math.fma(x, v.x, Math.fma(y, v.y, Math.fma(z, v.z, w * v.w)));
    }

    public Vector4d mul(Matrix4d m) {
        double rx = Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03 * w)));
        double ry = Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13 * w)));
        double rz = Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23 * w)));
        double rw = Math.fma(m.m30, x, Math.fma(m.m31, y, Math.fma(m.m32, z, m.m33 * w)));
        x = rx;
        y = ry;
        z = rz;
        w = rw;
        return this;
    }

    public Vector4d lerp(Vector4d other, double t) {
        x = Math.fma(t, other.x - x, x);
        y = Math.fma(t, other.y - y, y);
        z = Math.fma(t, other.z - z, z);
        w = Math.fma(t, other.w - w, w);
        return this;
    }

    public Vector4d mulPosition(Matrix4d m) {
        double rx = Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03)));
        double ry = Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13)));
        double rz = Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23)));
        double rw = Math.fma(m.m30, x, Math.fma(m.m31, y, Math.fma(m.m32, z, m.m33)));
        double invW = 1.0 / rw;
        x = rx * invW;
        y = ry * invW;
        z = rz * invW;
        w = 1.0;
        return this;
    }

    public Vector4f toVector4f() {
        return new Vector4f((float) x, (float) y, (float) z, (float) w);
    }

    public Vector4d zero() {
        x = y = z = w = 0.0;
        return this;
    }

    public Vector4d abs() {
        x = Math.abs(x);
        y = Math.abs(y);
        z = Math.abs(z);
        w = Math.abs(w);
        return this;
    }

    public double get(int component) {
        return switch (component) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            case 3 -> w;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public double[] get(double[] dest) {
        dest[0] = x;
        dest[1] = y;
        dest[2] = z;
        dest[3] = w;
        return dest;
    }

    public Vector4d load(MemorySegment segment, long offset) {
        x = segment.get(ValueLayout.JAVA_DOUBLE, offset);
        y = segment.get(ValueLayout.JAVA_DOUBLE, offset + 8);
        z = segment.get(ValueLayout.JAVA_DOUBLE, offset + 16);
        w = segment.get(ValueLayout.JAVA_DOUBLE, offset + 24);
        return this;
    }

    public Vector4d store(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_DOUBLE, offset, x);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 8, y);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 16, z);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 24, w);
        return this;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z) ^ Double.hashCode(w);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector4d v)) return false;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(v.x)
            && Double.doubleToLongBits(y) == Double.doubleToLongBits(v.y)
            && Double.doubleToLongBits(z) == Double.doubleToLongBits(v.z)
            && Double.doubleToLongBits(w) == Double.doubleToLongBits(v.w);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}

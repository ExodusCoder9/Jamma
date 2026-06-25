package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector4d(double x, double y, double z, double w) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector4d() { this(0.0, 0.0, 0.0, 0.0); }
    public Vector4d(Vector4d v) { this(v.x, v.y, v.z, v.w); }
    public Vector4d(double[] values) { this(values[0], values[1], values[2], values[3]); }
    public Vector4d(Vector2d v, double z, double w) { this(v.x(), v.y(), z, w); }
    public Vector4d(Vector3d v, double w) { this(v.x(), v.y(), v.z(), w); }

    public static Vector4d fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector4d(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 24)
        );
    }

    public static Vector4d read(MemorySegment segment, long offset) {
        return fromMemorySegment(segment, offset);
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, z);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 24, w);
    }

    public void write(MemorySegment segment, long offset) {
        writeToMemorySegment(segment, offset);
    }

    public static Vector4d fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector4d(src.get(), src.get(), src.get(), src.get());
    }

    public static Vector4d fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector4d(src.get(index), src.get(index + 1), src.get(index + 2), src.get(index + 3));
    }

    public java.nio.DoubleBuffer writeToBuffer(java.nio.DoubleBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        dest.put(w);
        return dest;
    }

    public java.nio.DoubleBuffer writeToBuffer(int index, java.nio.DoubleBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        dest.put(index + 3, w);
        return dest;
    }

    public Vector4d add(Vector4d v) { return new Vector4d(x + v.x, y + v.y, z + v.z, w + v.w); }
    public Vector4d add(double x, double y, double z, double w) { return new Vector4d(this.x + x, this.y + y, this.z + z, this.w + w); }
    public Vector4d sub(Vector4d v) { return new Vector4d(x - v.x, y - v.y, z - v.z, w - v.w); }
    public Vector4d sub(double x, double y, double z, double w) { return new Vector4d(this.x - x, this.y - y, this.z - z, this.w - w); }
    public Vector4d mul(Vector4d v) { return new Vector4d(x * v.x, y * v.y, z * v.z, w * v.w); }
    public Vector4d mul(double scalar) { return new Vector4d(x * scalar, y * scalar, z * scalar, w * scalar); }
    public Vector4d mul(double x, double y, double z, double w) { return new Vector4d(this.x * x, this.y * y, this.z * z, this.w * w); }
    public Vector4d div(Vector4d v) { return new Vector4d(x / v.x, y / v.y, z / v.z, w / v.w); }
    public Vector4d div(double scalar) { return new Vector4d(x / scalar, y / scalar, z / scalar, w / scalar); }
    public Vector4d div(double x, double y, double z, double w) { return new Vector4d(this.x / x, this.y / y, this.z / z, this.w / w); }
    public Vector4d scale(double s) { return new Vector4d(x * s, y * s, z * s, w * s); }
    public Vector4d negate() { return new Vector4d(-x, -y, -z, -w); }
    public Vector4d abs() { return new Vector4d(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w)); }
    public double dot(Vector4d v) { return Math.fma(x, v.x, Math.fma(y, v.y, Math.fma(z, v.z, w * v.w))); }
    public double length() { return Math.sqrt(dot(this)); }
    public double lengthSquared() { return dot(this); }
    public double distance(Vector4d v) { return Math.sqrt(distanceSquared(v)); }
    public double distance(double x, double y, double z, double w) { return Math.sqrt(distanceSquared(x, y, z, w)); }
    public double distanceSquared(Vector4d v) { return distanceSquared(v.x, v.y, v.z, v.w); }
    public double distanceSquared(double x, double y, double z, double w) { return Math.fma(this.x - x, this.x - x, Math.fma(this.y - y, this.y - y, Math.fma(this.z - z, this.z - z, (this.w - w) * (this.w - w)))); }
    public Vector4d normalize() { double len = length(); return new Vector4d(x / len, y / len, z / len, w / len); }
    public Vector4d normalize(double length) { return scale(length / length()); }
    public double angle(Vector4d v) { return Math.acos(Math.clamp(dot(v) / (length() * v.length()), -1.0, 1.0)); }
    public Vector4d lerp(Vector4d other, double t) { return new Vector4d(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y), Math.fma(t, other.z - z, z), Math.fma(t, other.w - w, w)); }
    public Vector4d ceil() { return new Vector4d(Math.ceil(x), Math.ceil(y), Math.ceil(z), Math.ceil(w)); }
    public Vector4d floor() { return new Vector4d(Math.floor(x), Math.floor(y), Math.floor(z), Math.floor(w)); }
    public Vector4d round() { return new Vector4d(Math.round(x), Math.round(y), Math.round(z), Math.round(w)); }
    public Vector4d min(Vector4d v) { return new Vector4d(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z), Math.min(w, v.w)); }
    public Vector4d max(Vector4d v) { return new Vector4d(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z), Math.max(w, v.w)); }
    public Vector4d fma(Vector4d a, Vector4d b) { return new Vector4d(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y), Math.fma(z, a.z, b.z), Math.fma(w, a.w, b.w)); }
    public Vector4d fma(double a, Vector4d b) { return new Vector4d(Math.fma(a, x, b.x), Math.fma(a, y, b.y), Math.fma(a, z, b.z), Math.fma(a, w, b.w)); }

    public boolean equals(Vector4d other, double delta) { return Math.abs(x - other.x) <= delta && Math.abs(y - other.y) <= delta && Math.abs(z - other.z) <= delta && Math.abs(w - other.w) <= delta; }

    public double minComponent() { return Math.min(Math.min(x, y), Math.min(z, w)); }
    public double maxComponent() { return Math.max(Math.max(x, y), Math.max(z, w)); }
    public int maxComponentIndex() { return x >= y && x >= z && x >= w ? 0 : y >= z && y >= w ? 1 : z >= w ? 2 : 3; }
    public int minComponentIndex() { return x <= y && x <= z && x <= w ? 0 : y <= z && y <= w ? 1 : z <= w ? 2 : 3; }
    public Vector2d toVector2d() { return new Vector2d(x, y); }
    public Vector3d toVector3d() { return new Vector3d(x, y, z); }
    public Vector4f toVector4f() { return new Vector4f((float) x, (float) y, (float) z, (float) w); }
    public Vector2d xy() { return new Vector2d(x, y); }
    public Vector3d xyz() { return new Vector3d(x, y, z); }
    public double[] get(double[] dest) { dest[0] = x; dest[1] = y; dest[2] = z; dest[3] = w; return dest; }

    public String toString() { return "(" + x + ", " + y + ", " + z + ", " + w + ")"; }
}

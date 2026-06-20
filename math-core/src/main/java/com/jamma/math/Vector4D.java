package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector4D(double x, double y, double z, double w) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector4D() { this(0.0, 0.0, 0.0, 0.0); }
    public Vector4D(Vector4D v) { this(v.x, v.y, v.z, v.w); }
    public Vector4D(double[] values) { this(values[0], values[1], values[2], values[3]); }
    public Vector4D(Vector2D v, double z, double w) { this(v.x(), v.y(), z, w); }
    public Vector4D(Vector3D v, double w) { this(v.x(), v.y(), v.z(), w); }

    public static Vector4D fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector4D(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 24)
        );
    }

    public static Vector4D read(MemorySegment segment, long offset) {
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

    public static Vector4D fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector4D(src.get(), src.get(), src.get(), src.get());
    }

    public static Vector4D fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector4D(src.get(index), src.get(index + 1), src.get(index + 2), src.get(index + 3));
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

    public Vector4D add(Vector4D v) { return new Vector4D(x + v.x, y + v.y, z + v.z, w + v.w); }
    public Vector4D add(double x, double y, double z, double w) { return new Vector4D(this.x + x, this.y + y, this.z + z, this.w + w); }
    public Vector4D sub(Vector4D v) { return new Vector4D(x - v.x, y - v.y, z - v.z, w - v.w); }
    public Vector4D sub(double x, double y, double z, double w) { return new Vector4D(this.x - x, this.y - y, this.z - z, this.w - w); }
    public Vector4D mul(Vector4D v) { return new Vector4D(x * v.x, y * v.y, z * v.z, w * v.w); }
    public Vector4D mul(double scalar) { return new Vector4D(x * scalar, y * scalar, z * scalar, w * scalar); }
    public Vector4D mul(double x, double y, double z, double w) { return new Vector4D(this.x * x, this.y * y, this.z * z, this.w * w); }
    public Vector4D div(Vector4D v) { return new Vector4D(x / v.x, y / v.y, z / v.z, w / v.w); }
    public Vector4D div(double scalar) { return new Vector4D(x / scalar, y / scalar, z / scalar, w / scalar); }
    public Vector4D div(double x, double y, double z, double w) { return new Vector4D(this.x / x, this.y / y, this.z / z, this.w / w); }
    public Vector4D scale(double s) { return new Vector4D(x * s, y * s, z * s, w * s); }
    public Vector4D negate() { return new Vector4D(-x, -y, -z, -w); }
    public Vector4D abs() { return new Vector4D(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w)); }
    public Vector4D absolute() { return abs(); }
    public double dot(Vector4D v) { return Math.fma(x, v.x, Math.fma(y, v.y, Math.fma(z, v.z, w * v.w))); }
    public double length() { return Math.sqrt(dot(this)); }
    public double lengthSquared() { return dot(this); }
    public double distance(Vector4D v) { return sub(v).length(); }
    public double distance(double x, double y, double z, double w) { return sub(x, y, z, w).length(); }
    public double distanceSquared(Vector4D v) { return sub(v).lengthSquared(); }
    public double distanceSquared(double x, double y, double z, double w) { return sub(x, y, z, w).lengthSquared(); }
    public Vector4D normalize() { double len = length(); return new Vector4D(x / len, y / len, z / len, w / len); }
    public Vector4D normalize(double length) { return scale(length / length()); }
    public double angle(Vector4D v) { return Math.acos(clamp(dot(v) / (length() * v.length()), -1.0, 1.0)); }
    public Vector4D lerp(Vector4D other, double t) { return new Vector4D(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y), Math.fma(t, other.z - z, z), Math.fma(t, other.w - w, w)); }
    public Vector4D ceil() { return new Vector4D(Math.ceil(x), Math.ceil(y), Math.ceil(z), Math.ceil(w)); }
    public Vector4D floor() { return new Vector4D(Math.floor(x), Math.floor(y), Math.floor(z), Math.floor(w)); }
    public Vector4D round() { return new Vector4D(Math.round(x), Math.round(y), Math.round(z), Math.round(w)); }
    public Vector4D min(Vector4D v) { return new Vector4D(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z), Math.min(w, v.w)); }
    public Vector4D max(Vector4D v) { return new Vector4D(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z), Math.max(w, v.w)); }
    public Vector4D fma(Vector4D a, Vector4D b) { return new Vector4D(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y), Math.fma(z, a.z, b.z), Math.fma(w, a.w, b.w)); }
    public Vector4D fma(double a, Vector4D b) { return new Vector4D(Math.fma(a, x, b.x), Math.fma(a, y, b.y), Math.fma(a, z, b.z), Math.fma(a, w, b.w)); }

    public boolean equals(Vector4D other, double delta) { return Math.abs(x - other.x) <= delta && Math.abs(y - other.y) <= delta && Math.abs(z - other.z) <= delta && Math.abs(w - other.w) <= delta; }

    public double minComponent() { return Math.min(Math.min(x, y), Math.min(z, w)); }
    public double maxComponent() { return Math.max(Math.max(x, y), Math.max(z, w)); }
    public int maxComponentIndex() { return x >= y && x >= z && x >= w ? 0 : y >= z && y >= w ? 1 : z >= w ? 2 : 3; }
    public int minComponentIndex() { return x <= y && x <= z && x <= w ? 0 : y <= z && y <= w ? 1 : z <= w ? 2 : 3; }
    public Vector2D toVector2D() { return new Vector2D(x, y); }
    public Vector3D toVector3D() { return new Vector3D(x, y, z); }
    public Vector4f toVector4f() { return new Vector4f((float) x, (float) y, (float) z, (float) w); }
    public Vector2D xy() { return new Vector2D(x, y); }
    public Vector3D xyz() { return new Vector3D(x, y, z); }
    public double[] get(double[] dest) { dest[0] = x; dest[1] = y; dest[2] = z; dest[3] = w; return dest; }

    public String toString() { return "(" + x + ", " + y + ", " + z + ", " + w + ")"; }

    private static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}

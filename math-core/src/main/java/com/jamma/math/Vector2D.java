package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector2D(double x, double y) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector2D() { this(0.0, 0.0); }
    public Vector2D(Vector2D v) { this(v.x, v.y); }
    public Vector2D(double[] values) { this(values[0], values[1]); }

    public static Vector2D fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector2D(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8)
        );
    }

    public static Vector2D read(MemorySegment segment, long offset) {
        return fromMemorySegment(segment, offset);
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
    }

    public void write(MemorySegment segment, long offset) {
        writeToMemorySegment(segment, offset);
    }

    public static Vector2D fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector2D(src.get(), src.get());
    }

    public static Vector2D fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector2D(src.get(index), src.get(index + 1));
    }

    public java.nio.DoubleBuffer writeToBuffer(java.nio.DoubleBuffer dest) {
        dest.put(x);
        dest.put(y);
        return dest;
    }

    public java.nio.DoubleBuffer writeToBuffer(int index, java.nio.DoubleBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        return dest;
    }

    public Vector2D add(Vector2D v) { return new Vector2D(x + v.x, y + v.y); }
    public Vector2D add(double x, double y) { return new Vector2D(this.x + x, this.y + y); }
    public Vector2D sub(Vector2D v) { return new Vector2D(x - v.x, y - v.y); }
    public Vector2D sub(double x, double y) { return new Vector2D(this.x - x, this.y - y); }
    public Vector2D mul(Vector2D v) { return new Vector2D(x * v.x, y * v.y); }
    public Vector2D mul(double scalar) { return new Vector2D(x * scalar, y * scalar); }
    public Vector2D mul(double x, double y) { return new Vector2D(this.x * x, this.y * y); }
    public Vector2D div(Vector2D v) { return new Vector2D(x / v.x, y / v.y); }
    public Vector2D div(double scalar) { return new Vector2D(x / scalar, y / scalar); }
    public Vector2D div(double x, double y) { return new Vector2D(this.x / x, this.y / y); }
    public Vector2D scale(double s) { return new Vector2D(x * s, y * s); }
    public Vector2D negate() { return new Vector2D(-x, -y); }
    public Vector2D abs() { return new Vector2D(Math.abs(x), Math.abs(y)); }
    public Vector2D absolute() { return abs(); }
    public double dot(Vector2D v) { return Math.fma(x, v.x, y * v.y); }
    public double length() { return Math.sqrt(dot(this)); }
    public double lengthSquared() { return dot(this); }
    public double distance(Vector2D v) { return sub(v).length(); }
    public double distance(double x, double y) { return sub(x, y).length(); }
    public double distanceSquared(Vector2D v) { return sub(v).lengthSquared(); }
    public double distanceSquared(double x, double y) { return sub(x, y).lengthSquared(); }
    public Vector2D normalize() { double len = length(); return new Vector2D(x / len, y / len); }
    public Vector2D normalize(double length) { return scale(length / length()); }
    public double angle(Vector2D v) { return Math.acos(clamp(dot(v) / (length() * v.length()), -1.0, 1.0)); }
    public double angleSigned(Vector2D v) { return Math.atan2(x * v.y - y * v.x, dot(v)); }
    public Vector2D reflect(Vector2D normal) { double d = 2.0 * dot(normal); return new Vector2D(x - d * normal.x, y - d * normal.y); }
    public Vector2D project(Vector2D onto) { double s = dot(onto) / onto.dot(onto); return onto.scale(s); }
    public Vector2D reject(Vector2D onto) { return sub(project(onto)); }
    public Vector2D lerp(Vector2D other, double t) { return new Vector2D(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y)); }
    public Vector2D perpendicular() { return new Vector2D(-y, x); }
    public Vector2D rotate(double angle) { double c = Math.cos(angle); double s = Math.sin(angle); return new Vector2D(x * c - y * s, x * s + y * c); }
    public Vector2D halfway(Vector2D other) { return new Vector2D((x + other.x) * 0.5, (y + other.y) * 0.5); }
    public Vector2D ceil() { return new Vector2D(Math.ceil(x), Math.ceil(y)); }
    public Vector2D floor() { return new Vector2D(Math.floor(x), Math.floor(y)); }
    public Vector2D round() { return new Vector2D(Math.round(x), Math.round(y)); }
    public Vector2D min(Vector2D v) { return new Vector2D(Math.min(x, v.x), Math.min(y, v.y)); }
    public Vector2D max(Vector2D v) { return new Vector2D(Math.max(x, v.x), Math.max(y, v.y)); }
    public Vector2D fma(Vector2D a, Vector2D b) { return new Vector2D(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y)); }
    public Vector2D fma(double a, Vector2D b) { return new Vector2D(Math.fma(a, x, b.x), Math.fma(a, y, b.y)); }

    public boolean isPerpendicular(Vector2D v, double epsilon) { return Math.abs(dot(v)) <= epsilon; }
    public boolean equals(Vector2D other, double delta) { return Math.abs(x - other.x) <= delta && Math.abs(y - other.y) <= delta; }

    public double minComponent() { return Math.min(x, y); }
    public double maxComponent() { return Math.max(x, y); }
    public int maxComponentIndex() { return x >= y ? 0 : 1; }
    public int minComponentIndex() { return x <= y ? 0 : 1; }
    public Vector3D toVector3D() { return new Vector3D(x, y, 0.0); }
    public Vector3D toVector3D(double z) { return new Vector3D(x, y, z); }
    public Vector4D toVector4D() { return new Vector4D(x, y, 0.0, 0.0); }
    public Vector4D toVector4D(double z, double w) { return new Vector4D(x, y, z, w); }
    public Vector2f toVector2f() { return new Vector2f((float) x, (float) y); }
    public double[] get(double[] dest) { dest[0] = x; dest[1] = y; return dest; }

    public String toString() { return "(" + x + ", " + y + ")"; }

    private static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}

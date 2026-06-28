package com.jamma.math;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector2d(double x, double y) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector2d() { this(0.0, 0.0); }
    public Vector2d(Vector2d v) { this(v.x, v.y); }
    public Vector2d(double[] values) { this(values[0], values[1]); }

    public static Vector2d fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector2d(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8)
        );
    }

    public static Vector2d read(MemorySegment segment, long offset) {
        return fromMemorySegment(segment, offset);
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
    }

    public void write(MemorySegment segment, long offset) {
        writeToMemorySegment(segment, offset);
    }

    public static Vector2d fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector2d(src.get(), src.get());
    }

    public static Vector2d fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector2d(src.get(index), src.get(index + 1));
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

    public Vector2d add(Vector2d v) { return new Vector2d(x + v.x, y + v.y); }
    public Vector2d add(double x, double y) { return new Vector2d(this.x + x, this.y + y); }
    public Vector2d sub(Vector2d v) { return new Vector2d(x - v.x, y - v.y); }
    public Vector2d sub(double x, double y) { return new Vector2d(this.x - x, this.y - y); }
    public Vector2d mul(Vector2d v) { return new Vector2d(x * v.x, y * v.y); }
    public Vector2d mul(double scalar) { return new Vector2d(x * scalar, y * scalar); }
    public Vector2d mul(double x, double y) { return new Vector2d(this.x * x, this.y * y); }
    public Vector2d div(Vector2d v) { return new Vector2d(x / v.x, y / v.y); }
    public Vector2d div(double scalar) { return new Vector2d(x / scalar, y / scalar); }
    public Vector2d div(double x, double y) { return new Vector2d(this.x / x, this.y / y); }
    public Vector2d scale(double s) { return new Vector2d(x * s, y * s); }
    public Vector2d negate() { return new Vector2d(-x, -y); }
    public Vector2d abs() { return new Vector2d(Math.abs(x), Math.abs(y)); }
    public double dot(Vector2d v) { return Math.fma(x, v.x, y * v.y); }
    public double length() { return Math.sqrt(dot(this)); }
    public double lengthSquared() { return dot(this); }
    public double distance(Vector2d v) { return Math.sqrt(distanceSquared(v)); }
    public double distance(double x, double y) { return Math.sqrt(distanceSquared(x, y)); }
    public double distanceSquared(Vector2d v) { return distanceSquared(v.x, v.y); }
    public double distanceSquared(double x, double y) { return Math.fma(this.x - x, this.x - x, (this.y - y) * (this.y - y)); }
    public Vector2d normalize() {
        double lenSq = lengthSquared();
        if (lenSq == 0.0) return new Vector2d(0.0, 0.0);
        double invLen = 1.0 / Math.sqrt(lenSq);
        return new Vector2d(x * invLen, y * invLen);
    }
    public Vector2d normalize(double length) {
        double lenSq = lengthSquared();
        if (lenSq == 0.0) return new Vector2d(0.0, 0.0);
        return scale(length / Math.sqrt(lenSq));
    }
    public double angle(Vector2d v) {
        double lenSq = lengthSquared();
        double otherLenSq = v.lengthSquared();
        if (lenSq == 0.0 || otherLenSq == 0.0) return 0.0;
        return Math.acos(Math.clamp(dot(v) / Math.sqrt(lenSq * otherLenSq), -1.0, 1.0));
    }
    public double angleSigned(Vector2d v) { return Math.atan2(x * v.y - y * v.x, dot(v)); }
    public Vector2d reflect(Vector2d normal) { double d = 2.0 * dot(normal); return new Vector2d(x - d * normal.x, y - d * normal.y); }
    public Vector2d project(Vector2d onto) {
        double denom = onto.dot(onto);
        if (denom == 0.0) return new Vector2d(0.0, 0.0);
        double s = dot(onto) / denom;
        return onto.scale(s);
    }
    public Vector2d reject(Vector2d onto) { return sub(project(onto)); }
    public Vector2d lerp(Vector2d other, double t) { return new Vector2d(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y)); }
    @SuppressWarnings("SuspiciousNameCombination")
    public Vector2d perpendicular() { return new Vector2d(-y, x); }
    public Vector2d rotate(double angle) { double c = Math.cos(angle); double s = Math.sin(angle); return new Vector2d(x * c - y * s, x * s + y * c); }
    public Vector2d halfway(Vector2d other) { return new Vector2d(x + other.x, y + other.y).normalize(); }
    public Vector2d ceil() { return new Vector2d(Math.ceil(x), Math.ceil(y)); }
    public Vector2d floor() { return new Vector2d(Math.floor(x), Math.floor(y)); }
    public Vector2d round() { return new Vector2d(Math.round(x), Math.round(y)); }
    public Vector2d min(Vector2d v) { return new Vector2d(Math.min(x, v.x), Math.min(y, v.y)); }
    public Vector2d max(Vector2d v) { return new Vector2d(Math.max(x, v.x), Math.max(y, v.y)); }
    public Vector2d fma(Vector2d a, Vector2d b) { return new Vector2d(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y)); }
    public Vector2d fma(double a, Vector2d b) { return new Vector2d(Math.fma(a, x, b.x), Math.fma(a, y, b.y)); }

    public boolean isPerpendicular(Vector2d v, double epsilon) { return Math.abs(dot(v)) <= epsilon; }
    public boolean equals(Vector2d other, double delta) { return Math.abs(x - other.x) <= delta && Math.abs(y - other.y) <= delta; }

    public double minComponent() { return Math.min(x, y); }
    public double maxComponent() { return Math.max(x, y); }
    public int maxComponentIndex() { return x >= y ? 0 : 1; }
    public int minComponentIndex() { return x <= y ? 0 : 1; }
    public Vector3d toVector3d() { return new Vector3d(x, y, 0.0); }
    public Vector3d toVector3d(double z) { return new Vector3d(x, y, z); }
    public Vector4d toVector4d() { return new Vector4d(x, y, 0.0, 0.0); }
    public Vector4d toVector4d(double z, double w) { return new Vector4d(x, y, z, w); }
    public Vector2f toVector2f() { return new Vector2f((float) x, (float) y); }
    public double[] get(double[] dest) { dest[0] = x; dest[1] = y; return dest; }

    public String toString() { return "(" + x + ", " + y + ")"; }
}

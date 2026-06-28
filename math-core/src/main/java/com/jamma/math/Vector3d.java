package com.jamma.math;

import com.jamma.math.quaternion.Quaterniond;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector3d(double x, double y, double z) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector3d() { this(0.0, 0.0, 0.0); }
    public Vector3d(Vector3d v) { this(v.x, v.y, v.z); }
    public Vector3d(double[] values) { this(values[0], values[1], values[2]); }
    public Vector3d(Vector2d v, double z) { this(v.x(), v.y(), z); }

    public static Vector3d fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector3d(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16)
        );
    }

    public static Vector3d read(MemorySegment segment, long offset) {
        return fromMemorySegment(segment, offset);
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, z);
    }

    public void write(MemorySegment segment, long offset) {
        writeToMemorySegment(segment, offset);
    }

    public static Vector3d fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector3d(src.get(), src.get(), src.get());
    }

    public static Vector3d fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector3d(src.get(index), src.get(index + 1), src.get(index + 2));
    }

    public java.nio.DoubleBuffer writeToBuffer(java.nio.DoubleBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        return dest;
    }

    public java.nio.DoubleBuffer writeToBuffer(int index, java.nio.DoubleBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        return dest;
    }

    public Vector3d add(Vector3d v) { return new Vector3d(x + v.x, y + v.y, z + v.z); }
    public Vector3d add(double x, double y, double z) { return new Vector3d(this.x + x, this.y + y, this.z + z); }
    public Vector3d sub(Vector3d v) { return new Vector3d(x - v.x, y - v.y, z - v.z); }
    public Vector3d sub(double x, double y, double z) { return new Vector3d(this.x - x, this.y - y, this.z - z); }
    public Vector3d mul(Vector3d v) { return new Vector3d(x * v.x, y * v.y, z * v.z); }
    public Vector3d mul(double scalar) { return new Vector3d(x * scalar, y * scalar, z * scalar); }
    public Vector3d mul(double x, double y, double z) { return new Vector3d(this.x * x, this.y * y, this.z * z); }
    public Vector3d div(Vector3d v) { return new Vector3d(x / v.x, y / v.y, z / v.z); }
    public Vector3d div(double scalar) { return new Vector3d(x / scalar, y / scalar, z / scalar); }
    public Vector3d div(double x, double y, double z) { return new Vector3d(this.x / x, this.y / y, this.z / z); }
    public Vector3d scale(double s) { return new Vector3d(x * s, y * s, z * s); }
    public Vector3d negate() { return new Vector3d(-x, -y, -z); }
    public Vector3d abs() { return new Vector3d(Math.abs(x), Math.abs(y), Math.abs(z)); }
    public double dot(Vector3d v) { return Math.fma(x, v.x, Math.fma(y, v.y, z * v.z)); }
    public Vector3d cross(Vector3d v) { return new Vector3d(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x); }
    public double length() { return Math.sqrt(dot(this)); }
    public double lengthSquared() { return dot(this); }
    public double distance(Vector3d v) { return Math.sqrt(distanceSquared(v)); }
    public double distance(double x, double y, double z) { return Math.sqrt(distanceSquared(x, y, z)); }
    public double distanceSquared(Vector3d v) { return distanceSquared(v.x, v.y, v.z); }
    public double distanceSquared(double x, double y, double z) { return Math.fma(this.x - x, this.x - x, Math.fma(this.y - y, this.y - y, (this.z - z) * (this.z - z))); }
    public Vector3d normalize() { double len = length(); return new Vector3d(x / len, y / len, z / len); }
    public Vector3d normalize(double length) { return scale(length / length()); }
    public double angle(Vector3d v) { return Math.acos(Math.clamp(dot(v) / (length() * v.length()), -1.0, 1.0)); }
    public double angleSigned(Vector3d v, Vector3d normal) { return Math.atan2(cross(v).dot(normal), dot(v)); }
    public Vector3d reflect(Vector3d normal) { double d = 2.0 * dot(normal); return new Vector3d(x - d * normal.x, y - d * normal.y, z - d * normal.z); }
    public Vector3d project(Vector3d onto) {
        double denom = onto.dot(onto);
        if (denom == 0.0) return new Vector3d(0.0, 0.0, 0.0);
        double s = dot(onto) / denom;
        return onto.scale(s);
    }
    public Vector3d reject(Vector3d onto) { return sub(project(onto)); }
    public Vector3d lerp(Vector3d other, double t) { return new Vector3d(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y), Math.fma(t, other.z - z, z)); }
    public Vector3d nlerp(Vector3d other, double t) { return lerp(other, t).normalize(); }
    public Vector3d rotate(double ang, double axisX, double axisY, double axisZ) {
        double axisLenSq = axisX * axisX + axisY * axisY + axisZ * axisZ;
        if (axisLenSq == 0.0) {
            return this;
        }
        double c = Math.cos(ang);
        double s = Math.sin(ang);
        double oc = 1.0 - c;
        double invLen = 1.0 / Math.sqrt(axisLenSq);
        double ux = axisX * invLen;
        double uy = axisY * invLen;
        double uz = axisZ * invLen;
        double dot = x * ux + y * uy + z * uz;
        return new Vector3d(
            x * c + (uy * z - uz * y) * s + ux * dot * oc,
            y * c + (uz * x - ux * z) * s + uy * dot * oc,
            z * c + (ux * y - uy * x) * s + uz * dot * oc
        );
    }
    public Vector3d rotate(double ang, Vector3d axis) { return rotate(ang, axis.x(), axis.y(), axis.z()); }
    public Vector3d rotate(Quaterniond quat) { return quat.transform(this); }
    public Vector3d halfway(Vector3d other) { return new Vector3d(x + other.x, y + other.y, z + other.z).normalize(); }
    public Vector3d ceil() { return new Vector3d(Math.ceil(x), Math.ceil(y), Math.ceil(z)); }
    public Vector3d floor() { return new Vector3d(Math.floor(x), Math.floor(y), Math.floor(z)); }
    public Vector3d round() { return new Vector3d(Math.round(x), Math.round(y), Math.round(z)); }
    public Vector3d min(Vector3d v) { return new Vector3d(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z)); }
    public Vector3d max(Vector3d v) { return new Vector3d(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z)); }
    public Vector3d fma(Vector3d a, Vector3d b) { return new Vector3d(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y), Math.fma(z, a.z, b.z)); }
    public Vector3d fma(double a, Vector3d b) { return new Vector3d(Math.fma(a, x, b.x), Math.fma(a, y, b.y), Math.fma(a, z, b.z)); }
    public Vector3d orthogonalize(Vector3d v) { return sub(project(v)); }
    public Vector3d orthogonalizeUnit(Vector3d v) { return orthogonalize(v).normalize(); }

    public boolean isPerpendicular(Vector3d v, double epsilon) { return Math.abs(dot(v)) <= epsilon; }
    public boolean equals(Vector3d other, double delta) { return Math.abs(x - other.x) <= delta && Math.abs(y - other.y) <= delta && Math.abs(z - other.z) <= delta; }

    public double minComponent() { return Math.min(Math.min(x, y), z); }
    public double maxComponent() { return Math.max(Math.max(x, y), z); }
    public int maxComponentIndex() { return x >= y && x >= z ? 0 : y >= z ? 1 : 2; }
    public int minComponentIndex() { return x <= y && x <= z ? 0 : y <= z ? 1 : 2; }
    public Vector2d toVector2d() { return new Vector2d(x, y); }
    public Vector4d toVector4d() { return new Vector4d(x, y, z, 0.0); }
    public Vector4d toVector4d(double w) { return new Vector4d(x, y, z, w); }
    public Vector3f toVector3f() { return new Vector3f((float) x, (float) y, (float) z); }
    public double[] get(double[] dest) { dest[0] = x; dest[1] = y; dest[2] = z; return dest; }

    public String toString() { return "(" + x + ", " + y + ", " + z + ")"; }
}

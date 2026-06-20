package com.jamma.math;

import com.jamma.math.quaternion.Quaterniond;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector3D(double x, double y, double z) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector3D() { this(0.0, 0.0, 0.0); }
    public Vector3D(Vector3D v) { this(v.x, v.y, v.z); }
    public Vector3D(double[] values) { this(values[0], values[1], values[2]); }
    public Vector3D(Vector2D v, double z) { this(v.x(), v.y(), z); }

    public static Vector3D fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector3D(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16)
        );
    }

    public static Vector3D read(MemorySegment segment, long offset) {
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

    public static Vector3D fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector3D(src.get(), src.get(), src.get());
    }

    public static Vector3D fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector3D(src.get(index), src.get(index + 1), src.get(index + 2));
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

    public Vector3D add(Vector3D v) { return new Vector3D(x + v.x, y + v.y, z + v.z); }
    public Vector3D add(double x, double y, double z) { return new Vector3D(this.x + x, this.y + y, this.z + z); }
    public Vector3D sub(Vector3D v) { return new Vector3D(x - v.x, y - v.y, z - v.z); }
    public Vector3D sub(double x, double y, double z) { return new Vector3D(this.x - x, this.y - y, this.z - z); }
    public Vector3D mul(Vector3D v) { return new Vector3D(x * v.x, y * v.y, z * v.z); }
    public Vector3D mul(double scalar) { return new Vector3D(x * scalar, y * scalar, z * scalar); }
    public Vector3D mul(double x, double y, double z) { return new Vector3D(this.x * x, this.y * y, this.z * z); }
    public Vector3D div(Vector3D v) { return new Vector3D(x / v.x, y / v.y, z / v.z); }
    public Vector3D div(double scalar) { return new Vector3D(x / scalar, y / scalar, z / scalar); }
    public Vector3D div(double x, double y, double z) { return new Vector3D(this.x / x, this.y / y, this.z / z); }
    public Vector3D scale(double s) { return new Vector3D(x * s, y * s, z * s); }
    public Vector3D negate() { return new Vector3D(-x, -y, -z); }
    public Vector3D abs() { return new Vector3D(Math.abs(x), Math.abs(y), Math.abs(z)); }
    public Vector3D absolute() { return abs(); }
    public double dot(Vector3D v) { return Math.fma(x, v.x, Math.fma(y, v.y, z * v.z)); }
    public Vector3D cross(Vector3D v) { return new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x); }
    public double length() { return Math.sqrt(dot(this)); }
    public double lengthSquared() { return dot(this); }
    public double distance(Vector3D v) { return sub(v).length(); }
    public double distance(double x, double y, double z) { return sub(x, y, z).length(); }
    public double distanceSquared(Vector3D v) { return sub(v).lengthSquared(); }
    public double distanceSquared(double x, double y, double z) { return sub(x, y, z).lengthSquared(); }
    public Vector3D normalize() { double len = length(); return new Vector3D(x / len, y / len, z / len); }
    public Vector3D normalize(double length) { return scale(length / length()); }
    public double angle(Vector3D v) { return Math.acos(clamp(dot(v) / (length() * v.length()), -1.0, 1.0)); }
    public double angleSigned(Vector3D v, Vector3D normal) { return Math.atan2(cross(v).dot(normal), dot(v)); }
    public Vector3D reflect(Vector3D normal) { double d = 2.0 * dot(normal); return new Vector3D(x - d * normal.x, y - d * normal.y, z - d * normal.z); }
    public Vector3D project(Vector3D onto) { double s = dot(onto) / onto.dot(onto); return onto.scale(s); }
    public Vector3D reject(Vector3D onto) { return sub(project(onto)); }
    public Vector3D lerp(Vector3D other, double t) { return new Vector3D(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y), Math.fma(t, other.z - z, z)); }
    public Vector3D nlerp(Vector3D other, double t) { return lerp(other, t).normalize(); }
    public Vector3D rotate(double ang, double axisX, double axisY, double axisZ) {
        double c = Math.cos(ang);
        double s = Math.sin(ang);
        double oc = 1.0 - c;
        double invLen = 1.0 / Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        double ux = axisX * invLen;
        double uy = axisY * invLen;
        double uz = axisZ * invLen;
        double dot = x * ux + y * uy + z * uz;
        return new Vector3D(
            x * c + (uy * z - uz * y) * s + ux * dot * oc,
            y * c + (uz * x - ux * z) * s + uy * dot * oc,
            z * c + (ux * y - uy * x) * s + uz * dot * oc
        );
    }
    public Vector3D rotate(double ang, Vector3D axis) { return rotate(ang, axis.x(), axis.y(), axis.z()); }
    public Vector3D rotate(Quaterniond quat) { return quat.transform(this); }
    public Vector3D halfway(Vector3D other) { return new Vector3D((x + other.x) * 0.5, (y + other.y) * 0.5, (z + other.z) * 0.5); }
    public Vector3D ceil() { return new Vector3D(Math.ceil(x), Math.ceil(y), Math.ceil(z)); }
    public Vector3D floor() { return new Vector3D(Math.floor(x), Math.floor(y), Math.floor(z)); }
    public Vector3D round() { return new Vector3D(Math.round(x), Math.round(y), Math.round(z)); }
    public Vector3D min(Vector3D v) { return new Vector3D(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z)); }
    public Vector3D max(Vector3D v) { return new Vector3D(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z)); }
    public Vector3D fma(Vector3D a, Vector3D b) { return new Vector3D(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y), Math.fma(z, a.z, b.z)); }
    public Vector3D fma(double a, Vector3D b) { return new Vector3D(Math.fma(a, x, b.x), Math.fma(a, y, b.y), Math.fma(a, z, b.z)); }
    public Vector3D orthogonalize(Vector3D v) { return sub(project(v)); }
    public Vector3D orthogonalizeUnit(Vector3D v) { return orthogonalize(v).normalize(); }

    public boolean isPerpendicular(Vector3D v, double epsilon) { return Math.abs(dot(v)) <= epsilon; }
    public boolean equals(Vector3D other, double delta) { return Math.abs(x - other.x) <= delta && Math.abs(y - other.y) <= delta && Math.abs(z - other.z) <= delta; }

    public double minComponent() { return Math.min(Math.min(x, y), z); }
    public double maxComponent() { return Math.max(Math.max(x, y), z); }
    public int maxComponentIndex() { return x >= y && x >= z ? 0 : y >= z ? 1 : 2; }
    public int minComponentIndex() { return x <= y && x <= z ? 0 : y <= z ? 1 : 2; }
    public Vector2D toVector2D() { return new Vector2D(x, y); }
    public Vector4D toVector4D() { return new Vector4D(x, y, z, 0.0); }
    public Vector4D toVector4D(double w) { return new Vector4D(x, y, z, w); }
    public Vector3f toVector3f() { return new Vector3f((float) x, (float) y, (float) z); }
    public double[] get(double[] dest) { dest[0] = x; dest[1] = y; dest[2] = z; return dest; }

    public String toString() { return "(" + x + ", " + y + ", " + z + ")"; }

    private static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}

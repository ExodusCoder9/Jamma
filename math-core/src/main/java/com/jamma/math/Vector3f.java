package com.jamma.math;

import com.jamma.math.quaternion.Quaternionf;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

/**
 * A 3-component single-precision vector stored as an immutable record.
 * <p>
 * All operations return new instances. For allocation-free workloads, use the
 * corresponding static methods in {@link VectorMathf} which write into caller-provided arrays.
 * <p>
 * Memory layout: x, y, z (contiguous, 4 bytes each).
 */
public record Vector3f(float x, float y, float z) implements Serializable {

    public static final Vector3f ZERO = new Vector3f(0.0f, 0.0f, 0.0f);
    public static final Vector3f UNIT_X = new Vector3f(1.0f, 0.0f, 0.0f);
    public static final Vector3f UNIT_Y = new Vector3f(0.0f, 1.0f, 0.0f);
    public static final Vector3f UNIT_Z = new Vector3f(0.0f, 0.0f, 1.0f);

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector3f() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vector3f(Vector3f v) {
        this(v.x, v.y, v.z);
    }

    public Vector3f(float[] values) {
        this(values[0], values[1], values[2]);
    }

    public Vector3f(Vector2f v, float z) {
        this(v.x(), v.y(), z);
    }

    public static Vector3f fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector3f(
            src.get(ValueLayout.JAVA_FLOAT, byteOffset),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, x);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, y);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, z);
    }

    public static Vector3f fromBuffer(FloatBuffer src) {
        return new Vector3f(src.get(), src.get(), src.get());
    }

    public static Vector3f fromBuffer(int index, FloatBuffer src) {
        return new Vector3f(src.get(index), src.get(index + 1), src.get(index + 2));
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        return dest;
    }

    public Vector3f add(Vector3f v) { return new Vector3f(x + v.x, y + v.y, z + v.z); }
    public Vector3f add(float x, float y, float z) { return new Vector3f(this.x + x, this.y + y, this.z + z); }
    public Vector3f sub(Vector3f v) { return new Vector3f(x - v.x, y - v.y, z - v.z); }
    public Vector3f sub(float x, float y, float z) { return new Vector3f(this.x - x, this.y - y, this.z - z); }
    public Vector3f mul(Vector3f v) { return new Vector3f(x * v.x, y * v.y, z * v.z); }
    public Vector3f mul(float scalar) { return new Vector3f(x * scalar, y * scalar, z * scalar); }
    public Vector3f mul(float x, float y, float z) { return new Vector3f(this.x * x, this.y * y, this.z * z); }
    public Vector3f div(Vector3f v) { return new Vector3f(x / v.x, y / v.y, z / v.z); }
    public Vector3f div(float scalar) { return new Vector3f(x / scalar, y / scalar, z / scalar); }
    public Vector3f div(float x, float y, float z) { return new Vector3f(this.x / x, this.y / y, this.z / z); }
    public Vector3f scale(float s) { return new Vector3f(x * s, y * s, z * s); }
    public Vector3f negate() { return new Vector3f(-x, -y, -z); }
    public Vector3f abs() { return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z)); }
    public float dot(Vector3f v) { return Math.fma(x, v.x, Math.fma(y, v.y, z * v.z)); }
    public Vector3f cross(Vector3f v) { return new Vector3f(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x); }
    public float length() { return (float) Math.sqrt(dot(this)); }
    public float lengthSquared() { return dot(this); }
    public float distance(Vector3f v) { return (float) Math.sqrt(distanceSquared(v)); }
    public float distance(float x, float y, float z) { return (float) Math.sqrt(Math.fma(this.x - x, this.x - x, Math.fma(this.y - y, this.y - y, (this.z - z) * (this.z - z)))); }
    public float distanceSquared(Vector3f v) { return distanceSquared(v.x, v.y, v.z); }
    public float distanceSquared(float x, float y, float z) { return Math.fma(this.x - x, this.x - x, Math.fma(this.y - y, this.y - y, (this.z - z) * (this.z - z))); }
    public Vector3f normalize() { float len = length(); return new Vector3f(x / len, y / len, z / len); }
    public Vector3f normalize(float length) { float len = length(); float s = length / len; return new Vector3f(x * s, y * s, z * s); }
    public float angle(Vector3f v) { return (float) Math.acos(Math.clamp(dot(v) / (length() * v.length()), -1.0f, 1.0f)); }
    public float angleSigned(Vector3f v, Vector3f normal) { return (float) Math.atan2(cross(v).dot(normal), dot(v)); }
    public Vector3f reflect(Vector3f normal) { float d = 2.0f * dot(normal); return new Vector3f(x - d * normal.x, y - d * normal.y, z - d * normal.z); }
    public Vector3f project(Vector3f onto) {
        float denom = onto.dot(onto);
        if (denom == 0.0f) return new Vector3f(0.0f, 0.0f, 0.0f);
        float s = dot(onto) / denom;
        return onto.scale(s);
    }
    public Vector3f reject(Vector3f onto) { return sub(project(onto)); }
    public Vector3f lerp(Vector3f other, float t) { return new Vector3f(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y), Math.fma(t, other.z - z, z)); }
    public Vector3f nlerp(Vector3f other, float t) { return lerp(other, t).normalize(); }
    public Vector3f rotate(float ang, float axisX, float axisY, float axisZ) {
        float axisLenSq = axisX * axisX + axisY * axisY + axisZ * axisZ;
        if (axisLenSq == 0.0f) {
            return this;
        }
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float oc = 1.0f - c;
        float invLen = 1.0f / (float) Math.sqrt(axisLenSq);
        float ux = axisX * invLen, uy = axisY * invLen, uz = axisZ * invLen;
        return new Vector3f(
            x * (c + ux * ux * oc) + y * (ux * uy * oc - uz * s) + z * (ux * uz * oc + uy * s),
            x * (uy * ux * oc + uz * s) + y * (c + uy * uy * oc) + z * (uy * uz * oc - ux * s),
            x * (uz * ux * oc - uy * s) + y * (uz * uy * oc + ux * s) + z * (c + uz * uz * oc)
        );
    }
    public Vector3f rotate(float ang, Vector3f axis) { return rotate(ang, axis.x, axis.y, axis.z); }
    public Vector3f rotate(Quaternionf quat) {
        float qx = quat.x(), qy = quat.y(), qz = quat.z(), qw = quat.w();
        float cx = qy * z - qz * y;
        float cy = qz * x - qx * z;
        float cz = qx * y - qy * x;
        float cxx = qy * cz - qz * cy;
        float cyy = qz * cx - qx * cz;
        float czz = qx * cy - qy * cx;
        return new Vector3f(
            x + 2.0f * (qw * cx + cxx),
            y + 2.0f * (qw * cy + cyy),
            z + 2.0f * (qw * cz + czz)
        );
    }
    public Vector3f halfway(Vector3f other) { return new Vector3f(x + other.x, y + other.y, z + other.z).normalize(); }
    public Vector3f ceil() { return new Vector3f((float) Math.ceil(x), (float) Math.ceil(y), (float) Math.ceil(z)); }
    public Vector3f floor() { return new Vector3f((float) Math.floor(x), (float) Math.floor(y), (float) Math.floor(z)); }
    public Vector3f round() { return new Vector3f((float) Math.round(x), (float) Math.round(y), (float) Math.round(z)); }
    public Vector3f min(Vector3f v) { return new Vector3f(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z)); }
    public Vector3f max(Vector3f v) { return new Vector3f(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z)); }
    public Vector3f fma(Vector3f a, Vector3f b) { return new Vector3f(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y), Math.fma(z, a.z, b.z)); }
    public Vector3f fma(float a, Vector3f b) { return new Vector3f(Math.fma(a, x, b.x), Math.fma(a, y, b.y), Math.fma(a, z, b.z)); }
    public Vector3f orthogonalize(Vector3f v) {
        float d = dot(v);
        float lenSq = v.dot(v);
        float s = d / lenSq;
        return new Vector3f(x - s * v.x, y - s * v.y, z - s * v.z);
    }
    public Vector3f orthogonalizeUnit(Vector3f v) {
        float d = dot(v);
        return new Vector3f(x - d * v.x, y - d * v.y, z - d * v.z);
    }
    public boolean isPerpendicular(Vector3f v, float epsilon) { return Math.abs(dot(v)) < epsilon; }
    public boolean equals(Vector3f other, float delta) { return Math.abs(x - other.x) < delta && Math.abs(y - other.y) < delta && Math.abs(z - other.z) < delta; }
    public float minComponent() { return Math.min(Math.min(x, y), z); }
    public float maxComponent() { return Math.max(Math.max(x, y), z); }
    public int maxComponentIndex() { return x >= y && x >= z ? 0 : y >= z ? 1 : 2; }
    public int minComponentIndex() { return x <= y && x <= z ? 0 : y <= z ? 1 : 2; }
    public Vector2f toVector2f() { return new Vector2f(x, y); }
    public Vector4f toVector4f() { return new Vector4f(x, y, z, 0.0f); }
    public Vector4f toVector4f(float w) { return new Vector4f(x, y, z, w); }
    public Vector3d toVector3d() { return new Vector3d(x, y, z); }
    public float[] get(float[] dest) { dest[0] = x; dest[1] = y; dest[2] = z; return dest; }

    @Override
    public String toString() {
        return "Vector3f(" + x + ", " + y + ", " + z + ")";
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, y);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, z);
    }

    public static Vector3f read(MemorySegment segment, long offset) {
        return new Vector3f(
            segment.get(ValueLayout.JAVA_FLOAT, offset),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 4),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 8)
        );
    }
}

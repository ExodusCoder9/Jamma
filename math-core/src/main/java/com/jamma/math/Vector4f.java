package com.jamma.math;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

public record Vector4f(float x, float y, float z, float w) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector4f() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Vector4f(Vector4f v) {
        this(v.x, v.y, v.z, v.w);
    }

    public Vector4f(float[] values) {
        this(values[0], values[1], values[2], values[3]);
    }

    public Vector4f(Vector2f v, float z, float w) {
        this(v.x(), v.y(), z, w);
    }

    public Vector4f(Vector3f v, float w) {
        this(v.x(), v.y(), v.z(), w);
    }

    public static Vector4f fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector4f(
            src.get(ValueLayout.JAVA_FLOAT, byteOffset),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 12)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, x);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, y);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, z);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 12, w);
    }

    public static Vector4f fromBuffer(FloatBuffer src) {
        return new Vector4f(src.get(), src.get(), src.get(), src.get());
    }

    public static Vector4f fromBuffer(int index, FloatBuffer src) {
        return new Vector4f(src.get(index), src.get(index + 1), src.get(index + 2), src.get(index + 3));
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        dest.put(w);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        dest.put(index + 3, w);
        return dest;
    }

    public Vector4f add(Vector4f v) { return new Vector4f(x + v.x, y + v.y, z + v.z, w + v.w); }
    public Vector4f add(float x, float y, float z, float w) { return new Vector4f(this.x + x, this.y + y, this.z + z, this.w + w); }
    public Vector4f sub(Vector4f v) { return new Vector4f(x - v.x, y - v.y, z - v.z, w - v.w); }
    public Vector4f sub(float x, float y, float z, float w) { return new Vector4f(this.x - x, this.y - y, this.z - z, this.w - w); }
    public Vector4f mul(Vector4f v) { return new Vector4f(x * v.x, y * v.y, z * v.z, w * v.w); }
    public Vector4f mul(float scalar) { return new Vector4f(x * scalar, y * scalar, z * scalar, w * scalar); }
    public Vector4f mul(float x, float y, float z, float w) { return new Vector4f(this.x * x, this.y * y, this.z * z, this.w * w); }
    public Vector4f div(Vector4f v) { return new Vector4f(x / v.x, y / v.y, z / v.z, w / v.w); }
    public Vector4f div(float scalar) { return new Vector4f(x / scalar, y / scalar, z / scalar, w / scalar); }
    public Vector4f div(float x, float y, float z, float w) { return new Vector4f(this.x / x, this.y / y, this.z / z, this.w / w); }
    public Vector4f scale(float s) { return new Vector4f(x * s, y * s, z * s, w * s); }
    public Vector4f negate() { return new Vector4f(-x, -y, -z, -w); }
    public Vector4f abs() { return new Vector4f(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w)); }
    public float dot(Vector4f v) { return Math.fma(x, v.x, Math.fma(y, v.y, Math.fma(z, v.z, w * v.w))); }
    public float length() { return (float) Math.sqrt(dot(this)); }
    public float lengthSquared() { return dot(this); }
    public float distance(Vector4f v) { return (float) Math.sqrt(distanceSquared(v)); }
    public float distance(float x, float y, float z, float w) { return (float) Math.sqrt(distanceSquared(x, y, z, w)); }
    public float distanceSquared(Vector4f v) { return distanceSquared(v.x, v.y, v.z, v.w); }
    public float distanceSquared(float x, float y, float z, float w) { return Math.fma(this.x - x, this.x - x, Math.fma(this.y - y, this.y - y, Math.fma(this.z - z, this.z - z, (this.w - w) * (this.w - w)))); }
    public Vector4f normalize() {
        float lenSq = lengthSquared();
        if (lenSq == 0.0f) return new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
        float invLen = 1.0f / (float) Math.sqrt(lenSq);
        return new Vector4f(x * invLen, y * invLen, z * invLen, w * invLen);
    }
    public Vector4f normalize(float length) {
        float lenSq = lengthSquared();
        if (lenSq == 0.0f) return new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
        float s = length / (float) Math.sqrt(lenSq);
        return new Vector4f(x * s, y * s, z * s, w * s);
    }
    public Vector4f lerp(Vector4f other, float t) { return new Vector4f(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y), Math.fma(t, other.z - z, z), Math.fma(t, other.w - w, w)); }
    public Vector4f ceil() { return new Vector4f((float) Math.ceil(x), (float) Math.ceil(y), (float) Math.ceil(z), (float) Math.ceil(w)); }
    public Vector4f floor() { return new Vector4f((float) Math.floor(x), (float) Math.floor(y), (float) Math.floor(z), (float) Math.floor(w)); }
    public Vector4f round() { return new Vector4f((float) Math.round(x), (float) Math.round(y), (float) Math.round(z), (float) Math.round(w)); }
    public Vector4f min(Vector4f v) { return new Vector4f(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z), Math.min(w, v.w)); }
    public Vector4f max(Vector4f v) { return new Vector4f(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z), Math.max(w, v.w)); }
    public Vector4f fma(Vector4f a, Vector4f b) { return new Vector4f(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y), Math.fma(z, a.z, b.z), Math.fma(w, a.w, b.w)); }
    public Vector4f fma(float a, Vector4f b) { return new Vector4f(Math.fma(a, x, b.x), Math.fma(a, y, b.y), Math.fma(a, z, b.z), Math.fma(a, w, b.w)); }
    public float angle(Vector4f v) {
        float lenSq = lengthSquared();
        float otherLenSq = v.lengthSquared();
        if (lenSq == 0.0f || otherLenSq == 0.0f) return 0.0f;
        return (float) Math.acos(Math.clamp(dot(v) / (float) Math.sqrt(lenSq * otherLenSq), -1.0f, 1.0f));
    }
    public boolean equals(Vector4f other, float delta) { return Math.abs(x - other.x) < delta && Math.abs(y - other.y) < delta && Math.abs(z - other.z) < delta && Math.abs(w - other.w) < delta; }
    public float minComponent() { return Math.min(Math.min(x, y), Math.min(z, w)); }
    public float maxComponent() { return Math.max(Math.max(x, y), Math.max(z, w)); }
    public int maxComponentIndex() {
        if (x >= y && x >= z && x >= w) return 0;
        if (y >= z && y >= w) return 1;
        if (z >= w) return 2;
        return 3;
    }
    public int minComponentIndex() {
        if (x <= y && x <= z && x <= w) return 0;
        if (y <= z && y <= w) return 1;
        if (z <= w) return 2;
        return 3;
    }
    public Vector2f xy() { return new Vector2f(x, y); }
    public Vector3f xyz() { return new Vector3f(x, y, z); }
    public Vector2f toVector2f() { return new Vector2f(x, y); }
    public Vector3f toVector3f() { return new Vector3f(x, y, z); }
    public Vector4d toVector4d() { return new Vector4d(x, y, z, w); }
    public float[] get(float[] dest) { dest[0] = x; dest[1] = y; dest[2] = z; dest[3] = w; return dest; }

    @Override
    public String toString() {
        return "Vector4f(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, y);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, z);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 12, w);
    }

    public static Vector4f read(MemorySegment segment, long offset) {
        return new Vector4f(
            segment.get(ValueLayout.JAVA_FLOAT, offset),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 4),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 8),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 12)
        );
    }
}

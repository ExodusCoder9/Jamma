package com.jamma.math;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

public record Vector2f(float x, float y) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector2f() {
        this(0.0f, 0.0f);
    }

    public Vector2f(Vector2f v) {
        this(v.x, v.y);
    }

    public Vector2f(float[] values) {
        this(values[0], values[1]);
    }

    public static Vector2f fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector2f(
            src.get(ValueLayout.JAVA_FLOAT, byteOffset),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, x);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, y);
    }

    public static Vector2f fromBuffer(FloatBuffer src) {
        return new Vector2f(src.get(), src.get());
    }

    public static Vector2f fromBuffer(int index, FloatBuffer src) {
        return new Vector2f(src.get(index), src.get(index + 1));
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(x);
        dest.put(y);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        return dest;
    }

    public Vector2f add(Vector2f v) { return new Vector2f(x + v.x, y + v.y); }
    public Vector2f add(float x, float y) { return new Vector2f(this.x + x, this.y + y); }
    public Vector2f sub(Vector2f v) { return new Vector2f(x - v.x, y - v.y); }
    public Vector2f sub(float x, float y) { return new Vector2f(this.x - x, this.y - y); }
    public Vector2f mul(Vector2f v) { return new Vector2f(x * v.x, y * v.y); }
    public Vector2f mul(float scalar) { return new Vector2f(x * scalar, y * scalar); }
    public Vector2f mul(float x, float y) { return new Vector2f(this.x * x, this.y * y); }
    public Vector2f div(Vector2f v) { return new Vector2f(x / v.x, y / v.y); }
    public Vector2f div(float scalar) { return new Vector2f(x / scalar, y / scalar); }
    public Vector2f div(float x, float y) { return new Vector2f(this.x / x, this.y / y); }
    public Vector2f scale(float s) { return new Vector2f(x * s, y * s); }
    public Vector2f negate() { return new Vector2f(-x, -y); }
    public Vector2f abs() { return new Vector2f(Math.abs(x), Math.abs(y)); }
    public float dot(Vector2f v) { return Math.fma(x, v.x, y * v.y); }
    public float length() { return (float) Math.sqrt(dot(this)); }
    public float lengthSquared() { return dot(this); }
    public float distance(Vector2f v) { return (float) Math.sqrt(distanceSquared(v)); }
    public float distance(float x, float y) { return (float) Math.sqrt(distanceSquared(x, y)); }
    public float distanceSquared(Vector2f v) { return Math.fma(this.x - v.x, this.x - v.x, (this.y - v.y) * (this.y - v.y)); }
    public float distanceSquared(float x, float y) { return Math.fma(this.x - x, this.x - x, (this.y - y) * (this.y - y)); }
    public Vector2f normalize() { float len = length(); return new Vector2f(x / len, y / len); }
    public Vector2f normalize(float length) { float len = length(); float s = length / len; return new Vector2f(x * s, y * s); }
    public float angle(Vector2f v) { return (float) Math.acos(Math.clamp(dot(v) / (length() * v.length()), -1.0f, 1.0f)); }
    public float angleSigned(Vector2f v) { return (float) Math.atan2(x * v.y - y * v.x, dot(v)); }
    public Vector2f reflect(Vector2f normal) { float d = 2.0f * dot(normal); return new Vector2f(x - d * normal.x, y - d * normal.y); }
    public Vector2f project(Vector2f onto) {
        float denom = onto.dot(onto);
        if (denom == 0.0f) return new Vector2f(0.0f, 0.0f);
        float s = dot(onto) / denom;
        return onto.scale(s);
    }
    public Vector2f reject(Vector2f onto) { return sub(project(onto)); }
    public Vector2f lerp(Vector2f other, float t) { return new Vector2f(Math.fma(t, other.x - x, x), Math.fma(t, other.y - y, y)); }
    @SuppressWarnings("SuspiciousNameCombination")
    public Vector2f perpendicular() { return new Vector2f(-y, x); }
    public Vector2f rotate(float angle) { float c = (float) Math.cos(angle); float s = (float) Math.sin(angle); return new Vector2f(x * c - y * s, x * s + y * c); }
    public Vector2f halfway(Vector2f other) { return new Vector2f(x + other.x, y + other.y).normalize(); }
    public Vector2f ceil() { return new Vector2f((float) Math.ceil(x), (float) Math.ceil(y)); }
    public Vector2f floor() { return new Vector2f((float) Math.floor(x), (float) Math.floor(y)); }
    public Vector2f round() { return new Vector2f((float) Math.round(x), (float) Math.round(y)); }
    public Vector2f min(Vector2f v) { return new Vector2f(Math.min(x, v.x), Math.min(y, v.y)); }
    public Vector2f max(Vector2f v) { return new Vector2f(Math.max(x, v.x), Math.max(y, v.y)); }
    public Vector2f fma(Vector2f a, Vector2f b) { return new Vector2f(Math.fma(x, a.x, b.x), Math.fma(y, a.y, b.y)); }
    public Vector2f fma(float a, Vector2f b) { return new Vector2f(Math.fma(a, x, b.x), Math.fma(a, y, b.y)); }
    public boolean isPerpendicular(Vector2f v, float epsilon) { return Math.abs(dot(v)) < epsilon; }
    public boolean equals(Vector2f other, float delta) { return Math.abs(x - other.x) < delta && Math.abs(y - other.y) < delta; }
    public float minComponent() { return Math.min(x, y); }
    public float maxComponent() { return Math.max(x, y); }
    public int maxComponentIndex() { return x >= y ? 0 : 1; }
    public int minComponentIndex() { return x <= y ? 0 : 1; }
    public Vector3f toVector3f() { return new Vector3f(x, y, 0.0f); }
    public Vector3f toVector3f(float z) { return new Vector3f(x, y, z); }
    public Vector4f toVector4f() { return new Vector4f(x, y, 0.0f, 0.0f); }
    public Vector4f toVector4f(float z, float w) { return new Vector4f(x, y, z, w); }
    public Vector2d toVector2d() { return new Vector2d(x, y); }
    public float[] get(float[] dest) { dest[0] = x; dest[1] = y; return dest; }

    @Override
    public String toString() {
        return "Vector2f(" + x + ", " + y + ")";
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, y);
    }

    public static Vector2f read(MemorySegment segment, long offset) {
        return new Vector2f(
            segment.get(ValueLayout.JAVA_FLOAT, offset),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 4)
        );
    }
}

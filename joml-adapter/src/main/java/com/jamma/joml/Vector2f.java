package com.jamma.joml;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vector2f {

    public float x;
    public float y;

    public Vector2f() {
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2f(float[] f) {
        this.x = f[0];
        this.y = f[1];
    }

    public Vector2f(com.jamma.math.Vector2f jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
    }

    public com.jamma.math.Vector2f toJamma() {
        return new com.jamma.math.Vector2f(x, y);
    }

    public static Vector2f fromJamma(com.jamma.math.Vector2f jamma) {
        return new Vector2f(jamma.x(), jamma.y());
    }

    public Vector2f set(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(float[] f) {
        this.x = f[0];
        this.y = f[1];
        return this;
    }

    public Vector2f add(Vector2f v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vector2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f sub(Vector2f v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector2f sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2f mul(Vector2f v) {
        x *= v.x;
        y *= v.y;
        return this;
    }

    public Vector2f mul(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vector2f mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2f div(Vector2f v) {
        x /= v.x;
        y /= v.y;
        return this;
    }

    public Vector2f div(float scalar) {
        x /= scalar;
        y /= scalar;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public Vector2f normalize() {
        float inv = 1.0f / length();
        x *= inv;
        y *= inv;
        return this;
    }

    public Vector2f negate() {
        x = -x;
        y = -y;
        return this;
    }

    public float dot(Vector2f v) {
        return Math.fma(x, v.x, y * v.y);
    }

    public float distance(Vector2f v) {
        float dx = x - v.x;
        float dy = y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float distanceSquared(Vector2f v) {
        float dx = x - v.x;
        float dy = y - v.y;
        return dx * dx + dy * dy;
    }

    public float cross(Vector2f v) {
        return x * v.y - y * v.x;
    }

    public float angle(Vector2f v) {
        float d = dot(v) / (length() * v.length());
        return (float) Math.acos(Math.clamp(d, -1.0f, 1.0f));
    }

    public Vector2f lerp(Vector2f other, float t) {
        x = Math.fma(t, other.x - x, x);
        y = Math.fma(t, other.y - y, y);
        return this;
    }

    public Vector2f rotate(float ang) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float tx = x * c - y * s;
        float ty = x * s + y * c;
        x = tx;
        y = ty;
        return this;
    }

    public Vector2f perpendicular() {
        float tx = -y;
        y = x;
        x = tx;
        return this;
    }

    public Vector2f zero() {
        x = y = 0.0f;
        return this;
    }

    public Vector2f abs() {
        x = Math.abs(x);
        y = Math.abs(y);
        return this;
    }

    public float get(int component) {
        return switch (component) {
            case 0 -> x;
            case 1 -> y;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public float[] get(float[] dest) {
        dest[0] = x;
        dest[1] = y;
        return dest;
    }

    public Vector2f load(MemorySegment segment, long offset) {
        x = segment.get(ValueLayout.JAVA_FLOAT, offset);
        y = segment.get(ValueLayout.JAVA_FLOAT, offset + 4);
        return this;
    }

    public Vector2f store(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, y);
        return this;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x) ^ Float.hashCode(y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2f v)) return false;
        return Float.floatToIntBits(x) == Float.floatToIntBits(v.x)
            && Float.floatToIntBits(y) == Float.floatToIntBits(v.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

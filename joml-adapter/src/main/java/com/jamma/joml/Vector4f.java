package com.jamma.joml;

import com.jamma.math.matrix.Matrix4f;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vector4f {

    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f() {
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector4f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    public Vector4f(float[] f) {
        this.x = f[0];
        this.y = f[1];
        this.z = f[2];
        this.w = f[3];
    }

    public Vector4f(com.jamma.math.Vector4f jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
        this.w = jamma.w();
    }

    public com.jamma.math.Vector4f toJamma() {
        return new com.jamma.math.Vector4f(x, y, z, w);
    }

    public static Vector4f fromJamma(com.jamma.math.Vector4f jamma) {
        return new Vector4f(jamma.x(), jamma.y(), jamma.z(), jamma.w());
    }

    public Vector4f set(Vector4f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    public Vector4f set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4f set(float[] f) {
        this.x = f[0];
        this.y = f[1];
        this.z = f[2];
        this.w = f[3];
        return this;
    }

    public Vector4f add(Vector4f v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += v.w;
        return this;
    }

    public Vector4f add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    public Vector4f sub(Vector4f v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= v.w;
        return this;
    }

    public Vector4f mul(Vector4f v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        w *= v.w;
        return this;
    }

    public Vector4f mul(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        w *= scalar;
        return this;
    }

    public Vector4f div(Vector4f v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
        w /= v.w;
        return this;
    }

    public Vector4f div(float scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        w /= scalar;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public Vector4f normalize() {
        float inv = 1.0f / length();
        x *= inv;
        y *= inv;
        z *= inv;
        w *= inv;
        return this;
    }

    public Vector4f negate() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
        return this;
    }

    public float dot(Vector4f v) {
        return Math.fma(x, v.x, Math.fma(y, v.y, Math.fma(z, v.z, w * v.w)));
    }

    public Vector4f mul(Matrix4f m) {
        float rx = Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03 * w)));
        float ry = Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13 * w)));
        float rz = Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23 * w)));
        float rw = Math.fma(m.m30, x, Math.fma(m.m31, y, Math.fma(m.m32, z, m.m33 * w)));
        x = rx;
        y = ry;
        z = rz;
        w = rw;
        return this;
    }

    public Vector4f lerp(Vector4f other, float t) {
        x = Math.fma(t, other.x - x, x);
        y = Math.fma(t, other.y - y, y);
        z = Math.fma(t, other.z - z, z);
        w = Math.fma(t, other.w - w, w);
        return this;
    }

    public Vector4f mulPosition(Matrix4f m) {
        float rx = Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03)));
        float ry = Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13)));
        float rz = Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23)));
        float rw = Math.fma(m.m30, x, Math.fma(m.m31, y, Math.fma(m.m32, z, m.m33)));
        float invW = 1.0f / rw;
        x = rx * invW;
        y = ry * invW;
        z = rz * invW;
        w = 1.0f;
        return this;
    }

    public Vector4f mulDirection(Matrix4f m) {
        float rx = Math.fma(m.m00, x, Math.fma(m.m01, y, m.m02 * z));
        float ry = Math.fma(m.m10, x, Math.fma(m.m11, y, m.m12 * z));
        float rz = Math.fma(m.m20, x, Math.fma(m.m21, y, m.m22 * z));
        x = rx;
        y = ry;
        z = rz;
        return this;
    }

    public Vector4f zero() {
        x = y = z = w = 0.0f;
        return this;
    }

    public Vector4f abs() {
        x = Math.abs(x);
        y = Math.abs(y);
        z = Math.abs(z);
        w = Math.abs(w);
        return this;
    }

    public Vector4f min(Vector4f v) {
        x = Math.min(x, v.x);
        y = Math.min(y, v.y);
        z = Math.min(z, v.z);
        w = Math.min(w, v.w);
        return this;
    }

    public Vector4f max(Vector4f v) {
        x = Math.max(x, v.x);
        y = Math.max(y, v.y);
        z = Math.max(z, v.z);
        w = Math.max(w, v.w);
        return this;
    }

    public float get(int component) {
        return switch (component) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            case 3 -> w;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public float[] get(float[] dest) {
        dest[0] = x;
        dest[1] = y;
        dest[2] = z;
        dest[3] = w;
        return dest;
    }

    public Vector4f load(MemorySegment segment, long offset) {
        x = segment.get(ValueLayout.JAVA_FLOAT, offset);
        y = segment.get(ValueLayout.JAVA_FLOAT, offset + 4);
        z = segment.get(ValueLayout.JAVA_FLOAT, offset + 8);
        w = segment.get(ValueLayout.JAVA_FLOAT, offset + 12);
        return this;
    }

    public Vector4f store(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, y);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, z);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 12, w);
        return this;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x) ^ Float.hashCode(y) ^ Float.hashCode(z) ^ Float.hashCode(w);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector4f v)) return false;
        return Float.floatToIntBits(x) == Float.floatToIntBits(v.x)
            && Float.floatToIntBits(y) == Float.floatToIntBits(v.y)
            && Float.floatToIntBits(z) == Float.floatToIntBits(v.z)
            && Float.floatToIntBits(w) == Float.floatToIntBits(v.w);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}

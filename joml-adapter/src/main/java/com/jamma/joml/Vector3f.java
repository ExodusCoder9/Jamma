package com.jamma.joml;

import com.jamma.math.Quaternionf;
import com.jamma.math.matrix.Matrix4f;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vector3f {

    public float x;
    public float y;
    public float z;

    public Vector3f() {
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3f(float[] f) {
        this.x = f[0];
        this.y = f[1];
        this.z = f[2];
    }

    public Vector3f(com.jamma.math.Vector3f jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
    }

    public com.jamma.math.Vector3f toJamma() {
        return new com.jamma.math.Vector3f(x, y, z);
    }

    public static Vector3f fromJamma(com.jamma.math.Vector3f jamma) {
        return new Vector3f(jamma.x(), jamma.y(), jamma.z());
    }

    public Vector3f set(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(float[] f) {
        this.x = f[0];
        this.y = f[1];
        this.z = f[2];
        return this;
    }

    public Vector3f set(com.jamma.math.Vector3f jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
        return this;
    }

    public Vector3f add(Vector3f v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f sub(Vector3f v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vector3f sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3f mul(Vector3f v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        return this;
    }

    public Vector3f mul(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    public Vector3f mul(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3f div(Vector3f v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
        return this;
    }

    public Vector3f div(float scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        return this;
    }

    public Vector3f div(float x, float y, float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3f normalize() {
        float inv = 1.0f / length();
        x *= inv;
        y *= inv;
        z *= inv;
        return this;
    }

    public Vector3f negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public float dot(Vector3f v) {
        return Math.fma(x, v.x, Math.fma(y, v.y, z * v.z));
    }

    public Vector3f cross(Vector3f v) {
        float tx = Math.fma(y, v.z, -z * v.y);
        float ty = Math.fma(z, v.x, -x * v.z);
        float tz = Math.fma(x, v.y, -y * v.x);
        x = tx;
        y = ty;
        z = tz;
        return this;
    }

    public float distance(Vector3f v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public float distanceSquared(Vector3f v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public Vector3f lerp(Vector3f other, float t) {
        x = Math.fma(t, other.x - x, x);
        y = Math.fma(t, other.y - y, y);
        z = Math.fma(t, other.z - z, z);
        return this;
    }

    public Vector3f reflect(Vector3f normal) {
        float d = 2.0f * dot(normal);
        x -= d * normal.x;
        y -= d * normal.y;
        z -= d * normal.z;
        return this;
    }

    public Vector3f rotate(float ang, float axisX, float axisY, float axisZ) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float oc = 1.0f - c;
        float invLen = 1.0f / (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        float ux = axisX * invLen;
        float uy = axisY * invLen;
        float uz = axisZ * invLen;
        float d = x * ux + y * uy + z * uz;
        float tx = x * c + (uy * z - uz * y) * s + ux * d * oc;
        float ty = y * c + (uz * x - ux * z) * s + uy * d * oc;
        float tz = z * c + (ux * y - uy * x) * s + uz * d * oc;
        x = tx;
        y = ty;
        z = tz;
        return this;
    }

    public Vector3f rotate(Quaternionf quat) {
        return transform(quat);
    }

    public Vector3f transform(Quaternionf q) {
        float qx = q.x(), qy = q.y(), qz = q.z(), qw = q.w();
        float tx = 2.0f * (qy * z - qz * y);
        float ty = 2.0f * (qz * x - qx * z);
        float tz = 2.0f * (qx * y - qy * x);
        x += qw * tx + (qy * tz - qz * ty);
        y += qw * ty + (qz * tx - qx * tz);
        z += qw * tz + (qx * ty - qy * tx);
        return this;
    }

    public Vector3f mul(Matrix4f m) {
        float rx = Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03)));
        float ry = Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13)));
        float rz = Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23)));
        x = rx;
        y = ry;
        z = rz;
        return this;
    }

    public Vector3f mulPosition(Matrix4f m) {
        float w = Math.fma(m.m30, x, Math.fma(m.m31, y, Math.fma(m.m32, z, m.m33)));
        float invW = 1.0f / w;
        float rx = (Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03)))) * invW;
        float ry = (Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13)))) * invW;
        float rz = (Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23)))) * invW;
        x = rx;
        y = ry;
        z = rz;
        return this;
    }

    public Vector3f mulDirection(Matrix4f m) {
        float rx = Math.fma(m.m00, x, Math.fma(m.m01, y, m.m02 * z));
        float ry = Math.fma(m.m10, x, Math.fma(m.m11, y, m.m12 * z));
        float rz = Math.fma(m.m20, x, Math.fma(m.m21, y, m.m22 * z));
        x = rx;
        y = ry;
        z = rz;
        return this;
    }

    public Vector3f abs() {
        x = Math.abs(x);
        y = Math.abs(y);
        z = Math.abs(z);
        return this;
    }

    public Vector3f min(Vector3f v) {
        x = Math.min(x, v.x);
        y = Math.min(y, v.y);
        z = Math.min(z, v.z);
        return this;
    }

    public Vector3f max(Vector3f v) {
        x = Math.max(x, v.x);
        y = Math.max(y, v.y);
        z = Math.max(z, v.z);
        return this;
    }

    public Vector3f clamp(Vector3f min, Vector3f max) {
        x = Math.clamp(x, min.x, max.x);
        y = Math.clamp(y, min.y, max.y);
        z = Math.clamp(z, min.z, max.z);
        return this;
    }

    public Vector3f zero() {
        x = y = z = 0.0f;
        return this;
    }

    public Vector3f perpendicular() {
        float tx = -y;
        float ty = x;
        x = tx;
        y = ty;
        return this;
    }

    public Vector3f project(Vector3f onto) {
        float s = dot(onto) / onto.lengthSquared();
        x = onto.x * s;
        y = onto.y * s;
        z = onto.z * s;
        return this;
    }

    public Vector3f reflect(float nx, float ny, float nz) {
        float d = 2.0f * (x * nx + y * ny + z * nz);
        x -= d * nx;
        y -= d * ny;
        z -= d * nz;
        return this;
    }

    public Vector3f rotateX(float ang) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float ty = y * c - z * s;
        float tz = y * s + z * c;
        y = ty;
        z = tz;
        return this;
    }

    public Vector3f rotateY(float ang) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float tx = x * c + z * s;
        float tz = -x * s + z * c;
        x = tx;
        z = tz;
        return this;
    }

    public Vector3f rotateZ(float ang) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float tx = x * c - y * s;
        float ty = x * s + y * c;
        x = tx;
        y = ty;
        return this;
    }

    public Vector3f fma(Vector3f a, Vector3f b) {
        x = Math.fma(x, a.x, b.x);
        y = Math.fma(y, a.y, b.y);
        z = Math.fma(z, a.z, b.z);
        return this;
    }

    public Vector3f fma(float a, Vector3f b) {
        x = Math.fma(a, x, b.x);
        y = Math.fma(a, y, b.y);
        z = Math.fma(a, z, b.z);
        return this;
    }

    public Vector3f ceil() {
        x = (float) Math.ceil(x);
        y = (float) Math.ceil(y);
        z = (float) Math.ceil(z);
        return this;
    }

    public Vector3f floor() {
        x = (float) Math.floor(x);
        y = (float) Math.floor(y);
        z = (float) Math.floor(z);
        return this;
    }

    public Vector3f round() {
        x = Math.round(x);
        y = Math.round(y);
        z = Math.round(z);
        return this;
    }

    public boolean isFinite() {
        return Float.isFinite(x) && Float.isFinite(y) && Float.isFinite(z);
    }

    public float minComponent() {
        return Math.min(Math.min(x, y), z);
    }

    public float maxComponent() {
        return Math.max(Math.max(x, y), z);
    }

    public int minComponentIndex() {
        return x <= y && x <= z ? 0 : y <= z ? 1 : 2;
    }

    public int maxComponentIndex() {
        return x >= y && x >= z ? 0 : y >= z ? 1 : 2;
    }

    public Vector3f normalize(float length) {
        float inv = 1.0f / length();
        x *= inv * length;
        y *= inv * length;
        z *= inv * length;
        return this;
    }

    public float get(int component) {
        return switch (component) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public Vector3f setComponent(int component, float value) {
        switch (component) {
            case 0 -> x = value;
            case 1 -> y = value;
            case 2 -> z = value;
            default -> throw new IndexOutOfBoundsException();
        }
        return this;
    }

    public float[] get(float[] dest) {
        dest[0] = x;
        dest[1] = y;
        dest[2] = z;
        return dest;
    }

    public Vector3f load(MemorySegment segment, long offset) {
        x = segment.get(ValueLayout.JAVA_FLOAT, offset);
        y = segment.get(ValueLayout.JAVA_FLOAT, offset + 4);
        z = segment.get(ValueLayout.JAVA_FLOAT, offset + 8);
        return this;
    }

    public Vector3f store(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, y);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, z);
        return this;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x) ^ Float.hashCode(y) ^ Float.hashCode(z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector3f v)) return false;
        return Float.floatToIntBits(x) == Float.floatToIntBits(v.x)
            && Float.floatToIntBits(y) == Float.floatToIntBits(v.y)
            && Float.floatToIntBits(z) == Float.floatToIntBits(v.z);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}

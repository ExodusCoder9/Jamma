package com.jamma.joml;

import com.jamma.math.quaternion.Quaterniond;
import com.jamma.math.matrix.Matrix4d;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vector3d {

    public double x;
    public double y;
    public double z;

    public Vector3d() {
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3d(double[] d) {
        this.x = d[0];
        this.y = d[1];
        this.z = d[2];
    }

    public Vector3d(com.jamma.math.Vector3d jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
    }

    public com.jamma.math.Vector3d toJamma() {
        return new com.jamma.math.Vector3d(x, y, z);
    }

    public static Vector3d fromJamma(com.jamma.math.Vector3d jamma) {
        return new Vector3d(jamma.x(), jamma.y(), jamma.z());
    }

    public Vector3d set(Vector3d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3d set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3d set(double[] d) {
        this.x = d[0];
        this.y = d[1];
        this.z = d[2];
        return this;
    }

    public Vector3d set(com.jamma.math.Vector3d jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
        return this;
    }

    public Vector3d add(Vector3d v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector3d add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3d sub(Vector3d v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vector3d sub(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3d mul(Vector3d v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        return this;
    }

    public Vector3d mul(double scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    public Vector3d mul(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3d div(Vector3d v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
        return this;
    }

    public Vector3d div(double scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        return this;
    }

    public Vector3d div(double x, double y, double z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3d normalize() {
        double inv = 1.0 / length();
        x *= inv;
        y *= inv;
        z *= inv;
        return this;
    }

    public Vector3d negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public double dot(Vector3d v) {
        return Math.fma(x, v.x, Math.fma(y, v.y, z * v.z));
    }

    public Vector3d cross(Vector3d v) {
        double tx = Math.fma(y, v.z, -z * v.y);
        double ty = Math.fma(z, v.x, -x * v.z);
        double tz = Math.fma(x, v.y, -y * v.x);
        x = tx;
        y = ty;
        z = tz;
        return this;
    }

    public double distance(Vector3d v) {
        double dx = x - v.x;
        double dy = y - v.y;
        double dz = z - v.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceSquared(Vector3d v) {
        double dx = x - v.x;
        double dy = y - v.y;
        double dz = z - v.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public Vector3d lerp(Vector3d other, double t) {
        x = Math.fma(t, other.x - x, x);
        y = Math.fma(t, other.y - y, y);
        z = Math.fma(t, other.z - z, z);
        return this;
    }

    public Vector3d reflect(Vector3d normal) {
        double d = 2.0 * dot(normal);
        x -= d * normal.x;
        y -= d * normal.y;
        z -= d * normal.z;
        return this;
    }

    public Vector3d rotate(double ang, double axisX, double axisY, double axisZ) {
        double c = Math.cos(ang);
        double s = Math.sin(ang);
        double oc = 1.0 - c;
        double invLen = 1.0 / Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        double ux = axisX * invLen;
        double uy = axisY * invLen;
        double uz = axisZ * invLen;
        double d = x * ux + y * uy + z * uz;
        double tx = x * c + (uy * z - uz * y) * s + ux * d * oc;
        double ty = y * c + (uz * x - ux * z) * s + uy * d * oc;
        double tz = z * c + (ux * y - uy * x) * s + uz * d * oc;
        x = tx;
        y = ty;
        z = tz;
        return this;
    }

    public Vector3d rotate(Quaterniond quat) {
        return transform(quat);
    }

    public Vector3d transform(Quaterniond q) {
        double qx = q.x(), qy = q.y(), qz = q.z(), qw = q.w();
        double tx = 2.0 * (qy * z - qz * y);
        double ty = 2.0 * (qz * x - qx * z);
        double tz = 2.0 * (qx * y - qy * x);
        x += qw * tx + (qy * tz - qz * ty);
        y += qw * ty + (qz * tx - qx * tz);
        z += qw * tz + (qx * ty - qy * tx);
        return this;
    }

    public Vector3d mul(Matrix4d m) {
        double rx = Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03)));
        double ry = Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13)));
        double rz = Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23)));
        x = rx;
        y = ry;
        z = rz;
        return this;
    }

    public Vector3d mulPosition(Matrix4d m) {
        double w = Math.fma(m.m30, x, Math.fma(m.m31, y, Math.fma(m.m32, z, m.m33)));
        double invW = 1.0 / w;
        double rx = (Math.fma(m.m00, x, Math.fma(m.m01, y, Math.fma(m.m02, z, m.m03)))) * invW;
        double ry = (Math.fma(m.m10, x, Math.fma(m.m11, y, Math.fma(m.m12, z, m.m13)))) * invW;
        double rz = (Math.fma(m.m20, x, Math.fma(m.m21, y, Math.fma(m.m22, z, m.m23)))) * invW;
        x = rx;
        y = ry;
        z = rz;
        return this;
    }

    public Vector3d mulDirection(Matrix4d m) {
        double rx = Math.fma(m.m00, x, Math.fma(m.m01, y, m.m02 * z));
        double ry = Math.fma(m.m10, x, Math.fma(m.m11, y, m.m12 * z));
        double rz = Math.fma(m.m20, x, Math.fma(m.m21, y, m.m22 * z));
        x = rx;
        y = ry;
        z = rz;
        return this;
    }

    public Vector3d abs() {
        x = Math.abs(x);
        y = Math.abs(y);
        z = Math.abs(z);
        return this;
    }

    public Vector3d min(Vector3d v) {
        x = Math.min(x, v.x);
        y = Math.min(y, v.y);
        z = Math.min(z, v.z);
        return this;
    }

    public Vector3d max(Vector3d v) {
        x = Math.max(x, v.x);
        y = Math.max(y, v.y);
        z = Math.max(z, v.z);
        return this;
    }

    public Vector3d clamp(Vector3d min, Vector3d max) {
        x = Math.clamp(x, min.x, max.x);
        y = Math.clamp(y, min.y, max.y);
        z = Math.clamp(z, min.z, max.z);
        return this;
    }

    public Vector3d zero() {
        x = y = z = 0.0;
        return this;
    }

    public Vector3d perpendicular() {
        double tx = -y;
        double ty = x;
        x = tx;
        y = ty;
        return this;
    }

    public Vector3d project(Vector3d onto) {
        double s = dot(onto) / onto.lengthSquared();
        x = onto.x * s;
        y = onto.y * s;
        z = onto.z * s;
        return this;
    }

    public Vector3d rotateX(double ang) {
        double c = Math.cos(ang);
        double s = Math.sin(ang);
        double ty = y * c - z * s;
        double tz = y * s + z * c;
        y = ty;
        z = tz;
        return this;
    }

    public Vector3d rotateY(double ang) {
        double c = Math.cos(ang);
        double s = Math.sin(ang);
        double tx = x * c + z * s;
        double tz = -x * s + z * c;
        x = tx;
        z = tz;
        return this;
    }

    public Vector3d rotateZ(double ang) {
        double c = Math.cos(ang);
        double s = Math.sin(ang);
        double tx = x * c - y * s;
        double ty = x * s + y * c;
        x = tx;
        y = ty;
        return this;
    }

    public Vector3d fma(Vector3d a, Vector3d b) {
        x = Math.fma(x, a.x, b.x);
        y = Math.fma(y, a.y, b.y);
        z = Math.fma(z, a.z, b.z);
        return this;
    }

    public Vector3d fma(double a, Vector3d b) {
        x = Math.fma(a, x, b.x);
        y = Math.fma(a, y, b.y);
        z = Math.fma(a, z, b.z);
        return this;
    }

    public boolean isFinite() {
        return Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z);
    }

    public double minComponent() {
        return Math.min(Math.min(x, y), z);
    }

    public double maxComponent() {
        return Math.max(Math.max(x, y), z);
    }

    public int minComponentIndex() {
        return x <= y && x <= z ? 0 : y <= z ? 1 : 2;
    }

    public int maxComponentIndex() {
        return x >= y && x >= z ? 0 : y >= z ? 1 : 2;
    }

    public Vector3d normalize(double length) {
        double inv = 1.0 / length();
        x *= inv * length;
        y *= inv * length;
        z *= inv * length;
        return this;
    }

    public double get(int component) {
        return switch (component) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public Vector3d setComponent(int component, double value) {
        switch (component) {
            case 0 -> x = value;
            case 1 -> y = value;
            case 2 -> z = value;
            default -> throw new IndexOutOfBoundsException();
        }
        return this;
    }

    public double[] get(double[] dest) {
        dest[0] = x;
        dest[1] = y;
        dest[2] = z;
        return dest;
    }

    public Vector3d load(MemorySegment segment, long offset) {
        x = segment.get(ValueLayout.JAVA_DOUBLE, offset);
        y = segment.get(ValueLayout.JAVA_DOUBLE, offset + 8);
        z = segment.get(ValueLayout.JAVA_DOUBLE, offset + 16);
        return this;
    }

    public Vector3d store(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_DOUBLE, offset, x);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 8, y);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 16, z);
        return this;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector3d v)) return false;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(v.x)
            && Double.doubleToLongBits(y) == Double.doubleToLongBits(v.y)
            && Double.doubleToLongBits(z) == Double.doubleToLongBits(v.z);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}

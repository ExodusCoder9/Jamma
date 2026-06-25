package com.jamma.math.geometry;

import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix4f;

import java.io.Serial;
import java.io.Serializable;

public class Rayf implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public Vector3f origin;
    public Vector3f direction;

    public Rayf(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Rayf(float ox, float oy, float oz, float dx, float dy, float dz) {
        this.origin = new Vector3f(ox, oy, oz);
        this.direction = new Vector3f(dx, dy, dz);
    }

    public Vector3f getPoint(float t, Vector3f dest) {
        return new Vector3f(
            origin.x() + t * direction.x(),
            origin.y() + t * direction.y(),
            origin.z() + t * direction.z()
        );
    }

    public float distanceSquared(Vector3f point) {
        float dx = point.x() - origin.x();
        float dy = point.y() - origin.y();
        float dz = point.z() - origin.z();
        float dot = dx * direction.x() + dy * direction.y() + dz * direction.z();
        float lenSq = direction.x() * direction.x() + direction.y() * direction.y() + direction.z() * direction.z();
        float t = Math.max(0.0f, dot / lenSq);
        float cx = origin.x() + t * direction.x();
        float cy = origin.y() + t * direction.y();
        float cz = origin.z() + t * direction.z();
        dx = point.x() - cx;
        dy = point.y() - cy;
        dz = point.z() - cz;
        return dx * dx + dy * dy + dz * dz;
    }

    public Vector3f closestPoint(Vector3f point, Vector3f dest) {
        float dx = point.x() - origin.x();
        float dy = point.y() - origin.y();
        float dz = point.z() - origin.z();
        float dot = dx * direction.x() + dy * direction.y() + dz * direction.z();
        float lenSq = direction.x() * direction.x() + direction.y() * direction.y() + direction.z() * direction.z();
        float t = Math.max(0.0f, dot / lenSq);
        return new Vector3f(
            origin.x() + t * direction.x(),
            origin.y() + t * direction.y(),
            origin.z() + t * direction.z()
        );
    }

    public float intersectsAABB(AABBf box) {
        return box.intersectsRay(origin, direction);
    }

    public float intersectsPlane(float nx, float ny, float nz, float d, Vector3f dest) {
        float dot = nx * direction.x() + ny * direction.y() + nz * direction.z();
        if (Math.abs(dot) < 1e-30f) return Float.NaN;
        float t = -(nx * origin.x() + ny * origin.y() + nz * origin.z() + d) / dot;
        return t >= 0.0f ? t : Float.NaN;
    }

    public float intersectsSphere(Vector3f center, float radius) {
        float dx = origin.x() - center.x();
        float dy = origin.y() - center.y();
        float dz = origin.z() - center.z();
        float a = direction.x() * direction.x() + direction.y() * direction.y() + direction.z() * direction.z();
        float b = 2.0f * (dx * direction.x() + dy * direction.y() + dz * direction.z());
        float c = dx * dx + dy * dy + dz * dz - radius * radius;
        float disc = b * b - 4.0f * a * c;
        if (disc < 0.0f) return -1.0f;
        float sqrtDisc = (float) Math.sqrt(disc);
        float t1 = (-b - sqrtDisc) / (2.0f * a);
        float t2 = (-b + sqrtDisc) / (2.0f * a);
        if (t1 >= 0.0f) return t1;
        if (t2 >= 0.0f) return t2;
        return -1.0f;
    }

    public Rayf transform(Matrix4f m) {
        float x = origin.x(), y = origin.y(), z = origin.z();
        float w = 1.0f / (m.m03() * x + m.m13() * y + m.m23() * z + m.m33());
        float ox = (m.m00() * x + m.m10() * y + m.m20() * z + m.m30()) * w;
        float oy = (m.m01() * x + m.m11() * y + m.m21() * z + m.m31()) * w;
        float oz = (m.m02() * x + m.m12() * y + m.m22() * z + m.m32()) * w;
        float dx = m.m00() * direction.x() + m.m10() * direction.y() + m.m20() * direction.z();
        float dy = m.m01() * direction.x() + m.m11() * direction.y() + m.m21() * direction.z();
        float dz = m.m02() * direction.x() + m.m12() * direction.y() + m.m22() * direction.z();
        origin = new Vector3f(ox, oy, oz);
        direction = new Vector3f(dx, dy, dz);
        return this;
    }

    public Rayf normalize() {
        float invLen = 1.0f / (float) Math.sqrt(
            direction.x() * direction.x() +
            direction.y() * direction.y() +
            direction.z() * direction.z()
        );
        direction = new Vector3f(
            direction.x() * invLen,
            direction.y() * invLen,
            direction.z() * invLen
        );
        return this;
    }

    @Override
    public String toString() {
        return "Rayf[origin=" + origin + ", direction=" + direction + "]";
    }
}

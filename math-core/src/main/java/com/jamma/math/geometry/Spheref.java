package com.jamma.math.geometry;

import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix4f;

import java.io.Serial;
import java.io.Serializable;

public record Spheref(Vector3f center, float radius) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Spheref(float cx, float cy, float cz, float radius) {
        this(new Vector3f(cx, cy, cz), radius);
    }

    public boolean contains(Vector3f point) {
        float dx = point.x() - center.x();
        float dy = point.y() - center.y();
        float dz = point.z() - center.z();
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    public boolean intersects(Spheref other) {
        float dx = other.center().x() - center.x();
        float dy = other.center().y() - center.y();
        float dz = other.center().z() - center.z();
        float sum = radius + other.radius();
        return dx * dx + dy * dy + dz * dz <= sum * sum;
    }

    public float intersects(Rayf ray) {
        float dx = ray.origin.x() - center.x();
        float dy = ray.origin.y() - center.y();
        float dz = ray.origin.z() - center.z();
        float a = ray.direction.x() * ray.direction.x()
                 + ray.direction.y() * ray.direction.y()
                 + ray.direction.z() * ray.direction.z();
        float b = 2.0f * (dx * ray.direction.x() + dy * ray.direction.y() + dz * ray.direction.z());
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

    public Spheref transform(Matrix4f m) {
        float x = center.x(), y = center.y(), z = center.z();
        float w = 1.0f / (m.m03() * x + m.m13() * y + m.m23() * z + m.m33());
        float cx = (m.m00() * x + m.m10() * y + m.m20() * z + m.m30()) * w;
        float cy = (m.m01() * x + m.m11() * y + m.m21() * z + m.m31()) * w;
        float cz = (m.m02() * x + m.m12() * y + m.m22() * z + m.m32()) * w;
        float sx = m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02();
        float sy = m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12();
        float sz = m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22();
        float r = radius * (float) Math.sqrt(Math.max(sx, Math.max(sy, sz)));
        return new Spheref(cx, cy, cz, r);
    }
}

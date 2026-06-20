package com.jamma.math.geometry;

import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix4d;
import java.io.Serializable;

public record Sphere(Vector3d center, double radius) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Sphere(double cx, double cy, double cz, double radius) {
        this(new Vector3d(cx, cy, cz), radius);
    }

    public boolean contains(Vector3d point) {
        double dx = point.x() - center.x();
        double dy = point.y() - center.y();
        double dz = point.z() - center.z();
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    public boolean intersects(Sphere other) {
        double dx = other.center().x() - center.x();
        double dy = other.center().y() - center.y();
        double dz = other.center().z() - center.z();
        double sum = radius + other.radius();
        return dx * dx + dy * dy + dz * dz <= sum * sum;
    }

    public double intersects(Ray ray) {
        double dx = ray.origin.x() - center.x();
        double dy = ray.origin.y() - center.y();
        double dz = ray.origin.z() - center.z();
        double a = ray.direction.x() * ray.direction.x()
                 + ray.direction.y() * ray.direction.y()
                 + ray.direction.z() * ray.direction.z();
        double b = 2.0 * (dx * ray.direction.x() + dy * ray.direction.y() + dz * ray.direction.z());
        double c = dx * dx + dy * dy + dz * dz - radius * radius;
        double disc = b * b - 4.0 * a * c;
        if (disc < 0.0) return -1.0;
        double sqrtDisc = Math.sqrt(disc);
        double t1 = (-b - sqrtDisc) / (2.0 * a);
        double t2 = (-b + sqrtDisc) / (2.0 * a);
        if (t1 >= 0.0) return t1;
        if (t2 >= 0.0) return t2;
        return -1.0;
    }

    public Sphere transform(Matrix4d m) {
        double x = center.x(), y = center.y(), z = center.z();
        double w = 1.0 / (m.m03() * x + m.m13() * y + m.m23() * z + m.m33());
        double cx = (m.m00() * x + m.m10() * y + m.m20() * z + m.m30()) * w;
        double cy = (m.m01() * x + m.m11() * y + m.m21() * z + m.m31()) * w;
        double cz = (m.m02() * x + m.m12() * y + m.m22() * z + m.m32()) * w;
        double sx = m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02();
        double sy = m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12();
        double sz = m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22();
        double maxScale = Math.sqrt(Math.max(sx, Math.max(sy, sz)));
        return new Sphere(cx, cy, cz, radius * maxScale);
    }

    @Override
    public String toString() {
        return "Sphere[center=" + center + ", radius=" + radius + "]";
    }
}

package com.jamma.math.geometry;

import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix4d;

import java.io.Serial;
import java.io.Serializable;

public class Ray implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public Vector3d origin;
    public Vector3d direction;

    public Ray(Vector3d origin, Vector3d direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Ray(double ox, double oy, double oz, double dx, double dy, double dz) {
        this.origin = new Vector3d(ox, oy, oz);
        this.direction = new Vector3d(dx, dy, dz);
    }

    public Vector3d getPoint(double t, Vector3d dest) {
        return new Vector3d(
            origin.x() + t * direction.x(),
            origin.y() + t * direction.y(),
            origin.z() + t * direction.z()
        );
    }

    public double distanceSquared(Vector3d point) {
        double dx = point.x() - origin.x();
        double dy = point.y() - origin.y();
        double dz = point.z() - origin.z();
        double dot = dx * direction.x() + dy * direction.y() + dz * direction.z();
        double lenSq = direction.x() * direction.x() + direction.y() * direction.y() + direction.z() * direction.z();
        double t = Math.max(0.0, dot / lenSq);
        double cx = origin.x() + t * direction.x();
        double cy = origin.y() + t * direction.y();
        double cz = origin.z() + t * direction.z();
        dx = point.x() - cx;
        dy = point.y() - cy;
        dz = point.z() - cz;
        return dx * dx + dy * dy + dz * dz;
    }

    public Vector3d closestPoint(Vector3d point, Vector3d dest) {
        double dx = point.x() - origin.x();
        double dy = point.y() - origin.y();
        double dz = point.z() - origin.z();
        double dot = dx * direction.x() + dy * direction.y() + dz * direction.z();
        double lenSq = direction.x() * direction.x() + direction.y() * direction.y() + direction.z() * direction.z();
        double t = Math.max(0.0, dot / lenSq);
        return new Vector3d(
            origin.x() + t * direction.x(),
            origin.y() + t * direction.y(),
            origin.z() + t * direction.z()
        );
    }

    public double intersectsAABB(AABB box) {
        return box.intersectsRay(origin, direction);
    }

    public double intersectsPlane(double nx, double ny, double nz, double d, Vector3d dest) {
        double dot = nx * direction.x() + ny * direction.y() + nz * direction.z();
        if (Math.abs(dot) < 1e-30) return Double.NaN;
        double t = -(nx * origin.x() + ny * origin.y() + nz * origin.z() + d) / dot;
        return t >= 0.0 ? t : Double.NaN;
    }

    public double intersectsSphere(Vector3d center, double radius) {
        double dx = origin.x() - center.x();
        double dy = origin.y() - center.y();
        double dz = origin.z() - center.z();
        double a = direction.x() * direction.x() + direction.y() * direction.y() + direction.z() * direction.z();
        double b = 2.0 * (dx * direction.x() + dy * direction.y() + dz * direction.z());
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

    public Ray transform(Matrix4d m) {
        double x = origin.x(), y = origin.y(), z = origin.z();
        double w = 1.0 / (m.m03() * x + m.m13() * y + m.m23() * z + m.m33());
        double ox = (m.m00() * x + m.m10() * y + m.m20() * z + m.m30()) * w;
        double oy = (m.m01() * x + m.m11() * y + m.m21() * z + m.m31()) * w;
        double oz = (m.m02() * x + m.m12() * y + m.m22() * z + m.m32()) * w;
        double dx = m.m00() * direction.x() + m.m10() * direction.y() + m.m20() * direction.z();
        double dy = m.m01() * direction.x() + m.m11() * direction.y() + m.m21() * direction.z();
        double dz = m.m02() * direction.x() + m.m12() * direction.y() + m.m22() * direction.z();
        origin = new Vector3d(ox, oy, oz);
        direction = new Vector3d(dx, dy, dz);
        return this;
    }

    public Ray normalize() {
        double invLen = 1.0 / Math.sqrt(
            direction.x() * direction.x() +
            direction.y() * direction.y() +
            direction.z() * direction.z()
        );
        direction = new Vector3d(
            direction.x() * invLen,
            direction.y() * invLen,
            direction.z() * invLen
        );
        return this;
    }

    @Override
    public String toString() {
        return "Ray[origin=" + origin + ", direction=" + direction + "]";
    }
}

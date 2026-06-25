package com.jamma.math.geometry;

import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix4d;

import java.io.Serial;
import java.io.Serializable;

public record Plane(Vector3d normal, double d) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Plane(double nx, double ny, double nz, double d) {
        this(new Vector3d(nx, ny, nz), d);
    }

    public static Plane fromPointNormal(Vector3d point, Vector3d normal) {
        double invLen = 1.0 / Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        double nx = normal.x() * invLen;
        double ny = normal.y() * invLen;
        double nz = normal.z() * invLen;
        return new Plane(nx, ny, nz, -(nx * point.x() + ny * point.y() + nz * point.z()));
    }

    public static Plane fromPoints(Vector3d a, Vector3d b, Vector3d c) {
        double abx = b.x() - a.x(), aby = b.y() - a.y(), abz = b.z() - a.z();
        double acx = c.x() - a.x(), acy = c.y() - a.y(), acz = c.z() - a.z();
        double nx = aby * acz - abz * acy;
        double ny = abz * acx - abx * acz;
        double nz = abx * acy - aby * acx;
        double invLen = 1.0 / Math.sqrt(nx * nx + ny * ny + nz * nz);
        nx *= invLen;
        ny *= invLen;
        nz *= invLen;
        return new Plane(nx, ny, nz, -(nx * a.x() + ny * a.y() + nz * a.z()));
    }

    public double distance(Vector3d point) {
        return normal.x() * point.x() + normal.y() * point.y() + normal.z() * point.z() + d;
    }

    public double intersectRay(Ray ray) {
        double dot = normal.x() * ray.direction.x() + normal.y() * ray.direction.y() + normal.z() * ray.direction.z();
        if (Math.abs(dot) < 1e-30) return Double.NaN;
        double t = -(normal.x() * ray.origin.x() + normal.y() * ray.origin.y() + normal.z() * ray.origin.z() + d) / dot;
        return t >= 0.0 ? t : Double.NaN;
    }

    public Plane normalize() {
        double invLen = 1.0 / Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        return new Plane(normal.x() * invLen, normal.y() * invLen, normal.z() * invLen, d * invLen);
    }

    public Plane transform(Matrix4d m) {
        double nx = normal.x(), ny = normal.y(), nz = normal.z();
        double mx = m.m00() * nx + m.m10() * ny + m.m20() * nz;
        double my = m.m01() * nx + m.m11() * ny + m.m21() * nz;
        double mz = m.m02() * nx + m.m12() * ny + m.m22() * nz;
        double md = d - (mx * m.m30() + my * m.m31() + mz * m.m32());
        return new Plane(mx, my, mz, md);
    }

    @Override
    public String toString() {
        return "Plane[normal=" + normal + ", d=" + d + "]";
    }
}

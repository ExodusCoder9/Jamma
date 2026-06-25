package com.jamma.math.geometry;

import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix4f;

import java.io.Serial;
import java.io.Serializable;

public record Planef(Vector3f normal, float d) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Planef(float nx, float ny, float nz, float d) {
        this(new Vector3f(nx, ny, nz), d);
    }

    public static Planef fromPointNormal(Vector3f point, Vector3f normal) {
        float invLen = 1.0f / (float) Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        float nx = normal.x() * invLen;
        float ny = normal.y() * invLen;
        float nz = normal.z() * invLen;
        return new Planef(nx, ny, nz, -(nx * point.x() + ny * point.y() + nz * point.z()));
    }

    public static Planef fromPoints(Vector3f a, Vector3f b, Vector3f c) {
        float abx = b.x() - a.x(), aby = b.y() - a.y(), abz = b.z() - a.z();
        float acx = c.x() - a.x(), acy = c.y() - a.y(), acz = c.z() - a.z();
        float nx = aby * acz - abz * acy;
        float ny = abz * acx - abx * acz;
        float nz = abx * acy - aby * acx;
        float invLen = 1.0f / (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        nx *= invLen;
        ny *= invLen;
        nz *= invLen;
        return new Planef(nx, ny, nz, -(nx * a.x() + ny * a.y() + nz * a.z()));
    }

    public float distance(Vector3f point) {
        return normal.x() * point.x() + normal.y() * point.y() + normal.z() * point.z() + d;
    }

    public float intersectRay(Rayf ray) {
        float dot = normal.x() * ray.direction.x() + normal.y() * ray.direction.y() + normal.z() * ray.direction.z();
        if (Math.abs(dot) < 1e-30f) return Float.NaN;
        float t = -(normal.x() * ray.origin.x() + normal.y() * ray.origin.y() + normal.z() * ray.origin.z() + d) / dot;
        return t >= 0.0f ? t : Float.NaN;
    }

    public Planef normalize() {
        float invLen = 1.0f / (float) Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        return new Planef(normal.x() * invLen, normal.y() * invLen, normal.z() * invLen, d * invLen);
    }

    public Planef transform(Matrix4f m) {
        float nx = normal.x(), ny = normal.y(), nz = normal.z();
        float mx = m.m00() * nx + m.m10() * ny + m.m20() * nz;
        float my = m.m01() * nx + m.m11() * ny + m.m21() * nz;
        float mz = m.m02() * nx + m.m12() * ny + m.m22() * nz;
        float md = d - (mx * m.m30() + my * m.m31() + mz * m.m32());
        return new Planef(mx, my, mz, md);
    }

    @Override
    public String toString() {
        return "Planef[normal=" + normal + ", d=" + d + "]";
    }
}

package com.jamma.math.incubator;

import com.jamma.math.Vector2D;
import com.jamma.math.Vector3D;
import com.jamma.math.Vector4D;

public final class VectorMath {

    private VectorMath() {
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x() + b.x(), a.y() + b.y());
    }

    public static Vector3D add(Vector3D a, Vector3D b) {
        return new Vector3D(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
    }

    public static Vector4D add(Vector4D a, Vector4D b) {
        return new Vector4D(a.x() + b.x(), a.y() + b.y(), a.z() + b.z(), a.w() + b.w());
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(a.x() - b.x(), a.y() - b.y());
    }

    public static Vector3D sub(Vector3D a, Vector3D b) {
        return new Vector3D(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
    }

    public static Vector4D sub(Vector4D a, Vector4D b) {
        return new Vector4D(a.x() - b.x(), a.y() - b.y(), a.z() - b.z(), a.w() - b.w());
    }

    public static Vector2D mul(Vector2D a, Vector2D b) {
        return new Vector2D(a.x() * b.x(), a.y() * b.y());
    }

    public static Vector3D mul(Vector3D a, Vector3D b) {
        return new Vector3D(a.x() * b.x(), a.y() * b.y(), a.z() * b.z());
    }

    public static Vector4D mul(Vector4D a, Vector4D b) {
        return new Vector4D(a.x() * b.x(), a.y() * b.y(), a.z() * b.z(), a.w() * b.w());
    }

    public static Vector2D scale(Vector2D v, double s) {
        return new Vector2D(v.x() * s, v.y() * s);
    }

    public static Vector3D scale(Vector3D v, double s) {
        return new Vector3D(v.x() * s, v.y() * s, v.z() * s);
    }

    public static Vector4D scale(Vector4D v, double s) {
        return new Vector4D(v.x() * s, v.y() * s, v.z() * s, v.w() * s);
    }

    public static double dot(Vector2D a, Vector2D b) {
        return Math.fma(a.x(), b.x(), a.y() * b.y());
    }

    public static double dot(Vector3D a, Vector3D b) {
        return Math.fma(a.x(), b.x(), Math.fma(a.y(), b.y(), a.z() * b.z()));
    }

    public static double dot(Vector4D a, Vector4D b) {
        return Math.fma(a.x(), b.x(), Math.fma(a.y(), b.y(), Math.fma(a.z(), b.z(), a.w() * b.w())));
    }

    public static Vector3D cross(Vector3D a, Vector3D b) {
        return new Vector3D(
            a.y() * b.z() - a.z() * b.y(),
            a.z() * b.x() - a.x() * b.z(),
            a.x() * b.y() - a.y() * b.x()
        );
    }

    public static double length(Vector2D v) {
        return Math.sqrt(dot(v, v));
    }

    public static double length(Vector3D v) {
        return Math.sqrt(dot(v, v));
    }

    public static double length(Vector4D v) {
        return Math.sqrt(dot(v, v));
    }

    public static double lengthSquared(Vector2D v) {
        return dot(v, v);
    }

    public static double lengthSquared(Vector3D v) {
        return dot(v, v);
    }

    public static double lengthSquared(Vector4D v) {
        return dot(v, v);
    }

    public static Vector2D normalize(Vector2D v) {
        double len = length(v);
        return new Vector2D(v.x() / len, v.y() / len);
    }

    public static Vector3D normalize(Vector3D v) {
        double len = length(v);
        return new Vector3D(v.x() / len, v.y() / len, v.z() / len);
    }

    public static Vector4D normalize(Vector4D v) {
        double len = length(v);
        return new Vector4D(v.x() / len, v.y() / len, v.z() / len, v.w() / len);
    }

    public static double distance(Vector2D a, Vector2D b) {
        return length(sub(a, b));
    }

    public static double distance(Vector3D a, Vector3D b) {
        return length(sub(a, b));
    }

    public static double distance(Vector4D a, Vector4D b) {
        return length(sub(a, b));
    }

    public static Vector2D lerp(Vector2D a, Vector2D b, double t) {
        return new Vector2D(Math.fma(t, b.x() - a.x(), a.x()), Math.fma(t, b.y() - a.y(), a.y()));
    }

    public static Vector3D lerp(Vector3D a, Vector3D b, double t) {
        return new Vector3D(
            Math.fma(t, b.x() - a.x(), a.x()),
            Math.fma(t, b.y() - a.y(), a.y()),
            Math.fma(t, b.z() - a.z(), a.z())
        );
    }

    public static Vector4D lerp(Vector4D a, Vector4D b, double t) {
        return new Vector4D(
            Math.fma(t, b.x() - a.x(), a.x()),
            Math.fma(t, b.y() - a.y(), a.y()),
            Math.fma(t, b.z() - a.z(), a.z()),
            Math.fma(t, b.w() - a.w(), a.w())
        );
    }

    public static Vector2D reflect(Vector2D direction, Vector2D normal) {
        double dDotN = dot(direction, normal);
        return new Vector2D(
            direction.x() - 2.0 * dDotN * normal.x(),
            direction.y() - 2.0 * dDotN * normal.y()
        );
    }

    public static Vector3D reflect(Vector3D direction, Vector3D normal) {
        double dDotN = dot(direction, normal);
        return new Vector3D(
            direction.x() - 2.0 * dDotN * normal.x(),
            direction.y() - 2.0 * dDotN * normal.y(),
            direction.z() - 2.0 * dDotN * normal.z()
        );
    }

    public static Vector2D project(Vector2D v, Vector2D onto) {
        double s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }

    public static Vector3D project(Vector3D v, Vector3D onto) {
        double s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }

    public static double angle(Vector2D a, Vector2D b) {
        return Math.acos(clamp(dot(a, b) / (length(a) * length(b)), -1.0, 1.0));
    }

    public static double angle(Vector3D a, Vector3D b) {
        return Math.acos(clamp(dot(a, b) / (length(a) * length(b)), -1.0, 1.0));
    }

    public static Vector2D min(Vector2D a, Vector2D b) {
        return new Vector2D(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
    }

    public static Vector3D min(Vector3D a, Vector3D b) {
        return new Vector3D(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
    }

    public static Vector4D min(Vector4D a, Vector4D b) {
        return new Vector4D(
            Math.min(a.x(), b.x()), Math.min(a.y(), b.y()),
            Math.min(a.z(), b.z()), Math.min(a.w(), b.w())
        );
    }

    public static Vector2D max(Vector2D a, Vector2D b) {
        return new Vector2D(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
    }

    public static Vector3D max(Vector3D a, Vector3D b) {
        return new Vector3D(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
    }

    public static Vector4D max(Vector4D a, Vector4D b) {
        return new Vector4D(
            Math.max(a.x(), b.x()), Math.max(a.y(), b.y()),
            Math.max(a.z(), b.z()), Math.max(a.w(), b.w())
        );
    }

    public static Vector2D clamp(Vector2D v, Vector2D min, Vector2D max) {
        return new Vector2D(
            Math.min(Math.max(v.x(), min.x()), max.x()),
            Math.min(Math.max(v.y(), min.y()), max.y())
        );
    }

    public static Vector3D clamp(Vector3D v, Vector3D min, Vector3D max) {
        return new Vector3D(
            Math.min(Math.max(v.x(), min.x()), max.x()),
            Math.min(Math.max(v.y(), min.y()), max.y()),
            Math.min(Math.max(v.z(), min.z()), max.z())
        );
    }

    public static Vector4D clamp(Vector4D v, Vector4D min, Vector4D max) {
        return new Vector4D(
            Math.min(Math.max(v.x(), min.x()), max.x()),
            Math.min(Math.max(v.y(), min.y()), max.y()),
            Math.min(Math.max(v.z(), min.z()), max.z()),
            Math.min(Math.max(v.w(), min.w()), max.w())
        );
    }

    public static Vector2D perpendicular(Vector2D v) {
        return new Vector2D(-v.y(), v.x());
    }

    public static Vector3D[] batchAdd(Vector3D[] a, Vector3D[] b) {
        int n = a.length;
        Vector3D[] result = new Vector3D[n];
        for (int i = 0; i < n; i++) {
            result[i] = add(a[i], b[i]);
        }
        return result;
    }

    private static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}

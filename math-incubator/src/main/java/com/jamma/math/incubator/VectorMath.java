package com.jamma.math.incubator;

import com.jamma.math.Vector2d;
import com.jamma.math.Vector3d;
import com.jamma.math.Vector4d;

public final class VectorMath {

    private static final double EPSILON = 1.0e-10;

    private VectorMath() {
    }

    public static Vector2d add(Vector2d a, Vector2d b) { return new Vector2d(a.x() + b.x(), a.y() + b.y()); }
    public static Vector3d add(Vector3d a, Vector3d b) { return new Vector3d(a.x() + b.x(), a.y() + b.y(), a.z() + b.z()); }
    public static Vector4d add(Vector4d a, Vector4d b) { return new Vector4d(a.x() + b.x(), a.y() + b.y(), a.z() + b.z(), a.w() + b.w()); }

    public static Vector2d sub(Vector2d a, Vector2d b) { return new Vector2d(a.x() - b.x(), a.y() - b.y()); }
    public static Vector3d sub(Vector3d a, Vector3d b) { return new Vector3d(a.x() - b.x(), a.y() - b.y(), a.z() - b.z()); }
    public static Vector4d sub(Vector4d a, Vector4d b) { return new Vector4d(a.x() - b.x(), a.y() - b.y(), a.z() - b.z(), a.w() - b.w()); }

    public static Vector2d mul(Vector2d a, Vector2d b) { return new Vector2d(a.x() * b.x(), a.y() * b.y()); }
    public static Vector3d mul(Vector3d a, Vector3d b) { return new Vector3d(a.x() * b.x(), a.y() * b.y(), a.z() * b.z()); }
    public static Vector4d mul(Vector4d a, Vector4d b) { return new Vector4d(a.x() * b.x(), a.y() * b.y(), a.z() * b.z(), a.w() * b.w()); }

    public static Vector2d div(Vector2d a, Vector2d b) { return new Vector2d(a.x() / b.x(), a.y() / b.y()); }
    public static Vector3d div(Vector3d a, Vector3d b) { return new Vector3d(a.x() / b.x(), a.y() / b.y(), a.z() / b.z()); }
    public static Vector4d div(Vector4d a, Vector4d b) { return new Vector4d(a.x() / b.x(), a.y() / b.y(), a.z() / b.z(), a.w() / b.w()); }

    public static Vector2d scale(Vector2d v, double s) { return new Vector2d(v.x() * s, v.y() * s); }
    public static Vector3d scale(Vector3d v, double s) { return new Vector3d(v.x() * s, v.y() * s, v.z() * s); }
    public static Vector4d scale(Vector4d v, double s) { return new Vector4d(v.x() * s, v.y() * s, v.z() * s, v.w() * s); }

    public static Vector2d negate(Vector2d v) { return new Vector2d(-v.x(), -v.y()); }
    public static Vector3d negate(Vector3d v) { return new Vector3d(-v.x(), -v.y(), -v.z()); }
    public static Vector4d negate(Vector4d v) { return new Vector4d(-v.x(), -v.y(), -v.z(), -v.w()); }

    public static Vector2d abs(Vector2d v) { return new Vector2d(Math.abs(v.x()), Math.abs(v.y())); }
    public static Vector3d abs(Vector3d v) { return new Vector3d(Math.abs(v.x()), Math.abs(v.y()), Math.abs(v.z())); }
    public static Vector4d abs(Vector4d v) { return new Vector4d(Math.abs(v.x()), Math.abs(v.y()), Math.abs(v.z()), Math.abs(v.w())); }

    public static Vector2d sign(Vector2d v) { return new Vector2d(Math.signum(v.x()), Math.signum(v.y())); }
    public static Vector3d sign(Vector3d v) { return new Vector3d(Math.signum(v.x()), Math.signum(v.y()), Math.signum(v.z())); }
    public static Vector4d sign(Vector4d v) { return new Vector4d(Math.signum(v.x()), Math.signum(v.y()), Math.signum(v.z()), Math.signum(v.w())); }

    public static double dot(Vector2d a, Vector2d b) { return Math.fma(a.x(), b.x(), a.y() * b.y()); }
    public static double dot(Vector3d a, Vector3d b) {
        return Math.fma(a.x(), b.x(), Math.fma(a.y(), b.y(), a.z() * b.z()));
    }
    public static double dot(Vector4d a, Vector4d b) {
        return Math.fma(a.x(), b.x(), Math.fma(a.y(), b.y(), Math.fma(a.z(), b.z(), a.w() * b.w())));
    }

    public static Vector3d cross(Vector3d a, Vector3d b) {
        return new Vector3d(
            a.y() * b.z() - a.z() * b.y(),
            a.z() * b.x() - a.x() * b.z(),
            a.x() * b.y() - a.y() * b.x()
        );
    }
    public static double cross2D(Vector2d a, Vector2d b) {
        return a.x() * b.y() - a.y() * b.x();
    }
    public static double cross2D(Vector2d a, Vector2d b, Vector2d c) {
        return (b.x() - a.x()) * (c.y() - a.y()) - (b.y() - a.y()) * (c.x() - a.x());
    }

    public static double tripleScalar(Vector3d a, Vector3d b, Vector3d c) {
        return dot(a, cross(b, c));
    }
    public static Vector3d tripleVector(Vector3d a, Vector3d b, Vector3d c) {
        return sub(scale(b, dot(a, c)), scale(c, dot(a, b)));
    }

    public static double length(Vector2d v) { return Math.sqrt(dot(v, v)); }
    public static double length(Vector3d v) { return Math.sqrt(dot(v, v)); }
    public static double length(Vector4d v) { return Math.sqrt(dot(v, v)); }
    public static double lengthSquared(Vector2d v) { return dot(v, v); }
    public static double lengthSquared(Vector3d v) { return dot(v, v); }
    public static double lengthSquared(Vector4d v) { return dot(v, v); }

    public static double distance(Vector2d a, Vector2d b) { return length(sub(a, b)); }
    public static double distance(Vector3d a, Vector3d b) { return length(sub(a, b)); }
    public static double distance(Vector4d a, Vector4d b) { return length(sub(a, b)); }
    public static double distanceSquared(Vector2d a, Vector2d b) { return lengthSquared(sub(a, b)); }
    public static double distanceSquared(Vector3d a, Vector3d b) { return lengthSquared(sub(a, b)); }
    public static double distanceSquared(Vector4d a, Vector4d b) { return lengthSquared(sub(a, b)); }

    public static Vector2d normalize(Vector2d v) {
        double len = length(v);
        return new Vector2d(v.x() / len, v.y() / len);
    }
    public static Vector3d normalize(Vector3d v) {
        double len = length(v);
        return new Vector3d(v.x() / len, v.y() / len, v.z() / len);
    }
    public static Vector4d normalize(Vector4d v) {
        double len = length(v);
        return new Vector4d(v.x() / len, v.y() / len, v.z() / len, v.w() / len);
    }

    public static Vector2d safeNormalize(Vector2d v, Vector2d fallback) {
        double lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        double invLen = 1.0 / Math.sqrt(lenSq);
        return new Vector2d(v.x() * invLen, v.y() * invLen);
    }
    public static Vector3d safeNormalize(Vector3d v, Vector3d fallback) {
        double lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        double invLen = 1.0 / Math.sqrt(lenSq);
        return new Vector3d(v.x() * invLen, v.y() * invLen, v.z() * invLen);
    }
    public static Vector4d safeNormalize(Vector4d v, Vector4d fallback) {
        double lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        double invLen = 1.0 / Math.sqrt(lenSq);
        return new Vector4d(v.x() * invLen, v.y() * invLen, v.z() * invLen, v.w() * invLen);
    }

    public static Vector2d setLength(Vector2d v, double len) {
        return scale(normalize(v), len);
    }
    public static Vector3d setLength(Vector3d v, double len) {
        return scale(normalize(v), len);
    }
    public static Vector4d setLength(Vector4d v, double len) {
        return scale(normalize(v), len);
    }

    public static Vector2d limit(Vector2d v, double maxLen) {
        double lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / Math.sqrt(lenSq));
        return v;
    }
    public static Vector3d limit(Vector3d v, double maxLen) {
        double lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / Math.sqrt(lenSq));
        return v;
    }
    public static Vector4d limit(Vector4d v, double maxLen) {
        double lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / Math.sqrt(lenSq));
        return v;
    }

    public static Vector2d midpoint(Vector2d a, Vector2d b) {
        return new Vector2d((a.x() + b.x()) * 0.5, (a.y() + b.y()) * 0.5);
    }
    public static Vector3d midpoint(Vector3d a, Vector3d b) {
        return new Vector3d((a.x() + b.x()) * 0.5, (a.y() + b.y()) * 0.5, (a.z() + b.z()) * 0.5);
    }
    public static Vector4d midpoint(Vector4d a, Vector4d b) {
        return new Vector4d((a.x() + b.x()) * 0.5, (a.y() + b.y()) * 0.5, (a.z() + b.z()) * 0.5, (a.w() + b.w()) * 0.5);
    }

    public static Vector2d lerp(Vector2d a, Vector2d b, double t) {
        return new Vector2d(Math.fma(t, b.x() - a.x(), a.x()), Math.fma(t, b.y() - a.y(), a.y()));
    }
    public static Vector3d lerp(Vector3d a, Vector3d b, double t) {
        return new Vector3d(
            Math.fma(t, b.x() - a.x(), a.x()),
            Math.fma(t, b.y() - a.y(), a.y()),
            Math.fma(t, b.z() - a.z(), a.z())
        );
    }
    public static Vector4d lerp(Vector4d a, Vector4d b, double t) {
        return new Vector4d(
            Math.fma(t, b.x() - a.x(), a.x()),
            Math.fma(t, b.y() - a.y(), a.y()),
            Math.fma(t, b.z() - a.z(), a.z()),
            Math.fma(t, b.w() - a.w(), a.w())
        );
    }

    public static Vector3d nlerp(Vector3d a, Vector3d b, double t) {
        return normalize(lerp(a, b, t));
    }
    public static Vector3d slerp(Vector3d a, Vector3d b, double t) {
        double d = clamp(dot(a, b), -1.0, 1.0);
        double theta = Math.acos(d) * t;
        Vector3d relative = normalize(sub(b, scale(a, d)));
        return add(scale(a, Math.cos(theta)), scale(relative, Math.sin(theta)));
    }

    public static Vector2d reflect(Vector2d direction, Vector2d normal) {
        double dDotN = dot(direction, normal);
        return new Vector2d(
            direction.x() - 2.0 * dDotN * normal.x(),
            direction.y() - 2.0 * dDotN * normal.y()
        );
    }
    public static Vector3d reflect(Vector3d direction, Vector3d normal) {
        double dDotN = dot(direction, normal);
        return new Vector3d(
            direction.x() - 2.0 * dDotN * normal.x(),
            direction.y() - 2.0 * dDotN * normal.y(),
            direction.z() - 2.0 * dDotN * normal.z()
        );
    }

    public static Vector2d refract(Vector2d direction, Vector2d normal, double eta) {
        double dDotN = dot(direction, normal);
        double k = 1.0 - eta * eta * (1.0 - dDotN * dDotN);
        if (k < 0.0) return new Vector2d(0.0, 0.0);
        return sub(scale(direction, eta), scale(normal, eta * dDotN + Math.sqrt(k)));
    }
    public static Vector3d refract(Vector3d direction, Vector3d normal, double eta) {
        double dDotN = dot(direction, normal);
        double k = 1.0 - eta * eta * (1.0 - dDotN * dDotN);
        if (k < 0.0) return new Vector3d(0.0, 0.0, 0.0);
        return sub(scale(direction, eta), scale(normal, eta * dDotN + Math.sqrt(k)));
    }

    public static Vector2d project(Vector2d v, Vector2d onto) {
        double s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }
    public static Vector3d project(Vector3d v, Vector3d onto) {
        double s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }
    public static Vector2d reject(Vector2d v, Vector2d onto) { return sub(v, project(v, onto)); }
    public static Vector3d reject(Vector3d v, Vector3d onto) { return sub(v, project(v, onto)); }

    public static Vector3d faceforward(Vector3d n, Vector3d i, Vector3d nRef) {
        return dot(nRef, i) < 0.0 ? n : negate(n);
    }
    public static Vector3d projectOnPlane(Vector3d v, Vector3d planeNormal) {
        return sub(v, scale(planeNormal, dot(v, planeNormal)));
    }

    public static double angle(Vector2d a, Vector2d b) {
        return Math.acos(clamp(dot(a, b) / (length(a) * length(b)), -1.0, 1.0));
    }
    public static double angle(Vector3d a, Vector3d b) {
        return Math.acos(clamp(dot(a, b) / (length(a) * length(b)), -1.0, 1.0));
    }
    public static double angleSigned(Vector2d a, Vector2d b) {
        return Math.atan2(cross2D(a, b), dot(a, b));
    }
    public static double angleSigned(Vector3d a, Vector3d b, Vector3d normal) {
        double d = dot(a, b) / (length(a) * length(b));
        Vector3d c = cross(a, b);
        double sign = dot(c, normal) >= 0.0 ? 1.0 : -1.0;
        return sign * Math.acos(clamp(d, -1.0, 1.0));
    }

    public static Vector2d rotate(Vector2d v, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Vector2d(v.x() * c - v.y() * s, v.x() * s + v.y() * c);
    }
    public static Vector3d rotateAroundAxis(Vector3d v, Vector3d axis, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Vector3d k = normalize(axis);
        return add(add(scale(v, c), scale(cross(k, v), s)), scale(k, dot(k, v) * (1.0 - c)));
    }

    public static Vector2d min(Vector2d a, Vector2d b) {
        return new Vector2d(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
    }
    public static Vector3d min(Vector3d a, Vector3d b) {
        return new Vector3d(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
    }
    public static Vector4d min(Vector4d a, Vector4d b) {
        return new Vector4d(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()), Math.min(a.w(), b.w()));
    }
    public static Vector2d max(Vector2d a, Vector2d b) {
        return new Vector2d(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
    }
    public static Vector3d max(Vector3d a, Vector3d b) {
        return new Vector3d(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
    }
    public static Vector4d max(Vector4d a, Vector4d b) {
        return new Vector4d(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()), Math.max(a.w(), b.w()));
    }

    public static Vector2d clamp(Vector2d v, Vector2d min, Vector2d max) {
        return new Vector2d(clamp(v.x(), min.x(), max.x()), clamp(v.y(), min.y(), max.y()));
    }
    public static Vector3d clamp(Vector3d v, Vector3d min, Vector3d max) {
        return new Vector3d(clamp(v.x(), min.x(), max.x()), clamp(v.y(), min.y(), max.y()), clamp(v.z(), min.z(), max.z()));
    }
    public static Vector4d clamp(Vector4d v, Vector4d min, Vector4d max) {
        return new Vector4d(clamp(v.x(), min.x(), max.x()), clamp(v.y(), min.y(), max.y()), clamp(v.z(), min.z(), max.z()), clamp(v.w(), min.w(), max.w()));
    }

    public static Vector2d perpendicular(Vector2d v) { return new Vector2d(-v.y(), v.x()); }

    public static boolean isCollinear(Vector2d a, Vector2d b, Vector2d c) {
        return Math.abs(cross2D(a, b, c)) < EPSILON;
    }
    public static boolean isCollinear(Vector3d a, Vector3d b, Vector3d c) {
        return length(cross(sub(b, a), sub(c, a))) < EPSILON;
    }
    public static boolean isCoplanar(Vector3d a, Vector3d b, Vector3d c, Vector3d d) {
        return Math.abs(tripleScalar(sub(b, a), sub(c, a), sub(d, a))) < EPSILON;
    }
    public static boolean isOrthogonal(Vector2d a, Vector2d b) { return Math.abs(dot(a, b)) < EPSILON; }
    public static boolean isOrthogonal(Vector3d a, Vector3d b) { return Math.abs(dot(a, b)) < EPSILON; }
    public static boolean isParallel(Vector2d a, Vector2d b) { return Math.abs(cross2D(a, b)) < EPSILON; }
    public static boolean isParallel(Vector3d a, Vector3d b) { return length(cross(a, b)) < EPSILON; }

    public static Vector2d closestPointOnSegment(Vector2d p, Vector2d a, Vector2d b) {
        Vector2d ab = sub(b, a);
        double t = clamp(dot(sub(p, a), ab) / dot(ab, ab), 0.0, 1.0);
        return lerp(a, b, t);
    }
    public static Vector3d closestPointOnSegment(Vector3d p, Vector3d a, Vector3d b) {
        Vector3d ab = sub(b, a);
        double t = clamp(dot(sub(p, a), ab) / dot(ab, ab), 0.0, 1.0);
        return lerp(a, b, t);
    }
    public static double distanceToSegment(Vector2d p, Vector2d a, Vector2d b) {
        return distance(p, closestPointOnSegment(p, a, b));
    }
    public static double distanceToSegment(Vector3d p, Vector3d a, Vector3d b) {
        return distance(p, closestPointOnSegment(p, a, b));
    }

    public static Vector2d catmullRom(Vector2d a, Vector2d b, Vector2d c, Vector2d d, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        return new Vector2d(
            0.5 * ((2.0 * b.x()) + (-a.x() + c.x()) * t + (2.0 * a.x() - 5.0 * b.x() + 4.0 * c.x() - d.x()) * t2 + (-a.x() + 3.0 * b.x() - 3.0 * c.x() + d.x()) * t3),
            0.5 * ((2.0 * b.y()) + (-a.y() + c.y()) * t + (2.0 * a.y() - 5.0 * b.y() + 4.0 * c.y() - d.y()) * t2 + (-a.y() + 3.0 * b.y() - 3.0 * c.y() + d.y()) * t3)
        );
    }
    public static Vector3d catmullRom(Vector3d a, Vector3d b, Vector3d c, Vector3d d, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        return new Vector3d(
            0.5 * ((2.0 * b.x()) + (-a.x() + c.x()) * t + (2.0 * a.x() - 5.0 * b.x() + 4.0 * c.x() - d.x()) * t2 + (-a.x() + 3.0 * b.x() - 3.0 * c.x() + d.x()) * t3),
            0.5 * ((2.0 * b.y()) + (-a.y() + c.y()) * t + (2.0 * a.y() - 5.0 * b.y() + 4.0 * c.y() - d.y()) * t2 + (-a.y() + 3.0 * b.y() - 3.0 * c.y() + d.y()) * t3),
            0.5 * ((2.0 * b.z()) + (-a.z() + c.z()) * t + (2.0 * a.z() - 5.0 * b.z() + 4.0 * c.z() - d.z()) * t2 + (-a.z() + 3.0 * b.z() - 3.0 * c.z() + d.z()) * t3)
        );
    }

    public static Vector2d bezier(Vector2d a, Vector2d b, Vector2d c, Vector2d d, double t) {
        double u = 1.0 - t;
        return add(add(add(scale(a, u * u * u), scale(b, 3.0 * u * u * t)), scale(c, 3.0 * u * t * t)), scale(d, t * t * t));
    }
    public static Vector3d bezier(Vector3d a, Vector3d b, Vector3d c, Vector3d d, double t) {
        double u = 1.0 - t;
        return add(add(add(scale(a, u * u * u), scale(b, 3.0 * u * u * t)), scale(c, 3.0 * u * t * t)), scale(d, t * t * t));
    }

    public static Vector3d orthonormalBasisX(Vector3d v) {
        Vector3d n = normalize(v);
        if (Math.abs(n.x()) < Math.abs(n.z())) {
            return normalize(cross(n, new Vector3d(0.0, -1.0, 0.0)));
        }
        return normalize(cross(n, new Vector3d(0.0, 0.0, 1.0)));
    }
    public static Vector3d orthonormalBasisY(Vector3d v) { return normalize(v); }
    public static Vector3d orthonormalBasisZ(Vector3d v) {
        return cross(orthonormalBasisX(v), normalize(v));
    }

    public static double[] toArray(Vector2d v) { return new double[]{v.x(), v.y()}; }
    public static double[] toArray3(Vector3d v) { return new double[]{v.x(), v.y(), v.z()}; }
    public static double[] toArray4(Vector4d v) { return new double[]{v.x(), v.y(), v.z(), v.w()}; }
    public static Vector2d fromArray2(double[] arr, int offset) { return new Vector2d(arr[offset], arr[offset + 1]); }
    public static Vector3d fromArray3(double[] arr, int offset) { return new Vector3d(arr[offset], arr[offset + 1], arr[offset + 2]); }
    public static Vector4d fromArray4(double[] arr, int offset) { return new Vector4d(arr[offset], arr[offset + 1], arr[offset + 2], arr[offset + 3]); }

    public static Vector3d[] batchAdd(Vector3d[] a, Vector3d[] b) {
        int n = a.length;
        Vector3d[] result = new Vector3d[n];
        for (int i = 0; i < n; i++) result[i] = add(a[i], b[i]);
        return result;
    }
    public static double[] batchDot(Vector3d[] a, Vector3d[] b) {
        int n = a.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = dot(a[i], b[i]);
        return result;
    }
    public static Vector3d[] batchScale(Vector3d[] v, double s) {
        int n = v.length;
        Vector3d[] result = new Vector3d[n];
        for (int i = 0; i < n; i++) result[i] = scale(v[i], s);
        return result;
    }
    public static Vector3d[] batchNormalize(Vector3d[] v) {
        int n = v.length;
        Vector3d[] result = new Vector3d[n];
        for (int i = 0; i < n; i++) result[i] = normalize(v[i]);
        return result;
    }
    public static Vector3d[] batchNegate(Vector3d[] v) {
        int n = v.length;
        Vector3d[] result = new Vector3d[n];
        for (int i = 0; i < n; i++) result[i] = negate(v[i]);
        return result;
    }

    public static boolean isFinite(Vector2d v) { return Double.isFinite(v.x()) && Double.isFinite(v.y()); }
    public static boolean isFinite(Vector3d v) { return Double.isFinite(v.x()) && Double.isFinite(v.y()) && Double.isFinite(v.z()); }
    public static boolean isFinite(Vector4d v) { return Double.isFinite(v.x()) && Double.isFinite(v.y()) && Double.isFinite(v.z()) && Double.isFinite(v.w()); }
    public static boolean hasNaN(Vector2d v) { return Double.isNaN(v.x()) || Double.isNaN(v.y()); }
    public static boolean hasNaN(Vector3d v) { return Double.isNaN(v.x()) || Double.isNaN(v.y()) || Double.isNaN(v.z()); }
    public static boolean hasNaN(Vector4d v) { return Double.isNaN(v.x()) || Double.isNaN(v.y()) || Double.isNaN(v.z()) || Double.isNaN(v.w()); }

    public static double componentMin(Vector2d v) { return Math.min(v.x(), v.y()); }
    public static double componentMin(Vector3d v) { return Math.min(v.x(), Math.min(v.y(), v.z())); }
    public static double componentMin(Vector4d v) { return Math.min(Math.min(v.x(), v.y()), Math.min(v.z(), v.w())); }
    public static double componentMax(Vector2d v) { return Math.max(v.x(), v.y()); }
    public static double componentMax(Vector3d v) { return Math.max(v.x(), Math.max(v.y(), v.z())); }
    public static double componentMax(Vector4d v) { return Math.max(Math.max(v.x(), v.y()), Math.max(v.z(), v.w())); }
    public static double componentSum(Vector2d v) { return v.x() + v.y(); }
    public static double componentSum(Vector3d v) { return v.x() + v.y() + v.z(); }
    public static double componentSum(Vector4d v) { return v.x() + v.y() + v.z() + v.w(); }

    private static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}

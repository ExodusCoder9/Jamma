package com.jamma.math;

public final class VectorMath {

    private static final double EPSILON = 1.0e-10;

    private VectorMath() {
    }

    public static Vector2D add(Vector2D a, Vector2D b) { return new Vector2D(a.x() + b.x(), a.y() + b.y()); }
    public static Vector3D add(Vector3D a, Vector3D b) { return new Vector3D(a.x() + b.x(), a.y() + b.y(), a.z() + b.z()); }
    public static Vector4D add(Vector4D a, Vector4D b) { return new Vector4D(a.x() + b.x(), a.y() + b.y(), a.z() + b.z(), a.w() + b.w()); }

    public static Vector2D sub(Vector2D a, Vector2D b) { return new Vector2D(a.x() - b.x(), a.y() - b.y()); }
    public static Vector3D sub(Vector3D a, Vector3D b) { return new Vector3D(a.x() - b.x(), a.y() - b.y(), a.z() - b.z()); }
    public static Vector4D sub(Vector4D a, Vector4D b) { return new Vector4D(a.x() - b.x(), a.y() - b.y(), a.z() - b.z(), a.w() - b.w()); }

    public static Vector2D mul(Vector2D a, Vector2D b) { return new Vector2D(a.x() * b.x(), a.y() * b.y()); }
    public static Vector3D mul(Vector3D a, Vector3D b) { return new Vector3D(a.x() * b.x(), a.y() * b.y(), a.z() * b.z()); }
    public static Vector4D mul(Vector4D a, Vector4D b) { return new Vector4D(a.x() * b.x(), a.y() * b.y(), a.z() * b.z(), a.w() * b.w()); }

    public static Vector2D div(Vector2D a, Vector2D b) { return new Vector2D(a.x() / b.x(), a.y() / b.y()); }
    public static Vector3D div(Vector3D a, Vector3D b) { return new Vector3D(a.x() / b.x(), a.y() / b.y(), a.z() / b.z()); }
    public static Vector4D div(Vector4D a, Vector4D b) { return new Vector4D(a.x() / b.x(), a.y() / b.y(), a.z() / b.z(), a.w() / b.w()); }

    public static Vector2D scale(Vector2D v, double s) { return new Vector2D(v.x() * s, v.y() * s); }
    public static Vector3D scale(Vector3D v, double s) { return new Vector3D(v.x() * s, v.y() * s, v.z() * s); }
    public static Vector4D scale(Vector4D v, double s) { return new Vector4D(v.x() * s, v.y() * s, v.z() * s, v.w() * s); }

    public static Vector2D negate(Vector2D v) { return new Vector2D(-v.x(), -v.y()); }
    public static Vector3D negate(Vector3D v) { return new Vector3D(-v.x(), -v.y(), -v.z()); }
    public static Vector4D negate(Vector4D v) { return new Vector4D(-v.x(), -v.y(), -v.z(), -v.w()); }

    public static Vector2D abs(Vector2D v) { return new Vector2D(Math.abs(v.x()), Math.abs(v.y())); }
    public static Vector3D abs(Vector3D v) { return new Vector3D(Math.abs(v.x()), Math.abs(v.y()), Math.abs(v.z())); }
    public static Vector4D abs(Vector4D v) { return new Vector4D(Math.abs(v.x()), Math.abs(v.y()), Math.abs(v.z()), Math.abs(v.w())); }

    public static Vector2D sign(Vector2D v) { return new Vector2D(Math.signum(v.x()), Math.signum(v.y())); }
    public static Vector3D sign(Vector3D v) { return new Vector3D(Math.signum(v.x()), Math.signum(v.y()), Math.signum(v.z())); }
    public static Vector4D sign(Vector4D v) { return new Vector4D(Math.signum(v.x()), Math.signum(v.y()), Math.signum(v.z()), Math.signum(v.w())); }

    public static double dot(Vector2D a, Vector2D b) { return Math.fma(a.x(), b.x(), a.y() * b.y()); }
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
    public static double cross2D(Vector2D a, Vector2D b) {
        return a.x() * b.y() - a.y() * b.x();
    }
    public static double cross2D(Vector2D a, Vector2D b, Vector2D c) {
        return (b.x() - a.x()) * (c.y() - a.y()) - (b.y() - a.y()) * (c.x() - a.x());
    }

    public static double tripleScalar(Vector3D a, Vector3D b, Vector3D c) {
        return dot(a, cross(b, c));
    }
    public static Vector3D tripleVector(Vector3D a, Vector3D b, Vector3D c) {
        return sub(scale(b, dot(a, c)), scale(c, dot(a, b)));
    }

    public static double length(Vector2D v) { return Math.sqrt(dot(v, v)); }
    public static double length(Vector3D v) { return Math.sqrt(dot(v, v)); }
    public static double length(Vector4D v) { return Math.sqrt(dot(v, v)); }
    public static double lengthSquared(Vector2D v) { return dot(v, v); }
    public static double lengthSquared(Vector3D v) { return dot(v, v); }
    public static double lengthSquared(Vector4D v) { return dot(v, v); }

    public static double distance(Vector2D a, Vector2D b) { return length(sub(a, b)); }
    public static double distance(Vector3D a, Vector3D b) { return length(sub(a, b)); }
    public static double distance(Vector4D a, Vector4D b) { return length(sub(a, b)); }
    public static double distanceSquared(Vector2D a, Vector2D b) { return lengthSquared(sub(a, b)); }
    public static double distanceSquared(Vector3D a, Vector3D b) { return lengthSquared(sub(a, b)); }
    public static double distanceSquared(Vector4D a, Vector4D b) { return lengthSquared(sub(a, b)); }

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

    public static Vector2D safeNormalize(Vector2D v, Vector2D fallback) {
        double lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        double invLen = 1.0 / Math.sqrt(lenSq);
        return new Vector2D(v.x() * invLen, v.y() * invLen);
    }
    public static Vector3D safeNormalize(Vector3D v, Vector3D fallback) {
        double lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        double invLen = 1.0 / Math.sqrt(lenSq);
        return new Vector3D(v.x() * invLen, v.y() * invLen, v.z() * invLen);
    }
    public static Vector4D safeNormalize(Vector4D v, Vector4D fallback) {
        double lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        double invLen = 1.0 / Math.sqrt(lenSq);
        return new Vector4D(v.x() * invLen, v.y() * invLen, v.z() * invLen, v.w() * invLen);
    }

    public static Vector2D setLength(Vector2D v, double len) {
        return scale(normalize(v), len);
    }
    public static Vector3D setLength(Vector3D v, double len) {
        return scale(normalize(v), len);
    }
    public static Vector4D setLength(Vector4D v, double len) {
        return scale(normalize(v), len);
    }

    public static Vector2D limit(Vector2D v, double maxLen) {
        double lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / Math.sqrt(lenSq));
        return v;
    }
    public static Vector3D limit(Vector3D v, double maxLen) {
        double lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / Math.sqrt(lenSq));
        return v;
    }
    public static Vector4D limit(Vector4D v, double maxLen) {
        double lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / Math.sqrt(lenSq));
        return v;
    }

    public static Vector2D midpoint(Vector2D a, Vector2D b) {
        return new Vector2D((a.x() + b.x()) * 0.5, (a.y() + b.y()) * 0.5);
    }
    public static Vector3D midpoint(Vector3D a, Vector3D b) {
        return new Vector3D((a.x() + b.x()) * 0.5, (a.y() + b.y()) * 0.5, (a.z() + b.z()) * 0.5);
    }
    public static Vector4D midpoint(Vector4D a, Vector4D b) {
        return new Vector4D((a.x() + b.x()) * 0.5, (a.y() + b.y()) * 0.5, (a.z() + b.z()) * 0.5, (a.w() + b.w()) * 0.5);
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

    public static Vector3D nlerp(Vector3D a, Vector3D b, double t) {
        return normalize(lerp(a, b, t));
    }
    public static Vector3D slerp(Vector3D a, Vector3D b, double t) {
        double d = clamp(dot(a, b), -1.0, 1.0);
        double theta = Math.acos(d) * t;
        Vector3D relative = normalize(sub(b, scale(a, d)));
        return add(scale(a, Math.cos(theta)), scale(relative, Math.sin(theta)));
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

    public static Vector2D refract(Vector2D direction, Vector2D normal, double eta) {
        double dDotN = dot(direction, normal);
        double k = 1.0 - eta * eta * (1.0 - dDotN * dDotN);
        if (k < 0.0) return new Vector2D(0.0, 0.0);
        return sub(scale(direction, eta), scale(normal, eta * dDotN + Math.sqrt(k)));
    }
    public static Vector3D refract(Vector3D direction, Vector3D normal, double eta) {
        double dDotN = dot(direction, normal);
        double k = 1.0 - eta * eta * (1.0 - dDotN * dDotN);
        if (k < 0.0) return new Vector3D(0.0, 0.0, 0.0);
        return sub(scale(direction, eta), scale(normal, eta * dDotN + Math.sqrt(k)));
    }

    public static Vector2D project(Vector2D v, Vector2D onto) {
        double s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }
    public static Vector3D project(Vector3D v, Vector3D onto) {
        double s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }
    public static Vector2D reject(Vector2D v, Vector2D onto) { return sub(v, project(v, onto)); }
    public static Vector3D reject(Vector3D v, Vector3D onto) { return sub(v, project(v, onto)); }

    public static Vector3D faceforward(Vector3D n, Vector3D i, Vector3D nRef) {
        return dot(nRef, i) < 0.0 ? n : negate(n);
    }
    public static Vector3D projectOnPlane(Vector3D v, Vector3D planeNormal) {
        return sub(v, scale(planeNormal, dot(v, planeNormal)));
    }

    public static double angle(Vector2D a, Vector2D b) {
        return Math.acos(clamp(dot(a, b) / (length(a) * length(b)), -1.0, 1.0));
    }
    public static double angle(Vector3D a, Vector3D b) {
        return Math.acos(clamp(dot(a, b) / (length(a) * length(b)), -1.0, 1.0));
    }
    public static double angleSigned(Vector2D a, Vector2D b) {
        return Math.atan2(cross2D(a, b), dot(a, b));
    }
    public static double angleSigned(Vector3D a, Vector3D b, Vector3D normal) {
        double d = dot(a, b) / (length(a) * length(b));
        Vector3D c = cross(a, b);
        double sign = dot(c, normal) >= 0.0 ? 1.0 : -1.0;
        return sign * Math.acos(clamp(d, -1.0, 1.0));
    }

    public static Vector2D rotate(Vector2D v, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Vector2D(v.x() * c - v.y() * s, v.x() * s + v.y() * c);
    }
    public static Vector3D rotateAroundAxis(Vector3D v, Vector3D axis, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Vector3D k = normalize(axis);
        return add(add(scale(v, c), scale(cross(k, v), s)), scale(k, dot(k, v) * (1.0 - c)));
    }

    public static Vector2D min(Vector2D a, Vector2D b) {
        return new Vector2D(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
    }
    public static Vector3D min(Vector3D a, Vector3D b) {
        return new Vector3D(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
    }
    public static Vector4D min(Vector4D a, Vector4D b) {
        return new Vector4D(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()), Math.min(a.w(), b.w()));
    }
    public static Vector2D max(Vector2D a, Vector2D b) {
        return new Vector2D(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
    }
    public static Vector3D max(Vector3D a, Vector3D b) {
        return new Vector3D(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
    }
    public static Vector4D max(Vector4D a, Vector4D b) {
        return new Vector4D(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()), Math.max(a.w(), b.w()));
    }

    public static Vector2D clamp(Vector2D v, Vector2D min, Vector2D max) {
        return new Vector2D(clamp(v.x(), min.x(), max.x()), clamp(v.y(), min.y(), max.y()));
    }
    public static Vector3D clamp(Vector3D v, Vector3D min, Vector3D max) {
        return new Vector3D(clamp(v.x(), min.x(), max.x()), clamp(v.y(), min.y(), max.y()), clamp(v.z(), min.z(), max.z()));
    }
    public static Vector4D clamp(Vector4D v, Vector4D min, Vector4D max) {
        return new Vector4D(clamp(v.x(), min.x(), max.x()), clamp(v.y(), min.y(), max.y()), clamp(v.z(), min.z(), max.z()), clamp(v.w(), min.w(), max.w()));
    }

    public static Vector2D perpendicular(Vector2D v) { return new Vector2D(-v.y(), v.x()); }

    public static boolean isCollinear(Vector2D a, Vector2D b, Vector2D c) {
        return Math.abs(cross2D(a, b, c)) < EPSILON;
    }
    public static boolean isCollinear(Vector3D a, Vector3D b, Vector3D c) {
        return length(cross(sub(b, a), sub(c, a))) < EPSILON;
    }
    public static boolean isCoplanar(Vector3D a, Vector3D b, Vector3D c, Vector3D d) {
        return Math.abs(tripleScalar(sub(b, a), sub(c, a), sub(d, a))) < EPSILON;
    }
    public static boolean isOrthogonal(Vector2D a, Vector2D b) { return Math.abs(dot(a, b)) < EPSILON; }
    public static boolean isOrthogonal(Vector3D a, Vector3D b) { return Math.abs(dot(a, b)) < EPSILON; }
    public static boolean isParallel(Vector2D a, Vector2D b) { return Math.abs(cross2D(a, b)) < EPSILON; }
    public static boolean isParallel(Vector3D a, Vector3D b) { return length(cross(a, b)) < EPSILON; }

    public static Vector2D closestPointOnSegment(Vector2D p, Vector2D a, Vector2D b) {
        Vector2D ab = sub(b, a);
        double t = clamp(dot(sub(p, a), ab) / dot(ab, ab), 0.0, 1.0);
        return lerp(a, b, t);
    }
    public static Vector3D closestPointOnSegment(Vector3D p, Vector3D a, Vector3D b) {
        Vector3D ab = sub(b, a);
        double t = clamp(dot(sub(p, a), ab) / dot(ab, ab), 0.0, 1.0);
        return lerp(a, b, t);
    }
    public static double distanceToSegment(Vector2D p, Vector2D a, Vector2D b) {
        return distance(p, closestPointOnSegment(p, a, b));
    }
    public static double distanceToSegment(Vector3D p, Vector3D a, Vector3D b) {
        return distance(p, closestPointOnSegment(p, a, b));
    }

    public static Vector2D catmullRom(Vector2D a, Vector2D b, Vector2D c, Vector2D d, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        return new Vector2D(
            0.5 * ((2.0 * b.x()) + (-a.x() + c.x()) * t + (2.0 * a.x() - 5.0 * b.x() + 4.0 * c.x() - d.x()) * t2 + (-a.x() + 3.0 * b.x() - 3.0 * c.x() + d.x()) * t3),
            0.5 * ((2.0 * b.y()) + (-a.y() + c.y()) * t + (2.0 * a.y() - 5.0 * b.y() + 4.0 * c.y() - d.y()) * t2 + (-a.y() + 3.0 * b.y() - 3.0 * c.y() + d.y()) * t3)
        );
    }
    public static Vector3D catmullRom(Vector3D a, Vector3D b, Vector3D c, Vector3D d, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        return new Vector3D(
            0.5 * ((2.0 * b.x()) + (-a.x() + c.x()) * t + (2.0 * a.x() - 5.0 * b.x() + 4.0 * c.x() - d.x()) * t2 + (-a.x() + 3.0 * b.x() - 3.0 * c.x() + d.x()) * t3),
            0.5 * ((2.0 * b.y()) + (-a.y() + c.y()) * t + (2.0 * a.y() - 5.0 * b.y() + 4.0 * c.y() - d.y()) * t2 + (-a.y() + 3.0 * b.y() - 3.0 * c.y() + d.y()) * t3),
            0.5 * ((2.0 * b.z()) + (-a.z() + c.z()) * t + (2.0 * a.z() - 5.0 * b.z() + 4.0 * c.z() - d.z()) * t2 + (-a.z() + 3.0 * b.z() - 3.0 * c.z() + d.z()) * t3)
        );
    }

    public static Vector2D bezier(Vector2D a, Vector2D b, Vector2D c, Vector2D d, double t) {
        double u = 1.0 - t;
        return add(add(add(scale(a, u * u * u), scale(b, 3.0 * u * u * t)), scale(c, 3.0 * u * t * t)), scale(d, t * t * t));
    }
    public static Vector3D bezier(Vector3D a, Vector3D b, Vector3D c, Vector3D d, double t) {
        double u = 1.0 - t;
        return add(add(add(scale(a, u * u * u), scale(b, 3.0 * u * u * t)), scale(c, 3.0 * u * t * t)), scale(d, t * t * t));
    }

    public static Vector3D orthonormalBasisX(Vector3D v) {
        Vector3D n = normalize(v);
        if (Math.abs(n.x()) < Math.abs(n.z())) {
            return normalize(cross(n, new Vector3D(0.0, -1.0, 0.0)));
        }
        return normalize(cross(n, new Vector3D(0.0, 0.0, 1.0)));
    }
    public static Vector3D orthonormalBasisY(Vector3D v) { return normalize(v); }
    public static Vector3D orthonormalBasisZ(Vector3D v) {
        return cross(orthonormalBasisX(v), normalize(v));
    }

    public static double[] toArray(Vector2D v) { return new double[]{v.x(), v.y()}; }
    public static double[] toArray3(Vector3D v) { return new double[]{v.x(), v.y(), v.z()}; }
    public static double[] toArray4(Vector4D v) { return new double[]{v.x(), v.y(), v.z(), v.w()}; }
    public static Vector2D fromArray2(double[] arr, int offset) { return new Vector2D(arr[offset], arr[offset + 1]); }
    public static Vector3D fromArray3(double[] arr, int offset) { return new Vector3D(arr[offset], arr[offset + 1], arr[offset + 2]); }
    public static Vector4D fromArray4(double[] arr, int offset) { return new Vector4D(arr[offset], arr[offset + 1], arr[offset + 2], arr[offset + 3]); }

    public static Vector3D[] batchAdd(Vector3D[] a, Vector3D[] b) {
        int n = a.length;
        Vector3D[] result = new Vector3D[n];
        for (int i = 0; i < n; i++) result[i] = add(a[i], b[i]);
        return result;
    }
    public static double[] batchDot(Vector3D[] a, Vector3D[] b) {
        int n = a.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = dot(a[i], b[i]);
        return result;
    }
    public static Vector3D[] batchScale(Vector3D[] v, double s) {
        int n = v.length;
        Vector3D[] result = new Vector3D[n];
        for (int i = 0; i < n; i++) result[i] = scale(v[i], s);
        return result;
    }
    public static Vector3D[] batchNormalize(Vector3D[] v) {
        int n = v.length;
        Vector3D[] result = new Vector3D[n];
        for (int i = 0; i < n; i++) result[i] = normalize(v[i]);
        return result;
    }
    public static Vector3D[] batchNegate(Vector3D[] v) {
        int n = v.length;
        Vector3D[] result = new Vector3D[n];
        for (int i = 0; i < n; i++) result[i] = negate(v[i]);
        return result;
    }

    public static boolean isFinite(Vector2D v) { return Double.isFinite(v.x()) && Double.isFinite(v.y()); }
    public static boolean isFinite(Vector3D v) { return Double.isFinite(v.x()) && Double.isFinite(v.y()) && Double.isFinite(v.z()); }
    public static boolean isFinite(Vector4D v) { return Double.isFinite(v.x()) && Double.isFinite(v.y()) && Double.isFinite(v.z()) && Double.isFinite(v.w()); }
    public static boolean hasNaN(Vector2D v) { return Double.isNaN(v.x()) || Double.isNaN(v.y()); }
    public static boolean hasNaN(Vector3D v) { return Double.isNaN(v.x()) || Double.isNaN(v.y()) || Double.isNaN(v.z()); }
    public static boolean hasNaN(Vector4D v) { return Double.isNaN(v.x()) || Double.isNaN(v.y()) || Double.isNaN(v.z()) || Double.isNaN(v.w()); }

    public static double componentMin(Vector2D v) { return Math.min(v.x(), v.y()); }
    public static double componentMin(Vector3D v) { return Math.min(v.x(), Math.min(v.y(), v.z())); }
    public static double componentMin(Vector4D v) { return Math.min(Math.min(v.x(), v.y()), Math.min(v.z(), v.w())); }
    public static double componentMax(Vector2D v) { return Math.max(v.x(), v.y()); }
    public static double componentMax(Vector3D v) { return Math.max(v.x(), Math.max(v.y(), v.z())); }
    public static double componentMax(Vector4D v) { return Math.max(Math.max(v.x(), v.y()), Math.max(v.z(), v.w())); }
    public static double componentSum(Vector2D v) { return v.x() + v.y(); }
    public static double componentSum(Vector3D v) { return v.x() + v.y() + v.z(); }
    public static double componentSum(Vector4D v) { return v.x() + v.y() + v.z() + v.w(); }

    private static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}

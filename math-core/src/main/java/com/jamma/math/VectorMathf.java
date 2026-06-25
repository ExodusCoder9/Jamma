package com.jamma.math;

/**
 * Allocation-free static vector math operations for single-precision types.
 * <p>
 * Each operation has two overloads:
 * <ul>
 *   <li>AN allocating overload that returns a new {@code record} instance.
 *   <li>A {@code float[] dest, int offset} overload that writes into a caller-provided
 *       array — zero allocations.
 * </ul>
 */
public final class VectorMathf {

    private static final float EPSILON = 1.0e-6f;

    private VectorMathf() {
    }

    public static Vector2f add(Vector2f a, Vector2f b) { return new Vector2f(a.x() + b.x(), a.y() + b.y()); }
    public static Vector3f add(Vector3f a, Vector3f b) { return new Vector3f(a.x() + b.x(), a.y() + b.y(), a.z() + b.z()); }
    public static Vector4f add(Vector4f a, Vector4f b) { return new Vector4f(a.x() + b.x(), a.y() + b.y(), a.z() + b.z(), a.w() + b.w()); }
    public static void add(Vector3f a, Vector3f b, float[] dest, int offset) {
        dest[offset] = a.x() + b.x(); dest[offset + 1] = a.y() + b.y(); dest[offset + 2] = a.z() + b.z();
    }

    public static Vector2f sub(Vector2f a, Vector2f b) { return new Vector2f(a.x() - b.x(), a.y() - b.y()); }
    public static Vector3f sub(Vector3f a, Vector3f b) { return new Vector3f(a.x() - b.x(), a.y() - b.y(), a.z() - b.z()); }
    public static Vector4f sub(Vector4f a, Vector4f b) { return new Vector4f(a.x() - b.x(), a.y() - b.y(), a.z() - b.z(), a.w() - b.w()); }
    public static void sub(Vector3f a, Vector3f b, float[] dest, int offset) {
        dest[offset] = a.x() - b.x(); dest[offset + 1] = a.y() - b.y(); dest[offset + 2] = a.z() - b.z();
    }

    public static Vector2f mul(Vector2f a, Vector2f b) { return new Vector2f(a.x() * b.x(), a.y() * b.y()); }
    public static Vector3f mul(Vector3f a, Vector3f b) { return new Vector3f(a.x() * b.x(), a.y() * b.y(), a.z() * b.z()); }
    public static Vector4f mul(Vector4f a, Vector4f b) { return new Vector4f(a.x() * b.x(), a.y() * b.y(), a.z() * b.z(), a.w() * b.w()); }
    public static void mul(Vector3f a, Vector3f b, float[] dest, int offset) {
        dest[offset] = a.x() * b.x(); dest[offset + 1] = a.y() * b.y(); dest[offset + 2] = a.z() * b.z();
    }

    public static Vector2f div(Vector2f a, Vector2f b) { return new Vector2f(a.x() / b.x(), a.y() / b.y()); }
    public static Vector3f div(Vector3f a, Vector3f b) { return new Vector3f(a.x() / b.x(), a.y() / b.y(), a.z() / b.z()); }
    public static Vector4f div(Vector4f a, Vector4f b) { return new Vector4f(a.x() / b.x(), a.y() / b.y(), a.z() / b.z(), a.w() / b.w()); }

    public static Vector2f scale(Vector2f v, float s) { return new Vector2f(v.x() * s, v.y() * s); }
    public static Vector3f scale(Vector3f v, float s) { return new Vector3f(v.x() * s, v.y() * s, v.z() * s); }
    public static Vector4f scale(Vector4f v, float s) { return new Vector4f(v.x() * s, v.y() * s, v.z() * s, v.w() * s); }
    public static void scale(Vector3f v, float s, float[] dest, int offset) {
        dest[offset] = v.x() * s; dest[offset + 1] = v.y() * s; dest[offset + 2] = v.z() * s;
    }

    public static Vector2f negate(Vector2f v) { return new Vector2f(-v.x(), -v.y()); }
    public static Vector3f negate(Vector3f v) { return new Vector3f(-v.x(), -v.y(), -v.z()); }
    public static Vector4f negate(Vector4f v) { return new Vector4f(-v.x(), -v.y(), -v.z(), -v.w()); }

    public static Vector2f abs(Vector2f v) { return new Vector2f(Math.abs(v.x()), Math.abs(v.y())); }
    public static Vector3f abs(Vector3f v) { return new Vector3f(Math.abs(v.x()), Math.abs(v.y()), Math.abs(v.z())); }
    public static Vector4f abs(Vector4f v) { return new Vector4f(Math.abs(v.x()), Math.abs(v.y()), Math.abs(v.z()), Math.abs(v.w())); }

    public static Vector2f sign(Vector2f v) { return new Vector2f(Math.signum(v.x()), Math.signum(v.y())); }
    public static Vector3f sign(Vector3f v) { return new Vector3f(Math.signum(v.x()), Math.signum(v.y()), Math.signum(v.z())); }
    public static Vector4f sign(Vector4f v) { return new Vector4f(Math.signum(v.x()), Math.signum(v.y()), Math.signum(v.z()), Math.signum(v.w())); }

    public static float dot(Vector2f a, Vector2f b) { return Math.fma(a.x(), b.x(), a.y() * b.y()); }
    public static float dot(Vector3f a, Vector3f b) {
        return Math.fma(a.x(), b.x(), Math.fma(a.y(), b.y(), a.z() * b.z()));
    }
    public static float dot(Vector4f a, Vector4f b) {
        return Math.fma(a.x(), b.x(), Math.fma(a.y(), b.y(), Math.fma(a.z(), b.z(), a.w() * b.w())));
    }

    public static Vector3f cross(Vector3f a, Vector3f b) {
        return new Vector3f(
            a.y() * b.z() - a.z() * b.y(),
            a.z() * b.x() - a.x() * b.z(),
            a.x() * b.y() - a.y() * b.x()
        );
    }
    public static void cross(Vector3f a, Vector3f b, float[] dest, int offset) {
        dest[offset] = a.y() * b.z() - a.z() * b.y();
        dest[offset + 1] = a.z() * b.x() - a.x() * b.z();
        dest[offset + 2] = a.x() * b.y() - a.y() * b.x();
    }
    public static float cross2D(Vector2f a, Vector2f b) {
        return a.x() * b.y() - a.y() * b.x();
    }
    public static float cross2D(Vector2f a, Vector2f b, Vector2f c) {
        return (b.x() - a.x()) * (c.y() - a.y()) - (b.y() - a.y()) * (c.x() - a.x());
    }

    public static float tripleScalar(Vector3f a, Vector3f b, Vector3f c) {
        return dot(a, cross(b, c));
    }
    public static Vector3f tripleVector(Vector3f a, Vector3f b, Vector3f c) {
        return sub(scale(b, dot(a, c)), scale(c, dot(a, b)));
    }

    public static float length(Vector2f v) { return (float) Math.sqrt(dot(v, v)); }
    public static float length(Vector3f v) { return (float) Math.sqrt(dot(v, v)); }
    public static float length(Vector4f v) { return (float) Math.sqrt(dot(v, v)); }
    public static float lengthSquared(Vector2f v) { return dot(v, v); }
    public static float lengthSquared(Vector3f v) { return dot(v, v); }
    public static float lengthSquared(Vector4f v) { return dot(v, v); }

    public static float distance(Vector2f a, Vector2f b) { return (float) Math.sqrt(distanceSquared(a, b)); }
    public static float distance(Vector3f a, Vector3f b) { return (float) Math.sqrt(distanceSquared(a, b)); }
    public static float distance(Vector4f a, Vector4f b) { return (float) Math.sqrt(distanceSquared(a, b)); }
    public static float distanceSquared(Vector2f a, Vector2f b) {
        float dx = a.x() - b.x(), dy = a.y() - b.y();
        return Math.fma(dx, dx, dy * dy);
    }
    public static float distanceSquared(Vector3f a, Vector3f b) {
        float dx = a.x() - b.x(), dy = a.y() - b.y(), dz = a.z() - b.z();
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }
    public static float distanceSquared(Vector4f a, Vector4f b) {
        float dx = a.x() - b.x(), dy = a.y() - b.y(), dz = a.z() - b.z(), dw = a.w() - b.w();
        return Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw)));
    }

    public static Vector2f normalize(Vector2f v) {
        float len = length(v);
        return new Vector2f(v.x() / len, v.y() / len);
    }
    public static Vector3f normalize(Vector3f v) {
        float len = length(v);
        return new Vector3f(v.x() / len, v.y() / len, v.z() / len);
    }
    public static void normalize(Vector3f v, float[] dest, int offset) {
        float invLen = 1.0f / length(v);
        dest[offset] = v.x() * invLen; dest[offset + 1] = v.y() * invLen; dest[offset + 2] = v.z() * invLen;
    }
    public static Vector4f normalize(Vector4f v) {
        float len = length(v);
        return new Vector4f(v.x() / len, v.y() / len, v.z() / len, v.w() / len);
    }

    public static Vector2f safeNormalize(Vector2f v, Vector2f fallback) {
        float lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        float invLen = 1.0f / (float) Math.sqrt(lenSq);
        return new Vector2f(v.x() * invLen, v.y() * invLen);
    }
    public static Vector3f safeNormalize(Vector3f v, Vector3f fallback) {
        float lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        float invLen = 1.0f / (float) Math.sqrt(lenSq);
        return new Vector3f(v.x() * invLen, v.y() * invLen, v.z() * invLen);
    }
    public static Vector4f safeNormalize(Vector4f v, Vector4f fallback) {
        float lenSq = lengthSquared(v);
        if (lenSq < EPSILON) return fallback;
        float invLen = 1.0f / (float) Math.sqrt(lenSq);
        return new Vector4f(v.x() * invLen, v.y() * invLen, v.z() * invLen, v.w() * invLen);
    }

    public static Vector2f setLength(Vector2f v, float len) {
        return scale(normalize(v), len);
    }
    public static Vector3f setLength(Vector3f v, float len) {
        return scale(normalize(v), len);
    }
    public static Vector4f setLength(Vector4f v, float len) {
        return scale(normalize(v), len);
    }

    public static Vector2f limit(Vector2f v, float maxLen) {
        float lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / (float) Math.sqrt(lenSq));
        return v;
    }
    public static Vector3f limit(Vector3f v, float maxLen) {
        float lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / (float) Math.sqrt(lenSq));
        return v;
    }
    public static Vector4f limit(Vector4f v, float maxLen) {
        float lenSq = lengthSquared(v);
        if (lenSq > maxLen * maxLen) return scale(v, maxLen / (float) Math.sqrt(lenSq));
        return v;
    }

    public static Vector2f midpoint(Vector2f a, Vector2f b) {
        return new Vector2f((a.x() + b.x()) * 0.5f, (a.y() + b.y()) * 0.5f);
    }
    public static Vector3f midpoint(Vector3f a, Vector3f b) {
        return new Vector3f((a.x() + b.x()) * 0.5f, (a.y() + b.y()) * 0.5f, (a.z() + b.z()) * 0.5f);
    }
    public static Vector4f midpoint(Vector4f a, Vector4f b) {
        return new Vector4f((a.x() + b.x()) * 0.5f, (a.y() + b.y()) * 0.5f, (a.z() + b.z()) * 0.5f, (a.w() + b.w()) * 0.5f);
    }

    public static Vector2f lerp(Vector2f a, Vector2f b, float t) {
        return new Vector2f(Math.fma(t, b.x() - a.x(), a.x()), Math.fma(t, b.y() - a.y(), a.y()));
    }
    public static Vector3f lerp(Vector3f a, Vector3f b, float t) {
        return new Vector3f(
            Math.fma(t, b.x() - a.x(), a.x()),
            Math.fma(t, b.y() - a.y(), a.y()),
            Math.fma(t, b.z() - a.z(), a.z())
        );
    }
    public static void lerp(Vector3f a, Vector3f b, float t, float[] dest, int offset) {
        dest[offset] = Math.fma(t, b.x() - a.x(), a.x());
        dest[offset + 1] = Math.fma(t, b.y() - a.y(), a.y());
        dest[offset + 2] = Math.fma(t, b.z() - a.z(), a.z());
    }
    public static Vector4f lerp(Vector4f a, Vector4f b, float t) {
        return new Vector4f(
            Math.fma(t, b.x() - a.x(), a.x()),
            Math.fma(t, b.y() - a.y(), a.y()),
            Math.fma(t, b.z() - a.z(), a.z()),
            Math.fma(t, b.w() - a.w(), a.w())
        );
    }

    public static Vector3f nlerp(Vector3f a, Vector3f b, float t) {
        return normalize(lerp(a, b, t));
    }
    public static Vector3f slerp(Vector3f a, Vector3f b, float t) {
        float d = Math.clamp(dot(a, b), -1.0f, 1.0f);
        float theta = (float) Math.acos(d) * t;
        Vector3f relative = normalize(sub(b, scale(a, d)));
        return add(scale(a, (float) Math.cos(theta)), scale(relative, (float) Math.sin(theta)));
    }

    public static Vector2f reflect(Vector2f direction, Vector2f normal) {
        float dDotN = dot(direction, normal);
        return new Vector2f(
            direction.x() - 2.0f * dDotN * normal.x(),
            direction.y() - 2.0f * dDotN * normal.y()
        );
    }
    public static Vector3f reflect(Vector3f direction, Vector3f normal) {
        float dDotN = dot(direction, normal);
        return new Vector3f(
            direction.x() - 2.0f * dDotN * normal.x(),
            direction.y() - 2.0f * dDotN * normal.y(),
            direction.z() - 2.0f * dDotN * normal.z()
        );
    }

    public static Vector2f refract(Vector2f direction, Vector2f normal, float eta) {
        float dDotN = dot(direction, normal);
        float k = 1.0f - eta * eta * (1.0f - dDotN * dDotN);
        if (k < 0.0f) return new Vector2f(0.0f, 0.0f);
        return sub(scale(direction, eta), scale(normal, eta * dDotN + (float) Math.sqrt(k)));
    }
    public static Vector3f refract(Vector3f direction, Vector3f normal, float eta) {
        float dDotN = dot(direction, normal);
        float k = 1.0f - eta * eta * (1.0f - dDotN * dDotN);
        if (k < 0.0f) return new Vector3f(0.0f, 0.0f, 0.0f);
        return sub(scale(direction, eta), scale(normal, eta * dDotN + (float) Math.sqrt(k)));
    }

    public static Vector2f project(Vector2f v, Vector2f onto) {
        float s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }
    public static Vector3f project(Vector3f v, Vector3f onto) {
        float s = dot(v, onto) / dot(onto, onto);
        return scale(onto, s);
    }
    public static Vector2f reject(Vector2f v, Vector2f onto) { return sub(v, project(v, onto)); }
    public static Vector3f reject(Vector3f v, Vector3f onto) { return sub(v, project(v, onto)); }

    public static Vector3f faceforward(Vector3f n, Vector3f i, Vector3f nRef) {
        return dot(nRef, i) < 0.0f ? n : negate(n);
    }
    public static Vector3f projectOnPlane(Vector3f v, Vector3f planeNormal) {
        return sub(v, scale(planeNormal, dot(v, planeNormal)));
    }

    public static float angle(Vector2f a, Vector2f b) {
        return (float) Math.acos(Math.clamp(dot(a, b) / (length(a) * length(b)), -1.0f, 1.0f));
    }
    public static float angle(Vector3f a, Vector3f b) {
        return (float) Math.acos(Math.clamp(dot(a, b) / (length(a) * length(b)), -1.0f, 1.0f));
    }
    public static float angleSigned(Vector2f a, Vector2f b) {
        return (float) Math.atan2(cross2D(a, b), dot(a, b));
    }
    public static float angleSigned(Vector3f a, Vector3f b, Vector3f normal) {
        float d = dot(a, b) / (length(a) * length(b));
        Vector3f c = cross(a, b);
        float sign = dot(c, normal) >= 0.0f ? 1.0f : -1.0f;
        return sign * (float) Math.acos(Math.clamp(d, -1.0f, 1.0f));
    }

    public static Vector2f rotate(Vector2f v, float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        return new Vector2f(v.x() * c - v.y() * s, v.x() * s + v.y() * c);
    }
    public static Vector3f rotateAroundAxis(Vector3f v, Vector3f axis, float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        Vector3f k = normalize(axis);
        return add(add(scale(v, c), scale(cross(k, v), s)), scale(k, dot(k, v) * (1.0f - c)));
    }

    public static Vector2f min(Vector2f a, Vector2f b) {
        return new Vector2f(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
    }
    public static Vector3f min(Vector3f a, Vector3f b) {
        return new Vector3f(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
    }
    public static Vector4f min(Vector4f a, Vector4f b) {
        return new Vector4f(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()), Math.min(a.w(), b.w()));
    }
    public static Vector2f max(Vector2f a, Vector2f b) {
        return new Vector2f(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
    }
    public static Vector3f max(Vector3f a, Vector3f b) {
        return new Vector3f(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
    }
    public static Vector4f max(Vector4f a, Vector4f b) {
        return new Vector4f(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()), Math.max(a.w(), b.w()));
    }

    public static Vector2f clamp(Vector2f v, Vector2f min, Vector2f max) {
        return new Vector2f(Math.clamp(v.x(), min.x(), max.x()), Math.clamp(v.y(), min.y(), max.y()));
    }
    public static Vector3f clamp(Vector3f v, Vector3f min, Vector3f max) {
        return new Vector3f(Math.clamp(v.x(), min.x(), max.x()), Math.clamp(v.y(), min.y(), max.y()), Math.clamp(v.z(), min.z(), max.z()));
    }
    public static Vector4f clamp(Vector4f v, Vector4f min, Vector4f max) {
        return new Vector4f(Math.clamp(v.x(), min.x(), max.x()), Math.clamp(v.y(), min.y(), max.y()), Math.clamp(v.z(), min.z(), max.z()), Math.clamp(v.w(), min.w(), max.w()));
    }

    public static Vector2f perpendicular(Vector2f v) { return new Vector2f(-v.y(), v.x()); }

    public static boolean isCollinear(Vector2f a, Vector2f b, Vector2f c) {
        return Math.abs(cross2D(a, b, c)) < EPSILON;
    }
    public static boolean isCollinear(Vector3f a, Vector3f b, Vector3f c) {
        return length(cross(sub(b, a), sub(c, a))) < EPSILON;
    }
    public static boolean isCoplanar(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        return Math.abs(tripleScalar(sub(b, a), sub(c, a), sub(d, a))) < EPSILON;
    }
    public static boolean isOrthogonal(Vector2f a, Vector2f b) { return Math.abs(dot(a, b)) < EPSILON; }
    public static boolean isOrthogonal(Vector3f a, Vector3f b) { return Math.abs(dot(a, b)) < EPSILON; }
    public static boolean isParallel(Vector2f a, Vector2f b) { return Math.abs(cross2D(a, b)) < EPSILON; }
    public static boolean isParallel(Vector3f a, Vector3f b) { return length(cross(a, b)) < EPSILON; }

    public static Vector2f closestPointOnSegment(Vector2f p, Vector2f a, Vector2f b) {
        Vector2f ab = sub(b, a);
        float t = Math.clamp(dot(sub(p, a), ab) / dot(ab, ab), 0.0f, 1.0f);
        return lerp(a, b, t);
    }
    public static Vector3f closestPointOnSegment(Vector3f p, Vector3f a, Vector3f b) {
        Vector3f ab = sub(b, a);
        float t = Math.clamp(dot(sub(p, a), ab) / dot(ab, ab), 0.0f, 1.0f);
        return lerp(a, b, t);
    }
    public static float distanceToSegment(Vector2f p, Vector2f a, Vector2f b) {
        return distance(p, closestPointOnSegment(p, a, b));
    }
    public static float distanceToSegment(Vector3f p, Vector3f a, Vector3f b) {
        return distance(p, closestPointOnSegment(p, a, b));
    }

    public static Vector2f catmullRom(Vector2f a, Vector2f b, Vector2f c, Vector2f d, float t) {
        float t2 = t * t;
        float t3 = t2 * t;
        return new Vector2f(
            0.5f * ((2.0f * b.x()) + (-a.x() + c.x()) * t + (2.0f * a.x() - 5.0f * b.x() + 4.0f * c.x() - d.x()) * t2 + (-a.x() + 3.0f * b.x() - 3.0f * c.x() + d.x()) * t3),
            0.5f * ((2.0f * b.y()) + (-a.y() + c.y()) * t + (2.0f * a.y() - 5.0f * b.y() + 4.0f * c.y() - d.y()) * t2 + (-a.y() + 3.0f * b.y() - 3.0f * c.y() + d.y()) * t3)
        );
    }
    public static Vector3f catmullRom(Vector3f a, Vector3f b, Vector3f c, Vector3f d, float t) {
        float t2 = t * t;
        float t3 = t2 * t;
        return new Vector3f(
            0.5f * ((2.0f * b.x()) + (-a.x() + c.x()) * t + (2.0f * a.x() - 5.0f * b.x() + 4.0f * c.x() - d.x()) * t2 + (-a.x() + 3.0f * b.x() - 3.0f * c.x() + d.x()) * t3),
            0.5f * ((2.0f * b.y()) + (-a.y() + c.y()) * t + (2.0f * a.y() - 5.0f * b.y() + 4.0f * c.y() - d.y()) * t2 + (-a.y() + 3.0f * b.y() - 3.0f * c.y() + d.y()) * t3),
            0.5f * ((2.0f * b.z()) + (-a.z() + c.z()) * t + (2.0f * a.z() - 5.0f * b.z() + 4.0f * c.z() - d.z()) * t2 + (-a.z() + 3.0f * b.z() - 3.0f * c.z() + d.z()) * t3)
        );
    }

    public static Vector2f bezier(Vector2f a, Vector2f b, Vector2f c, Vector2f d, float t) {
        float u = 1.0f - t;
        return add(add(add(scale(a, u * u * u), scale(b, 3.0f * u * u * t)), scale(c, 3.0f * u * t * t)), scale(d, t * t * t));
    }
    public static Vector3f bezier(Vector3f a, Vector3f b, Vector3f c, Vector3f d, float t) {
        float u = 1.0f - t;
        return add(add(add(scale(a, u * u * u), scale(b, 3.0f * u * u * t)), scale(c, 3.0f * u * t * t)), scale(d, t * t * t));
    }

    public static float[] toArray(Vector2f v) { return new float[]{v.x(), v.y()}; }
    public static float[] toArray3(Vector3f v) { return new float[]{v.x(), v.y(), v.z()}; }
    public static float[] toArray4(Vector4f v) { return new float[]{v.x(), v.y(), v.z(), v.w()}; }
    public static Vector2f fromArray2(float[] arr, int offset) { return new Vector2f(arr[offset], arr[offset + 1]); }
    public static Vector3f fromArray3(float[] arr, int offset) { return new Vector3f(arr[offset], arr[offset + 1], arr[offset + 2]); }
    public static Vector4f fromArray4(float[] arr, int offset) { return new Vector4f(arr[offset], arr[offset + 1], arr[offset + 2], arr[offset + 3]); }

    public static boolean isFinite(Vector2f v) { return Float.isFinite(v.x()) && Float.isFinite(v.y()); }
    public static boolean isFinite(Vector3f v) { return Float.isFinite(v.x()) && Float.isFinite(v.y()) && Float.isFinite(v.z()); }
    public static boolean isFinite(Vector4f v) { return Float.isFinite(v.x()) && Float.isFinite(v.y()) && Float.isFinite(v.z()) && Float.isFinite(v.w()); }
    public static boolean hasNaN(Vector2f v) { return Float.isNaN(v.x()) || Float.isNaN(v.y()); }
    public static boolean hasNaN(Vector3f v) { return Float.isNaN(v.x()) || Float.isNaN(v.y()) || Float.isNaN(v.z()); }
    public static boolean hasNaN(Vector4f v) { return Float.isNaN(v.x()) || Float.isNaN(v.y()) || Float.isNaN(v.z()) || Float.isNaN(v.w()); }

    public static float componentMin(Vector2f v) { return Math.min(v.x(), v.y()); }
    public static float componentMin(Vector3f v) { return Math.min(v.x(), Math.min(v.y(), v.z())); }
    public static float componentMin(Vector4f v) { return Math.min(Math.min(v.x(), v.y()), Math.min(v.z(), v.w())); }
    public static float componentMax(Vector2f v) { return Math.max(v.x(), v.y()); }
    public static float componentMax(Vector3f v) { return Math.max(v.x(), Math.max(v.y(), v.z())); }
    public static float componentMax(Vector4f v) { return Math.max(Math.max(v.x(), v.y()), Math.max(v.z(), v.w())); }
    public static float componentSum(Vector2f v) { return v.x() + v.y(); }
    public static float componentSum(Vector3f v) { return v.x() + v.y() + v.z(); }
    public static float componentSum(Vector4f v) { return v.x() + v.y() + v.z() + v.w(); }
}

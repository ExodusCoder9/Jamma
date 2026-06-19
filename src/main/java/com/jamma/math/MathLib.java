package com.jamma.math;

import com.jamma.math.incubator.ParallelOps;
import com.jamma.math.incubator.VectorMath;
import com.jamma.math.scalar.ScalarMath;

public final class MathLib {

    private MathLib() {
    }

    public static final double PI = ScalarMath.PI;
    public static final double E = ScalarMath.E;
    public static final double TAU = ScalarMath.TAU;

    public static double sin(double x) {
        return ScalarMath.sin(x);
    }

    public static double cos(double x) {
        return ScalarMath.cos(x);
    }

    public static double tan(double x) {
        return ScalarMath.tan(x);
    }

    public static double asin(double x) {
        return ScalarMath.asin(x);
    }

    public static double acos(double x) {
        return ScalarMath.acos(x);
    }

    public static double atan(double x) {
        return ScalarMath.atan(x);
    }

    public static double atan2(double y, double x) {
        return ScalarMath.atan2(y, x);
    }

    public static double sinh(double x) {
        return ScalarMath.sinh(x);
    }

    public static double cosh(double x) {
        return ScalarMath.cosh(x);
    }

    public static double tanh(double x) {
        return ScalarMath.tanh(x);
    }

    public static double sqrt(double x) {
        return ScalarMath.sqrt(x);
    }

    public static double invSqrt(double x) {
        return ScalarMath.invSqrt(x);
    }

    public static double cbrt(double x) {
        return ScalarMath.cbrt(x);
    }

    public static double pow(double a, double b) {
        return ScalarMath.pow(a, b);
    }

    public static double exp(double x) {
        return ScalarMath.exp(x);
    }

    public static double log(double x) {
        return ScalarMath.log(x);
    }

    public static double log10(double x) {
        return ScalarMath.log10(x);
    }

    public static double log2(double x) {
        return ScalarMath.log2(x);
    }

    public static double abs(double x) {
        return ScalarMath.abs(x);
    }

    public static float abs(float x) {
        return ScalarMath.abs(x);
    }

    public static int abs(int x) {
        return ScalarMath.abs(x);
    }

    public static double min(double a, double b) {
        return ScalarMath.min(a, b);
    }

    public static double max(double a, double b) {
        return ScalarMath.max(a, b);
    }

    public static double clamp(double value, double min, double max) {
        return ScalarMath.clamp(value, min, max);
    }

    public static double saturate(double value) {
        return ScalarMath.saturate(value);
    }

    public static double floor(double x) {
        return ScalarMath.floor(x);
    }

    public static double ceil(double x) {
        return ScalarMath.ceil(x);
    }

    public static double round(double x) {
        return ScalarMath.round(x);
    }

    public static double lerp(double a, double b, double t) {
        return ScalarMath.lerp(a, b, t);
    }

    public static double inverseLerp(double a, double b, double value) {
        return ScalarMath.inverseLerp(a, b, value);
    }

    public static double smoothstep(double edge0, double edge1, double x) {
        return ScalarMath.smoothstep(edge0, edge1, x);
    }

    public static double signum(double x) {
        return ScalarMath.signum(x);
    }

    public static double fma(double a, double b, double c) {
        return ScalarMath.fma(a, b, c);
    }

    public static double toRadians(double degrees) {
        return ScalarMath.toRadians(degrees);
    }

    public static double toDegrees(double radians) {
        return ScalarMath.toDegrees(radians);
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return VectorMath.add(a, b);
    }

    public static Vector3D add(Vector3D a, Vector3D b) {
        return VectorMath.add(a, b);
    }

    public static Vector4D add(Vector4D a, Vector4D b) {
        return VectorMath.add(a, b);
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return VectorMath.sub(a, b);
    }

    public static Vector3D sub(Vector3D a, Vector3D b) {
        return VectorMath.sub(a, b);
    }

    public static Vector4D sub(Vector4D a, Vector4D b) {
        return VectorMath.sub(a, b);
    }

    public static Vector2D mul(Vector2D a, Vector2D b) {
        return VectorMath.mul(a, b);
    }

    public static Vector3D mul(Vector3D a, Vector3D b) {
        return VectorMath.mul(a, b);
    }

    public static Vector4D mul(Vector4D a, Vector4D b) {
        return VectorMath.mul(a, b);
    }

    public static Vector2D scale(Vector2D v, double s) {
        return VectorMath.scale(v, s);
    }

    public static Vector3D scale(Vector3D v, double s) {
        return VectorMath.scale(v, s);
    }

    public static Vector4D scale(Vector4D v, double s) {
        return VectorMath.scale(v, s);
    }

    public static double dot(Vector2D a, Vector2D b) {
        return VectorMath.dot(a, b);
    }

    public static double dot(Vector3D a, Vector3D b) {
        return VectorMath.dot(a, b);
    }

    public static double dot(Vector4D a, Vector4D b) {
        return VectorMath.dot(a, b);
    }

    public static Vector3D cross(Vector3D a, Vector3D b) {
        return VectorMath.cross(a, b);
    }

    public static double length(Vector2D v) {
        return VectorMath.length(v);
    }

    public static double length(Vector3D v) {
        return VectorMath.length(v);
    }

    public static double length(Vector4D v) {
        return VectorMath.length(v);
    }

    public static double lengthSquared(Vector2D v) {
        return VectorMath.lengthSquared(v);
    }

    public static double lengthSquared(Vector3D v) {
        return VectorMath.lengthSquared(v);
    }

    public static double lengthSquared(Vector4D v) {
        return VectorMath.lengthSquared(v);
    }

    public static Vector2D normalize(Vector2D v) {
        return VectorMath.normalize(v);
    }

    public static Vector3D normalize(Vector3D v) {
        return VectorMath.normalize(v);
    }

    public static Vector4D normalize(Vector4D v) {
        return VectorMath.normalize(v);
    }

    public static double distance(Vector2D a, Vector2D b) {
        return VectorMath.distance(a, b);
    }

    public static double distance(Vector3D a, Vector3D b) {
        return VectorMath.distance(a, b);
    }

    public static double distance(Vector4D a, Vector4D b) {
        return VectorMath.distance(a, b);
    }

    public static Vector2D lerp(Vector2D a, Vector2D b, double t) {
        return VectorMath.lerp(a, b, t);
    }

    public static Vector3D lerp(Vector3D a, Vector3D b, double t) {
        return VectorMath.lerp(a, b, t);
    }

    public static Vector4D lerp(Vector4D a, Vector4D b, double t) {
        return VectorMath.lerp(a, b, t);
    }

    public static Vector2D reflect(Vector2D direction, Vector2D normal) {
        return VectorMath.reflect(direction, normal);
    }

    public static Vector3D reflect(Vector3D direction, Vector3D normal) {
        return VectorMath.reflect(direction, normal);
    }

    public static Vector2D project(Vector2D v, Vector2D onto) {
        return VectorMath.project(v, onto);
    }

    public static Vector3D project(Vector3D v, Vector3D onto) {
        return VectorMath.project(v, onto);
    }

    public static double angle(Vector2D a, Vector2D b) {
        return VectorMath.angle(a, b);
    }

    public static double angle(Vector3D a, Vector3D b) {
        return VectorMath.angle(a, b);
    }

    public static Vector2D min(Vector2D a, Vector2D b) {
        return VectorMath.min(a, b);
    }

    public static Vector3D min(Vector3D a, Vector3D b) {
        return VectorMath.min(a, b);
    }

    public static Vector4D min(Vector4D a, Vector4D b) {
        return VectorMath.min(a, b);
    }

    public static Vector2D max(Vector2D a, Vector2D b) {
        return VectorMath.max(a, b);
    }

    public static Vector3D max(Vector3D a, Vector3D b) {
        return VectorMath.max(a, b);
    }

    public static Vector4D max(Vector4D a, Vector4D b) {
        return VectorMath.max(a, b);
    }

    public static Vector2D clamp(Vector2D v, Vector2D min, Vector2D max) {
        return VectorMath.clamp(v, min, max);
    }

    public static Vector3D clamp(Vector3D v, Vector3D min, Vector3D max) {
        return VectorMath.clamp(v, min, max);
    }

    public static Vector4D clamp(Vector4D v, Vector4D min, Vector4D max) {
        return VectorMath.clamp(v, min, max);
    }

    public static Vector2D perpendicular(Vector2D v) {
        return VectorMath.perpendicular(v);
    }

    public static Vector3D[] batchAdd(Vector3D[] a, Vector3D[] b) {
        return VectorMath.batchAdd(a, b);
    }

    public static Vector3D[] batchAddParallel(Vector3D[] a, Vector3D[] b) {
        return ParallelOps.batchAddParallel(a, b);
    }
}

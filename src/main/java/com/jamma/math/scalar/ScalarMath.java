package com.jamma.math.scalar;

public final class ScalarMath {

    private ScalarMath() {
    }

    public static final double PI = Math.PI;
    public static final double E = Math.E;
    public static final double TAU = 2.0 * Math.PI;

    private static final double DEG_TO_RAD = PI / 180.0;
    private static final double RAD_TO_DEG = 180.0 / PI;
    private static final double LOG2_E = Math.log(2.0);

    public static double sin(double x) {
        return Math.sin(x);
    }

    public static double cos(double x) {
        return Math.cos(x);
    }

    public static double tan(double x) {
        return Math.tan(x);
    }

    public static double asin(double x) {
        return Math.asin(x);
    }

    public static double acos(double x) {
        return Math.acos(x);
    }

    public static double atan(double x) {
        return Math.atan(x);
    }

    public static double atan2(double y, double x) {
        return Math.atan2(y, x);
    }

    public static double sinh(double x) {
        return Math.sinh(x);
    }

    public static double cosh(double x) {
        return Math.cosh(x);
    }

    public static double tanh(double x) {
        return Math.tanh(x);
    }

    public static double sqrt(double x) {
        return Math.sqrt(x);
    }

    public static double invSqrt(double x) {
        return 1.0 / Math.sqrt(x);
    }

    public static double cbrt(double x) {
        return Math.cbrt(x);
    }

    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    public static double exp(double x) {
        return Math.exp(x);
    }

    public static double log(double x) {
        return Math.log(x);
    }

    public static double log10(double x) {
        return Math.log10(x);
    }

    public static double log2(double x) {
        return Math.log(x) / LOG2_E;
    }

    public static double abs(double x) {
        return Math.abs(x);
    }

    public static float abs(float x) {
        return Math.abs(x);
    }

    public static int abs(int x) {
        return Math.abs(x);
    }

    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double saturate(double value) {
        return clamp(value, 0.0, 1.0);
    }

    public static double floor(double x) {
        return Math.floor(x);
    }

    public static double ceil(double x) {
        return Math.ceil(x);
    }

    public static double round(double x) {
        return Math.round(x);
    }

    public static double lerp(double a, double b, double t) {
        return Math.fma(t, b - a, a);
    }

    public static double inverseLerp(double a, double b, double value) {
        return (value - a) / (b - a);
    }

    public static double smoothstep(double edge0, double edge1, double x) {
        double t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }

    public static double signum(double x) {
        return Math.signum(x);
    }

    public static double fma(double a, double b, double c) {
        return Math.fma(a, b, c);
    }

    public static double toRadians(double degrees) {
        return degrees * DEG_TO_RAD;
    }

    public static double toDegrees(double radians) {
        return radians * RAD_TO_DEG;
    }
}

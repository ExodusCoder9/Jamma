package com.jamma.math;

public final class Interpolationd {

    private Interpolationd() {}

    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static double smoothStep(double t) {
        return t * t * (3.0 - 2.0 * t);
    }

    public static double smootherStep(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }

    public static double easeInQuad(double t) {
        return t * t;
    }

    public static double easeInCubic(double t) {
        return t * t * t;
    }

    public static double easeInQuart(double t) {
        return t * t * t * t;
    }

    public static double easeInQuint(double t) {
        return t * t * t * t * t;
    }

    public static double easeInSine(double t) {
        return 1.0 - Math.cos(t * Math.PI / 2.0);
    }

    public static double easeInExpo(double t) {
        return Math.pow(2.0, 10.0 * (t - 1.0));
    }

    public static double easeInCirc(double t) {
        return 1.0 - Math.sqrt(1.0 - t * t);
    }

    public static double easeInBack(double t) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return c3 * t * t * t - c1 * t * t;
    }

    public static double easeOutQuad(double t) {
        return 1.0 - (1.0 - t) * (1.0 - t);
    }

    public static double easeOutCubic(double t) {
        return 1.0 - (1.0 - t) * (1.0 - t) * (1.0 - t);
    }

    public static double easeOutQuart(double t) {
        return 1.0 - Math.pow(1.0 - t, 4);
    }

    public static double easeOutQuint(double t) {
        return 1.0 - Math.pow(1.0 - t, 5);
    }

    public static double easeOutSine(double t) {
        return Math.sin(t * Math.PI / 2.0);
    }

    public static double easeOutExpo(double t) {
        return 1.0 - Math.pow(2.0, -10.0 * t);
    }

    public static double easeOutCirc(double t) {
        return Math.sqrt(1.0 - (t - 1.0) * (t - 1.0));
    }

    public static double easeOutBack(double t) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return 1.0 + c3 * Math.pow(t - 1.0, 3) + c1 * Math.pow(t - 1.0, 2);
    }

    public static double easeInOutQuad(double t) {
        return t < 0.5 ? 2.0 * t * t : 1.0 - Math.pow(-2.0 * t + 2.0, 2) / 2.0;
    }

    public static double easeInOutCubic(double t) {
        return t < 0.5 ? 4.0 * t * t * t : 1.0 - Math.pow(-2.0 * t + 2.0, 3) / 2.0;
    }

    public static double easeInOutQuart(double t) {
        return t < 0.5 ? 8.0 * t * t * t * t : 1.0 - Math.pow(-2.0 * t + 2.0, 4) / 2.0;
    }

    public static double easeInOutQuint(double t) {
        return t < 0.5 ? 16.0 * t * t * t * t * t : 1.0 - Math.pow(-2.0 * t + 2.0, 5) / 2.0;
    }

    public static double easeInOutSine(double t) {
        return -Math.cos(Math.PI * t) / 2.0 + 0.5;
    }

    public static double easeInOutExpo(double t) {
        return t == 0.0 ? 0.0 : t == 1.0 ? 1.0
            : t < 0.5 ? Math.pow(2.0, 20.0 * t - 10.0) / 2.0
            : (2.0 - Math.pow(2.0, -20.0 * t + 10.0)) / 2.0;
    }

    public static double easeInOutCirc(double t) {
        return t < 0.5
            ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * t, 2))) / 2.0
            : (Math.sqrt(1.0 - Math.pow(-2.0 * t + 2.0, 2)) + 1.0) / 2.0;
    }

    public static double easeInOutBack(double t) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return t < 0.5
            ? (Math.pow(2.0 * t, 2) * ((c2 + 1.0) * 2.0 * t - c2)) / 2.0
            : (Math.pow(2.0 * t - 2.0, 2) * ((c2 + 1.0) * (t * 2.0 - 2.0) + c2) + 2.0) / 2.0;
    }

    public static double catmullRom(double p0, double p1, double p2, double p3, double t) {
        return 0.5 * ((2.0 * p1) + (-p0 + p2) * t + (2.0 * p0 - 5.0 * p1 + 4.0 * p2 - p3) * t * t + (-p0 + 3.0 * p1 - 3.0 * p2 + p3) * t * t * t);
    }

    public static double bezierCubic(double p0, double p1, double p2, double p3, double t) {
        double u = 1.0 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;
        return uuu * p0 + 3.0 * uu * t * p1 + 3.0 * u * tt * p2 + ttt * p3;
    }

    public static double bezierQuadratic(double p0, double p1, double p2, double t) {
        double u = 1.0 - t;
        return u * u * p0 + 2.0 * u * t * p1 + t * t * p2;
    }

    public static double hermite(double p0, double p1, double p2, double p3, double t, double tension, double bias) {
        double t2 = t * t;
        double t3 = t2 * t;
        double h1 = 2.0 * t3 - 3.0 * t2 + 1.0;
        double h2 = -2.0 * t3 + 3.0 * t2;
        double h3 = t3 - 2.0 * t2 + t;
        double h4 = t3 - t2;
        double d1 = (p1 - p0) * (1.0 + tension) * (1.0 - bias);
        double d2 = (p2 - p1) * (1.0 + tension) * (1.0 + bias);
        double d3 = (p3 - p2) * (1.0 + tension) * (1.0 + bias);
        double d4 = (p2 - p1) * (1.0 + tension) * (1.0 - bias);
        return h1 * p1 + h2 * p2 + h3 * d1 + h4 * d2;
    }

    public static double map(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static double map(double value, double srcMin, double srcMax, double dstMin, double dstMax) {
        return dstMin + (value - srcMin) / (srcMax - srcMin) * (dstMax - dstMin);
    }

    public static double saturate(double value) {
        return value < 0.0 ? 0.0 : value > 1.0 ? 1.0 : value;
    }

    public static double bicubic(double[][] grid, double u, double v) {
        double[] col = new double[4];
        for (int i = 0; i < 4; i++) {
            col[i] = catmullRom(grid[i][0], grid[i][1], grid[i][2], grid[i][3], u);
        }
        return catmullRom(col[0], col[1], col[2], col[3], v);
    }

    public static double bilinear(double[][] grid, double u, double v) {
        double top = grid[0][0] * (1 - u) + grid[0][1] * u;
        double bottom = grid[1][0] * (1 - u) + grid[1][1] * u;
        return top * (1 - v) + bottom * v;
    }
}

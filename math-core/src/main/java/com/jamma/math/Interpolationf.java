package com.jamma.math;

public final class Interpolationf {

    private Interpolationf() {}

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float smoothStep(float t) {
        return t * t * (3.0f - 2.0f * t);
    }

    public static float smootherStep(float t) {
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }

    public static float easeInQuad(float t) {
        return t * t;
    }

    public static float easeInCubic(float t) {
        return t * t * t;
    }

    public static float easeInQuart(float t) {
        return t * t * t * t;
    }

    public static float easeInQuint(float t) {
        return t * t * t * t * t;
    }

    public static float easeInSine(float t) {
        return 1.0f - (float) Math.cos(t * Math.PI / 2.0);
    }

    public static float easeInExpo(float t) {
        return (float) Math.pow(2.0, 10.0 * (t - 1.0));
    }

    public static float easeInCirc(float t) {
        return 1.0f - (float) Math.sqrt(1.0 - t * t);
    }

    public static float easeInBack(float t) {
        float c1 = 1.70158f;
        float c3 = c1 + 1.0f;
        return c3 * t * t * t - c1 * t * t;
    }

    public static float easeOutQuad(float t) {
        return 1.0f - (1.0f - t) * (1.0f - t);
    }

    public static float easeOutCubic(float t) {
        return 1.0f - (1.0f - t) * (1.0f - t) * (1.0f - t);
    }

    public static float easeOutQuart(float t) {
        return 1.0f - (float) Math.pow(1.0f - t, 4);
    }

    public static float easeOutQuint(float t) {
        return 1.0f - (float) Math.pow(1.0f - t, 5);
    }

    public static float easeOutSine(float t) {
        return (float) Math.sin(t * Math.PI / 2.0);
    }

    public static float easeOutExpo(float t) {
        return 1.0f - (float) Math.pow(2.0, -10.0 * t);
    }

    public static float easeOutCirc(float t) {
        return (float) Math.sqrt(1.0 - (t - 1.0f) * (t - 1.0f));
    }

    public static float easeOutBack(float t) {
        float c1 = 1.70158f;
        float c3 = c1 + 1.0f;
        return 1.0f + c3 * (float) Math.pow(t - 1.0f, 3) + c1 * (float) Math.pow(t - 1.0f, 2);
    }

    public static float easeInOutQuad(float t) {
        return t < 0.5f ? 2.0f * t * t : 1.0f - (float) Math.pow(-2.0f * t + 2.0f, 2) / 2.0f;
    }

    public static float easeInOutCubic(float t) {
        return t < 0.5f ? 4.0f * t * t * t : 1.0f - (float) Math.pow(-2.0f * t + 2.0f, 3) / 2.0f;
    }

    public static float easeInOutQuart(float t) {
        return t < 0.5f ? 8.0f * t * t * t * t : 1.0f - (float) Math.pow(-2.0f * t + 2.0f, 4) / 2.0f;
    }

    public static float easeInOutQuint(float t) {
        return t < 0.5f ? 16.0f * t * t * t * t * t : 1.0f - (float) Math.pow(-2.0f * t + 2.0f, 5) / 2.0f;
    }

    public static float easeInOutSine(float t) {
        return -(float) Math.cos(Math.PI * t) / 2.0f + 0.5f;
    }

    public static float easeInOutExpo(float t) {
        return t == 0.0f ? 0.0f : t == 1.0f ? 1.0f
            : t < 0.5f ? (float) Math.pow(2.0, 20.0 * t - 10.0) / 2.0f
            : (2.0f - (float) Math.pow(2.0, -20.0 * t + 10.0)) / 2.0f;
    }

    public static float easeInOutCirc(float t) {
        return t < 0.5f
            ? (1.0f - (float) Math.sqrt(1.0 - Math.pow(2.0f * t, 2))) / 2.0f
            : ((float) Math.sqrt(1.0 - Math.pow(-2.0f * t + 2.0f, 2)) + 1.0f) / 2.0f;
    }

    public static float easeInOutBack(float t) {
        float c1 = 1.70158f;
        float c2 = c1 * 1.525f;
        return t < 0.5f
            ? (float) (Math.pow(2.0f * t, 2) * ((c2 + 1.0f) * 2.0f * t - c2)) / 2.0f
            : (float) (Math.pow(2.0f * t - 2.0f, 2) * ((c2 + 1.0f) * (t * 2.0f - 2.0f) + c2) + 2.0f) / 2.0f;
    }

    public static float catmullRom(float p0, float p1, float p2, float p3, float t) {
        return 0.5f * ((2.0f * p1) + (-p0 + p2) * t + (2.0f * p0 - 5.0f * p1 + 4.0f * p2 - p3) * t * t + (-p0 + 3.0f * p1 - 3.0f * p2 + p3) * t * t * t);
    }

    public static float bezierCubic(float p0, float p1, float p2, float p3, float t) {
        float u = 1.0f - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;
        return uuu * p0 + 3.0f * uu * t * p1 + 3.0f * u * tt * p2 + ttt * p3;
    }

    public static float bezierQuadratic(float p0, float p1, float p2, float t) {
        float u = 1.0f - t;
        return u * u * p0 + 2.0f * u * t * p1 + t * t * p2;
    }

    public static float hermite(float p0, float p1, float p2, float p3, float t, float tension, float bias) {
        float t2 = t * t;
        float t3 = t2 * t;
        float h1 = 2.0f * t3 - 3.0f * t2 + 1.0f;
        float h2 = -2.0f * t3 + 3.0f * t2;
        float h3 = t3 - 2.0f * t2 + t;
        float h4 = t3 - t2;
        float d1 = (p1 - p0) * (1.0f + tension) * (1.0f - bias);
        float d2 = (p2 - p1) * (1.0f + tension) * (1.0f + bias);
        float d3 = (p3 - p2) * (1.0f + tension) * (1.0f + bias);
        float d4 = (p2 - p1) * (1.0f + tension) * (1.0f - bias);
        return h1 * p1 + h2 * p2 + h3 * d1 + h4 * d2;
    }

    public static float map(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    public static float map(float value, float srcMin, float srcMax, float dstMin, float dstMax) {
        return dstMin + (value - srcMin) / (srcMax - srcMin) * (dstMax - dstMin);
    }

    public static float saturate(float value) {
        return value < 0.0f ? 0.0f : value > 1.0f ? 1.0f : value;
    }

    public static float bicubic(float[][] grid, float u, float v) {
        float[] col = new float[4];
        for (int i = 0; i < 4; i++) {
            col[i] = catmullRom(grid[i][0], grid[i][1], grid[i][2], grid[i][3], u);
        }
        return catmullRom(col[0], col[1], col[2], col[3], v);
    }

    public static float bilinear(float[][] grid, float u, float v) {
        float top = grid[0][0] * (1 - u) + grid[0][1] * u;
        float bottom = grid[1][0] * (1 - u) + grid[1][1] * u;
        return top * (1 - v) + bottom * v;
    }
}

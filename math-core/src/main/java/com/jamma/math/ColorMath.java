package com.jamma.math;

import java.util.Objects;

public final class ColorMath {

    private ColorMath() {}

    public static float srgbToLinear(float value) {
        if (value <= 0.04045f) {
            return value / 12.92f;
        }
        return (float) Math.pow((value + 0.055f) / 1.055f, 2.4);
    }

    public static float linearToSrgb(float value) {
        if (value <= 0.0031308f) {
            return 12.92f * value;
        }
        return 1.055f * (float) Math.pow(value, 1.0f / 2.4f) - 0.055f;
    }

    public static Vector3f srgbToLinear(float r, float g, float b) {
        return new Vector3f(srgbToLinear(r), srgbToLinear(g), srgbToLinear(b));
    }

    public static Vector3f linearToSrgb(float r, float g, float b) {
        return new Vector3f(linearToSrgb(r), linearToSrgb(g), linearToSrgb(b));
    }

    public static int packRgb(float r, float g, float b) {
        int ri = Math.round(Math.clamp(r, 0.0f, 1.0f) * 255.0f);
        int gi = Math.round(Math.clamp(g, 0.0f, 1.0f) * 255.0f);
        int bi = Math.round(Math.clamp(b, 0.0f, 1.0f) * 255.0f);
        return (ri << 16) | (gi << 8) | bi;
    }

    public static int packRgba(float r, float g, float b, float a) {
        int ri = Math.round(Math.clamp(r, 0.0f, 1.0f) * 255.0f);
        int gi = Math.round(Math.clamp(g, 0.0f, 1.0f) * 255.0f);
        int bi = Math.round(Math.clamp(b, 0.0f, 1.0f) * 255.0f);
        int ai = Math.round(Math.clamp(a, 0.0f, 1.0f) * 255.0f);
        return (ai << 24) | (ri << 16) | (gi << 8) | bi;
    }

    public static void unpackRgb(int rgb, Vector3f dest) {
        Objects.requireNonNull(dest);
        float r = ((rgb >> 16) & 0xFF) / 255.0f;
        float g = ((rgb >> 8) & 0xFF) / 255.0f;
        float b = (rgb & 0xFF) / 255.0f;
        dest = new Vector3f(r, g, 0.0f);
    }

    public static void unpackRgba(int rgba, Vector3f dest) {
        Objects.requireNonNull(dest);
        float r = ((rgba >> 16) & 0xFF) / 255.0f;
        float g = ((rgba >> 8) & 0xFF) / 255.0f;
        float b = (rgba & 0xFF) / 255.0f;
        dest = new Vector3f(r, g, b);
    }

    public static float[] unpackRgb(int rgb) {
        float r = ((rgb >> 16) & 0xFF) / 255.0f;
        float g = ((rgb >> 8) & 0xFF) / 255.0f;
        float b = (rgb & 0xFF) / 255.0f;
        return new float[]{r, g, b};
    }

    public static float[] unpackRgba(int rgba) {
        float a = ((rgba >> 24) & 0xFF) / 255.0f;
        float r = ((rgba >> 16) & 0xFF) / 255.0f;
        float g = ((rgba >> 8) & 0xFF) / 255.0f;
        float b = (rgba & 0xFF) / 255.0f;
        return new float[]{r, g, b, a};
    }

    public static Vector3f hsvToRgb(float h, float s, float v) {
        float c = v * s;
        float hp = h / 60.0f;
        float x = c * (1.0f - Math.abs(hp % 2.0f - 1.0f));
        float m = v - c;
        float r, g, b;
        int hi = (int) hp % 6;
        if (hi < 0) hi += 6;
        switch (hi) {
            case 0 -> { r = c; g = x; b = 0; }
            case 1 -> { r = x; g = c; b = 0; }
            case 2 -> { r = 0; g = c; b = x; }
            case 3 -> { r = 0; g = x; b = c; }
            case 4 -> { r = x; g = 0; b = c; }
            case 5 -> { r = c; g = 0; b = x; }
            default -> { r = 0; g = 0; b = 0; }
        }
        return new Vector3f(r + m, g + m, b + m);
    }

    public static void rgbToHsv(float r, float g, float b, Vector3f dest) {
        Objects.requireNonNull(dest);
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float delta = max - min;
        float h = 0.0f;
        float s = (max > 0.0f) ? delta / max : 0.0f;
        if (delta > 0.0f) {
            if (max == r) {
                h = ((g - b) / delta) % 6.0f;
            } else if (max == g) {
                h = (b - r) / delta + 2.0f;
            } else {
                h = (r - g) / delta + 4.0f;
            }
            h *= 60.0f;
            if (h < 0.0f) h += 360.0f;
        }
        dest = new Vector3f(h, s, max);
    }

    public static Vector3f colorTemperature(float kelvin) {
        float k = kelvin / 100.0f;
        float r, g, b;
        if (k <= 66.0f) {
            r = 1.0f;
        } else {
            r = (float) Math.clamp(1.2929362 * Math.pow(k - 60.0, -0.13320476), 0.0, 1.0);
        }
        if (k <= 66.0f) {
            g = (float) Math.clamp(0.39008158 * Math.log(k) - 0.63184144, 0.0, 1.0);
        } else {
            g = (float) Math.clamp(1.1298909 * Math.pow(k - 60.0, -0.07551485), 0.0, 1.0);
        }
        if (k >= 66.0f) {
            b = 1.0f;
        } else if (k <= 19.0f) {
            b = 0.0f;
        } else {
            b = (float) Math.clamp(0.54320679 * Math.log(k - 10.0) - 1.1962541, 0.0, 1.0);
        }
        return new Vector3f(r, g, b);
    }
}

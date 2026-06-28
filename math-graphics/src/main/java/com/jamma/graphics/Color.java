package com.jamma.graphics;

import com.jamma.math.ColorMath;
import com.jamma.math.Vector3f;

import java.util.Locale;

public record Color(float r, float g, float b, float a) {

    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    public static final Color BLACK       = new Color(0, 0, 0, 1);
    public static final Color WHITE       = new Color(1, 1, 1, 1);
    public static final Color RED         = new Color(1, 0, 0, 1);
    public static final Color GREEN       = new Color(0, 1, 0, 1);
    public static final Color BLUE        = new Color(0, 0, 1, 1);
    public static final Color YELLOW      = new Color(1, 1, 0, 1);
    public static final Color CYAN        = new Color(0, 1, 1, 1);
    public static final Color MAGENTA     = new Color(1, 0, 1, 1);
    public static final Color ORANGE      = new Color(1, 0.647f, 0, 1);
    public static final Color PINK        = new Color(1, 0.412f, 0.706f, 1);
    public static final Color GRAY        = new Color(0.5f, 0.5f, 0.5f, 1);
    public static final Color DARK_GRAY   = new Color(0.25f, 0.25f, 0.25f, 1);
    public static final Color LIGHT_GRAY  = new Color(0.75f, 0.75f, 0.75f, 1);

    public Color(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public Color(int rgba) {
        this(
            ((rgba >> 16) & 0xFF) / 255.0f,
            ((rgba >> 8) & 0xFF) / 255.0f,
            (rgba & 0xFF) / 255.0f,
            ((rgba >> 24) & 0xFF) / 255.0f
        );
    }

    public int toRgba() {
        int ri = Math.round(Math.clamp(r, 0, 1) * 255);
        int gi = Math.round(Math.clamp(g, 0, 1) * 255);
        int bi = Math.round(Math.clamp(b, 0, 1) * 255);
        int ai = Math.round(Math.clamp(a, 0, 1) * 255);
        return (ai << 24) | (ri << 16) | (gi << 8) | bi;
    }

    public int toRgb() {
        int ri = Math.round(Math.clamp(r, 0, 1) * 255);
        int gi = Math.round(Math.clamp(g, 0, 1) * 255);
        int bi = Math.round(Math.clamp(b, 0, 1) * 255);
        return (ri << 16) | (gi << 8) | bi;
    }

    public Vector3f toRgbVector() {
        return new Vector3f(r, g, b);
    }

    public Color toLinear() {
        return new Color(
            ColorMath.srgbToLinear(r),
            ColorMath.srgbToLinear(g),
            ColorMath.srgbToLinear(b),
            a
        );
    }

    public Color toSrgb() {
        return new Color(
            ColorMath.linearToSrgb(r),
            ColorMath.linearToSrgb(g),
            ColorMath.linearToSrgb(b),
            a
        );
    }

    public float[] hsv() {
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float delta = max - min;
        float h = 0;
        float s = max > 0 ? delta / max : 0;
        if (delta > 0) {
            if (max == r) h = ((g - b) / delta) % 6;
            else if (max == g) h = (b - r) / delta + 2;
            else h = (r - g) / delta + 4;
            h *= 60;
            if (h < 0) h += 360;
        }
        return new float[]{h, s, max};
    }

    public static Color fromHsv(float h, float s, float v) {
        var rgb = ColorMath.hsvToRgb(h, s, v);
        return new Color(rgb.x(), rgb.y(), rgb.z(), 1);
    }

    public Color withAlpha(float alpha) {
        return new Color(r, g, b, alpha);
    }

    public Color blend(Color other, float t) {
        return new Color(
            r + (other.r - r) * t,
            g + (other.g - g) * t,
            b + (other.b - b) * t,
            a + (other.a - a) * t
        );
    }

    public Color multiply(Color other) {
        return new Color(r * other.r, g * other.g, b * other.b, a * other.a);
    }

    public String toHex() {
        return String.format(Locale.US, "#%02x%02x%02x%02x",
            Math.round(Math.clamp(r, 0, 1) * 255),
            Math.round(Math.clamp(g, 0, 1) * 255),
            Math.round(Math.clamp(b, 0, 1) * 255),
            Math.round(Math.clamp(a, 0, 1) * 255));
    }

    public static Color fromHex(String hex) {
        String h = hex.startsWith("#") ? hex.substring(1) : hex;
        int len = h.length();
        if (len == 3) {
            int ri = Integer.parseInt(h.substring(0, 1), 16) * 17;
            int gi = Integer.parseInt(h.substring(1, 2), 16) * 17;
            int bi = Integer.parseInt(h.substring(2, 3), 16) * 17;
            return new Color(ri / 255.0f, gi / 255.0f, bi / 255.0f, 1);
        }
        if (len == 4) {
            int ri = Integer.parseInt(h.substring(0, 1), 16) * 17;
            int gi = Integer.parseInt(h.substring(1, 2), 16) * 17;
            int bi = Integer.parseInt(h.substring(2, 3), 16) * 17;
            int ai = Integer.parseInt(h.substring(3, 4), 16) * 17;
            return new Color(ri / 255.0f, gi / 255.0f, bi / 255.0f, ai / 255.0f);
        }
        if (len == 6) {
            int ri = Integer.parseInt(h.substring(0, 2), 16);
            int gi = Integer.parseInt(h.substring(2, 4), 16);
            int bi = Integer.parseInt(h.substring(4, 6), 16);
            return new Color(ri / 255.0f, gi / 255.0f, bi / 255.0f, 1);
        }
        int ri = Integer.parseInt(h.substring(0, 2), 16);
        int gi = Integer.parseInt(h.substring(2, 4), 16);
        int bi = Integer.parseInt(h.substring(4, 6), 16);
        int ai = Integer.parseInt(h.substring(6, 8), 16);
        return new Color(ri / 255.0f, gi / 255.0f, bi / 255.0f, ai / 255.0f);
    }

    public static Color fromTemperature(float kelvin) {
        var rgb = ColorMath.colorTemperature(kelvin);
        return new Color(rgb.x(), rgb.y(), rgb.z(), 1);
    }


}

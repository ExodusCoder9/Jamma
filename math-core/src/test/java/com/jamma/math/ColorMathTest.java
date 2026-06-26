package com.jamma.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.jamma.math.Vector3f;

class ColorMathTest {
    static final float F = 1e-5f;

    @Test void srgbToLinearZero() {
        assertEquals(0.0f, ColorMath.srgbToLinear(0.0f), F);
    }

    @Test void srgbToLinearOne() {
        assertEquals(1.0f, ColorMath.srgbToLinear(1.0f), F);
    }

    @Test void srgbLinearRoundtrip() {
        float linear = ColorMath.srgbToLinear(0.5f);
        assertEquals(0.5f, ColorMath.linearToSrgb(linear), F);
    }

    @Test void packRgbRed() {
        assertEquals(0x00FF0000, ColorMath.packRgb(1.0f, 0.0f, 0.0f));
    }

    @Test void packRgbaGreen() {
        assertEquals(0xFF00FF00, ColorMath.packRgba(0.0f, 1.0f, 0.0f, 1.0f));
    }

    @Test void unpackRgbRed() {
        Vector3f c = ColorMath.unpackRgb(0x00FF0000);
        assertEquals(1.0f, c.x(), F);
        assertEquals(0.0f, c.y(), F);
        assertEquals(0.0f, c.z(), F);
    }

    @Test void hsvToRgbWhite() {
        Vector3f c = ColorMath.hsvToRgb(0, 0, 1);
        assertEquals(1.0f, c.x(), F);
        assertEquals(1.0f, c.y(), F);
        assertEquals(1.0f, c.z(), F);
    }

    @Test void colorTemperature() {
        Vector3f c = ColorMath.colorTemperature(6500);
        assertTrue(c.x() > 0);
        assertTrue(c.y() > 0);
        assertTrue(c.z() > 0);
    }
}

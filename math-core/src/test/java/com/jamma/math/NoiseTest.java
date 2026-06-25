package com.jamma.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NoiseTest {
    static final double EPS = 1e-5;

    @Test void simplex2D() {
        assertEquals(0.0, SimplexNoise.noise(0.0, 0.0), EPS);
    }

    @Test void simplex3D() {
        assertEquals(0.0, SimplexNoise.noise(0.0, 0.0, 0.0), EPS);
    }

    @Test void simplexSeeded() {
        double a = SimplexNoise.noise(1.5, 2.3, 42);
        double b = SimplexNoise.noise(1.5, 2.3, 99);
        assertNotEquals(a, b);
    }

    @Test void simplexFloat2D() {
        assertEquals(SimplexNoise.noise(1.5, 2.3), SimplexNoise.noise(1.5f, 2.3f), EPS);
    }

    @Test void simplexFloat3D() {
        assertEquals(SimplexNoise.noise(1.5, 2.3, 0.7), SimplexNoise.noise(1.5f, 2.3f, 0.7f), EPS);
    }

    @Test void perlin3D() {
        assertEquals(0.0, PerlinNoise.noise(0.0, 0.0, 0.0), EPS);
    }

    @Test void perlinFbm() {
        assertNotEquals(0.0, PerlinNoise.fBm(1.5, 2.3, 3, 2.0, 0.5), EPS);
    }

    @Test void simplex2DRange() {
        for (double x = -5; x <= 5; x++) {
            for (double y = -5; y <= 5; y++) {
                double n = SimplexNoise.noise(x, y);
                assertTrue(n >= -1.0 && n <= 1.0, "n=" + n);
            }
        }
    }

    @Test void simplex3DRange() {
        for (double x = -5; x <= 5; x++) {
            for (double y = -5; y <= 5; y++) {
                double n = SimplexNoise.noise(x, y, 0.5);
                assertTrue(n >= -1.0 && n <= 1.0, "n=" + n);
            }
        }
    }

    @Test void perlin2DRange() {
        for (double x = -5; x <= 5; x++) {
            for (double y = -5; y <= 5; y++) {
                double n = PerlinNoise.noise(x, y);
                assertTrue(n >= -1.0 && n <= 1.0, "n=" + n);
            }
        }
    }
}

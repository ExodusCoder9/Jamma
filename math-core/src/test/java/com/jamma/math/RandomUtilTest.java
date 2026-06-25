package com.jamma.math;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomUtilTest {
    static final float F = 1e-5f;
    final RandomGenerator rng = RandomGenerator.getDefault();

    @Test void nextFloatInRange() {
        for (int i = 0; i < 100; i++) {
            float v = RandomUtil.nextFloat(rng, 0, 10);
            assertTrue(v >= 0 && v < 10, "v=" + v);
        }
    }

    @Test void nextDoubleInRange() {
        for (int i = 0; i < 100; i++) {
            double v = RandomUtil.nextDouble(rng, -5, 5);
            assertTrue(v >= -5 && v < 5, "v=" + v);
        }
    }

    @Test void randomPointInSphere() {
        for (int i = 0; i < 100; i++) {
            Vector3f p = RandomUtil.randomPointInSphere(rng, 5);
            assertTrue(p.length() <= 5.0f + F, "len=" + p.length());
        }
    }

    @Test void randomPointOnSphere() {
        for (int i = 0; i < 100; i++) {
            Vector3f p = RandomUtil.randomPointOnSphere(rng, 5);
            assertEquals(5.0f, p.length(), 1e-4f);
        }
    }

    @Test void randomVector2fRange() {
        for (int i = 0; i < 100; i++) {
            Vector2f v = RandomUtil.randomVector2f(rng, -1, 1);
            assertTrue(v.x() >= -1 && v.x() < 1, "x=" + v.x());
            assertTrue(v.y() >= -1 && v.y() < 1, "y=" + v.y());
        }
    }

    @Test void randomVector3fRange() {
        for (int i = 0; i < 100; i++) {
            Vector3f v = RandomUtil.randomVector3f(rng, -1, 1);
            assertTrue(v.x() >= -1 && v.x() < 1, "x=" + v.x());
            assertTrue(v.y() >= -1 && v.y() < 1, "y=" + v.y());
            assertTrue(v.z() >= -1 && v.z() < 1, "z=" + v.z());
        }
    }
}

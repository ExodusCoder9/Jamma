package com.jamma.math;

import java.util.random.RandomGenerator;

public final class RandomUtil {

    private RandomUtil() {}

    public static float nextFloat(RandomGenerator rng, float min, float max) {
        return rng.nextFloat(min, max);
    }

    public static double nextDouble(RandomGenerator rng, double min, double max) {
        return rng.nextDouble(min, max);
    }

    public static Vector3f randomPointInSphere(RandomGenerator rng, float radius) {
        double u = rng.nextDouble();
        double v = rng.nextDouble();
        double w = rng.nextDouble();
        double theta = 2.0 * Math.PI * u;
        double phi = Math.acos(2.0 * v - 1.0);
        double r = radius * Math.cbrt(w);
        double sinPhi = Math.sin(phi);
        return new Vector3f(
            (float) (r * sinPhi * Math.cos(theta)),
            (float) (r * sinPhi * Math.sin(theta)),
            (float) (r * Math.cos(phi))
        );
    }

    public static Vector3f randomPointOnSphere(RandomGenerator rng, float radius) {
        double u = rng.nextDouble();
        double v = rng.nextDouble();
        double theta = 2.0 * Math.PI * u;
        double phi = Math.acos(2.0 * v - 1.0);
        double sinPhi = Math.sin(phi);
        return new Vector3f(
            (float) (radius * sinPhi * Math.cos(theta)),
            (float) (radius * sinPhi * Math.sin(theta)),
            (float) (radius * Math.cos(phi))
        );
    }

    public static Vector3f randomPointInCircle(RandomGenerator rng, float radius) {
        double u = rng.nextDouble();
        double theta = 2.0 * Math.PI * rng.nextDouble();
        double r = radius * Math.sqrt(u);
        return new Vector3f(
            (float) (r * Math.cos(theta)),
            (float) (r * Math.sin(theta)),
            0.0f
        );
    }

    public static Vector2f randomVector2f(RandomGenerator rng, float min, float max) {
        return new Vector2f(rng.nextFloat(min, max), rng.nextFloat(min, max));
    }

    public static Vector3f randomVector3f(RandomGenerator rng, float min, float max) {
        return new Vector3f(rng.nextFloat(min, max), rng.nextFloat(min, max), rng.nextFloat(min, max));
    }
}

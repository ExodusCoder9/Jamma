package com.jamma.math;

import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled // Exclude from standard unit tests, run explicitly for profiling
public class PerformanceBenchmarkTest {

    private static final int WARMUP_RUNS = 200_000;
    private static final int TEST_RUNS = 1_000_000;
    private static volatile double sinkD;
    private static volatile float sinkF;
    private static volatile int sinkI;

    @Test
    public void runLibraryBenchmarks() {
        System.out.println("\n==================================================================");
        System.out.println("                      JAMMA MICROBENCHMARKS                       ");
        System.out.println("==================================================================");
        System.out.printf("| %-28s | %-12s | %-12s |%n", "Operation (1M runs)", "Jamma (ms)", "JOML (ms)");
        System.out.println("|------------------------------|--------------|--------------|");

        benchmarkMatrix4fMultiply();
        benchmarkVector3fAddition();
        benchmarkVector3fNormalize();
        benchmarkQuaternionfSlerp();
        benchmarkFrustumCull();
        System.out.println("==================================================================\n");
    }

    private void benchmarkMatrix4fMultiply() {
        final Matrix4f[] jm1 = { new Matrix4f().identity() };
        final Matrix4f jm2 = new Matrix4f().identity().scale(2.0f, 3.0f, 4.0f);

        final org.joml.Matrix4f jomlM1 = new org.joml.Matrix4f().identity();
        org.joml.Matrix4f jomlM2 = new org.joml.Matrix4f().identity().scale(2.0f, 3.0f, 4.0f);

        for (int i = 0; i < WARMUP_RUNS; i++) {
            jm1[0].multiply(jm2);
            jomlM1.mul(jomlM2);
        }

        long jammaTime = time(() -> sinkF = jm1[0].multiply(jm2).m00());
        long jomlTime = time(() -> sinkF = jomlM1.mul(jomlM2).m00());

        System.out.printf("| %-28s | %-12d | %-12d |%n", "Matrix4f multiply", jammaTime, jomlTime);
    }

    private void benchmarkVector3fAddition() {
        final Vector3f[] jv1 = { new Vector3f(1.0f, 2.0f, 3.0f) };
        final Vector3f jv2 = new Vector3f(4.0f, 5.0f, 6.0f);

        final org.joml.Vector3f jomlV1 = new org.joml.Vector3f(1.0f, 2.0f, 3.0f);
        org.joml.Vector3f jomlV2 = new org.joml.Vector3f(4.0f, 5.0f, 6.0f);

        for (int i = 0; i < WARMUP_RUNS; i++) {
            jv1[0] = jv1[0].add(jv2);
            jomlV1.add(jomlV2);
        }

        long jammaTime = time(() -> sinkF = (jv1[0] = jv1[0].add(jv2)).x());
        long jomlTime = time(() -> sinkF = jomlV1.add(jomlV2).x);

        System.out.printf("| %-28s | %-12d | %-12d |%n", "Vector3f addition", jammaTime, jomlTime);
    }

    private void benchmarkVector3fNormalize() {
        final Vector3f[] jv = { new Vector3f(3.0f, 4.0f, 5.0f) };
        final org.joml.Vector3f jomlV = new org.joml.Vector3f(3.0f, 4.0f, 5.0f);

        for (int i = 0; i < WARMUP_RUNS; i++) {
            jv[0] = jv[0].normalize();
            jomlV.normalize();
        }

        long jammaTime = time(() -> sinkF = (jv[0] = jv[0].normalize()).x());
        long jomlTime = time(() -> sinkF = jomlV.normalize().x);

        System.out.printf("| %-28s | %-12d | %-12d |%n", "Vector3f normalize", jammaTime, jomlTime);
    }

    private void benchmarkQuaternionfSlerp() {
        final Quaternionf[] jq1 = { Quaternionf.identity() };
        Quaternionf jq2 = Quaternionf.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f), 1.57f);

        final org.joml.Quaternionf jomlQ1 = new org.joml.Quaternionf().identity();
        org.joml.Quaternionf jomlQ2 = new org.joml.Quaternionf().setAngleAxis(1.57f, 0.0f, 1.0f, 0.0f);

        for (int i = 0; i < WARMUP_RUNS; i++) {
            jq1[0] = jq1[0].slerp(jq2, 0.5f);
            jomlQ1.slerp(jomlQ2, 0.5f);
        }

        long jammaTime = time(() -> sinkF = (jq1[0] = jq1[0].slerp(jq2, 0.5f)).x());
        long jomlTime = time(() -> sinkF = jomlQ1.slerp(jomlQ2, 0.5f).x);

        System.out.printf("| %-28s | %-12d | %-12d |%n", "Quaternionf slerp", jammaTime, jomlTime);
    }

    private void benchmarkFrustumCull() {
        com.jamma.math.geometry.FrustumIntersection frustum = new com.jamma.math.geometry.FrustumIntersection(new Matrix4f().identity());
        com.jamma.math.geometry.AABB box = new com.jamma.math.geometry.AABB(-1, -1, -1, 1, 1, 1);

        for (int i = 0; i < WARMUP_RUNS; i++) {
            sinkI = frustum.testAABB(box);
        }

        long jammaTime = time(() -> sinkI = frustum.testAABB(box));
        System.out.printf("| %-28s | %-12d | %-12s |%n", "Frustum AABB test", jammaTime, "-");
    }

    private long time(Runnable action) {
        long start = System.nanoTime();
        for (int i = 0; i < TEST_RUNS; i++) {
            action.run();
        }
        return (System.nanoTime() - start) / 1_000_000;
    }
}

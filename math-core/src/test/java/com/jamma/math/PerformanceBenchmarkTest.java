package com.jamma.math;

import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled // Exclude from standard unit tests, run explicitly for profiling
public class PerformanceBenchmarkTest {

    private static final int WARMUP_RUNS = 200_000;
    private static final int TEST_RUNS = 1_000_000;

    @Test
    public void runJammaVsJomlBenchmarks() {
        System.out.println("\n==================================================================");
        System.out.println("                 JAMMA VS JOML MICROBENCHMARK RESULT              ");
        System.out.println("==================================================================");
        System.out.printf("| %-25s | %-12s | %-12s |%n", "Operation (1M runs)", "Jamma (ms)", "JOML (ms)");
        System.out.println("|---------------------------|--------------|--------------|");

        benchmarkMatrix4fMultiply();
        benchmarkVector3fAddition();
        benchmarkQuaternionfSlerp();
        System.out.println("==================================================================\n");
    }

    private void benchmarkMatrix4fMultiply() {
        // Jamma
        Matrix4f jm1 = new Matrix4f().identity();
        Matrix4f jm2 = new Matrix4f().identity();
        jm2.m00(2.0f).m11(3.0f).m22(4.0f);

        // JOML
        org.joml.Matrix4f jomlM1 = new org.joml.Matrix4f().identity();
        org.joml.Matrix4f jomlM2 = new org.joml.Matrix4f().identity();
        jomlM2.m00(2.0f).m11(3.0f).m22(4.0f);

        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            jm1.multiply(jm2);
            jomlM1.mul(jomlM2);
        }

        // Benchmark Jamma
        long start = System.nanoTime();
        for (int i = 0; i < TEST_RUNS; i++) {
            jm1.multiply(jm2);
        }
        long jammaTime = (System.nanoTime() - start) / 1_000_000;

        // Benchmark JOML
        start = System.nanoTime();
        for (int i = 0; i < TEST_RUNS; i++) {
            jomlM1.mul(jomlM2);
        }
        long jomlTime = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("| %-25s | %-12d | %-12d |%n", "Matrix4f Multiply", jammaTime, jomlTime);
    }

    private void benchmarkVector3fAddition() {
        // Jamma records are immutable, so we measure allocation + addition
        Vector3f jv1 = new Vector3f(1.0f, 2.0f, 3.0f);
        Vector3f jv2 = new Vector3f(4.0f, 5.0f, 6.0f);

        // JOML classes are mutable, but we measure typical allocation-free addition vs Jamma's records
        org.joml.Vector3f jomlV1 = new org.joml.Vector3f(1.0f, 2.0f, 3.0f);
        org.joml.Vector3f jomlV2 = new org.joml.Vector3f(4.0f, 5.0f, 6.0f);

        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            jv1 = new Vector3f(jv1.x() + jv2.x(), jv1.y() + jv2.y(), jv1.z() + jv2.z());
            jomlV1.add(jomlV2);
        }

        // Benchmark Jamma (Immutable record allocation)
        long start = System.nanoTime();
        for (int i = 0; i < TEST_RUNS; i++) {
            jv1 = new Vector3f(jv1.x() + jv2.x(), jv1.y() + jv2.y(), jv1.z() + jv2.z());
        }
        long jammaTime = (System.nanoTime() - start) / 1_000_000;

        // Benchmark JOML (Mutable add)
        start = System.nanoTime();
        for (int i = 0; i < TEST_RUNS; i++) {
            jomlV1.add(jomlV2);
        }
        long jomlTime = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("| %-25s | %-12d | %-12d |%n", "Vector3f Addition", jammaTime, jomlTime);
    }

    private void benchmarkQuaternionfSlerp() {
        Quaternionf jq1 = Quaternionf.identity();
        Quaternionf jq2 = Quaternionf.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f), 1.57f);

        org.joml.Quaternionf jomlQ1 = new org.joml.Quaternionf().identity();
        org.joml.Quaternionf jomlQ2 = new org.joml.Quaternionf().setAngleAxis(1.57f, 0.0f, 1.0f, 0.0f);

        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            jq1.slerp(jq2, 0.5f);
            jomlQ1.slerp(jomlQ2, 0.5f);
        }

        // Benchmark Jamma
        long start = System.nanoTime();
        for (int i = 0; i < TEST_RUNS; i++) {
            jq1 = jq1.slerp(jq2, 0.5f);
        }
        long jammaTime = (System.nanoTime() - start) / 1_000_000;

        // Benchmark JOML
        start = System.nanoTime();
        for (int i = 0; i < TEST_RUNS; i++) {
            jomlQ1.slerp(jomlQ2, 0.5f);
        }
        long jomlTime = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("| %-25s | %-12d | %-12d |%n", "Quaternionf Slerp", jammaTime, jomlTime);
    }
}

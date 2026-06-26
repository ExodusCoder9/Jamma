package com.jamma.math.incubator;

import com.jamma.math.Vector3d;
import com.jamma.math.Vector4d;
import com.jamma.math.matrix.Matrix4d;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public final class VectorApiMath {

    private static final VectorSpecies<Double> D_SPECIES = DoubleVector.SPECIES_256;
    private static final VectorSpecies<Float> F_SPECIES = FloatVector.SPECIES_128;

    private VectorApiMath() {
    }

    // ── DoubleVector (Vector4d) ──────────────────────────────────────────

    public static Vector4d add(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(D_SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.add(vb);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4d sub(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(D_SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.sub(vb);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4d mul(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(D_SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.mul(vb);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4d scale(Vector4d v, double s) {
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, new double[]{v.x(), v.y(), v.z(), v.w()}, 0);
        DoubleVector vr = va.mul(s);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static double dot(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(D_SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        return va.mul(vb).reduceLanes(VectorOperators.ADD);
    }

    public static void batchAdd(Vector4d[] results, Vector4d[] a, Vector4d[] b) {
        for (int i = 0; i < a.length; i++) {
            Vector4d va = a[i];
            Vector4d vb = b[i];
            double[] arrA = {va.x(), va.y(), va.z(), va.w()};
            double[] arrB = {vb.x(), vb.y(), vb.z(), vb.w()};
            DoubleVector vecA = DoubleVector.fromArray(D_SPECIES, arrA, 0);
            DoubleVector vecB = DoubleVector.fromArray(D_SPECIES, arrB, 0);
            DoubleVector vr = vecA.add(vecB);
            results[i] = new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
        }
    }

    public static double[] batchDot(Vector4d[] a, Vector4d[] b) {
        int n = a.length;
        double[] results = new double[n];
        for (int i = 0; i < n; i++) {
            results[i] = dot(a[i], b[i]);
        }
        return results;
    }

    public static Vector4d transform(Matrix4d m, Vector4d v) {
        double[] vecArr = {v.x(), v.y(), v.z(), v.w()};
        DoubleVector vec = DoubleVector.fromArray(D_SPECIES, vecArr, 0);

        double[] row0 = {m.m00, m.m01, m.m02, m.m03};
        double[] row1 = {m.m10, m.m11, m.m12, m.m13};
        double[] row2 = {m.m20, m.m21, m.m22, m.m23};
        double[] row3 = {m.m30, m.m31, m.m32, m.m33};

        DoubleVector r0 = DoubleVector.fromArray(D_SPECIES, row0, 0);
        DoubleVector r1 = DoubleVector.fromArray(D_SPECIES, row1, 0);
        DoubleVector r2 = DoubleVector.fromArray(D_SPECIES, row2, 0);
        DoubleVector r3 = DoubleVector.fromArray(D_SPECIES, row3, 0);

        return new Vector4d(
            r0.mul(vec).reduceLanes(VectorOperators.ADD),
            r1.mul(vec).reduceLanes(VectorOperators.ADD),
            r2.mul(vec).reduceLanes(VectorOperators.ADD),
            r3.mul(vec).reduceLanes(VectorOperators.ADD)
        );
    }

    public static Vector4d[] batchTransform(Matrix4d m, Vector4d[] v) {
        int n = v.length;
        Vector4d[] results = new Vector4d[n];
        for (int i = 0; i < n; i++) {
            results[i] = transform(m, v[i]);
        }
        return results;
    }

    // ── DoubleVector padded (Vector3d → 4-wide) ──────────────────────────

    public static Vector3d add3(Vector3d a, Vector3d b) {
        double[] arrA = {a.x(), a.y(), a.z(), 0.0};
        double[] arrB = {b.x(), b.y(), b.z(), 0.0};
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, arrA, 0);
        DoubleVector vb = DoubleVector.fromArray(D_SPECIES, arrB, 0);
        DoubleVector vr = va.add(vb);
        return new Vector3d(vr.lane(0), vr.lane(1), vr.lane(2));
    }

    public static Vector3d sub3(Vector3d a, Vector3d b) {
        double[] arrA = {a.x(), a.y(), a.z(), 0.0};
        double[] arrB = {b.x(), b.y(), b.z(), 0.0};
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, arrA, 0);
        DoubleVector vb = DoubleVector.fromArray(D_SPECIES, arrB, 0);
        DoubleVector vr = va.sub(vb);
        return new Vector3d(vr.lane(0), vr.lane(1), vr.lane(2));
    }

    public static Vector3d scale3(Vector3d v, double s) {
        double[] arr = {v.x(), v.y(), v.z(), 0.0};
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, arr, 0);
        DoubleVector vr = va.mul(s);
        return new Vector3d(vr.lane(0), vr.lane(1), vr.lane(2));
    }

    public static double dot3(Vector3d a, Vector3d b) {
        double[] arrA = {a.x(), a.y(), a.z(), 0.0};
        double[] arrB = {b.x(), b.y(), b.z(), 0.0};
        DoubleVector va = DoubleVector.fromArray(D_SPECIES, arrA, 0);
        DoubleVector vb = DoubleVector.fromArray(D_SPECIES, arrB, 0);
        return va.mul(vb).reduceLanes(VectorOperators.ADD);
    }

    // ── FloatVector (Vector4f) ───────────────────────────────────────────

    private static com.jamma.math.Vector4f toV4f(FloatVector v) {
        return new com.jamma.math.Vector4f(v.lane(0), v.lane(1), v.lane(2), v.lane(3));
    }

    private static FloatVector fromV4f(com.jamma.math.Vector4f v) {
        return FloatVector.fromArray(F_SPECIES, new float[]{v.x(), v.y(), v.z(), v.w()}, 0);
    }

    public static com.jamma.math.Vector4f add4f(com.jamma.math.Vector4f a, com.jamma.math.Vector4f b) {
        return toV4f(fromV4f(a).add(fromV4f(b)));
    }

    public static com.jamma.math.Vector4f sub4f(com.jamma.math.Vector4f a, com.jamma.math.Vector4f b) {
        return toV4f(fromV4f(a).sub(fromV4f(b)));
    }

    public static com.jamma.math.Vector4f mul4f(com.jamma.math.Vector4f a, com.jamma.math.Vector4f b) {
        return toV4f(fromV4f(a).mul(fromV4f(b)));
    }

    public static com.jamma.math.Vector4f scale4f(com.jamma.math.Vector4f v, float s) {
        return toV4f(fromV4f(v).mul(s));
    }

    public static float dot4f(com.jamma.math.Vector4f a, com.jamma.math.Vector4f b) {
        return fromV4f(a).mul(fromV4f(b)).reduceLanes(VectorOperators.ADD);
    }

    public static void batchAdd4f(com.jamma.math.Vector4f[] results, com.jamma.math.Vector4f[] a, com.jamma.math.Vector4f[] b) {
        for (int i = 0; i < a.length; i++) {
            FloatVector va = fromV4f(a[i]);
            FloatVector vb = fromV4f(b[i]);
            results[i] = toV4f(va.add(vb));
        }
    }
}

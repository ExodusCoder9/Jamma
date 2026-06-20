package com.jamma.math.incubator;

import com.jamma.math.Vector4d;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

public final class VectorApiMath {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;

    private VectorApiMath() {
    }

    public static Vector4d add(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.add(vb);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4d sub(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.sub(vb);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4d mul(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.mul(vb);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4d scale(Vector4d v, double s) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{v.x(), v.y(), v.z(), v.w()}, 0);
        DoubleVector vr = va.mul(s);
        return new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static double dot(Vector4d a, Vector4d b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        return va.mul(vb).reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
    }

    public static void batchAdd(Vector4d[] results, Vector4d[] a, Vector4d[] b) {
        double[] arrayA = new double[4];
        double[] arrayB = new double[4];
        for (int i = 0; i < a.length; i++) {
            Vector4d va = a[i];
            Vector4d vb = b[i];
            arrayA[0] = va.x(); arrayA[1] = va.y(); arrayA[2] = va.z(); arrayA[3] = va.w();
            arrayB[0] = vb.x(); arrayB[1] = vb.y(); arrayB[2] = vb.z(); arrayB[3] = vb.w();
            DoubleVector vecA = DoubleVector.fromArray(SPECIES, arrayA, 0);
            DoubleVector vecB = DoubleVector.fromArray(SPECIES, arrayB, 0);
            DoubleVector vr = vecA.add(vecB);
            results[i] = new Vector4d(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
        }
    }
}


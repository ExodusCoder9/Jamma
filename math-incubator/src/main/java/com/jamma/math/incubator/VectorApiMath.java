package com.jamma.math.incubator;

import com.jamma.math.Vector4D;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

public final class VectorApiMath {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;

    private VectorApiMath() {
    }

    public static Vector4D add(Vector4D a, Vector4D b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.add(vb);
        return new Vector4D(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4D sub(Vector4D a, Vector4D b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.sub(vb);
        return new Vector4D(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4D mul(Vector4D a, Vector4D b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        DoubleVector vr = va.mul(vb);
        return new Vector4D(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static Vector4D scale(Vector4D v, double s) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{v.x(), v.y(), v.z(), v.w()}, 0);
        DoubleVector vr = va.mul(s);
        return new Vector4D(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
    }

    public static double dot(Vector4D a, Vector4D b) {
        DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a.x(), a.y(), a.z(), a.w()}, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b.x(), b.y(), b.z(), b.w()}, 0);
        return va.mul(vb).reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
    }

    public static void batchAdd(Vector4D[] results, Vector4D[] a, Vector4D[] b) {
        for (int i = 0; i < a.length; i++) {
            DoubleVector va = DoubleVector.fromArray(SPECIES, new double[]{a[i].x(), a[i].y(), a[i].z(), a[i].w()}, 0);
            DoubleVector vb = DoubleVector.fromArray(SPECIES, new double[]{b[i].x(), b[i].y(), b[i].z(), b[i].w()}, 0);
            DoubleVector vr = va.add(vb);
            results[i] = new Vector4D(vr.lane(0), vr.lane(1), vr.lane(2), vr.lane(3));
        }
    }
}


package com.jamma.math.incubator;

import com.jamma.math.Vector4D;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

public final class VectorApiMath {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;

    private VectorApiMath() {
    }

    public static Vector4D add(Vector4D a, Vector4D b) {
        double[] arr = new double[4];
        double[] brr = new double[4];
        arr[0] = a.x(); arr[1] = a.y(); arr[2] = a.z(); arr[3] = a.w();
        brr[0] = b.x(); brr[1] = b.y(); brr[2] = b.z(); brr[3] = b.w();
        DoubleVector va = DoubleVector.fromArray(SPECIES, arr, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, brr, 0);
        DoubleVector vr = va.add(vb);
        vr.intoArray(arr, 0);
        return new Vector4D(arr[0], arr[1], arr[2], arr[3]);
    }

    public static Vector4D sub(Vector4D a, Vector4D b) {
        double[] arr = new double[4];
        double[] brr = new double[4];
        arr[0] = a.x(); arr[1] = a.y(); arr[2] = a.z(); arr[3] = a.w();
        brr[0] = b.x(); brr[1] = b.y(); brr[2] = b.z(); brr[3] = b.w();
        DoubleVector va = DoubleVector.fromArray(SPECIES, arr, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, brr, 0);
        DoubleVector vr = va.sub(vb);
        vr.intoArray(arr, 0);
        return new Vector4D(arr[0], arr[1], arr[2], arr[3]);
    }

    public static Vector4D mul(Vector4D a, Vector4D b) {
        double[] arr = new double[4];
        double[] brr = new double[4];
        arr[0] = a.x(); arr[1] = a.y(); arr[2] = a.z(); arr[3] = a.w();
        brr[0] = b.x(); brr[1] = b.y(); brr[2] = b.z(); brr[3] = b.w();
        DoubleVector va = DoubleVector.fromArray(SPECIES, arr, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, brr, 0);
        DoubleVector vr = va.mul(vb);
        vr.intoArray(arr, 0);
        return new Vector4D(arr[0], arr[1], arr[2], arr[3]);
    }

    public static Vector4D scale(Vector4D v, double s) {
        double[] arr = new double[4];
        arr[0] = v.x(); arr[1] = v.y(); arr[2] = v.z(); arr[3] = v.w();
        DoubleVector va = DoubleVector.fromArray(SPECIES, arr, 0);
        DoubleVector vr = va.mul(s);
        vr.intoArray(arr, 0);
        return new Vector4D(arr[0], arr[1], arr[2], arr[3]);
    }

    public static double dot(Vector4D a, Vector4D b) {
        double[] arr = new double[4];
        double[] brr = new double[4];
        arr[0] = a.x(); arr[1] = a.y(); arr[2] = a.z(); arr[3] = a.w();
        brr[0] = b.x(); brr[1] = b.y(); brr[2] = b.z(); brr[3] = b.w();
        DoubleVector va = DoubleVector.fromArray(SPECIES, arr, 0);
        DoubleVector vb = DoubleVector.fromArray(SPECIES, brr, 0);
        return va.mul(vb).reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
    }

    public static void batchAdd(Vector4D[] results, Vector4D[] a, Vector4D[] b) {
        int i = 0;
        for (; i <= a.length - SPECIES.length(); i += SPECIES.length()) {
            double[] ar = new double[SPECIES.length() * 4];
            double[] br = new double[SPECIES.length() * 4];
            for (int j = 0; j < SPECIES.length(); j++) {
                ar[j * 4] = a[i + j].x(); ar[j * 4 + 1] = a[i + j].y();
                ar[j * 4 + 2] = a[i + j].z(); ar[j * 4 + 3] = a[i + j].w();
                br[j * 4] = b[i + j].x(); br[j * 4 + 1] = b[i + j].y();
                br[j * 4 + 2] = b[i + j].z(); br[j * 4 + 3] = b[i + j].w();
            }
        }
        for (; i < a.length; i++) {
            results[i] = add(a[i], b[i]);
        }
    }
}

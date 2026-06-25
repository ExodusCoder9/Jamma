package com.jamma.math;

import java.util.Objects;

public final class MemUtil {

    private MemUtil() {}

    public static void copy(float[] src, int srcOff, float[] dest, int destOff, int length) {
        Objects.checkFromIndexSize(srcOff, length, src.length);
        Objects.checkFromIndexSize(destOff, length, dest.length);
        System.arraycopy(src, srcOff, dest, destOff, length);
    }

    public static void copy(double[] src, int srcOff, double[] dest, int destOff, int length) {
        Objects.checkFromIndexSize(srcOff, length, src.length);
        Objects.checkFromIndexSize(destOff, length, dest.length);
        System.arraycopy(src, srcOff, dest, destOff, length);
    }

    public static void fill(float[] arr, int off, int len, float val) {
        Objects.checkFromIndexSize(off, len, arr.length);
        for (int i = off; i < off + len; i++) {
            arr[i] = val;
        }
    }

    public static void fill(double[] arr, int off, int len, double val) {
        Objects.checkFromIndexSize(off, len, arr.length);
        for (int i = off; i < off + len; i++) {
            arr[i] = val;
        }
    }

    public static <T> void memcpy(T[] src, int srcOff, T[] dest, int destOff, int len) {
        Objects.checkFromIndexSize(srcOff, len, src.length);
        Objects.checkFromIndexSize(destOff, len, dest.length);
        System.arraycopy(src, srcOff, dest, destOff, len);
    }
}

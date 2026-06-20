package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector3D(double x, double y, double z) implements Serializable {

    private static final long serialVersionUID = 1L;
    public static Vector3D fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector3D(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, z);
    }

    public static Vector3D fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector3D(src.get(), src.get(), src.get());
    }

    public static Vector3D fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector3D(src.get(index), src.get(index + 1), src.get(index + 2));
    }

    public java.nio.DoubleBuffer writeToBuffer(java.nio.DoubleBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        return dest;
    }

    public java.nio.DoubleBuffer writeToBuffer(int index, java.nio.DoubleBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        return dest;
    }
}

package com.jamma.math;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector3D(double x, double y, double z) {
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
}

package com.jamma.math;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector4D(double x, double y, double z, double w) {
    public static Vector4D fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector4D(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 24)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, z);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 24, w);
    }
}

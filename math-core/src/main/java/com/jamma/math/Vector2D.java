package com.jamma.math;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector2D(double x, double y) {
    public static Vector2D fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector2D(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
    }
}

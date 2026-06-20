package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record Vector2D(double x, double y) implements Serializable {

    private static final long serialVersionUID = 1L;
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

    public static Vector2D fromBuffer(java.nio.DoubleBuffer src) {
        return new Vector2D(src.get(), src.get());
    }

    public static Vector2D fromBuffer(int index, java.nio.DoubleBuffer src) {
        return new Vector2D(src.get(index), src.get(index + 1));
    }

    public java.nio.DoubleBuffer writeToBuffer(java.nio.DoubleBuffer dest) {
        dest.put(x);
        dest.put(y);
        return dest;
    }

    public java.nio.DoubleBuffer writeToBuffer(int index, java.nio.DoubleBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        return dest;
    }
}

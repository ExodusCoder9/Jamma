package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

public record Vector2f(float x, float y) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Vector2f fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector2f(
            src.get(ValueLayout.JAVA_FLOAT, byteOffset),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, x);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, y);
    }

    public static Vector2f fromBuffer(FloatBuffer src) {
        return new Vector2f(src.get(), src.get());
    }

    public static Vector2f fromBuffer(int index, FloatBuffer src) {
        return new Vector2f(src.get(index), src.get(index + 1));
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(x);
        dest.put(y);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        return dest;
    }
}

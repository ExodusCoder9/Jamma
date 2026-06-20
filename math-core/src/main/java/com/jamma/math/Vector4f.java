package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

public record Vector4f(float x, float y, float z, float w) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Vector4f fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector4f(
            src.get(ValueLayout.JAVA_FLOAT, byteOffset),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 12)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, x);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, y);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, z);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 12, w);
    }

    public static Vector4f fromBuffer(FloatBuffer src) {
        return new Vector4f(src.get(), src.get(), src.get(), src.get());
    }

    public static Vector4f fromBuffer(int index, FloatBuffer src) {
        return new Vector4f(src.get(index), src.get(index + 1), src.get(index + 2), src.get(index + 3));
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        dest.put(w);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        dest.put(index + 3, w);
        return dest;
    }
}

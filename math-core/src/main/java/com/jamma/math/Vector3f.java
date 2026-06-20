package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

public record Vector3f(float x, float y, float z) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Vector3f fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector3f(
            src.get(ValueLayout.JAVA_FLOAT, byteOffset),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, x);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, y);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, z);
    }

    public static Vector3f fromBuffer(FloatBuffer src) {
        return new Vector3f(src.get(), src.get(), src.get());
    }

    public static Vector3f fromBuffer(int index, FloatBuffer src) {
        return new Vector3f(src.get(index), src.get(index + 1), src.get(index + 2));
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        return dest;
    }
}

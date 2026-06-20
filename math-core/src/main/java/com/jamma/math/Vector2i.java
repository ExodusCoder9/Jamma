package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.IntBuffer;

public record Vector2i(int x, int y) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Vector2i fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector2i(
            src.get(ValueLayout.JAVA_INT, byteOffset),
            src.get(ValueLayout.JAVA_INT, byteOffset + 4)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_INT, byteOffset, x);
        dest.set(ValueLayout.JAVA_INT, byteOffset + 4, y);
    }

    public static Vector2i fromBuffer(IntBuffer src) {
        return new Vector2i(src.get(), src.get());
    }

    public static Vector2i fromBuffer(int index, IntBuffer src) {
        return new Vector2i(src.get(index), src.get(index + 1));
    }

    public IntBuffer writeToBuffer(IntBuffer dest) {
        dest.put(x);
        dest.put(y);
        return dest;
    }

    public IntBuffer writeToBuffer(int index, IntBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        return dest;
    }

    public Vector2i add(Vector2i other) {
        return new Vector2i(this.x + other.x, this.y + other.y);
    }

    public Vector2i add(int x, int y) {
        return new Vector2i(this.x + x, this.y + y);
    }

    public Vector2i sub(Vector2i other) {
        return new Vector2i(this.x - other.x, this.y - other.y);
    }

    public Vector2i sub(int x, int y) {
        return new Vector2i(this.x - x, this.y - y);
    }

    public Vector2i mul(int factor) {
        return new Vector2i(this.x * factor, this.y * factor);
    }

    public Vector2i div(int divisor) {
        return new Vector2i(this.x / divisor, this.y / divisor);
    }

    public long lengthSquared() {
        return (long) x * x + (long) y * y;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public long distanceSquared(Vector2i other) {
        long dx = this.x - other.x;
        long dy = this.y - other.y;
        return dx * dx + dy * dy;
    }

    public double distance(Vector2i other) {
        return Math.sqrt(distanceSquared(other));
    }
}

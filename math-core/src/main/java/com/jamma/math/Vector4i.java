package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.IntBuffer;

public record Vector4i(int x, int y, int z, int w) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Vector4i fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector4i(
            src.get(ValueLayout.JAVA_INT, byteOffset),
            src.get(ValueLayout.JAVA_INT, byteOffset + 4),
            src.get(ValueLayout.JAVA_INT, byteOffset + 8),
            src.get(ValueLayout.JAVA_INT, byteOffset + 12)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_INT, byteOffset, x);
        dest.set(ValueLayout.JAVA_INT, byteOffset + 4, y);
        dest.set(ValueLayout.JAVA_INT, byteOffset + 8, z);
        dest.set(ValueLayout.JAVA_INT, byteOffset + 12, w);
    }

    public static Vector4i fromBuffer(IntBuffer src) {
        return new Vector4i(src.get(), src.get(), src.get(), src.get());
    }

    public static Vector4i fromBuffer(int index, IntBuffer src) {
        return new Vector4i(src.get(index), src.get(index + 1), src.get(index + 2), src.get(index + 3));
    }

    public IntBuffer writeToBuffer(IntBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        dest.put(w);
        return dest;
    }

    public IntBuffer writeToBuffer(int index, IntBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        dest.put(index + 3, w);
        return dest;
    }

    public Vector4i add(Vector4i other) {
        return new Vector4i(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
    }

    public Vector4i add(int x, int y, int z, int w) {
        return new Vector4i(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Vector4i sub(Vector4i other) {
        return new Vector4i(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
    }

    public Vector4i sub(int x, int y, int z, int w) {
        return new Vector4i(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    public Vector4i mul(int factor) {
        return new Vector4i(this.x * factor, this.y * factor, this.z * factor, this.w * factor);
    }

    public Vector4i div(int divisor) {
        return new Vector4i(this.x / divisor, this.y / divisor, this.z / divisor, this.w / divisor);
    }

    public long lengthSquared() {
        return (long) x * x + (long) y * y + (long) z * z + (long) w * w;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public long distanceSquared(Vector4i other) {
        long dx = this.x - other.x;
        long dy = this.y - other.y;
        long dz = this.z - other.z;
        long dw = this.w - other.w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    public double distance(Vector4i other) {
        return Math.sqrt(distanceSquared(other));
    }
}

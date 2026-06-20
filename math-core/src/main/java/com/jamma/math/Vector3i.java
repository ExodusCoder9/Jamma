package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.IntBuffer;

public record Vector3i(int x, int y, int z) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Vector3i fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Vector3i(
            src.get(ValueLayout.JAVA_INT, byteOffset),
            src.get(ValueLayout.JAVA_INT, byteOffset + 4),
            src.get(ValueLayout.JAVA_INT, byteOffset + 8)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_INT, byteOffset, x);
        dest.set(ValueLayout.JAVA_INT, byteOffset + 4, y);
        dest.set(ValueLayout.JAVA_INT, byteOffset + 8, z);
    }

    public static Vector3i fromBuffer(IntBuffer src) {
        return new Vector3i(src.get(), src.get(), src.get());
    }

    public static Vector3i fromBuffer(int index, IntBuffer src) {
        return new Vector3i(src.get(index), src.get(index + 1), src.get(index + 2));
    }

    public IntBuffer writeToBuffer(IntBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        return dest;
    }

    public IntBuffer writeToBuffer(int index, IntBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        return dest;
    }

    public Vector3i add(Vector3i other) {
        return new Vector3i(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3i add(int x, int y, int z) {
        return new Vector3i(this.x + x, this.y + y, this.z + z);
    }

    public Vector3i sub(Vector3i other) {
        return new Vector3i(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3i sub(int x, int y, int z) {
        return new Vector3i(this.x - x, this.y - y, this.z - z);
    }

    public Vector3i mul(int factor) {
        return new Vector3i(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector3i div(int divisor) {
        return new Vector3i(this.x / divisor, this.y / divisor, this.z / divisor);
    }

    public long lengthSquared() {
        return (long) x * x + (long) y * y + (long) z * z;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public long distanceSquared(Vector3i other) {
        long dx = this.x - other.x;
        long dy = this.y - other.y;
        long dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(Vector3i other) {
        return Math.sqrt(distanceSquared(other));
    }
}

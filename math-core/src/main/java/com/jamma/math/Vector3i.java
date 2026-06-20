package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.IntBuffer;

public record Vector3i(int x, int y, int z) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector3i() {
        this(0, 0, 0);
    }

    public Vector3i(int[] values) {
        this(values[0], values[1], values[2]);
    }

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

    public Vector3i mul(Vector3i v) {
        return new Vector3i(this.x * v.x, this.y * v.y, this.z * v.z);
    }

    public Vector3i mul(int factor) {
        return new Vector3i(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector3i mul(int x, int y, int z) {
        return new Vector3i(this.x * x, this.y * y, this.z * z);
    }

    public Vector3i div(Vector3i v) {
        return new Vector3i(this.x / v.x, this.y / v.y, this.z / v.z);
    }

    public Vector3i div(int divisor) {
        return new Vector3i(this.x / divisor, this.y / divisor, this.z / divisor);
    }

    public Vector3i negate() {
        return new Vector3i(-x, -y, -z);
    }

    public Vector3i abs() {
        return new Vector3i(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public int dot(Vector3i v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3i cross(Vector3i v) {
        return new Vector3i(
            this.y * v.z - this.z * v.y,
            this.z * v.x - this.x * v.z,
            this.x * v.y - this.y * v.x
        );
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

    public int gridDistance(Vector3i v) {
        return Math.abs(x - v.x) + Math.abs(y - v.y) + Math.abs(z - v.z);
    }

    public int minComponent() {
        return Math.min(Math.min(x, y), z);
    }

    public int maxComponent() {
        return Math.max(Math.max(x, y), z);
    }

    public Vector3i min(Vector3i v) {
        return new Vector3i(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z));
    }

    public Vector3i max(Vector3i v) {
        return new Vector3i(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z));
    }

    public Vector3i zero() {
        return new Vector3i(0, 0, 0);
    }

    public boolean equals(Vector3i v, int delta) {
        if (this == v) return true;
        return Math.abs(x - v.x) <= delta && Math.abs(y - v.y) <= delta && Math.abs(z - v.z) <= delta;
    }

    public int[] get(int[] dest) {
        dest[0] = x;
        dest[1] = y;
        dest[2] = z;
        return dest;
    }

    public Vector3f toVector3f() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public Vector3d toVector3d() {
        return new Vector3d((double) x, (double) y, (double) z);
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_INT, offset, x);
        segment.set(ValueLayout.JAVA_INT, offset + 4, y);
        segment.set(ValueLayout.JAVA_INT, offset + 8, z);
    }

    public static Vector3i read(MemorySegment segment, long offset) {
        return new Vector3i(
            segment.get(ValueLayout.JAVA_INT, offset),
            segment.get(ValueLayout.JAVA_INT, offset + 4),
            segment.get(ValueLayout.JAVA_INT, offset + 8)
        );
    }
}

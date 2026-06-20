package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.IntBuffer;

public record Vector4i(int x, int y, int z, int w) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector4i() {
        this(0, 0, 0, 0);
    }

    public Vector4i(int[] values) {
        this(values[0], values[1], values[2], values[3]);
    }

    public Vector4i(Vector2i v, int z, int w) {
        this(v.x(), v.y(), z, w);
    }

    public Vector4i(Vector3i v, int w) {
        this(v.x(), v.y(), v.z(), w);
    }

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

    public Vector4i mul(Vector4i v) {
        return new Vector4i(this.x * v.x, this.y * v.y, this.z * v.z, this.w * v.w);
    }

    public Vector4i mul(int factor) {
        return new Vector4i(this.x * factor, this.y * factor, this.z * factor, this.w * factor);
    }

    public Vector4i mul(int x, int y, int z, int w) {
        return new Vector4i(this.x * x, this.y * y, this.z * z, this.w * w);
    }

    public Vector4i div(Vector4i v) {
        return new Vector4i(this.x / v.x, this.y / v.y, this.z / v.z, this.w / v.w);
    }

    public Vector4i div(int divisor) {
        return new Vector4i(this.x / divisor, this.y / divisor, this.z / divisor, this.w / divisor);
    }

    public Vector4i negate() {
        return new Vector4i(-x, -y, -z, -w);
    }

    public Vector4i abs() {
        return new Vector4i(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
    }

    public Vector2i toVector2i() {
        return new Vector2i(x, y);
    }

    public Vector3i toVector3i() {
        return new Vector3i(x, y, z);
    }

    public int dot(Vector4i v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
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

    public int gridDistance(Vector4i v) {
        return Math.abs(x - v.x) + Math.abs(y - v.y) + Math.abs(z - v.z) + Math.abs(w - v.w);
    }

    public int minComponent() {
        return Math.min(Math.min(x, y), Math.min(z, w));
    }

    public int maxComponent() {
        return Math.max(Math.max(x, y), Math.max(z, w));
    }

    public Vector4i min(Vector4i v) {
        return new Vector4i(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z), Math.min(w, v.w));
    }

    public Vector4i max(Vector4i v) {
        return new Vector4i(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z), Math.max(w, v.w));
    }

    public Vector4i zero() {
        return new Vector4i(0, 0, 0, 0);
    }

    public boolean equals(Vector4i v, int delta) {
        if (this == v) return true;
        return Math.abs(x - v.x) <= delta && Math.abs(y - v.y) <= delta && Math.abs(z - v.z) <= delta && Math.abs(w - v.w) <= delta;
    }

    public int[] get(int[] dest) {
        dest[0] = x;
        dest[1] = y;
        dest[2] = z;
        dest[3] = w;
        return dest;
    }

    public Vector4f toVector4f() {
        return new Vector4f((float) x, (float) y, (float) z, (float) w);
    }

    public Vector4D toVector4D() {
        return new Vector4D((double) x, (double) y, (double) z, (double) w);
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_INT, offset, x);
        segment.set(ValueLayout.JAVA_INT, offset + 4, y);
        segment.set(ValueLayout.JAVA_INT, offset + 8, z);
        segment.set(ValueLayout.JAVA_INT, offset + 12, w);
    }

    public static Vector4i read(MemorySegment segment, long offset) {
        return new Vector4i(
            segment.get(ValueLayout.JAVA_INT, offset),
            segment.get(ValueLayout.JAVA_INT, offset + 4),
            segment.get(ValueLayout.JAVA_INT, offset + 8),
            segment.get(ValueLayout.JAVA_INT, offset + 12)
        );
    }
}

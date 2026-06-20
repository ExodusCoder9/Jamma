package com.jamma.math;

import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.IntBuffer;

public record Vector2i(int x, int y) implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector2i() {
        this(0, 0);
    }

    public Vector2i(int[] values) {
        this(values[0], values[1]);
    }

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

    public Vector2i mul(Vector2i v) {
        return new Vector2i(this.x * v.x, this.y * v.y);
    }

    public Vector2i mul(int factor) {
        return new Vector2i(this.x * factor, this.y * factor);
    }

    public Vector2i mul(int x, int y) {
        return new Vector2i(this.x * x, this.y * y);
    }

    public Vector2i div(Vector2i v) {
        return new Vector2i(this.x / v.x, this.y / v.y);
    }

    public Vector2i div(int divisor) {
        return new Vector2i(this.x / divisor, this.y / divisor);
    }

    public Vector2i negate() {
        return new Vector2i(-x, -y);
    }

    public Vector2i abs() {
        return new Vector2i(Math.abs(x), Math.abs(y));
    }

    public int dot(Vector2i v) {
        return x * v.x + y * v.y;
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

    public int gridDistance(Vector2i v) {
        return Math.abs(x - v.x) + Math.abs(y - v.y);
    }

    public int minComponent() {
        return Math.min(x, y);
    }

    public int maxComponent() {
        return Math.max(x, y);
    }

    public Vector2i min(Vector2i v) {
        return new Vector2i(Math.min(x, v.x), Math.min(y, v.y));
    }

    public Vector2i max(Vector2i v) {
        return new Vector2i(Math.max(x, v.x), Math.max(y, v.y));
    }

    public Vector2i zero() {
        return new Vector2i(0, 0);
    }

    public boolean equals(Vector2i v, int delta) {
        if (this == v) return true;
        return Math.abs(x - v.x) <= delta && Math.abs(y - v.y) <= delta;
    }

    public int[] get(int[] dest) {
        dest[0] = x;
        dest[1] = y;
        return dest;
    }

    public Vector2f toVector2f() {
        return new Vector2f((float) x, (float) y);
    }

    public Vector2D toVector2D() {
        return new Vector2D((double) x, (double) y);
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_INT, offset, x);
        segment.set(ValueLayout.JAVA_INT, offset + 4, y);
    }

    public static Vector2i read(MemorySegment segment, long offset) {
        return new Vector2i(
            segment.get(ValueLayout.JAVA_INT, offset),
            segment.get(ValueLayout.JAVA_INT, offset + 4)
        );
    }
}

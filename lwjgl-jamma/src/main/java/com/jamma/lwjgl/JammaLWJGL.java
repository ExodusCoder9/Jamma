package com.jamma.lwjgl;

import com.jamma.math.Vector2f;
import com.jamma.math.Vector3d;
import com.jamma.math.Vector3f;
import com.jamma.math.Vector4f;
import com.jamma.math.matrix.Matrix4d;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaterniond;
import com.jamma.math.quaternion.Quaternionf;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

/**
 * LWJGL interop utilities for Jamma math types.
 *
 * <p>All methods write directly into LWJGL-compatible buffers
 * (column-major for matrices, xyzw for vectors) without allocation.</p>
 *
 * <p>Usage with MemoryStack:</p>
 * <pre>{@code
 * try (var stack = MemoryStack.stackPush()) {
 *     FloatBuffer buf = stack.mallocFloat(16);
 *     JammaLWJGL.put(matrix, buf);
 *     glUniformMatrix4fv(location, false, buf);
 * }
 * }</pre>
 */
public final class JammaLWJGL {

    private JammaLWJGL() {}

    // ─── Matrix4f ───────────────────────────────────────────────

    public static FloatBuffer put(Matrix4f m, FloatBuffer buf) {
        buf.put(m.m00()).put(m.m01()).put(m.m02()).put(m.m03());
        buf.put(m.m10()).put(m.m11()).put(m.m12()).put(m.m13());
        buf.put(m.m20()).put(m.m21()).put(m.m22()).put(m.m23());
        buf.put(m.m30()).put(m.m31()).put(m.m32()).put(m.m33());
        return buf;
    }

    public static Matrix4f get(FloatBuffer buf, Matrix4f dest) {
        dest.m00(buf.get()); dest.m01(buf.get()); dest.m02(buf.get()); dest.m03(buf.get());
        dest.m10(buf.get()); dest.m11(buf.get()); dest.m12(buf.get()); dest.m13(buf.get());
        dest.m20(buf.get()); dest.m21(buf.get()); dest.m22(buf.get()); dest.m23(buf.get());
        dest.m30(buf.get()); dest.m31(buf.get()); dest.m32(buf.get()); dest.m33(buf.get());
        return dest;
    }

    public static MemorySegment put(Matrix4f m, MemorySegment seg, long offset) {
        m.get(seg, offset);
        return seg;
    }

    // ─── Matrix4d ───────────────────────────────────────────────

    public static DoubleBuffer put(Matrix4d m, DoubleBuffer buf) {
        buf.put(m.m00()).put(m.m01()).put(m.m02()).put(m.m03());
        buf.put(m.m10()).put(m.m11()).put(m.m12()).put(m.m13());
        buf.put(m.m20()).put(m.m21()).put(m.m22()).put(m.m23());
        buf.put(m.m30()).put(m.m31()).put(m.m32()).put(m.m33());
        return buf;
    }

    public static Matrix4d get(DoubleBuffer buf, Matrix4d dest) {
        dest.m00(buf.get()); dest.m01(buf.get()); dest.m02(buf.get()); dest.m03(buf.get());
        dest.m10(buf.get()); dest.m11(buf.get()); dest.m12(buf.get()); dest.m13(buf.get());
        dest.m20(buf.get()); dest.m21(buf.get()); dest.m22(buf.get()); dest.m23(buf.get());
        dest.m30(buf.get()); dest.m31(buf.get()); dest.m32(buf.get()); dest.m33(buf.get());
        return dest;
    }

    public static MemorySegment put(Matrix4d m, MemorySegment seg, long offset) {
        m.get(seg, offset);
        return seg;
    }

    // ─── Vector3f ───────────────────────────────────────────────

    public static FloatBuffer put(Vector3f v, FloatBuffer buf) {
        buf.put(v.x()).put(v.y()).put(v.z());
        return buf;
    }

    public static void put(Vector3f v, MemorySegment seg, long offset) {
        seg.set(ValueLayout.JAVA_FLOAT, offset, v.x());
        seg.set(ValueLayout.JAVA_FLOAT, offset + 4, v.y());
        seg.set(ValueLayout.JAVA_FLOAT, offset + 8, v.z());
    }

    public static Vector3f getVec3(FloatBuffer buf, Vector3f dest) {
        return new Vector3f(buf.get(), buf.get(), buf.get());
    }

    // ─── Vector4f ───────────────────────────────────────────────

    public static FloatBuffer put(Vector4f v, FloatBuffer buf) {
        buf.put(v.x()).put(v.y()).put(v.z()).put(v.w());
        return buf;
    }

    public static void put(Vector4f v, MemorySegment seg, long offset) {
        seg.set(ValueLayout.JAVA_FLOAT, offset, v.x());
        seg.set(ValueLayout.JAVA_FLOAT, offset + 4, v.y());
        seg.set(ValueLayout.JAVA_FLOAT, offset + 8, v.z());
        seg.set(ValueLayout.JAVA_FLOAT, offset + 12, v.w());
    }

    // ─── Vector3d ───────────────────────────────────────────────

    public static DoubleBuffer put(Vector3d v, DoubleBuffer buf) {
        buf.put(v.x()).put(v.y()).put(v.z());
        return buf;
    }

    public static void put(Vector3d v, MemorySegment seg, long offset) {
        seg.set(ValueLayout.JAVA_DOUBLE, offset, v.x());
        seg.set(ValueLayout.JAVA_DOUBLE, offset + 8, v.y());
        seg.set(ValueLayout.JAVA_DOUBLE, offset + 16, v.z());
    }

    // ─── Vector2f ───────────────────────────────────────────────

    public static FloatBuffer put(Vector2f v, FloatBuffer buf) {
        buf.put(v.x()).put(v.y());
        return buf;
    }

    public static void put(Vector2f v, MemorySegment seg, long offset) {
        seg.set(ValueLayout.JAVA_FLOAT, offset, v.x());
        seg.set(ValueLayout.JAVA_FLOAT, offset + 4, v.y());
    }

    // ─── Quaternion ─────────────────────────────────────────────

    public static FloatBuffer put(Quaternionf q, FloatBuffer buf) {
        buf.put(q.x()).put(q.y()).put(q.z()).put(q.w());
        return buf;
    }

    public static DoubleBuffer put(Quaterniond q, DoubleBuffer buf) {
        buf.put(q.x()).put(q.y()).put(q.z()).put(q.w());
        return buf;
    }

    // ─── Convenience: MemorySegment from ByteBuffer ─────────────

    public static MemorySegment segment(ByteBuffer bb) {
        return MemorySegment.ofBuffer(bb);
    }

    public static MemorySegment segment(FloatBuffer fb) {
        return MemorySegment.ofBuffer(fb);
    }

    public static MemorySegment segment(DoubleBuffer db) {
        return MemorySegment.ofBuffer(db);
    }
}

package com.jamma.math.incubator;

import com.jamma.math.quaternion.Quaterniond;
import com.jamma.math.Vector2d;
import com.jamma.math.Vector3d;
import com.jamma.math.Vector4d;
import com.jamma.math.matrix.Matrix4d;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public final class MemoryOps {

    private MemoryOps() {
    }

    // ── Vec4 layout (double) ────────────────────────────────────────────

    private static final MemoryLayout VEC4_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("x"),
        ValueLayout.JAVA_DOUBLE.withName("y"),
        ValueLayout.JAVA_DOUBLE.withName("z"),
        ValueLayout.JAVA_DOUBLE.withName("w")
    );

    private static final VarHandle VEC4_X = VEC4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle VEC4_Y = VEC4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("y"));
    private static final VarHandle VEC4_Z = VEC4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("z"));
    private static final VarHandle VEC4_W = VEC4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("w"));

    public static MemorySegment allocateVec4(Arena arena) {
        return arena.allocate(VEC4_LAYOUT);
    }

    public static MemorySegment allocateVec4Array(Arena arena, long count) {
        return arena.allocate(VEC4_LAYOUT, count);
    }

    public static void setVec4(MemorySegment segment, double x, double y, double z, double w) {
        VEC4_X.set(segment, 0L, x);
        VEC4_Y.set(segment, 0L, y);
        VEC4_Z.set(segment, 0L, z);
        VEC4_W.set(segment, 0L, w);
    }

    public static void setVec4(MemorySegment segment, long index, Vector4d v) {
        long offset = index * 32;
        VEC4_X.set(segment, offset, v.x());
        VEC4_Y.set(segment, offset, v.y());
        VEC4_Z.set(segment, offset, v.z());
        VEC4_W.set(segment, offset, v.w());
    }

    public static Vector4d getVec4(MemorySegment segment, long index) {
        long offset = index * 32;
        return new Vector4d(
            (double) VEC4_X.get(segment, offset),
            (double) VEC4_Y.get(segment, offset),
            (double) VEC4_Z.get(segment, offset),
            (double) VEC4_W.get(segment, offset)
        );
    }

    public static double getVec4X(MemorySegment segment) {
        return (double) VEC4_X.get(segment, 0L);
    }

    public static double getVec4Y(MemorySegment segment) {
        return (double) VEC4_Y.get(segment, 0L);
    }

    public static double getVec4Z(MemorySegment segment) {
        return (double) VEC4_Z.get(segment, 0L);
    }

    public static double getVec4W(MemorySegment segment) {
        return (double) VEC4_W.get(segment, 0L);
    }

    // ── Vec3 layout (double) ────────────────────────────────────────────

    private static final MemoryLayout VEC3_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("x"),
        ValueLayout.JAVA_DOUBLE.withName("y"),
        ValueLayout.JAVA_DOUBLE.withName("z")
    );

    private static final VarHandle VEC3_X = VEC3_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle VEC3_Y = VEC3_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("y"));
    private static final VarHandle VEC3_Z = VEC3_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("z"));

    public static MemorySegment allocateVec3(Arena arena) {
        return arena.allocate(VEC3_LAYOUT);
    }

    public static MemorySegment allocateVec3Array(Arena arena, long count) {
        return arena.allocate(VEC3_LAYOUT, count);
    }

    public static void setVec3(MemorySegment segment, long index, Vector3d v) {
        long offset = index * 24;
        VEC3_X.set(segment, offset, v.x());
        VEC3_Y.set(segment, offset, v.y());
        VEC3_Z.set(segment, offset, v.z());
    }

    public static Vector3d getVec3(MemorySegment segment, long index) {
        long offset = index * 24;
        return new Vector3d(
            (double) VEC3_X.get(segment, offset),
            (double) VEC3_Y.get(segment, offset),
            (double) VEC3_Z.get(segment, offset)
        );
    }

    // ── Vec2 layout (double) ────────────────────────────────────────────

    private static final MemoryLayout VEC2_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("x"),
        ValueLayout.JAVA_DOUBLE.withName("y")
    );

    private static final VarHandle VEC2_X = VEC2_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle VEC2_Y = VEC2_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("y"));

    public static MemorySegment allocateVec2(Arena arena) {
        return arena.allocate(VEC2_LAYOUT);
    }

    public static MemorySegment allocateVec2Array(Arena arena, long count) {
        return arena.allocate(VEC2_LAYOUT, count);
    }

    public static void setVec2(MemorySegment segment, long index, Vector2d v) {
        long offset = index * 16;
        VEC2_X.set(segment, offset, v.x());
        VEC2_Y.set(segment, offset, v.y());
    }

    public static Vector2d getVec2(MemorySegment segment, long index) {
        long offset = index * 16;
        return new Vector2d(
            (double) VEC2_X.get(segment, offset),
            (double) VEC2_Y.get(segment, offset)
        );
    }

    // ── Quaterniond layout (double) ─────────────────────────────────────

    private static final MemoryLayout QUAT_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("x"),
        ValueLayout.JAVA_DOUBLE.withName("y"),
        ValueLayout.JAVA_DOUBLE.withName("z"),
        ValueLayout.JAVA_DOUBLE.withName("w")
    );

    private static final VarHandle QUAT_X = QUAT_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle QUAT_Y = QUAT_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("y"));
    private static final VarHandle QUAT_Z = QUAT_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("z"));
    private static final VarHandle QUAT_W = QUAT_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("w"));

    public static MemorySegment allocateQuaterniond(Arena arena) {
        return arena.allocate(QUAT_LAYOUT);
    }

    public static MemorySegment allocateQuaterniondArray(Arena arena, long count) {
        return arena.allocate(QUAT_LAYOUT, count);
    }

    public static void setQuaterniond(MemorySegment segment, long index, Quaterniond q) {
        long offset = index * 32;
        QUAT_X.set(segment, offset, q.x());
        QUAT_Y.set(segment, offset, q.y());
        QUAT_Z.set(segment, offset, q.z());
        QUAT_W.set(segment, offset, q.w());
    }

    public static Quaterniond getQuaterniond(MemorySegment segment, long index) {
        long offset = index * 32;
        return new Quaterniond(
            (double) QUAT_X.get(segment, offset),
            (double) QUAT_Y.get(segment, offset),
            (double) QUAT_Z.get(segment, offset),
            (double) QUAT_W.get(segment, offset)
        );
    }

    // ── Matrix4d layout (column-major, 16 doubles) ──────────────────────

    private static final MemoryLayout MAT4_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("m00"),
        ValueLayout.JAVA_DOUBLE.withName("m10"),
        ValueLayout.JAVA_DOUBLE.withName("m20"),
        ValueLayout.JAVA_DOUBLE.withName("m30"),
        ValueLayout.JAVA_DOUBLE.withName("m01"),
        ValueLayout.JAVA_DOUBLE.withName("m11"),
        ValueLayout.JAVA_DOUBLE.withName("m21"),
        ValueLayout.JAVA_DOUBLE.withName("m31"),
        ValueLayout.JAVA_DOUBLE.withName("m02"),
        ValueLayout.JAVA_DOUBLE.withName("m12"),
        ValueLayout.JAVA_DOUBLE.withName("m22"),
        ValueLayout.JAVA_DOUBLE.withName("m32"),
        ValueLayout.JAVA_DOUBLE.withName("m03"),
        ValueLayout.JAVA_DOUBLE.withName("m13"),
        ValueLayout.JAVA_DOUBLE.withName("m23"),
        ValueLayout.JAVA_DOUBLE.withName("m33")
    );

    private static final VarHandle MAT_M00 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m00"));
    private static final VarHandle MAT_M10 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m10"));
    private static final VarHandle MAT_M20 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m20"));
    private static final VarHandle MAT_M30 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m30"));
    private static final VarHandle MAT_M01 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m01"));
    private static final VarHandle MAT_M11 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m11"));
    private static final VarHandle MAT_M21 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m21"));
    private static final VarHandle MAT_M31 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m31"));
    private static final VarHandle MAT_M02 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m02"));
    private static final VarHandle MAT_M12 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m12"));
    private static final VarHandle MAT_M22 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m22"));
    private static final VarHandle MAT_M32 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m32"));
    private static final VarHandle MAT_M03 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m03"));
    private static final VarHandle MAT_M13 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m13"));
    private static final VarHandle MAT_M23 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m23"));
    private static final VarHandle MAT_M33 = MAT4_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("m33"));

    public static MemorySegment allocateMatrix4d(Arena arena) {
        return arena.allocate(MAT4_LAYOUT);
    }

    public static MemorySegment allocateMatrix4dArray(Arena arena, long count) {
        return arena.allocate(MAT4_LAYOUT, count);
    }

    public static void setMatrix4d(MemorySegment segment, long index, Matrix4d m) {
        long base = index * 128;
        MAT_M00.set(segment, base, m.m00); MAT_M01.set(segment, base, m.m01);
        MAT_M10.set(segment, base, m.m10); MAT_M11.set(segment, base, m.m11);
        MAT_M20.set(segment, base, m.m20); MAT_M21.set(segment, base, m.m21);
        MAT_M30.set(segment, base, m.m30); MAT_M31.set(segment, base, m.m31);
        MAT_M02.set(segment, base, m.m02); MAT_M03.set(segment, base, m.m03);
        MAT_M12.set(segment, base, m.m12); MAT_M13.set(segment, base, m.m13);
        MAT_M22.set(segment, base, m.m22); MAT_M23.set(segment, base, m.m23);
        MAT_M32.set(segment, base, m.m32); MAT_M33.set(segment, base, m.m33);
    }

    public static Matrix4d getMatrix4d(MemorySegment segment, long index) {
        long base = index * 128;
        return new Matrix4d(
            (double) MAT_M00.get(segment, base), (double) MAT_M01.get(segment, base),
            (double) MAT_M10.get(segment, base), (double) MAT_M11.get(segment, base),
            (double) MAT_M20.get(segment, base), (double) MAT_M21.get(segment, base),
            (double) MAT_M30.get(segment, base), (double) MAT_M31.get(segment, base),
            (double) MAT_M02.get(segment, base), (double) MAT_M03.get(segment, base),
            (double) MAT_M12.get(segment, base), (double) MAT_M13.get(segment, base),
            (double) MAT_M22.get(segment, base), (double) MAT_M23.get(segment, base),
            (double) MAT_M32.get(segment, base), (double) MAT_M33.get(segment, base)
        );
    }

    // ── Bulk copy ───────────────────────────────────────────────────────

    public static void copyVec4Array(MemorySegment src, long srcIndex, MemorySegment dst, long dstIndex, long count) {
        long size = count * 32;
        MemorySegment.copy(src, srcIndex * 32, dst, dstIndex * 32, size);
    }

    public static void copyVec3Array(MemorySegment src, long srcIndex, MemorySegment dst, long dstIndex, long count) {
        long size = count * 24;
        MemorySegment.copy(src, srcIndex * 24, dst, dstIndex * 24, size);
    }

    public static void copyMatrix4dArray(MemorySegment src, long srcIndex, MemorySegment dst, long dstIndex, long count) {
        long size = count * 128;
        MemorySegment.copy(src, srcIndex * 128, dst, dstIndex * 128, size);
    }

    public static void writeVec4Array(MemorySegment segment, long offset, Vector4d[] vecs) {
        for (int i = 0; i < vecs.length; i++) {
            setVec4(segment, offset + i, vecs[i]);
        }
    }

    public static void readVec4Array(MemorySegment segment, long offset, Vector4d[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = getVec4(segment, offset + i);
        }
    }
}

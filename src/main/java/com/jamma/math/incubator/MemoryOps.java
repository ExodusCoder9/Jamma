package com.jamma.math.incubator;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public final class MemoryOps {

    private static final ValueLayout.OfDouble DOUBLE_0 = ValueLayout.JAVA_DOUBLE;
    private static final ValueLayout.OfDouble DOUBLE_1 = ValueLayout.JAVA_DOUBLE;
    private static final ValueLayout.OfDouble DOUBLE_2 = ValueLayout.JAVA_DOUBLE;
    private static final ValueLayout.OfDouble DOUBLE_3 = ValueLayout.JAVA_DOUBLE;

    private static final MemoryLayout VEC4_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("x"),
        ValueLayout.JAVA_DOUBLE.withName("y"),
        ValueLayout.JAVA_DOUBLE.withName("z"),
        ValueLayout.JAVA_DOUBLE.withName("w")
    );

    private static final VarHandle VEC4_X = VEC4_LAYOUT.varHandle(
        MemoryLayout.PathElement.groupElement("x")
    );
    private static final VarHandle VEC4_Y = VEC4_LAYOUT.varHandle(
        MemoryLayout.PathElement.groupElement("y")
    );
    private static final VarHandle VEC4_Z = VEC4_LAYOUT.varHandle(
        MemoryLayout.PathElement.groupElement("z")
    );
    private static final VarHandle VEC4_W = VEC4_LAYOUT.varHandle(
        MemoryLayout.PathElement.groupElement("w")
    );

    private MemoryOps() {
    }

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
}

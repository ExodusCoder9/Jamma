package com.jamma.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import com.jamma.math.matrix.Matrix3d;
import com.jamma.math.matrix.Matrix4d;

class FfmIntegrationTest {

    @Test
    void testMatrix4dFfm() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(128); // 16 doubles = 128 bytes
            Matrix4d m = new Matrix4d(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
            );
            m.get(segment, 0);

            Matrix4d copy = new Matrix4d().zero();
            copy.set(segment, 0);

            assertEquals(m, copy);
        }
    }

    @Test
    void testMatrix3dFfmTightlyPacked() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(72); // 9 doubles = 72 bytes
            Matrix3d m = new Matrix3d().m00(1).m01(2).m02(3).m10(4).m11(5).m12(6).m20(7).m21(8).m22(9);
            m.get(segment, 0);

            Matrix3d copy = new Matrix3d().zero();
            copy.set(segment, 0);

            assertEquals(m.m00(), copy.m00());
            assertEquals(m.m11(), copy.m11());
            assertEquals(m.m22(), copy.m22());
            assertEquals(m.m12(), copy.m12());
        }
    }

    @Test
    void testMatrix3dFfVulkanAligned() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(96); // 3 rows * 4 doubles = 96 bytes (std140/std430 Vulkan layout)
            Matrix3d m = new Matrix3d().m00(1).m01(2).m02(3).m10(4).m11(5).m12(6).m20(7).m21(8).m22(9);
            m.getVulkan(segment, 0);

            Matrix3d copy = new Matrix3d().zero();
            copy.setVulkan(segment, 0);

            assertEquals(m.m00(), copy.m00());
            assertEquals(m.m11(), copy.m11());
            assertEquals(m.m22(), copy.m22());
            assertEquals(m.m12(), copy.m12());
        }
    }

    @Test
    void testVectorsFfm() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(32); // 4 doubles = 32 bytes

            Vector2d v2 = new Vector2d(1.5, 2.5);
            v2.writeToMemorySegment(segment, 0);
            Vector2d copy2 = Vector2d.fromMemorySegment(segment, 0);
            assertEquals(v2, copy2);

            Vector3d v3 = new Vector3d(1.1, 2.2, 3.3);
            v3.writeToMemorySegment(segment, 0);
            Vector3d copy3 = Vector3d.fromMemorySegment(segment, 0);
            assertEquals(v3, copy3);

            Vector4d v4 = new Vector4d(1.0, 2.0, 3.0, 4.0);
            v4.writeToMemorySegment(segment, 0);
            Vector4d copy4 = Vector4d.fromMemorySegment(segment, 0);
            assertEquals(v4, copy4);
        }
    }
}

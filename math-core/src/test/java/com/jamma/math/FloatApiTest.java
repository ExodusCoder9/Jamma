package com.jamma.math;

import com.jamma.math.matrix.Matrix3f;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;
import org.junit.jupiter.api.Test;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;
import static org.junit.jupiter.api.Assertions.*;

public class FloatApiTest {

    @Test
    public void testVector2f() {
        Vector2f v = new Vector2f(1.0f, 2.0f);
        assertEquals(1.0f, v.x());
        assertEquals(2.0f, v.y());

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(8);
            v.writeToMemorySegment(segment, 0);
            Vector2f v2 = Vector2f.fromMemorySegment(segment, 0);
            assertEquals(v, v2);
        }
    }

    @Test
    public void testVector3f() {
        Vector3f v = new Vector3f(1.0f, 2.0f, 3.0f);
        assertEquals(1.0f, v.x());
        assertEquals(2.0f, v.y());
        assertEquals(3.0f, v.z());

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(12);
            v.writeToMemorySegment(segment, 0);
            Vector3f v2 = Vector3f.fromMemorySegment(segment, 0);
            assertEquals(v, v2);
        }
    }

    @Test
    public void testVector4f() {
        Vector4f v = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(1.0f, v.x());
        assertEquals(2.0f, v.y());
        assertEquals(3.0f, v.z());
        assertEquals(4.0f, v.w());

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(16);
            v.writeToMemorySegment(segment, 0);
            Vector4f v2 = Vector4f.fromMemorySegment(segment, 0);
            assertEquals(v, v2);
        }
    }

    @Test
    public void testMatrix3fVulkanAlignment() {
        Matrix3f m = new Matrix3f();
        m.m00(1.0f).m01(2.0f).m02(3.0f)
         .m10(4.0f).m11(5.0f).m12(6.0f)
         .m20(7.0f).m21(8.0f).m22(9.0f);

        try (Arena arena = Arena.ofConfined()) {
            // Vulkan layout requires 16-byte alignment per row for std140/std430, so 48 bytes total
            MemorySegment segment = arena.allocate(48);
            m.getVulkan(segment, 0);

            // Row 0 starts at offset 0: m00, m01, m02, plus 4 bytes padding
            assertEquals(1.0f, segment.get(ValueLayout.JAVA_FLOAT, 0));
            assertEquals(2.0f, segment.get(ValueLayout.JAVA_FLOAT, 4));
            assertEquals(3.0f, segment.get(ValueLayout.JAVA_FLOAT, 8));

            // Row 1 starts at offset 16: m10, m11, m12, plus 4 bytes padding
            assertEquals(4.0f, segment.get(ValueLayout.JAVA_FLOAT, 16));
            assertEquals(5.0f, segment.get(ValueLayout.JAVA_FLOAT, 20));
            assertEquals(6.0f, segment.get(ValueLayout.JAVA_FLOAT, 24));

            // Row 2 starts at offset 32: m20, m21, m22, plus 4 bytes padding
            assertEquals(7.0f, segment.get(ValueLayout.JAVA_FLOAT, 32));
            assertEquals(8.0f, segment.get(ValueLayout.JAVA_FLOAT, 36));
            assertEquals(9.0f, segment.get(ValueLayout.JAVA_FLOAT, 40));

            // Restore from segment using setVulkan
            Matrix3f restored = new Matrix3f().setVulkan(segment, 0);
            assertEquals(m, restored);
        }
    }

    @Test
    public void testMatrix4fBuffer() {
        Matrix4f m = new Matrix4f();
        m.m00(1.0f).m01(2.0f).m02(3.0f).m03(4.0f)
         .m10(5.0f).m11(6.0f).m12(7.0f).m13(8.0f)
         .m20(9.0f).m21(10.0f).m22(11.0f).m23(12.0f)
         .m30(13.0f).m31(14.0f).m32(15.0f).m33(16.0f);

        FloatBuffer buffer = FloatBuffer.allocate(16);
        m.writeToBuffer(buffer);
        buffer.flip();

        Matrix4f m2 = Matrix4f.fromBuffer(buffer);
        assertEquals(m.m00(), m2.m00());
        assertEquals(m.m33(), m2.m33());
    }

    @Test
    public void testQuaternionfSlerpAndSquad() {
        Quaternionf q1 = Quaternionf.identity();
        Quaternionf q2 = Quaternionf.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f), (float) Math.toRadians(90.0));

        // Test slerp half way
        Quaternionf half = q1.slerp(q2, 0.5f);
        Vector3f angles = half.getEulerAnglesXYZ();
        assertEquals(0.0f, angles.x(), 1e-4f);
        assertEquals(45.0f, (float) Math.toDegrees(angles.y()), 1e-2f);
        assertEquals(0.0f, angles.z(), 1e-4f);

        // Test squad
        Quaternionf q0 = Quaternionf.identity();
        Quaternionf qa = Quaternionf.fromAxisAngle(new Vector3f(1.0f, 0.0f, 0.0f), (float) Math.toRadians(30.0));
        Quaternionf qb = Quaternionf.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f), (float) Math.toRadians(30.0));
        Quaternionf q3 = Quaternionf.fromAxisAngle(new Vector3f(0.0f, 0.0f, 1.0f), (float) Math.toRadians(30.0));
        
        Quaternionf res = q0.squad(qa, qb, q3, 0.5f);
        assertNotNull(res);
    }
}

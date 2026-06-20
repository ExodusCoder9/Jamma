package com.jamma.math;

import org.junit.jupiter.api.Test;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.IntBuffer;
import static org.junit.jupiter.api.Assertions.*;

public class IntegerApiTest {

    @Test
    public void testVector2i() {
        Vector2i v1 = new Vector2i(1, 2);
        Vector2i v2 = new Vector2i(3, 4);

        assertEquals(new Vector2i(4, 6), v1.add(v2));
        assertEquals(new Vector2i(-2, -2), v1.sub(v2));
        assertEquals(new Vector2i(2, 4), v1.mul(2));
        assertEquals(new Vector2i(0, 1), v1.div(2));
        assertEquals(5, v1.lengthSquared());
        assertEquals(Math.sqrt(5), v1.length());
        assertEquals(8, v1.distanceSquared(v2));
        assertEquals(Math.sqrt(8), v1.distance(v2));

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(8);
            v1.writeToMemorySegment(segment, 0);
            Vector2i restored = Vector2i.fromMemorySegment(segment, 0);
            assertEquals(v1, restored);
        }

        IntBuffer buffer = IntBuffer.allocate(2);
        v1.writeToBuffer(buffer);
        buffer.flip();
        Vector2i restoredBuf = Vector2i.fromBuffer(buffer);
        assertEquals(v1, restoredBuf);
    }

    @Test
    public void testVector3i() {
        Vector3i v1 = new Vector3i(1, 2, 3);
        Vector3i v2 = new Vector3i(4, 5, 6);

        assertEquals(new Vector3i(5, 7, 9), v1.add(v2));
        assertEquals(new Vector3i(-3, -3, -3), v1.sub(v2));
        assertEquals(new Vector3i(3, 6, 9), v1.mul(3));
        assertEquals(14, v1.lengthSquared());

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(12);
            v1.writeToMemorySegment(segment, 0);
            Vector3i restored = Vector3i.fromMemorySegment(segment, 0);
            assertEquals(v1, restored);
        }

        IntBuffer buffer = IntBuffer.allocate(3);
        v1.writeToBuffer(buffer);
        buffer.flip();
        Vector3i restoredBuf = Vector3i.fromBuffer(buffer);
        assertEquals(v1, restoredBuf);
    }

    @Test
    public void testVector4i() {
        Vector4i v1 = new Vector4i(1, 2, 3, 4);
        Vector4i v2 = new Vector4i(5, 6, 7, 8);

        assertEquals(new Vector4i(6, 8, 10, 12), v1.add(v2));
        assertEquals(new Vector4i(-4, -4, -4, -4), v1.sub(v2));
        assertEquals(new Vector4i(2, 4, 6, 8), v1.mul(2));
        assertEquals(30, v1.lengthSquared());

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(16);
            v1.writeToMemorySegment(segment, 0);
            Vector4i restored = Vector4i.fromMemorySegment(segment, 0);
            assertEquals(v1, restored);
        }

        IntBuffer buffer = IntBuffer.allocate(4);
        v1.writeToBuffer(buffer);
        buffer.flip();
        Vector4i restoredBuf = Vector4i.fromBuffer(buffer);
        assertEquals(v1, restoredBuf);
    }
}

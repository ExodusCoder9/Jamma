package com.jamma.joml;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JomlAdapterTest {

    static final float F = 1e-5f;

    // ── Vector3f ────────────────────────────────────────────────────────

    @Test void v3fConstruct() {
        var v = new Vector3f(1, 2, 3);
        assertEquals(1, v.x, F);
        assertEquals(2, v.y, F);
        assertEquals(3, v.z, F);
    }

    @Test void v3fAdd() {
        var v = new Vector3f(1, 2, 3).add(new Vector3f(4, 5, 6));
        assertEquals(5, v.x, F);
        assertEquals(7, v.y, F);
        assertEquals(9, v.z, F);
    }

    @Test void v3fSub() {
        var v = new Vector3f(5, 7, 9).sub(new Vector3f(1, 2, 3));
        assertEquals(4, v.x, F);
        assertEquals(5, v.y, F);
        assertEquals(6, v.z, F);
    }

    @Test void v3fMul() {
        var v = new Vector3f(2, 3, 4).mul(2);
        assertEquals(4, v.x, F);
        assertEquals(6, v.y, F);
        assertEquals(8, v.z, F);
    }

    @Test void v3fDiv() {
        var v = new Vector3f(6, 9, 12).div(3);
        assertEquals(2, v.x, F);
        assertEquals(3, v.y, F);
        assertEquals(4, v.z, F);
    }

    @Test void v3fNormalize() {
        var v = new Vector3f(3, 0, 0).normalize();
        assertEquals(1, v.x, F);
        assertEquals(0, v.y, F);
        assertEquals(0, v.z, F);
        assertEquals(1, v.length(), F);
    }

    @Test void v3fLength() {
        assertEquals(5, new Vector3f(3, 4, 0).length(), F);
        assertEquals(25, new Vector3f(3, 4, 0).lengthSquared(), F);
    }

    @Test void v3fDot() {
        float d = new Vector3f(1, 2, 3).dot(new Vector3f(4, 5, 6));
        assertEquals(32, d, F);
    }

    @Test void v3fCross() {
        var v = new Vector3f(1, 0, 0).cross(new Vector3f(0, 1, 0));
        assertEquals(0, v.x, F);
        assertEquals(0, v.y, F);
        assertEquals(1, v.z, F);
    }

    @Test void v3fNegate() {
        var v = new Vector3f(1, -2, 3).negate();
        assertEquals(-1, v.x, F);
        assertEquals(2, v.y, F);
        assertEquals(-3, v.z, F);
    }

    @Test void v3fLerp() {
        var v = new Vector3f(0, 0, 0).lerp(new Vector3f(10, 20, 30), 0.5f);
        assertEquals(5, v.x, F);
        assertEquals(10, v.y, F);
        assertEquals(15, v.z, F);
    }

    @Test void v3fToJamma() {
        var adapted = new Vector3f(1, 2, 3);
        var jamma = adapted.toJamma();
        assertEquals(1, jamma.x(), F);
        assertEquals(2, jamma.y(), F);
        assertEquals(3, jamma.z(), F);
    }

    @Test void v3fFromJamma() {
        var jamma = new com.jamma.math.Vector3f(4, 5, 6);
        var adapted = Vector3f.fromJamma(jamma);
        assertEquals(4, adapted.x, F);
        assertEquals(5, adapted.y, F);
        assertEquals(6, adapted.z, F);
    }

    // ── Vector3d ────────────────────────────────────────────────────────

    @Test void v3dConstruct() {
        var v = new Vector3d(1, 2, 3);
        assertEquals(1, v.x, 1e-12);
        assertEquals(2, v.y, 1e-12);
        assertEquals(3, v.z, 1e-12);
    }

    @Test void v3dNormalize() {
        var v = new Vector3d(3, 0, 0).normalize();
        assertEquals(1, v.x, 1e-12);
        assertEquals(0, v.y, 1e-12);
        assertEquals(0, v.z, 1e-12);
    }

    @Test void v3dDot() {
        double d = new Vector3d(1, 2, 3).dot(new Vector3d(4, 5, 6));
        assertEquals(32, d, 1e-12);
    }

    // ── Vector4f ────────────────────────────────────────────────────────

    @Test void v4fAdd() {
        var v = new Vector4f(1, 2, 3, 4).add(new Vector4f(5, 6, 7, 8));
        assertEquals(6, v.x, F);
        assertEquals(8, v.y, F);
        assertEquals(10, v.z, F);
        assertEquals(12, v.w, F);
    }

    @Test void v4fNormalize() {
        var v = new Vector4f(2, 0, 0, 0).normalize();
        assertEquals(1, v.x, F);
        assertEquals(0, v.y, F);
        assertEquals(0, v.z, F);
        assertEquals(0, v.w, F);
    }

    // ── Vector2f ────────────────────────────────────────────────────────

    @Test void v2fAdd() {
        var v = new Vector2f(1, 2).add(new Vector2f(3, 4));
        assertEquals(4, v.x, F);
        assertEquals(6, v.y, F);
    }

    @Test void v2fRotate() {
        var v = new Vector2f(1, 0).rotate((float) Math.PI / 2);
        assertEquals(0, v.x, F);
        assertEquals(1, v.y, 1e-4f);
    }

    @Test void v2fCross() {
        float c = new Vector2f(1, 0).cross(new Vector2f(0, 1));
        assertEquals(1, c, F);
    }

    // ── Vector2d ────────────────────────────────────────────────────────

    @Test void v2dAdd() {
        var v = new Vector2d(1, 2).add(new Vector2d(3, 4));
        assertEquals(4, v.x, 1e-12);
        assertEquals(6, v.y, 1e-12);
    }

    // ── Quaternionf ─────────────────────────────────────────────────────

    @Test void quatIdentity() {
        var q = new Quaternionf();
        assertEquals(0, q.x, F);
        assertEquals(0, q.y, F);
        assertEquals(0, q.z, F);
        assertEquals(1, q.w, F);
    }

    @Test void quatNormalize() {
        var q = new Quaternionf(2, 0, 0, 0).normalize();
        assertEquals(1, q.length(), F);
    }

    @Test void quatMul() {
        // (1,0,0,0) * (0,1,0,0) = (0,0,1,0) — cross product of basis vectors
        var q = new Quaternionf(1, 0, 0, 0).mul(new Quaternionf(0, 1, 0, 0));
        assertEquals(0, q.x, F);
        assertEquals(0, q.y, F);
        assertEquals(1, q.z, F);
        assertEquals(0, q.w, F);
    }

    @Test void quatToJamma() {
        var adapted = new Quaternionf(0, 0, 0, 1);
        var jamma = adapted.toJamma();
        assertEquals(0, jamma.x(), F);
        assertEquals(0, jamma.y(), F);
        assertEquals(0, jamma.z(), F);
        assertEquals(1, jamma.w(), F);
    }

    // ── Matrix4f ────────────────────────────────────────────────────────

    @Test void mat4Identity() {
        var m = new Matrix4f();
        assertEquals(1, m.m00, F);
        assertEquals(1, m.m11, F);
        assertEquals(1, m.m22, F);
        assertEquals(1, m.m33, F);
        assertEquals(0, m.m01, F);
    }

    @Test void mat4Mul() {
        var a = new Matrix4f().translate(1, 2, 3);
        var b = new Matrix4f().scale(2);
        // a.mul(b) = T * S: first scale, then translate
        // translation column stays (1,2,3) since scale has identity in col 3
        a.mul(b);
        assertEquals(1, a.m03, 1e-4f);
        assertEquals(2, a.m13, 1e-4f);
        assertEquals(3, a.m23, 1e-4f);
        assertEquals(2, a.m00, 1e-4f); // scaling applied
    }

    @Test void mat4Perspective() {
        var m = new Matrix4f().perspective(1.57f, 16f/9f, 0.1f, 100f);
        // perspective projection should not be singular
        assertNotEquals(0, m.determinant(), 1e-6f);
        // near plane maps to -1, far plane to +1
        var v = new Vector4f(0, 0, -0.1f, 1);
        m.transform(v);
        assertTrue(v.z < 0); // after perspective divide, near = -1
    }

    @Test void mat4ToJamma() {
        var adapted = new Matrix4f();
        adapted.translate(5, 10, 15);
        var jamma = adapted.toJamma();
        assertEquals(5, jamma.m03, 1e-4f);
        assertEquals(10, jamma.m13, 1e-4f);
        assertEquals(15, jamma.m23, 1e-4f);
    }

    // ── Vector3f memory segment ─────────────────────────────────────────

    @Test void v3fMemorySegment() {
        try (var arena = java.lang.foreign.Arena.ofConfined()) {
            var seg = arena.allocate(12);
            new Vector3f(1, 2, 3).store(seg, 0);
            var loaded = new Vector3f().load(seg, 0);
            assertEquals(1, loaded.x, F);
            assertEquals(2, loaded.y, F);
            assertEquals(3, loaded.z, F);
        }
    }
}

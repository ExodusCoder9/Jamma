package com.jamma.math.matrix;

import com.jamma.math.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.PI;

class Matrix4fTest {

    @Test void identityFactory() {
        Matrix4f a = Matrix4f.identityMatrix();
        Matrix4f b = Matrix4f.identityMatrix();
        assertNotSame(a, b);
        assertTrue(a.isIdentity());
        assertTrue(b.isIdentity());
    }

    @Test void rotateZeroAxisNoop() {
        Matrix4f m = new Matrix4f();
        Matrix4f original = new Matrix4f(m);
        m.rotate((float) (PI / 2), 0.0f, 0.0f, 0.0f);
        assertEquals(original, m);
    }

    @Test void translateAndTransform() {
        Vector3f p = new Matrix4f().translate(3, 4, 5).transformPosition(new Vector3f(1, 2, 3));
        assertEquals(new Vector3f(4, 6, 8), p);
    }
}

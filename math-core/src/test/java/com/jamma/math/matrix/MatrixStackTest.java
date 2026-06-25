package com.jamma.math.matrix;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jamma.math.Vector3f;

class MatrixStackTest {

    static final float eps = 1e-5f;

    @Test void newStack_sizeOne() { assertEquals(1, new MatrixStack().size()); }

    @Test void newStack_isIdentity() {
        assertTrue(new MatrixStack().peek().isIdentity());
    }

    @Test void push_duplicatesAndIncreasesSize() {
        MatrixStack s = new MatrixStack();
        s.push();
        assertEquals(2, s.size());
        assertTrue(s.peek().isIdentity());
    }

    @Test void pop_returnsAndDecreasesSize() {
        MatrixStack s = new MatrixStack();
        s.push();
        assertEquals(2, s.size());
        Matrix4f popped = s.pop();
        assertTrue(popped.isIdentity());
        assertEquals(1, s.size());
    }

    @Test void pop_underflow() {
        MatrixStack s = new MatrixStack();
        s.pop();
        assertThrows(java.util.EmptyStackException.class, () -> s.pop());
    }

    @Test void translate_modifiesTop() {
        MatrixStack s = new MatrixStack();
        s.translate(3, 4, 5);
        Vector3f r = s.peek().transformPosition(new Vector3f(0, 0, 0));
        assertEquals(3.0f, r.x(), eps);
        assertEquals(4.0f, r.y(), eps);
        assertEquals(5.0f, r.z(), eps);
    }

    @Test void rotate_modifiesTop() {
        MatrixStack s = new MatrixStack();
        s.rotate((float) java.lang.Math.PI / 2, 0, 0, 1);
        Vector3f r = s.peek().transformPosition(new Vector3f(1, 0, 0));
        assertEquals(1.0f, r.y(), eps);
    }

    @Test void scale_modifiesTop() {
        MatrixStack s = new MatrixStack();
        s.scale(2, 3, 4);
        Vector3f r = s.peek().transformPosition(new Vector3f(1, 1, 1));
        assertEquals(2.0f, r.x(), eps);
        assertEquals(3.0f, r.y(), eps);
        assertEquals(4.0f, r.z(), eps);
    }

    @Test void clear_resetsToSingleIdentity() {
        MatrixStack s = new MatrixStack();
        s.translate(10, 20, 30);
        s.push();
        s.scale(2, 2, 2);
        s.clear();
        assertEquals(1, s.size());
        assertTrue(s.peek().isIdentity());
    }

    @Test void mul_multipliesTop() {
        MatrixStack s = new MatrixStack();
        s.translate(5, 0, 0);
        s.mul(new Matrix4f().scale(2, 2, 2));
        Vector3f r = s.peek().transformPosition(new Vector3f(0, 0, 0));
        assertEquals(5.0f, r.x(), eps);
    }

    @Test void identity_resetsTop() {
        MatrixStack s = new MatrixStack();
        s.translate(10, 0, 0);
        s.identity();
        assertTrue(s.peek().isIdentity());
    }
}

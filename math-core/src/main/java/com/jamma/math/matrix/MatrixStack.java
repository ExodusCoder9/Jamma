package com.jamma.math.matrix;

import com.jamma.math.Vector3f;
import com.jamma.math.quaternion.Quaternionf;
import java.util.ArrayDeque;
import java.util.EmptyStackException;

public class MatrixStack {

    private final ArrayDeque<Matrix4f> stack;

    public MatrixStack() {
        stack = new ArrayDeque<>();
        stack.push(new Matrix4f());
    }

    @SuppressWarnings("unused")
    public MatrixStack(Matrix4f initial) {
        stack = new ArrayDeque<>();
        stack.push(new Matrix4f(initial));
    }

    public void push() {
        stack.push(new Matrix4f(peek()));
    }

    public Matrix4f pop() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }

    public Matrix4f peek() {
        Matrix4f top = stack.peek();
        if (top == null) {
            throw new EmptyStackException();
        }
        return top;
    }

    public MatrixStack mul(Matrix4f m) {
        peek().multiply(m);
        return this;
    }

    public MatrixStack translate(float x, float y, float z) {
        peek().translate(x, y, z);
        return this;
    }

    public MatrixStack translate(Vector3f offset) {
        peek().translate(offset);
        return this;
    }

    public MatrixStack rotate(float angle, float x, float y, float z) {
        peek().rotate(angle, x, y, z);
        return this;
    }

    public MatrixStack rotate(Quaternionf q) {
        peek().rotate(q);
        return this;
    }

    public MatrixStack scale(float x, float y, float z) {
        peek().scale(x, y, z);
        return this;
    }

    public MatrixStack scale(float factor) {
        peek().scale(factor);
        return this;
    }

    public MatrixStack scale(Vector3f xyz) {
        peek().scale(xyz);
        return this;
    }

    public MatrixStack identity() {
        peek().identity();
        return this;
    }

    public void clear() {
        stack.clear();
        stack.push(new Matrix4f());
    }

    public int size() {
        return stack.size();
    }
}
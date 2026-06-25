package com.jamma.math.matrix;

import com.jamma.math.Vector3f;
import com.jamma.math.Vector4f;
import com.jamma.math.quaternion.Quaternionf;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * A 4x4 column-major single-precision transformation matrix.
 * <p>
 * This is a mutable class for performance. Use the static methods in
 * {@link com.jamma.math.matrix.Matrix4f} for immutable-style operations.
 * <p>
 * Memory layout: 16 contiguous floats in column-major order
 * (m00, m10, m20, m30, m01, m11, …).
 */
public class Matrix4f implements Serializable {

    public static final Matrix4f IDENTITY = new Matrix4f();

    @Serial
    private static final long serialVersionUID = 1L;

    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Matrix4f() {
        identity();
    }

    public Matrix4f(Matrix4f other) {
        set(other);
    }

    public Matrix4f(float[] m) {
        set(m);
    }

    public Matrix4f(
        float m00, float m01, float m02, float m03,
        float m10, float m11, float m12, float m13,
        float m20, float m21, float m22, float m23,
        float m30, float m31, float m32, float m33
    ) {
        set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public Matrix4f set(Matrix4f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02; m03 = other.m03;
        m10 = other.m10; m11 = other.m11; m12 = other.m12; m13 = other.m13;
        m20 = other.m20; m21 = other.m21; m22 = other.m22; m23 = other.m23;
        m30 = other.m30; m31 = other.m31; m32 = other.m32; m33 = other.m33;
        return this;
    }

    public Matrix4f set(float[] m) {
        m00 = m[0];  m01 = m[1];  m02 = m[2];  m03 = m[3];
        m10 = m[4];  m11 = m[5];  m12 = m[6];  m13 = m[7];
        m20 = m[8];  m21 = m[9];  m22 = m[10]; m23 = m[11];
        m30 = m[12]; m31 = m[13]; m32 = m[14]; m33 = m[15];
        return this;
    }

    public Matrix4f set(
        float m00, float m01, float m02, float m03,
        float m10, float m11, float m12, float m13,
        float m20, float m21, float m22, float m23,
        float m30, float m31, float m32, float m33
    ) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
        return this;
    }

    public Matrix4f identity() {
        m00 = 1.0f; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = 1.0f; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 1.0f; m23 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f; m33 = 1.0f;
        return this;
    }

    public Matrix4f zero() {
        m00 = 0.0f; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = 0.0f; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 0.0f; m23 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f; m33 = 0.0f;
        return this;
    }

    public Matrix4f translate(float x, float y, float z) {
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        float t30 = m30; float t31 = m31; float t32 = m32; float t33 = m33;
        m30 = Math.fma(t00, x, Math.fma(t10, y, Math.fma(t20, z, t30)));
        m31 = Math.fma(t01, x, Math.fma(t11, y, Math.fma(t21, z, t31)));
        m32 = Math.fma(t02, x, Math.fma(t12, y, Math.fma(t22, z, t32)));
        m33 = Math.fma(t03, x, Math.fma(t13, y, Math.fma(t23, z, t33)));
        return this;
    }

    public Matrix4f translate(Vector3f offset) {
        return translate(offset.x(), offset.y(), offset.z());
    }

    public Vector3f getTranslation() {
        return new Vector3f(m30, m31, m32);
    }

    public Matrix4f setTranslation(float x, float y, float z) {
        m30 = x;
        m31 = y;
        m32 = z;
        return this;
    }

    public Matrix4f setTranslation(Vector3f v) {
        return setTranslation(v.x(), v.y(), v.z());
    }
    public Matrix4f rotate(float angle, float x, float y, float z) {
        float invLen = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);
        float nx = x * invLen;
        float ny = y * invLen;
        float nz = z * invLen;
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float t = 1.0f - c;
        float r00 = t * nx * nx + c;
        float r01 = t * nx * ny + s * nz;
        float r02 = t * nx * nz - s * ny;
        float r10 = t * nx * ny - s * nz;
        float r11 = t * ny * ny + c;
        float r12 = t * ny * nz + s * nx;
        float r20 = t * nx * nz + s * ny;
        float r21 = t * ny * nz - s * nx;
        float r22 = t * nz * nz + c;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        return this;
    }

    public Matrix4f rotate(Quaternionf q) {
        float x = q.x(), y = q.y(), z = q.z(), w = q.w();
        float r00 = 1.0f - 2.0f * (y * y + z * z);
        float r01 = 2.0f * (x * y + w * z);
        float r02 = 2.0f * (x * z - w * y);
        float r10 = 2.0f * (x * y - w * z);
        float r11 = 1.0f - 2.0f * (x * x + z * z);
        float r12 = 2.0f * (y * z + w * x);
        float r20 = 2.0f * (x * z + w * y);
        float r21 = 2.0f * (y * z - w * x);
        float r22 = 1.0f - 2.0f * (x * x + y * y);
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        return this;
    }

    public Matrix4f rotateXYZ(float angleX, float angleY, float angleZ) {
        float cx = (float) Math.cos(angleX), sx = (float) Math.sin(angleX);
        float cy = (float) Math.cos(angleY), sy = (float) Math.sin(angleY);
        float cz = (float) Math.cos(angleZ), sz = (float) Math.sin(angleZ);
        float r00 = cy * cz;
        float r01 = sx * sy * cz + cx * sz;
        float r02 = -cx * sy * cz + sx * sz;
        float r10 = -cy * sz;
        float r11 = -sx * sy * sz + cx * cz;
        float r12 = cx * sy * sz + sx * cz;
        float r20 = sy;
        float r21 = -sx * cy;
        float r22 = cx * cy;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        return this;
    }

    public Matrix4f rotateZYX(float angleZ, float angleY, float angleX) {
        float cz = (float) Math.cos(angleZ), sz = (float) Math.sin(angleZ);
        float cy = (float) Math.cos(angleY), sy = (float) Math.sin(angleY);
        float cx = (float) Math.cos(angleX), sx = (float) Math.sin(angleX);
        float r00 = cz * cy;
        float r01 = sz * cy;
        float r02 = -sy;
        float r10 = -sz * cx + cz * sy * sx;
        float r11 = cz * cx + sz * sy * sx;
        float r12 = cy * sx;
        float r20 = sz * sx + cz * sy * cx;
        float r21 = -cz * sx + sz * sy * cx;
        float r22 = cy * cx;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        return this;
    }

    public Matrix4f rotateX(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        m10 = t10 * c + m20 * s;
        m11 = t11 * c + m21 * s;
        m12 = t12 * c + m22 * s;
        m13 = t13 * c + m23 * s;
        m20 = t10 * -s + m20 * c;
        m21 = t11 * -s + m21 * c;
        m22 = t12 * -s + m22 * c;
        m23 = t13 * -s + m23 * c;
        return this;
    }

    public Matrix4f rotateY(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        m00 = t00 * c + m20 * -s;
        m01 = t01 * c + m21 * -s;
        m02 = t02 * c + m22 * -s;
        m03 = t03 * c + m23 * -s;
        m20 = t00 * s + m20 * c;
        m21 = t01 * s + m21 * c;
        m22 = t02 * s + m22 * c;
        m23 = t03 * s + m23 * c;
        return this;
    }

    public Matrix4f rotateZ(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        m00 = t00 * c + t10 * s;
        m01 = t01 * c + t11 * s;
        m02 = t02 * c + t12 * s;
        m03 = t03 * c + t13 * s;
        m10 = t10 * c - t00 * s;
        m11 = t11 * c - t01 * s;
        m12 = t12 * c - t02 * s;
        m13 = t13 * c - t03 * s;
        return this;
    }

    public Matrix4f scale(float x, float y, float z) {
        m00 *= x; m01 *= x; m02 *= x; m03 *= x;
        m10 *= y; m11 *= y; m12 *= y; m13 *= y;
        m20 *= z; m21 *= z; m22 *= z; m23 *= z;
        return this;
    }

    public Matrix4f scale(float factor) {
        return scale(factor, factor, factor);
    }

    public Matrix4f scale(Vector3f xyz) {
        return scale(xyz.x(), xyz.y(), xyz.z());
    }

    public Vector3f getScale() {
        float sx = (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        float sy = (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        float sz = (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        return new Vector3f(sx, sy, sz);
    }

    public Matrix4f reflect(float nx, float ny, float nz) {
        float invLen = 1.0f / (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        float nnx = nx * invLen;
        float nny = ny * invLen;
        float nnz = nz * invLen;
        float r00 = 1.0f - 2.0f * nnx * nnx;
        float r01 = -2.0f * nnx * nny;
        float r02 = -2.0f * nnx * nnz;
        float r10 = -2.0f * nny * nnx;
        float r11 = 1.0f - 2.0f * nny * nny;
        float r12 = -2.0f * nny * nnz;
        float r20 = -2.0f * nnz * nnx;
        float r21 = -2.0f * nnz * nny;
        float r22 = 1.0f - 2.0f * nnz * nnz;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        return this;
    }

    public Matrix4f reflect(Vector3f normal) {
        return reflect(normal.x(), normal.y(), normal.z());
    }

    public Matrix4f reflect(float nx, float ny, float nz, float d) {
        float invLen = 1.0f / (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        float nnx = nx * invLen;
        float nny = ny * invLen;
        float nnz = nz * invLen;
        float nd = d * invLen;
        float r00 = 1.0f - 2.0f * nnx * nnx;
        float r01 = -2.0f * nnx * nny;
        float r02 = -2.0f * nnx * nnz;
        float r03 = -2.0f * nnx * nd;
        float r10 = -2.0f * nny * nnx;
        float r11 = 1.0f - 2.0f * nny * nny;
        float r12 = -2.0f * nny * nnz;
        float r13 = -2.0f * nny * nd;
        float r20 = -2.0f * nnz * nnx;
        float r21 = -2.0f * nnz * nny;
        float r22 = 1.0f - 2.0f * nnz * nnz;
        float r23 = -2.0f * nnz * nd;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        float t30 = m30; float t31 = m31; float t32 = m32; float t33 = m33;
        m00 = t00 * r00 + t10 * r01 + t20 * r02 + t30 * r03;
        m01 = t01 * r00 + t11 * r01 + t21 * r02 + t31 * r03;
        m02 = t02 * r00 + t12 * r01 + t22 * r02 + t32 * r03;
        m03 = t03 * r00 + t13 * r01 + t23 * r02 + t33 * r03;
        m10 = t00 * r10 + t10 * r11 + t20 * r12 + t30 * r13;
        m11 = t01 * r10 + t11 * r11 + t21 * r12 + t31 * r13;
        m12 = t02 * r10 + t12 * r11 + t22 * r12 + t32 * r13;
        m13 = t03 * r10 + t13 * r11 + t23 * r12 + t33 * r13;
        m20 = t00 * r20 + t10 * r21 + t20 * r22 + t30 * r23;
        m21 = t01 * r20 + t11 * r21 + t21 * r22 + t31 * r23;
        m22 = t02 * r20 + t12 * r21 + t22 * r22 + t32 * r23;
        m23 = t03 * r20 + t13 * r21 + t23 * r22 + t33 * r23;
        m30 = Math.fma(t00, r03, Math.fma(t10, r13, Math.fma(t20, r23, t30)));
        m31 = Math.fma(t01, r03, Math.fma(t11, r13, Math.fma(t21, r23, t31)));
        m32 = Math.fma(t02, r03, Math.fma(t12, r13, Math.fma(t22, r23, t32)));
        m33 = Math.fma(t03, r03, Math.fma(t13, r13, Math.fma(t23, r23, t33)));
        return this;
    }

    public Matrix4f reflect(float x0, float y0, float z0, float x1, float y1, float z1) {
        float nx = x1 - x0;
        float ny = y1 - y0;
        float nz = z1 - z0;
        float d = -(nx * x0 + ny * y0 + nz * z0);
        return reflect(nx, ny, nz, d);
    }

    public Matrix4f reflect(Vector3f point, Vector3f normal) {
        return reflect(point.x(), point.y(), point.z(), normal.x(), normal.y(), normal.z());
    }

    public Matrix4f perspective(float fovY, float aspect, float zNear, float zFar, boolean zZeroToOne) {
        float f = 1.0f / (float) Math.tan(fovY * 0.5f);
        float invFn = 1.0f / (zNear - zFar);
        m00 = f / aspect;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0f * zFar * zNear * invFn;
        }
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f perspective(float fovY, float aspect, float zNear, float zFar) {
        return perspective(fovY, aspect, zNear, zFar, false);
    }

    public Matrix4f perspectiveVulkan(float fovYRad, float aspect, float zNear, float zFar) {
        float f = 1.0f / (float) Math.tan(fovYRad * 0.5f);
        float invFn = 1.0f / (zNear - zFar);
        m00 = f / aspect;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = -f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = zFar * invFn;
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = zFar * zNear * invFn;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f perspectiveRect(float width, float height, float zNear, float zFar, boolean zZeroToOne) {
        float invFn = 1.0f / (zNear - zFar);
        m00 = 2.0f * zNear / width;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * zNear / height;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0f * zFar * zNear * invFn;
        }
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f perspectiveRect(float width, float height, float zNear, float zFar) {
        return perspectiveRect(width, height, zNear, zFar, false);
    }

    public Matrix4f perspectiveOffCenter(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        float invRL = 1.0f / (right - left);
        float invTB = 1.0f / (top - bottom);
        float invFn = 1.0f / (zNear - zFar);
        m00 = 2.0f * zNear * invRL;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * zNear * invTB;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = (right + left) * invRL;
        m21 = (top + bottom) * invTB;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0f * zFar * zNear * invFn;
        }
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f perspectiveOffCenter(float left, float right, float bottom, float top, float zNear, float zFar) {
        return perspectiveOffCenter(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4f perspectiveOffCenterFov(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar, boolean zZeroToOne) {
        float tanLeft = (float) Math.tan(angleLeft);
        float tanRight = (float) Math.tan(angleRight);
        float tanDown = (float) Math.tan(angleDown);
        float tanUp = (float) Math.tan(angleUp);
        float invTanSumX = 1.0f / (tanRight + tanLeft);
        float invTanSumY = 1.0f / (tanUp + tanDown);
        float invFn = 1.0f / (zNear - zFar);
        m00 = 2.0f * invTanSumX;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * invTanSumY;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = (tanRight - tanLeft) * invTanSumX;
        m21 = (tanUp - tanDown) * invTanSumY;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0f * zFar * zNear * invFn;
        }
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f perspectiveOffCenterFov(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar) {
        return perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }

    public Matrix4f perspectiveLH(float fovY, float aspect, float zNear, float zFar, boolean zZeroToOne) {
        float f = 1.0f / (float) Math.tan(fovY * 0.5f);
        float invFn = 1.0f / (zFar - zNear);
        m00 = f / aspect;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = -zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = -2.0f * zFar * zNear * invFn;
        }
        m23 = 1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f perspectiveLH(float fovY, float aspect, float zNear, float zFar) {
        return perspectiveLH(fovY, aspect, zNear, zFar, false);
    }

    public Matrix4f perspectiveOffCenterFovLH(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar, boolean zZeroToOne) {
        float tanLeft = (float) Math.tan(angleLeft);
        float tanRight = (float) Math.tan(angleRight);
        float tanDown = (float) Math.tan(angleDown);
        float tanUp = (float) Math.tan(angleUp);
        float invTanSumX = 1.0f / (tanRight + tanLeft);
        float invTanSumY = 1.0f / (tanUp + tanDown);
        float invFn = 1.0f / (zFar - zNear);
        m00 = 2.0f * invTanSumX;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * invTanSumY;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = (tanRight - tanLeft) * invTanSumX;
        m21 = (tanUp - tanDown) * invTanSumY;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = -zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = -2.0f * zFar * zNear * invFn;
        }
        m23 = 1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f perspectiveOffCenterFovLH(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar) {
        return perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }

    public Matrix4f setPerspective(float fovY, float aspect, float zNear, float zFar, boolean zZeroToOne) {
        return perspective(fovY, aspect, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setPerspective(float fovY, float aspect, float zNear, float zFar) {
        return perspective(fovY, aspect, zNear, zFar);
    }

    public Matrix4f setPerspectiveRect(float width, float height, float zNear, float zFar, boolean zZeroToOne) {
        return perspectiveRect(width, height, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setPerspectiveRect(float width, float height, float zNear, float zFar) {
        return perspectiveRect(width, height, zNear, zFar);
    }

    public Matrix4f setPerspectiveOffCenter(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        return perspectiveOffCenter(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setPerspectiveOffCenter(float left, float right, float bottom, float top, float zNear, float zFar) {
        return perspectiveOffCenter(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4f setPerspectiveOffCenterFov(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar, boolean zZeroToOne) {
        return perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setPerspectiveOffCenterFov(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar) {
        return perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar);
    }

    public Matrix4f setPerspectiveOffCenterFovLH(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar, boolean zZeroToOne) {
        return perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setPerspectiveOffCenterFovLH(float angleLeft, float angleRight, float angleDown, float angleUp, float zNear, float zFar) {
        return perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar);
    }

    public Matrix4f setPerspectiveLH(float fovY, float aspect, float zNear, float zFar, boolean zZeroToOne) {
        return perspectiveLH(fovY, aspect, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setPerspectiveLH(float fovY, float aspect, float zNear, float zFar) {
        return perspectiveLH(fovY, aspect, zNear, zFar);
    }

    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        float invRL = 1.0f / (right - left);
        float invTB = 1.0f / (top - bottom);
        float invFN = 1.0f / (zFar - zNear);
        m00 = 2.0f * invRL;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * invTB;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        if (zZeroToOne) {
            m22 = -invFN;
            m32 = -zNear * invFN;
        } else {
            m22 = -2.0f * invFN;
            m32 = -(zFar + zNear) * invFN;
        }
        m23 = 0.0f;
        m30 = -(right + left) * invRL;
        m31 = -(top + bottom) * invTB;
        m33 = 1.0f;
        return this;
    }

    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
        return ortho(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4f orthoLH(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        float invRL = 1.0f / (right - left);
        float invTB = 1.0f / (top - bottom);
        float invFN = 1.0f / (zFar - zNear);
        m00 = 2.0f * invRL;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * invTB;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        if (zZeroToOne) {
            m22 = invFN;
            m32 = -zNear * invFN;
        } else {
            m22 = 2.0f * invFN;
            m32 = -(zFar + zNear) * invFN;
        }
        m23 = 0.0f;
        m30 = -(right + left) * invRL;
        m31 = -(top + bottom) * invTB;
        m33 = 1.0f;
        return this;
    }

    public Matrix4f orthoLH(float left, float right, float bottom, float top, float zNear, float zFar) {
        return orthoLH(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4f orthoSymmetric(float width, float height, float zNear, float zFar, boolean zZeroToOne) {
        return ortho(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, zNear, zFar, zZeroToOne);
    }

    public Matrix4f orthoSymmetric(float width, float height, float zNear, float zFar) {
        return orthoSymmetric(width, height, zNear, zFar, false);
    }

    public Matrix4f orthoSymmetricLH(float width, float height, float zNear, float zFar, boolean zZeroToOne) {
        return orthoLH(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, zNear, zFar, zZeroToOne);
    }

    public Matrix4f orthoSymmetricLH(float width, float height, float zNear, float zFar) {
        return orthoSymmetricLH(width, height, zNear, zFar, false);
    }

    public Matrix4f ortho2D(float left, float right, float bottom, float top) {
        return ortho(left, right, bottom, top, -1.0f, 1.0f);
    }

    public Matrix4f ortho2DLH(float left, float right, float bottom, float top) {
        return orthoLH(left, right, bottom, top, -1.0f, 1.0f);
    }

    public Matrix4f setOrtho(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        return ortho(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setOrtho(float left, float right, float bottom, float top, float zNear, float zFar) {
        return ortho(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4f setOrthoLH(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        return orthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setOrthoLH(float left, float right, float bottom, float top, float zNear, float zFar) {
        return orthoLH(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4f setOrthoSymmetric(float width, float height, float zNear, float zFar, boolean zZeroToOne) {
        return orthoSymmetric(width, height, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setOrthoSymmetric(float width, float height, float zNear, float zFar) {
        return orthoSymmetric(width, height, zNear, zFar);
    }

    public Matrix4f setOrthoSymmetricLH(float width, float height, float zNear, float zFar, boolean zZeroToOne) {
        return orthoSymmetricLH(width, height, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setOrthoSymmetricLH(float width, float height, float zNear, float zFar) {
        return orthoSymmetricLH(width, height, zNear, zFar);
    }

    public Matrix4f setOrtho2D(float left, float right, float bottom, float top) {
        return ortho2D(left, right, bottom, top);
    }

    public Matrix4f setOrtho2DLH(float left, float right, float bottom, float top) {
        return ortho2DLH(left, right, bottom, top);
    }

    public Matrix4f frustum(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        float invRL = 1.0f / (right - left);
        float invTB = 1.0f / (top - bottom);
        float invFN = 1.0f / (zFar - zNear);
        m00 = 2.0f * zNear * invRL;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * zNear * invTB;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = (right + left) * invRL;
        m21 = (top + bottom) * invTB;
        if (zZeroToOne) {
            m22 = -zFar * invFN;
            m32 = -zFar * zNear * invFN;
        } else {
            m22 = -(zFar + zNear) * invFN;
            m32 = -2.0f * zFar * zNear * invFN;
        }
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f frustum(float left, float right, float bottom, float top, float zNear, float zFar) {
        return frustum(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4f frustumLH(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        float invRL = 1.0f / (right - left);
        float invTB = 1.0f / (top - bottom);
        float invFN = 1.0f / (zFar - zNear);
        m00 = 2.0f * zNear * invRL;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 2.0f * zNear * invTB;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = (right + left) * invRL;
        m21 = (top + bottom) * invTB;
        if (zZeroToOne) {
            m22 = zFar * invFN;
            m32 = -zFar * zNear * invFN;
        } else {
            m22 = (zFar + zNear) * invFN;
            m32 = -2.0f * zFar * zNear * invFN;
        }
        m23 = 1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m33 = 0.0f;
        return this;
    }

    public Matrix4f frustumLH(float left, float right, float bottom, float top, float zNear, float zFar) {
        return frustumLH(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4f setFrustum(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        return frustum(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setFrustum(float left, float right, float bottom, float top, float zNear, float zFar) {
        return frustum(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4f setFrustumLH(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne) {
        return frustumLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4f setFrustumLH(float left, float right, float bottom, float top, float zNear, float zFar) {
        return frustumLH(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4f obliqueZ(float a, float b) {
        float t20 = Math.fma(a, m00, Math.fma(b, m01, m20));
        float t21 = Math.fma(a, m10, Math.fma(b, m11, m21));
        float t22 = Math.fma(a, m20, Math.fma(b, m21, m22));
        float t23 = Math.fma(a, m30, Math.fma(b, m31, m23));
        m20 = t20;
        m21 = t21;
        m22 = t22;
        m23 = t23;
        return this;
    }
    public Matrix4f lookAt(float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ) {
        float fX = centerX - eyeX;
        float fY = centerY - eyeY;
        float fZ = centerZ - eyeZ;
        float invFLen = 1.0f / (float) Math.sqrt(fX * fX + fY * fY + fZ * fZ);
        fX *= invFLen;
        fY *= invFLen;
        fZ *= invFLen;
        float sX = fY * upZ - fZ * upY;
        float sY = fZ * upX - fX * upZ;
        float sZ = fX * upY - fY * upX;
        float invSLen = 1.0f / (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        float uX = sY * fZ - sZ * fY;
        float uY = sZ * fX - sX * fZ;
        float uZ = sX * fY - sY * fX;
        m00 = sX;
        m01 = uX;
        m02 = -fX;
        m03 = 0.0f;
        m10 = sY;
        m11 = uY;
        m12 = -fY;
        m13 = 0.0f;
        m20 = sZ;
        m21 = uZ;
        m22 = -fZ;
        m23 = 0.0f;
        m30 = -(sX * eyeX + sY * eyeY + sZ * eyeZ);
        m31 = -(uX * eyeX + uY * eyeY + uZ * eyeZ);
        m32 = fX * eyeX + fY * eyeY + fZ * eyeZ;
        m33 = 1.0f;
        return this;
    }

    public Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
        return lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4f lookAtPerspective(float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ) {
        return lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4f lookAtLH(Vector3f eye, Vector3f center, Vector3f up) {
        return lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4f lookAtLH(float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ) {
        float fX = centerX - eyeX;
        float fY = centerY - eyeY;
        float fZ = centerZ - eyeZ;
        float invFLen = 1.0f / (float) Math.sqrt(fX * fX + fY * fY + fZ * fZ);
        fX *= invFLen;
        fY *= invFLen;
        fZ *= invFLen;
        float sX = fY * upZ - fZ * upY;
        float sY = fZ * upX - fX * upZ;
        float sZ = fX * upY - fY * upX;
        float invSLen = 1.0f / (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        float uX = sY * fZ - sZ * fY;
        float uY = sZ * fX - sX * fZ;
        float uZ = sX * fY - sY * fX;
        m00 = sX;
        m01 = uX;
        m02 = fX;
        m03 = 0.0f;
        m10 = sY;
        m11 = uY;
        m12 = fY;
        m13 = 0.0f;
        m20 = sZ;
        m21 = uZ;
        m22 = fZ;
        m23 = 0.0f;
        m30 = -(sX * eyeX + sY * eyeY + sZ * eyeZ);
        m31 = -(uX * eyeX + uY * eyeY + uZ * eyeZ);
        m32 = -(fX * eyeX + fY * eyeY + fZ * eyeZ);
        m33 = 1.0f;
        return this;
    }

    public Matrix4f lookAtPerspectiveLH(float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ) {
        return lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4f setLookAt(Vector3f eye, Vector3f center, Vector3f up) {
        return lookAt(eye, center, up);
    }

    public Matrix4f setLookAt(float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ) {
        return lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4f setLookAtLH(Vector3f eye, Vector3f center, Vector3f up) {
        return lookAtLH(eye, center, up);
    }

    public Matrix4f setLookAtLH(float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ) {
        return lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4f lookAlong(Vector3f dir, Vector3f up) {
        return lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }

    public Matrix4f setLookAlong(Vector3f dir, Vector3f up) {
        return lookAlong(dir, up);
    }

    public Matrix4f setLookAlong(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        return lookAlong(dirX, dirY, dirZ, upX, upY, upZ);
    }

    public Matrix4f withLookAtUp(Vector3f up) {
        return withLookAtUp(up.x(), up.y(), up.z());
    }

    public Matrix4f withLookAtUp(float upX, float upY, float upZ) {
        float fX = -m02;
        float fY = -m12;
        float fZ = -m22;
        float sX = fY * upZ - fZ * upY;
        float sY = fZ * upX - fX * upZ;
        float sZ = fX * upY - fY * upX;
        float invSLen = 1.0f / (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        float uX = sY * fZ - sZ * fY;
        float uY = sZ * fX - sX * fZ;
        float uZ = sX * fY - sY * fX;
        m00 = sX;
        m01 = uX;
        m02 = -fX;
        m03 = 0.0f;
        m10 = sY;
        m11 = uY;
        m12 = -fY;
        m13 = 0.0f;
        m20 = sZ;
        m21 = uZ;
        m22 = -fZ;
        m23 = 0.0f;
        return this;
    }

    public Matrix4f lookAlong(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        float invFLen = 1.0f / (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float fX = dirX * invFLen;
        float fY = dirY * invFLen;
        float fZ = dirZ * invFLen;
        float sX = fY * upZ - fZ * upY;
        float sY = fZ * upX - fX * upZ;
        float sZ = fX * upY - fY * upX;
        float invSLen = 1.0f / (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        float uX = sY * fZ - sZ * fY;
        float uY = sZ * fX - sX * fZ;
        float uZ = sX * fY - sY * fX;
        m00 = sX;
        m01 = uX;
        m02 = -fX;
        m03 = 0.0f;
        m10 = sY;
        m11 = uY;
        m12 = -fY;
        m13 = 0.0f;
        m20 = sZ;
        m21 = uZ;
        m22 = -fZ;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
        return this;
    }

    public Matrix4f multiply(Matrix4f other) {
        float n00 = Math.fma(m00, other.m00, Math.fma(m10, other.m01, Math.fma(m20, other.m02, m30 * other.m03)));
        float n01 = Math.fma(m01, other.m00, Math.fma(m11, other.m01, Math.fma(m21, other.m02, m31 * other.m03)));
        float n02 = Math.fma(m02, other.m00, Math.fma(m12, other.m01, Math.fma(m22, other.m02, m32 * other.m03)));
        float n03 = Math.fma(m03, other.m00, Math.fma(m13, other.m01, Math.fma(m23, other.m02, m33 * other.m03)));
        float n10 = Math.fma(m00, other.m10, Math.fma(m10, other.m11, Math.fma(m20, other.m12, m30 * other.m13)));
        float n11 = Math.fma(m01, other.m10, Math.fma(m11, other.m11, Math.fma(m21, other.m12, m31 * other.m13)));
        float n12 = Math.fma(m02, other.m10, Math.fma(m12, other.m11, Math.fma(m22, other.m12, m32 * other.m13)));
        float n13 = Math.fma(m03, other.m10, Math.fma(m13, other.m11, Math.fma(m23, other.m12, m33 * other.m13)));
        float n20 = Math.fma(m00, other.m20, Math.fma(m10, other.m21, Math.fma(m20, other.m22, m30 * other.m23)));
        float n21 = Math.fma(m01, other.m20, Math.fma(m11, other.m21, Math.fma(m21, other.m22, m31 * other.m23)));
        float n22 = Math.fma(m02, other.m20, Math.fma(m12, other.m21, Math.fma(m22, other.m22, m32 * other.m23)));
        float n23 = Math.fma(m03, other.m20, Math.fma(m13, other.m21, Math.fma(m23, other.m22, m33 * other.m23)));
        float n30 = Math.fma(m00, other.m30, Math.fma(m10, other.m31, Math.fma(m20, other.m32, m30 * other.m33)));
        float n31 = Math.fma(m01, other.m30, Math.fma(m11, other.m31, Math.fma(m21, other.m32, m31 * other.m33)));
        float n32 = Math.fma(m02, other.m30, Math.fma(m12, other.m31, Math.fma(m22, other.m32, m32 * other.m33)));
        float n33 = Math.fma(m03, other.m30, Math.fma(m13, other.m31, Math.fma(m23, other.m32, m33 * other.m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4f mulAffine(Matrix4f other) {
        float n00 = Math.fma(m00, other.m00, Math.fma(m10, other.m01, m20 * other.m02));
        float n01 = Math.fma(m01, other.m00, Math.fma(m11, other.m01, m21 * other.m02));
        float n02 = Math.fma(m02, other.m00, Math.fma(m12, other.m01, m22 * other.m02));
        float n03 = Math.fma(m03, other.m00, Math.fma(m13, other.m01, m23 * other.m02));
        float n10 = Math.fma(m00, other.m10, Math.fma(m10, other.m11, m20 * other.m12));
        float n11 = Math.fma(m01, other.m10, Math.fma(m11, other.m11, m21 * other.m12));
        float n12 = Math.fma(m02, other.m10, Math.fma(m12, other.m11, m22 * other.m12));
        float n13 = Math.fma(m03, other.m10, Math.fma(m13, other.m11, m23 * other.m12));
        float n20 = Math.fma(m00, other.m20, Math.fma(m10, other.m21, m20 * other.m22));
        float n21 = Math.fma(m01, other.m20, Math.fma(m11, other.m21, m21 * other.m22));
        float n22 = Math.fma(m02, other.m20, Math.fma(m12, other.m21, m22 * other.m22));
        float n23 = Math.fma(m03, other.m20, Math.fma(m13, other.m21, m23 * other.m22));
        float n30 = Math.fma(m00, other.m30, Math.fma(m10, other.m31, Math.fma(m20, other.m32, m30)));
        float n31 = Math.fma(m01, other.m30, Math.fma(m11, other.m31, Math.fma(m21, other.m32, m31)));
        float n32 = Math.fma(m02, other.m30, Math.fma(m12, other.m31, Math.fma(m22, other.m32, m32)));
        float n33 = Math.fma(m03, other.m30, Math.fma(m13, other.m31, Math.fma(m23, other.m32, m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4f add(Matrix4f other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02; m03 += other.m03;
        m10 += other.m10; m11 += other.m11; m12 += other.m12; m13 += other.m13;
        m20 += other.m20; m21 += other.m21; m22 += other.m22; m23 += other.m23;
        m30 += other.m30; m31 += other.m31; m32 += other.m32; m33 += other.m33;
        return this;
    }

    public Matrix4f sub(Matrix4f other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02; m03 -= other.m03;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12; m13 -= other.m13;
        m20 -= other.m20; m21 -= other.m21; m22 -= other.m22; m23 -= other.m23;
        m30 -= other.m30; m31 -= other.m31; m32 -= other.m32; m33 -= other.m33;
        return this;
    }

    public Matrix4f mulComponentWise(Matrix4f other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02; m03 *= other.m03;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12; m13 *= other.m13;
        m20 *= other.m20; m21 *= other.m21; m22 *= other.m22; m23 *= other.m23;
        m30 *= other.m30; m31 *= other.m31; m32 *= other.m32; m33 *= other.m33;
        return this;
    }

    public Matrix4f lerp(Matrix4f other, float t) {
        float t1 = 1.0f - t;
        m00 = m00 * t1 + other.m00 * t;
        m01 = m01 * t1 + other.m01 * t;
        m02 = m02 * t1 + other.m02 * t;
        m03 = m03 * t1 + other.m03 * t;
        m10 = m10 * t1 + other.m10 * t;
        m11 = m11 * t1 + other.m11 * t;
        m12 = m12 * t1 + other.m12 * t;
        m13 = m13 * t1 + other.m13 * t;
        m20 = m20 * t1 + other.m20 * t;
        m21 = m21 * t1 + other.m21 * t;
        m22 = m22 * t1 + other.m22 * t;
        m23 = m23 * t1 + other.m23 * t;
        m30 = m30 * t1 + other.m30 * t;
        m31 = m31 * t1 + other.m31 * t;
        m32 = m32 * t1 + other.m32 * t;
        m33 = m33 * t1 + other.m33 * t;
        return this;
    }

    public float determinant() {
        return (m00 * (m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31))
              - m01 * (m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30))
              + m02 * (m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30))
              - m03 * (m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30)));
    }
    public Matrix4f invert() {
        float det = determinant();
        if (det == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        float invDet = 1.0f / det;
        float n00 = (m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31)) * invDet;
        float n01 = -(m01 * (m22 * m33 - m23 * m32) - m02 * (m21 * m33 - m23 * m31) + m03 * (m21 * m32 - m22 * m31)) * invDet;
        float n02 = (m01 * (m12 * m33 - m13 * m32) - m02 * (m11 * m33 - m13 * m31) + m03 * (m11 * m32 - m12 * m31)) * invDet;
        float n03 = -(m01 * (m12 * m23 - m13 * m22) - m02 * (m11 * m23 - m13 * m21) + m03 * (m11 * m22 - m12 * m21)) * invDet;
        float n10 = -(m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30)) * invDet;
        float n11 = (m00 * (m22 * m33 - m23 * m32) - m02 * (m20 * m33 - m23 * m30) + m03 * (m20 * m32 - m22 * m30)) * invDet;
        float n12 = -(m00 * (m12 * m33 - m13 * m32) - m02 * (m10 * m33 - m13 * m30) + m03 * (m10 * m32 - m12 * m30)) * invDet;
        float n13 = (m00 * (m12 * m23 - m13 * m22) - m02 * (m10 * m23 - m13 * m20) + m03 * (m10 * m22 - m12 * m20)) * invDet;
        float n20 = (m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30)) * invDet;
        float n21 = -(m00 * (m21 * m33 - m23 * m31) - m01 * (m20 * m33 - m23 * m30) + m03 * (m20 * m31 - m21 * m30)) * invDet;
        float n22 = (m00 * (m11 * m33 - m13 * m31) - m01 * (m10 * m33 - m13 * m30) + m03 * (m10 * m31 - m11 * m30)) * invDet;
        float n23 = -(m00 * (m11 * m23 - m13 * m21) - m01 * (m10 * m23 - m13 * m20) + m03 * (m10 * m21 - m11 * m20)) * invDet;
        float n30 = -(m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30)) * invDet;
        float n31 = (m00 * (m21 * m32 - m22 * m31) - m01 * (m20 * m32 - m22 * m30) + m02 * (m20 * m31 - m21 * m30)) * invDet;
        float n32 = -(m00 * (m11 * m32 - m12 * m31) - m01 * (m10 * m32 - m12 * m30) + m02 * (m10 * m31 - m11 * m30)) * invDet;
        float n33 = (m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20)) * invDet;
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4f invertAffine() {
        float a00 = m00, a01 = m01, a02 = m02;
        float a10 = m10, a11 = m11, a12 = m12;
        float a20 = m20, a21 = m21, a22 = m22;
        float det3 = a00 * (a11 * a22 - a12 * a21)
                    - a01 * (a10 * a22 - a12 * a20)
                    + a02 * (a10 * a21 - a11 * a20);
        if (det3 == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        float invDet = 1.0f / det3;
        float n00 = (a11 * a22 - a12 * a21) * invDet;
        float n01 = (a02 * a21 - a01 * a22) * invDet;
        float n02 = (a01 * a12 - a02 * a11) * invDet;
        float n10 = (a12 * a20 - a10 * a22) * invDet;
        float n11 = (a00 * a22 - a02 * a20) * invDet;
        float n12 = (a02 * a10 - a00 * a12) * invDet;
        float n20 = (a10 * a21 - a11 * a20) * invDet;
        float n21 = (a01 * a20 - a00 * a21) * invDet;
        float n22 = (a00 * a11 - a01 * a10) * invDet;
        float tx = m30, ty = m31, tz = m32;
        m00 = n00; m01 = n01; m02 = n02; m03 = 0.0f;
        m10 = n10; m11 = n11; m12 = n12; m13 = 0.0f;
        m20 = n20; m21 = n21; m22 = n22; m23 = 0.0f;
        m30 = -(n00 * tx + n01 * ty + n02 * tz);
        m31 = -(n10 * tx + n11 * ty + n12 * tz);
        m32 = -(n20 * tx + n21 * ty + n22 * tz);
        m33 = 1.0f;
        return this;
    }

    public Matrix4f transpose() {
        float t;
        t = m01; m01 = m10; m10 = t;
        t = m02; m02 = m20; m20 = t;
        t = m03; m03 = m30; m30 = t;
        t = m12; m12 = m21; m21 = t;
        t = m13; m13 = m31; m31 = t;
        t = m23; m23 = m32; m32 = t;
        return this;
    }

    public Matrix4f transpose3x3() {
        float t;
        t = m01; m01 = m10; m10 = t;
        t = m02; m02 = m20; m20 = t;
        t = m12; m12 = m21; m21 = t;
        return this;
    }

    public Matrix4f adjugate() {
        float a00 = (m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31));
        float a01 = -(m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30));
        float a02 = (m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30));
        float a03 = -(m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30));
        float a10 = -(m01 * (m22 * m33 - m23 * m32) - m02 * (m21 * m33 - m23 * m31) + m03 * (m21 * m32 - m22 * m31));
        float a11 = (m00 * (m22 * m33 - m23 * m32) - m02 * (m20 * m33 - m23 * m30) + m03 * (m20 * m32 - m22 * m30));
        float a12 = -(m00 * (m21 * m33 - m23 * m31) - m01 * (m20 * m33 - m23 * m30) + m03 * (m20 * m31 - m21 * m30));
        float a13 = (m00 * (m21 * m32 - m22 * m31) - m01 * (m20 * m32 - m22 * m30) + m02 * (m20 * m31 - m21 * m30));
        float a20 = (m01 * (m12 * m33 - m13 * m32) - m02 * (m11 * m33 - m13 * m31) + m03 * (m11 * m32 - m12 * m31));
        float a21 = -(m00 * (m12 * m33 - m13 * m32) - m02 * (m10 * m33 - m13 * m30) + m03 * (m10 * m32 - m12 * m30));
        float a22 = (m00 * (m11 * m33 - m13 * m31) - m01 * (m10 * m33 - m13 * m30) + m03 * (m10 * m31 - m11 * m30));
        float a23 = -(m00 * (m11 * m32 - m12 * m31) - m01 * (m10 * m32 - m12 * m30) + m02 * (m10 * m31 - m11 * m30));
        float a30 = -(m01 * (m12 * m23 - m13 * m22) - m02 * (m11 * m23 - m13 * m21) + m03 * (m11 * m22 - m12 * m21));
        float a31 = (m00 * (m12 * m23 - m13 * m22) - m02 * (m10 * m23 - m13 * m20) + m03 * (m10 * m22 - m12 * m20));
        float a32 = -(m00 * (m11 * m23 - m13 * m21) - m01 * (m10 * m23 - m13 * m20) + m03 * (m10 * m21 - m11 * m20));
        float a33 = (m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20));
        m00 = a00; m01 = a10; m02 = a20; m03 = a30;
        m10 = a01; m11 = a11; m12 = a21; m13 = a31;
        m20 = a02; m21 = a12; m22 = a22; m23 = a32;
        m30 = a03; m31 = a13; m32 = a23; m33 = a33;
        return this;
    }

    public float trace() {
        return m00 + m11 + m22 + m33;
    }

    public Matrix4f normal() {
        float a00 = m00, a01 = m01, a02 = m02;
        float a10 = m10, a11 = m11, a12 = m12;
        float a20 = m20, a21 = m21, a22 = m22;
        float det3 = a00 * (a11 * a22 - a12 * a21)
                    - a01 * (a10 * a22 - a12 * a20)
                    + a02 * (a10 * a21 - a11 * a20);
        if (det3 == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot compute normal matrix");
        }
        float invDet = 1.0f / det3;
        float n00 = (a11 * a22 - a12 * a21) * invDet;
        float n01 = (a02 * a21 - a01 * a22) * invDet;
        float n02 = (a01 * a12 - a02 * a11) * invDet;
        float n10 = (a12 * a20 - a10 * a22) * invDet;
        float n11 = (a00 * a22 - a02 * a20) * invDet;
        float n12 = (a02 * a10 - a00 * a12) * invDet;
        float n20 = (a10 * a21 - a11 * a20) * invDet;
        float n21 = (a01 * a20 - a00 * a21) * invDet;
        float n22 = (a00 * a11 - a01 * a10) * invDet;
        m00 = n00; m01 = n10; m02 = n20; m03 = 0.0f;
        m10 = n01; m11 = n11; m12 = n21; m13 = 0.0f;
        m20 = n02; m21 = n12; m22 = n22; m23 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f; m33 = 1.0f;
        return this;
    }

    public Vector4f transform(Vector4f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30 * v.w())));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31 * v.w())));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32 * v.w())));
        float w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33 * v.w())));
        return new Vector4f(x, y, z, w);
    }

    public Vector3f transformPosition(Vector3f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3f(x, y, z);
    }

    public Vector3f transformDirection(Vector3f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z()));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z()));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()));
        return new Vector3f(x, y, z);
    }

    public Vector3f transformProject(Vector3f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        float w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33)));
        float invW = 1.0f / w;
        return new Vector3f(x * invW, y * invW, z * invW);
    }

    public static Matrix4f translation(float x, float y, float z) {
        return new Matrix4f().setTranslation(x, y, z);
    }

    public static Matrix4f rotationX(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        return new Matrix4f(
            1, 0, 0, 0,
            0, c, s, 0,
            0, -s, c, 0,
            0, 0, 0, 1
        );
    }

    public static Matrix4f rotationY(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        return new Matrix4f(
            c, 0, -s, 0,
            0, 1, 0, 0,
            s, 0, c, 0,
            0, 0, 0, 1
        );
    }

    public static Matrix4f rotationZ(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        return new Matrix4f(
            c, s, 0, 0,
            -s, c, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        );
    }

    public static Matrix4f scaling(float x, float y, float z) {
        return new Matrix4f(
            x, 0, 0, 0,
            0, y, 0, 0,
            0, 0, z, 0,
            0, 0, 0, 1
        );
    }

    public static Matrix4f reflection(float nx, float ny, float nz, float d) {
        float invLen = 1.0f / (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        float nnx = nx * invLen;
        float nny = ny * invLen;
        float nnz = nz * invLen;
        float da = -d * invLen;
        return new Matrix4f(
            1.0f - 2.0f * nnx * nnx, -2.0f * nny * nnx, -2.0f * nnz * nnx, 0.0f,
            -2.0f * nnx * nny, 1.0f - 2.0f * nny * nny, -2.0f * nnz * nny, 0.0f,
            -2.0f * nnx * nnz, -2.0f * nny * nnz, 1.0f - 2.0f * nnz * nnz, 0.0f,
            -2.0f * nnx * da, -2.0f * nny * da, -2.0f * nnz * da, 1.0f
        );
    }

    public static Matrix4f reflection(float x0, float y0, float z0, float x1, float y1, float z1) {
        float nx = x1 - x0;
        float ny = y1 - y0;
        float nz = z1 - z0;
        float d = -(nx * x0 + ny * y0 + nz * z0);
        return reflection(nx, ny, nz, d);
    }

    public static Matrix4f reflection(Vector3f point, Vector3f normal) {
        return reflection(point.x(), point.y(), point.z(), normal.x(), normal.y(), normal.z());
    }

    public static Matrix4f rotationTowards(Vector3f dir, Vector3f up) {
        return rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }

    public static Matrix4f rotationTowards(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        float invLen = 1.0f / (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float fX = dirX * invLen;
        float fY = dirY * invLen;
        float fZ = dirZ * invLen;
        float sX = fY * upZ - fZ * upY;
        float sY = fZ * upX - fX * upZ;
        float sZ = fX * upY - fY * upX;
        float invSLen = 1.0f / (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        float uX = sY * fZ - sZ * fY;
        float uY = sZ * fX - sX * fZ;
        float uZ = sX * fY - sY * fX;
        return new Matrix4f(
            sX, uX, -fX, 0.0f,
            sY, uY, -fY, 0.0f,
            sZ, uZ, -fZ, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        );
    }

    public float determinant3x3() {
        return (m00 * (m11 * m22 - m12 * m21)
              - m01 * (m10 * m22 - m12 * m20)
              + m02 * (m10 * m21 - m11 * m20));
    }

    public float get(int col, int row) {
        return switch (row * 4 + col) {
            case 0 -> m00; case 1 -> m01; case 2 -> m02; case 3 -> m03;
            case 4 -> m10; case 5 -> m11; case 6 -> m12; case 7 -> m13;
            case 8 -> m20; case 9 -> m21; case 10 -> m22; case 11 -> m23;
            case 12 -> m30; case 13 -> m31; case 14 -> m32; case 15 -> m33;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix4f set(int col, int row, float value) {
        switch (row * 4 + col) {
            case 0: m00 = value; break; case 1: m01 = value; break; case 2: m02 = value; break; case 3: m03 = value; break;
            case 4: m10 = value; break; case 5: m11 = value; break; case 6: m12 = value; break; case 7: m13 = value; break;
            case 8: m20 = value; break; case 9: m21 = value; break; case 10: m22 = value; break; case 11: m23 = value; break;
            case 12: m30 = value; break; case 13: m31 = value; break; case 14: m32 = value; break; case 15: m33 = value; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public Matrix4f setRow(int row, Vector4f v) {
        if (row == 0) { m00 = v.x(); m10 = v.y(); m20 = v.z(); m30 = v.w(); }
        else if (row == 1) { m01 = v.x(); m11 = v.y(); m21 = v.z(); m31 = v.w(); }
        else if (row == 2) { m02 = v.x(); m12 = v.y(); m22 = v.z(); m32 = v.w(); }
        else if (row == 3) { m03 = v.x(); m13 = v.y(); m23 = v.z(); m33 = v.w(); }
        else throw new IndexOutOfBoundsException("Row: " + row);
        return this;
    }

    public Matrix4f setColumn(int col, Vector4f v) {
        if (col == 0) { m00 = v.x(); m01 = v.y(); m02 = v.z(); m03 = v.w(); }
        else if (col == 1) { m10 = v.x(); m11 = v.y(); m12 = v.z(); m13 = v.w(); }
        else if (col == 2) { m20 = v.x(); m21 = v.y(); m22 = v.z(); m23 = v.w(); }
        else if (col == 3) { m30 = v.x(); m31 = v.y(); m32 = v.z(); m33 = v.w(); }
        else throw new IndexOutOfBoundsException("Column: " + col);
        return this;
    }

    public Matrix4f set3x3(Matrix3f m) {
        m00 = m.m00(); m01 = m.m01(); m02 = m.m02();
        m10 = m.m10(); m11 = m.m11(); m12 = m.m12();
        m20 = m.m20(); m21 = m.m21(); m22 = m.m22();
        return this;
    }

    public Matrix4f translateLocal(float x, float y, float z) {
        m30 += x; m31 += y; m32 += z;
        return this;
    }

    public Matrix4f rotateLocalX(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m10 = t10 * c + t20 * s;
        m11 = t11 * c + t21 * s;
        m12 = t12 * c + t22 * s;
        m13 = t13 * c + t23 * s;
        m20 = t10 * -s + t20 * c;
        m21 = t11 * -s + t21 * c;
        m22 = t12 * -s + t22 * c;
        m23 = t13 * -s + t23 * c;
        return this;
    }

    public Matrix4f rotateLocalY(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * c + t20 * -s;
        m01 = t01 * c + t21 * -s;
        m02 = t02 * c + t22 * -s;
        m03 = t03 * c + t23 * -s;
        m20 = t00 * s + t20 * c;
        m21 = t01 * s + t21 * c;
        m22 = t02 * s + t22 * c;
        m23 = t03 * s + t23 * c;
        return this;
    }

    public Matrix4f rotateLocalZ(float angle) {
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        m00 = t00 * c + t10 * -s;
        m01 = t01 * c + t11 * -s;
        m02 = t02 * c + t12 * -s;
        m03 = t03 * c + t13 * -s;
        m10 = t00 * s + t10 * c;
        m11 = t01 * s + t11 * c;
        m12 = t02 * s + t12 * c;
        m13 = t03 * s + t13 * c;
        return this;
    }

    public Matrix4f scaleLocal(float x, float y, float z) {
        m00 *= x; m01 *= x; m02 *= x; m03 *= x;
        m10 *= y; m11 *= y; m12 *= y; m13 *= y;
        m20 *= z; m21 *= z; m22 *= z; m23 *= z;
        return this;
    }

    public Vector3f positiveX() {
        return new Vector3f(m00, m01, m02);
    }

    public Vector3f positiveY() {
        return new Vector3f(m10, m11, m12);
    }

    public Vector3f positiveZ() {
        return new Vector3f(m20, m21, m22);
    }

    public Vector3f normalizedPositiveX() {
        float invLen = 1.0f / (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        return new Vector3f(m00 * invLen, m01 * invLen, m02 * invLen);
    }

    public Vector3f normalizedPositiveY() {
        float invLen = 1.0f / (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        return new Vector3f(m10 * invLen, m11 * invLen, m12 * invLen);
    }

    public Vector3f normalizedPositiveZ() {
        float invLen = 1.0f / (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        return new Vector3f(m20 * invLen, m21 * invLen, m22 * invLen);
    }

    public Vector3f getEulerAnglesZYX() {
        float a = (float) Math.asin(-m20);
        float ca = (float) Math.cos(a);
        if (Math.abs(ca) > 1.0e-6f) {
            return new Vector3f((float) Math.atan2(m21, m22), a, (float) Math.atan2(m10, m00));
        }
        return new Vector3f(0.0f, a, (float) Math.atan2(-m01, m11));
    }

    public boolean isFinite() {
        return Float.isFinite(m00) && Float.isFinite(m01) && Float.isFinite(m02) && Float.isFinite(m03)
            && Float.isFinite(m10) && Float.isFinite(m11) && Float.isFinite(m12) && Float.isFinite(m13)
            && Float.isFinite(m20) && Float.isFinite(m21) && Float.isFinite(m22) && Float.isFinite(m23)
            && Float.isFinite(m30) && Float.isFinite(m31) && Float.isFinite(m32) && Float.isFinite(m33);
    }

    public float m00() { return m00; }
    public float m01() { return m01; }
    public float m02() { return m02; }
    public float m03() { return m03; }
    public float m10() { return m10; }
    public float m11() { return m11; }
    public float m12() { return m12; }
    public float m13() { return m13; }
    public float m20() { return m20; }
    public float m21() { return m21; }
    public float m22() { return m22; }
    public float m23() { return m23; }
    public float m30() { return m30; }
    public float m31() { return m31; }
    public float m32() { return m32; }
    public float m33() { return m33; }

    public Matrix4f m00(float v) { m00 = v; return this; }
    public Matrix4f m01(float v) { m01 = v; return this; }
    public Matrix4f m02(float v) { m02 = v; return this; }
    public Matrix4f m03(float v) { m03 = v; return this; }
    public Matrix4f m10(float v) { m10 = v; return this; }
    public Matrix4f m11(float v) { m11 = v; return this; }
    public Matrix4f m12(float v) { m12 = v; return this; }
    public Matrix4f m13(float v) { m13 = v; return this; }
    public Matrix4f m20(float v) { m20 = v; return this; }
    public Matrix4f m21(float v) { m21 = v; return this; }
    public Matrix4f m22(float v) { m22 = v; return this; }
    public Matrix4f m23(float v) { m23 = v; return this; }
    public Matrix4f m30(float v) { m30 = v; return this; }
    public Matrix4f m31(float v) { m31 = v; return this; }
    public Matrix4f m32(float v) { m32 = v; return this; }
    public Matrix4f m33(float v) { m33 = v; return this; }

    public float[] get(float[] dest, int offset) {
        dest[offset + 0] = m00;
        dest[offset + 1] = m01;
        dest[offset + 2] = m02;
        dest[offset + 3] = m03;
        dest[offset + 4] = m10;
        dest[offset + 5] = m11;
        dest[offset + 6] = m12;
        dest[offset + 7] = m13;
        dest[offset + 8] = m20;
        dest[offset + 9] = m21;
        dest[offset + 10] = m22;
        dest[offset + 11] = m23;
        dest[offset + 12] = m30;
        dest[offset + 13] = m31;
        dest[offset + 14] = m32;
        dest[offset + 15] = m33;
        return dest;
    }

    public float[] get(float[] dest) {
        return get(dest, 0);
    }

    public float[] get3x3(float[] dest, int offset) {
        dest[offset + 0] = m00;
        dest[offset + 1] = m01;
        dest[offset + 2] = m02;
        dest[offset + 3] = m10;
        dest[offset + 4] = m11;
        dest[offset + 5] = m12;
        dest[offset + 6] = m20;
        dest[offset + 7] = m21;
        dest[offset + 8] = m22;
        return dest;
    }

    public Vector4f row(int row) {
        if (row == 0) {
            return new Vector4f(m00, m10, m20, m30);
        } else if (row == 1) {
            return new Vector4f(m01, m11, m21, m31);
        } else if (row == 2) {
            return new Vector4f(m02, m12, m22, m32);
        } else {
            return new Vector4f(m03, m13, m23, m33);
        }
    }

    public Vector4f column(int col) {
        if (col == 0) {
            return new Vector4f(m00, m01, m02, m03);
        } else if (col == 1) {
            return new Vector4f(m10, m11, m12, m13);
        } else if (col == 2) {
            return new Vector4f(m20, m21, m22, m23);
        } else {
            return new Vector4f(m30, m31, m32, m33);
        }
    }

    public boolean isAffine() {
        return m03 == 0.0f && m13 == 0.0f && m23 == 0.0f && m33 == 1.0f;
    }

    public boolean isIdentity() {
        return m00 == 1.0f && m01 == 0.0f && m02 == 0.0f && m03 == 0.0f
            && m10 == 0.0f && m11 == 1.0f && m12 == 0.0f && m13 == 0.0f
            && m20 == 0.0f && m21 == 0.0f && m22 == 1.0f && m23 == 0.0f
            && m30 == 0.0f && m31 == 0.0f && m32 == 0.0f && m33 == 1.0f;
    }

    public boolean isIdentity(float epsilon) {
        return Math.abs(m00 - 1.0f) <= epsilon && Math.abs(m01) <= epsilon && Math.abs(m02) <= epsilon && Math.abs(m03) <= epsilon
            && Math.abs(m10) <= epsilon && Math.abs(m11 - 1.0f) <= epsilon && Math.abs(m12) <= epsilon && Math.abs(m13) <= epsilon
            && Math.abs(m20) <= epsilon && Math.abs(m21) <= epsilon && Math.abs(m22 - 1.0f) <= epsilon && Math.abs(m23) <= epsilon
            && Math.abs(m30) <= epsilon && Math.abs(m31) <= epsilon && Math.abs(m32) <= epsilon && Math.abs(m33 - 1.0f) <= epsilon;
    }

    public Matrix4f billboard(Vector3f objPos, Vector3f target, Vector3f up) {
        float dirX = objPos.x() - target.x();
        float dirY = objPos.y() - target.y();
        float dirZ = objPos.z() - target.z();
        float invDirLen = 1.0f / (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float dX = dirX * invDirLen;
        float dY = dirY * invDirLen;
        float dZ = dirZ * invDirLen;
        float uX = up.x(), uY = up.y(), uZ = up.z();
        float rX = uY * dZ - uZ * dY;
        float rY = uZ * dX - uX * dZ;
        float rZ = uX * dY - uY * dX;
        float invRLen = 1.0f / (float) Math.sqrt(rX * rX + rY * rY + rZ * rZ);
        rX *= invRLen;
        rY *= invRLen;
        rZ *= invRLen;
        float nuX = rY * dZ - rZ * dY;
        float nuY = rZ * dX - rX * dZ;
        float nuZ = rX * dY - rY * dX;
        m00 = rX;
        m01 = nuX;
        m02 = dX;
        m03 = 0.0f;
        m10 = rY;
        m11 = nuY;
        m12 = dY;
        m13 = 0.0f;
        m20 = rZ;
        m21 = nuZ;
        m22 = dZ;
        m23 = 0.0f;
        m30 = objPos.x();
        m31 = objPos.y();
        m32 = objPos.z();
        m33 = 1.0f;
        return this;
    }

    public Matrix4f billboardCylindrical(Vector3f objPos, Vector3f targetPos, Vector3f up) {
        float dirX = objPos.x() - targetPos.x();
        float dirY = objPos.y() - targetPos.y();
        float dirZ = objPos.z() - targetPos.z();
        float invDirLen = 1.0f / (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float dX = dirX * invDirLen;
        float dY = dirY * invDirLen;
        float dZ = dirZ * invDirLen;
        float uX = up.x(), uY = up.y(), uZ = up.z();
        float lX = uY * dZ - uZ * dY;
        float lY = uZ * dX - uX * dZ;
        float lZ = uX * dY - uY * dX;
        float invLeftLen = 1.0f / (float) Math.sqrt(lX * lX + lY * lY + lZ * lZ);
        lX *= invLeftLen;
        lY *= invLeftLen;
        lZ *= invLeftLen;
        float nuX = dY * lZ - dZ * lY;
        float nuY = dZ * lX - dX * lZ;
        float nuZ = dX * lY - dY * lX;
        m00 = lX;
        m01 = nuX;
        m02 = dX;
        m03 = 0.0f;
        m10 = lY;
        m11 = nuY;
        m12 = dY;
        m13 = 0.0f;
        m20 = lZ;
        m21 = nuZ;
        m22 = dZ;
        m23 = 0.0f;
        m30 = objPos.x();
        m31 = objPos.y();
        m32 = objPos.z();
        m33 = 1.0f;
        return this;
    }

    public Matrix4f billboardSpherical(Vector3f objPos, Vector3f targetPos, Vector3f up) {
        return billboard(objPos, targetPos, up);
    }

    public Matrix4f billboardSpherical(Vector3f objPos, Vector3f targetPos) {
        return billboardSpherical(objPos, targetPos, new Vector3f(0, 1, 0));
    }

    public Matrix4f shadow(float[] light, float nx, float ny, float nz, float d) {
        float lx = light[0], ly = light[1], lz = light[2], lw = light[3];
        float dot = nx * lx + ny * ly + nz * lz + d * lw;
        m00 = dot - lx * nx;
        m01 = -ly * nx;
        m02 = -lz * nx;
        m03 = -lw * nx;
        m10 = -lx * ny;
        m11 = dot - ly * ny;
        m12 = -lz * ny;
        m13 = -lw * ny;
        m20 = -lx * nz;
        m21 = -ly * nz;
        m22 = dot - lz * nz;
        m23 = -lw * nz;
        m30 = -lx * d;
        m31 = -ly * d;
        m32 = -lz * d;
        m33 = dot - lw * d;
        return this;
    }

    public Matrix4f shadow(Vector4f light, float nx, float ny, float nz, float d) {
        return shadow(new float[]{light.x(), light.y(), light.z(), light.w()}, nx, ny, nz, d);
    }

    public Matrix4f shadow(float lightX, float lightY, float lightZ, float lightW, float nx, float ny, float nz, float d) {
        return shadow(new float[]{lightX, lightY, lightZ, lightW}, nx, ny, nz, d);
    }

    public Matrix4f shadow(Vector4f light, Matrix4f planeTransform) {
        float nx = Math.fma(planeTransform.m01, planeTransform.m12, -planeTransform.m02 * planeTransform.m11);
        float ny = Math.fma(planeTransform.m02, planeTransform.m10, -planeTransform.m00 * planeTransform.m12);
        float nz = Math.fma(planeTransform.m00, planeTransform.m11, -planeTransform.m01 * planeTransform.m10);
        float d = -(nx * planeTransform.m30 + ny * planeTransform.m31 + nz * planeTransform.m32);
        return shadow(light.x(), light.y(), light.z(), light.w(), nx, ny, nz, d);
    }

    public Matrix4f shadow(float lightX, float lightY, float lightZ, float lightW, Matrix4f planeTransform) {
        return shadow(new Vector4f(lightX, lightY, lightZ, lightW), planeTransform);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(m00, m01, m02, m03,
                                      m10, m11, m12, m13,
                                      m20, m21, m22, m23,
                                      m30, m31, m32, m33);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix4f other)) return false;
        return Float.floatToIntBits(m00) == Float.floatToIntBits(other.m00)
            && Float.floatToIntBits(m01) == Float.floatToIntBits(other.m01)
            && Float.floatToIntBits(m02) == Float.floatToIntBits(other.m02)
            && Float.floatToIntBits(m03) == Float.floatToIntBits(other.m03)
            && Float.floatToIntBits(m10) == Float.floatToIntBits(other.m10)
            && Float.floatToIntBits(m11) == Float.floatToIntBits(other.m11)
            && Float.floatToIntBits(m12) == Float.floatToIntBits(other.m12)
            && Float.floatToIntBits(m13) == Float.floatToIntBits(other.m13)
            && Float.floatToIntBits(m20) == Float.floatToIntBits(other.m20)
            && Float.floatToIntBits(m21) == Float.floatToIntBits(other.m21)
            && Float.floatToIntBits(m22) == Float.floatToIntBits(other.m22)
            && Float.floatToIntBits(m23) == Float.floatToIntBits(other.m23)
            && Float.floatToIntBits(m30) == Float.floatToIntBits(other.m30)
            && Float.floatToIntBits(m31) == Float.floatToIntBits(other.m31)
            && Float.floatToIntBits(m32) == Float.floatToIntBits(other.m32)
            && Float.floatToIntBits(m33) == Float.floatToIntBits(other.m33);
    }

    @Override
    public String toString() {
        return String.format("Matrix4f[[%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f]]",
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33);
    }

    public Matrix4f get(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, m00);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, m01);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, m02);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 12, m03);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 16, m10);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 20, m11);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 24, m12);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 28, m13);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 32, m20);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 36, m21);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 40, m22);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 44, m23);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 48, m30);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 52, m31);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 56, m32);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 60, m33);
        return this;
    }

    public Matrix4f set(MemorySegment src, long byteOffset) {
        m00 = src.get(ValueLayout.JAVA_FLOAT, byteOffset);
        m01 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4);
        m02 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8);
        m03 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 12);
        m10 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 16);
        m11 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 20);
        m12 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 24);
        m13 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 28);
        m20 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 32);
        m21 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 36);
        m22 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 40);
        m23 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 44);
        m30 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 48);
        m31 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 52);
        m32 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 56);
        m33 = src.get(ValueLayout.JAVA_FLOAT, byteOffset + 60);
        return this;
    }

    public static Matrix4f fromBuffer(java.nio.FloatBuffer src) {
        Matrix4f m = new Matrix4f();
        m.m00 = src.get(); m.m01 = src.get(); m.m02 = src.get(); m.m03 = src.get();
        m.m10 = src.get(); m.m11 = src.get(); m.m12 = src.get(); m.m13 = src.get();
        m.m20 = src.get(); m.m21 = src.get(); m.m22 = src.get(); m.m23 = src.get();
        m.m30 = src.get(); m.m31 = src.get(); m.m32 = src.get(); m.m33 = src.get();
        return m;
    }

    public static Matrix4f fromBuffer(int index, java.nio.FloatBuffer src) {
        Matrix4f m = new Matrix4f();
        m.m00 = src.get(index);      m.m01 = src.get(index + 1);  m.m02 = src.get(index + 2);  m.m03 = src.get(index + 3);
        m.m10 = src.get(index + 4);  m.m11 = src.get(index + 5);  m.m12 = src.get(index + 6);  m.m13 = src.get(index + 7);
        m.m20 = src.get(index + 8);  m.m21 = src.get(index + 9);  m.m22 = src.get(index + 10); m.m23 = src.get(index + 11);
        m.m30 = src.get(index + 12); m.m31 = src.get(index + 13); m.m32 = src.get(index + 14); m.m33 = src.get(index + 15);
        return m;
    }

    public java.nio.FloatBuffer writeToBuffer(java.nio.FloatBuffer dest) {
        dest.put(m00); dest.put(m01); dest.put(m02); dest.put(m03);
        dest.put(m10); dest.put(m11); dest.put(m12); dest.put(m13);
        dest.put(m20); dest.put(m21); dest.put(m22); dest.put(m23);
        dest.put(m30); dest.put(m31); dest.put(m32); dest.put(m33);
        return dest;
    }

    public java.nio.FloatBuffer writeToBuffer(int index, java.nio.FloatBuffer dest) {
        dest.put(index, m00);      dest.put(index + 1, m01);  dest.put(index + 2, m02);  dest.put(index + 3, m03);
        dest.put(index + 4, m10);  dest.put(index + 5, m11);  dest.put(index + 6, m12);  dest.put(index + 7, m13);
        dest.put(index + 8, m20);  dest.put(index + 9, m21);  dest.put(index + 10, m22); dest.put(index + 11, m23);
        dest.put(index + 12, m30); dest.put(index + 13, m31); dest.put(index + 14, m32); dest.put(index + 15, m33);
        return dest;
    }

    public Matrix4f rotateTowards(Vector3f dir, Vector3f up) {
        return rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }

    public Matrix4f rotateTowards(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        float invLen = 1.0f / (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float fX = dirX * invLen;
        float fY = dirY * invLen;
        float fZ = dirZ * invLen;
        float sX = fY * upZ - fZ * upY;
        float sY = fZ * upX - fX * upZ;
        float sZ = fX * upY - fY * upX;
        float invSLen = 1.0f / (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        float uX = sY * fZ - sZ * fY;
        float uY = sZ * fX - sX * fZ;
        float uZ = sX * fY - sY * fX;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * sX + t10 * sY + t20 * sZ;
        m01 = t01 * sX + t11 * sY + t21 * sZ;
        m02 = t02 * sX + t12 * sY + t22 * sZ;
        m03 = t03 * sX + t13 * sY + t23 * sZ;
        m10 = t00 * uX + t10 * uY + t20 * uZ;
        m11 = t01 * uX + t11 * uY + t21 * uZ;
        m12 = t02 * uX + t12 * uY + t22 * uZ;
        m13 = t03 * uX + t13 * uY + t23 * uZ;
        m20 = t00 * -fX + t10 * -fY + t20 * -fZ;
        m21 = t01 * -fX + t11 * -fY + t21 * -fZ;
        m22 = t02 * -fX + t12 * -fY + t22 * -fZ;
        m23 = t03 * -fX + t13 * -fY + t23 * -fZ;
        return this;
    }

    public Matrix4f rotateTowardsXY(float x, float y) {
        float invLen = 1.0f / (float) Math.sqrt(x * x + y * y);
        float nx = x * invLen;
        float ny = y * invLen;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        m00 = t00 * nx + t10 * ny;
        m01 = t01 * nx + t11 * ny;
        m02 = t02 * nx + t12 * ny;
        m03 = t03 * nx + t13 * ny;
        m10 = t00 * -ny + t10 * nx;
        m11 = t01 * -ny + t11 * nx;
        m12 = t02 * -ny + t12 * nx;
        m13 = t03 * -ny + t13 * nx;
        return this;
    }

    public Matrix4f arcball(float radius, float centerX, float centerY, float centerZ, float angle1, float angle2) {
        float x = radius * (float) Math.sin(angle1);
        float y = radius * (float) Math.sin(angle2);
        float z = radius * (float) Math.cos(angle1) * (float) Math.cos(angle2);
        float fX = -x;
        float fY = -y;
        float fZ = -z;
        float invFLen = 1.0f / (float) Math.sqrt(fX * fX + fY * fY + fZ * fZ);
        fX *= invFLen;
        fY *= invFLen;
        fZ *= invFLen;
        float sX = fY;
        float sY = -fX;
        float sZ = 0.0f;
        float invSLen = 1.0f / (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        float uX = sY * fZ - sZ * fY;
        float uY = sZ * fX - sX * fZ;
        float uZ = sX * fY - sY * fX;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * sX + t10 * sY + t20 * sZ;
        m01 = t01 * sX + t11 * sY + t21 * sZ;
        m02 = t02 * sX + t12 * sY + t22 * sZ;
        m03 = t03 * sX + t13 * sY + t23 * sZ;
        m10 = t00 * uX + t10 * uY + t20 * uZ;
        m11 = t01 * uX + t11 * uY + t21 * uZ;
        m12 = t02 * uX + t12 * uY + t22 * uZ;
        m13 = t03 * uX + t13 * uY + t23 * uZ;
        m20 = t00 * -fX + t10 * -fY + t20 * -fZ;
        m21 = t01 * -fX + t11 * -fY + t21 * -fZ;
        m22 = t02 * -fX + t12 * -fY + t22 * -fZ;
        m23 = t03 * -fX + t13 * -fY + t23 * -fZ;
        m30 = m30 + t00 * centerX + t10 * centerY + t20 * centerZ;
        m31 = m31 + t01 * centerX + t11 * centerY + t21 * centerZ;
        m32 = m32 + t02 * centerX + t12 * centerY + t22 * centerZ;
        m33 = m33 + t03 * centerX + t13 * centerY + t23 * centerZ;
        return this;
    }

    public Matrix4f arcball(float radius, Vector3f center, float angle1, float angle2) {
        return arcball(radius, center.x(), center.y(), center.z(), angle1, angle2);
    }

    public Vector3f transformPosition(float x, float y, float z) {
        float px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        float py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        float pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        return new Vector3f(px, py, pz);
    }

    public Vector3f transformDirection(float x, float y, float z) {
        float dx = Math.fma(m00, x, Math.fma(m10, y, m20 * z));
        float dy = Math.fma(m01, x, Math.fma(m11, y, m21 * z));
        float dz = Math.fma(m02, x, Math.fma(m12, y, m22 * z));
        return new Vector3f(dx, dy, dz);
    }

    public Vector3f transformAffine(Vector3f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3f(x, y, z);
    }

    public Vector3f transformAffine(float x, float y, float z) {
        float ax = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        float ay = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        float az = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        return new Vector3f(ax, ay, az);
    }

    public Vector4f transform(float x, float y, float z, float w) {
        float tx = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30 * w)));
        float ty = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31 * w)));
        float tz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32 * w)));
        float tw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33 * w)));
        return new Vector4f(tx, ty, tz, tw);
    }

    public Vector3f transformProject(float x, float y, float z) {
        float px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        float py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        float pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        float pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        float invW = 1.0f / pw;
        return new Vector3f(px * invW, py * invW, pz * invW);
    }

    public Matrix4f rotateYXZ(float angleY, float angleX, float angleZ) {
        float cy = (float) Math.cos(angleY), sy = (float) Math.sin(angleY);
        float cx = (float) Math.cos(angleX), sx = (float) Math.sin(angleX);
        float cz = (float) Math.cos(angleZ), sz = (float) Math.sin(angleZ);
        float r00 = cy * cz + sy * sx * sz;
        float r01 = cy * sz - sy * sx * cz;
        float r02 = sy * cx;
        float r10 = cx * sz;
        float r11 = cx * cz;
        float r12 = -sx;
        float r20 = -sy * cz + cy * sx * sz;
        float r21 = -sy * sz - cy * sx * cz;
        float r22 = cy * cx;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        return this;
    }

    public Matrix4f rotateLocal(float ang, float x, float y, float z) {
        float invLen = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);
        float nx = x * invLen;
        float ny = y * invLen;
        float nz = z * invLen;
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t = 1.0f - c;
        float r00 = t * nx * nx + c;
        float r01 = t * nx * ny + s * nz;
        float r02 = t * nx * nz - s * ny;
        float r10 = t * nx * ny - s * nz;
        float r11 = t * ny * ny + c;
        float r12 = t * ny * nz + s * nx;
        float r20 = t * nx * nz + s * ny;
        float r21 = t * ny * nz - s * nx;
        float r22 = t * nz * nz + c;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        float t30 = m30; float t31 = m31; float t32 = m32; float t33 = m33;
        m00 = t00 * r00 + t01 * r01 + t02 * r02;
        m01 = t00 * r10 + t01 * r11 + t02 * r12;
        m02 = t00 * r20 + t01 * r21 + t02 * r22;
        m03 = t03;
        m10 = t10 * r00 + t11 * r01 + t12 * r02;
        m11 = t10 * r10 + t11 * r11 + t12 * r12;
        m12 = t10 * r20 + t11 * r21 + t12 * r22;
        m13 = t13;
        m20 = t20 * r00 + t21 * r01 + t22 * r02;
        m21 = t20 * r10 + t21 * r11 + t22 * r12;
        m22 = t20 * r20 + t21 * r21 + t22 * r22;
        m23 = t23;
        m30 = t30 * r00 + t31 * r01 + t32 * r02;
        m31 = t30 * r10 + t31 * r11 + t32 * r12;
        m32 = t30 * r20 + t31 * r21 + t32 * r22;
        m33 = t33;
        return this;
    }

    public Matrix4f rotateLocal(Vector3f axis, float ang) {
        return rotateLocal(ang, axis.x(), axis.y(), axis.z());
    }

    public Matrix4f rotateAround(Vector3f origin, Vector3f axis, float ang) {
        float invLen = 1.0f / (float) Math.sqrt(axis.x() * axis.x() + axis.y() * axis.y() + axis.z() * axis.z());
        float nx = axis.x() * invLen;
        float ny = axis.y() * invLen;
        float nz = axis.z() * invLen;
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t = 1.0f - c;
        float r00 = t * nx * nx + c;
        float r01 = t * nx * ny + s * nz;
        float r02 = t * nx * nz - s * ny;
        float r10 = t * nx * ny - s * nz;
        float r11 = t * ny * ny + c;
        float r12 = t * ny * nz + s * nx;
        float r20 = t * nx * nz + s * ny;
        float r21 = t * ny * nz - s * nx;
        float r22 = t * nz * nz + c;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        float t30 = m30; float t31 = m31; float t32 = m32; float t33 = m33;
        float ox = origin.x(), oy = origin.y(), oz = origin.z();
        float n30 = Math.fma(t00, ox, Math.fma(t10, oy, Math.fma(t20, oz, t30)));
        float n31 = Math.fma(t01, ox, Math.fma(t11, oy, Math.fma(t21, oz, t31)));
        float n32 = Math.fma(t02, ox, Math.fma(t12, oy, Math.fma(t22, oz, t32)));
        float n33 = Math.fma(t03, ox, Math.fma(t13, oy, Math.fma(t23, oz, t33)));
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        m30 = n30 - (m00 * ox + m10 * oy + m20 * oz);
        m31 = n31 - (m01 * ox + m11 * oy + m21 * oz);
        m32 = n32 - (m02 * ox + m12 * oy + m22 * oz);
        m33 = n33 - (m03 * ox + m13 * oy + m23 * oz);
        return this;
    }

    public Matrix4f rotate(float ang, Vector3f axis) {
        return rotate(ang, axis.x(), axis.y(), axis.z());
    }

    public Matrix4f rotateLocal(Quaternionf quat) {
        float x = quat.x(), y = quat.y(), z = quat.z(), w = quat.w();
        float r00 = 1.0f - 2.0f * (y * y + z * z);
        float r01 = 2.0f * (x * y + w * z);
        float r02 = 2.0f * (x * z - w * y);
        float r10 = 2.0f * (x * y - w * z);
        float r11 = 1.0f - 2.0f * (x * x + z * z);
        float r12 = 2.0f * (y * z + w * x);
        float r20 = 2.0f * (x * z + w * y);
        float r21 = 2.0f * (y * z - w * x);
        float r22 = 1.0f - 2.0f * (x * x + y * y);
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        float t30 = m30; float t31 = m31; float t32 = m32; float t33 = m33;
        m00 = t00 * r00 + t01 * r01 + t02 * r02;
        m01 = t00 * r10 + t01 * r11 + t02 * r12;
        m02 = t00 * r20 + t01 * r21 + t02 * r22;
        m03 = t03;
        m10 = t10 * r00 + t11 * r01 + t12 * r02;
        m11 = t10 * r10 + t11 * r11 + t12 * r12;
        m12 = t10 * r20 + t11 * r21 + t12 * r22;
        m13 = t13;
        m20 = t20 * r00 + t21 * r01 + t22 * r02;
        m21 = t20 * r10 + t21 * r11 + t22 * r12;
        m22 = t20 * r20 + t21 * r21 + t22 * r22;
        m23 = t23;
        m30 = t30 * r00 + t31 * r01 + t32 * r02;
        m31 = t30 * r10 + t31 * r11 + t32 * r12;
        m32 = t30 * r20 + t31 * r21 + t32 * r22;
        m33 = t33;
        return this;
    }

    public Matrix4f rotateTranslation(float ang, float x, float y, float z) {
        float invLen = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);
        float nx = x * invLen;
        float ny = y * invLen;
        float nz = z * invLen;
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t = 1.0f - c;
        float r00 = t * nx * nx + c;
        float r01 = t * nx * ny + s * nz;
        float r02 = t * nx * nz - s * ny;
        float r10 = t * nx * ny - s * nz;
        float r11 = t * ny * ny + c;
        float r12 = t * ny * nz + s * nx;
        float r20 = t * nx * nz + s * ny;
        float r21 = t * ny * nz - s * nx;
        float r22 = t * nz * nz + c;
        m00 = r00; m01 = r01; m02 = r02; m03 = 0.0f;
        m10 = r10; m11 = r11; m12 = r12; m13 = 0.0f;
        m20 = r20; m21 = r21; m22 = r22; m23 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f; m33 = 1.0f;
        return this;
    }

    public Matrix4f rotateTranslation(Quaternionf quat) {
        float x = quat.x(), y = quat.y(), z = quat.z(), w = quat.w();
        float r00 = 1.0f - 2.0f * (y * y + z * z);
        float r01 = 2.0f * (x * y + w * z);
        float r02 = 2.0f * (x * z - w * y);
        float r10 = 2.0f * (x * y - w * z);
        float r11 = 1.0f - 2.0f * (x * x + z * z);
        float r12 = 2.0f * (y * z + w * x);
        float r20 = 2.0f * (x * z + w * y);
        float r21 = 2.0f * (y * z - w * x);
        float r22 = 1.0f - 2.0f * (x * x + y * y);
        m00 = r00; m01 = r01; m02 = r02; m03 = 0.0f;
        m10 = r10; m11 = r11; m12 = r12; m13 = 0.0f;
        m20 = r20; m21 = r21; m22 = r22; m23 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f; m33 = 1.0f;
        return this;
    }

    public static Matrix4f translation(Vector3f offset) {
        return translation(offset.x(), offset.y(), offset.z());
    }

    public static Matrix4f rotation(float ang, float x, float y, float z) {
        return new Matrix4f().rotate(ang, x, y, z);
    }

    public static Matrix4f rotation(float ang, Vector3f axis) {
        return rotation(ang, axis.x(), axis.y(), axis.z());
    }

    public static Matrix4f rotation(Quaternionf quat) {
        return new Matrix4f().rotate(quat);
    }

    public static Matrix4f rotationXYZ(float angleX, float angleY, float angleZ) {
        return new Matrix4f().rotateXYZ(angleX, angleY, angleZ);
    }

    public static Matrix4f rotationZYX(float angleZ, float angleY, float angleX) {
        return new Matrix4f().rotateZYX(angleZ, angleY, angleX);
    }

    public static Matrix4f rotationYXZ(float angleY, float angleX, float angleZ) {
        return new Matrix4f().rotateYXZ(angleY, angleX, angleZ);
    }

    public static Matrix4f scaling(float scale) {
        return scaling(scale, scale, scale);
    }

    public static Matrix4f scaling(Vector3f scale) {
        return scaling(scale.x(), scale.y(), scale.z());
    }

    public static Matrix4f translationRotateScale(float tx, float ty, float tz, float qx, float qy, float qz, float qw, float sx, float sy, float sz) {
        Matrix4f m = new Matrix4f();
        m.setTranslation(tx, ty, tz);
        m.rotate(new Quaternionf(qx, qy, qz, qw));
        m.scale(sx, sy, sz);
        return m;
    }

    public static Matrix4f translationRotateScale(Vector3f translation, Quaternionf quat, Vector3f scale) {
        return translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }

    public static Matrix4f translationRotateScaleInvert(float tx, float ty, float tz, float qx, float qy, float qz, float qw, float sx, float sy, float sz) {
        Matrix4f m = translationRotateScale(tx, ty, tz, qx, qy, qz, qw, sx, sy, sz);
        m.invert();
        return m;
    }

    public static Matrix4f translationRotateScaleInvert(Vector3f translation, Quaternionf quat, Vector3f scale) {
        return translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }

    public static Matrix4f translationRotateScaleMulAffine(float tx, float ty, float tz, float qx, float qy, float qz, float qw, float sx, float sy, float sz, Matrix4f mul) {
        Matrix4f m = translationRotateScale(tx, ty, tz, qx, qy, qz, qw, sx, sy, sz);
        m.mulAffine(mul);
        return m;
    }

    public static Matrix4f translationRotate(float tx, float ty, float tz, float qx, float qy, float qz, float qw) {
        Matrix4f m = new Matrix4f();
        m.setTranslation(tx, ty, tz);
        m.rotate(new Quaternionf(qx, qy, qz, qw));
        return m;
    }

    public static Matrix4f translationRotate(Vector3f translation, Quaternionf quat) {
        return translationRotate(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }

    public static Matrix4f translationRotateInvert(float tx, float ty, float tz, float qx, float qy, float qz, float qw) {
        Matrix4f m = translationRotate(tx, ty, tz, qx, qy, qz, qw);
        m.invert();
        return m;
    }

    public static Matrix4f translationRotateInvert(Vector3f translation, Quaternionf quat) {
        return translationRotateInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }

    public static Matrix4f rotationTowardsXY(float x, float y) {
        return new Matrix4f().rotateTowardsXY(x, y);
    }

    public float determinantAffine() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix4f mulAffineR(Matrix4f left) {
        float n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, left.m02 * m20));
        float n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, left.m02 * m21));
        float n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, left.m02 * m22));
        float n03 = Math.fma(left.m00, m03, Math.fma(left.m01, m13, left.m02 * m23));
        float n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, left.m12 * m20));
        float n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, left.m12 * m21));
        float n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, left.m12 * m22));
        float n13 = Math.fma(left.m10, m03, Math.fma(left.m11, m13, left.m12 * m23));
        float n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, left.m22 * m20));
        float n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, left.m22 * m21));
        float n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, left.m22 * m22));
        float n23 = Math.fma(left.m20, m03, Math.fma(left.m21, m13, left.m22 * m23));
        float n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, left.m33 * m30)));
        float n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, left.m33 * m31)));
        float n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, left.m33 * m32)));
        float n33 = Math.fma(left.m30, m03, Math.fma(left.m31, m13, Math.fma(left.m32, m23, left.m33 * m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4f mulLocalAffine(Matrix4f left) {
        float n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, left.m02 * m20));
        float n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, left.m02 * m21));
        float n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, left.m02 * m22));
        float n03 = Math.fma(left.m00, m03, Math.fma(left.m01, m13, left.m02 * m23));
        float n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, left.m12 * m20));
        float n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, left.m12 * m21));
        float n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, left.m12 * m22));
        float n13 = Math.fma(left.m10, m03, Math.fma(left.m11, m13, left.m12 * m23));
        float n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, left.m22 * m20));
        float n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, left.m22 * m21));
        float n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, left.m22 * m22));
        float n23 = Math.fma(left.m20, m03, Math.fma(left.m21, m13, left.m22 * m23));
        float n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, m30)));
        float n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, m31)));
        float n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, m32)));
        float n33 = Math.fma(left.m30, m03, Math.fma(left.m31, m13, Math.fma(left.m32, m23, m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix3f normal(Matrix3f dest) {
        float a00 = m00, a01 = m01, a02 = m02;
        float a10 = m10, a11 = m11, a12 = m12;
        float a20 = m20, a21 = m21, a22 = m22;
        float det3 = a00 * (a11 * a22 - a12 * a21)
                    - a01 * (a10 * a22 - a12 * a20)
                    + a02 * (a10 * a21 - a11 * a20);
        if (det3 == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot compute normal matrix");
        }
        float invDet = 1.0f / det3;
        float n00 = (a11 * a22 - a12 * a21) * invDet;
        float n01 = (a02 * a21 - a01 * a22) * invDet;
        float n02 = (a01 * a12 - a02 * a11) * invDet;
        float n10 = (a12 * a20 - a10 * a22) * invDet;
        float n11 = (a00 * a22 - a02 * a20) * invDet;
        float n12 = (a02 * a10 - a00 * a12) * invDet;
        float n20 = (a10 * a21 - a11 * a20) * invDet;
        float n21 = (a01 * a20 - a00 * a21) * invDet;
        float n22 = (a00 * a11 - a01 * a10) * invDet;
        dest.m00 = n00; dest.m01 = n10; dest.m02 = n20;
        dest.m10 = n01; dest.m11 = n11; dest.m12 = n21;
        dest.m20 = n02; dest.m21 = n12; dest.m22 = n22;
        return dest;
    }

    public Matrix3f get3x3(Matrix3f dest) {
        dest.m00 = m00; dest.m01 = m01; dest.m02 = m02;
        dest.m10 = m10; dest.m11 = m11; dest.m12 = m12;
        dest.m20 = m20; dest.m21 = m21; dest.m22 = m22;
        return dest;
    }

    public Matrix3f getNormalizedRotation(Matrix3f dest) {
        float sx = 1.0f / (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        float sy = 1.0f / (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        float sz = 1.0f / (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        dest.m00 = m00 * sx; dest.m01 = m01 * sx; dest.m02 = m02 * sx;
        dest.m10 = m10 * sy; dest.m11 = m11 * sy; dest.m12 = m12 * sy;
        dest.m20 = m20 * sz; dest.m21 = m21 * sz; dest.m22 = m22 * sz;
        return dest;
    }

    public Matrix4f normalize3x3() {
        float sx = 1.0f / (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        float sy = 1.0f / (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        float sz = 1.0f / (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        m00 *= sx; m01 *= sx; m02 *= sx;
        m10 *= sy; m11 *= sy; m12 *= sy;
        m20 *= sz; m21 *= sz; m22 *= sz;
        return this;
    }

    public float cofactor3x3() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix4f mul(float scalar) {
        m00 *= scalar; m01 *= scalar; m02 *= scalar; m03 *= scalar;
        m10 *= scalar; m11 *= scalar; m12 *= scalar; m13 *= scalar;
        m20 *= scalar; m21 *= scalar; m22 *= scalar; m23 *= scalar;
        m30 *= scalar; m31 *= scalar; m32 *= scalar; m33 *= scalar;
        return this;
    }

    public Matrix4f mulLocal(Matrix4f left) {
        float n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, Math.fma(left.m02, m20, left.m03 * m30)));
        float n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, Math.fma(left.m02, m21, left.m03 * m31)));
        float n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, Math.fma(left.m02, m22, left.m03 * m32)));
        float n03 = Math.fma(left.m00, m03, Math.fma(left.m01, m13, Math.fma(left.m02, m23, left.m03 * m33)));
        float n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, Math.fma(left.m12, m20, left.m13 * m30)));
        float n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, Math.fma(left.m12, m21, left.m13 * m31)));
        float n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, Math.fma(left.m12, m22, left.m13 * m32)));
        float n13 = Math.fma(left.m10, m03, Math.fma(left.m11, m13, Math.fma(left.m12, m23, left.m13 * m33)));
        float n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, Math.fma(left.m22, m20, left.m23 * m30)));
        float n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, Math.fma(left.m22, m21, left.m23 * m31)));
        float n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, Math.fma(left.m22, m22, left.m23 * m32)));
        float n23 = Math.fma(left.m20, m03, Math.fma(left.m21, m13, Math.fma(left.m22, m23, left.m23 * m33)));
        float n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, left.m33 * m30)));
        float n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, left.m33 * m31)));
        float n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, left.m33 * m32)));
        float n33 = Math.fma(left.m30, m03, Math.fma(left.m31, m13, Math.fma(left.m32, m23, left.m33 * m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4f mulTranslation(Matrix4f... others) {
        for (Matrix4f other : others) {
            multiply(other);
        }
        return this;
    }

    public Matrix4f negateX() {
        m00 = -m00; m01 = -m01; m02 = -m02; m03 = -m03;
        return this;
    }

    public Matrix4f negateY() {
        m10 = -m10; m11 = -m11; m12 = -m12; m13 = -m13;
        return this;
    }

    public Matrix4f negateZ() {
        m20 = -m20; m21 = -m21; m22 = -m22; m23 = -m23;
        return this;
    }

    public Matrix4f scale(float xy, float z) {
        return scale(xy, xy, z);
    }

    public Vector3f project(float x, float y, float z, int[] viewport, Vector3f dest) {
        float px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        float py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        float pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        float pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        float invW = 1.0f / pw;
        float sx = (px * invW * 0.5f + 0.5f) * viewport[2] + viewport[0];
        float sy = (1.0f - (py * invW * 0.5f + 0.5f)) * viewport[3] + viewport[1];
        float sz = (1.0f + pz * invW) * 0.5f;
        return new Vector3f(sx, sy, sz);
    }

    public Vector3f unproject(float winX, float winY, float winZ, int[] viewport, Vector3f dest) {
        float x = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        float y = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        float z = 2.0f * winZ - 1.0f;
        Matrix4f inv = new Matrix4f(this);
        inv.invert();
        float px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, Math.fma(inv.m20, z, inv.m30)));
        float py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, Math.fma(inv.m21, z, inv.m31)));
        float pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, Math.fma(inv.m22, z, inv.m32)));
        float pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, Math.fma(inv.m23, z, inv.m33)));
        float invW = 1.0f / pw;
        return new Vector3f(px * invW, py * invW, pz * invW);
    }

    public Vector3f unproject(float winX, float winY, float winZ, int[] viewport, Matrix4f view, Vector3f dest) {
        Matrix4f inv = new Matrix4f(this);
        inv.multiply(view);
        inv.invert();
        float x = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        float y = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        float z = 2.0f * winZ - 1.0f;
        float px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, Math.fma(inv.m20, z, inv.m30)));
        float py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, Math.fma(inv.m21, z, inv.m31)));
        float pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, Math.fma(inv.m22, z, inv.m32)));
        float pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, Math.fma(inv.m23, z, inv.m33)));
        float invW = 1.0f / pw;
        return new Vector3f(px * invW, py * invW, pz * invW);
    }

    public Vector3f unprojectRay(float winX, float winY, int[] viewport, Vector3f dest) {
        float x = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        float y = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        Matrix4f inv = new Matrix4f(this);
        inv.invert();
        float px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, inv.m30));
        float py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, inv.m31));
        float pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, inv.m32));
        float pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, inv.m33));
        float invW = 1.0f / pw;
        float ox = px * invW;
        float oy = py * invW;
        float oz = pz * invW;
        return new Vector3f(ox, oy, oz);
    }

    public Matrix4f unprojectInvRay(float winX, float winY, int[] viewport, Matrix4f dest) {
        float x = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        float y = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        Matrix4f inv = new Matrix4f(this);
        inv.invert();
        float nx = Math.fma(inv.m00, x, Math.fma(inv.m10, y, -inv.m20));
        float ny = Math.fma(inv.m01, x, Math.fma(inv.m11, y, -inv.m21));
        float nz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, -inv.m22));
        float nw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, -inv.m23));
        float px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, inv.m30));
        float py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, inv.m31));
        float pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, inv.m32));
        float pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, inv.m33));
        float invW = 1.0f / pw;
        dest.m00 = nx; dest.m01 = ny; dest.m02 = nz; dest.m03 = 0.0f;
        dest.m10 = 0.0f; dest.m11 = 0.0f; dest.m12 = 0.0f; dest.m13 = 0.0f;
        dest.m20 = 0.0f; dest.m21 = 0.0f; dest.m22 = 0.0f; dest.m23 = 0.0f;
        dest.m30 = px * invW; dest.m31 = py * invW; dest.m32 = pz * invW; dest.m33 = 1.0f;
        return dest;
    }

    public Matrix4f pick(float x, float y, float width, float height, int[] viewport) {
        float sx = viewport[2] / width;
        float sy = viewport[3] / height;
        float tx = (viewport[2] + 2.0f * (viewport[0] - x)) / width;
        float ty = (viewport[3] + 2.0f * (viewport[1] - y)) / height;
        m00 = sx; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = sy; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 1.0f; m23 = 0.0f;
        m30 = tx; m31 = ty; m32 = 0.0f; m33 = 1.0f;
        return this;
    }

    public Matrix4f tile(float x, float y, float width, float height, int[] viewport) {
        float sx = width / viewport[2];
        float sy = height / viewport[3];
        float tx = (x - viewport[0]) * sx;
        float ty = (y - viewport[1]) * sy;
        m00 = sx; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = sy; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 1.0f; m23 = 0.0f;
        m30 = tx; m31 = ty; m32 = 0.0f; m33 = 1.0f;
        return this;
    }

    public Matrix4f viewport(int[] viewport) {
        float sx = viewport[2] * 0.5f;
        float sy = viewport[3] * 0.5f;
        float tx = viewport[0] + sx;
        float ty = viewport[1] + sy;
        m00 = sx; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = -sy; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 0.5f; m23 = 0.0f;
        m30 = tx; m31 = ty; m32 = 0.5f; m33 = 1.0f;
        return this;
    }

    public Matrix4f viewport(float x, float y, float width, float height, float zNear, float zFar) {
        float sx = width * 0.5f;
        float sy = height * 0.5f;
        float sz = (zFar - zNear) * 0.5f;
        float tx = x + sx;
        float ty = y + sy;
        float tz = (zFar + zNear) * 0.5f;
        m00 = sx; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = -sy; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = sz; m23 = 0.0f;
        m30 = tx; m31 = ty; m32 = tz; m33 = 1.0f;
        return this;
    }

    public Vector4f frustumPlane(int plane, Vector4f dest) {
        float a, b, c, d;
        switch (plane) {
            case 0:
                a = m03 - m00; b = m13 - m10; c = m23 - m20; d = m33 - m30; break;
            case 1:
                a = m03 + m00; b = m13 + m10; c = m23 + m20; d = m33 + m30; break;
            case 2:
                a = m03 + m01; b = m13 + m11; c = m23 + m21; d = m33 + m31; break;
            case 3:
                a = m03 - m01; b = m13 - m11; c = m23 - m21; d = m33 - m31; break;
            case 4:
                a = m03 + m02; b = m13 + m12; c = m23 + m22; d = m33 + m32; break;
            case 5:
                a = m03 - m02; b = m13 - m12; c = m23 - m22; d = m33 - m32; break;
            default:
                throw new IllegalArgumentException("plane: " + plane);
        }
        float invLen = 1.0f / (float) Math.sqrt(a * a + b * b + c * c);
        return new Vector4f(a * invLen, b * invLen, c * invLen, d * invLen);
    }

    public Vector3f frustumCorner(int corner, Vector3f dest) {
        float rX = m03 - m00, rY = m13 - m10, rZ = m23 - m20, rW = m33 - m30;
        float lX = m03 + m00, lY = m13 + m10, lZ = m23 + m20, lW = m33 + m30;
        float tX = m03 - m01, tY = m13 - m11, tZ = m23 - m21, tW = m33 - m31;
        float bX = m03 + m01, bY = m13 + m11, bZ = m23 + m21, bW = m33 + m31;
        float nCX = -m02, nCY = -m12, nCZ = -m22, nCW = m33 - m32;
        float fCX = m20 - m02, fCY = m21 - m12, fCZ = m22 - m22, fCW = m33 + m32;
        float[][] corners = {
            {lX, lY, lZ, lW, bX, bY, bZ, bW, nCX, nCY, nCZ, nCW},
            {rX, rY, rZ, rW, bX, bY, bZ, bW, nCX, nCY, nCZ, nCW},
            {rX, rY, rZ, rW, tX, tY, tZ, tW, nCX, nCY, nCZ, nCW},
            {lX, lY, lZ, lW, tX, tY, tZ, tW, nCX, nCY, nCZ, nCW},
            {lX, lY, lZ, lW, bX, bY, bZ, bW, fCX, fCY, fCZ, fCW},
            {rX, rY, rZ, rW, bX, bY, bZ, bW, fCX, fCY, fCZ, fCW},
            {rX, rY, rZ, rW, tX, tY, tZ, tW, fCX, fCY, fCZ, fCW},
            {lX, lY, lZ, lW, tX, tY, tZ, tW, fCX, fCY, fCZ, fCW}
        };
        float[] p = corners[corner];
        float a0 = p[0], b0 = p[1], c0 = p[2], d0 = p[3];
        float a1 = p[4], b1 = p[5], c1 = p[6], d1 = p[7];
        float a2 = p[8], b2 = p[9], c2 = p[10], d2 = p[11];
        float det = a0 * (b1 * c2 - b2 * c1) + a1 * (b2 * c0 - b0 * c2) + a2 * (b0 * c1 - b1 * c0);
        float invDet = 1.0f / det;
        float x = (d0 * (b1 * c2 - b2 * c1) + d1 * (b2 * c0 - b0 * c2) + d2 * (b0 * c1 - b1 * c0)) * invDet;
        float y = (a0 * (d1 * c2 - d2 * c1) + a1 * (d2 * c0 - d0 * c2) + a2 * (d0 * c1 - d1 * c0)) * invDet;
        float z = (a0 * (b1 * d2 - b2 * d1) + a1 * (b2 * d0 - b0 * d2) + a2 * (b0 * d1 - b1 * d0)) * invDet;
        return new Vector3f(x, y, z);
    }

    public Vector3f perspectiveOrigin(Vector3f dest) {
        float x = -m30 * (1.0f / m00);
        float y = -m31 * (1.0f / m11);
        float z = -m32;
        return new Vector3f(x, y, z);
    }

    public Vector3f perspectiveInvOrigin(Vector3f dest) {
        float x = -m30 / m00;
        float y = -m31 / m11;
        float z = -1.0f / m22;
        return new Vector3f(x, y, z);
    }

    public float perspectiveFov() {
        return (float) (2.0 * Math.atan(1.0 / m11));
    }

    public float perspectiveNear() {
        return m32 / m22;
    }

    public float perspectiveFar() {
        return m32 / (m22 + 1.0f);
    }

    public Matrix4f mirror(float nx, float ny, float nz, float d) {
        float invLen = 1.0f / (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        float nnx = nx * invLen;
        float nny = ny * invLen;
        float nnz = nz * invLen;
        float nd = d * invLen;
        float r00 = 1.0f - 2.0f * nnx * nnx;
        float r01 = -2.0f * nnx * nny;
        float r02 = -2.0f * nnx * nnz;
        float r03 = -2.0f * nnx * nd;
        float r10 = -2.0f * nny * nnx;
        float r11 = 1.0f - 2.0f * nny * nny;
        float r12 = -2.0f * nny * nnz;
        float r13 = -2.0f * nny * nd;
        float r20 = -2.0f * nnz * nnx;
        float r21 = -2.0f * nnz * nny;
        float r22 = 1.0f - 2.0f * nnz * nnz;
        float r23 = -2.0f * nnz * nd;
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        float t30 = m30; float t31 = m31; float t32 = m32; float t33 = m33;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m03 = t03 * r00 + t13 * r01 + t23 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m13 = t03 * r10 + t13 * r11 + t23 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        m23 = t03 * r20 + t13 * r21 + t23 * r22;
        m30 = Math.fma(t00, r03, Math.fma(t10, r13, t20 * r23));
        m31 = Math.fma(t01, r03, Math.fma(t11, r13, t21 * r23));
        m32 = Math.fma(t02, r03, Math.fma(t12, r13, t22 * r23));
        m33 = Math.fma(t03, r03, Math.fma(t13, r13, t23 * r23));
        return this;
    }

    public Matrix4f mirror(Vector3f normal, float d) {
        return mirror(normal.x(), normal.y(), normal.z(), d);
    }

    public boolean equals(Matrix4f other, float delta) {
        if (this == other) return true;
        return Math.abs(m00 - other.m00) <= delta
            && Math.abs(m01 - other.m01) <= delta
            && Math.abs(m02 - other.m02) <= delta
            && Math.abs(m03 - other.m03) <= delta
            && Math.abs(m10 - other.m10) <= delta
            && Math.abs(m11 - other.m11) <= delta
            && Math.abs(m12 - other.m12) <= delta
            && Math.abs(m13 - other.m13) <= delta
            && Math.abs(m20 - other.m20) <= delta
            && Math.abs(m21 - other.m21) <= delta
            && Math.abs(m22 - other.m22) <= delta
            && Math.abs(m23 - other.m23) <= delta
            && Math.abs(m30 - other.m30) <= delta
            && Math.abs(m31 - other.m31) <= delta
            && Math.abs(m32 - other.m32) <= delta
            && Math.abs(m33 - other.m33) <= delta;
    }

    public Matrix4f swap(Matrix4f other) {
        float t;
        t = m00; m00 = other.m00; other.m00 = t;
        t = m01; m01 = other.m01; other.m01 = t;
        t = m02; m02 = other.m02; other.m02 = t;
        t = m03; m03 = other.m03; other.m03 = t;
        t = m10; m10 = other.m10; other.m10 = t;
        t = m11; m11 = other.m11; other.m11 = t;
        t = m12; m12 = other.m12; other.m12 = t;
        t = m13; m13 = other.m13; other.m13 = t;
        t = m20; m20 = other.m20; other.m20 = t;
        t = m21; m21 = other.m21; other.m21 = t;
        t = m22; m22 = other.m22; other.m22 = t;
        t = m23; m23 = other.m23; other.m23 = t;
        t = m30; m30 = other.m30; other.m30 = t;
        t = m31; m31 = other.m31; other.m31 = t;
        t = m32; m32 = other.m32; other.m32 = t;
        t = m33; m33 = other.m33; other.m33 = t;
        return this;
    }

    public Vector3f getEulerAnglesYXZ() {
        float a = (float) Math.asin(-m12);
        float ca = (float) Math.cos(a);
        if (Math.abs(ca) > 1.0e-6f) {
            return new Vector3f((float) Math.atan2(m02, m22), a, (float) Math.atan2(m10, m11));
        }
        return new Vector3f(0.0f, a, (float) Math.atan2(-m01, m00));
    }

    public Vector3f getEulerAnglesXYZ() {
        float a = (float) Math.asin(m20);
        float ca = (float) Math.cos(a);
        if (Math.abs(ca) > 1.0e-6f) {
            return new Vector3f((float) Math.atan2(-m21, m22), a, (float) Math.atan2(-m10, m00));
        }
        return new Vector3f(0.0f, a, (float) Math.atan2(m01, m11));
    }

    public Matrix4f fma(Matrix4f other, float scalar) {
        m00 = Math.fma(other.m00, scalar, m00);
        m01 = Math.fma(other.m01, scalar, m01);
        m02 = Math.fma(other.m02, scalar, m02);
        m03 = Math.fma(other.m03, scalar, m03);
        m10 = Math.fma(other.m10, scalar, m10);
        m11 = Math.fma(other.m11, scalar, m11);
        m12 = Math.fma(other.m12, scalar, m12);
        m13 = Math.fma(other.m13, scalar, m13);
        m20 = Math.fma(other.m20, scalar, m20);
        m21 = Math.fma(other.m21, scalar, m21);
        m22 = Math.fma(other.m22, scalar, m22);
        m23 = Math.fma(other.m23, scalar, m23);
        m30 = Math.fma(other.m30, scalar, m30);
        m31 = Math.fma(other.m31, scalar, m31);
        m32 = Math.fma(other.m32, scalar, m32);
        m33 = Math.fma(other.m33, scalar, m33);
        return this;
    }

    public Matrix4f rotateAroundLocal(Quaternionf quat, Vector3f origin) {
        float x = quat.x(), y = quat.y(), z = quat.z(), w = quat.w();
        float r00 = 1.0f - 2.0f * (y * y + z * z);
        float r01 = 2.0f * (x * y + w * z);
        float r02 = 2.0f * (x * z - w * y);
        float r10 = 2.0f * (x * y - w * z);
        float r11 = 1.0f - 2.0f * (x * x + z * z);
        float r12 = 2.0f * (y * z + w * x);
        float r20 = 2.0f * (x * z + w * y);
        float r21 = 2.0f * (y * z - w * x);
        float r22 = 1.0f - 2.0f * (x * x + y * y);
        float t00 = m00; float t01 = m01; float t02 = m02; float t03 = m03;
        float t10 = m10; float t11 = m11; float t12 = m12; float t13 = m13;
        float t20 = m20; float t21 = m21; float t22 = m22; float t23 = m23;
        float t30 = m30; float t31 = m31; float t32 = m32; float t33 = m33;
        float ox = origin.x(), oy = origin.y(), oz = origin.z();
        float n30 = Math.fma(t00, ox, Math.fma(t10, oy, Math.fma(t20, oz, t30)));
        float n31 = Math.fma(t01, ox, Math.fma(t11, oy, Math.fma(t21, oz, t31)));
        float n32 = Math.fma(t02, ox, Math.fma(t12, oy, Math.fma(t22, oz, t32)));
        float n33 = Math.fma(t03, ox, Math.fma(t13, oy, Math.fma(t23, oz, t33)));
        m00 = t00 * r00 + t01 * r01 + t02 * r02;
        m01 = t00 * r10 + t01 * r11 + t02 * r12;
        m02 = t00 * r20 + t01 * r21 + t02 * r22;
        m03 = t03;
        m10 = t10 * r00 + t11 * r01 + t12 * r02;
        m11 = t10 * r10 + t11 * r11 + t12 * r12;
        m12 = t10 * r20 + t11 * r21 + t12 * r22;
        m13 = t13;
        m20 = t20 * r00 + t21 * r01 + t22 * r02;
        m21 = t20 * r10 + t21 * r11 + t22 * r12;
        m22 = t20 * r20 + t21 * r21 + t22 * r22;
        m23 = t23;
        m30 = n30 - (m00 * ox + m10 * oy + m20 * oz);
        m31 = n31 - (m01 * ox + m11 * oy + m21 * oz);
        m32 = n32 - (m02 * ox + m12 * oy + m22 * oz);
        m33 = n33 - (m03 * ox + m13 * oy + m23 * oz);
        return this;
    }

    public Matrix4f zeroTranslation() {
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f;
        return this;
    }

    public Matrix4f setRow(int row, float... values) {
        float v0 = values.length > 0 ? values[0] : 0.0f;
        float v1 = values.length > 1 ? values[1] : 0.0f;
        float v2 = values.length > 2 ? values[2] : 0.0f;
        float v3 = values.length > 3 ? values[3] : 0.0f;
        if (row == 0) { m00 = v0; m10 = v1; m20 = v2; m30 = v3; }
        else if (row == 1) { m01 = v0; m11 = v1; m21 = v2; m31 = v3; }
        else if (row == 2) { m02 = v0; m12 = v1; m22 = v2; m32 = v3; }
        else if (row == 3) { m03 = v0; m13 = v1; m23 = v2; m33 = v3; }
        else throw new IndexOutOfBoundsException("Row: " + row);
        return this;
    }

    public Matrix4f setColumn(int col, float... values) {
        float v0 = values.length > 0 ? values[0] : 0.0f;
        float v1 = values.length > 1 ? values[1] : 0.0f;
        float v2 = values.length > 2 ? values[2] : 0.0f;
        float v3 = values.length > 3 ? values[3] : 0.0f;
        if (col == 0) { m00 = v0; m01 = v1; m02 = v2; m03 = v3; }
        else if (col == 1) { m10 = v0; m11 = v1; m12 = v2; m13 = v3; }
        else if (col == 2) { m20 = v0; m21 = v1; m22 = v2; m23 = v3; }
        else if (col == 3) { m30 = v0; m31 = v1; m32 = v2; m33 = v3; }
        else throw new IndexOutOfBoundsException("Column: " + col);
        return this;
    }

    public Vector4f getRow(int row) {
        if (row == 0) return new Vector4f(m00, m10, m20, m30);
        else if (row == 1) return new Vector4f(m01, m11, m21, m31);
        else if (row == 2) return new Vector4f(m02, m12, m22, m32);
        else return new Vector4f(m03, m13, m23, m33);
    }

    public Vector4f getColumn(int col) {
        if (col == 0) return new Vector4f(m00, m01, m02, m03);
        else if (col == 1) return new Vector4f(m10, m11, m12, m13);
        else if (col == 2) return new Vector4f(m20, m21, m22, m23);
        else return new Vector4f(m30, m31, m32, m33);
    }

    public Matrix4f add4x3(Matrix4f other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02;
        m10 += other.m10; m11 += other.m11; m12 += other.m12;
        m20 += other.m20; m21 += other.m21; m22 += other.m22;
        m30 += other.m30; m31 += other.m31; m32 += other.m32;
        return this;
    }

    public Matrix4f sub4x3(Matrix4f other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12;
        m20 -= other.m20; m21 -= other.m21; m22 -= other.m22;
        m30 -= other.m30; m31 -= other.m31; m32 -= other.m32;
        return this;
    }

    public Matrix4f mul4x3ComponentWise(Matrix4f other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12;
        m20 *= other.m20; m21 *= other.m21; m22 *= other.m22;
        m30 *= other.m30; m31 *= other.m31; m32 *= other.m32;
        return this;
    }

    public Matrix4f fma4x3(Matrix4f other, float scalar) {
        m00 = Math.fma(other.m00, scalar, m00);
        m01 = Math.fma(other.m01, scalar, m01);
        m02 = Math.fma(other.m02, scalar, m02);
        m10 = Math.fma(other.m10, scalar, m10);
        m11 = Math.fma(other.m11, scalar, m11);
        m12 = Math.fma(other.m12, scalar, m12);
        m20 = Math.fma(other.m20, scalar, m20);
        m21 = Math.fma(other.m21, scalar, m21);
        m22 = Math.fma(other.m22, scalar, m22);
        m30 = Math.fma(other.m30, scalar, m30);
        m31 = Math.fma(other.m31, scalar, m31);
        m32 = Math.fma(other.m32, scalar, m32);
        return this;
    }

    public Matrix4f mul0(Matrix4f right) {
        float n00 = Math.fma(m00, right.m00, Math.fma(m10, right.m01, Math.fma(m20, right.m02, m30 * right.m03)));
        float n01 = Math.fma(m01, right.m00, Math.fma(m11, right.m01, Math.fma(m21, right.m02, m31 * right.m03)));
        float n02 = Math.fma(m02, right.m00, Math.fma(m12, right.m01, Math.fma(m22, right.m02, m32 * right.m03)));
        float n03 = Math.fma(m03, right.m00, Math.fma(m13, right.m01, Math.fma(m23, right.m02, m33 * right.m03)));
        float n10 = Math.fma(m00, right.m10, Math.fma(m10, right.m11, Math.fma(m20, right.m12, m30 * right.m13)));
        float n11 = Math.fma(m01, right.m10, Math.fma(m11, right.m11, Math.fma(m21, right.m12, m31 * right.m13)));
        float n12 = Math.fma(m02, right.m10, Math.fma(m12, right.m11, Math.fma(m22, right.m12, m32 * right.m13)));
        float n13 = Math.fma(m03, right.m10, Math.fma(m13, right.m11, Math.fma(m23, right.m12, m33 * right.m13)));
        float n20 = Math.fma(m00, right.m20, Math.fma(m10, right.m21, Math.fma(m20, right.m22, m30 * right.m23)));
        float n21 = Math.fma(m01, right.m20, Math.fma(m11, right.m21, Math.fma(m21, right.m22, m31 * right.m23)));
        float n22 = Math.fma(m02, right.m20, Math.fma(m12, right.m21, Math.fma(m22, right.m22, m32 * right.m23)));
        float n23 = Math.fma(m03, right.m20, Math.fma(m13, right.m21, Math.fma(m23, right.m22, m33 * right.m23)));
        float n30 = Math.fma(m00, right.m30, Math.fma(m10, right.m31, Math.fma(m20, right.m32, m30 * right.m33)));
        float n31 = Math.fma(m01, right.m30, Math.fma(m11, right.m31, Math.fma(m21, right.m32, m31 * right.m33)));
        float n32 = Math.fma(m02, right.m30, Math.fma(m12, right.m31, Math.fma(m22, right.m32, m32 * right.m33)));
        float n33 = Math.fma(m03, right.m30, Math.fma(m13, right.m31, Math.fma(m23, right.m32, m33 * right.m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix3f getUnnormalizedRotation(Matrix3f dest) {
        dest.m00 = m00; dest.m01 = m01; dest.m02 = m02;
        dest.m10 = m10; dest.m11 = m11; dest.m12 = m12;
        dest.m20 = m20; dest.m21 = m21; dest.m22 = m22;
        return dest;
    }

    public boolean testPoint(float x, float y, float z) {
        float px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        float py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        float pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        float pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        return Math.abs(px) <= pw && Math.abs(py) <= pw && Math.abs(pz) <= pw;
    }

    public boolean testSphere(float x, float y, float z, float r) {
        float px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        float py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        float pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        float pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        float ax = Math.abs(px) + Math.fma(Math.abs(m00), r, Math.fma(Math.abs(m10), r, Math.abs(m20) * r));
        float ay = Math.abs(py) + Math.fma(Math.abs(m01), r, Math.fma(Math.abs(m11), r, Math.abs(m21) * r));
        float az = Math.abs(pz) + Math.fma(Math.abs(m02), r, Math.fma(Math.abs(m12), r, Math.abs(m22) * r));
        float nw = Math.abs(pw);
        return ax <= nw && ay <= nw && az <= nw;
    }

    public boolean testAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        float nxX = (m00 < 0 ? maxX : minX), nxY = (m10 < 0 ? maxY : minY), nxZ = (m20 < 0 ? maxZ : minZ);
        float pxX = (m00 < 0 ? minX : maxX), pxY = (m10 < 0 ? minY : maxY), pxZ = (m20 < 0 ? minZ : maxZ);
        float nyX = (m01 < 0 ? maxX : minX), nyY = (m11 < 0 ? maxY : minY), nyZ = (m21 < 0 ? maxZ : minZ);
        float pyX = (m01 < 0 ? minX : maxX), pyY = (m11 < 0 ? minY : maxY), pyZ = (m21 < 0 ? minZ : maxZ);
        float nzX = (m02 < 0 ? maxX : minX), nzY = (m12 < 0 ? maxY : minY), nzZ = (m22 < 0 ? maxZ : minZ);
        float pzX = (m02 < 0 ? minX : maxX), pzY = (m12 < 0 ? minY : maxY), pzZ = (m22 < 0 ? minZ : maxZ);
        float px = Math.fma(m00, pxX, Math.fma(m10, pxY, Math.fma(m20, pxZ, m30)));
        float nx = Math.fma(m00, nxX, Math.fma(m10, nxY, Math.fma(m20, nxZ, m30)));
        float py = Math.fma(m01, pyX, Math.fma(m11, pyY, Math.fma(m21, pyZ, m31)));
        float ny = Math.fma(m01, nyX, Math.fma(m11, nyY, Math.fma(m21, nyZ, m31)));
        float pz = Math.fma(m02, pzX, Math.fma(m12, pzY, Math.fma(m22, pzZ, m32)));
        float nz = Math.fma(m02, nzX, Math.fma(m12, nzY, Math.fma(m22, nzZ, m32)));
        float pw = Math.fma(m03, pxX, Math.fma(m13, pxY, Math.fma(m23, pxZ, m33)));
        float nw = Math.fma(m03, nxX, Math.fma(m13, nxY, Math.fma(m23, nxZ, m33)));
        return Math.max(nx, -px) <= Math.min(pw, -nw) && Math.max(ny, -py) <= Math.min(pw, -nw) && Math.max(nz, -pz) <= Math.min(pw, -nw);
    }

    public Matrix4f frustumAabb(Vector3f min, Vector3f max) {
        Matrix4f inv = new Matrix4f(this);
        inv.invert();
        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < 8; i++) {
            float x = (i & 1) == 0 ? -1.0f : 1.0f;
            float y = (i & 2) == 0 ? -1.0f : 1.0f;
            float z = (i & 4) == 0 ? -1.0f : 1.0f;
            float px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, Math.fma(inv.m20, z, inv.m30)));
            float py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, Math.fma(inv.m21, z, inv.m31)));
            float pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, Math.fma(inv.m22, z, inv.m32)));
            float pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, Math.fma(inv.m23, z, inv.m33)));
            float invW = 1.0f / pw;
            float fx = px * invW, fy = py * invW, fz = pz * invW;
            if (fx < minX) minX = fx;
            if (fy < minY) minY = fy;
            if (fz < minZ) minZ = fz;
            if (fx > maxX) maxX = fx;
            if (fy > maxY) maxY = fy;
            if (fz > maxZ) maxZ = fz;
        }
        return this;
    }

    public Matrix4f transformAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Vector3f outMin, Vector3f outMax) {
        float nxX = (m00 < 0 ? maxX : minX), nxY = (m10 < 0 ? maxY : minY), nxZ = (m20 < 0 ? maxZ : minZ);
        float pxX = (m00 < 0 ? minX : maxX), pxY = (m10 < 0 ? minY : maxY), pxZ = (m20 < 0 ? minZ : maxZ);
        float nyX = (m01 < 0 ? maxX : minX), nyY = (m11 < 0 ? maxY : minY), nyZ = (m21 < 0 ? maxZ : minZ);
        float pyX = (m01 < 0 ? minX : maxX), pyY = (m11 < 0 ? minY : maxY), pyZ = (m21 < 0 ? minZ : maxZ);
        float nzX = (m02 < 0 ? maxX : minX), nzY = (m12 < 0 ? maxY : minY), nzZ = (m22 < 0 ? maxZ : minZ);
        float pzX = (m02 < 0 ? minX : maxX), pzY = (m12 < 0 ? minY : maxY), pzZ = (m22 < 0 ? minZ : maxZ);
        float fx = Math.fma(m00, pxX, Math.fma(m10, pxY, Math.fma(m20, pxZ, m30)));
        float fy = Math.fma(m01, pyX, Math.fma(m11, pyY, Math.fma(m21, pyZ, m31)));
        float fz = Math.fma(m02, pzX, Math.fma(m12, pzY, Math.fma(m22, pzZ, m32)));
        float nx = Math.fma(m00, nxX, Math.fma(m10, nxY, Math.fma(m20, nxZ, m30)));
        float ny = Math.fma(m01, nyX, Math.fma(m11, nyY, Math.fma(m21, nyZ, m31)));
        float nz = Math.fma(m02, nzX, Math.fma(m12, nzY, Math.fma(m22, nzZ, m32)));
        return this;
    }

    public Matrix4f affineSpan(Vector3f corner00, Vector3f corner01, Vector3f corner10, Vector3f corner11) {
        return this;
    }

    public Matrix4f setTransposed(Matrix4f other) {
        m00 = other.m00; m01 = other.m10; m02 = other.m20; m03 = other.m30;
        m10 = other.m01; m11 = other.m11; m12 = other.m21; m13 = other.m31;
        m20 = other.m02; m21 = other.m12; m22 = other.m22; m23 = other.m32;
        m30 = other.m03; m31 = other.m13; m32 = other.m23; m33 = other.m33;
        return this;
    }
}
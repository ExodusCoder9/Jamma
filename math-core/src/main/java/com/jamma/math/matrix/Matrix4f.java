package com.jamma.math.matrix;

import com.jamma.math.Vector3f;
import com.jamma.math.Vector4f;
import com.jamma.math.quaternion.Quaternionf;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix4f implements Serializable {

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

    public Vector3f getTranslation(Vector3f dest) {
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

    public Vector3f getScale(Vector3f dest) {
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

    public Matrix4f perspective(float fovYRad, float aspect, float zNear, float zFar) {
        float f = 1.0f / (float) Math.tan(fovYRad * 0.5f);
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
        m22 = (zFar + zNear) * invFn;
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 2.0f * zFar * zNear * invFn;
        m33 = 0.0f;
        return this;
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

    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
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
        m22 = -2.0f * invFN;
        m23 = 0.0f;
        m30 = -(right + left) * invRL;
        m31 = -(top + bottom) * invTB;
        m32 = -(zFar + zNear) * invFN;
        m33 = 1.0f;
        return this;
    }

    public Matrix4f orthoVulkan(float left, float right, float bottom, float top, float zNear, float zFar) {
        float invRL = 1.0f / (right - left);
        float invTB = 1.0f / (top - bottom);
        float invFN = 1.0f / (zFar - zNear);
        m00 = 2.0f * invRL;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = -2.0f * invTB;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = -invFN;
        m23 = 0.0f;
        m30 = -(right + left) * invRL;
        m31 = (top + bottom) * invTB;
        m32 = -zNear * invFN;
        m33 = 1.0f;
        return this;
    }

    public Matrix4f ortho2D(float left, float right, float bottom, float top) {
        return ortho(left, right, bottom, top, -1.0f, 1.0f);
    }

    public Matrix4f frustum(float left, float right, float bottom, float top, float zNear, float zFar) {
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
        m22 = -(zFar + zNear) * invFN;
        m23 = -1.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = -2.0f * zFar * zNear * invFN;
        m33 = 0.0f;
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
        return transform(v, null);
    }

    public Vector4f transform(Vector4f v, Vector4f dest) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30 * v.w())));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31 * v.w())));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32 * v.w())));
        float w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33 * v.w())));
        return new Vector4f(x, y, z, w);
    }

    public Vector3f transformPosition(Vector3f v) {
        return transformPosition(v, null);
    }

    public Vector3f transformPosition(Vector3f v, Vector3f dest) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3f(x, y, z);
    }

    public Vector3f transformDirection(Vector3f v) {
        return transformDirection(v, null);
    }

    public Vector3f transformDirection(Vector3f v, Vector3f dest) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z()));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z()));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()));
        return new Vector3f(x, y, z);
    }

    public Vector3f transformProject(Vector3f v) {
        return transformProject(v, null);
    }

    public Vector3f transformProject(Vector3f v, Vector3f dest) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        float w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33)));
        float invW = 1.0f / w;
        return new Vector3f(x * invW, y * invW, z * invW);
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

    public Vector4f row(int row, Vector4f dest) {
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

    public Vector4f column(int col, Vector4f dest) {
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

    public Matrix4f reflection(float nx, float ny, float nz, float d) {
        float invLen = 1.0f / (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        float nnx = nx * invLen;
        float nny = ny * invLen;
        float nnz = nz * invLen;
        float da = -d * invLen;
        m00 = 1.0f - 2.0f * nnx * nnx;
        m01 = -2.0f * nny * nnx;
        m02 = -2.0f * nnz * nnx;
        m03 = 0.0f;
        m10 = -2.0f * nnx * nny;
        m11 = 1.0f - 2.0f * nny * nny;
        m12 = -2.0f * nnz * nny;
        m13 = 0.0f;
        m20 = -2.0f * nnx * nnz;
        m21 = -2.0f * nny * nnz;
        m22 = 1.0f - 2.0f * nnz * nnz;
        m23 = 0.0f;
        m30 = -2.0f * nnx * da;
        m31 = -2.0f * nny * da;
        m32 = -2.0f * nnz * da;
        m33 = 1.0f;
        return this;
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
        if (!(obj instanceof Matrix4f)) return false;
        Matrix4f other = (Matrix4f) obj;
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
        return String.format("Matrix4d[[%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f]]",
            m00, m10, m20, m30,
            m01, m11, m21, m31,
            m02, m12, m22, m32,
            m03, m13, m23, m33);
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
}
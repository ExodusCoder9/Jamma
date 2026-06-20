package com.jamma.math.matrix;

import com.jamma.math.Vector3f;
import com.jamma.math.quaternion.Quaternionf;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix4x3f implements Serializable {

    private static final long serialVersionUID = 1L;

    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;
    public float m30, m31, m32;

    public Matrix4x3f() {
        identity();
    }

    public Matrix4x3f(Matrix4x3f other) {
        set(other);
    }

    public Matrix4x3f(Matrix4f other) {
        set(other);
    }

    public Matrix4x3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22, float m30, float m31, float m32) {
        set(m00, m01, m02, m10, m11, m12, m20, m21, m22, m30, m31, m32);
    }

    public Matrix4x3f(Matrix3f mat, Vector3f translation) {
        set(mat, translation);
    }

    public Matrix4x3f identity() {
        m00 = 1.0f; m01 = 0.0f; m02 = 0.0f;
        m10 = 0.0f; m11 = 1.0f; m12 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 1.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f;
        return this;
    }

    public Matrix4x3f zero() {
        m00 = 0.0f; m01 = 0.0f; m02 = 0.0f;
        m10 = 0.0f; m11 = 0.0f; m12 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f;
        return this;
    }

    public Matrix4x3f set(Matrix4x3f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
        m20 = other.m20; m21 = other.m21; m22 = other.m22;
        m30 = other.m30; m31 = other.m31; m32 = other.m32;
        return this;
    }

    public Matrix4x3f set(Matrix4f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
        m20 = other.m20; m21 = other.m21; m22 = other.m22;
        m30 = other.m30; m31 = other.m31; m32 = other.m32;
        return this;
    }

    public Matrix4x3f set(Matrix3f mat, Vector3f translation) {
        m00 = mat.m00(); m01 = mat.m01(); m02 = mat.m02();
        m10 = mat.m10(); m11 = mat.m11(); m12 = mat.m12();
        m20 = mat.m20(); m21 = mat.m21(); m22 = mat.m22();
        m30 = translation.x(); m31 = translation.y(); m32 = translation.z();
        return this;
    }

    public Matrix4x3f set(float[] m) {
        m00 = m[0];  m01 = m[1];  m02 = m[2];
        m10 = m[3];  m11 = m[4];  m12 = m[5];
        m20 = m[6];  m21 = m[7];  m22 = m[8];
        m30 = m[9];  m31 = m[10]; m32 = m[11];
        return this;
    }

    public Matrix4x3f set(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22, float m30, float m31, float m32) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
        this.m30 = m30; this.m31 = m31; this.m32 = m32;
        return this;
    }

    public float get(int col, int row) {
        return switch (row * 4 + col) {
            case 0 -> m00; case 1 -> m01; case 2 -> m02;
            case 4 -> m10; case 5 -> m11; case 6 -> m12;
            case 8 -> m20; case 9 -> m21; case 10 -> m22;
            case 12 -> m30; case 13 -> m31; case 14 -> m32;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix4x3f set(int col, int row, float val) {
        switch (row * 4 + col) {
            case 0: m00 = val; break; case 1: m01 = val; break; case 2: m02 = val; break;
            case 4: m10 = val; break; case 5: m11 = val; break; case 6: m12 = val; break;
            case 8: m20 = val; break; case 9: m21 = val; break; case 10: m22 = val; break;
            case 12: m30 = val; break; case 13: m31 = val; break; case 14: m32 = val; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public Vector3f getRow(int row) {
        return switch (row) {
            case 0 -> new Vector3f(m00, m10, m20);
            case 1 -> new Vector3f(m01, m11, m21);
            case 2 -> new Vector3f(m02, m12, m22);
            default -> throw new IndexOutOfBoundsException("Row index: " + row);
        };
    }

    public Matrix4x3f setRow(int row, float x, float y, float z) {
        switch (row) {
            case 0: m00 = x; m10 = y; m20 = z; break;
            case 1: m01 = x; m11 = y; m21 = z; break;
            case 2: m02 = x; m12 = y; m22 = z; break;
            default: throw new IndexOutOfBoundsException("Row index: " + row);
        }
        return this;
    }

    public Matrix4x3f setRow(int row, Vector3f v) {
        return setRow(row, v.x(), v.y(), v.z());
    }

    public Vector3f getColumn(int col) {
        return switch (col) {
            case 0 -> new Vector3f(m00, m01, m02);
            case 1 -> new Vector3f(m10, m11, m12);
            case 2 -> new Vector3f(m20, m21, m22);
            case 3 -> new Vector3f(m30, m31, m32);
            default -> throw new IndexOutOfBoundsException("Column index: " + col);
        };
    }

    public Matrix4x3f setColumn(int col, float x, float y, float z) {
        switch (col) {
            case 0: m00 = x; m01 = y; m02 = z; break;
            case 1: m10 = x; m11 = y; m12 = z; break;
            case 2: m20 = x; m21 = y; m22 = z; break;
            case 3: m30 = x; m31 = y; m32 = z; break;
            default: throw new IndexOutOfBoundsException("Column index: " + col);
        }
        return this;
    }

    public Matrix4x3f setColumn(int col, Vector3f v) {
        return setColumn(col, v.x(), v.y(), v.z());
    }

    public Matrix4x3f translate(float x, float y, float z) {
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        float t30 = m30; float t31 = m31; float t32 = m32;
        m30 = Math.fma(t00, x, Math.fma(t10, y, Math.fma(t20, z, t30)));
        m31 = Math.fma(t01, x, Math.fma(t11, y, Math.fma(t21, z, t31)));
        m32 = Math.fma(t02, x, Math.fma(t12, y, Math.fma(t22, z, t32)));
        return this;
    }

    public Matrix4x3f translate(Vector3f offset) {
        return translate(offset.x(), offset.y(), offset.z());
    }

    public Matrix4x3f rotate(float ang, float x, float y, float z) {
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
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        return this;
    }

    public Matrix4x3f rotate(float ang, Vector3f axis) {
        return rotate(ang, axis.x(), axis.y(), axis.z());
    }

    public Matrix4x3f rotate(Quaternionf q) {
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
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        return this;
    }

    public Matrix4x3f rotateX(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t10 = m10; float t11 = m11; float t12 = m12;
        m10 = t10 * c + m20 * s;
        m11 = t11 * c + m21 * s;
        m12 = t12 * c + m22 * s;
        m20 = t10 * -s + m20 * c;
        m21 = t11 * -s + m21 * c;
        m22 = t12 * -s + m22 * c;
        return this;
    }

    public Matrix4x3f rotateY(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t00 = m00; float t01 = m01; float t02 = m02;
        m00 = t00 * c + m20 * -s;
        m01 = t01 * c + m21 * -s;
        m02 = t02 * c + m22 * -s;
        m20 = t00 * s + m20 * c;
        m21 = t01 * s + m21 * c;
        m22 = t02 * s + m22 * c;
        return this;
    }

    public Matrix4x3f rotateZ(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        m00 = t00 * c + t10 * s;
        m01 = t01 * c + t11 * s;
        m02 = t02 * c + t12 * s;
        m10 = t10 * c - t00 * s;
        m11 = t11 * c - t01 * s;
        m12 = t12 * c - t02 * s;
        return this;
    }

    public Matrix4x3f rotateXYZ(float angleX, float angleY, float angleZ) {
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
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        return this;
    }

    public Matrix4x3f rotateZYX(float angleZ, float angleY, float angleX) {
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
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        return this;
    }

    public Matrix4x3f rotateYXZ(float angleY, float angleX, float angleZ) {
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
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        m00 = t00 * r00 + t10 * r01 + t20 * r02;
        m01 = t01 * r00 + t11 * r01 + t21 * r02;
        m02 = t02 * r00 + t12 * r01 + t22 * r02;
        m10 = t00 * r10 + t10 * r11 + t20 * r12;
        m11 = t01 * r10 + t11 * r11 + t21 * r12;
        m12 = t02 * r10 + t12 * r11 + t22 * r12;
        m20 = t00 * r20 + t10 * r21 + t20 * r22;
        m21 = t01 * r20 + t11 * r21 + t21 * r22;
        m22 = t02 * r20 + t12 * r21 + t22 * r22;
        return this;
    }

    public Matrix4x3f rotateLocal(float ang, float x, float y, float z) {
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
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        float t30 = m30; float t31 = m31; float t32 = m32;
        m00 = t00 * r00 + t01 * r01 + t02 * r02;
        m01 = t00 * r10 + t01 * r11 + t02 * r12;
        m02 = t00 * r20 + t01 * r21 + t02 * r22;
        m10 = t10 * r00 + t11 * r01 + t12 * r02;
        m11 = t10 * r10 + t11 * r11 + t12 * r12;
        m12 = t10 * r20 + t11 * r21 + t12 * r22;
        m20 = t20 * r00 + t21 * r01 + t22 * r02;
        m21 = t20 * r10 + t21 * r11 + t22 * r12;
        m22 = t20 * r20 + t21 * r21 + t22 * r22;
        m30 = t30 * r00 + t31 * r01 + t32 * r02;
        m31 = t30 * r10 + t31 * r11 + t32 * r12;
        m32 = t30 * r20 + t31 * r21 + t32 * r22;
        return this;
    }

    public Matrix4x3f rotateLocal(Quaternionf quat) {
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
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        float t30 = m30; float t31 = m31; float t32 = m32;
        m00 = t00 * r00 + t01 * r01 + t02 * r02;
        m01 = t00 * r10 + t01 * r11 + t02 * r12;
        m02 = t00 * r20 + t01 * r21 + t02 * r22;
        m10 = t10 * r00 + t11 * r01 + t12 * r02;
        m11 = t10 * r10 + t11 * r11 + t12 * r12;
        m12 = t10 * r20 + t11 * r21 + t12 * r22;
        m20 = t20 * r00 + t21 * r01 + t22 * r02;
        m21 = t20 * r10 + t21 * r11 + t22 * r12;
        m22 = t20 * r20 + t21 * r21 + t22 * r22;
        m30 = t30 * r00 + t31 * r01 + t32 * r02;
        m31 = t30 * r10 + t31 * r11 + t32 * r12;
        m32 = t30 * r20 + t31 * r21 + t32 * r22;
        return this;
    }

    public Matrix4x3f rotateLocalX(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t20 = m20; float t21 = m21; float t22 = m22;
        float t30 = m30; float t31 = m31; float t32 = m32;
        m10 = t10 * c + t20 * s;
        m11 = t11 * c + t21 * s;
        m12 = t12 * c + t22 * s;
        m20 = t10 * -s + t20 * c;
        m21 = t11 * -s + t21 * c;
        m22 = t12 * -s + t22 * c;
        return this;
    }

    public Matrix4x3f rotateLocalY(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t20 = m20; float t21 = m21; float t22 = m22;
        float t30 = m30; float t31 = m31; float t32 = m32;
        m00 = t00 * c + t20 * -s;
        m01 = t01 * c + t21 * -s;
        m02 = t02 * c + t22 * -s;
        m20 = t00 * s + t20 * c;
        m21 = t01 * s + t21 * c;
        m22 = t02 * s + t22 * c;
        return this;
    }

    public Matrix4x3f rotateLocalZ(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        float t00 = m00; float t01 = m01; float t02 = m02;
        float t10 = m10; float t11 = m11; float t12 = m12;
        float t30 = m30; float t31 = m31; float t32 = m32;
        m00 = t00 * c + t10 * -s;
        m01 = t01 * c + t11 * -s;
        m02 = t02 * c + t12 * -s;
        m10 = t00 * s + t10 * c;
        m11 = t01 * s + t11 * c;
        m12 = t02 * s + t12 * c;
        return this;
    }

    public Matrix4x3f scale(float x, float y, float z) {
        m00 *= x; m01 *= x; m02 *= x;
        m10 *= y; m11 *= y; m12 *= y;
        m20 *= z; m21 *= z; m22 *= z;
        return this;
    }

    public Matrix4x3f scale(float factor) {
        return scale(factor, factor, factor);
    }

    public Matrix4x3f scale(Vector3f xyz) {
        return scale(xyz.x(), xyz.y(), xyz.z());
    }

    public Matrix4x3f scale(float xy, float z) {
        return scale(xy, xy, z);
    }

    public Matrix4x3f mul(Matrix4x3f right) {
        float n00 = Math.fma(m00, right.m00, Math.fma(m10, right.m01, m20 * right.m02));
        float n01 = Math.fma(m01, right.m00, Math.fma(m11, right.m01, m21 * right.m02));
        float n02 = Math.fma(m02, right.m00, Math.fma(m12, right.m01, m22 * right.m02));
        float n10 = Math.fma(m00, right.m10, Math.fma(m10, right.m11, m20 * right.m12));
        float n11 = Math.fma(m01, right.m10, Math.fma(m11, right.m11, m21 * right.m12));
        float n12 = Math.fma(m02, right.m10, Math.fma(m12, right.m11, m22 * right.m12));
        float n20 = Math.fma(m00, right.m20, Math.fma(m10, right.m21, m20 * right.m22));
        float n21 = Math.fma(m01, right.m20, Math.fma(m11, right.m21, m21 * right.m22));
        float n22 = Math.fma(m02, right.m20, Math.fma(m12, right.m21, m22 * right.m22));
        float n30 = Math.fma(m00, right.m30, Math.fma(m10, right.m31, Math.fma(m20, right.m32, m30)));
        float n31 = Math.fma(m01, right.m30, Math.fma(m11, right.m31, Math.fma(m21, right.m32, m31)));
        float n32 = Math.fma(m02, right.m30, Math.fma(m12, right.m31, Math.fma(m22, right.m32, m32)));
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        m20 = n20; m21 = n21; m22 = n22;
        m30 = n30; m31 = n31; m32 = n32;
        return this;
    }

    public Matrix4x3f mulLocal(Matrix4x3f left) {
        float n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, left.m02 * m20));
        float n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, left.m02 * m21));
        float n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, left.m02 * m22));
        float n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, left.m12 * m20));
        float n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, left.m12 * m21));
        float n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, left.m12 * m22));
        float n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, left.m22 * m20));
        float n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, left.m22 * m21));
        float n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, left.m22 * m22));
        float n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, m30)));
        float n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, m31)));
        float n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, m32)));
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        m20 = n20; m21 = n21; m22 = n22;
        m30 = n30; m31 = n31; m32 = n32;
        return this;
    }

    public Matrix4x3f mulAffine(Matrix4x3f right) {
        return mul(right);
    }

    public Matrix4x3f mulTranslation(Matrix4x3f... others) {
        for (Matrix4x3f m : others) {
            mul(m);
        }
        return this;
    }

    public float determinant() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix4x3f invert() {
        float a00 = m00, a01 = m01, a02 = m02;
        float a10 = m10, a11 = m11, a12 = m12;
        float a20 = m20, a21 = m21, a22 = m22;
        float det = a00 * (a11 * a22 - a12 * a21)
                  - a01 * (a10 * a22 - a12 * a20)
                  + a02 * (a10 * a21 - a11 * a20);
        if (det == 0.0f) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        float invDet = 1.0f / det;
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
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        m20 = n20; m21 = n21; m22 = n22;
        m30 = -(n00 * tx + n01 * ty + n02 * tz);
        m31 = -(n10 * tx + n11 * ty + n12 * tz);
        m32 = -(n20 * tx + n21 * ty + n22 * tz);
        return this;
    }

    public Vector3f transformPosition(Vector3f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3f(x, y, z);
    }

    public Vector3f transformPosition(float x, float y, float z) {
        float px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        float py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        float pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        return new Vector3f(px, py, pz);
    }

    public Vector3f transformDirection(Vector3f v) {
        float x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z()));
        float y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z()));
        float z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()));
        return new Vector3f(x, y, z);
    }

    public Vector3f transformDirection(float x, float y, float z) {
        float dx = Math.fma(m00, x, Math.fma(m10, y, m20 * z));
        float dy = Math.fma(m01, x, Math.fma(m11, y, m21 * z));
        float dz = Math.fma(m02, x, Math.fma(m12, y, m22 * z));
        return new Vector3f(dx, dy, dz);
    }

    public Vector3f transformAffine(Vector3f v) {
        return transformPosition(v);
    }

    public Vector3f transformAffine(float x, float y, float z) {
        return transformPosition(x, y, z);
    }

    public static Matrix4x3f translation(float x, float y, float z) {
        return new Matrix4x3f(
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            x, y, z
        );
    }

    public static Matrix4x3f translation(Vector3f offset) {
        return translation(offset.x(), offset.y(), offset.z());
    }

    public static Matrix4x3f rotation(float ang, float x, float y, float z) {
        return new Matrix4x3f().rotate(ang, x, y, z);
    }

    public static Matrix4x3f rotation(float ang, Vector3f axis) {
        return rotation(ang, axis.x(), axis.y(), axis.z());
    }

    public static Matrix4x3f rotation(Quaternionf quat) {
        return new Matrix4x3f().rotate(quat);
    }

    public static Matrix4x3f rotationX(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        return new Matrix4x3f(
            1.0f, 0.0f, 0.0f,
            0.0f, c, s,
            0.0f, -s, c,
            0.0f, 0.0f, 0.0f
        );
    }

    public static Matrix4x3f rotationY(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        return new Matrix4x3f(
            c, 0.0f, -s,
            0.0f, 1.0f, 0.0f,
            s, 0.0f, c,
            0.0f, 0.0f, 0.0f
        );
    }

    public static Matrix4x3f rotationZ(float ang) {
        float s = (float) Math.sin(ang);
        float c = (float) Math.cos(ang);
        return new Matrix4x3f(
            c, s, 0.0f,
            -s, c, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f
        );
    }

    public static Matrix4x3f rotationXYZ(float angleX, float angleY, float angleZ) {
        return new Matrix4x3f().rotateXYZ(angleX, angleY, angleZ);
    }

    public static Matrix4x3f rotationZYX(float angleZ, float angleY, float angleX) {
        return new Matrix4x3f().rotateZYX(angleZ, angleY, angleX);
    }

    public static Matrix4x3f rotationYXZ(float angleY, float angleX, float angleZ) {
        return new Matrix4x3f().rotateYXZ(angleY, angleX, angleZ);
    }

    public static Matrix4x3f scaling(float scale) {
        return scaling(scale, scale, scale);
    }

    public static Matrix4x3f scaling(float x, float y, float z) {
        return new Matrix4x3f(
            x, 0.0f, 0.0f,
            0.0f, y, 0.0f,
            0.0f, 0.0f, z,
            0.0f, 0.0f, 0.0f
        );
    }

    public static Matrix4x3f scaling(Vector3f scale) {
        return scaling(scale.x(), scale.y(), scale.z());
    }

    public static Matrix4x3f translationRotateScale(float tx, float ty, float tz, float qx, float qy, float qz, float qw, float sx, float sy, float sz) {
        Matrix4x3f m = new Matrix4x3f();
        m.translate(tx, ty, tz);
        m.rotate(new Quaternionf(qx, qy, qz, qw));
        m.scale(sx, sy, sz);
        return m;
    }

    public static Matrix4x3f translationRotateScale(Vector3f translation, Quaternionf quat, Vector3f scale) {
        return translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    public Matrix4x3f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
        return lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4x3f lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
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
        m10 = sY;
        m11 = uY;
        m12 = -fY;
        m20 = sZ;
        m21 = uZ;
        m22 = -fZ;
        m30 = -(sX * eyeX + sY * eyeY + sZ * eyeZ);
        m31 = -(uX * eyeX + uY * eyeY + uZ * eyeZ);
        m32 = fX * eyeX + fY * eyeY + fZ * eyeZ;
        return this;
    }

    public Matrix4x3f lookAtLH(Vector3f eye, Vector3f center, Vector3f up) {
        return lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4x3f lookAtLH(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
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
        m10 = sY;
        m11 = uY;
        m12 = fY;
        m20 = sZ;
        m21 = uZ;
        m22 = fZ;
        m30 = -(sX * eyeX + sY * eyeY + sZ * eyeZ);
        m31 = -(uX * eyeX + uY * eyeY + uZ * eyeZ);
        m32 = -(fX * eyeX + fY * eyeY + fZ * eyeZ);
        return this;
    }

    public Matrix4x3f add(Matrix4x3f other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02;
        m10 += other.m10; m11 += other.m11; m12 += other.m12;
        m20 += other.m20; m21 += other.m21; m22 += other.m22;
        m30 += other.m30; m31 += other.m31; m32 += other.m32;
        return this;
    }

    public Matrix4x3f sub(Matrix4x3f other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12;
        m20 -= other.m20; m21 -= other.m21; m22 -= other.m22;
        m30 -= other.m30; m31 -= other.m31; m32 -= other.m32;
        return this;
    }

    public Matrix4x3f mulComponentWise(Matrix4x3f other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12;
        m20 *= other.m20; m21 *= other.m21; m22 *= other.m22;
        m30 *= other.m30; m31 *= other.m31; m32 *= other.m32;
        return this;
    }

    public Matrix4x3f negate() {
        m00 = -m00; m01 = -m01; m02 = -m02;
        m10 = -m10; m11 = -m11; m12 = -m12;
        m20 = -m20; m21 = -m21; m22 = -m22;
        m30 = -m30; m31 = -m31; m32 = -m32;
        return this;
    }

    public Matrix4x3f normalize3x3() {
        float inv00 = 1.0f / (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        m00 *= inv00; m01 *= inv00; m02 *= inv00;
        float inv10 = 1.0f / (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        m10 *= inv10; m11 *= inv10; m12 *= inv10;
        float inv20 = 1.0f / (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        m20 *= inv20; m21 *= inv20; m22 *= inv20;
        return this;
    }

    public Matrix4x3f zeroTranslation() {
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f;
        return this;
    }

    public Matrix4x3f setTranslation(float x, float y, float z) {
        m30 = x; m31 = y; m32 = z;
        return this;
    }

    public Matrix4x3f setTranslation(Vector3f trans) {
        return setTranslation(trans.x(), trans.y(), trans.z());
    }

    public float determinant3x3() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public boolean isFinite() {
        return Float.isFinite(m00) && Float.isFinite(m01) && Float.isFinite(m02)
            && Float.isFinite(m10) && Float.isFinite(m11) && Float.isFinite(m12)
            && Float.isFinite(m20) && Float.isFinite(m21) && Float.isFinite(m22)
            && Float.isFinite(m30) && Float.isFinite(m31) && Float.isFinite(m32);
    }

    public Matrix4f toMatrix4f() {
        return new Matrix4f(
            m00, m01, m02, 0.0f,
            m10, m11, m12, 0.0f,
            m20, m21, m22, 0.0f,
            m30, m31, m32, 1.0f
        );
    }

    public Matrix3f toMatrix3f() {
        return new Matrix3f(new float[] {
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22
        });
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(m00, m01, m02, m10, m11, m12, m20, m21, m22, m30, m31, m32);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix4x3f)) return false;
        Matrix4x3f other = (Matrix4x3f) obj;
        return Float.floatToIntBits(m00) == Float.floatToIntBits(other.m00)
            && Float.floatToIntBits(m01) == Float.floatToIntBits(other.m01)
            && Float.floatToIntBits(m02) == Float.floatToIntBits(other.m02)
            && Float.floatToIntBits(m10) == Float.floatToIntBits(other.m10)
            && Float.floatToIntBits(m11) == Float.floatToIntBits(other.m11)
            && Float.floatToIntBits(m12) == Float.floatToIntBits(other.m12)
            && Float.floatToIntBits(m20) == Float.floatToIntBits(other.m20)
            && Float.floatToIntBits(m21) == Float.floatToIntBits(other.m21)
            && Float.floatToIntBits(m22) == Float.floatToIntBits(other.m22)
            && Float.floatToIntBits(m30) == Float.floatToIntBits(other.m30)
            && Float.floatToIntBits(m31) == Float.floatToIntBits(other.m31)
            && Float.floatToIntBits(m32) == Float.floatToIntBits(other.m32);
    }

    @Override
    public String toString() {
        return String.format("Matrix4x3f[[%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f]]",
            m00, m10, m20, m30,
            m01, m11, m21, m31,
            m02, m12, m22, m32);
    }

    public Matrix4x3f swap(Matrix4x3f other) {
        float t;
        t = m00; m00 = other.m00; other.m00 = t;
        t = m01; m01 = other.m01; other.m01 = t;
        t = m02; m02 = other.m02; other.m02 = t;
        t = m10; m10 = other.m10; other.m10 = t;
        t = m11; m11 = other.m11; other.m11 = t;
        t = m12; m12 = other.m12; other.m12 = t;
        t = m20; m20 = other.m20; other.m20 = t;
        t = m21; m21 = other.m21; other.m21 = t;
        t = m22; m22 = other.m22; other.m22 = t;
        t = m30; m30 = other.m30; other.m30 = t;
        t = m31; m31 = other.m31; other.m31 = t;
        t = m32; m32 = other.m32; other.m32 = t;
        return this;
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, m00);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, m01);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, m02);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 12, m10);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 16, m11);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 20, m12);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 24, m20);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 28, m21);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 32, m22);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 36, m30);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 40, m31);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 44, m32);
    }

    public static Matrix4x3f read(MemorySegment segment, long offset) {
        return new Matrix4x3f(
            segment.get(ValueLayout.JAVA_FLOAT, offset),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 4),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 8),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 12),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 16),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 20),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 24),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 28),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 32),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 36),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 40),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 44)
        );
    }
}

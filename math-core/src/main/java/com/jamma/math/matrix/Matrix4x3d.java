package com.jamma.math.matrix;

import com.jamma.math.Vector3D;
import com.jamma.math.quaternion.Quaterniond;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix4x3d implements Serializable {

    private static final long serialVersionUID = 1L;

    public double m00, m01, m02;
    public double m10, m11, m12;
    public double m20, m21, m22;
    public double m30, m31, m32;

    public Matrix4x3d() {
        identity();
    }

    public Matrix4x3d(Matrix4x3d other) {
        set(other);
    }

    public Matrix4x3d(Matrix4d other) {
        set(other);
    }

    public Matrix4x3d(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22, double m30, double m31, double m32) {
        set(m00, m01, m02, m10, m11, m12, m20, m21, m22, m30, m31, m32);
    }

    public Matrix4x3d(Matrix3d mat, Vector3D translation) {
        set(mat, translation);
    }

    public Matrix4x3d identity() {
        m00 = 1.0; m01 = 0.0; m02 = 0.0;
        m10 = 0.0; m11 = 1.0; m12 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 1.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0;
        return this;
    }

    public Matrix4x3d zero() {
        m00 = 0.0; m01 = 0.0; m02 = 0.0;
        m10 = 0.0; m11 = 0.0; m12 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 0.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0;
        return this;
    }

    public Matrix4x3d set(Matrix4x3d other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
        m20 = other.m20; m21 = other.m21; m22 = other.m22;
        m30 = other.m30; m31 = other.m31; m32 = other.m32;
        return this;
    }

    public Matrix4x3d set(Matrix4d other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02;
        m10 = other.m10; m11 = other.m11; m12 = other.m12;
        m20 = other.m20; m21 = other.m21; m22 = other.m22;
        m30 = other.m30; m31 = other.m31; m32 = other.m32;
        return this;
    }

    public Matrix4x3d set(Matrix3d mat, Vector3D translation) {
        m00 = mat.m00(); m01 = mat.m01(); m02 = mat.m02();
        m10 = mat.m10(); m11 = mat.m11(); m12 = mat.m12();
        m20 = mat.m20(); m21 = mat.m21(); m22 = mat.m22();
        m30 = translation.x(); m31 = translation.y(); m32 = translation.z();
        return this;
    }

    public Matrix4x3d set(double[] m) {
        m00 = m[0];  m01 = m[1];  m02 = m[2];
        m10 = m[3];  m11 = m[4];  m12 = m[5];
        m20 = m[6];  m21 = m[7];  m22 = m[8];
        m30 = m[9];  m31 = m[10]; m32 = m[11];
        return this;
    }

    public Matrix4x3d set(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22, double m30, double m31, double m32) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
        this.m30 = m30; this.m31 = m31; this.m32 = m32;
        return this;
    }

    public double get(int col, int row) {
        return switch (row * 4 + col) {
            case 0 -> m00; case 1 -> m01; case 2 -> m02;
            case 4 -> m10; case 5 -> m11; case 6 -> m12;
            case 8 -> m20; case 9 -> m21; case 10 -> m22;
            case 12 -> m30; case 13 -> m31; case 14 -> m32;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix4x3d set(int col, int row, double val) {
        switch (row * 4 + col) {
            case 0: m00 = val; break; case 1: m01 = val; break; case 2: m02 = val; break;
            case 4: m10 = val; break; case 5: m11 = val; break; case 6: m12 = val; break;
            case 8: m20 = val; break; case 9: m21 = val; break; case 10: m22 = val; break;
            case 12: m30 = val; break; case 13: m31 = val; break; case 14: m32 = val; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public Vector3D getRow(int row) {
        return switch (row) {
            case 0 -> new Vector3D(m00, m10, m20);
            case 1 -> new Vector3D(m01, m11, m21);
            case 2 -> new Vector3D(m02, m12, m22);
            default -> throw new IndexOutOfBoundsException("Row index: " + row);
        };
    }

    public Matrix4x3d setRow(int row, double x, double y, double z) {
        switch (row) {
            case 0: m00 = x; m10 = y; m20 = z; break;
            case 1: m01 = x; m11 = y; m21 = z; break;
            case 2: m02 = x; m12 = y; m22 = z; break;
            default: throw new IndexOutOfBoundsException("Row index: " + row);
        }
        return this;
    }

    public Matrix4x3d setRow(int row, Vector3D v) {
        return setRow(row, v.x(), v.y(), v.z());
    }

    public Vector3D getColumn(int col) {
        return switch (col) {
            case 0 -> new Vector3D(m00, m01, m02);
            case 1 -> new Vector3D(m10, m11, m12);
            case 2 -> new Vector3D(m20, m21, m22);
            case 3 -> new Vector3D(m30, m31, m32);
            default -> throw new IndexOutOfBoundsException("Column index: " + col);
        };
    }

    public Matrix4x3d setColumn(int col, double x, double y, double z) {
        switch (col) {
            case 0: m00 = x; m01 = y; m02 = z; break;
            case 1: m10 = x; m11 = y; m12 = z; break;
            case 2: m20 = x; m21 = y; m22 = z; break;
            case 3: m30 = x; m31 = y; m32 = z; break;
            default: throw new IndexOutOfBoundsException("Column index: " + col);
        }
        return this;
    }

    public Matrix4x3d setColumn(int col, Vector3D v) {
        return setColumn(col, v.x(), v.y(), v.z());
    }

    public Matrix4x3d translate(double x, double y, double z) {
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
        double t30 = m30; double t31 = m31; double t32 = m32;
        m30 = Math.fma(t00, x, Math.fma(t10, y, Math.fma(t20, z, t30)));
        m31 = Math.fma(t01, x, Math.fma(t11, y, Math.fma(t21, z, t31)));
        m32 = Math.fma(t02, x, Math.fma(t12, y, Math.fma(t22, z, t32)));
        return this;
    }

    public Matrix4x3d translate(Vector3D offset) {
        return translate(offset.x(), offset.y(), offset.z());
    }

    public Matrix4x3d rotate(double ang, double x, double y, double z) {
        double invLen = 1.0 / Math.sqrt(x * x + y * y + z * z);
        double nx = x * invLen;
        double ny = y * invLen;
        double nz = z * invLen;
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t = 1.0 - c;
        double r00 = t * nx * nx + c;
        double r01 = t * nx * ny + s * nz;
        double r02 = t * nx * nz - s * ny;
        double r10 = t * nx * ny - s * nz;
        double r11 = t * ny * ny + c;
        double r12 = t * ny * nz + s * nx;
        double r20 = t * nx * nz + s * ny;
        double r21 = t * ny * nz - s * nx;
        double r22 = t * nz * nz + c;
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
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

    public Matrix4x3d rotate(double ang, Vector3D axis) {
        return rotate(ang, axis.x(), axis.y(), axis.z());
    }

    public Matrix4x3d rotate(Quaterniond q) {
        double x = q.x(), y = q.y(), z = q.z(), w = q.w();
        double r00 = 1.0 - 2.0 * (y * y + z * z);
        double r01 = 2.0 * (x * y + w * z);
        double r02 = 2.0 * (x * z - w * y);
        double r10 = 2.0 * (x * y - w * z);
        double r11 = 1.0 - 2.0 * (x * x + z * z);
        double r12 = 2.0 * (y * z + w * x);
        double r20 = 2.0 * (x * z + w * y);
        double r21 = 2.0 * (y * z - w * x);
        double r22 = 1.0 - 2.0 * (x * x + y * y);
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
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

    public Matrix4x3d rotateX(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t10 = m10; double t11 = m11; double t12 = m12;
        m10 = t10 * c + m20 * s;
        m11 = t11 * c + m21 * s;
        m12 = t12 * c + m22 * s;
        m20 = t10 * -s + m20 * c;
        m21 = t11 * -s + m21 * c;
        m22 = t12 * -s + m22 * c;
        return this;
    }

    public Matrix4x3d rotateY(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t00 = m00; double t01 = m01; double t02 = m02;
        m00 = t00 * c + m20 * -s;
        m01 = t01 * c + m21 * -s;
        m02 = t02 * c + m22 * -s;
        m20 = t00 * s + m20 * c;
        m21 = t01 * s + m21 * c;
        m22 = t02 * s + m22 * c;
        return this;
    }

    public Matrix4x3d rotateZ(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        m00 = t00 * c + t10 * s;
        m01 = t01 * c + t11 * s;
        m02 = t02 * c + t12 * s;
        m10 = t10 * c - t00 * s;
        m11 = t11 * c - t01 * s;
        m12 = t12 * c - t02 * s;
        return this;
    }

    public Matrix4x3d rotateXYZ(double angleX, double angleY, double angleZ) {
        double cx = Math.cos(angleX), sx = Math.sin(angleX);
        double cy = Math.cos(angleY), sy = Math.sin(angleY);
        double cz = Math.cos(angleZ), sz = Math.sin(angleZ);
        double r00 = cy * cz;
        double r01 = sx * sy * cz + cx * sz;
        double r02 = -cx * sy * cz + sx * sz;
        double r10 = -cy * sz;
        double r11 = -sx * sy * sz + cx * cz;
        double r12 = cx * sy * sz + sx * cz;
        double r20 = sy;
        double r21 = -sx * cy;
        double r22 = cx * cy;
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
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

    public Matrix4x3d rotateZYX(double angleZ, double angleY, double angleX) {
        double cz = Math.cos(angleZ), sz = Math.sin(angleZ);
        double cy = Math.cos(angleY), sy = Math.sin(angleY);
        double cx = Math.cos(angleX), sx = Math.sin(angleX);
        double r00 = cz * cy;
        double r01 = sz * cy;
        double r02 = -sy;
        double r10 = -sz * cx + cz * sy * sx;
        double r11 = cz * cx + sz * sy * sx;
        double r12 = cy * sx;
        double r20 = sz * sx + cz * sy * cx;
        double r21 = -cz * sx + sz * sy * cx;
        double r22 = cy * cx;
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
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

    public Matrix4x3d rotateYXZ(double angleY, double angleX, double angleZ) {
        double cy = Math.cos(angleY), sy = Math.sin(angleY);
        double cx = Math.cos(angleX), sx = Math.sin(angleX);
        double cz = Math.cos(angleZ), sz = Math.sin(angleZ);
        double r00 = cy * cz + sy * sx * sz;
        double r01 = cy * sz - sy * sx * cz;
        double r02 = sy * cx;
        double r10 = cx * sz;
        double r11 = cx * cz;
        double r12 = -sx;
        double r20 = -sy * cz + cy * sx * sz;
        double r21 = -sy * sz - cy * sx * cz;
        double r22 = cy * cx;
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
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

    public Matrix4x3d rotateLocal(double ang, double x, double y, double z) {
        double invLen = 1.0 / Math.sqrt(x * x + y * y + z * z);
        double nx = x * invLen;
        double ny = y * invLen;
        double nz = z * invLen;
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t = 1.0 - c;
        double r00 = t * nx * nx + c;
        double r01 = t * nx * ny + s * nz;
        double r02 = t * nx * nz - s * ny;
        double r10 = t * nx * ny - s * nz;
        double r11 = t * ny * ny + c;
        double r12 = t * ny * nz + s * nx;
        double r20 = t * nx * nz + s * ny;
        double r21 = t * ny * nz - s * nx;
        double r22 = t * nz * nz + c;
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
        double t30 = m30; double t31 = m31; double t32 = m32;
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

    public Matrix4x3d rotateLocal(Quaterniond quat) {
        double x = quat.x(), y = quat.y(), z = quat.z(), w = quat.w();
        double r00 = 1.0 - 2.0 * (y * y + z * z);
        double r01 = 2.0 * (x * y + w * z);
        double r02 = 2.0 * (x * z - w * y);
        double r10 = 2.0 * (x * y - w * z);
        double r11 = 1.0 - 2.0 * (x * x + z * z);
        double r12 = 2.0 * (y * z + w * x);
        double r20 = 2.0 * (x * z + w * y);
        double r21 = 2.0 * (y * z - w * x);
        double r22 = 1.0 - 2.0 * (x * x + y * y);
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
        double t30 = m30; double t31 = m31; double t32 = m32;
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

    public Matrix4x3d rotateLocalX(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t20 = m20; double t21 = m21; double t22 = m22;
        double t30 = m30; double t31 = m31; double t32 = m32;
        m10 = t10 * c + t20 * s;
        m11 = t11 * c + t21 * s;
        m12 = t12 * c + t22 * s;
        m20 = t10 * -s + t20 * c;
        m21 = t11 * -s + t21 * c;
        m22 = t12 * -s + t22 * c;
        return this;
    }

    public Matrix4x3d rotateLocalY(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t20 = m20; double t21 = m21; double t22 = m22;
        double t30 = m30; double t31 = m31; double t32 = m32;
        m00 = t00 * c + t20 * -s;
        m01 = t01 * c + t21 * -s;
        m02 = t02 * c + t22 * -s;
        m20 = t00 * s + t20 * c;
        m21 = t01 * s + t21 * c;
        m22 = t02 * s + t22 * c;
        return this;
    }

    public Matrix4x3d rotateLocalZ(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        double t00 = m00; double t01 = m01; double t02 = m02;
        double t10 = m10; double t11 = m11; double t12 = m12;
        double t30 = m30; double t31 = m31; double t32 = m32;
        m00 = t00 * c + t10 * -s;
        m01 = t01 * c + t11 * -s;
        m02 = t02 * c + t12 * -s;
        m10 = t00 * s + t10 * c;
        m11 = t01 * s + t11 * c;
        m12 = t02 * s + t12 * c;
        return this;
    }

    public Matrix4x3d scale(double x, double y, double z) {
        m00 *= x; m01 *= x; m02 *= x;
        m10 *= y; m11 *= y; m12 *= y;
        m20 *= z; m21 *= z; m22 *= z;
        return this;
    }

    public Matrix4x3d scale(double factor) {
        return scale(factor, factor, factor);
    }

    public Matrix4x3d scale(Vector3D xyz) {
        return scale(xyz.x(), xyz.y(), xyz.z());
    }

    public Matrix4x3d scale(double xy, double z) {
        return scale(xy, xy, z);
    }

    public Matrix4x3d mul(Matrix4x3d right) {
        double n00 = Math.fma(m00, right.m00, Math.fma(m10, right.m01, m20 * right.m02));
        double n01 = Math.fma(m01, right.m00, Math.fma(m11, right.m01, m21 * right.m02));
        double n02 = Math.fma(m02, right.m00, Math.fma(m12, right.m01, m22 * right.m02));
        double n10 = Math.fma(m00, right.m10, Math.fma(m10, right.m11, m20 * right.m12));
        double n11 = Math.fma(m01, right.m10, Math.fma(m11, right.m11, m21 * right.m12));
        double n12 = Math.fma(m02, right.m10, Math.fma(m12, right.m11, m22 * right.m12));
        double n20 = Math.fma(m00, right.m20, Math.fma(m10, right.m21, m20 * right.m22));
        double n21 = Math.fma(m01, right.m20, Math.fma(m11, right.m21, m21 * right.m22));
        double n22 = Math.fma(m02, right.m20, Math.fma(m12, right.m21, m22 * right.m22));
        double n30 = Math.fma(m00, right.m30, Math.fma(m10, right.m31, Math.fma(m20, right.m32, m30)));
        double n31 = Math.fma(m01, right.m30, Math.fma(m11, right.m31, Math.fma(m21, right.m32, m31)));
        double n32 = Math.fma(m02, right.m30, Math.fma(m12, right.m31, Math.fma(m22, right.m32, m32)));
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        m20 = n20; m21 = n21; m22 = n22;
        m30 = n30; m31 = n31; m32 = n32;
        return this;
    }

    public Matrix4x3d mulLocal(Matrix4x3d left) {
        double n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, left.m02 * m20));
        double n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, left.m02 * m21));
        double n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, left.m02 * m22));
        double n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, left.m12 * m20));
        double n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, left.m12 * m21));
        double n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, left.m12 * m22));
        double n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, left.m22 * m20));
        double n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, left.m22 * m21));
        double n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, left.m22 * m22));
        double n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, m30)));
        double n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, m31)));
        double n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, m32)));
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        m20 = n20; m21 = n21; m22 = n22;
        m30 = n30; m31 = n31; m32 = n32;
        return this;
    }

    public Matrix4x3d mulAffine(Matrix4x3d right) {
        return mul(right);
    }

    public Matrix4x3d mulTranslation(Matrix4x3d... others) {
        for (Matrix4x3d m : others) {
            mul(m);
        }
        return this;
    }

    public double determinant() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix4x3d invert() {
        double a00 = m00, a01 = m01, a02 = m02;
        double a10 = m10, a11 = m11, a12 = m12;
        double a20 = m20, a21 = m21, a22 = m22;
        double det = a00 * (a11 * a22 - a12 * a21)
                   - a01 * (a10 * a22 - a12 * a20)
                   + a02 * (a10 * a21 - a11 * a20);
        if (det == 0.0) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        double invDet = 1.0 / det;
        double n00 = (a11 * a22 - a12 * a21) * invDet;
        double n01 = (a02 * a21 - a01 * a22) * invDet;
        double n02 = (a01 * a12 - a02 * a11) * invDet;
        double n10 = (a12 * a20 - a10 * a22) * invDet;
        double n11 = (a00 * a22 - a02 * a20) * invDet;
        double n12 = (a02 * a10 - a00 * a12) * invDet;
        double n20 = (a10 * a21 - a11 * a20) * invDet;
        double n21 = (a01 * a20 - a00 * a21) * invDet;
        double n22 = (a00 * a11 - a01 * a10) * invDet;
        double tx = m30, ty = m31, tz = m32;
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        m20 = n20; m21 = n21; m22 = n22;
        m30 = -(n00 * tx + n01 * ty + n02 * tz);
        m31 = -(n10 * tx + n11 * ty + n12 * tz);
        m32 = -(n20 * tx + n21 * ty + n22 * tz);
        return this;
    }

    public Vector3D transformPosition(Vector3D v) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3D(x, y, z);
    }

    public Vector3D transformPosition(double x, double y, double z) {
        double px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        double py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        double pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        return new Vector3D(px, py, pz);
    }

    public Vector3D transformDirection(Vector3D v) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z()));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z()));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()));
        return new Vector3D(x, y, z);
    }

    public Vector3D transformDirection(double x, double y, double z) {
        double dx = Math.fma(m00, x, Math.fma(m10, y, m20 * z));
        double dy = Math.fma(m01, x, Math.fma(m11, y, m21 * z));
        double dz = Math.fma(m02, x, Math.fma(m12, y, m22 * z));
        return new Vector3D(dx, dy, dz);
    }

    public Vector3D transformAffine(Vector3D v) {
        return transformPosition(v);
    }

    public Vector3D transformAffine(double x, double y, double z) {
        return transformPosition(x, y, z);
    }

    public static Matrix4x3d translation(double x, double y, double z) {
        return new Matrix4x3d(
            1.0, 0.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 0.0, 1.0,
            x, y, z
        );
    }

    public static Matrix4x3d translation(Vector3D offset) {
        return translation(offset.x(), offset.y(), offset.z());
    }

    public static Matrix4x3d rotation(double ang, double x, double y, double z) {
        return new Matrix4x3d().rotate(ang, x, y, z);
    }

    public static Matrix4x3d rotation(double ang, Vector3D axis) {
        return rotation(ang, axis.x(), axis.y(), axis.z());
    }

    public static Matrix4x3d rotation(Quaterniond quat) {
        return new Matrix4x3d().rotate(quat);
    }

    public static Matrix4x3d rotationX(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        return new Matrix4x3d(
            1.0, 0.0, 0.0,
            0.0, c, s,
            0.0, -s, c,
            0.0, 0.0, 0.0
        );
    }

    public static Matrix4x3d rotationY(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        return new Matrix4x3d(
            c, 0.0, -s,
            0.0, 1.0, 0.0,
            s, 0.0, c,
            0.0, 0.0, 0.0
        );
    }

    public static Matrix4x3d rotationZ(double ang) {
        double s = Math.sin(ang);
        double c = Math.cos(ang);
        return new Matrix4x3d(
            c, s, 0.0,
            -s, c, 0.0,
            0.0, 0.0, 1.0,
            0.0, 0.0, 0.0
        );
    }

    public static Matrix4x3d rotationXYZ(double angleX, double angleY, double angleZ) {
        return new Matrix4x3d().rotateXYZ(angleX, angleY, angleZ);
    }

    public static Matrix4x3d rotationZYX(double angleZ, double angleY, double angleX) {
        return new Matrix4x3d().rotateZYX(angleZ, angleY, angleX);
    }

    public static Matrix4x3d rotationYXZ(double angleY, double angleX, double angleZ) {
        return new Matrix4x3d().rotateYXZ(angleY, angleX, angleZ);
    }

    public static Matrix4x3d scaling(double scale) {
        return scaling(scale, scale, scale);
    }

    public static Matrix4x3d scaling(double x, double y, double z) {
        return new Matrix4x3d(
            x, 0.0, 0.0,
            0.0, y, 0.0,
            0.0, 0.0, z,
            0.0, 0.0, 0.0
        );
    }

    public static Matrix4x3d scaling(Vector3D scale) {
        return scaling(scale.x(), scale.y(), scale.z());
    }

    public static Matrix4x3d translationRotateScale(double tx, double ty, double tz, double qx, double qy, double qz, double qw, double sx, double sy, double sz) {
        Matrix4x3d m = new Matrix4x3d();
        m.translate(tx, ty, tz);
        m.rotate(new Quaterniond(qx, qy, qz, qw));
        m.scale(sx, sy, sz);
        return m;
    }

    public static Matrix4x3d translationRotateScale(Vector3D translation, Quaterniond quat, Vector3D scale) {
        return translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }

    public Matrix4x3d lookAt(Vector3D eye, Vector3D center, Vector3D up) {
        return lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4x3d lookAt(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ) {
        double fX = centerX - eyeX;
        double fY = centerY - eyeY;
        double fZ = centerZ - eyeZ;
        double invFLen = 1.0 / Math.sqrt(fX * fX + fY * fY + fZ * fZ);
        fX *= invFLen;
        fY *= invFLen;
        fZ *= invFLen;
        double sX = fY * upZ - fZ * upY;
        double sY = fZ * upX - fX * upZ;
        double sZ = fX * upY - fY * upX;
        double invSLen = 1.0 / Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        double uX = sY * fZ - sZ * fY;
        double uY = sZ * fX - sX * fZ;
        double uZ = sX * fY - sY * fX;
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

    public Matrix4x3d lookAtLH(Vector3D eye, Vector3D center, Vector3D up) {
        return lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4x3d lookAtLH(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ) {
        double fX = centerX - eyeX;
        double fY = centerY - eyeY;
        double fZ = centerZ - eyeZ;
        double invFLen = 1.0 / Math.sqrt(fX * fX + fY * fY + fZ * fZ);
        fX *= invFLen;
        fY *= invFLen;
        fZ *= invFLen;
        double sX = fY * upZ - fZ * upY;
        double sY = fZ * upX - fX * upZ;
        double sZ = fX * upY - fY * upX;
        double invSLen = 1.0 / Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        double uX = sY * fZ - sZ * fY;
        double uY = sZ * fX - sX * fZ;
        double uZ = sX * fY - sY * fX;
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

    public Matrix4x3d add(Matrix4x3d other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02;
        m10 += other.m10; m11 += other.m11; m12 += other.m12;
        m20 += other.m20; m21 += other.m21; m22 += other.m22;
        m30 += other.m30; m31 += other.m31; m32 += other.m32;
        return this;
    }

    public Matrix4x3d sub(Matrix4x3d other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12;
        m20 -= other.m20; m21 -= other.m21; m22 -= other.m22;
        m30 -= other.m30; m31 -= other.m31; m32 -= other.m32;
        return this;
    }

    public Matrix4x3d mulComponentWise(Matrix4x3d other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12;
        m20 *= other.m20; m21 *= other.m21; m22 *= other.m22;
        m30 *= other.m30; m31 *= other.m31; m32 *= other.m32;
        return this;
    }

    public Matrix4x3d negate() {
        m00 = -m00; m01 = -m01; m02 = -m02;
        m10 = -m10; m11 = -m11; m12 = -m12;
        m20 = -m20; m21 = -m21; m22 = -m22;
        m30 = -m30; m31 = -m31; m32 = -m32;
        return this;
    }

    public Matrix4x3d normalize3x3() {
        double inv00 = 1.0 / Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        m00 *= inv00; m01 *= inv00; m02 *= inv00;
        double inv10 = 1.0 / Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        m10 *= inv10; m11 *= inv10; m12 *= inv10;
        double inv20 = 1.0 / Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        m20 *= inv20; m21 *= inv20; m22 *= inv20;
        return this;
    }

    public Matrix4x3d zeroTranslation() {
        m30 = 0.0; m31 = 0.0; m32 = 0.0;
        return this;
    }

    public Matrix4x3d setTranslation(double x, double y, double z) {
        m30 = x; m31 = y; m32 = z;
        return this;
    }

    public Matrix4x3d setTranslation(Vector3D trans) {
        return setTranslation(trans.x(), trans.y(), trans.z());
    }

    public double determinant3x3() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public boolean isFinite() {
        return Double.isFinite(m00) && Double.isFinite(m01) && Double.isFinite(m02)
            && Double.isFinite(m10) && Double.isFinite(m11) && Double.isFinite(m12)
            && Double.isFinite(m20) && Double.isFinite(m21) && Double.isFinite(m22)
            && Double.isFinite(m30) && Double.isFinite(m31) && Double.isFinite(m32);
    }

    public Matrix4d toMatrix4d() {
        return new Matrix4d(
            m00, m01, m02, 0.0,
            m10, m11, m12, 0.0,
            m20, m21, m22, 0.0,
            m30, m31, m32, 1.0
        );
    }

    public Matrix3d toMatrix3d() {
        return new Matrix3d(new double[] {
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
        if (!(obj instanceof Matrix4x3d)) return false;
        Matrix4x3d other = (Matrix4x3d) obj;
        return Double.doubleToLongBits(m00) == Double.doubleToLongBits(other.m00)
            && Double.doubleToLongBits(m01) == Double.doubleToLongBits(other.m01)
            && Double.doubleToLongBits(m02) == Double.doubleToLongBits(other.m02)
            && Double.doubleToLongBits(m10) == Double.doubleToLongBits(other.m10)
            && Double.doubleToLongBits(m11) == Double.doubleToLongBits(other.m11)
            && Double.doubleToLongBits(m12) == Double.doubleToLongBits(other.m12)
            && Double.doubleToLongBits(m20) == Double.doubleToLongBits(other.m20)
            && Double.doubleToLongBits(m21) == Double.doubleToLongBits(other.m21)
            && Double.doubleToLongBits(m22) == Double.doubleToLongBits(other.m22)
            && Double.doubleToLongBits(m30) == Double.doubleToLongBits(other.m30)
            && Double.doubleToLongBits(m31) == Double.doubleToLongBits(other.m31)
            && Double.doubleToLongBits(m32) == Double.doubleToLongBits(other.m32);
    }

    @Override
    public String toString() {
        return String.format("Matrix4x3d[[%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f]]",
            m00, m10, m20, m30,
            m01, m11, m21, m31,
            m02, m12, m22, m32);
    }

    public Matrix4x3d swap(Matrix4x3d other) {
        double t;
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
        segment.set(ValueLayout.JAVA_DOUBLE, offset, m00);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 8, m01);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 16, m02);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 24, m10);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 32, m11);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 40, m12);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 48, m20);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 56, m21);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 64, m22);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 72, m30);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 80, m31);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 88, m32);
    }

    public static Matrix4x3d read(MemorySegment segment, long offset) {
        return new Matrix4x3d(
            segment.get(ValueLayout.JAVA_DOUBLE, offset),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 8),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 16),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 24),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 32),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 40),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 48),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 56),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 64),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 72),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 80),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 88)
        );
    }
}

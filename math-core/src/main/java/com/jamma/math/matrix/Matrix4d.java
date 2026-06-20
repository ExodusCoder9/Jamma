package com.jamma.math.matrix;

import com.jamma.math.Vector3D;
import com.jamma.math.Vector4D;
import com.jamma.math.quaternion.Quaterniond;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix4d implements Serializable {

    private static final long serialVersionUID = 1L;

    public double m00, m01, m02, m03;
    public double m10, m11, m12, m13;
    public double m20, m21, m22, m23;
    public double m30, m31, m32, m33;

    public Matrix4d() {
        identity();
    }

    public Matrix4d(Matrix4d other) {
        set(other);
    }

    public Matrix4d(double[] m) {
        set(m);
    }

    public Matrix4d(
        double m00, double m01, double m02, double m03,
        double m10, double m11, double m12, double m13,
        double m20, double m21, double m22, double m23,
        double m30, double m31, double m32, double m33
    ) {
        set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public Matrix4d set(Matrix4d other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02; m03 = other.m03;
        m10 = other.m10; m11 = other.m11; m12 = other.m12; m13 = other.m13;
        m20 = other.m20; m21 = other.m21; m22 = other.m22; m23 = other.m23;
        m30 = other.m30; m31 = other.m31; m32 = other.m32; m33 = other.m33;
        return this;
    }

    public Matrix4d set(double[] m) {
        m00 = m[0];  m01 = m[1];  m02 = m[2];  m03 = m[3];
        m10 = m[4];  m11 = m[5];  m12 = m[6];  m13 = m[7];
        m20 = m[8];  m21 = m[9];  m22 = m[10]; m23 = m[11];
        m30 = m[12]; m31 = m[13]; m32 = m[14]; m33 = m[15];
        return this;
    }

    public Matrix4d set(
        double m00, double m01, double m02, double m03,
        double m10, double m11, double m12, double m13,
        double m20, double m21, double m22, double m23,
        double m30, double m31, double m32, double m33
    ) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
        return this;
    }

    public Matrix4d identity() {
        m00 = 1.0; m01 = 0.0; m02 = 0.0; m03 = 0.0;
        m10 = 0.0; m11 = 1.0; m12 = 0.0; m13 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 1.0; m23 = 0.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0; m33 = 1.0;
        return this;
    }

    public Matrix4d zero() {
        m00 = 0.0; m01 = 0.0; m02 = 0.0; m03 = 0.0;
        m10 = 0.0; m11 = 0.0; m12 = 0.0; m13 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 0.0; m23 = 0.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0; m33 = 0.0;
        return this;
    }

    public Matrix4d translate(double x, double y, double z) {
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
        double t30 = m30; double t31 = m31; double t32 = m32; double t33 = m33;
        m30 = Math.fma(t00, x, Math.fma(t10, y, Math.fma(t20, z, t30)));
        m31 = Math.fma(t01, x, Math.fma(t11, y, Math.fma(t21, z, t31)));
        m32 = Math.fma(t02, x, Math.fma(t12, y, Math.fma(t22, z, t32)));
        m33 = Math.fma(t03, x, Math.fma(t13, y, Math.fma(t23, z, t33)));
        return this;
    }

    public Matrix4d translate(Vector3D offset) {
        return translate(offset.x(), offset.y(), offset.z());
    }

    public Vector3D getTranslation(Vector3D dest) {
        return new Vector3D(m30, m31, m32);
    }

    public Matrix4d setTranslation(double x, double y, double z) {
        m30 = x;
        m31 = y;
        m32 = z;
        return this;
    }

    public Matrix4d setTranslation(Vector3D v) {
        return setTranslation(v.x(), v.y(), v.z());
    }
    public Matrix4d rotate(double angle, double x, double y, double z) {
        double invLen = 1.0 / Math.sqrt(x * x + y * y + z * z);
        double nx = x * invLen;
        double ny = y * invLen;
        double nz = z * invLen;
        double s = Math.sin(angle);
        double c = Math.cos(angle);
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
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
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

    public Matrix4d rotate(Quaterniond q) {
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
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
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

    public Matrix4d rotateXYZ(double angleX, double angleY, double angleZ) {
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
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
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

    public Matrix4d rotateZYX(double angleZ, double angleY, double angleX) {
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
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
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

    public Matrix4d rotateX(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
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

    public Matrix4d rotateY(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
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

    public Matrix4d rotateZ(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
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

    public Matrix4d scale(double x, double y, double z) {
        m00 *= x; m01 *= x; m02 *= x; m03 *= x;
        m10 *= y; m11 *= y; m12 *= y; m13 *= y;
        m20 *= z; m21 *= z; m22 *= z; m23 *= z;
        return this;
    }

    public Matrix4d scale(double factor) {
        return scale(factor, factor, factor);
    }

    public Matrix4d scale(Vector3D xyz) {
        return scale(xyz.x(), xyz.y(), xyz.z());
    }

    public Vector3D getScale(Vector3D dest) {
        double sx = Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        double sy = Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        double sz = Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        return new Vector3D(sx, sy, sz);
    }

    public Matrix4d reflect(double nx, double ny, double nz) {
        double invLen = 1.0 / Math.sqrt(nx * nx + ny * ny + nz * nz);
        double nnx = nx * invLen;
        double nny = ny * invLen;
        double nnz = nz * invLen;
        double r00 = 1.0 - 2.0 * nnx * nnx;
        double r01 = -2.0 * nnx * nny;
        double r02 = -2.0 * nnx * nnz;
        double r10 = -2.0 * nny * nnx;
        double r11 = 1.0 - 2.0 * nny * nny;
        double r12 = -2.0 * nny * nnz;
        double r20 = -2.0 * nnz * nnx;
        double r21 = -2.0 * nnz * nny;
        double r22 = 1.0 - 2.0 * nnz * nnz;
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
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

    public Matrix4d reflect(Vector3D normal) {
        return reflect(normal.x(), normal.y(), normal.z());
    }

    public Matrix4d perspective(double fovYRad, double aspect, double zNear, double zFar) {
        double f = 1.0 / Math.tan(fovYRad * 0.5);
        double invFn = 1.0 / (zNear - zFar);
        m00 = f / aspect;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = f;
        m12 = 0.0;
        m13 = 0.0;
        m20 = 0.0;
        m21 = 0.0;
        m22 = (zFar + zNear) * invFn;
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m32 = 2.0 * zFar * zNear * invFn;
        m33 = 0.0;
        return this;
    }

    public Matrix4d perspectiveVulkan(double fovYRad, double aspect, double zNear, double zFar) {
        double f = 1.0 / Math.tan(fovYRad * 0.5);
        double invFn = 1.0 / (zNear - zFar);
        m00 = f / aspect;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = -f;
        m12 = 0.0;
        m13 = 0.0;
        m20 = 0.0;
        m21 = 0.0;
        m22 = zFar * invFn;
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m32 = zFar * zNear * invFn;
        m33 = 0.0;
        return this;
    }

    public Matrix4d ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
        double invRL = 1.0 / (right - left);
        double invTB = 1.0 / (top - bottom);
        double invFN = 1.0 / (zFar - zNear);
        m00 = 2.0 * invRL;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = 2.0 * invTB;
        m12 = 0.0;
        m13 = 0.0;
        m20 = 0.0;
        m21 = 0.0;
        m22 = -2.0 * invFN;
        m23 = 0.0;
        m30 = -(right + left) * invRL;
        m31 = -(top + bottom) * invTB;
        m32 = -(zFar + zNear) * invFN;
        m33 = 1.0;
        return this;
    }

    public Matrix4d orthoVulkan(double left, double right, double bottom, double top, double zNear, double zFar) {
        double invRL = 1.0 / (right - left);
        double invTB = 1.0 / (top - bottom);
        double invFN = 1.0 / (zFar - zNear);
        m00 = 2.0 * invRL;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = -2.0 * invTB;
        m12 = 0.0;
        m13 = 0.0;
        m20 = 0.0;
        m21 = 0.0;
        m22 = -invFN;
        m23 = 0.0;
        m30 = -(right + left) * invRL;
        m31 = (top + bottom) * invTB;
        m32 = -zNear * invFN;
        m33 = 1.0;
        return this;
    }

    public Matrix4d ortho2D(double left, double right, double bottom, double top) {
        return ortho(left, right, bottom, top, -1.0, 1.0);
    }

    public Matrix4d frustum(double left, double right, double bottom, double top, double zNear, double zFar) {
        double invRL = 1.0 / (right - left);
        double invTB = 1.0 / (top - bottom);
        double invFN = 1.0 / (zFar - zNear);
        m00 = 2.0 * zNear * invRL;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = 2.0 * zNear * invTB;
        m12 = 0.0;
        m13 = 0.0;
        m20 = (right + left) * invRL;
        m21 = (top + bottom) * invTB;
        m22 = -(zFar + zNear) * invFN;
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m32 = -2.0 * zFar * zNear * invFN;
        m33 = 0.0;
        return this;
    }
    public Matrix4d lookAt(double eyeX, double eyeY, double eyeZ,
            double centerX, double centerY, double centerZ,
            double upX, double upY, double upZ) {
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
        m03 = 0.0;
        m10 = sY;
        m11 = uY;
        m12 = -fY;
        m13 = 0.0;
        m20 = sZ;
        m21 = uZ;
        m22 = -fZ;
        m23 = 0.0;
        m30 = -(sX * eyeX + sY * eyeY + sZ * eyeZ);
        m31 = -(uX * eyeX + uY * eyeY + uZ * eyeZ);
        m32 = fX * eyeX + fY * eyeY + fZ * eyeZ;
        m33 = 1.0;
        return this;
    }

    public Matrix4d lookAt(Vector3D eye, Vector3D center, Vector3D up) {
        return lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4d lookAlong(double dirX, double dirY, double dirZ, double upX, double upY, double upZ) {
        double invFLen = 1.0 / Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        double fX = dirX * invFLen;
        double fY = dirY * invFLen;
        double fZ = dirZ * invFLen;
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
        m03 = 0.0;
        m10 = sY;
        m11 = uY;
        m12 = -fY;
        m13 = 0.0;
        m20 = sZ;
        m21 = uZ;
        m22 = -fZ;
        m23 = 0.0;
        m30 = 0.0;
        m31 = 0.0;
        m32 = 0.0;
        m33 = 1.0;
        return this;
    }

    public Matrix4d multiply(Matrix4d other) {
        double n00 = Math.fma(m00, other.m00, Math.fma(m10, other.m01, Math.fma(m20, other.m02, m30 * other.m03)));
        double n01 = Math.fma(m01, other.m00, Math.fma(m11, other.m01, Math.fma(m21, other.m02, m31 * other.m03)));
        double n02 = Math.fma(m02, other.m00, Math.fma(m12, other.m01, Math.fma(m22, other.m02, m32 * other.m03)));
        double n03 = Math.fma(m03, other.m00, Math.fma(m13, other.m01, Math.fma(m23, other.m02, m33 * other.m03)));
        double n10 = Math.fma(m00, other.m10, Math.fma(m10, other.m11, Math.fma(m20, other.m12, m30 * other.m13)));
        double n11 = Math.fma(m01, other.m10, Math.fma(m11, other.m11, Math.fma(m21, other.m12, m31 * other.m13)));
        double n12 = Math.fma(m02, other.m10, Math.fma(m12, other.m11, Math.fma(m22, other.m12, m32 * other.m13)));
        double n13 = Math.fma(m03, other.m10, Math.fma(m13, other.m11, Math.fma(m23, other.m12, m33 * other.m13)));
        double n20 = Math.fma(m00, other.m20, Math.fma(m10, other.m21, Math.fma(m20, other.m22, m30 * other.m23)));
        double n21 = Math.fma(m01, other.m20, Math.fma(m11, other.m21, Math.fma(m21, other.m22, m31 * other.m23)));
        double n22 = Math.fma(m02, other.m20, Math.fma(m12, other.m21, Math.fma(m22, other.m22, m32 * other.m23)));
        double n23 = Math.fma(m03, other.m20, Math.fma(m13, other.m21, Math.fma(m23, other.m22, m33 * other.m23)));
        double n30 = Math.fma(m00, other.m30, Math.fma(m10, other.m31, Math.fma(m20, other.m32, m30 * other.m33)));
        double n31 = Math.fma(m01, other.m30, Math.fma(m11, other.m31, Math.fma(m21, other.m32, m31 * other.m33)));
        double n32 = Math.fma(m02, other.m30, Math.fma(m12, other.m31, Math.fma(m22, other.m32, m32 * other.m33)));
        double n33 = Math.fma(m03, other.m30, Math.fma(m13, other.m31, Math.fma(m23, other.m32, m33 * other.m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4d mulAffine(Matrix4d other) {
        double n00 = Math.fma(m00, other.m00, Math.fma(m10, other.m01, m20 * other.m02));
        double n01 = Math.fma(m01, other.m00, Math.fma(m11, other.m01, m21 * other.m02));
        double n02 = Math.fma(m02, other.m00, Math.fma(m12, other.m01, m22 * other.m02));
        double n03 = Math.fma(m03, other.m00, Math.fma(m13, other.m01, m23 * other.m02));
        double n10 = Math.fma(m00, other.m10, Math.fma(m10, other.m11, m20 * other.m12));
        double n11 = Math.fma(m01, other.m10, Math.fma(m11, other.m11, m21 * other.m12));
        double n12 = Math.fma(m02, other.m10, Math.fma(m12, other.m11, m22 * other.m12));
        double n13 = Math.fma(m03, other.m10, Math.fma(m13, other.m11, m23 * other.m12));
        double n20 = Math.fma(m00, other.m20, Math.fma(m10, other.m21, m20 * other.m22));
        double n21 = Math.fma(m01, other.m20, Math.fma(m11, other.m21, m21 * other.m22));
        double n22 = Math.fma(m02, other.m20, Math.fma(m12, other.m21, m22 * other.m22));
        double n23 = Math.fma(m03, other.m20, Math.fma(m13, other.m21, m23 * other.m22));
        double n30 = Math.fma(m00, other.m30, Math.fma(m10, other.m31, Math.fma(m20, other.m32, m30)));
        double n31 = Math.fma(m01, other.m30, Math.fma(m11, other.m31, Math.fma(m21, other.m32, m31)));
        double n32 = Math.fma(m02, other.m30, Math.fma(m12, other.m31, Math.fma(m22, other.m32, m32)));
        double n33 = Math.fma(m03, other.m30, Math.fma(m13, other.m31, Math.fma(m23, other.m32, m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4d add(Matrix4d other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02; m03 += other.m03;
        m10 += other.m10; m11 += other.m11; m12 += other.m12; m13 += other.m13;
        m20 += other.m20; m21 += other.m21; m22 += other.m22; m23 += other.m23;
        m30 += other.m30; m31 += other.m31; m32 += other.m32; m33 += other.m33;
        return this;
    }

    public Matrix4d sub(Matrix4d other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02; m03 -= other.m03;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12; m13 -= other.m13;
        m20 -= other.m20; m21 -= other.m21; m22 -= other.m22; m23 -= other.m23;
        m30 -= other.m30; m31 -= other.m31; m32 -= other.m32; m33 -= other.m33;
        return this;
    }

    public Matrix4d mulComponentWise(Matrix4d other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02; m03 *= other.m03;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12; m13 *= other.m13;
        m20 *= other.m20; m21 *= other.m21; m22 *= other.m22; m23 *= other.m23;
        m30 *= other.m30; m31 *= other.m31; m32 *= other.m32; m33 *= other.m33;
        return this;
    }

    public Matrix4d lerp(Matrix4d other, double t) {
        double t1 = 1.0 - t;
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

    public double determinant() {
        return (m00 * (m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31))
              - m01 * (m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30))
              + m02 * (m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30))
              - m03 * (m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30)));
    }
    public Matrix4d invert() {
        double det = determinant();
        if (det == 0.0) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        double invDet = 1.0 / det;
        double n00 = (m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31)) * invDet;
        double n01 = -(m01 * (m22 * m33 - m23 * m32) - m02 * (m21 * m33 - m23 * m31) + m03 * (m21 * m32 - m22 * m31)) * invDet;
        double n02 = (m01 * (m12 * m33 - m13 * m32) - m02 * (m11 * m33 - m13 * m31) + m03 * (m11 * m32 - m12 * m31)) * invDet;
        double n03 = -(m01 * (m12 * m23 - m13 * m22) - m02 * (m11 * m23 - m13 * m21) + m03 * (m11 * m22 - m12 * m21)) * invDet;
        double n10 = -(m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30)) * invDet;
        double n11 = (m00 * (m22 * m33 - m23 * m32) - m02 * (m20 * m33 - m23 * m30) + m03 * (m20 * m32 - m22 * m30)) * invDet;
        double n12 = -(m00 * (m12 * m33 - m13 * m32) - m02 * (m10 * m33 - m13 * m30) + m03 * (m10 * m32 - m12 * m30)) * invDet;
        double n13 = (m00 * (m12 * m23 - m13 * m22) - m02 * (m10 * m23 - m13 * m20) + m03 * (m10 * m22 - m12 * m20)) * invDet;
        double n20 = (m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30)) * invDet;
        double n21 = -(m00 * (m21 * m33 - m23 * m31) - m01 * (m20 * m33 - m23 * m30) + m03 * (m20 * m31 - m21 * m30)) * invDet;
        double n22 = (m00 * (m11 * m33 - m13 * m31) - m01 * (m10 * m33 - m13 * m30) + m03 * (m10 * m31 - m11 * m30)) * invDet;
        double n23 = -(m00 * (m11 * m23 - m13 * m21) - m01 * (m10 * m23 - m13 * m20) + m03 * (m10 * m21 - m11 * m20)) * invDet;
        double n30 = -(m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30)) * invDet;
        double n31 = (m00 * (m21 * m32 - m22 * m31) - m01 * (m20 * m32 - m22 * m30) + m02 * (m20 * m31 - m21 * m30)) * invDet;
        double n32 = -(m00 * (m11 * m32 - m12 * m31) - m01 * (m10 * m32 - m12 * m30) + m02 * (m10 * m31 - m11 * m30)) * invDet;
        double n33 = (m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20)) * invDet;
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4d invertAffine() {
        double a00 = m00, a01 = m01, a02 = m02;
        double a10 = m10, a11 = m11, a12 = m12;
        double a20 = m20, a21 = m21, a22 = m22;
        double det3 = a00 * (a11 * a22 - a12 * a21)
                    - a01 * (a10 * a22 - a12 * a20)
                    + a02 * (a10 * a21 - a11 * a20);
        if (det3 == 0.0) {
            throw new ArithmeticException("Matrix is singular, cannot invert");
        }
        double invDet = 1.0 / det3;
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
        m00 = n00; m01 = n01; m02 = n02; m03 = 0.0;
        m10 = n10; m11 = n11; m12 = n12; m13 = 0.0;
        m20 = n20; m21 = n21; m22 = n22; m23 = 0.0;
        m30 = -(n00 * tx + n01 * ty + n02 * tz);
        m31 = -(n10 * tx + n11 * ty + n12 * tz);
        m32 = -(n20 * tx + n21 * ty + n22 * tz);
        m33 = 1.0;
        return this;
    }

    public Matrix4d transpose() {
        double t;
        t = m01; m01 = m10; m10 = t;
        t = m02; m02 = m20; m20 = t;
        t = m03; m03 = m30; m30 = t;
        t = m12; m12 = m21; m21 = t;
        t = m13; m13 = m31; m31 = t;
        t = m23; m23 = m32; m32 = t;
        return this;
    }

    public Matrix4d transpose3x3() {
        double t;
        t = m01; m01 = m10; m10 = t;
        t = m02; m02 = m20; m20 = t;
        t = m12; m12 = m21; m21 = t;
        return this;
    }

    public Matrix4d adjugate() {
        double a00 = (m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31));
        double a01 = -(m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30));
        double a02 = (m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30));
        double a03 = -(m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30));
        double a10 = -(m01 * (m22 * m33 - m23 * m32) - m02 * (m21 * m33 - m23 * m31) + m03 * (m21 * m32 - m22 * m31));
        double a11 = (m00 * (m22 * m33 - m23 * m32) - m02 * (m20 * m33 - m23 * m30) + m03 * (m20 * m32 - m22 * m30));
        double a12 = -(m00 * (m21 * m33 - m23 * m31) - m01 * (m20 * m33 - m23 * m30) + m03 * (m20 * m31 - m21 * m30));
        double a13 = (m00 * (m21 * m32 - m22 * m31) - m01 * (m20 * m32 - m22 * m30) + m02 * (m20 * m31 - m21 * m30));
        double a20 = (m01 * (m12 * m33 - m13 * m32) - m02 * (m11 * m33 - m13 * m31) + m03 * (m11 * m32 - m12 * m31));
        double a21 = -(m00 * (m12 * m33 - m13 * m32) - m02 * (m10 * m33 - m13 * m30) + m03 * (m10 * m32 - m12 * m30));
        double a22 = (m00 * (m11 * m33 - m13 * m31) - m01 * (m10 * m33 - m13 * m30) + m03 * (m10 * m31 - m11 * m30));
        double a23 = -(m00 * (m11 * m32 - m12 * m31) - m01 * (m10 * m32 - m12 * m30) + m02 * (m10 * m31 - m11 * m30));
        double a30 = -(m01 * (m12 * m23 - m13 * m22) - m02 * (m11 * m23 - m13 * m21) + m03 * (m11 * m22 - m12 * m21));
        double a31 = (m00 * (m12 * m23 - m13 * m22) - m02 * (m10 * m23 - m13 * m20) + m03 * (m10 * m22 - m12 * m20));
        double a32 = -(m00 * (m11 * m23 - m13 * m21) - m01 * (m10 * m23 - m13 * m20) + m03 * (m10 * m21 - m11 * m20));
        double a33 = (m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20));
        m00 = a00; m01 = a10; m02 = a20; m03 = a30;
        m10 = a01; m11 = a11; m12 = a21; m13 = a31;
        m20 = a02; m21 = a12; m22 = a22; m23 = a32;
        m30 = a03; m31 = a13; m32 = a23; m33 = a33;
        return this;
    }

    public double trace() {
        return m00 + m11 + m22 + m33;
    }

    public Matrix4d normal() {
        double a00 = m00, a01 = m01, a02 = m02;
        double a10 = m10, a11 = m11, a12 = m12;
        double a20 = m20, a21 = m21, a22 = m22;
        double det3 = a00 * (a11 * a22 - a12 * a21)
                    - a01 * (a10 * a22 - a12 * a20)
                    + a02 * (a10 * a21 - a11 * a20);
        if (det3 == 0.0) {
            throw new ArithmeticException("Matrix is singular, cannot compute normal matrix");
        }
        double invDet = 1.0 / det3;
        double n00 = (a11 * a22 - a12 * a21) * invDet;
        double n01 = (a02 * a21 - a01 * a22) * invDet;
        double n02 = (a01 * a12 - a02 * a11) * invDet;
        double n10 = (a12 * a20 - a10 * a22) * invDet;
        double n11 = (a00 * a22 - a02 * a20) * invDet;
        double n12 = (a02 * a10 - a00 * a12) * invDet;
        double n20 = (a10 * a21 - a11 * a20) * invDet;
        double n21 = (a01 * a20 - a00 * a21) * invDet;
        double n22 = (a00 * a11 - a01 * a10) * invDet;
        m00 = n00; m01 = n10; m02 = n20; m03 = 0.0;
        m10 = n01; m11 = n11; m12 = n21; m13 = 0.0;
        m20 = n02; m21 = n12; m22 = n22; m23 = 0.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0; m33 = 1.0;
        return this;
    }

    public Vector4D transform(Vector4D v) {
        return transform(v, null);
    }

    public Vector4D transform(Vector4D v, Vector4D dest) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30 * v.w())));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31 * v.w())));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32 * v.w())));
        double w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33 * v.w())));
        return new Vector4D(x, y, z, w);
    }

    public Vector3D transformPosition(Vector3D v) {
        return transformPosition(v, null);
    }

    public Vector3D transformPosition(Vector3D v, Vector3D dest) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3D(x, y, z);
    }

    public Vector3D transformDirection(Vector3D v) {
        return transformDirection(v, null);
    }

    public Vector3D transformDirection(Vector3D v, Vector3D dest) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z()));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z()));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()));
        return new Vector3D(x, y, z);
    }

    public Vector3D transformProject(Vector3D v) {
        return transformProject(v, null);
    }

    public Vector3D transformProject(Vector3D v, Vector3D dest) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        double w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33)));
        double invW = 1.0 / w;
        return new Vector3D(x * invW, y * invW, z * invW);
    }
    public double m00() { return m00; }
    public double m01() { return m01; }
    public double m02() { return m02; }
    public double m03() { return m03; }
    public double m10() { return m10; }
    public double m11() { return m11; }
    public double m12() { return m12; }
    public double m13() { return m13; }
    public double m20() { return m20; }
    public double m21() { return m21; }
    public double m22() { return m22; }
    public double m23() { return m23; }
    public double m30() { return m30; }
    public double m31() { return m31; }
    public double m32() { return m32; }
    public double m33() { return m33; }

    public Matrix4d m00(double v) { m00 = v; return this; }
    public Matrix4d m01(double v) { m01 = v; return this; }
    public Matrix4d m02(double v) { m02 = v; return this; }
    public Matrix4d m03(double v) { m03 = v; return this; }
    public Matrix4d m10(double v) { m10 = v; return this; }
    public Matrix4d m11(double v) { m11 = v; return this; }
    public Matrix4d m12(double v) { m12 = v; return this; }
    public Matrix4d m13(double v) { m13 = v; return this; }
    public Matrix4d m20(double v) { m20 = v; return this; }
    public Matrix4d m21(double v) { m21 = v; return this; }
    public Matrix4d m22(double v) { m22 = v; return this; }
    public Matrix4d m23(double v) { m23 = v; return this; }
    public Matrix4d m30(double v) { m30 = v; return this; }
    public Matrix4d m31(double v) { m31 = v; return this; }
    public Matrix4d m32(double v) { m32 = v; return this; }
    public Matrix4d m33(double v) { m33 = v; return this; }

    public double[] get(double[] dest, int offset) {
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

    public double[] get(double[] dest) {
        return get(dest, 0);
    }

    public double[] get3x3(double[] dest, int offset) {
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

    public Vector4D row(int row, Vector4D dest) {
        if (row == 0) {
            return new Vector4D(m00, m10, m20, m30);
        } else if (row == 1) {
            return new Vector4D(m01, m11, m21, m31);
        } else if (row == 2) {
            return new Vector4D(m02, m12, m22, m32);
        } else {
            return new Vector4D(m03, m13, m23, m33);
        }
    }

    public Vector4D column(int col, Vector4D dest) {
        if (col == 0) {
            return new Vector4D(m00, m01, m02, m03);
        } else if (col == 1) {
            return new Vector4D(m10, m11, m12, m13);
        } else if (col == 2) {
            return new Vector4D(m20, m21, m22, m23);
        } else {
            return new Vector4D(m30, m31, m32, m33);
        }
    }

    public boolean isAffine() {
        return m03 == 0.0 && m13 == 0.0 && m23 == 0.0 && m33 == 1.0;
    }

    public boolean isIdentity() {
        return m00 == 1.0 && m01 == 0.0 && m02 == 0.0 && m03 == 0.0
            && m10 == 0.0 && m11 == 1.0 && m12 == 0.0 && m13 == 0.0
            && m20 == 0.0 && m21 == 0.0 && m22 == 1.0 && m23 == 0.0
            && m30 == 0.0 && m31 == 0.0 && m32 == 0.0 && m33 == 1.0;
    }

    public boolean isIdentity(double epsilon) {
        return Math.abs(m00 - 1.0) <= epsilon && Math.abs(m01) <= epsilon && Math.abs(m02) <= epsilon && Math.abs(m03) <= epsilon
            && Math.abs(m10) <= epsilon && Math.abs(m11 - 1.0) <= epsilon && Math.abs(m12) <= epsilon && Math.abs(m13) <= epsilon
            && Math.abs(m20) <= epsilon && Math.abs(m21) <= epsilon && Math.abs(m22 - 1.0) <= epsilon && Math.abs(m23) <= epsilon
            && Math.abs(m30) <= epsilon && Math.abs(m31) <= epsilon && Math.abs(m32) <= epsilon && Math.abs(m33 - 1.0) <= epsilon;
    }

    public Matrix4d billboard(Vector3D objPos, Vector3D target, Vector3D up) {
        double dirX = objPos.x() - target.x();
        double dirY = objPos.y() - target.y();
        double dirZ = objPos.z() - target.z();
        double invDirLen = 1.0 / Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        double dX = dirX * invDirLen;
        double dY = dirY * invDirLen;
        double dZ = dirZ * invDirLen;
        double uX = up.x(), uY = up.y(), uZ = up.z();
        double rX = uY * dZ - uZ * dY;
        double rY = uZ * dX - uX * dZ;
        double rZ = uX * dY - uY * dX;
        double invRLen = 1.0 / Math.sqrt(rX * rX + rY * rY + rZ * rZ);
        rX *= invRLen;
        rY *= invRLen;
        rZ *= invRLen;
        double nuX = rY * dZ - rZ * dY;
        double nuY = rZ * dX - rX * dZ;
        double nuZ = rX * dY - rY * dX;
        m00 = rX;
        m01 = nuX;
        m02 = dX;
        m03 = 0.0;
        m10 = rY;
        m11 = nuY;
        m12 = dY;
        m13 = 0.0;
        m20 = rZ;
        m21 = nuZ;
        m22 = dZ;
        m23 = 0.0;
        m30 = objPos.x();
        m31 = objPos.y();
        m32 = objPos.z();
        m33 = 1.0;
        return this;
    }

    public Matrix4d shadow(double[] light, double nx, double ny, double nz, double d) {
        double lx = light[0], ly = light[1], lz = light[2], lw = light[3];
        double dot = nx * lx + ny * ly + nz * lz + d * lw;
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

    public Matrix4d reflection(double nx, double ny, double nz, double d) {
        double invLen = 1.0 / Math.sqrt(nx * nx + ny * ny + nz * nz);
        double nnx = nx * invLen;
        double nny = ny * invLen;
        double nnz = nz * invLen;
        double da = -d * invLen;
        m00 = 1.0 - 2.0 * nnx * nnx;
        m01 = -2.0 * nny * nnx;
        m02 = -2.0 * nnz * nnx;
        m03 = 0.0;
        m10 = -2.0 * nnx * nny;
        m11 = 1.0 - 2.0 * nny * nny;
        m12 = -2.0 * nnz * nny;
        m13 = 0.0;
        m20 = -2.0 * nnx * nnz;
        m21 = -2.0 * nny * nnz;
        m22 = 1.0 - 2.0 * nnz * nnz;
        m23 = 0.0;
        m30 = -2.0 * nnx * da;
        m31 = -2.0 * nny * da;
        m32 = -2.0 * nnz * da;
        m33 = 1.0;
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
        if (!(obj instanceof Matrix4d)) return false;
        Matrix4d other = (Matrix4d) obj;
        return Double.doubleToLongBits(m00) == Double.doubleToLongBits(other.m00)
            && Double.doubleToLongBits(m01) == Double.doubleToLongBits(other.m01)
            && Double.doubleToLongBits(m02) == Double.doubleToLongBits(other.m02)
            && Double.doubleToLongBits(m03) == Double.doubleToLongBits(other.m03)
            && Double.doubleToLongBits(m10) == Double.doubleToLongBits(other.m10)
            && Double.doubleToLongBits(m11) == Double.doubleToLongBits(other.m11)
            && Double.doubleToLongBits(m12) == Double.doubleToLongBits(other.m12)
            && Double.doubleToLongBits(m13) == Double.doubleToLongBits(other.m13)
            && Double.doubleToLongBits(m20) == Double.doubleToLongBits(other.m20)
            && Double.doubleToLongBits(m21) == Double.doubleToLongBits(other.m21)
            && Double.doubleToLongBits(m22) == Double.doubleToLongBits(other.m22)
            && Double.doubleToLongBits(m23) == Double.doubleToLongBits(other.m23)
            && Double.doubleToLongBits(m30) == Double.doubleToLongBits(other.m30)
            && Double.doubleToLongBits(m31) == Double.doubleToLongBits(other.m31)
            && Double.doubleToLongBits(m32) == Double.doubleToLongBits(other.m32)
            && Double.doubleToLongBits(m33) == Double.doubleToLongBits(other.m33);
    }

    @Override
    public String toString() {
        return String.format("Matrix4d[[%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f], [%f, %f, %f, %f]]",
            m00, m10, m20, m30,
            m01, m11, m21, m31,
            m02, m12, m22, m32,
            m03, m13, m23, m33);
    }

    public Matrix4d get(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, m00);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, m01);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, m02);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 24, m03);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 32, m10);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 40, m11);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 48, m12);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 56, m13);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 64, m20);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 72, m21);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 80, m22);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 88, m23);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 96, m30);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 104, m31);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 112, m32);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 120, m33);
        return this;
    }

    public Matrix4d set(MemorySegment src, long byteOffset) {
        m00 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset);
        m01 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8);
        m02 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16);
        m03 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 24);
        m10 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 32);
        m11 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 40);
        m12 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 48);
        m13 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 56);
        m20 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 64);
        m21 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 72);
        m22 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 80);
        m23 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 88);
        m30 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 96);
        m31 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 104);
        m32 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 112);
        m33 = src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 120);
        return this;
    }

    public static Matrix4d fromBuffer(java.nio.DoubleBuffer src) {
        Matrix4d m = new Matrix4d();
        m.m00 = src.get(); m.m01 = src.get(); m.m02 = src.get(); m.m03 = src.get();
        m.m10 = src.get(); m.m11 = src.get(); m.m12 = src.get(); m.m13 = src.get();
        m.m20 = src.get(); m.m21 = src.get(); m.m22 = src.get(); m.m23 = src.get();
        m.m30 = src.get(); m.m31 = src.get(); m.m32 = src.get(); m.m33 = src.get();
        return m;
    }

    public static Matrix4d fromBuffer(int index, java.nio.DoubleBuffer src) {
        Matrix4d m = new Matrix4d();
        m.m00 = src.get(index);      m.m01 = src.get(index + 1);  m.m02 = src.get(index + 2);  m.m03 = src.get(index + 3);
        m.m10 = src.get(index + 4);  m.m11 = src.get(index + 5);  m.m12 = src.get(index + 6);  m.m13 = src.get(index + 7);
        m.m20 = src.get(index + 8);  m.m21 = src.get(index + 9);  m.m22 = src.get(index + 10); m.m23 = src.get(index + 11);
        m.m30 = src.get(index + 12); m.m31 = src.get(index + 13); m.m32 = src.get(index + 14); m.m33 = src.get(index + 15);
        return m;
    }

    public java.nio.DoubleBuffer writeToBuffer(java.nio.DoubleBuffer dest) {
        dest.put(m00); dest.put(m01); dest.put(m02); dest.put(m03);
        dest.put(m10); dest.put(m11); dest.put(m12); dest.put(m13);
        dest.put(m20); dest.put(m21); dest.put(m22); dest.put(m23);
        dest.put(m30); dest.put(m31); dest.put(m32); dest.put(m33);
        return dest;
    }

    public java.nio.DoubleBuffer writeToBuffer(int index, java.nio.DoubleBuffer dest) {
        dest.put(index, m00);      dest.put(index + 1, m01);  dest.put(index + 2, m02);  dest.put(index + 3, m03);
        dest.put(index + 4, m10);  dest.put(index + 5, m11);  dest.put(index + 6, m12);  dest.put(index + 7, m13);
        dest.put(index + 8, m20);  dest.put(index + 9, m21);  dest.put(index + 10, m22); dest.put(index + 11, m23);
        dest.put(index + 12, m30); dest.put(index + 13, m31); dest.put(index + 14, m32); dest.put(index + 15, m33);
        return dest;
    }
}
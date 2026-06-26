package com.jamma.math.matrix;

import com.jamma.math.Vector3d;
import com.jamma.math.Vector4d;
import com.jamma.math.quaternion.Quaterniond;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix4d implements Serializable {

    @Serial
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

    public Matrix4d translate(Vector3d offset) {
        return translate(offset.x(), offset.y(), offset.z());
    }

    public Vector3d getTranslation() {
        return new Vector3d(m30, m31, m32);
    }

    public Matrix4d setTranslation(double x, double y, double z) {
        m30 = x;
        m31 = y;
        m32 = z;
        return this;
    }

    public Matrix4d setTranslation(Vector3d v) {
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

    public Matrix4d scale(Vector3d xyz) {
        return scale(xyz.x(), xyz.y(), xyz.z());
    }

    public Vector3d getScale() {
        double sx = Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        double sy = Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        double sz = Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        return new Vector3d(sx, sy, sz);
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

    public Matrix4d reflect(Vector3d normal) {
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

    public Matrix4d lookAt(Vector3d eye, Vector3d center, Vector3d up) {
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

    public Vector4d transform(Vector4d v) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30 * v.w())));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31 * v.w())));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32 * v.w())));
        double w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33 * v.w())));
        return new Vector4d(x, y, z, w);
    }

    public Vector3d transformPosition(Vector3d v) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3d(x, y, z);
    }

    public Vector3d transformDirection(Vector3d v) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), m20 * v.z()));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), m21 * v.z()));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), m22 * v.z()));
        return new Vector3d(x, y, z);
    }

    public Vector3d transformProject(Vector3d v) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        double w = Math.fma(m03, v.x(), Math.fma(m13, v.y(), Math.fma(m23, v.z(), m33)));
        double invW = 1.0 / w;
        return new Vector3d(x * invW, y * invW, z * invW);
    }

    public static Matrix4d translation(double x, double y, double z) {
        return new Matrix4d().setTranslation(x, y, z);
    }

    public static Matrix4d rotationX(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        return new Matrix4d(
            1, 0, 0, 0,
            0, c, s, 0,
            0, -s, c, 0,
            0, 0, 0, 1
        );
    }

    public static Matrix4d rotationY(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        return new Matrix4d(
            c, 0, -s, 0,
            0, 1, 0, 0,
            s, 0, c, 0,
            0, 0, 0, 1
        );
    }

    public static Matrix4d rotationZ(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        return new Matrix4d(
            c, s, 0, 0,
            -s, c, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        );
    }

    public static Matrix4d scaling(double x, double y, double z) {
        return new Matrix4d(
            x, 0, 0, 0,
            0, y, 0, 0,
            0, 0, z, 0,
            0, 0, 0, 1
        );
    }

    public double determinant3x3() {
        return (m00 * (m11 * m22 - m12 * m21)
              - m01 * (m10 * m22 - m12 * m20)
              + m02 * (m10 * m21 - m11 * m20));
    }

    public double get(int col, int row) {
        return switch (row * 4 + col) {
            case 0 -> m00; case 1 -> m01; case 2 -> m02; case 3 -> m03;
            case 4 -> m10; case 5 -> m11; case 6 -> m12; case 7 -> m13;
            case 8 -> m20; case 9 -> m21; case 10 -> m22; case 11 -> m23;
            case 12 -> m30; case 13 -> m31; case 14 -> m32; case 15 -> m33;
            default -> throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        };
    }

    public Matrix4d set(int col, int row, double value) {
        switch (row * 4 + col) {
            case 0: m00 = value; break; case 1: m01 = value; break; case 2: m02 = value; break; case 3: m03 = value; break;
            case 4: m10 = value; break; case 5: m11 = value; break; case 6: m12 = value; break; case 7: m13 = value; break;
            case 8: m20 = value; break; case 9: m21 = value; break; case 10: m22 = value; break; case 11: m23 = value; break;
            case 12: m30 = value; break; case 13: m31 = value; break; case 14: m32 = value; break; case 15: m33 = value; break;
            default: throw new IndexOutOfBoundsException("(" + col + ", " + row + ")");
        }
        return this;
    }

    public Matrix4d setRow(int row, Vector4d v) {
        if (row == 0) { m00 = v.x(); m10 = v.y(); m20 = v.z(); m30 = v.w(); }
        else if (row == 1) { m01 = v.x(); m11 = v.y(); m21 = v.z(); m31 = v.w(); }
        else if (row == 2) { m02 = v.x(); m12 = v.y(); m22 = v.z(); m32 = v.w(); }
        else if (row == 3) { m03 = v.x(); m13 = v.y(); m23 = v.z(); m33 = v.w(); }
        else throw new IndexOutOfBoundsException("Row: " + row);
        return this;
    }

    public Matrix4d setColumn(int col, Vector4d v) {
        if (col == 0) { m00 = v.x(); m01 = v.y(); m02 = v.z(); m03 = v.w(); }
        else if (col == 1) { m10 = v.x(); m11 = v.y(); m12 = v.z(); m13 = v.w(); }
        else if (col == 2) { m20 = v.x(); m21 = v.y(); m22 = v.z(); m23 = v.w(); }
        else if (col == 3) { m30 = v.x(); m31 = v.y(); m32 = v.z(); m33 = v.w(); }
        else throw new IndexOutOfBoundsException("Column: " + col);
        return this;
    }

    public Matrix4d set3x3(Matrix3d m) {
        m00 = m.m00(); m01 = m.m01(); m02 = m.m02();
        m10 = m.m10(); m11 = m.m11(); m12 = m.m12();
        m20 = m.m20(); m21 = m.m21(); m22 = m.m22();
        return this;
    }

    public Matrix4d translateLocal(double x, double y, double z) {
        m30 += x; m31 += y; m32 += z;
        return this;
    }

    public Matrix4d rotateLocalX(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t10 = m10, t11 = m11, t12 = m12, t13 = m13;
        double t20 = m20, t21 = m21, t22 = m22, t23 = m23;
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

    public Matrix4d rotateLocalY(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t00 = m00, t01 = m01, t02 = m02, t03 = m03;
        double t20 = m20, t21 = m21, t22 = m22, t23 = m23;
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

    public Matrix4d rotateLocalZ(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t00 = m00, t01 = m01, t02 = m02, t03 = m03;
        double t10 = m10, t11 = m11, t12 = m12, t13 = m13;
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

    public Matrix4d scaleLocal(double x, double y, double z) {
        m00 *= x; m01 *= x; m02 *= x; m03 *= x;
        m10 *= y; m11 *= y; m12 *= y; m13 *= y;
        m20 *= z; m21 *= z; m22 *= z; m23 *= z;
        return this;
    }

    public Vector3d positiveX() { return new Vector3d(m00, m01, m02); }
    public Vector3d positiveY() { return new Vector3d(m10, m11, m12); }
    public Vector3d positiveZ() { return new Vector3d(m20, m21, m22); }

    public Vector3d normalizedPositiveX() {
        double invLen = 1.0 / Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        return new Vector3d(m00 * invLen, m01 * invLen, m02 * invLen);
    }
    public Vector3d normalizedPositiveY() {
        double invLen = 1.0 / Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        return new Vector3d(m10 * invLen, m11 * invLen, m12 * invLen);
    }
    public Vector3d normalizedPositiveZ() {
        double invLen = 1.0 / Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        return new Vector3d(m20 * invLen, m21 * invLen, m22 * invLen);
    }

    public Vector3d getEulerAnglesZYX() {
        double a = Math.asin(-m20);
        double ca = Math.cos(a);
        if (Math.abs(ca) > 1.0e-6) {
            return new Vector3d(Math.atan2(m21, m22), a, Math.atan2(m10, m00));
        }
        return new Vector3d(0.0, a, Math.atan2(-m01, m11));
    }

    public boolean isFinite() {
        return Double.isFinite(m00) && Double.isFinite(m01) && Double.isFinite(m02) && Double.isFinite(m03)
            && Double.isFinite(m10) && Double.isFinite(m11) && Double.isFinite(m12) && Double.isFinite(m13)
            && Double.isFinite(m20) && Double.isFinite(m21) && Double.isFinite(m22) && Double.isFinite(m23)
            && Double.isFinite(m30) && Double.isFinite(m31) && Double.isFinite(m32) && Double.isFinite(m33);
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

    public Vector4d row(int row) {
        if (row == 0) {
            return new Vector4d(m00, m10, m20, m30);
        } else if (row == 1) {
            return new Vector4d(m01, m11, m21, m31);
        } else if (row == 2) {
            return new Vector4d(m02, m12, m22, m32);
        } else {
            return new Vector4d(m03, m13, m23, m33);
        }
    }

    public Vector4d column(int col) {
        if (col == 0) {
            return new Vector4d(m00, m01, m02, m03);
        } else if (col == 1) {
            return new Vector4d(m10, m11, m12, m13);
        } else if (col == 2) {
            return new Vector4d(m20, m21, m22, m23);
        } else {
            return new Vector4d(m30, m31, m32, m33);
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

    public Matrix4d billboard(Vector3d objPos, Vector3d target, Vector3d up) {
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
        if (!(obj instanceof Matrix4d other)) return false;
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

    public Matrix4d rotateYXZ(double angleY, double angleX, double angleZ) {
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

    public Matrix4d rotateTowardsXY(double x, double y) {
        double invLen = 1.0 / Math.sqrt(x * x + y * y);
        double nx = x * invLen;
        double ny = y * invLen;
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
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

    public Matrix4d setLookAt(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ) {
        return lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4d setLookAt(Vector3d eye, Vector3d center, Vector3d up) {
        return lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4d setLookAlong(double dirX, double dirY, double dirZ, double upX, double upY, double upZ) {
        return lookAlong(dirX, dirY, dirZ, upX, upY, upZ);
    }

    public Matrix4d setLookAlong(Vector3d dir, Vector3d up) {
        return lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }

    public Matrix4d rotateLocal(double ang, double x, double y, double z) {
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

    public Matrix4d rotateLocal(Quaterniond q) {
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
        double n00 = m00 * r00 + m01 * r01 + m02 * r02;
        double n01 = m00 * r10 + m01 * r11 + m02 * r12;
        double n02 = m00 * r20 + m01 * r21 + m02 * r22;
        double n10 = m10 * r00 + m11 * r01 + m12 * r02;
        double n11 = m10 * r10 + m11 * r11 + m12 * r12;
        double n12 = m10 * r20 + m11 * r21 + m12 * r22;
        double n20 = m20 * r00 + m21 * r01 + m22 * r02;
        double n21 = m20 * r10 + m21 * r11 + m22 * r12;
        double n22 = m20 * r20 + m21 * r21 + m22 * r22;
        double n30 = m30 * r00 + m31 * r01 + m32 * r02;
        double n31 = m30 * r10 + m31 * r11 + m32 * r12;
        double n32 = m30 * r20 + m31 * r21 + m32 * r22;
        m00 = n00; m01 = n01; m02 = n02;
        m10 = n10; m11 = n11; m12 = n12;
        m20 = n20; m21 = n21; m22 = n22;
        m30 = n30; m31 = n31; m32 = n32;
        return this;
    }

    public static Matrix4d rotation(double ang, double x, double y, double z) {
        return new Matrix4d().rotate(ang, x, y, z);
    }

    public static Matrix4d rotationXYZ(double angleX, double angleY, double angleZ) {
        return new Matrix4d().rotateXYZ(angleX, angleY, angleZ);
    }

    public static Matrix4d rotationZYX(double angleZ, double angleY, double angleX) {
        return new Matrix4d().rotateZYX(angleZ, angleY, angleX);
    }

    public static Matrix4d rotationYXZ(double angleY, double angleX, double angleZ) {
        return new Matrix4d().rotateYXZ(angleY, angleX, angleZ);
    }

    public static Matrix4d scaling(double scale) {
        return scaling(scale, scale, scale);
    }

    public static Matrix4d scaling(Vector3d scale) {
        return scaling(scale.x(), scale.y(), scale.z());
    }

    public static Matrix4d translationRotateScale(double tx, double ty, double tz, double qx, double qy, double qz, double qw, double sx, double sy, double sz) {
        Matrix4d m = new Matrix4d();
        m.setTranslation(tx, ty, tz);
        m.rotate(new Quaterniond(qx, qy, qz, qw));
        m.scale(sx, sy, sz);
        return m;
    }

    public static Matrix4d translationRotateScale(Vector3d translation, Quaterniond quat, Vector3d scale) {
        return translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }

    public static Matrix4d translationRotateScaleInvert(double tx, double ty, double tz, double qx, double qy, double qz, double qw, double sx, double sy, double sz) {
        Matrix4d m = translationRotateScale(tx, ty, tz, qx, qy, qz, qw, sx, sy, sz);
        m.invert();
        return m;
    }

    public static Matrix4d translationRotateScaleInvert(Vector3d translation, Quaterniond quat, Vector3d scale) {
        return translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }

    public static Matrix4d translationRotateScaleMulAffine(double tx, double ty, double tz, double qx, double qy, double qz, double qw, double sx, double sy, double sz, Matrix4d mul) {
        Matrix4d m = translationRotateScale(tx, ty, tz, qx, qy, qz, qw, sx, sy, sz);
        m.mulAffine(mul);
        return m;
    }

    public static Matrix4d translationRotate(double tx, double ty, double tz, double qx, double qy, double qz, double qw) {
        Matrix4d m = new Matrix4d();
        m.setTranslation(tx, ty, tz);
        m.rotate(new Quaterniond(qx, qy, qz, qw));
        return m;
    }

    public static Matrix4d translationRotate(Vector3d translation, Quaterniond quat) {
        return translationRotate(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }

    public static Matrix4d translationRotateInvert(double tx, double ty, double tz, double qx, double qy, double qz, double qw) {
        Matrix4d m = translationRotate(tx, ty, tz, qx, qy, qz, qw);
        m.invert();
        return m;
    }

    public static Matrix4d translationRotateInvert(Vector3d translation, Quaterniond quat) {
        return translationRotateInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }

    public static Matrix4d rotationTowardsXY(double x, double y) {
        return new Matrix4d().rotateTowardsXY(x, y);
    }

    public static final Matrix4d IDENTITY = new Matrix4d();

    public Matrix4d reflect(double nx, double ny, double nz, double d) {
        double invLen = 1.0 / Math.sqrt(nx * nx + ny * ny + nz * nz);
        double nnx = nx * invLen;
        double nny = ny * invLen;
        double nnz = nz * invLen;
        double nd = d * invLen;
        double r00 = 1.0 - 2.0 * nnx * nnx;
        double r01 = -2.0 * nny * nnx;
        double r02 = -2.0 * nnz * nnx;
        double r03 = -2.0 * nnx * nd;
        double r10 = -2.0 * nnx * nny;
        double r11 = 1.0 - 2.0 * nny * nny;
        double r12 = -2.0 * nnz * nny;
        double r13 = -2.0 * nny * nd;
        double r20 = -2.0 * nnx * nnz;
        double r21 = -2.0 * nny * nnz;
        double r22 = 1.0 - 2.0 * nnz * nnz;
        double r23 = -2.0 * nnz * nd;
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
        double t30 = m30; double t31 = m31; double t32 = m32; double t33 = m33;
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

    public Matrix4d reflect(double x0, double y0, double z0, double x1, double y1, double z1) {
        double nx = x1 - x0;
        double ny = y1 - y0;
        double nz = z1 - z0;
        double d = -(nx * x0 + ny * y0 + nz * z0);
        return reflect(nx, ny, nz, d);
    }

    public Matrix4d reflect(Vector3d point, Vector3d normal) {
        return reflect(point.x(), point.y(), point.z(), normal.x(), normal.y(), normal.z());
    }

    public static Matrix4d reflection(double x0, double y0, double z0, double x1, double y1, double z1) {
        double nx = x1 - x0;
        double ny = y1 - y0;
        double nz = z1 - z0;
        double d = -(nx * x0 + ny * y0 + nz * z0);
        return new Matrix4d().reflection(nx, ny, nz, d);
    }

    public static Matrix4d reflection(Vector3d point, Vector3d normal) {
        return reflection(point.x(), point.y(), point.z(), normal.x(), normal.y(), normal.z());
    }

    public static Matrix4d rotationTowards(Vector3d dir, Vector3d up) {
        return rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }

    public static Matrix4d rotationTowards(double dirX, double dirY, double dirZ, double upX, double upY, double upZ) {
        double invLen = 1.0 / Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        double fX = dirX * invLen;
        double fY = dirY * invLen;
        double fZ = dirZ * invLen;
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
        return new Matrix4d(
            sX, uX, -fX, 0.0,
            sY, uY, -fY, 0.0,
            sZ, uZ, -fZ, 0.0,
            0.0, 0.0, 0.0, 1.0
        );
    }

    public Matrix4d perspective(double fovYRad, double aspect, double zNear, double zFar, boolean zZeroToOne) {
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
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0 * zFar * zNear * invFn;
        }
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d perspectiveRect(double width, double height, double zNear, double zFar, boolean zZeroToOne) {
        double invFn = 1.0 / (zNear - zFar);
        m00 = 2.0 * zNear / width;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = 2.0 * zNear / height;
        m12 = 0.0;
        m13 = 0.0;
        m20 = 0.0;
        m21 = 0.0;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0 * zFar * zNear * invFn;
        }
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d perspectiveRect(double width, double height, double zNear, double zFar) {
        return perspectiveRect(width, height, zNear, zFar, false);
    }

    public Matrix4d perspectiveOffCenter(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
        double invRL = 1.0 / (right - left);
        double invTB = 1.0 / (top - bottom);
        double invFn = 1.0 / (zNear - zFar);
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
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0 * zFar * zNear * invFn;
        }
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d perspectiveOffCenter(double left, double right, double bottom, double top, double zNear, double zFar) {
        return perspectiveOffCenter(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4d perspectiveOffCenterFov(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar, boolean zZeroToOne) {
        double tanLeft = Math.tan(angleLeft);
        double tanRight = Math.tan(angleRight);
        double tanDown = Math.tan(angleDown);
        double tanUp = Math.tan(angleUp);
        double invTanSumX = 1.0 / (tanRight + tanLeft);
        double invTanSumY = 1.0 / (tanUp + tanDown);
        double invFn = 1.0 / (zNear - zFar);
        m00 = 2.0 * invTanSumX;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = 2.0 * invTanSumY;
        m12 = 0.0;
        m13 = 0.0;
        m20 = (tanRight - tanLeft) * invTanSumX;
        m21 = (tanUp - tanDown) * invTanSumY;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = 2.0 * zFar * zNear * invFn;
        }
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d perspectiveOffCenterFov(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar) {
        return perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }

    public Matrix4d perspectiveLH(double fovY, double aspect, double zNear, double zFar, boolean zZeroToOne) {
        double f = 1.0 / Math.tan(fovY * 0.5);
        double invFn = 1.0 / (zFar - zNear);
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
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = -zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = -2.0 * zFar * zNear * invFn;
        }
        m23 = 1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d perspectiveLH(double fovY, double aspect, double zNear, double zFar) {
        return perspectiveLH(fovY, aspect, zNear, zFar, false);
    }

    public Matrix4d perspectiveOffCenterFovLH(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar, boolean zZeroToOne) {
        double tanLeft = Math.tan(angleLeft);
        double tanRight = Math.tan(angleRight);
        double tanDown = Math.tan(angleDown);
        double tanUp = Math.tan(angleUp);
        double invTanSumX = 1.0 / (tanRight + tanLeft);
        double invTanSumY = 1.0 / (tanUp + tanDown);
        double invFn = 1.0 / (zFar - zNear);
        m00 = 2.0 * invTanSumX;
        m01 = 0.0;
        m02 = 0.0;
        m03 = 0.0;
        m10 = 0.0;
        m11 = 2.0 * invTanSumY;
        m12 = 0.0;
        m13 = 0.0;
        m20 = (tanRight - tanLeft) * invTanSumX;
        m21 = (tanUp - tanDown) * invTanSumY;
        if (zZeroToOne) {
            m22 = zFar * invFn;
            m32 = -zFar * zNear * invFn;
        } else {
            m22 = (zFar + zNear) * invFn;
            m32 = -2.0 * zFar * zNear * invFn;
        }
        m23 = 1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d perspectiveOffCenterFovLH(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar) {
        return perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }

    public Matrix4d setPerspective(double fovY, double aspect, double zNear, double zFar, boolean zZeroToOne) {
        return perspective(fovY, aspect, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setPerspective(double fovY, double aspect, double zNear, double zFar) {
        return perspective(fovY, aspect, zNear, zFar);
    }

    public Matrix4d setPerspectiveRect(double width, double height, double zNear, double zFar, boolean zZeroToOne) {
        return perspectiveRect(width, height, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setPerspectiveRect(double width, double height, double zNear, double zFar) {
        return perspectiveRect(width, height, zNear, zFar);
    }

    public Matrix4d setPerspectiveOffCenter(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
        return perspectiveOffCenter(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setPerspectiveOffCenter(double left, double right, double bottom, double top, double zNear, double zFar) {
        return perspectiveOffCenter(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4d setPerspectiveOffCenterFov(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar, boolean zZeroToOne) {
        return perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setPerspectiveOffCenterFov(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar) {
        return perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar);
    }

    public Matrix4d setPerspectiveOffCenterFovLH(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar, boolean zZeroToOne) {
        return perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setPerspectiveOffCenterFovLH(double angleLeft, double angleRight, double angleDown, double angleUp, double zNear, double zFar) {
        return perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar);
    }

    public Matrix4d setPerspectiveLH(double fovY, double aspect, double zNear, double zFar, boolean zZeroToOne) {
        return perspectiveLH(fovY, aspect, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setPerspectiveLH(double fovY, double aspect, double zNear, double zFar) {
        return perspectiveLH(fovY, aspect, zNear, zFar);
    }

    public Matrix4d ortho(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
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
        if (zZeroToOne) {
            m22 = -invFN;
            m32 = -zNear * invFN;
        } else {
            m22 = -2.0 * invFN;
            m32 = -(zFar + zNear) * invFN;
        }
        m23 = 0.0;
        m30 = -(right + left) * invRL;
        m31 = -(top + bottom) * invTB;
        m33 = 1.0;
        return this;
    }

    public Matrix4d orthoLH(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
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
        if (zZeroToOne) {
            m22 = invFN;
            m32 = -zNear * invFN;
        } else {
            m22 = 2.0 * invFN;
            m32 = -(zFar + zNear) * invFN;
        }
        m23 = 0.0;
        m30 = -(right + left) * invRL;
        m31 = -(top + bottom) * invTB;
        m33 = 1.0;
        return this;
    }

    public Matrix4d orthoLH(double left, double right, double bottom, double top, double zNear, double zFar) {
        return orthoLH(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4d orthoSymmetric(double width, double height, double zNear, double zFar, boolean zZeroToOne) {
        return ortho(-width / 2.0, width / 2.0, -height / 2.0, height / 2.0, zNear, zFar, zZeroToOne);
    }

    public Matrix4d orthoSymmetric(double width, double height, double zNear, double zFar) {
        return orthoSymmetric(width, height, zNear, zFar, false);
    }

    public Matrix4d orthoSymmetricLH(double width, double height, double zNear, double zFar, boolean zZeroToOne) {
        return orthoLH(-width / 2.0, width / 2.0, -height / 2.0, height / 2.0, zNear, zFar, zZeroToOne);
    }

    public Matrix4d orthoSymmetricLH(double width, double height, double zNear, double zFar) {
        return orthoSymmetricLH(width, height, zNear, zFar, false);
    }

    public Matrix4d ortho2DLH(double left, double right, double bottom, double top) {
        return orthoLH(left, right, bottom, top, -1.0, 1.0);
    }

    public Matrix4d setOrtho(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
        return ortho(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setOrtho(double left, double right, double bottom, double top, double zNear, double zFar) {
        return ortho(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4d setOrthoLH(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
        return orthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setOrthoLH(double left, double right, double bottom, double top, double zNear, double zFar) {
        return orthoLH(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4d setOrthoSymmetric(double width, double height, double zNear, double zFar, boolean zZeroToOne) {
        return orthoSymmetric(width, height, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setOrthoSymmetric(double width, double height, double zNear, double zFar) {
        return orthoSymmetric(width, height, zNear, zFar);
    }

    public Matrix4d setOrthoSymmetricLH(double width, double height, double zNear, double zFar, boolean zZeroToOne) {
        return orthoSymmetricLH(width, height, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setOrthoSymmetricLH(double width, double height, double zNear, double zFar) {
        return orthoSymmetricLH(width, height, zNear, zFar);
    }

    public Matrix4d setOrtho2D(double left, double right, double bottom, double top) {
        return ortho2D(left, right, bottom, top);
    }

    public Matrix4d setOrtho2DLH(double left, double right, double bottom, double top) {
        return ortho2DLH(left, right, bottom, top);
    }

    public Matrix4d frustum(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
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
        if (zZeroToOne) {
            m22 = -zFar * invFN;
            m32 = -zFar * zNear * invFN;
        } else {
            m22 = -(zFar + zNear) * invFN;
            m32 = -2.0 * zFar * zNear * invFN;
        }
        m23 = -1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d frustumLH(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
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
        if (zZeroToOne) {
            m22 = zFar * invFN;
            m32 = -zFar * zNear * invFN;
        } else {
            m22 = (zFar + zNear) * invFN;
            m32 = -2.0 * zFar * zNear * invFN;
        }
        m23 = 1.0;
        m30 = 0.0;
        m31 = 0.0;
        m33 = 0.0;
        return this;
    }

    public Matrix4d frustumLH(double left, double right, double bottom, double top, double zNear, double zFar) {
        return frustumLH(left, right, bottom, top, zNear, zFar, false);
    }

    public Matrix4d setFrustum(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
        return frustum(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setFrustum(double left, double right, double bottom, double top, double zNear, double zFar) {
        return frustum(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4d setFrustumLH(double left, double right, double bottom, double top, double zNear, double zFar, boolean zZeroToOne) {
        return frustumLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
    }

    public Matrix4d setFrustumLH(double left, double right, double bottom, double top, double zNear, double zFar) {
        return frustumLH(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4d obliqueZ(double a, double b) {
        double t20 = Math.fma(a, m00, Math.fma(b, m01, m20));
        double t21 = Math.fma(a, m10, Math.fma(b, m11, m21));
        double t22 = Math.fma(a, m20, Math.fma(b, m21, m22));
        double t23 = Math.fma(a, m30, Math.fma(b, m31, m23));
        m20 = t20;
        m21 = t21;
        m22 = t22;
        m23 = t23;
        return this;
    }

    public Matrix4d lookAtPerspective(double eyeX, double eyeY, double eyeZ,
            double centerX, double centerY, double centerZ,
            double upX, double upY, double upZ) {
        return lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4d lookAtLH(Vector3d eye, Vector3d center, Vector3d up) {
        return lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public Matrix4d lookAtLH(double eyeX, double eyeY, double eyeZ,
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
        m02 = fX;
        m03 = 0.0;
        m10 = sY;
        m11 = uY;
        m12 = fY;
        m13 = 0.0;
        m20 = sZ;
        m21 = uZ;
        m22 = fZ;
        m23 = 0.0;
        m30 = -(sX * eyeX + sY * eyeY + sZ * eyeZ);
        m31 = -(uX * eyeX + uY * eyeY + uZ * eyeZ);
        m32 = -(fX * eyeX + fY * eyeY + fZ * eyeZ);
        m33 = 1.0;
        return this;
    }

    public Matrix4d lookAtPerspectiveLH(double eyeX, double eyeY, double eyeZ,
            double centerX, double centerY, double centerZ,
            double upX, double upY, double upZ) {
        return lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4d setLookAtLH(Vector3d eye, Vector3d center, Vector3d up) {
        return lookAtLH(eye, center, up);
    }

    public Matrix4d setLookAtLH(double eyeX, double eyeY, double eyeZ,
            double centerX, double centerY, double centerZ,
            double upX, double upY, double upZ) {
        return lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4d lookAlong(Vector3d dir, Vector3d up) {
        return lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }

    public Matrix4d withLookAtUp(Vector3d up) {
        return withLookAtUp(up.x(), up.y(), up.z());
    }

    public Matrix4d withLookAtUp(double upX, double upY, double upZ) {
        double fX = -m02;
        double fY = -m12;
        double fZ = -m22;
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
        return this;
    }

    public Vector4d transform(double x, double y, double z, double w) {
        double tx = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30 * w)));
        double ty = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31 * w)));
        double tz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32 * w)));
        double tw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33 * w)));
        return new Vector4d(tx, ty, tz, tw);
    }

    public Vector3d transformPosition(double x, double y, double z) {
        double px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        double py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        double pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        return new Vector3d(px, py, pz);
    }

    public Vector3d transformDirection(double x, double y, double z) {
        double dx = Math.fma(m00, x, Math.fma(m10, y, m20 * z));
        double dy = Math.fma(m01, x, Math.fma(m11, y, m21 * z));
        double dz = Math.fma(m02, x, Math.fma(m12, y, m22 * z));
        return new Vector3d(dx, dy, dz);
    }

    public Vector3d transformAffine(Vector3d v) {
        double x = Math.fma(m00, v.x(), Math.fma(m10, v.y(), Math.fma(m20, v.z(), m30)));
        double y = Math.fma(m01, v.x(), Math.fma(m11, v.y(), Math.fma(m21, v.z(), m31)));
        double z = Math.fma(m02, v.x(), Math.fma(m12, v.y(), Math.fma(m22, v.z(), m32)));
        return new Vector3d(x, y, z);
    }

    public Vector3d transformAffine(double x, double y, double z) {
        double ax = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        double ay = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        double az = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        return new Vector3d(ax, ay, az);
    }

    public Vector3d transformProject(double x, double y, double z) {
        double px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        double py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        double pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        double pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        double invW = 1.0 / pw;
        return new Vector3d(px * invW, py * invW, pz * invW);
    }

    public Matrix4d billboardCylindrical(Vector3d objPos, Vector3d targetPos, Vector3d up) {
        double dirX = objPos.x() - targetPos.x();
        double dirY = objPos.y() - targetPos.y();
        double dirZ = objPos.z() - targetPos.z();
        double invDirLen = 1.0 / Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        double dX = dirX * invDirLen;
        double dY = dirY * invDirLen;
        double dZ = dirZ * invDirLen;
        double uX = up.x(), uY = up.y(), uZ = up.z();
        double lX = uY * dZ - uZ * dY;
        double lY = uZ * dX - uX * dZ;
        double lZ = uX * dY - uY * dX;
        double invLeftLen = 1.0 / Math.sqrt(lX * lX + lY * lY + lZ * lZ);
        lX *= invLeftLen;
        lY *= invLeftLen;
        lZ *= invLeftLen;
        double nuX = dY * lZ - dZ * lY;
        double nuY = dZ * lX - dX * lZ;
        double nuZ = dX * lY - dY * lX;
        m00 = lX;
        m01 = nuX;
        m02 = dX;
        m03 = 0.0;
        m10 = lY;
        m11 = nuY;
        m12 = dY;
        m13 = 0.0;
        m20 = lZ;
        m21 = nuZ;
        m22 = dZ;
        m23 = 0.0;
        m30 = objPos.x();
        m31 = objPos.y();
        m32 = objPos.z();
        m33 = 1.0;
        return this;
    }

    public Matrix4d billboardSpherical(Vector3d objPos, Vector3d targetPos, Vector3d up) {
        return billboard(objPos, targetPos, up);
    }

    public Matrix4d billboardSpherical(Vector3d objPos, Vector3d targetPos) {
        return billboardSpherical(objPos, targetPos, new Vector3d(0, 1, 0));
    }

    public Matrix4d shadow(Vector4d light, double nx, double ny, double nz, double d) {
        return shadow(new double[]{light.x(), light.y(), light.z(), light.w()}, nx, ny, nz, d);
    }

    public Matrix4d shadow(double lightX, double lightY, double lightZ, double lightW, double nx, double ny, double nz, double d) {
        return shadow(new double[]{lightX, lightY, lightZ, lightW}, nx, ny, nz, d);
    }

    public Matrix4d shadow(Vector4d light, Matrix4d planeTransform) {
        double nx = Math.fma(planeTransform.m01, planeTransform.m12, -planeTransform.m02 * planeTransform.m11);
        double ny = Math.fma(planeTransform.m02, planeTransform.m10, -planeTransform.m00 * planeTransform.m12);
        double nz = Math.fma(planeTransform.m00, planeTransform.m11, -planeTransform.m01 * planeTransform.m10);
        double d = -(nx * planeTransform.m30 + ny * planeTransform.m31 + nz * planeTransform.m32);
        return shadow(light.x(), light.y(), light.z(), light.w(), nx, ny, nz, d);
    }

    public Matrix4d shadow(double lightX, double lightY, double lightZ, double lightW, Matrix4d planeTransform) {
        return shadow(new Vector4d(lightX, lightY, lightZ, lightW), planeTransform);
    }

    public Matrix4d rotateTowards(Vector3d dir, Vector3d up) {
        return rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }

    public Matrix4d rotateTowards(double dirX, double dirY, double dirZ, double upX, double upY, double upZ) {
        double invLen = 1.0 / Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        double fX = dirX * invLen;
        double fY = dirY * invLen;
        double fZ = dirZ * invLen;
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
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
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

    public Matrix4d arcball(double radius, double centerX, double centerY, double centerZ, double angle1, double angle2) {
        double x = radius * Math.sin(angle1);
        double y = radius * Math.sin(angle2);
        double z = radius * Math.cos(angle1) * Math.cos(angle2);
        double fX = -x;
        double fY = -y;
        double fZ = -z;
        double invFLen = 1.0 / Math.sqrt(fX * fX + fY * fY + fZ * fZ);
        fX *= invFLen;
        fY *= invFLen;
        fZ *= invFLen;
        double sX = fY * 1.0 - fZ * 0.0;
        double sY = fZ * 0.0 - fX * 1.0;
        double sZ = fX * 0.0 - fY * 0.0;
        double invSLen = 1.0 / Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX *= invSLen;
        sY *= invSLen;
        sZ *= invSLen;
        double uX = sY * fZ - sZ * fY;
        double uY = sZ * fX - sX * fZ;
        double uZ = sX * fY - sY * fX;
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
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

    public Matrix4d arcball(double radius, Vector3d center, double angle1, double angle2) {
        return arcball(radius, center.x(), center.y(), center.z(), angle1, angle2);
    }

    public Matrix4d rotate(double ang, Vector3d axis) {
        return rotate(ang, axis.x(), axis.y(), axis.z());
    }

    public Matrix4d rotateLocal(Vector3d axis, double ang) {
        return rotateLocal(ang, axis.x(), axis.y(), axis.z());
    }

    public Matrix4d rotateAround(Vector3d origin, Vector3d axis, double ang) {
        double invLen = 1.0 / Math.sqrt(axis.x() * axis.x() + axis.y() * axis.y() + axis.z() * axis.z());
        double nx = axis.x() * invLen;
        double ny = axis.y() * invLen;
        double nz = axis.z() * invLen;
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
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
        double t30 = m30; double t31 = m31; double t32 = m32; double t33 = m33;
        double ox = origin.x(), oy = origin.y(), oz = origin.z();
        double n30 = Math.fma(t00, ox, Math.fma(t10, oy, Math.fma(t20, oz, t30)));
        double n31 = Math.fma(t01, ox, Math.fma(t11, oy, Math.fma(t21, oz, t31)));
        double n32 = Math.fma(t02, ox, Math.fma(t12, oy, Math.fma(t22, oz, t32)));
        double n33 = Math.fma(t03, ox, Math.fma(t13, oy, Math.fma(t23, oz, t33)));
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

    public Matrix4d rotateTranslation(double ang, double x, double y, double z) {
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
        m00 = r00; m01 = r01; m02 = r02; m03 = 0.0;
        m10 = r10; m11 = r11; m12 = r12; m13 = 0.0;
        m20 = r20; m21 = r21; m22 = r22; m23 = 0.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0; m33 = 1.0;
        return this;
    }

    public Matrix4d rotateTranslation(Quaterniond quat) {
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
        m00 = r00; m01 = r01; m02 = r02; m03 = 0.0;
        m10 = r10; m11 = r11; m12 = r12; m13 = 0.0;
        m20 = r20; m21 = r21; m22 = r22; m23 = 0.0;
        m30 = 0.0; m31 = 0.0; m32 = 0.0; m33 = 1.0;
        return this;
    }

    public static Matrix4d translation(Vector3d offset) {
        return translation(offset.x(), offset.y(), offset.z());
    }

    public static Matrix4d rotation(double ang, Vector3d axis) {
        return rotation(ang, axis.x(), axis.y(), axis.z());
    }

    public static Matrix4d rotation(Quaterniond quat) {
        return new Matrix4d().rotate(quat);
    }

    public double determinantAffine() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix4d mulAffineR(Matrix4d left) {
        double n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, left.m02 * m20));
        double n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, left.m02 * m21));
        double n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, left.m02 * m22));
        double n03 = Math.fma(left.m00, m03, Math.fma(left.m01, m13, left.m02 * m23));
        double n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, left.m12 * m20));
        double n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, left.m12 * m21));
        double n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, left.m12 * m22));
        double n13 = Math.fma(left.m10, m03, Math.fma(left.m11, m13, left.m12 * m23));
        double n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, left.m22 * m20));
        double n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, left.m22 * m21));
        double n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, left.m22 * m22));
        double n23 = Math.fma(left.m20, m03, Math.fma(left.m21, m13, left.m22 * m23));
        double n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, left.m33 * m30)));
        double n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, left.m33 * m31)));
        double n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, left.m33 * m32)));
        double n33 = Math.fma(left.m30, m03, Math.fma(left.m31, m13, Math.fma(left.m32, m23, left.m33 * m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4d mulLocalAffine(Matrix4d left) {
        double n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, left.m02 * m20));
        double n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, left.m02 * m21));
        double n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, left.m02 * m22));
        double n03 = Math.fma(left.m00, m03, Math.fma(left.m01, m13, left.m02 * m23));
        double n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, left.m12 * m20));
        double n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, left.m12 * m21));
        double n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, left.m12 * m22));
        double n13 = Math.fma(left.m10, m03, Math.fma(left.m11, m13, left.m12 * m23));
        double n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, left.m22 * m20));
        double n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, left.m22 * m21));
        double n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, left.m22 * m22));
        double n23 = Math.fma(left.m20, m03, Math.fma(left.m21, m13, left.m22 * m23));
        double n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, m30)));
        double n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, m31)));
        double n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, m32)));
        double n33 = Math.fma(left.m30, m03, Math.fma(left.m31, m13, Math.fma(left.m32, m23, m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix3d normal(Matrix3d dest) {
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
        dest.m00 = n00; dest.m01 = n10; dest.m02 = n20;
        dest.m10 = n01; dest.m11 = n11; dest.m12 = n21;
        dest.m20 = n02; dest.m21 = n12; dest.m22 = n22;
        return dest;
    }

    public Matrix3d get3x3(Matrix3d dest) {
        dest.m00 = m00; dest.m01 = m01; dest.m02 = m02;
        dest.m10 = m10; dest.m11 = m11; dest.m12 = m12;
        dest.m20 = m20; dest.m21 = m21; dest.m22 = m22;
        return dest;
    }

    public Matrix3d getNormalizedRotation(Matrix3d dest) {
        double sx = 1.0 / Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        double sy = 1.0 / Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        double sz = 1.0 / Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        dest.m00 = m00 * sx; dest.m01 = m01 * sx; dest.m02 = m02 * sx;
        dest.m10 = m10 * sy; dest.m11 = m11 * sy; dest.m12 = m12 * sy;
        dest.m20 = m20 * sz; dest.m21 = m21 * sz; dest.m22 = m22 * sz;
        return dest;
    }

    public Matrix4d normalize3x3() {
        double sx = 1.0 / Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        double sy = 1.0 / Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        double sz = 1.0 / Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        m00 *= sx; m01 *= sx; m02 *= sx;
        m10 *= sy; m11 *= sy; m12 *= sy;
        m20 *= sz; m21 *= sz; m22 *= sz;
        return this;
    }

    public double cofactor3x3() {
        return m00 * (m11 * m22 - m12 * m21)
             - m01 * (m10 * m22 - m12 * m20)
             + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix4d mul(double scalar) {
        m00 *= scalar; m01 *= scalar; m02 *= scalar; m03 *= scalar;
        m10 *= scalar; m11 *= scalar; m12 *= scalar; m13 *= scalar;
        m20 *= scalar; m21 *= scalar; m22 *= scalar; m23 *= scalar;
        m30 *= scalar; m31 *= scalar; m32 *= scalar; m33 *= scalar;
        return this;
    }

    public Matrix4d mulLocal(Matrix4d left) {
        double n00 = Math.fma(left.m00, m00, Math.fma(left.m01, m10, Math.fma(left.m02, m20, left.m03 * m30)));
        double n01 = Math.fma(left.m00, m01, Math.fma(left.m01, m11, Math.fma(left.m02, m21, left.m03 * m31)));
        double n02 = Math.fma(left.m00, m02, Math.fma(left.m01, m12, Math.fma(left.m02, m22, left.m03 * m32)));
        double n03 = Math.fma(left.m00, m03, Math.fma(left.m01, m13, Math.fma(left.m02, m23, left.m03 * m33)));
        double n10 = Math.fma(left.m10, m00, Math.fma(left.m11, m10, Math.fma(left.m12, m20, left.m13 * m30)));
        double n11 = Math.fma(left.m10, m01, Math.fma(left.m11, m11, Math.fma(left.m12, m21, left.m13 * m31)));
        double n12 = Math.fma(left.m10, m02, Math.fma(left.m11, m12, Math.fma(left.m12, m22, left.m13 * m32)));
        double n13 = Math.fma(left.m10, m03, Math.fma(left.m11, m13, Math.fma(left.m12, m23, left.m13 * m33)));
        double n20 = Math.fma(left.m20, m00, Math.fma(left.m21, m10, Math.fma(left.m22, m20, left.m23 * m30)));
        double n21 = Math.fma(left.m20, m01, Math.fma(left.m21, m11, Math.fma(left.m22, m21, left.m23 * m31)));
        double n22 = Math.fma(left.m20, m02, Math.fma(left.m21, m12, Math.fma(left.m22, m22, left.m23 * m32)));
        double n23 = Math.fma(left.m20, m03, Math.fma(left.m21, m13, Math.fma(left.m22, m23, left.m23 * m33)));
        double n30 = Math.fma(left.m30, m00, Math.fma(left.m31, m10, Math.fma(left.m32, m20, left.m33 * m30)));
        double n31 = Math.fma(left.m30, m01, Math.fma(left.m31, m11, Math.fma(left.m32, m21, left.m33 * m31)));
        double n32 = Math.fma(left.m30, m02, Math.fma(left.m31, m12, Math.fma(left.m32, m22, left.m33 * m32)));
        double n33 = Math.fma(left.m30, m03, Math.fma(left.m31, m13, Math.fma(left.m32, m23, left.m33 * m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix4d mulTranslation(Matrix4d... others) {
        for (Matrix4d other : others) {
            multiply(other);
        }
        return this;
    }

    public Matrix4d negateX() {
        m00 = -m00; m01 = -m01; m02 = -m02; m03 = -m03;
        return this;
    }

    public Matrix4d negateY() {
        m10 = -m10; m11 = -m11; m12 = -m12; m13 = -m13;
        return this;
    }

    public Matrix4d negateZ() {
        m20 = -m20; m21 = -m21; m22 = -m22; m23 = -m23;
        return this;
    }

    public Matrix4d scale(double xy, double z) {
        return scale(xy, xy, z);
    }

    public Vector3d project(double x, double y, double z, int[] viewport, Vector3d dest) {
        double px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        double py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        double pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        double pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        double invW = 1.0 / pw;
        double sx = (px * invW * 0.5 + 0.5) * viewport[2] + viewport[0];
        double sy = (1.0 - (py * invW * 0.5 + 0.5)) * viewport[3] + viewport[1];
        double sz = (1.0 + pz * invW) * 0.5;
        return new Vector3d(sx, sy, sz);
    }

    public Vector3d unproject(double winX, double winY, double winZ, int[] viewport, Vector3d dest) {
        double x = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        double y = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        double z = 2.0 * winZ - 1.0;
        Matrix4d inv = new Matrix4d(this);
        inv.invert();
        double px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, Math.fma(inv.m20, z, inv.m30)));
        double py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, Math.fma(inv.m21, z, inv.m31)));
        double pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, Math.fma(inv.m22, z, inv.m32)));
        double pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, Math.fma(inv.m23, z, inv.m33)));
        double invW = 1.0 / pw;
        return new Vector3d(px * invW, py * invW, pz * invW);
    }

    public Vector3d unproject(double winX, double winY, double winZ, int[] viewport, Matrix4d view, Vector3d dest) {
        Matrix4d inv = new Matrix4d(this);
        inv.multiply(view);
        inv.invert();
        double x = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        double y = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        double z = 2.0 * winZ - 1.0;
        double px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, Math.fma(inv.m20, z, inv.m30)));
        double py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, Math.fma(inv.m21, z, inv.m31)));
        double pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, Math.fma(inv.m22, z, inv.m32)));
        double pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, Math.fma(inv.m23, z, inv.m33)));
        double invW = 1.0 / pw;
        return new Vector3d(px * invW, py * invW, pz * invW);
    }

    public Vector3d unprojectRay(double winX, double winY, int[] viewport, Vector3d dest) {
        double x = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        double y = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        Matrix4d inv = new Matrix4d(this);
        inv.invert();
        double nx = Math.fma(inv.m00, x, Math.fma(inv.m10, y, -inv.m20));
        double ny = Math.fma(inv.m01, x, Math.fma(inv.m11, y, -inv.m21));
        double nz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, -inv.m22));
        double nw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, -inv.m23));
        double px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, inv.m30));
        double py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, inv.m31));
        double pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, inv.m32));
        double pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, inv.m33));
        double invW = 1.0 / pw;
        double invNW = 1.0 / nw;
        double ox = px * invW;
        double oy = py * invW;
        double oz = pz * invW;
        return new Vector3d(ox, oy, oz);
    }

    public Matrix4d unprojectInvRay(double winX, double winY, int[] viewport, Matrix4d dest) {
        double x = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        double y = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        Matrix4d inv = new Matrix4d(this);
        inv.invert();
        double nx = Math.fma(inv.m00, x, Math.fma(inv.m10, y, -inv.m20));
        double ny = Math.fma(inv.m01, x, Math.fma(inv.m11, y, -inv.m21));
        double nz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, -inv.m22));
        double nw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, -inv.m23));
        double px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, inv.m30));
        double py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, inv.m31));
        double pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, inv.m32));
        double pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, inv.m33));
        double invW = 1.0 / pw;
        dest.m00 = nx; dest.m01 = ny; dest.m02 = nz; dest.m03 = 0.0;
        dest.m10 = 0.0; dest.m11 = 0.0; dest.m12 = 0.0; dest.m13 = 0.0;
        dest.m20 = 0.0; dest.m21 = 0.0; dest.m22 = 0.0; dest.m23 = 0.0;
        dest.m30 = px * invW; dest.m31 = py * invW; dest.m32 = pz * invW; dest.m33 = 1.0;
        return dest;
    }

    public Matrix4d pick(double x, double y, double width, double height, int[] viewport) {
        double sx = viewport[2] / width;
        double sy = viewport[3] / height;
        double tx = (viewport[2] + 2.0 * (viewport[0] - x)) / width;
        double ty = (viewport[3] + 2.0 * (viewport[1] - y)) / height;
        m00 = sx; m01 = 0.0; m02 = 0.0; m03 = 0.0;
        m10 = 0.0; m11 = sy; m12 = 0.0; m13 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 1.0; m23 = 0.0;
        m30 = tx; m31 = ty; m32 = 0.0; m33 = 1.0;
        return this;
    }

    public Matrix4d tile(double x, double y, double width, double height, int[] viewport) {
        double sx = width / viewport[2];
        double sy = height / viewport[3];
        double tx = (x - viewport[0]) * sx;
        double ty = (y - viewport[1]) * sy;
        m00 = sx; m01 = 0.0; m02 = 0.0; m03 = 0.0;
        m10 = 0.0; m11 = sy; m12 = 0.0; m13 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 1.0; m23 = 0.0;
        m30 = tx; m31 = ty; m32 = 0.0; m33 = 1.0;
        return this;
    }

    public Matrix4d viewport(int[] viewport) {
        double sx = viewport[2] * 0.5;
        double sy = viewport[3] * 0.5;
        double tx = viewport[0] + sx;
        double ty = viewport[1] + sy;
        m00 = sx; m01 = 0.0; m02 = 0.0; m03 = 0.0;
        m10 = 0.0; m11 = -sy; m12 = 0.0; m13 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 0.5; m23 = 0.0;
        m30 = tx; m31 = ty; m32 = 0.5; m33 = 1.0;
        return this;
    }

    public Matrix4d viewport(double x, double y, double width, double height, double zNear, double zFar) {
        double sx = width * 0.5;
        double sy = height * 0.5;
        double sz = (zFar - zNear) * 0.5;
        double tx = x + sx;
        double ty = y + sy;
        double tz = (zFar + zNear) * 0.5;
        m00 = sx; m01 = 0.0; m02 = 0.0; m03 = 0.0;
        m10 = 0.0; m11 = -sy; m12 = 0.0; m13 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = sz; m23 = 0.0;
        m30 = tx; m31 = ty; m32 = tz; m33 = 1.0;
        return this;
    }

    public Vector4d frustumPlane(int plane, Vector4d dest) {
        double a, b, c, d;
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
        double invLen = 1.0 / Math.sqrt(a * a + b * b + c * c);
        return new Vector4d(a * invLen, b * invLen, c * invLen, d * invLen);
    }

    public Vector3d frustumCorner(int corner, Vector3d dest) {
        double a = m30 - m20, b = m31 - m21, c = m32 - m22;
        double nX = m30 + m20, nY = m31 + m21, nZ = m32 + m22;
        double fX = m30 - m20, fY = m31 - m21, fZ = m32 - m22;
        double rX = m03 - m00, rY = m13 - m10, rZ = m23 - m20, rW = m33 - m30;
        double lX = m03 + m00, lY = m13 + m10, lZ = m23 + m20, lW = m33 + m30;
        double tX = m03 - m01, tY = m13 - m11, tZ = m23 - m21, tW = m33 - m31;
        double bX = m03 + m01, bY = m13 + m11, bZ = m23 + m21, bW = m33 + m31;
        double nCX = -m02, nCY = -m12, nCZ = -m22, nCW = m33 - m32;
        double fCX = m20 - m02, fCY = m21 - m12, fCZ = m22 - m22, fCW = m33 + m32;
        double[][] corners = {
            {lX, lY, lZ, lW, bX, bY, bZ, bW, nCX, nCY, nCZ, nCW},
            {rX, rY, rZ, rW, bX, bY, bZ, bW, nCX, nCY, nCZ, nCW},
            {rX, rY, rZ, rW, tX, tY, tZ, tW, nCX, nCY, nCZ, nCW},
            {lX, lY, lZ, lW, tX, tY, tZ, tW, nCX, nCY, nCZ, nCW},
            {lX, lY, lZ, lW, bX, bY, bZ, bW, fCX, fCY, fCZ, fCW},
            {rX, rY, rZ, rW, bX, bY, bZ, bW, fCX, fCY, fCZ, fCW},
            {rX, rY, rZ, rW, tX, tY, tZ, tW, fCX, fCY, fCZ, fCW},
            {lX, lY, lZ, lW, tX, tY, tZ, tW, fCX, fCY, fCZ, fCW}
        };
        double[] p = corners[corner];
        double a0 = p[0], b0 = p[1], c0 = p[2], d0 = p[3];
        double a1 = p[4], b1 = p[5], c1 = p[6], d1 = p[7];
        double a2 = p[8], b2 = p[9], c2 = p[10], d2 = p[11];
        double det = a0 * (b1 * c2 - b2 * c1) + a1 * (b2 * c0 - b0 * c2) + a2 * (b0 * c1 - b1 * c0);
        double invDet = 1.0 / det;
        double x = (d0 * (b1 * c2 - b2 * c1) + d1 * (b2 * c0 - b0 * c2) + d2 * (b0 * c1 - b1 * c0)) * invDet;
        double y = (a0 * (d1 * c2 - d2 * c1) + a1 * (d2 * c0 - d0 * c2) + a2 * (d0 * c1 - d1 * c0)) * invDet;
        double z = (a0 * (b1 * d2 - b2 * d1) + a1 * (b2 * d0 - b0 * d2) + a2 * (b0 * d1 - b1 * d0)) * invDet;
        return new Vector3d(x, y, z);
    }

    public Vector3d perspectiveOrigin(Vector3d dest) {
        double x = -m30 * (1.0 / m00);
        double y = -m31 * (1.0 / m11);
        double z = -m32;
        return new Vector3d(x, y, z);
    }

    public Vector3d perspectiveInvOrigin(Vector3d dest) {
        double x = -m30 / m00;
        double y = -m31 / m11;
        double z = -1.0 / m22;
        return new Vector3d(x, y, z);
    }

    public double perspectiveFov() {
        return 2.0 * Math.atan(1.0 / m11);
    }

    public double perspectiveNear() {
        return m32 / m22;
    }

    public double perspectiveFar() {
        return m32 / (m22 + 1.0);
    }

    public Matrix4d mirror(double nx, double ny, double nz, double d) {
        double invLen = 1.0 / Math.sqrt(nx * nx + ny * ny + nz * nz);
        double nnx = nx * invLen;
        double nny = ny * invLen;
        double nnz = nz * invLen;
        double nd = d * invLen;
        double r00 = 1.0 - 2.0 * nnx * nnx;
        double r01 = -2.0 * nny * nnx;
        double r02 = -2.0 * nnz * nnx;
        double r03 = -2.0 * nnx * nd;
        double r10 = -2.0 * nnx * nny;
        double r11 = 1.0 - 2.0 * nny * nny;
        double r12 = -2.0 * nnz * nny;
        double r13 = -2.0 * nny * nd;
        double r20 = -2.0 * nnx * nnz;
        double r21 = -2.0 * nny * nnz;
        double r22 = 1.0 - 2.0 * nnz * nnz;
        double r23 = -2.0 * nnz * nd;
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
        double t30 = m30; double t31 = m31; double t32 = m32; double t33 = m33;
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

    public Matrix4d mirror(Vector3d normal, double d) {
        return mirror(normal.x(), normal.y(), normal.z(), d);
    }

    public boolean equals(Matrix4d other, double delta) {
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

    public Matrix4d swap(Matrix4d other) {
        double t;
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

    public Vector3d getEulerAnglesYXZ() {
        double a = Math.asin(-m12);
        double ca = Math.cos(a);
        if (Math.abs(ca) > 1.0e-6) {
            return new Vector3d(Math.atan2(m02, m22), a, Math.atan2(m10, m11));
        }
        return new Vector3d(0.0, a, Math.atan2(-m01, m00));
    }

    public Vector3d getEulerAnglesXYZ() {
        double a = Math.asin(m20);
        double ca = Math.cos(a);
        if (Math.abs(ca) > 1.0e-6) {
            return new Vector3d(Math.atan2(-m21, m22), a, Math.atan2(-m10, m00));
        }
        return new Vector3d(0.0, a, Math.atan2(m01, m11));
    }

    public Matrix4d fma(Matrix4d other, double scalar) {
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

    public Matrix4d rotateAroundLocal(Quaterniond quat, Vector3d origin) {
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
        double t00 = m00; double t01 = m01; double t02 = m02; double t03 = m03;
        double t10 = m10; double t11 = m11; double t12 = m12; double t13 = m13;
        double t20 = m20; double t21 = m21; double t22 = m22; double t23 = m23;
        double t30 = m30; double t31 = m31; double t32 = m32; double t33 = m33;
        double ox = origin.x(), oy = origin.y(), oz = origin.z();
        double n30 = Math.fma(t00, ox, Math.fma(t10, oy, Math.fma(t20, oz, t30)));
        double n31 = Math.fma(t01, ox, Math.fma(t11, oy, Math.fma(t21, oz, t31)));
        double n32 = Math.fma(t02, ox, Math.fma(t12, oy, Math.fma(t22, oz, t32)));
        double n33 = Math.fma(t03, ox, Math.fma(t13, oy, Math.fma(t23, oz, t33)));
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

    public Matrix4d zeroTranslation() {
        m30 = 0.0; m31 = 0.0; m32 = 0.0;
        return this;
    }

    public Matrix4d setRow(int row, double... values) {
        double v0 = values.length > 0 ? values[0] : 0.0;
        double v1 = values.length > 1 ? values[1] : 0.0;
        double v2 = values.length > 2 ? values[2] : 0.0;
        double v3 = values.length > 3 ? values[3] : 0.0;
        if (row == 0) { m00 = v0; m10 = v1; m20 = v2; m30 = v3; }
        else if (row == 1) { m01 = v0; m11 = v1; m21 = v2; m31 = v3; }
        else if (row == 2) { m02 = v0; m12 = v1; m22 = v2; m32 = v3; }
        else if (row == 3) { m03 = v0; m13 = v1; m23 = v2; m33 = v3; }
        else throw new IndexOutOfBoundsException("Row: " + row);
        return this;
    }

    public Matrix4d setColumn(int col, double... values) {
        double v0 = values.length > 0 ? values[0] : 0.0;
        double v1 = values.length > 1 ? values[1] : 0.0;
        double v2 = values.length > 2 ? values[2] : 0.0;
        double v3 = values.length > 3 ? values[3] : 0.0;
        if (col == 0) { m00 = v0; m01 = v1; m02 = v2; m03 = v3; }
        else if (col == 1) { m10 = v0; m11 = v1; m12 = v2; m13 = v3; }
        else if (col == 2) { m20 = v0; m21 = v1; m22 = v2; m23 = v3; }
        else if (col == 3) { m30 = v0; m31 = v1; m32 = v2; m33 = v3; }
        else throw new IndexOutOfBoundsException("Column: " + col);
        return this;
    }

    public Vector4d getRow(int row) {
        if (row == 0) return new Vector4d(m00, m10, m20, m30);
        else if (row == 1) return new Vector4d(m01, m11, m21, m31);
        else if (row == 2) return new Vector4d(m02, m12, m22, m32);
        else return new Vector4d(m03, m13, m23, m33);
    }

    public Vector4d getColumn(int col) {
        if (col == 0) return new Vector4d(m00, m01, m02, m03);
        else if (col == 1) return new Vector4d(m10, m11, m12, m13);
        else if (col == 2) return new Vector4d(m20, m21, m22, m23);
        else return new Vector4d(m30, m31, m32, m33);
    }

    public Matrix4d add4x3(Matrix4d other) {
        m00 += other.m00; m01 += other.m01; m02 += other.m02;
        m10 += other.m10; m11 += other.m11; m12 += other.m12;
        m20 += other.m20; m21 += other.m21; m22 += other.m22;
        m30 += other.m30; m31 += other.m31; m32 += other.m32;
        return this;
    }

    public Matrix4d sub4x3(Matrix4d other) {
        m00 -= other.m00; m01 -= other.m01; m02 -= other.m02;
        m10 -= other.m10; m11 -= other.m11; m12 -= other.m12;
        m20 -= other.m20; m21 -= other.m21; m22 -= other.m22;
        m30 -= other.m30; m31 -= other.m31; m32 -= other.m32;
        return this;
    }

    public Matrix4d mul4x3ComponentWise(Matrix4d other) {
        m00 *= other.m00; m01 *= other.m01; m02 *= other.m02;
        m10 *= other.m10; m11 *= other.m11; m12 *= other.m12;
        m20 *= other.m20; m21 *= other.m21; m22 *= other.m22;
        m30 *= other.m30; m31 *= other.m31; m32 *= other.m32;
        return this;
    }

    public Matrix4d fma4x3(Matrix4d other, double scalar) {
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

    public Matrix4d mul0(Matrix4d right) {
        double n00 = Math.fma(m00, right.m00, Math.fma(m10, right.m01, Math.fma(m20, right.m02, m30 * right.m03)));
        double n01 = Math.fma(m01, right.m00, Math.fma(m11, right.m01, Math.fma(m21, right.m02, m31 * right.m03)));
        double n02 = Math.fma(m02, right.m00, Math.fma(m12, right.m01, Math.fma(m22, right.m02, m32 * right.m03)));
        double n03 = Math.fma(m03, right.m00, Math.fma(m13, right.m01, Math.fma(m23, right.m02, m33 * right.m03)));
        double n10 = Math.fma(m00, right.m10, Math.fma(m10, right.m11, Math.fma(m20, right.m12, m30 * right.m13)));
        double n11 = Math.fma(m01, right.m10, Math.fma(m11, right.m11, Math.fma(m21, right.m12, m31 * right.m13)));
        double n12 = Math.fma(m02, right.m10, Math.fma(m12, right.m11, Math.fma(m22, right.m12, m32 * right.m13)));
        double n13 = Math.fma(m03, right.m10, Math.fma(m13, right.m11, Math.fma(m23, right.m12, m33 * right.m13)));
        double n20 = Math.fma(m00, right.m20, Math.fma(m10, right.m21, Math.fma(m20, right.m22, m30 * right.m23)));
        double n21 = Math.fma(m01, right.m20, Math.fma(m11, right.m21, Math.fma(m21, right.m22, m31 * right.m23)));
        double n22 = Math.fma(m02, right.m20, Math.fma(m12, right.m21, Math.fma(m22, right.m22, m32 * right.m23)));
        double n23 = Math.fma(m03, right.m20, Math.fma(m13, right.m21, Math.fma(m23, right.m22, m33 * right.m23)));
        double n30 = Math.fma(m00, right.m30, Math.fma(m10, right.m31, Math.fma(m20, right.m32, m30 * right.m33)));
        double n31 = Math.fma(m01, right.m30, Math.fma(m11, right.m31, Math.fma(m21, right.m32, m31 * right.m33)));
        double n32 = Math.fma(m02, right.m30, Math.fma(m12, right.m31, Math.fma(m22, right.m32, m32 * right.m33)));
        double n33 = Math.fma(m03, right.m30, Math.fma(m13, right.m31, Math.fma(m23, right.m32, m33 * right.m33)));
        m00 = n00; m01 = n01; m02 = n02; m03 = n03;
        m10 = n10; m11 = n11; m12 = n12; m13 = n13;
        m20 = n20; m21 = n21; m22 = n22; m23 = n23;
        m30 = n30; m31 = n31; m32 = n32; m33 = n33;
        return this;
    }

    public Matrix3d getUnnormalizedRotation(Matrix3d dest) {
        dest.m00 = m00; dest.m01 = m01; dest.m02 = m02;
        dest.m10 = m10; dest.m11 = m11; dest.m12 = m12;
        dest.m20 = m20; dest.m21 = m21; dest.m22 = m22;
        return dest;
    }

    public boolean testPoint(double x, double y, double z) {
        double px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        double py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        double pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        double pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        return Math.abs(px) <= pw && Math.abs(py) <= pw && Math.abs(pz) <= pw;
    }

    public boolean testSphere(double x, double y, double z, double r) {
        double px = Math.fma(m00, x, Math.fma(m10, y, Math.fma(m20, z, m30)));
        double py = Math.fma(m01, x, Math.fma(m11, y, Math.fma(m21, z, m31)));
        double pz = Math.fma(m02, x, Math.fma(m12, y, Math.fma(m22, z, m32)));
        double pw = Math.fma(m03, x, Math.fma(m13, y, Math.fma(m23, z, m33)));
        double ax = Math.abs(px) + Math.fma(Math.abs(m00), r, Math.fma(Math.abs(m10), r, Math.abs(m20) * r));
        double ay = Math.abs(py) + Math.fma(Math.abs(m01), r, Math.fma(Math.abs(m11), r, Math.abs(m21) * r));
        double az = Math.abs(pz) + Math.fma(Math.abs(m02), r, Math.fma(Math.abs(m12), r, Math.abs(m22) * r));
        double nw = Math.abs(pw);
        return ax <= nw && ay <= nw && az <= nw;
    }

    public boolean testAab(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double nxX = (m00 < 0 ? maxX : minX), nxY = (m10 < 0 ? maxY : minY), nxZ = (m20 < 0 ? maxZ : minZ);
        double pxX = (m00 < 0 ? minX : maxX), pxY = (m10 < 0 ? minY : maxY), pxZ = (m20 < 0 ? minZ : maxZ);
        double nyX = (m01 < 0 ? maxX : minX), nyY = (m11 < 0 ? maxY : minY), nyZ = (m21 < 0 ? maxZ : minZ);
        double pyX = (m01 < 0 ? minX : maxX), pyY = (m11 < 0 ? minY : maxY), pyZ = (m21 < 0 ? minZ : maxZ);
        double nzX = (m02 < 0 ? maxX : minX), nzY = (m12 < 0 ? maxY : minY), nzZ = (m22 < 0 ? maxZ : minZ);
        double pzX = (m02 < 0 ? minX : maxX), pzY = (m12 < 0 ? minY : maxY), pzZ = (m22 < 0 ? minZ : maxZ);
        double px = Math.fma(m00, pxX, Math.fma(m10, pxY, Math.fma(m20, pxZ, m30)));
        double nx = Math.fma(m00, nxX, Math.fma(m10, nxY, Math.fma(m20, nxZ, m30)));
        double py = Math.fma(m01, pyX, Math.fma(m11, pyY, Math.fma(m21, pyZ, m31)));
        double ny = Math.fma(m01, nyX, Math.fma(m11, nyY, Math.fma(m21, nyZ, m31)));
        double pz = Math.fma(m02, pzX, Math.fma(m12, pzY, Math.fma(m22, pzZ, m32)));
        double nz = Math.fma(m02, nzX, Math.fma(m12, nzY, Math.fma(m22, nzZ, m32)));
        double pw = Math.fma(m03, pxX, Math.fma(m13, pxY, Math.fma(m23, pxZ, m33)));
        double nw = Math.fma(m03, nxX, Math.fma(m13, nxY, Math.fma(m23, nxZ, m33)));
        return Math.max(nx, -px) <= Math.min(pw, -nw) && Math.max(ny, -py) <= Math.min(pw, -nw) && Math.max(nz, -pz) <= Math.min(pw, -nw);
    }

    public Matrix4d frustumAabb(Vector3d min, Vector3d max) {
        Matrix4d inv = new Matrix4d(this);
        inv.invert();
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 8; i++) {
            double x = (i & 1) == 0 ? -1.0 : 1.0;
            double y = (i & 2) == 0 ? -1.0 : 1.0;
            double z = (i & 4) == 0 ? -1.0 : 1.0;
            double px = Math.fma(inv.m00, x, Math.fma(inv.m10, y, Math.fma(inv.m20, z, inv.m30)));
            double py = Math.fma(inv.m01, x, Math.fma(inv.m11, y, Math.fma(inv.m21, z, inv.m31)));
            double pz = Math.fma(inv.m02, x, Math.fma(inv.m12, y, Math.fma(inv.m22, z, inv.m32)));
            double pw = Math.fma(inv.m03, x, Math.fma(inv.m13, y, Math.fma(inv.m23, z, inv.m33)));
            double invW = 1.0 / pw;
            double fx = px * invW, fy = py * invW, fz = pz * invW;
            if (fx < minX) minX = fx;
            if (fy < minY) minY = fy;
            if (fz < minZ) minZ = fz;
            if (fx > maxX) maxX = fx;
            if (fy > maxY) maxY = fy;
            if (fz > maxZ) maxZ = fz;
        }
        return this;
    }

    public Matrix4d transformAab(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Vector3d outMin, Vector3d outMax) {
        double nxX = (m00 < 0 ? maxX : minX), nxY = (m10 < 0 ? maxY : minY), nxZ = (m20 < 0 ? maxZ : minZ);
        double pxX = (m00 < 0 ? minX : maxX), pxY = (m10 < 0 ? minY : maxY), pxZ = (m20 < 0 ? minZ : maxZ);
        double nyX = (m01 < 0 ? maxX : minX), nyY = (m11 < 0 ? maxY : minY), nyZ = (m21 < 0 ? maxZ : minZ);
        double pyX = (m01 < 0 ? minX : maxX), pyY = (m11 < 0 ? minY : maxY), pyZ = (m21 < 0 ? minZ : maxZ);
        double nzX = (m02 < 0 ? maxX : minX), nzY = (m12 < 0 ? maxY : minY), nzZ = (m22 < 0 ? maxZ : minZ);
        double pzX = (m02 < 0 ? minX : maxX), pzY = (m12 < 0 ? minY : maxY), pzZ = (m22 < 0 ? minZ : maxZ);
        double fx = Math.fma(m00, pxX, Math.fma(m10, pxY, Math.fma(m20, pxZ, m30)));
        double fy = Math.fma(m01, pyX, Math.fma(m11, pyY, Math.fma(m21, pyZ, m31)));
        double fz = Math.fma(m02, pzX, Math.fma(m12, pzY, Math.fma(m22, pzZ, m32)));
        double nx = Math.fma(m00, nxX, Math.fma(m10, nxY, Math.fma(m20, nxZ, m30)));
        double ny = Math.fma(m01, nyX, Math.fma(m11, nyY, Math.fma(m21, nyZ, m31)));
        double nz = Math.fma(m02, nzX, Math.fma(m12, nzY, Math.fma(m22, nzZ, m32)));
        return this;
    }

    public Matrix4d affineSpan(Vector3d corner00, Vector3d corner01, Vector3d corner10, Vector3d corner11) {
        return this;
    }

    public Matrix4d setTransposed(Matrix4d other) {
        m00 = other.m00; m01 = other.m10; m02 = other.m20; m03 = other.m30;
        m10 = other.m01; m11 = other.m11; m12 = other.m21; m13 = other.m31;
        m20 = other.m02; m21 = other.m12; m22 = other.m22; m23 = other.m32;
        m30 = other.m03; m31 = other.m13; m32 = other.m23; m33 = other.m33;
        return this;
    }
}
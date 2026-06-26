package com.jamma.joml;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Matrix4f {

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

    public Matrix4f(com.jamma.math.matrix.Matrix4f jamma) {
        m00 = jamma.m00; m01 = jamma.m01; m02 = jamma.m02; m03 = jamma.m03;
        m10 = jamma.m10; m11 = jamma.m11; m12 = jamma.m12; m13 = jamma.m13;
        m20 = jamma.m20; m21 = jamma.m21; m22 = jamma.m22; m23 = jamma.m23;
        m30 = jamma.m30; m31 = jamma.m31; m32 = jamma.m32; m33 = jamma.m33;
    }

    public com.jamma.math.matrix.Matrix4f toJamma() {
        return new com.jamma.math.matrix.Matrix4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public static Matrix4f fromJamma(com.jamma.math.matrix.Matrix4f jamma) {
        return new Matrix4f(jamma);
    }

    public Matrix4f set(Matrix4f other) {
        m00 = other.m00; m01 = other.m01; m02 = other.m02; m03 = other.m03;
        m10 = other.m10; m11 = other.m11; m12 = other.m12; m13 = other.m13;
        m20 = other.m20; m21 = other.m21; m22 = other.m22; m23 = other.m23;
        m30 = other.m30; m31 = other.m31; m32 = other.m32; m33 = other.m33;
        return this;
    }

    public Matrix4f identity() {
        m00 = 1f; m01 = 0f; m02 = 0f; m03 = 0f;
        m10 = 0f; m11 = 1f; m12 = 0f; m13 = 0f;
        m20 = 0f; m21 = 0f; m22 = 1f; m23 = 0f;
        m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f;
        return this;
    }

    public Matrix4f zero() {
        m00 = 0f; m01 = 0f; m02 = 0f; m03 = 0f;
        m10 = 0f; m11 = 0f; m12 = 0f; m13 = 0f;
        m20 = 0f; m21 = 0f; m22 = 0f; m23 = 0f;
        m30 = 0f; m31 = 0f; m32 = 0f; m33 = 0f;
        return this;
    }

    public Matrix4f mul(Matrix4f r) {
        float nm00 = Math.fma(m00, r.m00, Math.fma(m01, r.m10, Math.fma(m02, r.m20, m03 * r.m30)));
        float nm01 = Math.fma(m00, r.m01, Math.fma(m01, r.m11, Math.fma(m02, r.m21, m03 * r.m31)));
        float nm02 = Math.fma(m00, r.m02, Math.fma(m01, r.m12, Math.fma(m02, r.m22, m03 * r.m32)));
        float nm03 = Math.fma(m00, r.m03, Math.fma(m01, r.m13, Math.fma(m02, r.m23, m03 * r.m33)));
        float nm10 = Math.fma(m10, r.m00, Math.fma(m11, r.m10, Math.fma(m12, r.m20, m13 * r.m30)));
        float nm11 = Math.fma(m10, r.m01, Math.fma(m11, r.m11, Math.fma(m12, r.m21, m13 * r.m31)));
        float nm12 = Math.fma(m10, r.m02, Math.fma(m11, r.m12, Math.fma(m12, r.m22, m13 * r.m32)));
        float nm13 = Math.fma(m10, r.m03, Math.fma(m11, r.m13, Math.fma(m12, r.m23, m13 * r.m33)));
        float nm20 = Math.fma(m20, r.m00, Math.fma(m21, r.m10, Math.fma(m22, r.m20, m23 * r.m30)));
        float nm21 = Math.fma(m20, r.m01, Math.fma(m21, r.m11, Math.fma(m22, r.m21, m23 * r.m31)));
        float nm22 = Math.fma(m20, r.m02, Math.fma(m21, r.m12, Math.fma(m22, r.m22, m23 * r.m32)));
        float nm23 = Math.fma(m20, r.m03, Math.fma(m21, r.m13, Math.fma(m22, r.m23, m23 * r.m33)));
        float nm30 = Math.fma(m30, r.m00, Math.fma(m31, r.m10, Math.fma(m32, r.m20, m33 * r.m30)));
        float nm31 = Math.fma(m30, r.m01, Math.fma(m31, r.m11, Math.fma(m32, r.m21, m33 * r.m31)));
        float nm32 = Math.fma(m30, r.m02, Math.fma(m31, r.m12, Math.fma(m32, r.m22, m33 * r.m32)));
        float nm33 = Math.fma(m30, r.m03, Math.fma(m31, r.m13, Math.fma(m32, r.m23, m33 * r.m33)));
        m00 = nm00; m01 = nm01; m02 = nm02; m03 = nm03;
        m10 = nm10; m11 = nm11; m12 = nm12; m13 = nm13;
        m20 = nm20; m21 = nm21; m22 = nm22; m23 = nm23;
        m30 = nm30; m31 = nm31; m32 = nm32; m33 = nm33;
        return this;
    }

    public Matrix4f transpose() {
        float nm00 = m00; float nm01 = m10; float nm02 = m20; float nm03 = m30;
        float nm10 = m01; float nm11 = m11; float nm12 = m21; float nm13 = m31;
        float nm20 = m02; float nm21 = m12; float nm22 = m22; float nm23 = m32;
        float nm30 = m03; float nm31 = m13; float nm32 = m23; float nm33 = m33;
        m00 = nm00; m01 = nm01; m02 = nm02; m03 = nm03;
        m10 = nm10; m11 = nm11; m12 = nm12; m13 = nm13;
        m20 = nm20; m21 = nm21; m22 = nm22; m23 = nm23;
        m30 = nm30; m31 = nm31; m32 = nm32; m33 = nm33;
        return this;
    }

    public Matrix4f translate(float x, float y, float z) {
        m03 = Math.fma(m00, x, Math.fma(m01, y, Math.fma(m02, z, m03)));
        m13 = Math.fma(m10, x, Math.fma(m11, y, Math.fma(m12, z, m13)));
        m23 = Math.fma(m20, x, Math.fma(m21, y, Math.fma(m22, z, m23)));
        m33 = Math.fma(m30, x, Math.fma(m31, y, Math.fma(m32, z, m33)));
        return this;
    }

    public Matrix4f translate(Vector3f v) {
        return translate(v.x, v.y, v.z);
    }

    public Matrix4f scale(float x, float y, float z) {
        m00 *= x; m01 *= y; m02 *= z;
        m10 *= x; m11 *= y; m12 *= z;
        m20 *= x; m21 *= y; m22 *= z;
        m30 *= x; m31 *= y; m32 *= z;
        return this;
    }

    public Matrix4f scale(float s) {
        return scale(s, s, s);
    }

    public Matrix4f scale(Vector3f v) {
        return scale(v.x, v.y, v.z);
    }

    public Matrix4f rotateX(float ang) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float nm01 = m01 * c + m02 * s;
        float nm02 = m01 * -s + m02 * c;
        float nm11 = m11 * c + m12 * s;
        float nm12 = m11 * -s + m12 * c;
        float nm21 = m21 * c + m22 * s;
        float nm22 = m21 * -s + m22 * c;
        float nm31 = m31 * c + m32 * s;
        float nm32 = m31 * -s + m32 * c;
        m01 = nm01; m02 = nm02;
        m11 = nm11; m12 = nm12;
        m21 = nm21; m22 = nm22;
        m31 = nm31; m32 = nm32;
        return this;
    }

    public Matrix4f rotateY(float ang) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float nm00 = m00 * c + m02 * -s;
        float nm02 = m00 * s + m02 * c;
        float nm10 = m10 * c + m12 * -s;
        float nm12 = m10 * s + m12 * c;
        float nm20 = m20 * c + m22 * -s;
        float nm22 = m20 * s + m22 * c;
        float nm30 = m30 * c + m32 * -s;
        float nm32 = m30 * s + m32 * c;
        m00 = nm00; m02 = nm02;
        m10 = nm10; m12 = nm12;
        m20 = nm20; m22 = nm22;
        m30 = nm30; m32 = nm32;
        return this;
    }

    public Matrix4f rotateZ(float ang) {
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        float nm00 = m00 * c + m01 * s;
        float nm01 = m00 * -s + m01 * c;
        float nm10 = m10 * c + m11 * s;
        float nm11 = m10 * -s + m11 * c;
        float nm20 = m20 * c + m21 * s;
        float nm21 = m20 * -s + m21 * c;
        float nm30 = m30 * c + m31 * s;
        float nm31 = m30 * -s + m31 * c;
        m00 = nm00; m01 = nm01;
        m10 = nm10; m11 = nm11;
        m20 = nm20; m21 = nm21;
        m30 = nm30; m31 = nm31;
        return this;
    }

    public float determinant() {
        return (m00 * m11 - m01 * m10) * (m22 * m33 - m23 * m32)
             - (m00 * m12 - m02 * m10) * (m21 * m33 - m23 * m31)
             + (m00 * m13 - m03 * m10) * (m21 * m32 - m22 * m31)
             + (m01 * m12 - m02 * m11) * (m20 * m33 - m23 * m30)
             - (m01 * m13 - m03 * m11) * (m20 * m32 - m22 * m30)
             + (m02 * m13 - m03 * m12) * (m20 * m31 - m21 * m30);
    }

    public Matrix4f invert() {
        float det = determinant();
        if (det == 0.0f) return zero();
        float invDet = 1.0f / det;
        float nm00 = (m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31)) * invDet;
        float nm01 = (m01 * (m22 * m33 - m23 * m32) - m02 * (m21 * m33 - m23 * m31) + m03 * (m21 * m32 - m22 * m31)) * -invDet;
        float nm02 = (m01 * (m12 * m33 - m13 * m32) - m02 * (m11 * m33 - m13 * m31) + m03 * (m11 * m32 - m12 * m31)) * invDet;
        float nm03 = (m01 * (m12 * m23 - m13 * m22) - m02 * (m11 * m23 - m13 * m21) + m03 * (m11 * m22 - m12 * m21)) * -invDet;
        float nm10 = (m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30)) * -invDet;
        float nm11 = (m00 * (m22 * m33 - m23 * m32) - m02 * (m20 * m33 - m23 * m30) + m03 * (m20 * m32 - m22 * m30)) * invDet;
        float nm12 = (m00 * (m12 * m33 - m13 * m32) - m02 * (m10 * m33 - m13 * m30) + m03 * (m10 * m32 - m12 * m30)) * -invDet;
        float nm13 = (m00 * (m12 * m23 - m13 * m22) - m02 * (m10 * m23 - m13 * m20) + m03 * (m10 * m22 - m12 * m20)) * invDet;
        float nm20 = (m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30)) * invDet;
        float nm21 = (m00 * (m21 * m33 - m23 * m31) - m01 * (m20 * m33 - m23 * m30) + m03 * (m20 * m31 - m21 * m30)) * -invDet;
        float nm22 = (m00 * (m11 * m33 - m13 * m31) - m01 * (m10 * m33 - m13 * m30) + m03 * (m10 * m31 - m11 * m30)) * invDet;
        float nm23 = (m00 * (m11 * m23 - m13 * m21) - m01 * (m10 * m23 - m13 * m20) + m03 * (m10 * m21 - m11 * m20)) * -invDet;
        float nm30 = (m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30)) * -invDet;
        float nm31 = (m00 * (m21 * m32 - m22 * m31) - m01 * (m20 * m32 - m22 * m30) + m02 * (m20 * m31 - m21 * m30)) * invDet;
        float nm32 = (m00 * (m11 * m32 - m12 * m31) - m01 * (m10 * m32 - m12 * m30) + m02 * (m10 * m31 - m11 * m30)) * -invDet;
        float nm33 = (m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20)) * invDet;
        m00 = nm00; m01 = nm01; m02 = nm02; m03 = nm03;
        m10 = nm10; m11 = nm11; m12 = nm12; m13 = nm13;
        m20 = nm20; m21 = nm21; m22 = nm22; m23 = nm23;
        m30 = nm30; m31 = nm31; m32 = nm32; m33 = nm33;
        return this;
    }

    public Matrix4f perspective(float fovY, float aspect, float zNear, float zFar) {
        float h = (float) Math.tan(fovY * 0.5f);
        m00 = 1.0f / (aspect * h);
        m01 = 0f; m02 = 0f; m03 = 0f;
        m10 = 0f; m11 = 1.0f / h;
        m12 = 0f; m13 = 0f;
        m20 = 0f; m21 = 0f;
        m22 = -(zFar + zNear) / (zFar - zNear);
        m23 = -2.0f * zFar * zNear / (zFar - zNear);
        m30 = 0f; m31 = 0f; m32 = -1f; m33 = 0f;
        return this;
    }

    public Matrix4f lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        float dirX = centerX - eyeX;
        float dirY = centerY - eyeY;
        float dirZ = centerZ - eyeZ;
        float dirLen = (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX /= dirLen; dirY /= dirLen; dirZ /= dirLen;
        float upLen = (float) Math.sqrt(upX * upX + upY * upY + upZ * upZ);
        upX /= upLen; upY /= upLen; upZ /= upLen;
        float sX = dirY * upZ - dirZ * upY;
        float sY = dirZ * upX - dirX * upZ;
        float sZ = dirX * upY - dirY * upX;
        float sLen = (float) Math.sqrt(sX * sX + sY * sY + sZ * sZ);
        sX /= sLen; sY /= sLen; sZ /= sLen;
        float uX = sY * dirZ - sZ * dirY;
        float uY = sZ * dirX - sX * dirZ;
        float uZ = sX * dirY - sY * dirX;
        m00 = sX; m01 = sY; m02 = sZ; m03 = -(sX * eyeX + sY * eyeY + sZ * eyeZ);
        m10 = uX; m11 = uY; m12 = uZ; m13 = -(uX * eyeX + uY * eyeY + uZ * eyeZ);
        m20 = -dirX; m21 = -dirY; m22 = -dirZ; m23 = dirX * eyeX + dirY * eyeY + dirZ * eyeZ;
        m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f;
        return this;
    }

    public Vector4f transform(Vector4f v) {
        float rx = Math.fma(m00, v.x, Math.fma(m01, v.y, Math.fma(m02, v.z, m03 * v.w)));
        float ry = Math.fma(m10, v.x, Math.fma(m11, v.y, Math.fma(m12, v.z, m13 * v.w)));
        float rz = Math.fma(m20, v.x, Math.fma(m21, v.y, Math.fma(m22, v.z, m23 * v.w)));
        float rw = Math.fma(m30, v.x, Math.fma(m31, v.y, Math.fma(m32, v.z, m33 * v.w)));
        v.x = rx; v.y = ry; v.z = rz; v.w = rw;
        return v;
    }

    public Vector3f transformPosition(Vector3f v) {
        float rx = Math.fma(m00, v.x, Math.fma(m01, v.y, Math.fma(m02, v.z, m03)));
        float ry = Math.fma(m10, v.x, Math.fma(m11, v.y, Math.fma(m12, v.z, m13)));
        float rz = Math.fma(m20, v.x, Math.fma(m21, v.y, Math.fma(m22, v.z, m23)));
        float rw = Math.fma(m30, v.x, Math.fma(m31, v.y, Math.fma(m32, v.z, m33)));
        float invW = 1.0f / rw;
        v.x = rx * invW; v.y = ry * invW; v.z = rz * invW;
        return v;
    }

    public Vector3f transformDirection(Vector3f v) {
        float rx = Math.fma(m00, v.x, Math.fma(m01, v.y, m02 * v.z));
        float ry = Math.fma(m10, v.x, Math.fma(m11, v.y, m12 * v.z));
        float rz = Math.fma(m20, v.x, Math.fma(m21, v.y, m22 * v.z));
        v.x = rx; v.y = ry; v.z = rz;
        return v;
    }

    public float get(int col, int row) {
        return switch (col * 4 + row) {
            case 0 -> m00; case 4 -> m01; case 8 -> m02; case 12 -> m03;
            case 1 -> m10; case 5 -> m11; case 9 -> m12; case 13 -> m13;
            case 2 -> m20; case 6 -> m21; case 10 -> m22; case 14 -> m23;
            case 3 -> m30; case 7 -> m31; case 11 -> m32; case 15 -> m33;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public Matrix4f get(Matrix4f dest) {
        dest.set(this);
        return dest;
    }

    public float[] get(float[] dest) {
        dest[0] = m00; dest[1] = m10; dest[2] = m20; dest[3] = m30;
        dest[4] = m01; dest[5] = m11; dest[6] = m21; dest[7] = m31;
        dest[8] = m02; dest[9] = m12; dest[10] = m22; dest[11] = m32;
        dest[12] = m03; dest[13] = m13; dest[14] = m23; dest[15] = m33;
        return dest;
    }

    public Matrix4f load(MemorySegment segment, long offset) {
        m00 = segment.get(ValueLayout.JAVA_FLOAT, offset);
        m10 = segment.get(ValueLayout.JAVA_FLOAT, offset + 4);
        m20 = segment.get(ValueLayout.JAVA_FLOAT, offset + 8);
        m30 = segment.get(ValueLayout.JAVA_FLOAT, offset + 12);
        m01 = segment.get(ValueLayout.JAVA_FLOAT, offset + 16);
        m11 = segment.get(ValueLayout.JAVA_FLOAT, offset + 20);
        m21 = segment.get(ValueLayout.JAVA_FLOAT, offset + 24);
        m31 = segment.get(ValueLayout.JAVA_FLOAT, offset + 28);
        m02 = segment.get(ValueLayout.JAVA_FLOAT, offset + 32);
        m12 = segment.get(ValueLayout.JAVA_FLOAT, offset + 36);
        m22 = segment.get(ValueLayout.JAVA_FLOAT, offset + 40);
        m32 = segment.get(ValueLayout.JAVA_FLOAT, offset + 44);
        m03 = segment.get(ValueLayout.JAVA_FLOAT, offset + 48);
        m13 = segment.get(ValueLayout.JAVA_FLOAT, offset + 52);
        m23 = segment.get(ValueLayout.JAVA_FLOAT, offset + 56);
        m33 = segment.get(ValueLayout.JAVA_FLOAT, offset + 60);
        return this;
    }

    public Matrix4f store(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, m00);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, m10);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, m20);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 12, m30);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 16, m01);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 20, m11);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 24, m21);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 28, m31);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 32, m02);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 36, m12);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 40, m22);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 44, m32);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 48, m03);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 52, m13);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 56, m23);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 60, m33);
        return this;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(m00) ^ Float.hashCode(m11) ^ Float.hashCode(m22) ^ Float.hashCode(m33);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix4f m)) return false;
        return Float.floatToIntBits(m00) == Float.floatToIntBits(m.m00)
            && Float.floatToIntBits(m01) == Float.floatToIntBits(m.m01)
            && Float.floatToIntBits(m02) == Float.floatToIntBits(m.m02)
            && Float.floatToIntBits(m03) == Float.floatToIntBits(m.m03)
            && Float.floatToIntBits(m10) == Float.floatToIntBits(m.m10)
            && Float.floatToIntBits(m11) == Float.floatToIntBits(m.m11)
            && Float.floatToIntBits(m12) == Float.floatToIntBits(m.m12)
            && Float.floatToIntBits(m13) == Float.floatToIntBits(m.m13)
            && Float.floatToIntBits(m20) == Float.floatToIntBits(m.m20)
            && Float.floatToIntBits(m21) == Float.floatToIntBits(m.m21)
            && Float.floatToIntBits(m22) == Float.floatToIntBits(m.m22)
            && Float.floatToIntBits(m23) == Float.floatToIntBits(m.m23)
            && Float.floatToIntBits(m30) == Float.floatToIntBits(m.m30)
            && Float.floatToIntBits(m31) == Float.floatToIntBits(m.m31)
            && Float.floatToIntBits(m32) == Float.floatToIntBits(m.m32)
            && Float.floatToIntBits(m33) == Float.floatToIntBits(m.m33);
    }

    @Override
    public String toString() {
        return String.format("""
            %f %f %f %f
            %f %f %f %f
            %f %f %f %f
            %f %f %f %f""", m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }
}

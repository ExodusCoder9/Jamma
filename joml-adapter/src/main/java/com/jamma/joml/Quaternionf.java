package com.jamma.joml;

public class Quaternionf {

    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternionf() {
        w = 1.0f;
    }

    public Quaternionf(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternionf(Quaternionf q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }

    public Quaternionf(com.jamma.math.Quaternionf jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
        this.w = jamma.w();
    }

    public com.jamma.math.Quaternionf toJamma() {
        return new com.jamma.math.Quaternionf(x, y, z, w);
    }

    public static Quaternionf fromJamma(com.jamma.math.Quaternionf jamma) {
        return new Quaternionf(jamma.x(), jamma.y(), jamma.z(), jamma.w());
    }

    public Quaternionf set(Quaternionf q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
        return this;
    }

    public Quaternionf set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternionf normalize() {
        float inv = 1.0f / (float) Math.sqrt(x * x + y * y + z * z + w * w);
        x *= inv;
        y *= inv;
        z *= inv;
        w *= inv;
        return this;
    }

    public Quaternionf conjugate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Quaternionf invert() {
        float normSq = x * x + y * y + z * z + w * w;
        float inv = 1.0f / normSq;
        x = -x * inv;
        y = -y * inv;
        z = -z * inv;
        w *= inv;
        return this;
    }

    public Quaternionf mul(Quaternionf q) {
        float rx = Math.fma(w, q.x, Math.fma(x, q.w, Math.fma(y, q.z, -z * q.y)));
        float ry = Math.fma(w, q.y, Math.fma(-x, q.z, Math.fma(y, q.w, z * q.x)));
        float rz = Math.fma(w, q.z, Math.fma(x, q.y, Math.fma(-y, q.x, z * q.w)));
        float rw = Math.fma(w, q.w, Math.fma(-x, q.x, Math.fma(-y, q.y, -z * q.z)));
        x = rx;
        y = ry;
        z = rz;
        w = rw;
        return this;
    }

    public Quaternionf premul(Quaternionf q) {
        float rx = Math.fma(q.w, x, Math.fma(q.x, w, Math.fma(q.y, z, -q.z * y)));
        float ry = Math.fma(q.w, y, Math.fma(-q.x, z, Math.fma(q.y, w, q.z * x)));
        float rz = Math.fma(q.w, z, Math.fma(q.x, y, Math.fma(-q.y, x, q.z * w)));
        float rw = Math.fma(q.w, w, Math.fma(-q.x, x, Math.fma(-q.y, y, -q.z * z)));
        x = rx;
        y = ry;
        z = rz;
        w = rw;
        return this;
    }

    public Vector3f transform(Vector3f v) {
        float qx = x, qy = y, qz = z, qw = w;
        float tx = 2.0f * (qy * v.z - qz * v.y);
        float ty = 2.0f * (qz * v.x - qx * v.z);
        float tz = 2.0f * (qx * v.y - qy * v.x);
        v.x += qw * tx + (qy * tz - qz * ty);
        v.y += qw * ty + (qz * tx - qx * tz);
        v.z += qw * tz + (qx * ty - qy * tx);
        return v;
    }

    public Quaternionf fromAxisAngle(Vector3f axis, float angle) {
        float half = angle * 0.5f;
        float s = (float) Math.sin(half);
        float len = axis.length();
        if (len == 0.0f) return identity();
        float invLen = 1.0f / len;
        x = axis.x * invLen * s;
        y = axis.y * invLen * s;
        z = axis.z * invLen * s;
        w = (float) Math.cos(half);
        return this;
    }

    public Quaternionf fromEulerAnglesXYZ(float xAngle, float yAngle, float zAngle) {
        float sx = (float) Math.sin(xAngle * 0.5f);
        float cx = (float) Math.cos(xAngle * 0.5f);
        float sy = (float) Math.sin(yAngle * 0.5f);
        float cy = (float) Math.cos(yAngle * 0.5f);
        float sz = (float) Math.sin(zAngle * 0.5f);
        float cz = (float) Math.cos(zAngle * 0.5f);
        x = Math.fma(sx, Math.fma(cy, cz, sy * sz), cx * Math.fma(sy, cz, -cy * sz));
        y = Math.fma(cx, Math.fma(sy, cz, cy * sz), sx * Math.fma(-cy, cz, sy * sz));
        z = Math.fma(cx, Math.fma(cy, sz, -sy * cz), sx * Math.fma(sy, sz, cy * cz));
        w = Math.fma(cx, Math.fma(cy, cz, -sy * sz), sx * Math.fma(-sy, cz, -cy * sz));
        return this;
    }

    public Quaternionf nlerp(Quaternionf target, float t) {
        float dot = x * target.x + y * target.y + z * target.z + w * target.w;
        float tx = x + t * (target.x - x);
        float ty = y + t * (target.y - y);
        float tz = z + t * (target.z - z);
        float tw = w + t * (target.w - w);
        if (dot < 0.0f) {
            tx = x + t * (-target.x - x);
            ty = y + t * (-target.y - y);
            tz = z + t * (-target.z - z);
            tw = w + t * (-target.w - w);
        }
        float inv = 1.0f / (float) Math.sqrt(tx * tx + ty * ty + tz * tz + tw * tw);
        x = tx * inv;
        y = ty * inv;
        z = tz * inv;
        w = tw * inv;
        return this;
    }

    public Quaternionf slerp(Quaternionf target, float t) {
        float cosom = x * target.x + y * target.y + z * target.z + w * target.w;
        float sx = target.x, sy = target.y, sz = target.z, sw = target.w;
        if (cosom < 0.0f) {
            cosom = -cosom;
            sx = -sx;
            sy = -sy;
            sz = -sz;
            sw = -sw;
        }
        float scale0, scale1;
        if (1.0f - cosom > 1e-6f) {
            float omega = (float) Math.acos(cosom);
            float invSin = 1.0f / (float) Math.sin(omega);
            scale0 = (float) Math.sin((1.0f - t) * omega) * invSin;
            scale1 = (float) Math.sin(t * omega) * invSin;
        } else {
            scale0 = 1.0f - t;
            scale1 = t;
        }
        x = scale0 * x + scale1 * sx;
        y = scale0 * y + scale1 * sy;
        z = scale0 * z + scale1 * sz;
        w = scale0 * w + scale1 * sw;
        return this;
    }

    public Quaternionf identity() {
        x = y = z = 0.0f;
        w = 1.0f;
        return this;
    }

    public Quaternionf rotateTo(Vector3f from, Vector3f to) {
        float d = from.dot(to);
        float fromLen = from.length();
        float toLen = to.length();
        if (fromLen == 0.0f || toLen == 0.0f) return identity();
        Vector3f axis = new Vector3f(from).cross(to);
        if (axis.lengthSquared() < 1e-8f) {
            return identity();
        }
        axis.normalize();
        float angle = (float) Math.acos(Math.clamp(d / (fromLen * toLen), -1.0f, 1.0f));
        return fromAxisAngle(axis, angle);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public Quaternionf add(Quaternionf q) {
        x += q.x;
        y += q.y;
        z += q.z;
        w += q.w;
        return this;
    }

    public Quaternionf sub(Quaternionf q) {
        x -= q.x;
        y -= q.y;
        z -= q.z;
        w -= q.w;
        return this;
    }

    public Quaternionf mul(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        w *= scalar;
        return this;
    }

    public Quaternionf div(float scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        w /= scalar;
        return this;
    }

    public float dot(Quaternionf q) {
        return x * q.x + y * q.y + z * q.z + w * q.w;
    }

    public float angle() {
        return 2.0f * (float) Math.acos(Math.clamp(w, -1.0f, 1.0f));
    }

    public Vector3f getEulerAnglesXYZ(Vector3f dest) {
        dest.x = (float) Math.atan2(Math.fma(2.0f, y, w), Math.fma(-2.0f, z, -2.0f * x + 1.0f));
        dest.y = (float) Math.asin(Math.clamp(Math.fma(2.0f, w, -x), -1.0f, 1.0f));
        dest.z = (float) Math.atan2(Math.fma(2.0f, x, w), Math.fma(-2.0f, y, -2.0f * z + 1.0f));
        return dest;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x) ^ Float.hashCode(y) ^ Float.hashCode(z) ^ Float.hashCode(w);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quaternionf q)) return false;
        return Float.floatToIntBits(x) == Float.floatToIntBits(q.x)
            && Float.floatToIntBits(y) == Float.floatToIntBits(q.y)
            && Float.floatToIntBits(z) == Float.floatToIntBits(q.z)
            && Float.floatToIntBits(w) == Float.floatToIntBits(q.w);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}

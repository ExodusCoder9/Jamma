package com.jamma.math.quaternion;

import com.jamma.math.Vector3f;

public record Quaternionf(float x, float y, float z, float w) {

    public static final Quaternionf IDENTITY = new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);

    public static Quaternionf identity() {
        return IDENTITY;
    }

    public static Quaternionf fromAxisAngle(Vector3f axis, float angle) {
        float halfAngle = angle * 0.5f;
        float sinHalf = (float) Math.sin(halfAngle);
        float cosHalf = (float) Math.cos(halfAngle);
        float invLen = 1.0f / (float) Math.sqrt(axis.x() * axis.x() + axis.y() * axis.y() + axis.z() * axis.z());
        return new Quaternionf(
            axis.x() * invLen * sinHalf,
            axis.y() * invLen * sinHalf,
            axis.z() * invLen * sinHalf,
            cosHalf
        );
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public Quaternionf normalize() {
        float len = length();
        if (len == 0.0f) {
            return IDENTITY;
        }
        float invLen = 1.0f / len;
        return new Quaternionf(x * invLen, y * invLen, z * invLen, w * invLen);
    }

    public Quaternionf mul(Quaternionf q) {
        return new Quaternionf(
            Math.fma(w, q.x, Math.fma(x, q.w, Math.fma(y, q.z, -z * q.y))),
            Math.fma(w, q.y, Math.fma(-x, q.z, Math.fma(y, q.w, z * q.x))),
            Math.fma(w, q.z, Math.fma(x, q.y, Math.fma(-y, q.x, z * q.w))),
            Math.fma(w, q.w, Math.fma(-x, q.x, Math.fma(-y, q.y, -z * q.z)))
        );
    }

    public Quaternionf slerp(Quaternionf target, float t) {
        float cosHalfTheta = w * target.w + x * target.x + y * target.y + z * target.z;
        float q1x = target.x, q1y = target.y, q1z = target.z, q1w = target.w;
        if (cosHalfTheta < 0.0f) {
            cosHalfTheta = -cosHalfTheta;
            q1x = -q1x; q1y = -q1y; q1z = -q1z; q1w = -q1w;
        }
        if (Math.abs(cosHalfTheta) >= 1.0f) {
            return this;
        }
        float sinHalfTheta = (float) Math.sqrt(1.0f - cosHalfTheta * cosHalfTheta);
        if (Math.abs(sinHalfTheta) < 1e-3f) {
            return new Quaternionf(
                Math.fma(1.0f - t, x, t * q1x),
                Math.fma(1.0f - t, y, t * q1y),
                Math.fma(1.0f - t, z, t * q1z),
                Math.fma(1.0f - t, w, t * q1w)
            ).normalize();
        }
        float halfTheta = (float) Math.acos(cosHalfTheta);
        float ratioA = (float) Math.sin((1.0f - t) * halfTheta) / sinHalfTheta;
        float ratioB = (float) Math.sin(t * halfTheta) / sinHalfTheta;
        return new Quaternionf(
            x * ratioA + q1x * ratioB,
            y * ratioA + q1y * ratioB,
            z * ratioA + q1z * ratioB,
            w * ratioA + q1w * ratioB
        );
    }

    public Quaternionf squad(Quaternionf a, Quaternionf b, Quaternionf q2, float t) {
        Quaternionf s1 = this.slerp(q2, t);
        Quaternionf s2 = a.slerp(b, t);
        return s1.slerp(s2, 2.0f * t * (1.0f - t));
    }

    public Vector3f getEulerAnglesXYZ() {
        float m00 = Math.fma(2.0f, -y*y - z*z, 1.0f);
        float m10 = Math.fma(2.0f, x*y + z*w, 0.0f);
        float m20 = Math.fma(2.0f, x*z - y*w, 0.0f);
        float m21 = Math.fma(2.0f, y*z + x*w, 0.0f);
        float m22 = Math.fma(2.0f, -x*x - y*y, 1.0f);
        float m12 = Math.fma(2.0f, y*z - x*w, 0.0f);
        float m11 = Math.fma(2.0f, -x*x - z*z, 1.0f);
        
        float sy = (float) Math.sqrt(m00 * m00 + m10 * m10);
        float xAngle, yAngle, zAngle;
        if (sy > 1e-6f) {
            xAngle = (float) Math.atan2(-m21, m22);
            yAngle = (float) Math.atan2(-m20, sy);
            zAngle = (float) Math.atan2(m10, m00);
        } else {
            xAngle = (float) Math.atan2(-m12, m11);
            yAngle = (float) Math.atan2(-m20, sy);
            zAngle = 0.0f;
        }
        return new Vector3f(xAngle, yAngle, zAngle);
    }
}

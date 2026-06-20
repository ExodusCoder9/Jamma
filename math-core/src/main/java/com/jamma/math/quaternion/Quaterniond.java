package com.jamma.math.quaternion;

import com.jamma.math.Vector3D;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.DoubleBuffer;

public record Quaterniond(double x, double y, double z, double w) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Quaterniond fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Quaterniond(
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 8),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 16),
            src.get(ValueLayout.JAVA_DOUBLE, byteOffset + 24)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset, x);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 8, y);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 16, z);
        dest.set(ValueLayout.JAVA_DOUBLE, byteOffset + 24, w);
    }

    public static Quaterniond fromBuffer(DoubleBuffer src) {
        return new Quaterniond(src.get(), src.get(), src.get(), src.get());
    }

    public static Quaterniond fromBuffer(int index, DoubleBuffer src) {
        return new Quaterniond(src.get(index), src.get(index + 1), src.get(index + 2), src.get(index + 3));
    }

    public DoubleBuffer writeToBuffer(DoubleBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        dest.put(w);
        return dest;
    }

    public DoubleBuffer writeToBuffer(int index, DoubleBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        dest.put(index + 3, w);
        return dest;
    }

    public static final Quaterniond IDENTITY = new Quaterniond(0.0, 0.0, 0.0, 1.0);

    public static Quaterniond identity() {
        return IDENTITY;
    }

    public static Quaterniond fromAxisAngle(Vector3D axis, double angle) {
        double halfAngle = angle * 0.5;
        double sinHalf = Math.sin(halfAngle);
        double cosHalf = Math.cos(halfAngle);
        double invLen = 1.0 / Math.sqrt(axis.x() * axis.x() + axis.y() * axis.y() + axis.z() * axis.z());
        return new Quaterniond(
            axis.x() * invLen * sinHalf,
            axis.y() * invLen * sinHalf,
            axis.z() * invLen * sinHalf,
            cosHalf
        );
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public Quaterniond normalize() {
        double len = length();
        if (len == 0.0) {
            return IDENTITY;
        }
        double invLen = 1.0 / len;
        return new Quaterniond(x * invLen, y * invLen, z * invLen, w * invLen);
    }

    public Quaterniond mul(Quaterniond q) {
        return new Quaterniond(
            Math.fma(w, q.x, Math.fma(x, q.w, Math.fma(y, q.z, -z * q.y))),
            Math.fma(w, q.y, Math.fma(-x, q.z, Math.fma(y, q.w, z * q.x))),
            Math.fma(w, q.z, Math.fma(x, q.y, Math.fma(-y, q.x, z * q.w))),
            Math.fma(w, q.w, Math.fma(-x, q.x, Math.fma(-y, q.y, -z * q.z)))
        );
    }

    public Quaterniond slerp(Quaterniond target, double t) {
        double cosHalfTheta = w * target.w + x * target.x + y * target.y + z * target.z;
        double q1x = target.x, q1y = target.y, q1z = target.z, q1w = target.w;
        if (cosHalfTheta < 0.0) {
            cosHalfTheta = -cosHalfTheta;
            q1x = -q1x; q1y = -q1y; q1z = -q1z; q1w = -q1w;
        }
        if (Math.abs(cosHalfTheta) >= 1.0) {
            return this;
        }
        double sinHalfTheta = Math.sqrt(1.0 - cosHalfTheta * cosHalfTheta);
        if (Math.abs(sinHalfTheta) < 1e-3) {
            return new Quaterniond(
                Math.fma(1.0 - t, x, t * q1x),
                Math.fma(1.0 - t, y, t * q1y),
                Math.fma(1.0 - t, z, t * q1z),
                Math.fma(1.0 - t, w, t * q1w)
            ).normalize();
        }
        double halfTheta = Math.acos(cosHalfTheta);
        double ratioA = Math.sin((1.0 - t) * halfTheta) / sinHalfTheta;
        double ratioB = Math.sin(t * halfTheta) / sinHalfTheta;
        return new Quaterniond(
            x * ratioA + q1x * ratioB,
            y * ratioA + q1y * ratioB,
            z * ratioA + q1z * ratioB,
            w * ratioA + q1w * ratioB
        );
    }

    public Quaterniond squad(Quaterniond a, Quaterniond b, Quaterniond q2, double t) {
        Quaterniond s1 = this.slerp(q2, t);
        Quaterniond s2 = a.slerp(b, t);
        return s1.slerp(s2, 2.0 * t * (1.0 - t));
    }

    public Vector3D getEulerAnglesXYZ() {
        double m00 = Math.fma(2.0, -y*y - z*z, 1.0);
        double m10 = Math.fma(2.0, x*y + z*w, 0.0);
        double m20 = Math.fma(2.0, x*z - y*w, 0.0);
        double m21 = Math.fma(2.0, y*z + x*w, 0.0);
        double m22 = Math.fma(2.0, -x*x - y*y, 1.0);
        double m12 = Math.fma(2.0, y*z - x*w, 0.0);
        double m11 = Math.fma(2.0, -x*x - z*z, 1.0);
        
        double sy = Math.sqrt(m00 * m00 + m10 * m10);
        double xAngle, yAngle, zAngle;
        if (sy > 1e-6) {
            xAngle = Math.atan2(-m21, m22);
            yAngle = Math.atan2(-m20, sy);
            zAngle = Math.atan2(m10, m00);
        } else {
            xAngle = Math.atan2(-m12, m11);
            yAngle = Math.atan2(-m20, sy);
            zAngle = 0.0;
        }
        return new Vector3D(xAngle, yAngle, zAngle);
    }
}

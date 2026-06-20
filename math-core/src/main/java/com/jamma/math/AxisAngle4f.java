package com.jamma.math;

import com.jamma.math.matrix.Matrix3f;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record AxisAngle4f(float angle, float x, float y, float z) implements Serializable {

    private static final long serialVersionUID = 1L;

    public AxisAngle4f() {
        this(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public AxisAngle4f(float angle, Vector3f axis) {
        this(angle, axis.x(), axis.y(), axis.z());
    }

    public AxisAngle4f(float angle, float x, float y, float z) {
        float invLen = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);
        if (Float.isInfinite(invLen)) {
            this.angle = angle;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
        } else {
            this.angle = angle;
            this.x = x * invLen;
            this.y = y * invLen;
            this.z = z * invLen;
        }
    }

    public AxisAngle4f(Quaternionf q) {
        this(quaternionToAxisAngle(q));
    }

    public AxisAngle4f(Matrix3f m) {
        this(matrixToAxisAngle(m.m00(), m.m01(), m.m02(),
                               m.m10(), m.m11(), m.m12(),
                               m.m20(), m.m21(), m.m22()));
    }

    public AxisAngle4f(Matrix4f m) {
        this(matrixToAxisAngle(m.m00(), m.m01(), m.m02(),
                               m.m10(), m.m11(), m.m12(),
                               m.m20(), m.m21(), m.m22()));
    }

    public AxisAngle4f(AxisAngle4f other) {
        this(other.angle, other.x, other.y, other.z);
    }

    private AxisAngle4f(float[] data) {
        this(data[0], data[1], data[2], data[3]);
    }

    public AxisAngle4f normalize() {
        float invLen = 1.0f / (float) Math.sqrt(x * x + y * y + z * z);
        if (Float.isInfinite(invLen)) {
            return new AxisAngle4f(angle, 0.0f, 0.0f, 1.0f);
        }
        return new AxisAngle4f(angle, x * invLen, y * invLen, z * invLen);
    }

    public AxisAngle4f set(float angle, float x, float y, float z) {
        return new AxisAngle4f(angle, x, y, z);
    }

    public AxisAngle4f set(Vector3f axis) {
        return new AxisAngle4f(angle, axis);
    }

    public AxisAngle4f set(float angle, Vector3f axis) {
        return new AxisAngle4f(angle, axis);
    }

    public static AxisAngle4f from(Quaternionf q) {
        return new AxisAngle4f(q);
    }

    public static AxisAngle4f from(Matrix3f m) {
        return new AxisAngle4f(m);
    }

    public static AxisAngle4f from(Matrix4f m) {
        return new AxisAngle4f(m);
    }

    public Quaternionf toQuaternion() {
        float halfAngle = angle * 0.5f;
        float sinHalf = (float) Math.sin(halfAngle);
        return new Quaternionf(x * sinHalf, y * sinHalf, z * sinHalf, (float) Math.cos(halfAngle));
    }

    public Matrix3f toMatrix3() {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float t = 1.0f - c;
        return new Matrix3f(new float[] {
            t * x * x + c, t * x * y + s * z, t * x * z - s * y,
            t * x * y - s * z, t * y * y + c, t * y * z + s * x,
            t * x * z + s * y, t * y * z - s * x, t * z * z + c
        });
    }

    public Matrix4f toMatrix4() {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float t = 1.0f - c;
        return new Matrix4f(
            t * x * x + c, t * x * y + s * z, t * x * z - s * y, 0.0f,
            t * x * y - s * z, t * y * y + c, t * y * z + s * x, 0.0f,
            t * x * z + s * y, t * y * z - s * x, t * z * z + c, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        );
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, angle);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, y);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 12, z);
    }

    public static AxisAngle4f read(MemorySegment segment, long offset) {
        return new AxisAngle4f(
            segment.get(ValueLayout.JAVA_FLOAT, offset),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 4),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 8),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 12)
        );
    }

    private static float[] quaternionToAxisAngle(Quaternionf q) {
        float qw = q.w();
        float qx = q.x();
        float qy = q.y();
        float qz = q.z();
        if (qw > 1.0f) {
            float invLen = 1.0f / (float) Math.sqrt(Math.fma(qx, qx, Math.fma(qy, qy, Math.fma(qz, qz, qw * qw))));
            qw *= invLen;
            qx *= invLen;
            qy *= invLen;
            qz *= invLen;
        }
        float clamped = Math.min(1.0f, Math.max(-1.0f, qw));
        float angle = 2.0f * (float) Math.acos(clamped);
        float s = (float) Math.sqrt(1.0f - qw * qw);
        if (s < 1e-6f) {
            return new float[]{angle, qx, qy, qz};
        }
        return new float[]{angle, qx / s, qy / s, qz / s};
    }

    private static float[] matrixToAxisAngle(float m00, float m01, float m02,
                                              float m10, float m11, float m12,
                                              float m20, float m21, float m22) {
        float trace = m00 + m11 + m22;
        float cosVal = Math.fma(0.5f, trace, -0.5f);
        cosVal = Math.min(1.0f, Math.max(-1.0f, cosVal));
        float angle = (float) Math.acos(cosVal);
        float s = (float) Math.sin(angle);
        float x, y, z;
        if (s < 1e-6f) {
            if (angle < 1e-6f) {
                x = 0.0f;
                y = 0.0f;
                z = 1.0f;
            } else {
                float inv = 1.0f / (1.0f - cosVal);
                x = (float) Math.sqrt(Math.max(0.0f, (m00 - cosVal) * inv));
                y = (float) Math.sqrt(Math.max(0.0f, (m11 - cosVal) * inv));
                z = (float) Math.sqrt(Math.max(0.0f, (m22 - cosVal) * inv));
                if (m21 - m12 < 0) x = -x;
                if (m02 - m20 < 0) y = -y;
                if (m10 - m01 < 0) z = -z;
            }
        } else {
            float inv = 0.5f / s;
            x = (m21 - m12) * inv;
            y = (m02 - m20) * inv;
            z = (m10 - m01) * inv;
        }
        return new float[]{angle, x, y, z};
    }
}

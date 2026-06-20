package com.jamma.math;

import com.jamma.math.matrix.Matrix3d;
import com.jamma.math.matrix.Matrix4d;
import com.jamma.math.quaternion.Quaterniond;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public record AxisAngle4d(double angle, double x, double y, double z) implements Serializable {

    private static final long serialVersionUID = 1L;

    public AxisAngle4d() {
        this(0.0, 0.0, 0.0, 1.0);
    }

    public AxisAngle4d(double angle, Vector3D axis) {
        this(angle, axis.x(), axis.y(), axis.z());
    }

    public AxisAngle4d(double angle, double x, double y, double z) {
        double invLen = 1.0 / Math.sqrt(x * x + y * y + z * z);
        if (Double.isInfinite(invLen)) {
            this.angle = angle;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 1.0;
        } else {
            this.angle = angle;
            this.x = x * invLen;
            this.y = y * invLen;
            this.z = z * invLen;
        }
    }

    public AxisAngle4d(Quaterniond q) {
        this(quaternionToAxisAngle(q));
    }

    public AxisAngle4d(Matrix3d m) {
        this(matrixToAxisAngle(m.m00(), m.m01(), m.m02(),
                               m.m10(), m.m11(), m.m12(),
                               m.m20(), m.m21(), m.m22()));
    }

    public AxisAngle4d(Matrix4d m) {
        this(matrixToAxisAngle(m.m00(), m.m01(), m.m02(),
                               m.m10(), m.m11(), m.m12(),
                               m.m20(), m.m21(), m.m22()));
    }

    public AxisAngle4d(AxisAngle4d other) {
        this(other.angle, other.x, other.y, other.z);
    }

    private AxisAngle4d(double[] data) {
        this(data[0], data[1], data[2], data[3]);
    }

    public AxisAngle4d normalize() {
        double invLen = 1.0 / Math.sqrt(x * x + y * y + z * z);
        if (Double.isInfinite(invLen)) {
            return new AxisAngle4d(angle, 0.0, 0.0, 1.0);
        }
        return new AxisAngle4d(angle, x * invLen, y * invLen, z * invLen);
    }

    public AxisAngle4d set(double angle, double x, double y, double z) {
        return new AxisAngle4d(angle, x, y, z);
    }

    public AxisAngle4d set(Vector3D axis) {
        return new AxisAngle4d(angle, axis);
    }

    public AxisAngle4d set(double angle, Vector3D axis) {
        return new AxisAngle4d(angle, axis);
    }

    public static AxisAngle4d from(Quaterniond q) {
        return new AxisAngle4d(q);
    }

    public static AxisAngle4d from(Matrix3d m) {
        return new AxisAngle4d(m);
    }

    public static AxisAngle4d from(Matrix4d m) {
        return new AxisAngle4d(m);
    }

    public Quaterniond toQuaternion() {
        double halfAngle = angle * 0.5;
        double sinHalf = Math.sin(halfAngle);
        return new Quaterniond(x * sinHalf, y * sinHalf, z * sinHalf, Math.cos(halfAngle));
    }

    public Matrix3d toMatrix3() {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double t = 1.0 - c;
        return new Matrix3d(new double[] {
            t * x * x + c, t * x * y + s * z, t * x * z - s * y,
            t * x * y - s * z, t * y * y + c, t * y * z + s * x,
            t * x * z + s * y, t * y * z - s * x, t * z * z + c
        });
    }

    public Matrix4d toMatrix4() {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double t = 1.0 - c;
        return new Matrix4d(
            t * x * x + c, t * x * y + s * z, t * x * z - s * y, 0.0,
            t * x * y - s * z, t * y * y + c, t * y * z + s * x, 0.0,
            t * x * z + s * y, t * y * z - s * x, t * z * z + c, 0.0,
            0.0, 0.0, 0.0, 1.0
        );
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_DOUBLE, offset, angle);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 8, x);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 16, y);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 24, z);
    }

    public static AxisAngle4d read(MemorySegment segment, long offset) {
        return new AxisAngle4d(
            segment.get(ValueLayout.JAVA_DOUBLE, offset),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 8),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 16),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 24)
        );
    }

    private static double[] quaternionToAxisAngle(Quaterniond q) {
        double qw = q.w();
        double qx = q.x();
        double qy = q.y();
        double qz = q.z();
        if (qw > 1.0) {
            double invLen = 1.0 / Math.sqrt(Math.fma(qx, qx, Math.fma(qy, qy, Math.fma(qz, qz, qw * qw))));
            qw *= invLen;
            qx *= invLen;
            qy *= invLen;
            qz *= invLen;
        }
        double clamped = Math.min(1.0, Math.max(-1.0, qw));
        double angle = 2.0 * Math.acos(clamped);
        double s = Math.sqrt(1.0 - qw * qw);
        if (s < 1e-6) {
            return new double[]{angle, qx, qy, qz};
        }
        return new double[]{angle, qx / s, qy / s, qz / s};
    }

    private static double[] matrixToAxisAngle(double m00, double m01, double m02,
                                               double m10, double m11, double m12,
                                               double m20, double m21, double m22) {
        double trace = m00 + m11 + m22;
        double cosVal = Math.fma(0.5, trace, -0.5);
        cosVal = Math.min(1.0, Math.max(-1.0, cosVal));
        double angle = Math.acos(cosVal);
        double s = Math.sin(angle);
        double x, y, z;
        if (s < 1e-6) {
            if (angle < 1e-6) {
                x = 0.0;
                y = 0.0;
                z = 1.0;
            } else {
                double inv = 1.0 / (1.0 - cosVal);
                x = Math.sqrt(Math.max(0.0, (m00 - cosVal) * inv));
                y = Math.sqrt(Math.max(0.0, (m11 - cosVal) * inv));
                z = Math.sqrt(Math.max(0.0, (m22 - cosVal) * inv));
                if (m21 - m12 < 0) x = -x;
                if (m02 - m20 < 0) y = -y;
                if (m10 - m01 < 0) z = -z;
            }
        } else {
            double inv = 0.5 / s;
            x = (m21 - m12) * inv;
            y = (m02 - m20) * inv;
            z = (m10 - m01) * inv;
        }
        return new double[]{angle, x, y, z};
    }
}

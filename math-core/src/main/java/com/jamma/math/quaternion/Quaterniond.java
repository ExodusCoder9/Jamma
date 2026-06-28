package com.jamma.math.quaternion;

import com.jamma.math.AxisAngle4d;
import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix3d;
import com.jamma.math.matrix.Matrix4d;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.DoubleBuffer;

public record Quaterniond(double x, double y, double z, double w) implements Serializable {

    @Serial
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

    public static Quaterniond fromEulerAnglesXYZ(double xAngle, double yAngle, double zAngle) {
        double sx = Math.sin(xAngle * 0.5);
        double cx = Math.cos(xAngle * 0.5);
        double sy = Math.sin(yAngle * 0.5);
        double cy = Math.cos(yAngle * 0.5);
        double sz = Math.sin(zAngle * 0.5);
        double cz = Math.cos(zAngle * 0.5);
        return new Quaterniond(
            Math.fma(sx, Math.fma(cy, cz, sy * sz), cx * Math.fma(sy, cz, -cy * sz)),
            Math.fma(cx, Math.fma(sy, cz, cy * sz), sx * Math.fma(-cy, cz, sy * sz)),
            Math.fma(cx, Math.fma(cy, sz, -sy * cz), sx * Math.fma(sy, sz, cy * cz)),
            Math.fma(cx, Math.fma(cy, cz, -sy * sz), sx * Math.fma(-sy, cz, -cy * sz))
        );
    }

    public static Quaterniond rotateTo(Vector3d from, Vector3d to) {
        double d = from.dot(to);
        double x, y, z, w;
        if (d < -1.0 + 1e-6) {
            Vector3d axis = from.cross(new Vector3d(1, 0, 0));
            if (axis.lengthSquared() < 1e-6) {
                axis = from.cross(new Vector3d(0, 1, 0));
            }
            axis = axis.normalize();
            x = axis.x(); y = axis.y(); z = axis.z(); w = 0.0;
        } else {
            double s = Math.sqrt((1.0 + d) * 2.0);
            double invS = 1.0 / s;
            Vector3d c = from.cross(to);
            x = c.x() * invS;
            y = c.y() * invS;
            z = c.z() * invS;
            w = s * 0.5;
        }
        return new Quaterniond(x, y, z, w).normalize();
    }

    public static Quaterniond fromAxisAngle(Vector3d axis, double angle) {
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

    public Quaterniond conjugate() {
        return new Quaterniond(-x, -y, -z, w);
    }

    public Quaterniond invert() {
        double invLenSq = 1.0 / lengthSquared();
        return new Quaterniond(-x * invLenSq, -y * invLenSq, -z * invLenSq, w * invLenSq);
    }

    public Quaterniond difference(Quaterniond q) {
        return invert().mul(q);
    }

    public double dot(Quaterniond q) {
        return Math.fma(x, q.x, Math.fma(y, q.y, Math.fma(z, q.z, w * q.w)));
    }

    public double angle() {
        return 2.0 * Math.acos(w);
    }

    public Quaterniond mul(Quaterniond q) {
        return new Quaterniond(
            Math.fma(w, q.x, Math.fma(x, q.w, Math.fma(y, q.z, -z * q.y))),
            Math.fma(w, q.y, Math.fma(-x, q.z, Math.fma(y, q.w, z * q.x))),
            Math.fma(w, q.z, Math.fma(x, q.y, Math.fma(-y, q.x, z * q.w))),
            Math.fma(w, q.w, Math.fma(-x, q.x, Math.fma(-y, q.y, -z * q.z)))
        );
    }

    public Quaterniond nlerp(Quaterniond target, double t) {
        return new Quaterniond(
            Math.fma(1.0 - t, x, t * target.x),
            Math.fma(1.0 - t, y, t * target.y),
            Math.fma(1.0 - t, z, t * target.z),
            Math.fma(1.0 - t, w, t * target.w)
        ).normalize();
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

    public Matrix4d toMatrix() {
        double xx = x * x, yy = y * y, zz = z * z;
        double xy = x * y, xz = x * z, xw = x * w;
        double yz = y * z, yw = y * w, zw = z * w;
        return new Matrix4d(
            Math.fma(-2.0, yy + zz, 1.0), Math.fma(2.0, xy + zw, 0.0), Math.fma(2.0, xz - yw, 0.0), 0.0,
            Math.fma(2.0, xy - zw, 0.0), Math.fma(-2.0, xx + zz, 1.0), Math.fma(2.0, yz + xw, 0.0), 0.0,
            Math.fma(2.0, xz + yw, 0.0), Math.fma(2.0, yz - xw, 0.0), Math.fma(-2.0, xx + yy, 1.0), 0.0,
            0.0, 0.0, 0.0, 1.0
        );
    }

    public Matrix3d toMatrix3() {
        double xx = x * x, yy = y * y, zz = z * z;
        double xy = x * y, xz = x * z, xw = x * w;
        double yz = y * z, yw = y * w, zw = z * w;
        return new Matrix3d(new double[] {
            Math.fma(-2.0, yy + zz, 1.0), Math.fma(2.0, xy + zw, 0.0), Math.fma(2.0, xz - yw, 0.0),
            Math.fma(2.0, xy - zw, 0.0), Math.fma(-2.0, xx + zz, 1.0), Math.fma(2.0, yz + xw, 0.0),
            Math.fma(2.0, xz + yw, 0.0), Math.fma(2.0, yz - xw, 0.0), Math.fma(-2.0, xx + yy, 1.0)
        });
    }

    public Vector3d transform(Vector3d v) {
        double xx = x * x, yy = y * y, zz = z * z;
        double xy = x * y, xz = x * z, xw = x * w;
        double yz = y * z, yw = y * w, zw = z * w;
        double vx = v.x(), vy = v.y(), vz = v.z();
        return new Vector3d(
            Math.fma(1.0 - 2.0 * (yy + zz), vx, Math.fma(2.0 * (xy - zw), vy, 2.0 * (xz + yw) * vz)),
            Math.fma(2.0 * (xy + zw), vx, Math.fma(1.0 - 2.0 * (xx + zz), vy, 2.0 * (yz - xw) * vz)),
            Math.fma(2.0 * (xz - yw), vx, Math.fma(2.0 * (yz + xw), vy, (1.0 - 2.0 * (xx + yy)) * vz))
        );
    }

    public Vector3d positiveX() {
        double xz = x * z, yw = y * w;
        return new Vector3d(Math.fma(-2.0, y * y + z * z, 1.0), Math.fma(2.0, x * y + z * w, 0.0), Math.fma(2.0, xz - yw, 0.0));
    }

    public Vector3d positiveY() {
        return new Vector3d(Math.fma(2.0, x * y - z * w, 0.0), Math.fma(-2.0, x * x + z * z, 1.0), Math.fma(2.0, y * z + x * w, 0.0));
    }

    public Vector3d positiveZ() {
        return new Vector3d(Math.fma(2.0, x * z + y * w, 0.0), Math.fma(2.0, y * z - x * w, 0.0), Math.fma(-2.0, x * x + y * y, 1.0));
    }

    public Vector3d getEulerAnglesXYZ() {
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
        return new Vector3d(xAngle, yAngle, zAngle);
    }

    public Quaterniond() {
        this(0.0, 0.0, 0.0, 1.0);
    }

    public Quaterniond(Quaterniond q) {
        this(q.x, q.y, q.z, q.w);
    }

    public Quaterniond(double[] values) {
        this(values[0], values[1], values[2], values[3]);
    }

    public Quaterniond(Vector3d axis, double angle) {
        this(fromAxisAngle(axis, angle));
    }

    public Quaterniond(AxisAngle4d axisAngle) {
        this(axisAngle.x(), axisAngle.y(), axisAngle.z(), axisAngle.angle());
    }

    public Quaterniond(Matrix3d m) {
        this(fromMatrix3(m));
    }

    public Quaterniond(Matrix4d m) {
        this(fromMatrix3(new Matrix3d(new double[] {
            m.m00(), m.m01(), m.m02(),
            m.m10(), m.m11(), m.m12(),
            m.m20(), m.m21(), m.m22()
        })));
    }

    public Quaterniond add(Quaterniond q) {
        return new Quaterniond(x + q.x, y + q.y, z + q.z, w + q.w);
    }

    public Quaterniond sub(Quaterniond q) {
        return new Quaterniond(x - q.x, y - q.y, z - q.z, w - q.w);
    }

    public Quaterniond mul(double scalar) {
        return new Quaterniond(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public Quaterniond div(Quaterniond q) {
        return mul(q.invert());
    }

    public Quaterniond premul(Quaterniond q) {
        return q.mul(this);
    }

    public Quaterniond negate() {
        return new Quaterniond(-x, -y, -z, -w);
    }

    public double angle(Quaterniond q) {
        double cosHalfAngle = Math.abs(dot(q)) / (length() * q.length());
        return 2.0 * Math.acos(Math.min(1.0, cosHalfAngle));
    }

    public Quaterniond log() {
        double vLen = Math.sqrt(x * x + y * y + z * z);
        double len = length();
        if (vLen < 1e-6) {
            return new Quaterniond(0.0, 0.0, 0.0, Math.log(len));
        }
        double a = Math.acos(Math.min(1.0, Math.max(-1.0, w / len)));
        double inv = a / vLen;
        return new Quaterniond(x * inv, y * inv, z * inv, Math.log(len));
    }

    public Quaterniond exp() {
        double vLen = Math.sqrt(x * x + y * y + z * z);
        double e = Math.exp(w);
        if (vLen < 1e-6) {
            return new Quaterniond(0.0, 0.0, 0.0, e);
        }
        double s = e * Math.sin(vLen) / vLen;
        return new Quaterniond(x * s, y * s, z * s, e * Math.cos(vLen));
    }

    public Quaterniond pow(double exponent) {
        return log().mul(exponent).exp();
    }

    public Quaterniond pow(Quaterniond exponent) {
        return exponent.mul(log()).exp();
    }

    public Quaterniond sqrt() {
        return pow(0.5);
    }

    public Quaterniond lerp(Quaterniond target, double t) {
        return new Quaterniond(
            Math.fma(1.0 - t, x, t * target.x),
            Math.fma(1.0 - t, y, t * target.y),
            Math.fma(1.0 - t, z, t * target.z),
            Math.fma(1.0 - t, w, t * target.w)
        );
    }

    public Vector3d transform(double x, double y, double z) {
        return transform(new Vector3d(x, y, z));
    }

    public Vector3d positiveX(Vector3d dest) {
        return positiveX();
    }

    public Vector3d positiveY(Vector3d dest) {
        return positiveY();
    }

    public Vector3d positiveZ(Vector3d dest) {
        return positiveZ();
    }

    public static Quaterniond fromAxisAngle(double axisX, double axisY, double axisZ, double angle) {
        return fromAxisAngle(new Vector3d(axisX, axisY, axisZ), angle);
    }

    public static Quaterniond fromAxisAngleDeg(Vector3d axis, double angleDeg) {
        return fromAxisAngle(axis, angleDeg * Math.PI / 180.0);
    }

    public static Quaterniond fromAxisAngleDeg(double axisX, double axisY, double axisZ, double angleDeg) {
        return fromAxisAngleDeg(new Vector3d(axisX, axisY, axisZ), angleDeg);
    }

    public static Quaterniond fromEulerAnglesZYX(double angleZ, double angleY, double angleX) {
        double sx = Math.sin(angleX * 0.5);
        double cx = Math.cos(angleX * 0.5);
        double sy = Math.sin(angleY * 0.5);
        double cy = Math.cos(angleY * 0.5);
        double sz = Math.sin(angleZ * 0.5);
        double cz = Math.cos(angleZ * 0.5);
        return new Quaterniond(
            Math.fma(sx, cy * cz, cx * sy * sz),
            Math.fma(cx, sy * cz, -sx * cy * sz),
            Math.fma(cx, cy * sz, sx * sy * cz),
            Math.fma(cx, cy * cz, -sx * sy * sz)
        );
    }

    public static Quaterniond fromEulerAnglesYXZ(double angleY, double angleX, double angleZ) {
        double sx = Math.sin(angleX * 0.5);
        double cx = Math.cos(angleX * 0.5);
        double sy = Math.sin(angleY * 0.5);
        double cy = Math.cos(angleY * 0.5);
        double sz = Math.sin(angleZ * 0.5);
        double cz = Math.cos(angleZ * 0.5);
        return new Quaterniond(
            Math.fma(cz, sx * cy, sz * cx * sy),
            Math.fma(cz, cx * sy, sz * cx * cy),
            -sx * sy * sz,
            Math.fma(cz, cx * cy, -sz * cx * sy)
        );
    }

    public static Quaterniond fromAngleAxis(double angle, Vector3d axis) {
        return fromAxisAngle(axis, angle);
    }

    public static Quaterniond rotationTo(Vector3d from, Vector3d to) {
        return rotateTo(from, to);
    }

    public static Quaterniond lookAt(Vector3d dir, Vector3d up) {
        Vector3d forward = dir.normalize();
        Vector3d right = up.normalize().cross(forward).normalize();
        Vector3d newUp = forward.cross(right);
        Matrix3d m = new Matrix3d();
        m.m00 = right.x(); m.m01 = newUp.x(); m.m02 = forward.x();
        m.m10 = right.y(); m.m11 = newUp.y(); m.m12 = forward.y();
        m.m20 = right.z(); m.m21 = newUp.z(); m.m22 = forward.z();
        return new Quaterniond(m);
    }

    public static Quaterniond lookAt(double dirX, double dirY, double dirZ, double upX, double upY, double upZ) {
        return lookAt(new Vector3d(dirX, dirY, dirZ), new Vector3d(upX, upY, upZ));
    }

    public AxisAngle4d toAxisAngle() {
        return new AxisAngle4d(this);
    }

    public double[] get(double[] dest) {
        dest[0] = x;
        dest[1] = y;
        dest[2] = z;
        dest[3] = w;
        return dest;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public boolean equals(Quaterniond other, double delta) {
        return Math.abs(x - other.x) <= delta &&
               Math.abs(y - other.y) <= delta &&
               Math.abs(z - other.z) <= delta &&
               Math.abs(w - other.w) <= delta;
    }

    public boolean isFinite() {
        return Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z) && Double.isFinite(w);
    }

    public static Quaterniond spline(Quaterniond q0, Quaterniond q1, Quaterniond q2, Quaterniond q3, double t) {
        return squad(q0, q1, q2, q3, t);
    }

    public static Quaterniond squad(Quaterniond q0, Quaterniond q1, Quaterniond q2, Quaterniond q3, double t) {
        return q0.squad(q1, q2, q3, t);
    }

    public static Quaterniond squadInner(Quaterniond q0, Quaterniond q1, Quaterniond q2) {
        Quaterniond inv = q1.invert();
        return q1.mul(inv.mul(q2).log().add(inv.mul(q0).log()).mul(-0.25).exp());
    }

    public static Quaterniond nlerp(Quaterniond[] path, double t) {
        int len = path.length;
        if (len == 0) return IDENTITY;
        if (len == 1) return path[0];
        double step = 1.0 / (len - 1);
        int index = (int) Math.floor(t / step);
        double local = (t - index * step) / step;
        index = Math.min(index, len - 2);
        return path[index].nlerp(path[index + 1], local);
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_DOUBLE, offset, x);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 8, y);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 16, z);
        segment.set(ValueLayout.JAVA_DOUBLE, offset + 24, w);
    }

    public static Quaterniond read(MemorySegment segment, long offset) {
        return new Quaterniond(
            segment.get(ValueLayout.JAVA_DOUBLE, offset),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 8),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 16),
            segment.get(ValueLayout.JAVA_DOUBLE, offset + 24)
        );
    }

    private static Quaterniond fromMatrix3(Matrix3d m) {
        double m00 = m.m00(), m01 = m.m01(), m02 = m.m02();
        double m10 = m.m10(), m11 = m.m11(), m12 = m.m12();
        double m20 = m.m20(), m21 = m.m21(), m22 = m.m22();
        double t = m00 + m11 + m22;
        double x, y, z, w;
        if (t > 0.0) {
            double s = Math.sqrt(t + 1.0) * 2.0;
            w = s * 0.25;
            x = (m21 - m12) / s;
            y = (m02 - m20) / s;
            z = (m10 - m01) / s;
        } else if (m00 > m11 && m00 > m22) {
            double s = Math.sqrt(1.0 + m00 - m11 - m22) * 2.0;
            w = (m21 - m12) / s;
            x = s * 0.25;
            y = (m01 + m10) / s;
            z = (m02 + m20) / s;
        } else if (m11 > m22) {
            double s = Math.sqrt(1.0 + m11 - m00 - m22) * 2.0;
            w = (m02 - m20) / s;
            x = (m01 + m10) / s;
            y = s * 0.25;
            z = (m12 + m21) / s;
        } else {
            double s = Math.sqrt(1.0 + m22 - m00 - m11) * 2.0;
            w = (m10 - m01) / s;
            x = (m02 + m20) / s;
            y = (m12 + m21) / s;
            z = s * 0.25;
        }
        return new Quaterniond(x, y, z, w);
    }
}

package com.jamma.math.quaternion;

import com.jamma.math.AxisAngle4f;
import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix3f;
import com.jamma.math.matrix.Matrix4f;

import java.io.Serial;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

/**
 * A unit quaternion stored as an immutable record.
 * <p>
 * Represents a rotation in 3D space. For a rotation of angle {@code theta} around
 * axis {@code (ax, ay, az)}, the quaternion is
 * {@code (ax*sin(theta/2), ay*sin(theta/2), az*sin(theta/2), cos(theta/2))}.
 * <p>
 * Formulas assume the quaternion is normalized. Constructors do <b>not</b>
 * normalize automatically — call {@link #normalize()} when needed.
 */
public record Quaternionf(float x, float y, float z, float w) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static Quaternionf fromMemorySegment(MemorySegment src, long byteOffset) {
        return new Quaternionf(
            src.get(ValueLayout.JAVA_FLOAT, byteOffset),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 4),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 8),
            src.get(ValueLayout.JAVA_FLOAT, byteOffset + 12)
        );
    }

    public void writeToMemorySegment(MemorySegment dest, long byteOffset) {
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset, x);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 4, y);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 8, z);
        dest.set(ValueLayout.JAVA_FLOAT, byteOffset + 12, w);
    }

    public static Quaternionf fromBuffer(FloatBuffer src) {
        return new Quaternionf(src.get(), src.get(), src.get(), src.get());
    }

    public static Quaternionf fromBuffer(int index, FloatBuffer src) {
        return new Quaternionf(src.get(index), src.get(index + 1), src.get(index + 2), src.get(index + 3));
    }

    public FloatBuffer writeToBuffer(FloatBuffer dest) {
        dest.put(x);
        dest.put(y);
        dest.put(z);
        dest.put(w);
        return dest;
    }

    public FloatBuffer writeToBuffer(int index, FloatBuffer dest) {
        dest.put(index, x);
        dest.put(index + 1, y);
        dest.put(index + 2, z);
        dest.put(index + 3, w);
        return dest;
    }

    public static final Quaternionf IDENTITY = new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);

    public static Quaternionf identity() {
        return IDENTITY;
    }

    public static Quaternionf fromEulerAnglesXYZ(float xAngle, float yAngle, float zAngle) {
        float sx = (float) Math.sin(xAngle * 0.5f);
        float cx = (float) Math.cos(xAngle * 0.5f);
        float sy = (float) Math.sin(yAngle * 0.5f);
        float cy = (float) Math.cos(yAngle * 0.5f);
        float sz = (float) Math.sin(zAngle * 0.5f);
        float cz = (float) Math.cos(zAngle * 0.5f);
        return new Quaternionf(
            Math.fma(sx, Math.fma(cy, cz, sy * sz), cx * Math.fma(sy, cz, -cy * sz)),
            Math.fma(cx, Math.fma(sy, cz, cy * sz), sx * Math.fma(-cy, cz, sy * sz)),
            Math.fma(cx, Math.fma(cy, sz, -sy * cz), sx * Math.fma(sy, sz, cy * cz)),
            Math.fma(cx, Math.fma(cy, cz, -sy * sz), sx * Math.fma(-sy, cz, -cy * sz))
        );
    }

    public static Quaternionf rotateTo(Vector3f from, Vector3f to) {
        float d = from.dot(to);
        float x, y, z, w;
        if (d < -1.0f + 1e-6f) {
            Vector3f axis = from.cross(new Vector3f(1, 0, 0));
            if (axis.lengthSquared() < 1e-6f) {
                axis = from.cross(new Vector3f(0, 1, 0));
            }
            axis = axis.normalize();
            x = axis.x(); y = axis.y(); z = axis.z(); w = 0.0f;
        } else {
            float s = (float) Math.sqrt((1.0f + d) * 2.0f);
            float invS = 1.0f / s;
            Vector3f c = from.cross(to);
            x = c.x() * invS;
            y = c.y() * invS;
            z = c.z() * invS;
            w = s * 0.5f;
        }
        return new Quaternionf(x, y, z, w).normalize();
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

    public Quaternionf conjugate() {
        return new Quaternionf(-x, -y, -z, w);
    }

    public Quaternionf invert() {
        float lenSq = lengthSquared();
        if (lenSq == 0.0f) {
            return IDENTITY;
        }
        float invLenSq = 1.0f / lenSq;
        return new Quaternionf(-x * invLenSq, -y * invLenSq, -z * invLenSq, w * invLenSq);
    }

    public Quaternionf difference(Quaternionf q) {
        float lenSq = lengthSquared();
        if (lenSq == 0.0f) {
            return IDENTITY;
        }
        float invLenSq = 1.0f / lenSq;
        float ix = -x * invLenSq, iy = -y * invLenSq, iz = -z * invLenSq, iw = w * invLenSq;
        return new Quaternionf(
            Math.fma(iw, q.x, Math.fma(ix, q.w, Math.fma(iy, q.z, -iz * q.y))),
            Math.fma(iw, q.y, Math.fma(-ix, q.z, Math.fma(iy, q.w, iz * q.x))),
            Math.fma(iw, q.z, Math.fma(ix, q.y, Math.fma(-iy, q.x, iz * q.w))),
            Math.fma(iw, q.w, Math.fma(-ix, q.x, Math.fma(-iy, q.y, -iz * q.z)))
        );
    }

    public float dot(Quaternionf q) {
        return Math.fma(x, q.x, Math.fma(y, q.y, Math.fma(z, q.z, w * q.w)));
    }

    public float angle() {
        if (lengthSquared() == 0.0f) {
            return 0.0f;
        }
        return 2.0f * (float) Math.acos(Math.clamp(w, -1.0f, 1.0f));
    }

    public Quaternionf mul(Quaternionf q) {
        return new Quaternionf(
            Math.fma(w, q.x, Math.fma(x, q.w, Math.fma(y, q.z, -z * q.y))),
            Math.fma(w, q.y, Math.fma(-x, q.z, Math.fma(y, q.w, z * q.x))),
            Math.fma(w, q.z, Math.fma(x, q.y, Math.fma(-y, q.x, z * q.w))),
            Math.fma(w, q.w, Math.fma(-x, q.x, Math.fma(-y, q.y, -z * q.z)))
        );
    }

    public Quaternionf nlerp(Quaternionf target, float t) {
        return new Quaternionf(
            Math.fma(1.0f - t, x, t * target.x),
            Math.fma(1.0f - t, y, t * target.y),
            Math.fma(1.0f - t, z, t * target.z),
            Math.fma(1.0f - t, w, t * target.w)
        ).normalize();
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

    public Matrix4f toMatrix() {
        float xx = x * x, yy = y * y, zz = z * z;
        float xy = x * y, xz = x * z, xw = x * w;
        float yz = y * z, yw = y * w, zw = z * w;
        return new Matrix4f(
            1.0f - 2.0f * (yy + zz), 2.0f * (xy + zw), 2.0f * (xz - yw), 0.0f,
            2.0f * (xy - zw), 1.0f - 2.0f * (xx + zz), 2.0f * (yz + xw), 0.0f,
            2.0f * (xz + yw), 2.0f * (yz - xw), 1.0f - 2.0f * (xx + yy), 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        );
    }

    public Matrix3f toMatrix3() {
        float xx = x * x, yy = y * y, zz = z * z;
        float xy = x * y, xz = x * z, xw = x * w;
        float yz = y * z, yw = y * w, zw = z * w;
        return new Matrix3f(new float[] {
            1.0f - 2.0f * (yy + zz), 2.0f * (xy + zw), 2.0f * (xz - yw),
            2.0f * (xy - zw), 1.0f - 2.0f * (xx + zz), 2.0f * (yz + xw),
            2.0f * (xz + yw), 2.0f * (yz - xw), 1.0f - 2.0f * (xx + yy)
        });
    }

    public Vector3f transform(Vector3f v) {
        float xx = x * x, yy = y * y, zz = z * z;
        float xy = x * y, xz = x * z, xw = x * w;
        float yz = y * z, yw = y * w, zw = z * w;
        float vx = v.x(), vy = v.y(), vz = v.z();
        return new Vector3f(
            Math.fma(1.0f - 2.0f * (yy + zz), vx, Math.fma(2.0f * (xy - zw), vy, 2.0f * (xz + yw) * vz)),
            Math.fma(2.0f * (xy + zw), vx, Math.fma(1.0f - 2.0f * (xx + zz), vy, 2.0f * (yz - xw) * vz)),
            Math.fma(2.0f * (xz - yw), vx, Math.fma(2.0f * (yz + xw), vy, (1.0f - 2.0f * (xx + yy)) * vz))
        );
    }

    public Vector3f positiveX() {
        return new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y + z * w), 2.0f * (x * z - y * w));
    }

    public Vector3f positiveY() {
        return new Vector3f(2.0f * (x * y - z * w), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z + x * w));
    }

    public Vector3f positiveZ() {
        return new Vector3f(2.0f * (x * z + y * w), 2.0f * (y * z - x * w), 1.0f - 2.0f * (x * x + y * y));
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

    public Quaternionf() {
        this(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public Quaternionf(Quaternionf q) {
        this(q.x, q.y, q.z, q.w);
    }

    public Quaternionf(float[] values) {
        this(values[0], values[1], values[2], values[3]);
    }

    public Quaternionf(Vector3f axis, float angle) {
        this(fromAxisAngle(axis, angle));
    }

    public Quaternionf(AxisAngle4f axisAngle) {
        this(fromAxisAngle(
            new Vector3f(axisAngle.x(), axisAngle.y(), axisAngle.z()),
            axisAngle.angle()
        ));
    }

    public Quaternionf(Matrix3f m) {
        this(fromMatrix3(m));
    }

    public Quaternionf(Matrix4f m) {
        this(fromMatrix3(new Matrix3f(new float[] {
            m.m00(), m.m01(), m.m02(),
            m.m10(), m.m11(), m.m12(),
            m.m20(), m.m21(), m.m22()
        })));
    }

    public Quaternionf add(Quaternionf q) {
        return new Quaternionf(x + q.x, y + q.y, z + q.z, w + q.w);
    }

    public Quaternionf sub(Quaternionf q) {
        return new Quaternionf(x - q.x, y - q.y, z - q.z, w - q.w);
    }

    public Quaternionf mul(float scalar) {
        return new Quaternionf(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public Quaternionf div(Quaternionf q) {
        return mul(q.invert());
    }

    public Quaternionf premul(Quaternionf q) {
        return q.mul(this);
    }

    public Quaternionf negate() {
        return new Quaternionf(-x, -y, -z, -w);
    }

    public float angle(Quaternionf q) {
        float lenSq = lengthSquared();
        float otherLenSq = q.lengthSquared();
        if (lenSq == 0.0f || otherLenSq == 0.0f) {
            return 0.0f;
        }
        float cosHalfAngle = Math.abs(dot(q)) / (float) Math.sqrt(lenSq * otherLenSq);
        return 2.0f * (float) Math.acos(Math.clamp(cosHalfAngle, -1.0f, 1.0f));
    }

    public Quaternionf log() {
        float vLen = (float) Math.sqrt(x * x + y * y + z * z);
        float len = length();
        if (vLen < 1e-6f) {
            return new Quaternionf(0.0f, 0.0f, 0.0f, (float) Math.log(len));
        }
        float a = (float) Math.acos(Math.min(1.0f, Math.max(-1.0f, w / len)));
        float inv = a / vLen;
        return new Quaternionf(x * inv, y * inv, z * inv, (float) Math.log(len));
    }

    public Quaternionf exp() {
        float vLen = (float) Math.sqrt(x * x + y * y + z * z);
        float e = (float) Math.exp(w);
        if (vLen < 1e-6f) {
            return new Quaternionf(0.0f, 0.0f, 0.0f, e);
        }
        float s = e * (float) Math.sin(vLen) / vLen;
        return new Quaternionf(x * s, y * s, z * s, e * (float) Math.cos(vLen));
    }

    public Quaternionf pow(float exponent) {
        return log().mul(exponent).exp();
    }

    public Quaternionf pow(Quaternionf exponent) {
        return exponent.mul(log()).exp();
    }

    public Quaternionf sqrt() {
        return pow(0.5f);
    }

    public Quaternionf lerp(Quaternionf target, float t) {
        return new Quaternionf(
            Math.fma(1.0f - t, x, t * target.x),
            Math.fma(1.0f - t, y, t * target.y),
            Math.fma(1.0f - t, z, t * target.z),
            Math.fma(1.0f - t, w, t * target.w)
        );
    }

    public Vector3f transform(float x, float y, float z) {
        return transform(new Vector3f(x, y, z));
    }

    public Vector3f positiveX(Vector3f dest) {
        float xz = x * z, yw = y * w;
        return new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y + z * w), 2.0f * (xz - yw));
    }

    public Vector3f positiveY(Vector3f dest) {
        return new Vector3f(2.0f * (x * y - z * w), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z + x * w));
    }

    public Vector3f positiveZ(Vector3f dest) {
        return new Vector3f(2.0f * (x * z + y * w), 2.0f * (y * z - x * w), 1.0f - 2.0f * (x * x + y * y));
    }

    public static Quaternionf fromAxisAngle(float axisX, float axisY, float axisZ, float angle) {
        return fromAxisAngle(new Vector3f(axisX, axisY, axisZ), angle);
    }

    public static Quaternionf fromAxisAngleDeg(Vector3f axis, float angleDeg) {
        return fromAxisAngle(axis, angleDeg * (float) Math.PI / 180.0f);
    }

    public static Quaternionf fromAxisAngleDeg(float axisX, float axisY, float axisZ, float angleDeg) {
        return fromAxisAngleDeg(new Vector3f(axisX, axisY, axisZ), angleDeg);
    }

    public static Quaternionf fromEulerAnglesZYX(float angleZ, float angleY, float angleX) {
        float sx = (float) Math.sin(angleX * 0.5f);
        float cx = (float) Math.cos(angleX * 0.5f);
        float sy = (float) Math.sin(angleY * 0.5f);
        float cy = (float) Math.cos(angleY * 0.5f);
        float sz = (float) Math.sin(angleZ * 0.5f);
        float cz = (float) Math.cos(angleZ * 0.5f);
        return new Quaternionf(
            Math.fma(sx, cy * cz, cx * sy * sz),
            Math.fma(cx, sy * cz, -sx * cy * sz),
            Math.fma(cx, cy * sz, sx * sy * cz),
            Math.fma(cx, cy * cz, -sx * sy * sz)
        );
    }

    public static Quaternionf fromEulerAnglesYXZ(float angleY, float angleX, float angleZ) {
        float sx = (float) Math.sin(angleX * 0.5f);
        float cx = (float) Math.cos(angleX * 0.5f);
        float sy = (float) Math.sin(angleY * 0.5f);
        float cy = (float) Math.cos(angleY * 0.5f);
        float sz = (float) Math.sin(angleZ * 0.5f);
        float cz = (float) Math.cos(angleZ * 0.5f);
        return new Quaternionf(
            Math.fma(cz, sx * cy, sz * cx * sy),
            Math.fma(cz, cx * sy, sz * cx * cy),
            -sx * sy * sz,
            Math.fma(cz, cx * cy, -sz * cx * sy)
        );
    }

    public static Quaternionf fromAngleAxis(float angle, Vector3f axis) {
        return fromAxisAngle(axis, angle);
    }

    public static Quaternionf rotationTo(Vector3f from, Vector3f to) {
        return rotateTo(from, to);
    }

    public static Quaternionf lookAt(Vector3f dir, Vector3f up) {
        Vector3f forward = dir.normalize();
        Vector3f right = up.normalize().cross(forward).normalize();
        Vector3f newUp = forward.cross(right);
        Matrix3f m = new Matrix3f();
        m.m00 = right.x(); m.m01 = newUp.x(); m.m02 = forward.x();
        m.m10 = right.y(); m.m11 = newUp.y(); m.m12 = forward.y();
        m.m20 = right.z(); m.m21 = newUp.z(); m.m22 = forward.z();
        return new Quaternionf(m);
    }

    public static Quaternionf lookAt(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        return lookAt(new Vector3f(dirX, dirY, dirZ), new Vector3f(upX, upY, upZ));
    }

    public AxisAngle4f toAxisAngle() {
        return new AxisAngle4f(this);
    }

    public float[] get(float[] dest) {
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

    public boolean equals(Quaternionf other, float delta) {
        return Math.abs(x - other.x) <= delta &&
               Math.abs(y - other.y) <= delta &&
               Math.abs(z - other.z) <= delta &&
               Math.abs(w - other.w) <= delta;
    }

    public boolean isFinite() {
        return Float.isFinite(x) && Float.isFinite(y) && Float.isFinite(z) && Float.isFinite(w);
    }

    public static Quaternionf spline(Quaternionf q0, Quaternionf q1, Quaternionf q2, Quaternionf q3, float t) {
        return squad(q0, q1, q2, q3, t);
    }

    public static Quaternionf squad(Quaternionf q0, Quaternionf q1, Quaternionf q2, Quaternionf q3, float t) {
        return q0.squad(q1, q2, q3, t);
    }

    public static Quaternionf squadInner(Quaternionf q0, Quaternionf q1, Quaternionf q2) {
        Quaternionf inv = q1.invert();
        return q1.mul(inv.mul(q2).log().add(inv.mul(q0).log()).mul(-0.25f).exp());
    }

    public static Quaternionf nlerp(Quaternionf[] path, float t) {
        int len = path.length;
        if (len == 0) return IDENTITY;
        if (len == 1) return path[0];
        float step = 1.0f / (len - 1);
        int index = (int) Math.floor(t / step);
        float local = (t - index * step) / step;
        index = Math.min(index, len - 2);
        return path[index].nlerp(path[index + 1], local);
    }

    public void write(MemorySegment segment, long offset) {
        segment.set(ValueLayout.JAVA_FLOAT, offset, x);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 4, y);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 8, z);
        segment.set(ValueLayout.JAVA_FLOAT, offset + 12, w);
    }

    public static Quaternionf read(MemorySegment segment, long offset) {
        return new Quaternionf(
            segment.get(ValueLayout.JAVA_FLOAT, offset),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 4),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 8),
            segment.get(ValueLayout.JAVA_FLOAT, offset + 12)
        );
    }

    private static Quaternionf fromMatrix3(Matrix3f m) {
        float m00 = m.m00(), m01 = m.m01(), m02 = m.m02();
        float m10 = m.m10(), m11 = m.m11(), m12 = m.m12();
        float m20 = m.m20(), m21 = m.m21(), m22 = m.m22();
        float t = m00 + m11 + m22;
        float x, y, z, w;
        if (t > 0.0f) {
            float s = (float) Math.sqrt(t + 1.0f) * 2.0f;
            w = s * 0.25f;
            x = (m21 - m12) / s;
            y = (m02 - m20) / s;
            z = (m10 - m01) / s;
        } else if (m00 > m11 && m00 > m22) {
            float s = (float) Math.sqrt(1.0f + m00 - m11 - m22) * 2.0f;
            w = (m21 - m12) / s;
            x = s * 0.25f;
            y = (m01 + m10) / s;
            z = (m02 + m20) / s;
        } else if (m11 > m22) {
            float s = (float) Math.sqrt(1.0f + m11 - m00 - m22) * 2.0f;
            w = (m02 - m20) / s;
            x = (m01 + m10) / s;
            y = s * 0.25f;
            z = (m12 + m21) / s;
        } else {
            float s = (float) Math.sqrt(1.0f + m22 - m00 - m11) * 2.0f;
            w = (m10 - m01) / s;
            x = (m02 + m20) / s;
            y = (m12 + m21) / s;
            z = s * 0.25f;
        }
        return new Quaternionf(x, y, z, w);
    }
}

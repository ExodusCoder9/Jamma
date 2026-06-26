package com.jamma.joml;

public class Quaterniond {

    public double x;
    public double y;
    public double z;
    public double w;

    public Quaterniond() {
        w = 1.0;
    }

    public Quaterniond(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaterniond(Quaterniond q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }

    public Quaterniond(com.jamma.math.Quaterniond jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
        this.z = jamma.z();
        this.w = jamma.w();
    }

    public com.jamma.math.Quaterniond toJamma() {
        return new com.jamma.math.Quaterniond(x, y, z, w);
    }

    public static Quaterniond fromJamma(com.jamma.math.Quaterniond jamma) {
        return new Quaterniond(jamma.x(), jamma.y(), jamma.z(), jamma.w());
    }

    public Quaterniond set(Quaterniond q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
        return this;
    }

    public Quaterniond set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaterniond normalize() {
        double inv = 1.0 / Math.sqrt(x * x + y * y + z * z + w * w);
        x *= inv;
        y *= inv;
        z *= inv;
        w *= inv;
        return this;
    }

    public Quaterniond conjugate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Quaterniond invert() {
        double normSq = x * x + y * y + z * z + w * w;
        double inv = 1.0 / normSq;
        x = -x * inv;
        y = -y * inv;
        z = -z * inv;
        w *= inv;
        return this;
    }

    public Quaterniond mul(Quaterniond q) {
        double rx = Math.fma(w, q.x, Math.fma(x, q.w, Math.fma(y, q.z, -z * q.y)));
        double ry = Math.fma(w, q.y, Math.fma(-x, q.z, Math.fma(y, q.w, z * q.x)));
        double rz = Math.fma(w, q.z, Math.fma(x, q.y, Math.fma(-y, q.x, z * q.w)));
        double rw = Math.fma(w, q.w, Math.fma(-x, q.x, Math.fma(-y, q.y, -z * q.z)));
        x = rx;
        y = ry;
        z = rz;
        w = rw;
        return this;
    }

    public Quaterniond premul(Quaterniond q) {
        double rx = Math.fma(q.w, x, Math.fma(q.x, w, Math.fma(q.y, z, -q.z * y)));
        double ry = Math.fma(q.w, y, Math.fma(-q.x, z, Math.fma(q.y, w, q.z * x)));
        double rz = Math.fma(q.w, z, Math.fma(q.x, y, Math.fma(-q.y, x, q.z * w)));
        double rw = Math.fma(q.w, w, Math.fma(-q.x, x, Math.fma(-q.y, y, -q.z * z)));
        x = rx;
        y = ry;
        z = rz;
        w = rw;
        return this;
    }

    public Vector3d transform(Vector3d v) {
        double qx = x, qy = y, qz = z, qw = w;
        double tx = 2.0 * (qy * v.z - qz * v.y);
        double ty = 2.0 * (qz * v.x - qx * v.z);
        double tz = 2.0 * (qx * v.y - qy * v.x);
        v.x += qw * tx + (qy * tz - qz * ty);
        v.y += qw * ty + (qz * tx - qx * tz);
        v.z += qw * tz + (qx * ty - qy * tx);
        return v;
    }

    public Quaterniond fromAxisAngle(Vector3d axis, double angle) {
        double half = angle * 0.5;
        double s = Math.sin(half);
        double len = axis.length();
        if (len == 0.0) return identity();
        double invLen = 1.0 / len;
        x = axis.x * invLen * s;
        y = axis.y * invLen * s;
        z = axis.z * invLen * s;
        w = Math.cos(half);
        return this;
    }

    public Quaterniond fromEulerAnglesXYZ(double xAngle, double yAngle, double zAngle) {
        double sx = Math.sin(xAngle * 0.5);
        double cx = Math.cos(xAngle * 0.5);
        double sy = Math.sin(yAngle * 0.5);
        double cy = Math.cos(yAngle * 0.5);
        double sz = Math.sin(zAngle * 0.5);
        double cz = Math.cos(zAngle * 0.5);
        x = Math.fma(sx, Math.fma(cy, cz, sy * sz), cx * Math.fma(sy, cz, -cy * sz));
        y = Math.fma(cx, Math.fma(sy, cz, cy * sz), sx * Math.fma(-cy, cz, sy * sz));
        z = Math.fma(cx, Math.fma(cy, sz, -sy * cz), sx * Math.fma(sy, sz, cy * cz));
        w = Math.fma(cx, Math.fma(cy, cz, -sy * sz), sx * Math.fma(-sy, cz, -cy * sz));
        return this;
    }

    public Quaterniond nlerp(Quaterniond target, double t) {
        double dot = x * target.x + y * target.y + z * target.z + w * target.w;
        double tx = x + t * (target.x - x);
        double ty = y + t * (target.y - y);
        double tz = z + t * (target.z - z);
        double tw = w + t * (target.w - w);
        if (dot < 0.0) {
            tx = x + t * (-target.x - x);
            ty = y + t * (-target.y - y);
            tz = z + t * (-target.z - z);
            tw = w + t * (-target.w - w);
        }
        double inv = 1.0 / Math.sqrt(tx * tx + ty * ty + tz * tz + tw * tw);
        x = tx * inv;
        y = ty * inv;
        z = tz * inv;
        w = tw * inv;
        return this;
    }

    public Quaterniond slerp(Quaterniond target, double t) {
        double cosom = x * target.x + y * target.y + z * target.z + w * target.w;
        double sx = target.x, sy = target.y, sz = target.z, sw = target.w;
        if (cosom < 0.0) {
            cosom = -cosom;
            sx = -sx;
            sy = -sy;
            sz = -sz;
            sw = -sw;
        }
        double scale0, scale1;
        if (1.0 - cosom > 1e-12) {
            double omega = Math.acos(cosom);
            double invSin = 1.0 / Math.sin(omega);
            scale0 = Math.sin((1.0 - t) * omega) * invSin;
            scale1 = Math.sin(t * omega) * invSin;
        } else {
            scale0 = 1.0 - t;
            scale1 = t;
        }
        x = scale0 * x + scale1 * sx;
        y = scale0 * y + scale1 * sy;
        z = scale0 * z + scale1 * sz;
        w = scale0 * w + scale1 * sw;
        return this;
    }

    public Quaterniond identity() {
        x = y = z = 0.0;
        w = 1.0;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public Quaterniond add(Quaterniond q) {
        x += q.x;
        y += q.y;
        z += q.z;
        w += q.w;
        return this;
    }

    public Quaterniond sub(Quaterniond q) {
        x -= q.x;
        y -= q.y;
        z -= q.z;
        w -= q.w;
        return this;
    }

    public Quaterniond mul(double scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        w *= scalar;
        return this;
    }

    public double dot(Quaterniond q) {
        return x * q.x + y * q.y + z * q.z + w * q.w;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z) ^ Double.hashCode(w);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quaterniond q)) return false;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(q.x)
            && Double.doubleToLongBits(y) == Double.doubleToLongBits(q.y)
            && Double.doubleToLongBits(z) == Double.doubleToLongBits(q.z)
            && Double.doubleToLongBits(w) == Double.doubleToLongBits(q.w);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}

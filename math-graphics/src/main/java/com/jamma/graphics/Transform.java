package com.jamma.graphics;

import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;

public record Transform(Vector3f position, Quaternionf rotation, Vector3f scale) {

    public static final Transform IDENTITY = new Transform(
        Vector3f.ZERO, new Quaternionf(0, 0, 0, 1), new Vector3f(1, 1, 1)
    );

    public Transform() {
        this(Vector3f.ZERO, new Quaternionf(0, 0, 0, 1), new Vector3f(1, 1, 1));
    }

    public Transform(Vector3f position) {
        this(position, new Quaternionf(0, 0, 0, 1), new Vector3f(1, 1, 1));
    }

    public Transform translate(float x, float y, float z) {
        return new Transform(
            new Vector3f(position.x() + x, position.y() + y, position.z() + z),
            rotation,
            scale
        );
    }

    public Transform translate(Vector3f v) {
        return translate(v.x(), v.y(), v.z());
    }

    public Transform rotate(Quaternionf q) {
        return new Transform(position, q.mul(rotation), scale);
    }

    public Transform scale(float s) {
        return new Transform(
            position,
            rotation,
            new Vector3f(scale.x() * s, scale.y() * s, scale.z() * s)
        );
    }

    public Matrix4f toMatrix() {
        var m = new Matrix4f().identity();
        m.translate(position.x(), position.y(), position.z());
        m.rotate(rotation);
        m.scale(scale.x(), scale.y(), scale.z());
        return m;
    }

    public static Transform fromMatrix(Matrix4f m) {
        Vector3f pos = m.getTranslation();

        float sx = new Vector3f(m.m00(), m.m01(), m.m02()).length();
        float sy = new Vector3f(m.m10(), m.m11(), m.m12()).length();
        float sz = new Vector3f(m.m20(), m.m21(), m.m22()).length();

        float sxs = sx > 0 ? 1 / sx : 0;
        float sys = sy > 0 ? 1 / sy : 0;
        float szs = sz > 0 ? 1 / sz : 0;

        float r00 = m.m00() * sxs;
        float r01 = m.m01() * sxs;
        float r02 = m.m02() * sxs;
        float r10 = m.m10() * sys;
        float r11 = m.m11() * sys;
        float r12 = m.m12() * sys;
        float r20 = m.m20() * szs;
        float r21 = m.m21() * szs;
        float r22 = m.m22() * szs;

        float trace = r00 + r11 + r22;
        float qw, qx, qy, qz;
        if (trace > 0) {
            float s = (float) Math.sqrt(trace + 1) * 2;
            qw = s * 0.25f;
            qx = (r21 - r12) / s;
            qy = (r02 - r20) / s;
            qz = (r10 - r01) / s;
        } else if (r00 > r11 && r00 > r22) {
            float s = (float) Math.sqrt(1 + r00 - r11 - r22) * 2;
            qw = (r21 - r12) / s;
            qx = s * 0.25f;
            qy = (r01 + r10) / s;
            qz = (r02 + r20) / s;
        } else if (r11 > r22) {
            float s = (float) Math.sqrt(1 + r11 - r00 - r22) * 2;
            qw = (r02 - r20) / s;
            qx = (r01 + r10) / s;
            qy = s * 0.25f;
            qz = (r12 + r21) / s;
        } else {
            float s = (float) Math.sqrt(1 + r22 - r00 - r11) * 2;
            qw = (r10 - r01) / s;
            qx = (r02 + r20) / s;
            qy = (r12 + r21) / s;
            qz = s * 0.25f;
        }

        return new Transform(pos, new Quaternionf(qx, qy, qz, qw).normalize(),
            new Vector3f(sx, sy, sz));
    }

    public Vector3f apply(Vector3f v) {
        return rotation.transform(v).mul(scale).add(position);
    }

    public Transform lerp(Transform other, float t) {
        return new Transform(
            new Vector3f(
                position.x() + (other.position.x() - position.x()) * t,
                position.y() + (other.position.y() - position.y()) * t,
                position.z() + (other.position.z() - position.z()) * t
            ),
            rotation.slerp(other.rotation, t),
            new Vector3f(
                scale.x() + (other.scale.x() - scale.x()) * t,
                scale.y() + (other.scale.y() - scale.y()) * t,
                scale.z() + (other.scale.z() - scale.z()) * t
            )
        );
    }

    public Transform compose(Transform parent) {
        return new Transform(
            parent.rotation.transform(position).mul(parent.scale).add(parent.position),
            parent.rotation.mul(rotation),
            new Vector3f(parent.scale.x() * scale.x(), parent.scale.y() * scale.y(), parent.scale.z() * scale.z())
        );
    }
}

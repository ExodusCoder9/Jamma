package com.jamma.joml;

/**
 * Static conversion utilities between JOML and Jamma types.
 * <p>
 * These methods require {@code org.joml} on the classpath. If you don't need
 * direct JOML ↔ Jamma conversion, use the adapter types in this package which
 * have their own {@code toJamma()} / {@code fromJamma()} methods.
 */
public final class JomlCompat {

    private JomlCompat() {
    }

    // ── JOML Vector3f ↔ Jamma Vector3f ──────────────────────────────────

    public static com.jamma.math.Vector3f toJamma(org.joml.Vector3f v) {
        return new com.jamma.math.Vector3f(v.x, v.y, v.z);
    }

    public static Vector3f toAdapter(org.joml.Vector3f v) {
        return new Vector3f(v.x, v.y, v.z);
    }

    public static org.joml.Vector3f toJoml(com.jamma.math.Vector3f v, org.joml.Vector3f dest) {
        return dest.set(v.x(), v.y(), v.z());
    }

    public static org.joml.Vector3f toJoml(Vector3f v, org.joml.Vector3f dest) {
        return dest.set(v.x, v.y, v.z);
    }

    // ── JOML Vector3d ↔ Jamma Vector3d ──────────────────────────────────

    public static com.jamma.math.Vector3d toJamma(org.joml.Vector3d v) {
        return new com.jamma.math.Vector3d(v.x, v.y, v.z);
    }

    public static Vector3d toAdapter(org.joml.Vector3d v) {
        return new Vector3d(v.x, v.y, v.z);
    }

    public static org.joml.Vector3d toJoml(com.jamma.math.Vector3d v, org.joml.Vector3d dest) {
        return dest.set(v.x(), v.y(), v.z());
    }

    // ── JOML Vector4f ↔ Jamma Vector4f ──────────────────────────────────

    public static com.jamma.math.Vector4f toJamma(org.joml.Vector4f v) {
        return new com.jamma.math.Vector4f(v.x, v.y, v.z, v.w);
    }

    public static Vector4f toAdapter(org.joml.Vector4f v) {
        return new Vector4f(v.x, v.y, v.z, v.w);
    }

    // ── JOML Vector4d ↔ Jamma Vector4d ──────────────────────────────────

    public static com.jamma.math.Vector4d toJamma(org.joml.Vector4d v) {
        return new com.jamma.math.Vector4d(v.x, v.y, v.z, v.w);
    }

    public static Vector4d toAdapter(org.joml.Vector4d v) {
        return new Vector4d(v.x, v.y, v.z, v.w);
    }

    // ── JOML Vector2f ↔ Jamma Vector2f ──────────────────────────────────

    public static com.jamma.math.Vector2f toJamma(org.joml.Vector2f v) {
        return new com.jamma.math.Vector2f(v.x, v.y);
    }

    public static Vector2f toAdapter(org.joml.Vector2f v) {
        return new Vector2f(v.x, v.y);
    }

    // ── JOML Vector2d ↔ Jamma Vector2d ──────────────────────────────────

    public static com.jamma.math.Vector2d toJamma(org.joml.Vector2d v) {
        return new com.jamma.math.Vector2d(v.x, v.y);
    }

    public static Vector2d toAdapter(org.joml.Vector2d v) {
        return new Vector2d(v.x, v.y);
    }

    // ── JOML Matrix4f ↔ Jamma Matrix4f ──────────────────────────────────

    public static com.jamma.math.matrix.Matrix4f toJamma(org.joml.Matrix4f m) {
        return new com.jamma.math.matrix.Matrix4f(
            m.m00(), m.m01(), m.m02(), m.m03(),
            m.m10(), m.m11(), m.m12(), m.m13(),
            m.m20(), m.m21(), m.m22(), m.m23(),
            m.m30(), m.m31(), m.m32(), m.m33()
        );
    }

    // ── JOML Matrix4d ↔ Jamma Matrix4d ──────────────────────────────────

    public static com.jamma.math.matrix.Matrix4d toJamma(org.joml.Matrix4d m) {
        return new com.jamma.math.matrix.Matrix4d(
            m.m00(), m.m01(), m.m02(), m.m03(),
            m.m10(), m.m11(), m.m12(), m.m13(),
            m.m20(), m.m21(), m.m22(), m.m23(),
            m.m30(), m.m31(), m.m32(), m.m33()
        );
    }

    // ── JOML Quaternionf ↔ Jamma Quaternionf ────────────────────────────

    public static com.jamma.math.quaternion.Quaternionf toJamma(org.joml.Quaternionf q) {
        return new com.jamma.math.quaternion.Quaternionf(q.x, q.y, q.z, q.w);
    }

    public static Quaternionf toAdapter(org.joml.Quaternionf q) {
        return new Quaternionf(q.x, q.y, q.z, q.w);
    }

    // ── JOML Quaterniond ↔ Jamma Quaterniond ────────────────────────────

    public static com.jamma.math.quaternion.Quaterniond toJamma(org.joml.Quaterniond q) {
        return new com.jamma.math.quaternion.Quaterniond(q.x, q.y, q.z, q.w);
    }

    public static Quaterniond toAdapter(org.joml.Quaterniond q) {
        return new Quaterniond(q.x, q.y, q.z, q.w);
    }
}

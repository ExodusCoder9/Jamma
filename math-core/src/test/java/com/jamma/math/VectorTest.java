package com.jamma.math;

import com.jamma.math.quaternion.Quaternionf;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
    static final float F = 1e-5f;
    static final double D = 1e-12;

    // ── Vector2f ──────────────────────────────────────────────────────

    @Test void v2fAdd() {
        assertEquals(new Vector2f(4, 6), new Vector2f(1, 2).add(new Vector2f(3, 4)));
        assertEquals(new Vector2f(4, 6), new Vector2f(1, 2).add(3, 4));
    }
    @Test void v2fSub() {
        assertEquals(new Vector2f(-2, -2), new Vector2f(1, 2).sub(new Vector2f(3, 4)));
        assertEquals(new Vector2f(-2, -2), new Vector2f(1, 2).sub(3, 4));
    }
    @Test void v2fMul() {
        assertEquals(new Vector2f(3, 8), new Vector2f(1, 2).mul(new Vector2f(3, 4)));
        assertEquals(new Vector2f(5, 10), new Vector2f(1, 2).mul(5));
        assertEquals(new Vector2f(3, 8), new Vector2f(1, 2).mul(3, 4));
    }
    @Test void v2fDiv() {
        assertEquals(new Vector2f(3, 2), new Vector2f(9, 8).div(new Vector2f(3, 4)));
        assertEquals(new Vector2f(2, 4), new Vector2f(10, 20).div(5));
        assertEquals(new Vector2f(3, 2), new Vector2f(9, 8).div(3, 4));
    }
    @Test void v2fScaleNegateAbs() {
        assertEquals(new Vector2f(3, 6), new Vector2f(1, 2).scale(3));
        assertEquals(new Vector2f(-1, -2), new Vector2f(1, 2).negate());
        assertEquals(new Vector2f(1, 2), new Vector2f(-1, -2).abs());
    }
    @Test void v2fDot() { assertEquals(11, new Vector2f(1, 2).dot(new Vector2f(3, 4)), F); }
    @Test void v2fLength() {
        assertEquals(5, new Vector2f(3, 4).length(), F);
        assertEquals(25, new Vector2f(3, 4).lengthSquared(), F);
    }
    @Test void v2fDistance() {
        assertEquals(5, new Vector2f(0, 0).distance(new Vector2f(3, 4)), F);
        assertEquals(5, new Vector2f(0, 0).distance(3, 4), F);
        assertEquals(25, new Vector2f(0, 0).distanceSquared(new Vector2f(3, 4)), F);
        assertEquals(25, new Vector2f(0, 0).distanceSquared(3, 4), F);
    }
    @Test void v2fNormalize() {
        Vector2f n = new Vector2f(3, 0).normalize();
        assertEquals(1, n.x(), F); assertEquals(0, n.y(), F);
        assertEquals(1, n.length(), F);
    }
    @Test void v2fNormalizeLength() {
        Vector2f n = new Vector2f(3, 0).normalize(5);
        assertEquals(5, n.x(), F); assertEquals(0, n.y(), F);
    }
    @Test void v2fAngle() {
        assertEquals(0, new Vector2f(1, 0).angle(new Vector2f(1, 0)), F);
        assertEquals((float) Math.PI / 2, new Vector2f(1, 0).angle(new Vector2f(0, 1)), F);
    }
    @Test void v2fAngleSigned() {
        assertEquals((float) Math.PI / 2, new Vector2f(1, 0).angleSigned(new Vector2f(0, 1)), F);
        assertEquals((float) -Math.PI / 2, new Vector2f(0, 1).angleSigned(new Vector2f(1, 0)), F);
    }
    @Test void v2fReflect() {
        Vector2f r = new Vector2f(1, -1).reflect(new Vector2f(0, 1));
        assertEquals(1, r.x(), F); assertEquals(1, r.y(), F);
    }
    @Test void v2fProject() {
        Vector2f p = new Vector2f(3, 4).project(new Vector2f(1, 0));
        assertEquals(3, p.x(), F); assertEquals(0, p.y(), F);
    }
    @Test void v2fReject() {
        Vector2f r = new Vector2f(3, 4).reject(new Vector2f(1, 0));
        assertEquals(0, r.x(), F); assertEquals(4, r.y(), F);
    }
    @Test void v2fLerp() {
        Vector2f l = new Vector2f(0, 0).lerp(new Vector2f(10, 20), 0.5f);
        assertEquals(5, l.x(), F); assertEquals(10, l.y(), F);
    }
    @Test void v2fPerpendicular() {
        Vector2f v = new Vector2f(3, 4);
        Vector2f p = v.perpendicular();
        assertEquals(0, v.dot(p), F);
        assertEquals(v.length(), p.length(), F);
        assertEquals(new Vector2f(-4, 3), p);
    }
    @Test void v2fRotate() {
        Vector2f r = new Vector2f(1, 0).rotate((float) Math.PI / 2);
        assertEquals(0, r.x(), F); assertEquals(1, r.y(), F);
    }
    @Test void v2fHalfway() {
        Vector2f h = new Vector2f(1, 0).halfway(new Vector2f(0, 1));
        assertEquals(1, h.length(), F);
        assertTrue(h.x() > 0); assertTrue(h.y() > 0);
    }
    @Test void v2fCeilFloorRound() {
        assertEquals(new Vector2f(2, -1), new Vector2f(1.2f, -1.2f).ceil());
        assertEquals(new Vector2f(1, -2), new Vector2f(1.2f, -1.2f).floor());
        assertEquals(new Vector2f(1, -1), new Vector2f(1.2f, -1.2f).round());
    }
    @Test void v2fMinMax() {
        assertEquals(new Vector2f(1, 2), new Vector2f(1, 5).min(new Vector2f(3, 2)));
        assertEquals(new Vector2f(3, 5), new Vector2f(1, 5).max(new Vector2f(3, 2)));
    }
    @Test void v2fFma() {
        assertEquals(new Vector2f(5, 7), new Vector2f(1, 2).fma(new Vector2f(3, 4), new Vector2f(2, -1)));
        assertEquals(new Vector2f(5, 11), new Vector2f(1, 2).fma(3, new Vector2f(2, 5)));
    }
    @Test void v2fIsPerpendicular() {
        assertTrue(new Vector2f(1, 0).isPerpendicular(new Vector2f(0, 1), F));
        assertFalse(new Vector2f(1, 0).isPerpendicular(new Vector2f(1, 0), F));
    }
    @Test void v2fComponent() {
        assertEquals(1, new Vector2f(3, 1).minComponent(), F);
        assertEquals(3, new Vector2f(3, 1).maxComponent(), F);
        assertEquals(0, new Vector2f(3, 1).maxComponentIndex());
        assertEquals(1, new Vector2f(3, 1).minComponentIndex());
    }
    @Test void v2fConvert() {
        assertEquals(new Vector3f(1, 2, 0), new Vector2f(1, 2).toVector3f());
        assertEquals(new Vector3f(1, 2, 3), new Vector2f(1, 2).toVector3f(3));
        assertEquals(new Vector4f(1, 2, 0, 0), new Vector2f(1, 2).toVector4f());
        assertEquals(new Vector4f(1, 2, 3, 4), new Vector2f(1, 2).toVector4f(3, 4));
        assertEquals(new Vector2d(1, 2), new Vector2f(1, 2).toVector2d());
    }
    @Test void v2fGet() { assertArrayEquals(new float[]{1, 2}, new Vector2f(1, 2).get(new float[2]), F); }
    @Test void v2fEqualsDelta() {
        assertTrue(new Vector2f(1, 2).equals(new Vector2f(1.001f, 2.001f), 0.01f));
        assertFalse(new Vector2f(1, 2).equals(new Vector2f(1.1f, 2), 0.01f));
    }

    // ── Vector3f ──────────────────────────────────────────────────────

    @Test void v3fAdd() {
        assertEquals(new Vector3f(4, 6, 8), new Vector3f(1, 2, 3).add(new Vector3f(3, 4, 5)));
        assertEquals(new Vector3f(4, 6, 8), new Vector3f(1, 2, 3).add(3, 4, 5));
    }
    @Test void v3fSub() {
        assertEquals(new Vector3f(-2, -2, -2), new Vector3f(1, 2, 3).sub(new Vector3f(3, 4, 5)));
    }
    @Test void v3fMul() {
        assertEquals(new Vector3f(3, 8, 15), new Vector3f(1, 2, 3).mul(new Vector3f(3, 4, 5)));
        assertEquals(new Vector3f(5, 10, 15), new Vector3f(1, 2, 3).mul(5));
    }
    @Test void v3fDiv() {
        assertEquals(new Vector3f(3, 2, 1), new Vector3f(9, 8, 5).div(new Vector3f(3, 4, 5)));
        assertEquals(new Vector3f(2, 4, 2), new Vector3f(10, 20, 10).div(5));
    }
    @Test void v3fScaleNegateAbs() {
        assertEquals(new Vector3f(3, 6, 9), new Vector3f(1, 2, 3).scale(3));
        assertEquals(new Vector3f(-1, -2, -3), new Vector3f(1, 2, 3).negate());
        assertEquals(new Vector3f(1, 2, 3), new Vector3f(-1, -2, -3).abs());
    }
    @Test void v3fDot() { assertEquals(32, new Vector3f(1, 2, 3).dot(new Vector3f(4, 5, 6)), F); }
    @Test void v3fCross() {
        Vector3f c = new Vector3f(1, 0, 0).cross(new Vector3f(0, 1, 0));
        assertEquals(new Vector3f(0, 0, 1), c);
    }
    @Test void v3fCrossOrthogonal() {
        Vector3f a = new Vector3f(3, 4, 5);
        Vector3f b = new Vector3f(6, 7, 8);
        Vector3f c = a.cross(b);
        assertEquals(0, a.dot(c), F);
        assertEquals(0, b.dot(c), F);
    }
    @Test void v3fLength() {
        assertEquals(5, new Vector3f(3, 4, 0).length(), F);
        assertEquals(25, new Vector3f(3, 4, 0).lengthSquared(), F);
    }
    @Test void v3fDistance() {
        assertEquals(5, new Vector3f(0, 0, 0).distance(new Vector3f(3, 4, 0)), F);
        assertEquals(5, new Vector3f(0, 0, 0).distance(3, 4, 0), F);
    }
    @Test void v3fNormalize() {
        Vector3f n = new Vector3f(3, 0, 0).normalize();
        assertEquals(1, n.x(), F); assertEquals(0, n.y(), F); assertEquals(0, n.z(), F);
    }
    @Test void v3fAngle() {
        assertEquals(0, new Vector3f(1, 0, 0).angle(new Vector3f(1, 0, 0)), F);
        assertEquals((float) Math.PI / 2, new Vector3f(1, 0, 0).angle(new Vector3f(0, 1, 0)), F);
    }
    @Test void v3fAngleSigned() {
        float a = new Vector3f(1, 0, 0).angleSigned(new Vector3f(0, 1, 0), new Vector3f(0, 0, 1));
        assertEquals((float) Math.PI / 2, a, F);
    }
    @Test void v3fReflect() {
        Vector3f r = new Vector3f(0, -1, 0).reflect(new Vector3f(0, 1, 0));
        assertEquals(0, r.x(), F); assertEquals(1, r.y(), F); assertEquals(0, r.z(), F);
    }
    @Test void v3fProject() {
        Vector3f p = new Vector3f(3, 4, 5).project(new Vector3f(0, 1, 0));
        assertEquals(0, p.x(), F); assertEquals(4, p.y(), F); assertEquals(0, p.z(), F);
    }
    @Test void v3fReject() {
        Vector3f r = new Vector3f(3, 4, 5).reject(new Vector3f(0, 1, 0));
        assertEquals(3, r.x(), F); assertEquals(0, r.y(), F); assertEquals(5, r.z(), F);
    }
    @Test void v3fLerp() {
        Vector3f l = new Vector3f(0, 0, 0).lerp(new Vector3f(10, 20, 30), 0.5f);
        assertEquals(5, l.x(), F); assertEquals(10, l.y(), F); assertEquals(15, l.z(), F);
    }
    @Test void v3fNlerp() {
        Vector3f n = new Vector3f(1, 0, 0).nlerp(new Vector3f(0, 1, 0), 0.5f);
        assertEquals(1, n.length(), F);
    }
    @Test void v3fRotateAxis() {
        Vector3f r = new Vector3f(1, 0, 0).rotate((float) Math.PI / 2, 0, 1, 0);
        assertEquals(0, r.x(), F); assertEquals(0, r.y(), F); assertEquals(-1, r.z(), F);
    }
    @Test void v3fRotateQuat() {
        Quaternionf q = Quaternionf.fromAxisAngle(new Vector3f(0, 1, 0), (float) Math.PI / 2);
        Vector3f r = new Vector3f(1, 0, 0).rotate(q);
        assertEquals(0, r.x(), F); assertEquals(0, r.y(), F); assertEquals(-1, r.z(), F);
    }
    @Test void v3fHalfway() {
        Vector3f h = new Vector3f(1, 0, 0).halfway(new Vector3f(0, 1, 0));
        assertEquals(1, h.length(), F);
    }
    @Test void v3fCeilFloorRound() {
        assertEquals(new Vector3f(2, -1, 0), new Vector3f(1.2f, -1.2f, 0).ceil());
        assertEquals(new Vector3f(1, -2, 0), new Vector3f(1.2f, -1.2f, 0).floor());
        assertEquals(new Vector3f(1, -1, 0), new Vector3f(1.2f, -1.2f, 0).round());
    }
    @Test void v3fMinMax() {
        assertEquals(new Vector3f(1, 2, 3), new Vector3f(1, 5, 3).min(new Vector3f(3, 2, 4)));
        assertEquals(new Vector3f(3, 5, 4), new Vector3f(1, 5, 3).max(new Vector3f(3, 2, 4)));
    }
    @Test void v3fFma() {
        assertEquals(new Vector3f(5, 13, 5), new Vector3f(1, 2, 3).fma(new Vector3f(3, 4, 5), new Vector3f(2, 5, -10)));
        assertEquals(new Vector3f(5, 11, 17), new Vector3f(1, 2, 3).fma(3, new Vector3f(2, 5, 8)));
    }
    @Test void v3fOrthogonalize() {
        Vector3f a = new Vector3f(1, 1, 0);
        Vector3f b = new Vector3f(1, 0, 0);
        Vector3f o = a.orthogonalize(b);
        assertEquals(0, o.dot(b), F);
    }
    @Test void v3fOrthogonalizeUnit() {
        Vector3f a = new Vector3f(1, 1, 0);
        Vector3f b = new Vector3f(1, 0, 0);
        Vector3f o = a.orthogonalizeUnit(b);
        assertEquals(0, o.dot(b), F);
        assertEquals(1, o.length(), F);
    }
    @Test void v3fIsPerpendicular() {
        assertTrue(new Vector3f(1, 0, 0).isPerpendicular(new Vector3f(0, 1, 0), F));
        assertFalse(new Vector3f(1, 0, 0).isPerpendicular(new Vector3f(1, 0, 0), F));
    }
    @Test void v3fComponent() {
        assertEquals(1, new Vector3f(3, 1, 2).minComponent(), F);
        assertEquals(3, new Vector3f(3, 1, 2).maxComponent(), F);
        assertEquals(0, new Vector3f(3, 1, 2).maxComponentIndex());
        assertEquals(1, new Vector3f(3, 1, 2).minComponentIndex());
    }
    @Test void v3fConvert() {
        assertEquals(new Vector2f(1, 2), new Vector3f(1, 2, 3).toVector2f());
        assertEquals(new Vector4f(1, 2, 3, 0), new Vector3f(1, 2, 3).toVector4f());
        assertEquals(new Vector4f(1, 2, 3, 4), new Vector3f(1, 2, 3).toVector4f(4));
        assertEquals(new Vector3d(1, 2, 3), new Vector3f(1, 2, 3).toVector3d());
    }
    @Test void v3fEqualsDelta() {
        assertTrue(new Vector3f(1, 2, 3).equals(new Vector3f(1.001f, 2.001f, 3.001f), 0.01f));
        assertFalse(new Vector3f(1, 2, 3).equals(new Vector3f(1.1f, 2, 3), 0.01f));
    }

    // ── Vector4f ──────────────────────────────────────────────────────

    @Test void v4fAdd() {
        assertEquals(new Vector4f(4, 6, 8, 10), new Vector4f(1, 2, 3, 4).add(new Vector4f(3, 4, 5, 6)));
        assertEquals(new Vector4f(4, 6, 8, 10), new Vector4f(1, 2, 3, 4).add(3, 4, 5, 6));
    }
    @Test void v4fSub() {
        assertEquals(new Vector4f(-2, -2, -2, -2), new Vector4f(1, 2, 3, 4).sub(new Vector4f(3, 4, 5, 6)));
    }
    @Test void v4fMul() {
        assertEquals(new Vector4f(3, 8, 15, 24), new Vector4f(1, 2, 3, 4).mul(new Vector4f(3, 4, 5, 6)));
        assertEquals(new Vector4f(5, 10, 15, 20), new Vector4f(1, 2, 3, 4).mul(5));
    }
    @Test void v4fDot() { assertEquals(70, new Vector4f(1, 2, 3, 4).dot(new Vector4f(5, 6, 7, 8)), F); }
    @Test void v4fLength() {
        assertEquals(5, new Vector4f(3, 4, 0, 0).length(), F);
    }
    @Test void v4fNormalize() {
        Vector4f n = new Vector4f(3, 0, 0, 0).normalize();
        assertEquals(1, n.x(), F); assertEquals(1, n.length(), F);
    }
    @Test void v4fLerp() {
        Vector4f l = new Vector4f(0, 0, 0, 0).lerp(new Vector4f(10, 20, 30, 40), 0.5f);
        assertEquals(5, l.x(), F); assertEquals(10, l.y(), F); assertEquals(15, l.z(), F); assertEquals(20, l.w(), F);
    }
    @Test void v4fAngle() {
        assertEquals(0, new Vector4f(1, 0, 0, 0).angle(new Vector4f(1, 0, 0, 0)), F);
        assertEquals((float) Math.PI / 2, new Vector4f(1, 0, 0, 0).angle(new Vector4f(0, 1, 0, 0)), F);
    }
    @Test void v4fCeilFloorRound() {
        assertEquals(new Vector4f(2, -1, 1, 0), new Vector4f(1.2f, -1.2f, 0.5f, 0).ceil());
        assertEquals(new Vector4f(1, -2, 0, 0), new Vector4f(1.2f, -1.2f, 0.5f, 0).floor());
    }
    @Test void v4fMinMax() {
        assertEquals(new Vector4f(1, 2, 3, 4), new Vector4f(1, 5, 3, 4).min(new Vector4f(3, 2, 4, 5)));
    }
    @Test void v4fSwizzle() {
        assertEquals(new Vector2f(1, 2), new Vector4f(1, 2, 3, 4).xy());
        assertEquals(new Vector3f(1, 2, 3), new Vector4f(1, 2, 3, 4).xyz());
    }
    @Test void v4fConvert() {
        assertEquals(new Vector2f(1, 2), new Vector4f(1, 2, 3, 4).toVector2f());
        assertEquals(new Vector3f(1, 2, 3), new Vector4f(1, 2, 3, 4).toVector3f());
        assertEquals(new Vector4d(1, 2, 3, 4), new Vector4f(1, 2, 3, 4).toVector4d());
    }
    @Test void v4fComponent() {
        assertEquals(1, new Vector4f(3, 1, 2, 4).minComponent(), F);
        assertEquals(4, new Vector4f(3, 1, 2, 4).maxComponent(), F);
        assertEquals(0, new Vector4f(5, 1, 2, 4).maxComponentIndex());
        assertEquals(1, new Vector4f(3, 1, 2, 4).minComponentIndex());
        assertEquals(3, new Vector4f(3, 4, 2, 5).maxComponentIndex());
        assertEquals(2, new Vector4f(3, 4, 1, 5).minComponentIndex());
    }

    // ── Vector2d ──────────────────────────────────────────────────────

    @Test void v2dAdd() {
        assertEquals(new Vector2d(4, 6), new Vector2d(1, 2).add(new Vector2d(3, 4)));
        assertEquals(new Vector2d(4, 6), new Vector2d(1, 2).add(3, 4));
    }
    @Test void v2dDot() { assertEquals(11.0, new Vector2d(1, 2).dot(new Vector2d(3, 4)), D); }
    @Test void v2dLength() { assertEquals(5.0, new Vector2d(3, 4).length(), D); }
    @Test void v2dDistance() { assertEquals(5.0, new Vector2d(0, 0).distance(new Vector2d(3, 4)), D); }
    @Test void v2dPerpendicular() {
        Vector2d v = new Vector2d(3, 4);
        Vector2d p = v.perpendicular();
        assertEquals(0, v.dot(p), D);
        assertEquals(new Vector2d(-4, 3), p);
    }
    @Test void v2dRotate() {
        Vector2d r = new Vector2d(1, 0).rotate(Math.PI / 2);
        assertEquals(0, r.x(), D); assertEquals(1, r.y(), D);
    }
    @Test void v2dNormalize() {
        Vector2d n = new Vector2d(3, 0).normalize();
        assertEquals(1, n.x(), D); assertEquals(0, n.y(), D);
    }
    @Test void v2dReflect() {
        Vector2d r = new Vector2d(1, -1).reflect(new Vector2d(0, 1));
        assertEquals(1, r.x(), D); assertEquals(1, r.y(), D);
    }
    @Test void v2dProjectReject() {
        Vector2d p = new Vector2d(3, 4).project(new Vector2d(1, 0));
        assertEquals(3, p.x(), D);
        Vector2d r = new Vector2d(3, 4).reject(new Vector2d(1, 0));
        assertEquals(4, r.y(), D);
    }
    @Test void v2dConvert() {
        assertEquals(new Vector3d(1, 2, 0), new Vector2d(1, 2).toVector3d());
        assertEquals(new Vector3d(1, 2, 3), new Vector2d(1, 2).toVector3d(3));
        assertEquals(new Vector2f(1, 2), new Vector2d(1, 2).toVector2f());
    }

    // ── Vector3d ──────────────────────────────────────────────────────

    @Test void v3dAdd() { assertEquals(new Vector3d(4, 6, 8), new Vector3d(1, 2, 3).add(new Vector3d(3, 4, 5))); }
    @Test void v3dDot() { assertEquals(32.0, new Vector3d(1, 2, 3).dot(new Vector3d(4, 5, 6)), D); }
    @Test void v3dCross() {
        Vector3d c = new Vector3d(1, 0, 0).cross(new Vector3d(0, 1, 0));
        assertEquals(new Vector3d(0, 0, 1), c);
        Vector3d a = new Vector3d(3, 4, 5);
        Vector3d b = new Vector3d(6, 7, 8);
        assertEquals(0, a.dot(a.cross(b)), D);
        assertEquals(0, b.dot(a.cross(b)), D);
    }
    @Test void v3dLength() { assertEquals(5.0, new Vector3d(3, 4, 0).length(), D); }
    @Test void v3dNormalize() {
        Vector3d n = new Vector3d(3, 0, 0).normalize();
        assertEquals(1, n.x(), D);
    }
    @Test void v3dReflect() {
        Vector3d r = new Vector3d(0, -1, 0).reflect(new Vector3d(0, 1, 0));
        assertEquals(0, r.x(), D); assertEquals(1, r.y(), D); assertEquals(0, r.z(), D);
    }
    @Test void v3dRotateAxis() {
        Vector3d r = new Vector3d(1, 0, 0).rotate(Math.PI / 2, 0, 1, 0);
        assertEquals(0, r.x(), D); assertEquals(0, r.y(), D); assertEquals(-1, r.z(), D);
    }
    @Test void v3dOrthogonalize() {
        Vector3d o = new Vector3d(1, 1, 0).orthogonalize(new Vector3d(1, 0, 0));
        assertEquals(0, o.dot(new Vector3d(1, 0, 0)), D);
    }

    // ── Vector4d ──────────────────────────────────────────────────────

    @Test void v4dAdd() {
        assertEquals(new Vector4d(4, 6, 8, 10), new Vector4d(1, 2, 3, 4).add(new Vector4d(3, 4, 5, 6)));
    }
    @Test void v4dDot() { assertEquals(70.0, new Vector4d(1, 2, 3, 4).dot(new Vector4d(5, 6, 7, 8)), D); }
    @Test void v4dLength() { assertEquals(5.0, new Vector4d(3, 4, 0, 0).length(), D); }
    @Test void v4dNormalize() {
        Vector4d n = new Vector4d(3, 0, 0, 0).normalize();
        assertEquals(1, n.x(), D); assertEquals(1, n.length(), D);
    }
    @Test void v4dSwizzle() {
        assertEquals(new Vector2d(1, 2), new Vector4d(1, 2, 3, 4).xy());
        assertEquals(new Vector3d(1, 2, 3), new Vector4d(1, 2, 3, 4).xyz());
    }
    @Test void v4dConvert() {
        assertEquals(new Vector4f(1, 2, 3, 4), new Vector4d(1, 2, 3, 4).toVector4f());
    }
}

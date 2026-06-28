package com.jamma.math;

import com.jamma.math.matrix.Matrix4d;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaterniond;
import com.jamma.math.quaternion.Quaternionf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.PI;

class ParityTest {

    private static final double EPS = 1e-6;

    @Test void vectorNormalizeParity() {
        Vector3d vd = new Vector3d(3.0, -4.0, 12.0).normalize();
        Vector3f vf = new Vector3f(3.0f, -4.0f, 12.0f).normalize();
        assertClose(vd.x(), vf.x());
        assertClose(vd.y(), vf.y());
        assertClose(vd.z(), vf.z());
    }

    @Test void vectorAngleParity() {
        Vector3d a = new Vector3d(1.0, 2.0, 3.0);
        Vector3d b = new Vector3d(-2.0, 0.5, 4.0);
        assertClose(a.angle(b), new Vector3f(1.0f, 2.0f, 3.0f).angle(new Vector3f(-2.0f, 0.5f, 4.0f)));
    }

    @Test void vectorMathParity() {
        Vector3d a = new Vector3d(1.0, 2.0, 3.0);
        Vector3d b = new Vector3d(4.0, -5.0, 6.0);
        Vector3d ad = VectorMath.add(a, b);
        Vector3f af = VectorMathf.add(new Vector3f(1.0f, 2.0f, 3.0f), new Vector3f(4.0f, -5.0f, 6.0f));
        assertClose(ad.x(), af.x());
        assertClose(ad.y(), af.y());
        assertClose(ad.z(), af.z());

        Vector3d pd = VectorMath.project(a, b);
        Vector3f pf = VectorMathf.project(new Vector3f(1.0f, 2.0f, 3.0f), new Vector3f(4.0f, -5.0f, 6.0f));
        assertClose(pd.x(), pf.x());
        assertClose(pd.y(), pf.y());
        assertClose(pd.z(), pf.z());
    }

    @Test void quaternionRotationParity() {
        Vector3d vd = new Vector3d(1.0, 0.0, 0.0);
        Vector3f vf = new Vector3f(1.0f, 0.0f, 0.0f);
        Quaterniond qd = Quaterniond.fromAxisAngle(new Vector3d(0.0, 1.0, 0.0), PI / 2.0);
        Quaternionf qf = Quaternionf.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f), (float) (PI / 2.0));
        Vector3d rd = qd.transform(vd);
        Vector3f rf = qf.transform(vf);
        assertClose(rd.x(), rf.x());
        assertClose(rd.y(), rf.y());
        assertClose(rd.z(), rf.z());
    }

    @Test void matrixTransformParity() {
        Matrix4d md = new Matrix4d().translate(3.0, 4.0, 5.0).rotateY(PI / 3.0).scale(2.0, 3.0, 4.0);
        Matrix4f mf = new Matrix4f().translate(3.0f, 4.0f, 5.0f).rotateY((float) (PI / 3.0)).scale(2.0f, 3.0f, 4.0f);
        Vector3d vd = md.transformPosition(new Vector3d(1.0, 2.0, 3.0));
        Vector3f vf = mf.transformPosition(new Vector3f(1.0f, 2.0f, 3.0f));
        assertClose(vd.x(), vf.x());
        assertClose(vd.y(), vf.y());
        assertClose(vd.z(), vf.z());
    }

    @Test void matrixInverseParity() {
        Matrix4d md = new Matrix4d().translate(2.0, -1.0, 5.0).rotateX(PI / 4.0).scale(1.5, 2.0, 0.5);
        Matrix4f mf = new Matrix4f().translate(2.0f, -1.0f, 5.0f).rotateX((float) (PI / 4.0)).scale(1.5f, 2.0f, 0.5f);
        Matrix4d id = new Matrix4d(md).invert().multiply(md);
        Matrix4f ifv = new Matrix4f(mf).invert().multiply(mf);
        assertTrue(id.isIdentity(1e-9));
        assertTrue(ifv.isIdentity(1e-5f));
    }

    private static void assertClose(double expected, float actual) {
        assertEquals(expected, actual, EPS);
    }

    private static void assertClose(double expected, double actual) {
        assertEquals(expected, actual, EPS);
    }
}

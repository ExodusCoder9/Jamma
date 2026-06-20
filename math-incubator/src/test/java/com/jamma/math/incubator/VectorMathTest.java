package com.jamma.math.incubator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jamma.math.Vector2d;
import com.jamma.math.Vector3d;
import com.jamma.math.Vector4d;

class VectorMathTest {

    @Test void add3() {
        Vector3d a = new Vector3d(1.0, 2.0, 3.0);
        Vector3d b = new Vector3d(4.0, 5.0, 6.0);
        assertEquals(new Vector3d(5.0, 7.0, 9.0), VectorMath.add(a, b));
    }

    @Test void sub3() {
        Vector3d a = new Vector3d(5.0, 7.0, 9.0);
        Vector3d b = new Vector3d(1.0, 2.0, 3.0);
        assertEquals(new Vector3d(4.0, 5.0, 6.0), VectorMath.sub(a, b));
    }

    @Test void scale() {
        Vector2d v = new Vector2d(2.0, 3.0);
        assertEquals(new Vector2d(4.0, 6.0), VectorMath.scale(v, 2.0));
    }

    @Test void mul() {
        Vector3d a = new Vector3d(2.0, 3.0, 4.0);
        Vector3d b = new Vector3d(5.0, 6.0, 7.0);
        assertEquals(new Vector3d(10.0, 18.0, 28.0), VectorMath.mul(a, b));
    }

    @Test void div() {
        Vector3d a = new Vector3d(10.0, 18.0, 28.0);
        Vector3d b = new Vector3d(2.0, 3.0, 4.0);
        assertEquals(new Vector3d(5.0, 6.0, 7.0), VectorMath.div(a, b));
    }

    @Test void negate() {
        Vector3d v = new Vector3d(1.0, -2.0, 3.0);
        assertEquals(new Vector3d(-1.0, 2.0, -3.0), VectorMath.negate(v));
    }

    @Test void abs() {
        Vector3d v = new Vector3d(-1.0, 2.0, -3.0);
        assertEquals(new Vector3d(1.0, 2.0, 3.0), VectorMath.abs(v));
    }

    @Test void sign() {
        Vector3d v = new Vector3d(-5.0, 0.0, 3.0);
        assertEquals(new Vector3d(-1.0, 0.0, 1.0), VectorMath.sign(v));
    }

    @Test void dot() {
        Vector3d a = new Vector3d(1.0, 2.0, 3.0);
        Vector3d b = new Vector3d(4.0, 5.0, 6.0);
        assertEquals(32.0, VectorMath.dot(a, b), 1e-15);
    }

    @Test void cross() {
        Vector3d a = new Vector3d(1.0, 0.0, 0.0);
        Vector3d b = new Vector3d(0.0, 1.0, 0.0);
        assertEquals(new Vector3d(0.0, 0.0, 1.0), VectorMath.cross(a, b));
    }

    @Test void length() {
        Vector3d v = new Vector3d(3.0, 4.0, 0.0);
        assertEquals(5.0, VectorMath.length(v), 1e-15);
    }

    @Test void normalize() {
        Vector3d v = new Vector3d(3.0, 4.0, 0.0);
        Vector3d n = VectorMath.normalize(v);
        assertEquals(1.0, VectorMath.length(n), 1e-15);
    }

    @Test void safeNormalize() {
        Vector3d zero = new Vector3d(0.0, 0.0, 0.0);
        Vector3d fallback = new Vector3d(1.0, 0.0, 0.0);
        assertEquals(fallback, VectorMath.safeNormalize(zero, fallback));
    }

    @Test void reflect() {
        Vector2d dir = new Vector2d(1.0, -1.0);
        Vector2d normal = new Vector2d(0.0, 1.0);
        assertEquals(new Vector2d(1.0, 1.0), VectorMath.reflect(dir, normal));
    }

    @Test void refract() {
        Vector2d dir = new Vector2d(1.0, -1.0);
        Vector2d normal = new Vector2d(0.0, 1.0);
        assertEquals(new Vector2d(1.0, -1.0), VectorMath.refract(dir, normal, 1.0));
    }

    @Test void project() {
        Vector2d v = new Vector2d(3.0, 4.0);
        Vector2d onto = new Vector2d(1.0, 0.0);
        assertEquals(new Vector2d(3.0, 0.0), VectorMath.project(v, onto));
    }

    @Test void reject() {
        Vector2d v = new Vector2d(3.0, 4.0);
        Vector2d onto = new Vector2d(1.0, 0.0);
        assertEquals(new Vector2d(0.0, 4.0), VectorMath.reject(v, onto));
    }

    @Test void perpendicular() {
        Vector2d v = new Vector2d(1.0, 2.0);
        assertEquals(new Vector2d(-2.0, 1.0), VectorMath.perpendicular(v));
    }

    @Test void cross2D() {
        Vector2d a = new Vector2d(1.0, 0.0);
        Vector2d b = new Vector2d(0.0, 1.0);
        assertEquals(1.0, VectorMath.cross2D(a, b), 1e-15);
    }

    @Test void angle() {
        Vector2d a = new Vector2d(1.0, 0.0);
        Vector2d b = new Vector2d(0.0, 1.0);
        assertEquals(Math.PI / 2.0, VectorMath.angle(a, b), 1e-15);
    }

    @Test void angleSigned2D() {
        Vector2d a = new Vector2d(1.0, 0.0);
        Vector2d b = new Vector2d(0.0, 1.0);
        assertEquals(Math.PI / 2.0, VectorMath.angleSigned(a, b), 1e-15);
    }

    @Test void angleSigned3D() {
        Vector3d a = new Vector3d(1.0, 0.0, 0.0);
        Vector3d b = new Vector3d(0.0, 1.0, 0.0);
        Vector3d n = new Vector3d(0.0, 0.0, 1.0);
        assertEquals(Math.PI / 2.0, VectorMath.angleSigned(a, b, n), 1e-15);
    }

    @Test void rotate() {
        Vector2d v = new Vector2d(1.0, 0.0);
        Vector2d r = VectorMath.rotate(v, Math.PI / 2.0);
        assertEquals(0.0, r.x(), 1e-15);
        assertEquals(1.0, r.y(), 1e-15);
    }

    @Test void rotateAroundAxis() {
        Vector3d v = new Vector3d(1.0, 0.0, 0.0);
        Vector3d axis = new Vector3d(0.0, 0.0, 1.0);
        Vector3d r = VectorMath.rotateAroundAxis(v, axis, Math.PI / 2.0);
        assertEquals(0.0, r.x(), 1e-15);
        assertEquals(1.0, r.y(), 1e-10);
        assertEquals(0.0, r.z(), 1e-15);
    }

    @Test void midpoint() {
        Vector2d a = new Vector2d(0.0, 0.0);
        Vector2d b = new Vector2d(2.0, 4.0);
        assertEquals(new Vector2d(1.0, 2.0), VectorMath.midpoint(a, b));
    }

    @Test void min() {
        Vector2d a = new Vector2d(1.0, 5.0);
        Vector2d b = new Vector2d(3.0, 2.0);
        assertEquals(new Vector2d(1.0, 2.0), VectorMath.min(a, b));
    }

    @Test void max() {
        Vector2d a = new Vector2d(1.0, 5.0);
        Vector2d b = new Vector2d(3.0, 2.0);
        assertEquals(new Vector2d(3.0, 5.0), VectorMath.max(a, b));
    }

    @Test void clamp() {
        Vector2d v = new Vector2d(-1.0, 5.0);
        Vector2d min = new Vector2d(0.0, 0.0);
        Vector2d max = new Vector2d(3.0, 3.0);
        assertEquals(new Vector2d(0.0, 3.0), VectorMath.clamp(v, min, max));
    }

    @Test void limit() {
        Vector2d v = new Vector2d(10.0, 0.0);
        Vector2d c = VectorMath.limit(v, 3.0);
        assertEquals(3.0, VectorMath.length(c), 1e-15);
    }

    @Test void setLength() {
        Vector2d v = new Vector2d(1.0, 0.0);
        assertEquals(new Vector2d(5.0, 0.0), VectorMath.setLength(v, 5.0));
    }

    @Test void tripleScalar() {
        Vector3d a = new Vector3d(1.0, 0.0, 0.0);
        Vector3d b = new Vector3d(0.0, 1.0, 0.0);
        Vector3d c = new Vector3d(0.0, 0.0, 1.0);
        assertEquals(1.0, VectorMath.tripleScalar(a, b, c), 1e-15);
    }

    @Test void tripleVector() {
        Vector3d a = new Vector3d(1.0, 2.0, 3.0);
        Vector3d b = new Vector3d(0.0, 4.0, 0.0);
        Vector3d c = new Vector3d(5.0, 0.0, 6.0);
        assertEquals(new Vector3d(-40.0, 92.0, -48.0), VectorMath.tripleVector(a, b, c));
    }

    @Test void orthonormalBasis() {
        Vector3d v = new Vector3d(1.0, 0.0, 0.0);
        Vector3d bx = VectorMath.orthonormalBasisX(v);
        Vector3d by = VectorMath.orthonormalBasisY(v);
        Vector3d bz = VectorMath.orthonormalBasisZ(v);
        assertEquals(1.0, VectorMath.length(bx), 1e-15);
        assertEquals(1.0, VectorMath.length(by), 1e-15);
        assertEquals(1.0, VectorMath.length(bz), 1e-15);
        assertEquals(0.0, VectorMath.dot(bx, by), 1e-15);
    }

    @Test void nlerp() {
        Vector3d a = new Vector3d(1.0, 0.0, 0.0);
        Vector3d b = new Vector3d(0.0, 1.0, 0.0);
        Vector3d n = VectorMath.nlerp(a, b, 0.5);
        assertEquals(1.0, VectorMath.length(n), 1e-15);
    }

    @Test void slerp() {
        Vector3d a = new Vector3d(1.0, 0.0, 0.0);
        Vector3d b = new Vector3d(0.0, 1.0, 0.0);
        Vector3d s = VectorMath.slerp(a, b, 0.5);
        assertEquals(1.0, VectorMath.length(s), 1e-15);
    }

    @Test void faceforward() {
        Vector3d n = new Vector3d(0.0, 1.0, 0.0);
        Vector3d i = new Vector3d(0.0, -1.0, 0.0);
        Vector3d nRef = new Vector3d(0.0, 1.0, 0.0);
        assertEquals(n, VectorMath.faceforward(n, i, nRef));
    }

    @Test void projectOnPlane() {
        Vector3d v = new Vector3d(1.0, 1.0, 0.0);
        Vector3d normal = new Vector3d(0.0, 1.0, 0.0);
        assertEquals(new Vector3d(1.0, 0.0, 0.0), VectorMath.projectOnPlane(v, normal));
    }

    @Test void catmullRom() {
        Vector2d r = VectorMath.catmullRom(
            new Vector2d(0,0), new Vector2d(1,1),
            new Vector2d(2,0), new Vector2d(3,1), 0.5);
        assertEquals(1.5, r.x(), 1e-15);
    }

    @Test void bezier() {
        Vector2d r = VectorMath.bezier(
            new Vector2d(0,0), new Vector2d(0,1),
            new Vector2d(1,1), new Vector2d(1,0), 0.5);
        assertEquals(0.5, r.x(), 1e-15);
        assertEquals(0.75, r.y(), 1e-15);
    }

    @Test void distanceSquared() {
        Vector2d a = new Vector2d(0,0), b = new Vector2d(3,4);
        assertEquals(25.0, VectorMath.distanceSquared(a, b), 1e-15);
    }

    @Test void isFinite() {
        assertTrue(VectorMath.isFinite(new Vector2d(1,2)));
        assertFalse(VectorMath.hasNaN(new Vector2d(1,2)));
    }

    @Test void toFromArray() {
        Vector3d v = new Vector3d(1,2,3);
        double[] arr = VectorMath.toArray3(v);
        assertArrayEquals(new double[]{1,2,3}, arr, 1e-15);
        assertEquals(v, VectorMath.fromArray3(arr, 0));
    }

    @Test void collinear() {
        assertTrue(VectorMath.isCollinear(
            new Vector2d(0,0), new Vector2d(1,1), new Vector2d(2,2)));
        assertFalse(VectorMath.isCollinear(
            new Vector2d(0,0), new Vector2d(1,0), new Vector2d(0,1)));
    }

    @Test void coplanar() {
        assertTrue(VectorMath.isCoplanar(
            new Vector3d(0,0,0), new Vector3d(1,0,0),
            new Vector3d(0,1,0), new Vector3d(1,1,0)));
    }

    @Test void orthogonal() {
        assertTrue(VectorMath.isOrthogonal(
            new Vector2d(1,0), new Vector2d(0,1)));
        assertFalse(VectorMath.isOrthogonal(
            new Vector2d(1,0), new Vector2d(1,0)));
    }

    @Test void parallel() {
        assertTrue(VectorMath.isParallel(
            new Vector2d(2,0), new Vector2d(4,0)));
        assertFalse(VectorMath.isParallel(
            new Vector2d(1,0), new Vector2d(0,1)));
    }

    @Test void closestPointOnSegment() {
        Vector2d p = new Vector2d(0, 5);
        Vector2d a = new Vector2d(0, 0);
        Vector2d b = new Vector2d(0, 10);
        assertEquals(new Vector2d(0, 5), VectorMath.closestPointOnSegment(p, a, b));
    }

    @Test void distanceToSegment() {
        Vector2d p = new Vector2d(5, 5);
        Vector2d a = new Vector2d(0, 0);
        Vector2d b = new Vector2d(0, 10);
        assertEquals(5.0, VectorMath.distanceToSegment(p, a, b), 1e-15);
    }

    @Test void componentMinMax() {
        Vector3d v = new Vector3d(3, 1, 2);
        assertEquals(1.0, VectorMath.componentMin(v), 1e-15);
        assertEquals(3.0, VectorMath.componentMax(v), 1e-15);
    }

    @Test void componentSum() {
        Vector3d v = new Vector3d(1, 2, 3);
        assertEquals(6.0, VectorMath.componentSum(v), 1e-15);
    }

    @Test void add4() {
        Vector4d a = new Vector4d(1,2,3,4);
        Vector4d b = new Vector4d(5,6,7,8);
        assertEquals(new Vector4d(6,8,10,12), VectorMath.add(a, b));
    }

    @Test void batchAdd() {
        Vector3d[] a = {new Vector3d(1,0,0), new Vector3d(0,1,0)};
        Vector3d[] b = {new Vector3d(2,0,0), new Vector3d(0,2,0)};
        Vector3d[] r = VectorMath.batchAdd(a, b);
        assertEquals(new Vector3d(3,0,0), r[0]);
        assertEquals(new Vector3d(0,3,0), r[1]);
    }

    @Test void batchDot() {
        Vector3d[] a = {new Vector3d(1,0,0), new Vector3d(0,1,0)};
        Vector3d[] b = {new Vector3d(1,0,0), new Vector3d(0,1,0)};
        assertArrayEquals(new double[]{1,1}, VectorMath.batchDot(a, b), 1e-15);
    }

    @Test void batchScale() {
        Vector3d[] v = {new Vector3d(1,0,0), new Vector3d(0,2,0)};
        Vector3d[] r = VectorMath.batchScale(v, 2);
        assertEquals(new Vector3d(2,0,0), r[0]);
        assertEquals(new Vector3d(0,4,0), r[1]);
    }

    @Test void batchNormalize() {
        Vector3d[] v = {new Vector3d(2,0,0), new Vector3d(0,3,0)};
        Vector3d[] r = VectorMath.batchNormalize(v);
        assertEquals(1.0, VectorMath.length(r[0]), 1e-15);
        assertEquals(1.0, VectorMath.length(r[1]), 1e-15);
    }

    @Test void batchNegate() {
        Vector3d[] v = {new Vector3d(1,0,0), new Vector3d(0,-2,0)};
        Vector3d[] r = VectorMath.batchNegate(v);
        assertEquals(-1.0, r[0].x(), 1e-15);
        assertEquals(0.0, r[0].y(), 1e-15);
        assertEquals(0.0, r[0].z(), 1e-15);
        assertEquals(0.0, r[1].x(), 1e-15);
        assertEquals(2.0, r[1].y(), 1e-15);
        assertEquals(0.0, r[1].z(), 1e-15);
    }

    @Test void cross2DThreePoint() {
        Vector2d a = new Vector2d(0,0);
        Vector2d b = new Vector2d(1,0);
        Vector2d c = new Vector2d(0,1);
        assertEquals(1.0, VectorMath.cross2D(a, b, c), 1e-15);
    }
}

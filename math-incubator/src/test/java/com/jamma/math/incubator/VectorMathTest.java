package com.jamma.math.incubator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jamma.math.Vector2D;
import com.jamma.math.Vector3D;
import com.jamma.math.Vector4D;

class VectorMathTest {

    @Test void add3() {
        Vector3D a = new Vector3D(1.0, 2.0, 3.0);
        Vector3D b = new Vector3D(4.0, 5.0, 6.0);
        assertEquals(new Vector3D(5.0, 7.0, 9.0), VectorMath.add(a, b));
    }

    @Test void sub3() {
        Vector3D a = new Vector3D(5.0, 7.0, 9.0);
        Vector3D b = new Vector3D(1.0, 2.0, 3.0);
        assertEquals(new Vector3D(4.0, 5.0, 6.0), VectorMath.sub(a, b));
    }

    @Test void scale() {
        Vector2D v = new Vector2D(2.0, 3.0);
        assertEquals(new Vector2D(4.0, 6.0), VectorMath.scale(v, 2.0));
    }

    @Test void mul() {
        Vector3D a = new Vector3D(2.0, 3.0, 4.0);
        Vector3D b = new Vector3D(5.0, 6.0, 7.0);
        assertEquals(new Vector3D(10.0, 18.0, 28.0), VectorMath.mul(a, b));
    }

    @Test void div() {
        Vector3D a = new Vector3D(10.0, 18.0, 28.0);
        Vector3D b = new Vector3D(2.0, 3.0, 4.0);
        assertEquals(new Vector3D(5.0, 6.0, 7.0), VectorMath.div(a, b));
    }

    @Test void negate() {
        Vector3D v = new Vector3D(1.0, -2.0, 3.0);
        assertEquals(new Vector3D(-1.0, 2.0, -3.0), VectorMath.negate(v));
    }

    @Test void abs() {
        Vector3D v = new Vector3D(-1.0, 2.0, -3.0);
        assertEquals(new Vector3D(1.0, 2.0, 3.0), VectorMath.abs(v));
    }

    @Test void sign() {
        Vector3D v = new Vector3D(-5.0, 0.0, 3.0);
        assertEquals(new Vector3D(-1.0, 0.0, 1.0), VectorMath.sign(v));
    }

    @Test void dot() {
        Vector3D a = new Vector3D(1.0, 2.0, 3.0);
        Vector3D b = new Vector3D(4.0, 5.0, 6.0);
        assertEquals(32.0, VectorMath.dot(a, b), 1e-15);
    }

    @Test void cross() {
        Vector3D a = new Vector3D(1.0, 0.0, 0.0);
        Vector3D b = new Vector3D(0.0, 1.0, 0.0);
        assertEquals(new Vector3D(0.0, 0.0, 1.0), VectorMath.cross(a, b));
    }

    @Test void length() {
        Vector3D v = new Vector3D(3.0, 4.0, 0.0);
        assertEquals(5.0, VectorMath.length(v), 1e-15);
    }

    @Test void normalize() {
        Vector3D v = new Vector3D(3.0, 4.0, 0.0);
        Vector3D n = VectorMath.normalize(v);
        assertEquals(1.0, VectorMath.length(n), 1e-15);
    }

    @Test void safeNormalize() {
        Vector3D zero = new Vector3D(0.0, 0.0, 0.0);
        Vector3D fallback = new Vector3D(1.0, 0.0, 0.0);
        assertEquals(fallback, VectorMath.safeNormalize(zero, fallback));
    }

    @Test void reflect() {
        Vector2D dir = new Vector2D(1.0, -1.0);
        Vector2D normal = new Vector2D(0.0, 1.0);
        assertEquals(new Vector2D(1.0, 1.0), VectorMath.reflect(dir, normal));
    }

    @Test void refract() {
        Vector2D dir = new Vector2D(1.0, -1.0);
        Vector2D normal = new Vector2D(0.0, 1.0);
        assertEquals(new Vector2D(1.0, -1.0), VectorMath.refract(dir, normal, 1.0));
    }

    @Test void project() {
        Vector2D v = new Vector2D(3.0, 4.0);
        Vector2D onto = new Vector2D(1.0, 0.0);
        assertEquals(new Vector2D(3.0, 0.0), VectorMath.project(v, onto));
    }

    @Test void reject() {
        Vector2D v = new Vector2D(3.0, 4.0);
        Vector2D onto = new Vector2D(1.0, 0.0);
        assertEquals(new Vector2D(0.0, 4.0), VectorMath.reject(v, onto));
    }

    @Test void perpendicular() {
        Vector2D v = new Vector2D(1.0, 2.0);
        assertEquals(new Vector2D(-2.0, 1.0), VectorMath.perpendicular(v));
    }

    @Test void cross2D() {
        Vector2D a = new Vector2D(1.0, 0.0);
        Vector2D b = new Vector2D(0.0, 1.0);
        assertEquals(1.0, VectorMath.cross2D(a, b), 1e-15);
    }

    @Test void angle() {
        Vector2D a = new Vector2D(1.0, 0.0);
        Vector2D b = new Vector2D(0.0, 1.0);
        assertEquals(Math.PI / 2.0, VectorMath.angle(a, b), 1e-15);
    }

    @Test void angleSigned2D() {
        Vector2D a = new Vector2D(1.0, 0.0);
        Vector2D b = new Vector2D(0.0, 1.0);
        assertEquals(Math.PI / 2.0, VectorMath.angleSigned(a, b), 1e-15);
    }

    @Test void angleSigned3D() {
        Vector3D a = new Vector3D(1.0, 0.0, 0.0);
        Vector3D b = new Vector3D(0.0, 1.0, 0.0);
        Vector3D n = new Vector3D(0.0, 0.0, 1.0);
        assertEquals(Math.PI / 2.0, VectorMath.angleSigned(a, b, n), 1e-15);
    }

    @Test void rotate() {
        Vector2D v = new Vector2D(1.0, 0.0);
        Vector2D r = VectorMath.rotate(v, Math.PI / 2.0);
        assertEquals(0.0, r.x(), 1e-15);
        assertEquals(1.0, r.y(), 1e-15);
    }

    @Test void rotateAroundAxis() {
        Vector3D v = new Vector3D(1.0, 0.0, 0.0);
        Vector3D axis = new Vector3D(0.0, 0.0, 1.0);
        Vector3D r = VectorMath.rotateAroundAxis(v, axis, Math.PI / 2.0);
        assertEquals(0.0, r.x(), 1e-15);
        assertEquals(1.0, r.y(), 1e-10);
        assertEquals(0.0, r.z(), 1e-15);
    }

    @Test void midpoint() {
        Vector2D a = new Vector2D(0.0, 0.0);
        Vector2D b = new Vector2D(2.0, 4.0);
        assertEquals(new Vector2D(1.0, 2.0), VectorMath.midpoint(a, b));
    }

    @Test void min() {
        Vector2D a = new Vector2D(1.0, 5.0);
        Vector2D b = new Vector2D(3.0, 2.0);
        assertEquals(new Vector2D(1.0, 2.0), VectorMath.min(a, b));
    }

    @Test void max() {
        Vector2D a = new Vector2D(1.0, 5.0);
        Vector2D b = new Vector2D(3.0, 2.0);
        assertEquals(new Vector2D(3.0, 5.0), VectorMath.max(a, b));
    }

    @Test void clamp() {
        Vector2D v = new Vector2D(-1.0, 5.0);
        Vector2D min = new Vector2D(0.0, 0.0);
        Vector2D max = new Vector2D(3.0, 3.0);
        assertEquals(new Vector2D(0.0, 3.0), VectorMath.clamp(v, min, max));
    }

    @Test void limit() {
        Vector2D v = new Vector2D(10.0, 0.0);
        Vector2D c = VectorMath.limit(v, 3.0);
        assertEquals(3.0, VectorMath.length(c), 1e-15);
    }

    @Test void setLength() {
        Vector2D v = new Vector2D(1.0, 0.0);
        assertEquals(new Vector2D(5.0, 0.0), VectorMath.setLength(v, 5.0));
    }

    @Test void tripleScalar() {
        Vector3D a = new Vector3D(1.0, 0.0, 0.0);
        Vector3D b = new Vector3D(0.0, 1.0, 0.0);
        Vector3D c = new Vector3D(0.0, 0.0, 1.0);
        assertEquals(1.0, VectorMath.tripleScalar(a, b, c), 1e-15);
    }

    @Test void tripleVector() {
        Vector3D a = new Vector3D(1.0, 2.0, 3.0);
        Vector3D b = new Vector3D(0.0, 4.0, 0.0);
        Vector3D c = new Vector3D(5.0, 0.0, 6.0);
        assertEquals(new Vector3D(-40.0, 92.0, -48.0), VectorMath.tripleVector(a, b, c));
    }

    @Test void orthonormalBasis() {
        Vector3D v = new Vector3D(1.0, 0.0, 0.0);
        Vector3D bx = VectorMath.orthonormalBasisX(v);
        Vector3D by = VectorMath.orthonormalBasisY(v);
        Vector3D bz = VectorMath.orthonormalBasisZ(v);
        assertEquals(1.0, VectorMath.length(bx), 1e-15);
        assertEquals(1.0, VectorMath.length(by), 1e-15);
        assertEquals(1.0, VectorMath.length(bz), 1e-15);
        assertEquals(0.0, VectorMath.dot(bx, by), 1e-15);
    }

    @Test void nlerp() {
        Vector3D a = new Vector3D(1.0, 0.0, 0.0);
        Vector3D b = new Vector3D(0.0, 1.0, 0.0);
        Vector3D n = VectorMath.nlerp(a, b, 0.5);
        assertEquals(1.0, VectorMath.length(n), 1e-15);
    }

    @Test void slerp() {
        Vector3D a = new Vector3D(1.0, 0.0, 0.0);
        Vector3D b = new Vector3D(0.0, 1.0, 0.0);
        Vector3D s = VectorMath.slerp(a, b, 0.5);
        assertEquals(1.0, VectorMath.length(s), 1e-15);
    }

    @Test void faceforward() {
        Vector3D n = new Vector3D(0.0, 1.0, 0.0);
        Vector3D i = new Vector3D(0.0, -1.0, 0.0);
        Vector3D nRef = new Vector3D(0.0, 1.0, 0.0);
        assertEquals(n, VectorMath.faceforward(n, i, nRef));
    }

    @Test void projectOnPlane() {
        Vector3D v = new Vector3D(1.0, 1.0, 0.0);
        Vector3D normal = new Vector3D(0.0, 1.0, 0.0);
        assertEquals(new Vector3D(1.0, 0.0, 0.0), VectorMath.projectOnPlane(v, normal));
    }

    @Test void catmullRom() {
        Vector2D r = VectorMath.catmullRom(
            new Vector2D(0,0), new Vector2D(1,1),
            new Vector2D(2,0), new Vector2D(3,1), 0.5);
        assertEquals(1.5, r.x(), 1e-15);
    }

    @Test void bezier() {
        Vector2D r = VectorMath.bezier(
            new Vector2D(0,0), new Vector2D(0,1),
            new Vector2D(1,1), new Vector2D(1,0), 0.5);
        assertEquals(0.5, r.x(), 1e-15);
        assertEquals(0.75, r.y(), 1e-15);
    }

    @Test void distanceSquared() {
        Vector2D a = new Vector2D(0,0), b = new Vector2D(3,4);
        assertEquals(25.0, VectorMath.distanceSquared(a, b), 1e-15);
    }

    @Test void isFinite() {
        assertTrue(VectorMath.isFinite(new Vector2D(1,2)));
        assertFalse(VectorMath.hasNaN(new Vector2D(1,2)));
    }

    @Test void toFromArray() {
        Vector3D v = new Vector3D(1,2,3);
        double[] arr = VectorMath.toArray3(v);
        assertArrayEquals(new double[]{1,2,3}, arr, 1e-15);
        assertEquals(v, VectorMath.fromArray3(arr, 0));
    }

    @Test void collinear() {
        assertTrue(VectorMath.isCollinear(
            new Vector2D(0,0), new Vector2D(1,1), new Vector2D(2,2)));
        assertFalse(VectorMath.isCollinear(
            new Vector2D(0,0), new Vector2D(1,0), new Vector2D(0,1)));
    }

    @Test void coplanar() {
        assertTrue(VectorMath.isCoplanar(
            new Vector3D(0,0,0), new Vector3D(1,0,0),
            new Vector3D(0,1,0), new Vector3D(1,1,0)));
    }

    @Test void orthogonal() {
        assertTrue(VectorMath.isOrthogonal(
            new Vector2D(1,0), new Vector2D(0,1)));
        assertFalse(VectorMath.isOrthogonal(
            new Vector2D(1,0), new Vector2D(1,0)));
    }

    @Test void parallel() {
        assertTrue(VectorMath.isParallel(
            new Vector2D(2,0), new Vector2D(4,0)));
        assertFalse(VectorMath.isParallel(
            new Vector2D(1,0), new Vector2D(0,1)));
    }

    @Test void closestPointOnSegment() {
        Vector2D p = new Vector2D(0, 5);
        Vector2D a = new Vector2D(0, 0);
        Vector2D b = new Vector2D(0, 10);
        assertEquals(new Vector2D(0, 5), VectorMath.closestPointOnSegment(p, a, b));
    }

    @Test void distanceToSegment() {
        Vector2D p = new Vector2D(5, 5);
        Vector2D a = new Vector2D(0, 0);
        Vector2D b = new Vector2D(0, 10);
        assertEquals(5.0, VectorMath.distanceToSegment(p, a, b), 1e-15);
    }

    @Test void componentMinMax() {
        Vector3D v = new Vector3D(3, 1, 2);
        assertEquals(1.0, VectorMath.componentMin(v), 1e-15);
        assertEquals(3.0, VectorMath.componentMax(v), 1e-15);
    }

    @Test void componentSum() {
        Vector3D v = new Vector3D(1, 2, 3);
        assertEquals(6.0, VectorMath.componentSum(v), 1e-15);
    }

    @Test void add4() {
        Vector4D a = new Vector4D(1,2,3,4);
        Vector4D b = new Vector4D(5,6,7,8);
        assertEquals(new Vector4D(6,8,10,12), VectorMath.add(a, b));
    }

    @Test void batchAdd() {
        Vector3D[] a = {new Vector3D(1,0,0), new Vector3D(0,1,0)};
        Vector3D[] b = {new Vector3D(2,0,0), new Vector3D(0,2,0)};
        Vector3D[] r = VectorMath.batchAdd(a, b);
        assertEquals(new Vector3D(3,0,0), r[0]);
        assertEquals(new Vector3D(0,3,0), r[1]);
    }

    @Test void batchDot() {
        Vector3D[] a = {new Vector3D(1,0,0), new Vector3D(0,1,0)};
        Vector3D[] b = {new Vector3D(1,0,0), new Vector3D(0,1,0)};
        assertArrayEquals(new double[]{1,1}, VectorMath.batchDot(a, b), 1e-15);
    }

    @Test void batchScale() {
        Vector3D[] v = {new Vector3D(1,0,0), new Vector3D(0,2,0)};
        Vector3D[] r = VectorMath.batchScale(v, 2);
        assertEquals(new Vector3D(2,0,0), r[0]);
        assertEquals(new Vector3D(0,4,0), r[1]);
    }

    @Test void batchNormalize() {
        Vector3D[] v = {new Vector3D(2,0,0), new Vector3D(0,3,0)};
        Vector3D[] r = VectorMath.batchNormalize(v);
        assertEquals(1.0, VectorMath.length(r[0]), 1e-15);
        assertEquals(1.0, VectorMath.length(r[1]), 1e-15);
    }

    @Test void batchNegate() {
        Vector3D[] v = {new Vector3D(1,0,0), new Vector3D(0,-2,0)};
        Vector3D[] r = VectorMath.batchNegate(v);
        assertEquals(-1.0, r[0].x(), 1e-15);
        assertEquals(0.0, r[0].y(), 1e-15);
        assertEquals(0.0, r[0].z(), 1e-15);
        assertEquals(0.0, r[1].x(), 1e-15);
        assertEquals(2.0, r[1].y(), 1e-15);
        assertEquals(0.0, r[1].z(), 1e-15);
    }

    @Test void cross2DThreePoint() {
        Vector2D a = new Vector2D(0,0);
        Vector2D b = new Vector2D(1,0);
        Vector2D c = new Vector2D(0,1);
        assertEquals(1.0, VectorMath.cross2D(a, b, c), 1e-15);
    }
}

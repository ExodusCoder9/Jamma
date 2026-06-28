package com.jamma.math.geometry;

import com.jamma.math.Vector3d;
import com.jamma.math.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    @Test void fromPointNormalZeroNormal() {
        assertEquals(new Plane(new Vector3d(0, 0, 0), 0.0), Plane.fromPointNormal(new Vector3d(1, 2, 3), new Vector3d(0, 0, 0)));
    }

    @Test void fromPointsDegenerate() {
        assertEquals(new Plane(new Vector3d(0, 0, 0), 0.0), Plane.fromPoints(new Vector3d(1, 1, 1), new Vector3d(1, 1, 1), new Vector3d(1, 1, 1)));
    }

    @Test void normalizeZeroPlane() {
        Plane p = new Plane(new Vector3d(0, 0, 0), 2.0);
        assertEquals(p, p.normalize());
    }

    @Test void planefFromPointNormalZeroNormal() {
        assertEquals(new Planef(new Vector3f(0, 0, 0), 0.0f), Planef.fromPointNormal(new Vector3f(1, 2, 3), new Vector3f(0, 0, 0)));
    }

    @Test void planefFromPointsDegenerate() {
        assertEquals(new Planef(new Vector3f(0, 0, 0), 0.0f), Planef.fromPoints(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 1)));
    }

    @Test void planefNormalizeZeroPlane() {
        Planef p = new Planef(new Vector3f(0, 0, 0), 2.0f);
        assertEquals(p, p.normalize());
    }
}

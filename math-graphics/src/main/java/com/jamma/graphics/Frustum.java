package com.jamma.graphics;

import com.jamma.math.Vector3f;
import com.jamma.math.geometry.AABBf;
import com.jamma.math.geometry.FrustumIntersection;
import com.jamma.math.matrix.Matrix4f;

import java.util.Arrays;

public final class Frustum {

    public static final int INSIDE = FrustumIntersection.INSIDE;
    public static final int OUTSIDE = FrustumIntersection.OUTSIDE;
    public static final int INTERSECT = FrustumIntersection.INTERSECT;

    public static final int LEFT = FrustumIntersection.LEFT;
    public static final int RIGHT = FrustumIntersection.RIGHT;
    public static final int BOTTOM = FrustumIntersection.BOTTOM;
    public static final int TOP = FrustumIntersection.TOP;
    public static final int NEAR = FrustumIntersection.NEAR;
    public static final int FAR = FrustumIntersection.FAR;

    private final FrustumIntersection inner;

    private Frustum(FrustumIntersection inner) {
        this.inner = inner;
    }

    public static Frustum from(Matrix4f viewProjection) {
        return new Frustum(new FrustumIntersection(viewProjection));
    }

    public static Frustum from(Camera camera) {
        return camera.frustum();
    }

    public int testAABB(AABBf box) {
        return inner.testAABB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public int testSphere(Vector3f center, float radius) {
        return inner.testSphere(center, radius);
    }

    public int testPoint(Vector3f point) {
        return inner.testPoint(point);
    }

    public double[] plane(int index) {
        return inner.plane(index);
    }

    public Vector3f[] corners() {
        var planes = inner.planes;
        Vector3f[] result = new Vector3f[8];
        result[0] = intersectPlanes(planes[LEFT], planes[BOTTOM], planes[NEAR]);
        result[1] = intersectPlanes(planes[RIGHT], planes[BOTTOM], planes[NEAR]);
        result[2] = intersectPlanes(planes[LEFT], planes[TOP], planes[NEAR]);
        result[3] = intersectPlanes(planes[RIGHT], planes[TOP], planes[NEAR]);
        result[4] = intersectPlanes(planes[LEFT], planes[BOTTOM], planes[FAR]);
        result[5] = intersectPlanes(planes[RIGHT], planes[BOTTOM], planes[FAR]);
        result[6] = intersectPlanes(planes[LEFT], planes[TOP], planes[FAR]);
        result[7] = intersectPlanes(planes[RIGHT], planes[TOP], planes[FAR]);
        return result;
    }

    private static Vector3f intersectPlanes(double[] p1, double[] p2, double[] p3) {
        double d1 = p1[3], d2 = p2[3], d3 = p3[3];
        double n1x = p1[0], n1y = p1[1], n1z = p1[2];
        double n2x = p2[0], n2y = p2[1], n2z = p2[2];
        double n3x = p3[0], n3y = p3[1], n3z = p3[2];

        double det = n1x * (n2y * n3z - n2z * n3y)
                   - n1y * (n2x * n3z - n2z * n3x)
                   + n1z * (n2x * n3y - n2y * n3x);

        if (Math.abs(det) < 1e-20) return Vector3f.ZERO;

        double inv = -1.0 / det;
        float x = (float) (inv * (d1 * (n2y * n3z - n2z * n3y)
                                + d2 * (n1z * n3y - n1y * n3z)
                                + d3 * (n1y * n2z - n1z * n2y)));
        float y = (float) (inv * (d1 * (n2z * n3x - n2x * n3z)
                                + d2 * (n1x * n3z - n1z * n3x)
                                + d3 * (n1z * n2x - n1x * n2z)));
        float z = (float) (inv * (d1 * (n2x * n3y - n2y * n3x)
                                + d2 * (n1y * n3x - n1x * n3y)
                                + d3 * (n1x * n2y - n1y * n2x)));
        return new Vector3f(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Frustum other)) return false;
        return Arrays.deepEquals(inner.planes, other.inner.planes);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(inner.planes);
    }
}

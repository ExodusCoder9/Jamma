package com.jamma.math.geometry;

import com.jamma.math.Vector3D;
import com.jamma.math.matrix.Matrix4d;

public class FrustumIntersection {
    public static final int INSIDE = 1;
    public static final int OUTSIDE = -1;
    public static final int INTERSECT = 0;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int TOP = 3;
    public static final int NEAR = 4;
    public static final int FAR = 5;

    public final double[][] planes;

    public FrustumIntersection() {
        planes = new double[6][4];
    }

    public FrustumIntersection(Matrix4d m) {
        planes = new double[6][4];
        set(m);
    }

    public FrustumIntersection set(Matrix4d m) {
        double m00 = m.m00(), m01 = m.m01(), m02 = m.m02(), m03 = m.m03();
        double m10 = m.m10(), m11 = m.m11(), m12 = m.m12(), m13 = m.m13();
        double m20 = m.m20(), m21 = m.m21(), m22 = m.m22(), m23 = m.m23();
        double m30 = m.m30(), m31 = m.m31(), m32 = m.m32(), m33 = m.m33();

        planes[LEFT][0] = m03 + m00;
        planes[LEFT][1] = m13 + m10;
        planes[LEFT][2] = m23 + m20;
        planes[LEFT][3] = m33 + m30;
        normalize(LEFT);

        planes[RIGHT][0] = m03 - m00;
        planes[RIGHT][1] = m13 - m10;
        planes[RIGHT][2] = m23 - m20;
        planes[RIGHT][3] = m33 - m30;
        normalize(RIGHT);

        planes[BOTTOM][0] = m03 + m01;
        planes[BOTTOM][1] = m13 + m11;
        planes[BOTTOM][2] = m23 + m21;
        planes[BOTTOM][3] = m33 + m31;
        normalize(BOTTOM);

        planes[TOP][0] = m03 - m01;
        planes[TOP][1] = m13 - m11;
        planes[TOP][2] = m23 - m21;
        planes[TOP][3] = m33 - m31;
        normalize(TOP);

        planes[NEAR][0] = m03 + m02;
        planes[NEAR][1] = m13 + m12;
        planes[NEAR][2] = m23 + m22;
        planes[NEAR][3] = m33 + m32;
        normalize(NEAR);

        planes[FAR][0] = m03 - m02;
        planes[FAR][1] = m13 - m12;
        planes[FAR][2] = m23 - m22;
        planes[FAR][3] = m33 - m32;
        normalize(FAR);

        return this;
    }

    private void normalize(int index) {
        double invLen = 1.0 / Math.sqrt(
            planes[index][0] * planes[index][0] +
            planes[index][1] * planes[index][1] +
            planes[index][2] * planes[index][2]
        );
        planes[index][0] *= invLen;
        planes[index][1] *= invLen;
        planes[index][2] *= invLen;
        planes[index][3] *= invLen;
    }

    public int testAABB(AABB box) {
        for (int i = 0; i < 6; i++) {
            double nx = planes[i][0];
            double ny = planes[i][1];
            double nz = planes[i][2];
            double d = planes[i][3];

            double px = nx >= 0 ? box.maxX : box.minX;
            double py = ny >= 0 ? box.maxY : box.minY;
            double pz = nz >= 0 ? box.maxZ : box.minZ;
            if (nx * px + ny * py + nz * pz + d < 0) return OUTSIDE;

            double npx = nx >= 0 ? box.minX : box.maxX;
            double npy = ny >= 0 ? box.minY : box.maxY;
            double npz = nz >= 0 ? box.minZ : box.maxZ;
            if (nx * npx + ny * npy + nz * npz + d < 0) return INTERSECT;
        }
        return INSIDE;
    }

    public int testSphere(Vector3D center, double radius) {
        for (int i = 0; i < 6; i++) {
            double dist = planes[i][0] * center.x() + planes[i][1] * center.y() + planes[i][2] * center.z() + planes[i][3];
            if (dist < -radius) return OUTSIDE;
            if (dist < radius) return INTERSECT;
        }
        return INSIDE;
    }

    public int testPoint(Vector3D point) {
        for (int i = 0; i < 6; i++) {
            double dist = planes[i][0] * point.x() + planes[i][1] * point.y() + planes[i][2] * point.z() + planes[i][3];
            if (dist < 0) return OUTSIDE;
        }
        return INSIDE;
    }

    public double[] plane(int index) {
        return planes[index];
    }
}

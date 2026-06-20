package com.jamma.math;

import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix4d;
import java.io.Serializable;

public class FrustumRayBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector3d origin;
    public Vector3d dir00;
    public Vector3d dir01;
    public Vector3d dir10;
    public Vector3d dir11;

    public FrustumRayBuilder() {
        origin = new Vector3d();
        dir00 = new Vector3d();
        dir01 = new Vector3d();
        dir10 = new Vector3d();
        dir11 = new Vector3d();
    }

    public FrustumRayBuilder set(Matrix4d invProjView) {
        double ox = invProjView.m30;
        double oy = invProjView.m31;
        double oz = invProjView.m32;
        origin = new Vector3d(ox, oy, oz);

        double[][] ndcCorners = {{-1,-1,1}, {1,-1,1}, {-1,1,1}, {1,1,1}};
        Vector3d[] rayDirs = new Vector3d[4];

        for (int i = 0; i < 4; i++) {
            double cx = ndcCorners[i][0], cy = ndcCorners[i][1], cz = ndcCorners[i][2];
            double tx = invProjView.m00 * cx + invProjView.m10 * cy + invProjView.m20 * cz + invProjView.m30;
            double ty = invProjView.m01 * cx + invProjView.m11 * cy + invProjView.m21 * cz + invProjView.m31;
            double tz = invProjView.m02 * cx + invProjView.m12 * cy + invProjView.m22 * cz + invProjView.m32;
            double tw = invProjView.m03 * cx + invProjView.m13 * cy + invProjView.m23 * cz + invProjView.m33;
            double invW = 1.0 / tw;
            double wx = tx * invW, wy = ty * invW, wz = tz * invW;
            rayDirs[i] = new Vector3d(wx - ox, wy - oy, wz - oz);
        }

        dir00 = rayDirs[0];
        dir10 = rayDirs[1];
        dir01 = rayDirs[2];
        dir11 = rayDirs[3];

        return this;
    }

    public Vector3d getRay(float x, float y, Vector3d dest) {
        float right = x;
        float left = 1 - right;
        float up = y;
        float down = 1 - up;
        double dx = left * down * dir00.x() + right * down * dir10.x() + left * up * dir01.x() + right * up * dir11.x();
        double dy = left * down * dir00.y() + right * down * dir10.y() + left * up * dir01.y() + right * up * dir11.y();
        double dz = left * down * dir00.z() + right * down * dir10.z() + left * up * dir01.z() + right * up * dir11.z();
        return new Vector3d(dx, dy, dz);
    }

    public Vector3d getRay(float x, float y) {
        return getRay(x, y, new Vector3d());
    }

    public FrustumRayBuilder normalize() {
        dir00 = dir00.normalize();
        dir10 = dir10.normalize();
        dir01 = dir01.normalize();
        dir11 = dir11.normalize();
        return this;
    }
}

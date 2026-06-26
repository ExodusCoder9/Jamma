package com.jamma.math.geometry;

import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix4d;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class AABB implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public double minX, minY, minZ, maxX, maxY, maxZ;

    public AABB() {
        minX = minY = minZ = Double.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Double.NEGATIVE_INFINITY;
    }

    public AABB(AABB other) {
        minX = other.minX;
        minY = other.minY;
        minZ = other.minZ;
        maxX = other.maxX;
        maxY = other.maxY;
        maxZ = other.maxZ;
    }

    public AABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AABB(Vector3d min, Vector3d max) {
        minX = min.x();
        minY = min.y();
        minZ = min.z();
        maxX = max.x();
        maxY = max.y();
        maxZ = max.z();
    }

    public AABB set(AABB other) {
        minX = other.minX;
        minY = other.minY;
        minZ = other.minZ;
        maxX = other.maxX;
        maxY = other.maxY;
        maxZ = other.maxZ;
        return this;
    }

    public AABB set(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public Vector3d getMin(Vector3d dest) {
        return new Vector3d(minX, minY, minZ);
    }

    public Vector3d getMax(Vector3d dest) {
        return new Vector3d(maxX, maxY, maxZ);
    }

    public Vector3d getCenter(Vector3d dest) {
        return new Vector3d((minX + maxX) * 0.5, (minY + maxY) * 0.5, (minZ + maxZ) * 0.5);
    }

    public Vector3d getExtent(Vector3d dest) {
        return new Vector3d((maxX - minX) * 0.5, (maxY - minY) * 0.5, (maxZ - minZ) * 0.5);
    }

    public AABB union(AABB other) {
        minX = Math.min(minX, other.minX);
        minY = Math.min(minY, other.minY);
        minZ = Math.min(minZ, other.minZ);
        maxX = Math.max(maxX, other.maxX);
        maxY = Math.max(maxY, other.maxY);
        maxZ = Math.max(maxZ, other.maxZ);
        return this;
    }

    public AABB union(Vector3d point) {
        minX = Math.min(minX, point.x());
        minY = Math.min(minY, point.y());
        minZ = Math.min(minZ, point.z());
        maxX = Math.max(maxX, point.x());
        maxY = Math.max(maxY, point.y());
        maxZ = Math.max(maxZ, point.z());
        return this;
    }

    public AABB intersection(AABB other) {
        minX = Math.max(minX, other.minX);
        minY = Math.max(minY, other.minY);
        minZ = Math.max(minZ, other.minZ);
        maxX = Math.min(maxX, other.maxX);
        maxY = Math.min(maxY, other.maxY);
        maxZ = Math.min(maxZ, other.maxZ);
        return this;
    }

    public boolean contains(Vector3d point) {
        return point.x() >= minX && point.x() <= maxX &&
               point.y() >= minY && point.y() <= maxY &&
               point.z() >= minZ && point.z() <= maxZ;
    }

    public boolean contains(AABB other) {
        return other.minX >= minX && other.maxX <= maxX &&
               other.minY >= minY && other.maxY <= maxY &&
               other.minZ >= minZ && other.maxZ <= maxZ;
    }

    public boolean intersects(AABB other) {
        return maxX >= other.minX && minX <= other.maxX &&
               maxY >= other.minY && minY <= other.maxY &&
               maxZ >= other.minZ && minZ <= other.maxZ;
    }

    public boolean intersectsSphere(Vector3d center, double radius) {
        double closestX = Math.clamp(center.x(), minX, maxX);
        double closestY = Math.clamp(center.y(), minY, maxY);
        double closestZ = Math.clamp(center.z(), minZ, maxZ);
        double dx = closestX - center.x();
        double dy = closestY - center.y();
        double dz = closestZ - center.z();
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    public double intersectsRay(Vector3d origin, Vector3d direction) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;
        double[] origins = {origin.x(), origin.y(), origin.z()};
        double[] dirs = {direction.x(), direction.y(), direction.z()};
        double[] mins = {minX, minY, minZ};
        double[] maxs = {maxX, maxY, maxZ};
        for (int i = 0; i < 3; i++) {
            if (Math.abs(dirs[i]) < 1e-30) {
                if (origins[i] < mins[i] || origins[i] > maxs[i]) return -1.0;
            } else {
                double t1 = (mins[i] - origins[i]) / dirs[i];
                double t2 = (maxs[i] - origins[i]) / dirs[i];
                if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
                tmin = Math.max(tmin, t1);
                tmax = Math.min(tmax, t2);
                if (tmin > tmax) return -1.0;
            }
        }
        return tmin >= 0.0 ? tmin : tmax;
    }

    public boolean isEmpty() {
        return minX > maxX || minY > maxY || minZ > maxZ;
    }

    public void clear() {
        minX = minY = minZ = Double.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Double.NEGATIVE_INFINITY;
    }

    public AABB transform(Matrix4d m) {
        double x0 = minX, y0 = minY, z0 = minZ;
        double x1 = maxX, y1 = maxY, z1 = maxZ;
        double[] xs = new double[8];
        double[] ys = new double[8];
        double[] zs = new double[8];
        int idx = 0;
        for (int i = 0; i < 2; i++) {
            double x = (i == 0) ? x0 : x1;
            for (int j = 0; j < 2; j++) {
                double y = (j == 0) ? y0 : y1;
                for (int k = 0; k < 2; k++) {
                    double z = (k == 0) ? z0 : z1;
                    double w = 1.0 / (m.m03() * x + m.m13() * y + m.m23() * z + m.m33());
                    xs[idx] = (m.m00() * x + m.m10() * y + m.m20() * z + m.m30()) * w;
                    ys[idx] = (m.m01() * x + m.m11() * y + m.m21() * z + m.m31()) * w;
                    zs[idx] = (m.m02() * x + m.m12() * y + m.m22() * z + m.m32()) * w;
                    idx++;
                }
            }
        }
        minX = minY = minZ = Double.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 8; i++) {
            minX = Math.min(minX, xs[i]);
            minY = Math.min(minY, ys[i]);
            minZ = Math.min(minZ, zs[i]);
            maxX = Math.max(maxX, xs[i]);
            maxY = Math.max(maxY, ys[i]);
            maxZ = Math.max(maxZ, zs[i]);
        }
        return this;
    }

    public Vector3d getSize(Vector3d dest) {
        return new Vector3d(maxX - minX, maxY - minY, maxZ - minZ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AABB other)) return false;
        return Double.doubleToLongBits(minX) == Double.doubleToLongBits(other.minX) &&
               Double.doubleToLongBits(minY) == Double.doubleToLongBits(other.minY) &&
               Double.doubleToLongBits(minZ) == Double.doubleToLongBits(other.minZ) &&
               Double.doubleToLongBits(maxX) == Double.doubleToLongBits(other.maxX) &&
               Double.doubleToLongBits(maxY) == Double.doubleToLongBits(other.maxY) &&
               Double.doubleToLongBits(maxZ) == Double.doubleToLongBits(other.maxZ);
    }

    @Override
    public String toString() {
        return "AABB[min=(" + minX + "," + minY + "," + minZ + "), max=(" + maxX + "," + maxY + "," + maxZ + ")]";
    }
}

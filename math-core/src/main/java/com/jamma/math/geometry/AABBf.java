package com.jamma.math.geometry;

import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix4f;

import java.io.Serial;
import java.io.Serializable;

public class AABBf implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public float minX, minY, minZ, maxX, maxY, maxZ;

    public AABBf() {
        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
    }

    public AABBf(AABBf other) {
        minX = other.minX;
        minY = other.minY;
        minZ = other.minZ;
        maxX = other.maxX;
        maxY = other.maxY;
        maxZ = other.maxZ;
    }

    public AABBf(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AABBf(Vector3f min, Vector3f max) {
        minX = min.x();
        minY = min.y();
        minZ = min.z();
        maxX = max.x();
        maxY = max.y();
        maxZ = max.z();
    }

    public AABBf set(AABBf other) {
        minX = other.minX;
        minY = other.minY;
        minZ = other.minZ;
        maxX = other.maxX;
        maxY = other.maxY;
        maxZ = other.maxZ;
        return this;
    }

    public AABBf set(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public Vector3f getMin(Vector3f dest) {
        return new Vector3f(minX, minY, minZ);
    }

    public Vector3f getMax(Vector3f dest) {
        return new Vector3f(maxX, maxY, maxZ);
    }

    public Vector3f getCenter(Vector3f dest) {
        return new Vector3f((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, (minZ + maxZ) * 0.5f);
    }

    public Vector3f getExtent(Vector3f dest) {
        return new Vector3f((maxX - minX) * 0.5f, (maxY - minY) * 0.5f, (maxZ - minZ) * 0.5f);
    }

    public AABBf union(AABBf other) {
        minX = Math.min(minX, other.minX);
        minY = Math.min(minY, other.minY);
        minZ = Math.min(minZ, other.minZ);
        maxX = Math.max(maxX, other.maxX);
        maxY = Math.max(maxY, other.maxY);
        maxZ = Math.max(maxZ, other.maxZ);
        return this;
    }

    public AABBf union(Vector3f point) {
        minX = Math.min(minX, point.x());
        minY = Math.min(minY, point.y());
        minZ = Math.min(minZ, point.z());
        maxX = Math.max(maxX, point.x());
        maxY = Math.max(maxY, point.y());
        maxZ = Math.max(maxZ, point.z());
        return this;
    }

    public AABBf intersection(AABBf other) {
        minX = Math.max(minX, other.minX);
        minY = Math.max(minY, other.minY);
        minZ = Math.max(minZ, other.minZ);
        maxX = Math.min(maxX, other.maxX);
        maxY = Math.min(maxY, other.maxY);
        maxZ = Math.min(maxZ, other.maxZ);
        return this;
    }

    public boolean contains(Vector3f point) {
        return point.x() >= minX && point.x() <= maxX &&
               point.y() >= minY && point.y() <= maxY &&
               point.z() >= minZ && point.z() <= maxZ;
    }

    public boolean contains(AABBf other) {
        return other.minX >= minX && other.maxX <= maxX &&
               other.minY >= minY && other.maxY <= maxY &&
               other.minZ >= minZ && other.maxZ <= maxZ;
    }

    public boolean intersects(AABBf other) {
        return maxX >= other.minX && minX <= other.maxX &&
               maxY >= other.minY && minY <= other.maxY &&
               maxZ >= other.minZ && minZ <= other.maxZ;
    }

    public boolean intersectsSphere(Vector3f center, float radius) {
        float closestX = Math.max(minX, Math.min(center.x(), maxX));
        float closestY = Math.max(minY, Math.min(center.y(), maxY));
        float closestZ = Math.max(minZ, Math.min(center.z(), maxZ));
        float dx = closestX - center.x();
        float dy = closestY - center.y();
        float dz = closestZ - center.z();
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    public float intersectsRay(Vector3f origin, Vector3f direction) {
        float tmin = Float.NEGATIVE_INFINITY;
        float tmax = Float.POSITIVE_INFINITY;
        float[] origins = {origin.x(), origin.y(), origin.z()};
        float[] dirs = {direction.x(), direction.y(), direction.z()};
        float[] mins = {minX, minY, minZ};
        float[] maxs = {maxX, maxY, maxZ};
        for (int i = 0; i < 3; i++) {
            if (Math.abs(dirs[i]) < 1e-30f) {
                if (origins[i] < mins[i] || origins[i] > maxs[i]) return -1.0f;
            } else {
                float t1 = (mins[i] - origins[i]) / dirs[i];
                float t2 = (maxs[i] - origins[i]) / dirs[i];
                if (t1 > t2) { float tmp = t1; t1 = t2; t2 = tmp; }
                tmin = Math.max(tmin, t1);
                tmax = Math.min(tmax, t2);
                if (tmin > tmax) return -1.0f;
            }
        }
        return tmin >= 0.0f ? tmin : tmax;
    }

    public boolean isEmpty() {
        return minX > maxX || minY > maxY || minZ > maxZ;
    }

    public void clear() {
        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
    }

    public AABBf transform(Matrix4f m) {
        float x0 = minX, y0 = minY, z0 = minZ;
        float x1 = maxX, y1 = maxY, z1 = maxZ;
        float[] xs = new float[8];
        float[] ys = new float[8];
        float[] zs = new float[8];
        int idx = 0;
        for (int i = 0; i < 2; i++) {
            float x = (i == 0) ? x0 : x1;
            for (int j = 0; j < 2; j++) {
                float y = (j == 0) ? y0 : y1;
                for (int k = 0; k < 2; k++) {
                    float z = (k == 0) ? z0 : z1;
                    float w = 1.0f / (m.m03() * x + m.m13() * y + m.m23() * z + m.m33());
                    xs[idx] = (m.m00() * x + m.m10() * y + m.m20() * z + m.m30()) * w;
                    ys[idx] = (m.m01() * x + m.m11() * y + m.m21() * z + m.m31()) * w;
                    zs[idx] = (m.m02() * x + m.m12() * y + m.m22() * z + m.m32()) * w;
                    idx++;
                }
            }
        }
        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
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

    public Vector3f getSize(Vector3f dest) {
        return new Vector3f(maxX - minX, maxY - minY, maxZ - minZ);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(minX);
        result = prime * result + Float.floatToIntBits(minY);
        result = prime * result + Float.floatToIntBits(minZ);
        result = prime * result + Float.floatToIntBits(maxX);
        result = prime * result + Float.floatToIntBits(maxY);
        result = prime * result + Float.floatToIntBits(maxZ);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AABBf other)) return false;
        return Float.floatToIntBits(minX) == Float.floatToIntBits(other.minX) &&
               Float.floatToIntBits(minY) == Float.floatToIntBits(other.minY) &&
               Float.floatToIntBits(minZ) == Float.floatToIntBits(other.minZ) &&
               Float.floatToIntBits(maxX) == Float.floatToIntBits(other.maxX) &&
               Float.floatToIntBits(maxY) == Float.floatToIntBits(other.maxY) &&
               Float.floatToIntBits(maxZ) == Float.floatToIntBits(other.maxZ);
    }

    @Override
    public String toString() {
        return "AABBf[min=(" + minX + "," + minY + "," + minZ + "), max=(" + maxX + "," + maxY + "," + maxZ + ")]";
    }
}

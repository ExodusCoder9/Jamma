package com.jamma.math.geometry;

import com.jamma.math.Vector3d;

public final class Intersection {

    private Intersection() {
        // Prevent instantiation
    }

    /**
     * Test if a ray intersects a triangle using the Möller-Trumbore algorithm.
     *
     * @param r                  the ray
     * @param v0                 the first vertex of the triangle
     * @param v1                 the second vertex of the triangle
     * @param v2                 the third vertex of the triangle
     * @param intersectionPoint  a 1-element array to store the intersection point (optional)
     * @return true if the ray intersects the triangle, false otherwise
     */
    public static boolean testRayTriangle(Ray r, Vector3d v0, Vector3d v1, Vector3d v2, Vector3d[] intersectionPoint) {
        double edge1x = v1.x() - v0.x();
        double edge1y = v1.y() - v0.y();
        double edge1z = v1.z() - v0.z();
        double edge2x = v2.x() - v0.x();
        double edge2y = v2.y() - v0.y();
        double edge2z = v2.z() - v0.z();

        double hx = r.direction.y() * edge2z - r.direction.z() * edge2y;
        double hy = r.direction.z() * edge2x - r.direction.x() * edge2z;
        double hz = r.direction.x() * edge2y - r.direction.y() * edge2x;

        double a = edge1x * hx + edge1y * hy + edge1z * hz;
        if (a > -1e-15 && a < 1e-15) {
            return false;
        }

        double f = 1.0 / a;
        double sx = r.origin.x() - v0.x();
        double sy = r.origin.y() - v0.y();
        double sz = r.origin.z() - v0.z();

        double u = f * (sx * hx + sy * hy + sz * hz);
        if (u < 0.0 || u > 1.0) {
            return false;
        }

        double qx = sy * edge1z - sz * edge1y;
        double qy = sz * edge1x - sx * edge1z;
        double qz = sx * edge1y - sy * edge1x;

        double v = f * (r.direction.x() * qx + r.direction.y() * qy + r.direction.z() * qz);
        if (v < 0.0 || u + v > 1.0) {
            return false;
        }

        double t = f * (edge2x * qx + edge2y * qy + edge2z * qz);
        if (t > 1e-15) {
            if (intersectionPoint != null && intersectionPoint.length > 0) {
                intersectionPoint[0] = new Vector3d(
                    r.origin.x() + t * r.direction.x(),
                    r.origin.y() + t * r.direction.y(),
                    r.origin.z() + t * r.direction.z()
                );
            }
            return true;
        }
        return false;
    }

    /**
     * Test if a ray intersects an AABB.
     *
     * @param r               the ray
     * @param box             the axis-aligned bounding box
     * @param resultNearFar   a 2-element array to store the tMin and tMax distances (optional)
     * @return true if the ray intersects the box, false otherwise
     */
    public static boolean testRayAABB(Ray r, AABB box, double[] resultNearFar) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;

        double ox = r.origin.x(), oy = r.origin.y(), oz = r.origin.z();
        double dx = r.direction.x(), dy = r.direction.y(), dz = r.direction.z();

        // X slab
        if (Math.abs(dx) < 1e-15) {
            if (ox < box.minX || ox > box.maxX) return false;
        } else {
            double invD = 1.0 / dx;
            double t1 = (box.minX - ox) * invD;
            double t2 = (box.maxX - ox) * invD;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tmin = Math.max(tmin, t1);
            tmax = Math.min(tmax, t2);
            if (tmin > tmax) return false;
        }

        // Y slab
        if (Math.abs(dy) < 1e-15) {
            if (oy < box.minY || oy > box.maxY) return false;
        } else {
            double invD = 1.0 / dy;
            double t1 = (box.minY - oy) * invD;
            double t2 = (box.maxY - oy) * invD;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tmin = Math.max(tmin, t1);
            tmax = Math.min(tmax, t2);
            if (tmin > tmax) return false;
        }

        // Z slab
        if (Math.abs(dz) < 1e-15) {
            if (oz < box.minZ || oz > box.maxZ) return false;
        } else {
            double invD = 1.0 / dz;
            double t1 = (box.minZ - oz) * invD;
            double t2 = (box.maxZ - oz) * invD;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tmin = Math.max(tmin, t1);
            tmax = Math.min(tmax, t2);
            if (tmin > tmax) return false;
        }

        if (tmax < 0.0) return false;

        if (resultNearFar != null && resultNearFar.length >= 2) {
            resultNearFar[0] = tmin;
            resultNearFar[1] = tmax;
        }
        return true;
    }

    /**
     * Test if two axis-aligned bounding boxes overlap.
     *
     * @param a the first AABB
     * @param b the second AABB
     * @return true if they overlap, false otherwise
     */
    public static boolean testAABBAABB(AABB a, AABB b) {
        return a.intersects(b);
    }

    /**
     * Test if two spheres intersect.
     *
     * @param c0 the center of the first sphere
     * @param r0 the radius of the first sphere
     * @param c1 the center of the second sphere
     * @param r1 the radius of the second sphere
     * @return true if they intersect, false otherwise
     */
    public static boolean testSphereSphere(Vector3d c0, double r0, Vector3d c1, double r1) {
        double dx = c0.x() - c1.x();
        double dy = c0.y() - c1.y();
        double dz = c0.z() - c1.z();
        double distSq = dx * dx + dy * dy + dz * dz;
        double radiusSum = r0 + r1;
        return distSq <= radiusSum * radiusSum;
    }
}

package com.jamma.math.geometry;

import com.jamma.math.Vector3d;
import com.jamma.math.matrix.Matrix4d;
import java.util.ArrayList;

public final class Intersectiond {

    private Intersectiond() {}

    public static boolean testRayTriangle(Ray r, Vector3d v0, Vector3d v1, Vector3d v2, double[] t) {
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
        if (a > -1e-15 && a < 1e-15) return false;
        double f = 1.0 / a;
        double sx = r.origin.x() - v0.x();
        double sy = r.origin.y() - v0.y();
        double sz = r.origin.z() - v0.z();
        double u = f * (sx * hx + sy * hy + sz * hz);
        if (u < 0.0 || u > 1.0) return false;
        double qx = sy * edge1z - sz * edge1y;
        double qy = sz * edge1x - sx * edge1z;
        double qz = sx * edge1y - sy * edge1x;
        double v = f * (r.direction.x() * qx + r.direction.y() * qy + r.direction.z() * qz);
        if (v < 0.0 || u + v > 1.0) return false;
        double tv = f * (edge2x * qx + edge2y * qy + edge2z * qz);
        if (tv > 1e-15) {
            if (t != null && t.length > 0) t[0] = tv;
            return true;
        }
        return false;
    }

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
        if (a > -1e-15 && a < 1e-15) return false;
        double f = 1.0 / a;
        double sx = r.origin.x() - v0.x();
        double sy = r.origin.y() - v0.y();
        double sz = r.origin.z() - v0.z();
        double u = f * (sx * hx + sy * hy + sz * hz);
        if (u < 0.0 || u > 1.0) return false;
        double qx = sy * edge1z - sz * edge1y;
        double qy = sz * edge1x - sx * edge1z;
        double qz = sx * edge1y - sy * edge1x;
        double v = f * (r.direction.x() * qx + r.direction.y() * qy + r.direction.z() * qz);
        if (v < 0.0 || u + v > 1.0) return false;
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

    public static boolean testRayAABB(Ray r, AABB box, double[] tMin, double[] tMax) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;
        double ox = r.origin.x(), oy = r.origin.y(), oz = r.origin.z();
        double dx = r.direction.x(), dy = r.direction.y(), dz = r.direction.z();
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
        if (tMin != null && tMin.length >= 1) tMin[0] = tmin;
        if (tMax != null && tMax.length >= 1) tMax[0] = tmax;
        return true;
    }

    public static boolean testRayQuad(Ray r, Vector3d v0, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d[] intersectionPoint) {
        if (testRayTriangle(r, v0, v1, v2, intersectionPoint)) return true;
        return testRayTriangle(r, v0, v2, v3, intersectionPoint);
    }

    public static boolean testRaySphere(Ray r, Vector3d center, double radius, double[] t) {
        double dx = r.origin.x() - center.x();
        double dy = r.origin.y() - center.y();
        double dz = r.origin.z() - center.z();
        double a = r.direction.x() * r.direction.x() + r.direction.y() * r.direction.y() + r.direction.z() * r.direction.z();
        if (a == 0.0) return false;
        double b = 2.0 * (dx * r.direction.x() + dy * r.direction.y() + dz * r.direction.z());
        double c = dx * dx + dy * dy + dz * dz - radius * radius;
        double disc = b * b - 4.0 * a * c;
        if (disc < 0.0) return false;
        double sqrtDisc = Math.sqrt(disc);
        double t1 = (-b - sqrtDisc) / (2.0 * a);
        double t2 = (-b + sqrtDisc) / (2.0 * a);
        double result;
        if (t1 >= 0.0) result = t1;
        else if (t2 >= 0.0) result = t2;
        else return false;
        if (t != null && t.length > 0) t[0] = result;
        return true;
    }

    public static boolean testRaySphere(Ray r, Sphere sphere, double[] t) {
        return testRaySphere(r, sphere.center(), sphere.radius(), t);
    }

    public static boolean testRaySphere(Ray r, Vector3d center, double radius, Vector3d[] intersectionPoint) {
        double dx = r.origin.x() - center.x();
        double dy = r.origin.y() - center.y();
        double dz = r.origin.z() - center.z();
        double a = r.direction.x() * r.direction.x() + r.direction.y() * r.direction.y() + r.direction.z() * r.direction.z();
        if (a == 0.0) return false;
        double b = 2.0 * (dx * r.direction.x() + dy * r.direction.y() + dz * r.direction.z());
        double c = dx * dx + dy * dy + dz * dz - radius * radius;
        double disc = b * b - 4.0 * a * c;
        if (disc < 0.0) return false;
        double sqrtDisc = Math.sqrt(disc);
        double t1 = (-b - sqrtDisc) / (2.0 * a);
        double t2 = (-b + sqrtDisc) / (2.0 * a);
        double t;
        if (t1 >= 0.0) t = t1;
        else if (t2 >= 0.0) t = t2;
        else return false;
        if (intersectionPoint != null && intersectionPoint.length > 0) {
            intersectionPoint[0] = new Vector3d(
                r.origin.x() + t * r.direction.x(),
                r.origin.y() + t * r.direction.y(),
                r.origin.z() + t * r.direction.z()
            );
        }
        return true;
    }
    public static boolean testRayPlane(Ray r, Plane p, double[] t) {
        double nx = p.normal().x(), ny = p.normal().y(), nz = p.normal().z();
        double dot = nx * r.direction.x() + ny * r.direction.y() + nz * r.direction.z();
        if (Math.abs(dot) < 1e-15) return false;
        double tv = -(nx * r.origin.x() + ny * r.origin.y() + nz * r.origin.z() + p.d()) / dot;
        if (tv < 0.0) return false;
        if (t != null && t.length > 0) t[0] = tv;
        return true;
    }

    public static boolean testRayPlane(Ray r, Plane p, Vector3d[] intersectionPoint) {
        double nx = p.normal().x(), ny = p.normal().y(), nz = p.normal().z();
        double dot = nx * r.direction.x() + ny * r.direction.y() + nz * r.direction.z();
        if (Math.abs(dot) < 1e-15) return false;
        double t = -(nx * r.origin.x() + ny * r.origin.y() + nz * r.origin.z() + p.d()) / dot;
        if (t < 0.0) return false;
        if (intersectionPoint != null && intersectionPoint.length > 0) {
            intersectionPoint[0] = new Vector3d(
                r.origin.x() + t * r.direction.x(),
                r.origin.y() + t * r.direction.y(),
                r.origin.z() + t * r.direction.z()
            );
        }
        return true;
    }

    public static boolean testRayCircle(Ray r, Vector3d center, double radius, Vector3d planeNormal, Vector3d[] intersectionPoint) {
        double nx = planeNormal.x(), ny = planeNormal.y(), nz = planeNormal.z();
        double dot = nx * r.direction.x() + ny * r.direction.y() + nz * r.direction.z();
        if (Math.abs(dot) < 1e-15) return false;
        double t = -(nx * (r.origin.x() - center.x()) + ny * (r.origin.y() - center.y()) + nz * (r.origin.z() - center.z())) / dot;
        if (t < 0.0) return false;
        double px = r.origin.x() + t * r.direction.x();
        double py = r.origin.y() + t * r.direction.y();
        double pz = r.origin.z() + t * r.direction.z();
        double ddx = px - center.x();
        double ddy = py - center.y();
        double ddz = pz - center.z();
        if (ddx * ddx + ddy * ddy + ddz * ddz > radius * radius) return false;
        if (intersectionPoint != null && intersectionPoint.length > 0) {
            intersectionPoint[0] = new Vector3d(px, py, pz);
        }
        return true;
    }

    public static boolean testRayDisc(Ray r, Vector3d center, double radius, Vector3d planeNormal, Vector3d[] intersectionPoint) {
        double nx = planeNormal.x(), ny = planeNormal.y(), nz = planeNormal.z();
        double dot = nx * r.direction.x() + ny * r.direction.y() + nz * r.direction.z();
        if (Math.abs(dot) < 1e-15) return false;
        double t = -(nx * (r.origin.x() - center.x()) + ny * (r.origin.y() - center.y()) + nz * (r.origin.z() - center.z())) / dot;
        if (t < 0.0) return false;
        double px = r.origin.x() + t * r.direction.x();
        double py = r.origin.y() + t * r.direction.y();
        double pz = r.origin.z() + t * r.direction.z();
        double ddx = px - center.x();
        double ddy = py - center.y();
        double ddz = pz - center.z();
        if (ddx * ddx + ddy * ddy + ddz * ddz > radius * radius) return false;
        if (intersectionPoint != null && intersectionPoint.length > 0) {
            intersectionPoint[0] = new Vector3d(px, py, pz);
        }
        return true;
    }

    public static boolean testLineSegmentTriangle(Vector3d p0, Vector3d p1, Vector3d v0, Vector3d v1, Vector3d v2, Vector3d[] intersectionPoint) {
        double dirx = p1.x() - p0.x();
        double diry = p1.y() - p0.y();
        double dirz = p1.z() - p0.z();
        double edge1x = v1.x() - v0.x();
        double edge1y = v1.y() - v0.y();
        double edge1z = v1.z() - v0.z();
        double edge2x = v2.x() - v0.x();
        double edge2y = v2.y() - v0.y();
        double edge2z = v2.z() - v0.z();
        double hx = diry * edge2z - dirz * edge2y;
        double hy = dirz * edge2x - dirx * edge2z;
        double hz = dirx * edge2y - diry * edge2x;
        double a = edge1x * hx + edge1y * hy + edge1z * hz;
        if (a > -1e-15 && a < 1e-15) return false;
        double f = 1.0 / a;
        double sx = p0.x() - v0.x();
        double sy = p0.y() - v0.y();
        double sz = p0.z() - v0.z();
        double u = f * (sx * hx + sy * hy + sz * hz);
        if (u < 0.0 || u > 1.0) return false;
        double qx = sy * edge1z - sz * edge1y;
        double qy = sz * edge1x - sx * edge1z;
        double qz = sx * edge1y - sy * edge1x;
        double v = f * (dirx * qx + diry * qy + dirz * qz);
        if (v < 0.0 || u + v > 1.0) return false;
        double t = f * (edge2x * qx + edge2y * qy + edge2z * qz);
        if (t >= 0.0 && t <= 1.0) {
            if (intersectionPoint != null && intersectionPoint.length > 0) {
                intersectionPoint[0] = new Vector3d(
                    p0.x() + t * dirx, p0.y() + t * diry, p0.z() + t * dirz
                );
            }
            return true;
        }
        return false;
    }

    public static boolean testLineSegmentAABB(Vector3d p0, Vector3d p1, AABB box, Vector3d[] intersectionPoint) {
        double dx = p1.x() - p0.x();
        double dy = p1.y() - p0.y();
        double dz = p1.z() - p0.z();
        double tmin = 0.0;
        double tmax = 1.0;
        if (Math.abs(dx) < 1e-15) {
            if (p0.x() < box.minX || p0.x() > box.maxX) return false;
        } else {
            double invD = 1.0 / dx;
            double t1 = (box.minX - p0.x()) * invD;
            double t2 = (box.maxX - p0.x()) * invD;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tmin = Math.max(tmin, t1);
            tmax = Math.min(tmax, t2);
            if (tmin > tmax) return false;
        }
        if (Math.abs(dy) < 1e-15) {
            if (p0.y() < box.minY || p0.y() > box.maxY) return false;
        } else {
            double invD = 1.0 / dy;
            double t1 = (box.minY - p0.y()) * invD;
            double t2 = (box.maxY - p0.y()) * invD;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tmin = Math.max(tmin, t1);
            tmax = Math.min(tmax, t2);
            if (tmin > tmax) return false;
        }
        if (Math.abs(dz) < 1e-15) {
            if (p0.z() < box.minZ || p0.z() > box.maxZ) return false;
        } else {
            double invD = 1.0 / dz;
            double t1 = (box.minZ - p0.z()) * invD;
            double t2 = (box.maxZ - p0.z()) * invD;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tmin = Math.max(tmin, t1);
            tmax = Math.min(tmax, t2);
            if (tmin > tmax) return false;
        }
        if (intersectionPoint != null && intersectionPoint.length > 0) {
            double t = tmin >= 0.0 ? tmin : tmax;
            intersectionPoint[0] = new Vector3d(p0.x() + t * dx, p0.y() + t * dy, p0.z() + t * dz);
        }
        return true;
    }

    public static double distancePointTriangle(Vector3d point, Vector3d v0, Vector3d v1, Vector3d v2) {
        Vector3d closest = closestPointTriangle(point, v0, v1, v2);
        double dx = point.x() - closest.x();
        double dy = point.y() - closest.y();
        double dz = point.z() - closest.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Vector3d closestPointTriangle(Vector3d point, Vector3d v0, Vector3d v1, Vector3d v2, Vector3d dest) {
        return closestPointTriangle(point, v0, v1, v2);
    }

    public static Vector3d closestPointTriangle(Vector3d p, Vector3d a, Vector3d b, Vector3d c) {
        double abx = b.x() - a.x(), aby = b.y() - a.y(), abz = b.z() - a.z();
        double acx = c.x() - a.x(), acy = c.y() - a.y(), acz = c.z() - a.z();
        double apx = p.x() - a.x(), apy = p.y() - a.y(), apz = p.z() - a.z();
        double d1 = abx * apx + aby * apy + abz * apz;
        double d2 = acx * apx + acy * apy + acz * apz;
        if (d1 <= 0.0 && d2 <= 0.0) return a;
        double bpx = p.x() - b.x(), bpy = p.y() - b.y(), bpz = p.z() - b.z();
        double d3 = abx * bpx + aby * bpy + abz * bpz;
        double d4 = acx * bpx + acy * bpy + acz * bpz;
        if (d3 >= 0.0 && d4 <= d3) return b;
        double vc = d1 * d4 - d3 * d2;
        if (vc <= 0.0 && d1 >= 0.0 && d3 <= 0.0) {
            double v = d1 / (d1 - d3);
            return new Vector3d(a.x() + v * abx, a.y() + v * aby, a.z() + v * abz);
        }
        double cpx = p.x() - c.x(), cpy = p.y() - c.y(), cpz = p.z() - c.z();
        double d5 = abx * cpx + aby * cpy + abz * cpz;
        double d6 = acx * cpx + acy * cpy + acz * cpz;
        if (d6 >= 0.0 && d5 <= d6) return c;
        double vb = d5 * d2 - d1 * d6;
        if (vb <= 0.0 && d2 >= 0.0 && d6 <= 0.0) {
            double w = d2 / (d2 - d6);
            return new Vector3d(a.x() + w * acx, a.y() + w * acy, a.z() + w * acz);
        }
        double va = d3 * d6 - d5 * d4;
        if (va <= 0.0 && d4 - d3 >= 0.0 && d5 - d6 >= 0.0) {
            double w = (d4 - d3) / ((d4 - d3) + (d5 - d6));
            return new Vector3d(b.x() + w * (c.x() - b.x()), b.y() + w * (c.y() - b.y()), b.z() + w * (c.z() - b.z()));
        }
        double denom = 1.0 / (va + vb + vc);
        double v = vb * denom;
        double w = vc * denom;
        return new Vector3d(a.x() + v * abx + w * acx, a.y() + v * aby + w * acy, a.z() + v * abz + w * acz);
    }
    public static double distancePointLine(Vector3d point, Vector3d p0, Vector3d p1) {
        double abx = p1.x() - p0.x();
        double aby = p1.y() - p0.y();
        double abz = p1.z() - p0.z();
        double apx = point.x() - p0.x();
        double apy = point.y() - p0.y();
        double apz = point.z() - p0.z();
        double dot = apx * abx + apy * aby + apz * abz;
        double lenSq = abx * abx + aby * aby + abz * abz;
        double t = dot / lenSq;
        t = Math.max(0.0, Math.min(1.0, t));
        double cx = p0.x() + t * abx;
        double cy = p0.y() + t * aby;
        double cz = p0.z() + t * abz;
        double dx = point.x() - cx;
        double dy = point.y() - cy;
        double dz = point.z() - cz;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Vector3d closestPointLine(Vector3d point, Vector3d p0, Vector3d p1) {
        double abx = p1.x() - p0.x();
        double aby = p1.y() - p0.y();
        double abz = p1.z() - p0.z();
        double apx = point.x() - p0.x();
        double apy = point.y() - p0.y();
        double apz = point.z() - p0.z();
        double dot = apx * abx + apy * aby + apz * abz;
        double lenSq = abx * abx + aby * aby + abz * abz;
        if (lenSq == 0.0) return p0;
        double t = Math.max(0.0, Math.min(1.0, dot / lenSq));
        return new Vector3d(p0.x() + t * abx, p0.y() + t * aby, p0.z() + t * abz);
    }

    public static double distancePointPlane(Vector3d point, Plane p) {
        return Math.abs(p.normal().x() * point.x() + p.normal().y() * point.y() + p.normal().z() * point.z() + p.d());
    }

    public static double distancePointPlane(Vector3d point, double nx, double ny, double nz, double d) {
        return Math.abs(nx * point.x() + ny * point.y() + nz * point.z() + d);
    }

    public static double signedDistancePointPlane(Vector3d point, double nx, double ny, double nz, double d) {
        return nx * point.x() + ny * point.y() + nz * point.z() + d;
    }

    public static Vector3d closestPointPlane(Vector3d point, Plane p) {
        double nx = p.normal().x(), ny = p.normal().y(), nz = p.normal().z();
        double dist = nx * point.x() + ny * point.y() + nz * point.z() + p.d();
        return new Vector3d(point.x() - dist * nx, point.y() - dist * ny, point.z() - dist * nz);
    }

    public static Vector3d closestPointPlane(Vector3d point, double nx, double ny, double nz, double d) {
        double dist = nx * point.x() + ny * point.y() + nz * point.z() + d;
        return new Vector3d(point.x() - dist * nx, point.y() - dist * ny, point.z() - dist * nz);
    }

    public static boolean testSphereTriangle(Vector3d center, double radius, Vector3d v0, Vector3d v1, Vector3d v2) {
        Vector3d closest = closestPointTriangle(center, v0, v1, v2);
        double dx = center.x() - closest.x();
        double dy = center.y() - closest.y();
        double dz = center.z() - closest.z();
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    public static boolean testSphereAABB(Vector3d center, double radius, AABB box) {
        double closestX = Math.max(box.minX, Math.min(center.x(), box.maxX));
        double closestY = Math.max(box.minY, Math.min(center.y(), box.maxY));
        double closestZ = Math.max(box.minZ, Math.min(center.z(), box.maxZ));
        double dx = closestX - center.x();
        double dy = closestY - center.y();
        double dz = closestZ - center.z();
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    public static boolean testSpherePlane(Vector3d center, double radius, Plane p) {
        double dist = Math.abs(p.normal().x() * center.x() + p.normal().y() * center.y() + p.normal().z() * center.z() + p.d());
        return dist <= radius;
    }

    public static boolean testSphereFrustum(Vector3d center, double radius, Matrix4d projectionMatrix) {
        FrustumIntersection frustum = new FrustumIntersection(projectionMatrix);
        return frustum.testSphere(center, radius) != FrustumIntersection.OUTSIDE;
    }

    public static boolean testAABBAABB(AABB a, AABB b) {
        return a.intersects(b);
    }

    public static boolean testAABBPlane(AABB box, Plane p) {
        double nx = p.normal().x(), ny = p.normal().y(), nz = p.normal().z();
        double px = nx >= 0.0 ? box.maxX : box.minX;
        double py = ny >= 0.0 ? box.maxY : box.minY;
        double pz = nz >= 0.0 ? box.maxZ : box.minZ;
        if (nx * px + ny * py + nz * pz + p.d() < 0.0) return false;
        double nx2 = nx >= 0.0 ? box.minX : box.maxX;
        double ny2 = ny >= 0.0 ? box.minY : box.maxY;
        double nz2 = nz >= 0.0 ? box.minZ : box.maxZ;
        return nx * nx2 + ny * ny2 + nz * nz2 + p.d() >= 0.0;
    }

    public static boolean testAABBFrustum(AABB box, Matrix4d projectionMatrix) {
        FrustumIntersection frustum = new FrustumIntersection(projectionMatrix);
        return frustum.testAABB(box) != FrustumIntersection.OUTSIDE;
    }

    public static double intersectRayTriangle(Ray r, Vector3d v0, Vector3d v1, Vector3d v2, double epsilon) {
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
        if (a > -epsilon && a < epsilon) return -1.0;
        double f = 1.0 / a;
        double sx = r.origin.x() - v0.x();
        double sy = r.origin.y() - v0.y();
        double sz = r.origin.z() - v0.z();
        double u = f * (sx * hx + sy * hy + sz * hz);
        if (u < 0.0 || u > 1.0) return -1.0;
        double qx = sy * edge1z - sz * edge1y;
        double qy = sz * edge1x - sx * edge1z;
        double qz = sx * edge1y - sy * edge1x;
        double v = f * (r.direction.x() * qx + r.direction.y() * qy + r.direction.z() * qz);
        if (v < 0.0 || u + v > 1.0) return -1.0;
        double t = f * (edge2x * qx + edge2y * qy + edge2z * qz);
        if (t > epsilon) return t;
        return -1.0;
    }

    public static double intersectRaySphere(Ray r, Vector3d center, double radius) {
        double dx = r.origin.x() - center.x();
        double dy = r.origin.y() - center.y();
        double dz = r.origin.z() - center.z();
        double a = r.direction.x() * r.direction.x() + r.direction.y() * r.direction.y() + r.direction.z() * r.direction.z();
        double b = 2.0 * (dx * r.direction.x() + dy * r.direction.y() + dz * r.direction.z());
        double c = dx * dx + dy * dy + dz * dz - radius * radius;
        double disc = b * b - 4.0 * a * c;
        if (disc < 0.0) return -1.0;
        double sqrtDisc = Math.sqrt(disc);
        double t1 = (-b - sqrtDisc) / (2.0 * a);
        double t2 = (-b + sqrtDisc) / (2.0 * a);
        if (t1 >= 0.0) return t1;
        if (t2 >= 0.0) return t2;
        return -1.0;
    }

    public static double intersectRayPlane(Ray r, double nx, double ny, double nz, double d) {
        double dot = nx * r.direction.x() + ny * r.direction.y() + nz * r.direction.z();
        if (Math.abs(dot) < 1e-15) return Double.NaN;
        double t = -(nx * r.origin.x() + ny * r.origin.y() + nz * r.origin.z() + d) / dot;
        if (t >= 0.0) return t;
        return Double.NaN;
    }

    public static double intersectRayPlane(Ray r, Plane p) {
        return intersectRayPlane(r, p.normal().x(), p.normal().y(), p.normal().z(), p.d());
    }
    public static Vector3d intersectPlanePlane(Plane p0, Plane p1, Vector3d dest) {
        return intersectPlanePlane(p0, p1);
    }

    public static Vector3d intersectPlanePlane(Plane p0, Plane p1) {
        double n1x = p0.normal().x(), n1y = p0.normal().y(), n1z = p0.normal().z();
        double n2x = p1.normal().x(), n2y = p1.normal().y(), n2z = p1.normal().z();
        double d1 = p0.d(), d2 = p1.d();
        double a = n1x * n1x + n1y * n1y + n1z * n1z;
        double b = n1x * n2x + n1y * n2y + n1z * n2z;
        double c = n2x * n2x + n2y * n2y + n2z * n2z;
        double denom = a * c - b * b;
        if (Math.abs(denom) < 1e-30) return null;
        double u = (-c * d1 + b * d2) / denom;
        double v = (b * d1 - a * d2) / denom;
        return new Vector3d(u * n1x + v * n2x, u * n1y + v * n2y, u * n1z + v * n2z);
    }

    public static Vector3d intersectPlanePlanePlane(Plane p0, Plane p1, Plane p2) {
        double n1x = p0.normal().x(), n1y = p0.normal().y(), n1z = p0.normal().z();
        double n2x = p1.normal().x(), n2y = p1.normal().y(), n2z = p1.normal().z();
        double n3x = p2.normal().x(), n3y = p2.normal().y(), n3z = p2.normal().z();
        double d1 = p0.d(), d2 = p1.d(), d3 = p2.d();
        double c23x = n2y * n3z - n2z * n3y;
        double c23y = n2z * n3x - n2x * n3z;
        double c23z = n2x * n3y - n2y * n3x;
        double det = n1x * c23x + n1y * c23y + n1z * c23z;
        if (Math.abs(det) < 1e-30) return null;
        double invDet = 1.0 / det;
        double c31x = n3y * n1z - n3z * n1y;
        double c31y = n3z * n1x - n3x * n1z;
        double c31z = n3x * n1y - n3y * n1x;
        double c12x = n1y * n2z - n1z * n2y;
        double c12y = n1z * n2x - n1x * n2z;
        double c12z = n1x * n2y - n1y * n2x;
        return new Vector3d(
            (-d1 * c23x - d2 * c31x - d3 * c12x) * invDet,
            (-d1 * c23y - d2 * c31y - d3 * c12y) * invDet,
            (-d1 * c23z - d2 * c31z - d3 * c12z) * invDet
        );
    }

    public static Vector3d intersectLineLine(Vector3d p0, Vector3d d0, Vector3d p1, Vector3d d1) {
        Vector3d[] closest = findClosestPointsLineLine(p0, d0, p1, d1);
        return closest[0];
    }

    public static Vector3d[] findClosestPointsLineLine(Vector3d p0, Vector3d d0, Vector3d p1, Vector3d d1) {
        double px = p0.x(), py = p0.y(), pz = p0.z();
        double dx = d0.x(), dy = d0.y(), dz = d0.z();
        double qx = p1.x(), qy = p1.y(), qz = p1.z();
        double ex = d1.x(), ey = d1.y(), ez = d1.z();
        double rx = px - qx, ry = py - qy, rz = pz - qz;
        double a = dx * dx + dy * dy + dz * dz;
        double b = dx * ex + dy * ey + dz * ez;
        double c = ex * ex + ey * ey + ez * ez;
        double dval = dx * rx + dy * ry + dz * rz;
        double eval = ex * rx + ey * ry + ez * rz;
        double denom = a * c - b * b;
        double t, s;
        if (Math.abs(denom) < 1e-30) {
            t = 0.0;
            s = c > 0.0 ? eval / c : 0.0;
        } else {
            t = (b * eval - c * dval) / denom;
            s = (a * eval - b * dval) / denom;
        }
        return new Vector3d[] {
            new Vector3d(px + t * dx, py + t * dy, pz + t * dz),
            new Vector3d(qx + s * ex, qy + s * ey, qz + s * ez)
        };
    }

    public static boolean intersectPolygonPlane(Vector3d[] vertices, Plane p, Vector3d[] intersectionLine) {
        int n = vertices.length;
        if (n < 3) return false;
        double nx = p.normal().x(), ny = p.normal().y(), nz = p.normal().z();
        double pd = p.d();
        ArrayList<Vector3d> pts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vector3d a = vertices[i];
            Vector3d b = vertices[(i + 1) % n];
            double da = nx * a.x() + ny * a.y() + nz * a.z() + pd;
            double db = nx * b.x() + ny * b.y() + nz * b.z() + pd;
            if (da * db < 0.0) {
                double t = da / (da - db);
                pts.add(new Vector3d(
                    a.x() + t * (b.x() - a.x()),
                    a.y() + t * (b.y() - a.y()),
                    a.z() + t * (b.z() - a.z())
                ));
            }
        }
        if (pts.size() >= 2 && intersectionLine != null && intersectionLine.length >= 2) {
            intersectionLine[0] = pts.get(0);
            intersectionLine[1] = pts.get(pts.size() - 1);
            return true;
        }
        return false;
    }

    public static Vector3d[] transformAab(Vector3d min, Vector3d max, Matrix4d m) {
        double x0 = min.x(), y0 = min.y(), z0 = min.z();
        double x1 = max.x(), y1 = max.y(), z1 = max.z();
        double m00 = m.m00(), m01 = m.m01(), m02 = m.m02(), m03 = m.m03();
        double m10 = m.m10(), m11 = m.m11(), m12 = m.m12(), m13 = m.m13();
        double m20 = m.m20(), m21 = m.m21(), m22 = m.m22(), m23 = m.m23();
        double m30 = m.m30(), m31 = m.m31(), m32 = m.m32(), m33 = m.m33();
        double[] xs = new double[8], ys = new double[8], zs = new double[8];
        int idx = 0;
        for (int i = 0; i < 2; i++) {
            double x = (i == 0) ? x0 : x1;
            for (int j = 0; j < 2; j++) {
                double y = (j == 0) ? y0 : y1;
                for (int k = 0; k < 2; k++) {
                    double z = (k == 0) ? z0 : z1;
                    double w = 1.0 / (m03 * x + m13 * y + m23 * z + m33);
                    xs[idx] = (m00 * x + m10 * y + m20 * z + m30) * w;
                    ys[idx] = (m01 * x + m11 * y + m21 * z + m31) * w;
                    zs[idx] = (m02 * x + m12 * y + m22 * z + m32) * w;
                    idx++;
                }
            }
        }
        double nx0 = Double.POSITIVE_INFINITY, ny0 = Double.POSITIVE_INFINITY, nz0 = Double.POSITIVE_INFINITY;
        double nx1 = Double.NEGATIVE_INFINITY, ny1 = Double.NEGATIVE_INFINITY, nz1 = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 8; i++) {
            nx0 = Math.min(nx0, xs[i]); ny0 = Math.min(ny0, ys[i]); nz0 = Math.min(nz0, zs[i]);
            nx1 = Math.max(nx1, xs[i]); ny1 = Math.max(ny1, ys[i]); nz1 = Math.max(nz1, zs[i]);
        }
        return new Vector3d[] { new Vector3d(nx0, ny0, nz0), new Vector3d(nx1, ny1, nz1) };
    }

    public static boolean testPointFrustum(Vector3d point, Matrix4d projectionMatrix) {
        FrustumIntersection frustum = new FrustumIntersection(projectionMatrix);
        return frustum.testPoint(point) != FrustumIntersection.OUTSIDE;
    }

    public static int intersectSphereFrustum(Vector3d center, double radius, Matrix4d projectionMatrix) {
        FrustumIntersection frustum = new FrustumIntersection(projectionMatrix);
        return frustum.testSphere(center, radius);
    }

    public static int intersectAABBFrustum(AABB box, Matrix4d projectionMatrix) {
        FrustumIntersection frustum = new FrustumIntersection(projectionMatrix);
        return frustum.testAABB(box);
    }

    public static boolean isPointInsideFrustum(Vector3d point, FrustumIntersection frustum) {
        return frustum.testPoint(point) != FrustumIntersection.OUTSIDE;
    }

    public static boolean isPointInsideFrustum(Vector3d point, Matrix4d projectionMatrix) {
        FrustumIntersection frustum = new FrustumIntersection(projectionMatrix);
        return frustum.testPoint(point) != FrustumIntersection.OUTSIDE;
    }

    public static boolean isAabbInsideFrustum(AABB box, FrustumIntersection frustum) {
        return frustum.testAABB(box) != FrustumIntersection.OUTSIDE;
    }
}

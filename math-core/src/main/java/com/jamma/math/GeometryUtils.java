package com.jamma.math;

import com.jamma.math.matrix.Matrix3d;
import com.jamma.math.matrix.Matrix3f;
import com.jamma.math.matrix.Matrix4d;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;

public final class GeometryUtils {

    private GeometryUtils() {}

    public static Vector3f computeTangent(
        Vector3f p1, Vector3f p2, Vector3f p3,
        Vector2f uv1, Vector2f uv2, Vector2f uv3
    ) {
        Vector3f edge1 = p2.sub(p1);
        Vector3f edge2 = p3.sub(p1);
        Vector2f duv1 = uv2.sub(uv1);
        Vector2f duv2 = uv3.sub(uv1);
        float f = 1.0f / (duv1.x() * duv2.y() - duv2.x() * duv1.y());
        float tx = f * (duv2.y() * edge1.x() - duv1.y() * edge2.x());
        float ty = f * (duv2.y() * edge1.y() - duv1.y() * edge2.y());
        float tz = f * (duv2.y() * edge1.z() - duv1.y() * edge2.z());
        return new Vector3f(tx, ty, tz);
    }

    public static Vector3f computeBitangent(
        Vector3f p1, Vector3f p2, Vector3f p3,
        Vector2f uv1, Vector2f uv2, Vector2f uv3
    ) {
        Vector3f edge1 = p2.sub(p1);
        Vector3f edge2 = p3.sub(p1);
        Vector2f duv1 = uv2.sub(uv1);
        Vector2f duv2 = uv3.sub(uv1);
        float f = 1.0f / (duv1.x() * duv2.y() - duv2.x() * duv1.y());
        float bx = f * (-duv2.x() * edge1.x() + duv1.x() * edge2.x());
        float by = f * (-duv2.x() * edge1.y() + duv1.x() * edge2.y());
        float bz = f * (-duv2.x() * edge1.z() + duv1.x() * edge2.z());
        return new Vector3f(bx, by, bz);
    }

    public static Vector3f computeNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f edge1 = p2.sub(p1);
        Vector3f edge2 = p3.sub(p1);
        return edge1.cross(edge2).normalize();
    }

    public static Vector3f[] computeTangentBitangent(
        Vector3f p1, Vector3f p2, Vector3f p3,
        Vector2f uv1, Vector2f uv2, Vector2f uv3
    ) {
        Vector3f tangent = computeTangent(p1, p2, p3, uv1, uv2, uv3);
        Vector3f bitangent = computeBitangent(p1, p2, p3, uv1, uv2, uv3);
        return new Vector3f[] { tangent, bitangent };
    }

    public static Vector3D computeNormal(Vector3D p1, Vector3D p2, Vector3D p3) {
        Vector3D edge1 = p2.sub(p1);
        Vector3D edge2 = p3.sub(p1);
        return edge1.cross(edge2).normalize();
    }

    public static Vector3D computeTangent(
        Vector3D p1, Vector3D p2, Vector3D p3,
        Vector2D uv1, Vector2D uv2, Vector2D uv3
    ) {
        Vector3D edge1 = p2.sub(p1);
        Vector3D edge2 = p3.sub(p1);
        Vector2D duv1 = uv2.sub(uv1);
        Vector2D duv2 = uv3.sub(uv1);
        double f = 1.0 / (duv1.x() * duv2.y() - duv2.x() * duv1.y());
        double tx = f * (duv2.y() * edge1.x() - duv1.y() * edge2.x());
        double ty = f * (duv2.y() * edge1.y() - duv1.y() * edge2.y());
        double tz = f * (duv2.y() * edge1.z() - duv1.y() * edge2.z());
        return new Vector3D(tx, ty, tz);
    }

    public static Vector3D computeBitangent(
        Vector3D p1, Vector3D p2, Vector3D p3,
        Vector2D uv1, Vector2D uv2, Vector2D uv3
    ) {
        Vector3D edge1 = p2.sub(p1);
        Vector3D edge2 = p3.sub(p1);
        Vector2D duv1 = uv2.sub(uv1);
        Vector2D duv2 = uv3.sub(uv1);
        double f = 1.0 / (duv1.x() * duv2.y() - duv2.x() * duv1.y());
        double bx = f * (-duv2.x() * edge1.x() + duv1.x() * edge2.x());
        double by = f * (-duv2.x() * edge1.y() + duv1.x() * edge2.y());
        double bz = f * (-duv2.x() * edge1.z() + duv1.x() * edge2.z());
        return new Vector3D(bx, by, bz);
    }

    public static Vector3D[] computeTangentBitangent(
        Vector3D p1, Vector3D p2, Vector3D p3,
        Vector2D uv1, Vector2D uv2, Vector2D uv3
    ) {
        Vector3D tangent = computeTangent(p1, p2, p3, uv1, uv2, uv3);
        Vector3D bitangent = computeBitangent(p1, p2, p3, uv1, uv2, uv3);
        return new Vector3D[] { tangent, bitangent };
    }

    public static Matrix3f rotationMatrix(Vector3f from, Vector3f to) {
        Quaternionf q = Quaternionf.rotateTo(from, to);
        return q.toMatrix3();
    }

    public static Matrix3d rotationMatrix(Vector3D from, Vector3D to) {
        com.jamma.math.quaternion.Quaterniond q =
            com.jamma.math.quaternion.Quaterniond.rotateTo(from, to);
        return q.toMatrix3();
    }

    public static Vector3f barycentric(
        Vector3f point, Vector3f a, Vector3f b, Vector3f c
    ) {
        Vector3f v0 = c.sub(a);
        Vector3f v1 = b.sub(a);
        Vector3f v2 = point.sub(a);
        float dot00 = v0.dot(v0);
        float dot01 = v0.dot(v1);
        float dot02 = v0.dot(v2);
        float dot11 = v1.dot(v1);
        float dot12 = v1.dot(v2);
        float invDenom = 1.0f / (dot00 * dot11 - dot01 * dot01);
        float v = (dot11 * dot02 - dot01 * dot12) * invDenom;
        float w = (dot00 * dot12 - dot01 * dot02) * invDenom;
        float u = 1.0f - v - w;
        return new Vector3f(u, v, w);
    }

    public static Vector3D barycentric(
        Vector3D point, Vector3D a, Vector3D b, Vector3D c
    ) {
        Vector3D v0 = c.sub(a);
        Vector3D v1 = b.sub(a);
        Vector3D v2 = point.sub(a);
        double dot00 = v0.dot(v0);
        double dot01 = v0.dot(v1);
        double dot02 = v0.dot(v2);
        double dot11 = v1.dot(v1);
        double dot12 = v1.dot(v2);
        double invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
        double v = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double w = (dot00 * dot12 - dot01 * dot02) * invDenom;
        double u = 1.0 - v - w;
        return new Vector3D(u, v, w);
    }

    public static float interpolateBarycentric(
        float va, float vb, float vc, float u, float v, float w
    ) {
        return va * u + vb * v + vc * w;
    }

    public static float areaTriangle(Vector3f a, Vector3f b, Vector3f c) {
        Vector3f edge1 = b.sub(a);
        Vector3f edge2 = c.sub(a);
        return edge1.cross(edge2).length() * 0.5f;
    }

    public static double areaTriangle(Vector3D a, Vector3D b, Vector3D c) {
        Vector3D edge1 = b.sub(a);
        Vector3D edge2 = c.sub(a);
        return edge1.cross(edge2).length() * 0.5;
    }

    public static Vector3f centroid(Vector3f a, Vector3f b, Vector3f c) {
        return new Vector3f(
            (a.x() + b.x() + c.x()) / 3.0f,
            (a.y() + b.y() + c.y()) / 3.0f,
            (a.z() + b.z() + c.z()) / 3.0f
        );
    }

    public static Vector3D centroid(Vector3D a, Vector3D b, Vector3D c) {
        return new Vector3D(
            (a.x() + b.x() + c.x()) / 3.0,
            (a.y() + b.y() + c.y()) / 3.0,
            (a.z() + b.z() + c.z()) / 3.0
        );
    }

    public static Vector3f[] orthonormalBasis(Vector3f n0) {
        Vector3f n = n0.normalize();
        Vector3f t;
        if (Math.abs(n.x()) < Math.abs(n.y())) {
            t = new Vector3f(n.z(), 0, -n.x());
        } else {
            t = new Vector3f(0, -n.z(), n.y());
        }
        Vector3f tangent = t.normalize();
        Vector3f bitangent = n.cross(tangent);
        return new Vector3f[] { tangent, bitangent, n };
    }

    public static Vector3D[] orthonormalBasis(Vector3D n0) {
        Vector3D n = n0.normalize();
        Vector3D t;
        if (Math.abs(n.x()) < Math.abs(n.y())) {
            t = new Vector3D(n.z(), 0, -n.x());
        } else {
            t = new Vector3D(0, -n.z(), n.y());
        }
        Vector3D tangent = t.normalize();
        Vector3D bitangent = n.cross(tangent);
        return new Vector3D[] { tangent, bitangent, n };
    }

    public static Vector3f transformNormal(Matrix4f mat, Vector3f normal) {
        float m00 = mat.m00(), m01 = mat.m01(), m02 = mat.m02();
        float m10 = mat.m10(), m11 = mat.m11(), m12 = mat.m12();
        float m20 = mat.m20(), m21 = mat.m21(), m22 = mat.m22();
        float c00 = m11 * m22 - m12 * m21;
        float c01 = m12 * m20 - m10 * m22;
        float c02 = m10 * m21 - m11 * m20;
        float c10 = m02 * m21 - m01 * m22;
        float c11 = m00 * m22 - m02 * m20;
        float c12 = m01 * m20 - m00 * m21;
        float c20 = m01 * m12 - m02 * m11;
        float c21 = m02 * m10 - m00 * m12;
        float c22 = m00 * m11 - m01 * m10;
        float nx = c00 * normal.x() + c01 * normal.y() + c02 * normal.z();
        float ny = c10 * normal.x() + c11 * normal.y() + c12 * normal.z();
        float nz = c20 * normal.x() + c21 * normal.y() + c22 * normal.z();
        return new Vector3f(nx, ny, nz).normalize();
    }

    public static Vector3f transformNormal(Matrix4f mat, float nx, float ny, float nz) {
        return transformNormal(mat, new Vector3f(nx, ny, nz));
    }

    public static Vector3D transformNormal(Matrix4d mat, Vector3D normal) {
        double m00 = mat.m00(), m01 = mat.m01(), m02 = mat.m02();
        double m10 = mat.m10(), m11 = mat.m11(), m12 = mat.m12();
        double m20 = mat.m20(), m21 = mat.m21(), m22 = mat.m22();
        double c00 = m11 * m22 - m12 * m21;
        double c01 = m12 * m20 - m10 * m22;
        double c02 = m10 * m21 - m11 * m20;
        double c10 = m02 * m21 - m01 * m22;
        double c11 = m00 * m22 - m02 * m20;
        double c12 = m01 * m20 - m00 * m21;
        double c20 = m01 * m12 - m02 * m11;
        double c21 = m02 * m10 - m00 * m12;
        double c22 = m00 * m11 - m01 * m10;
        double tx = c00 * normal.x() + c01 * normal.y() + c02 * normal.z();
        double ty = c10 * normal.x() + c11 * normal.y() + c12 * normal.z();
        double tz = c20 * normal.x() + c21 * normal.y() + c22 * normal.z();
        return new Vector3D(tx, ty, tz).normalize();
    }

    public static Vector3D transformNormal(Matrix4d mat, double nx, double ny, double nz) {
        return transformNormal(mat, new Vector3D(nx, ny, nz));
    }

    public static Vector3f transformNormal(Matrix3f mat, Vector3f normal) {
        float m00 = mat.m00(), m01 = mat.m01(), m02 = mat.m02();
        float m10 = mat.m10(), m11 = mat.m11(), m12 = mat.m12();
        float m20 = mat.m20(), m21 = mat.m21(), m22 = mat.m22();
        float c00 = m11 * m22 - m12 * m21;
        float c01 = m12 * m20 - m10 * m22;
        float c02 = m10 * m21 - m11 * m20;
        float c10 = m02 * m21 - m01 * m22;
        float c11 = m00 * m22 - m02 * m20;
        float c12 = m01 * m20 - m00 * m21;
        float c20 = m01 * m12 - m02 * m11;
        float c21 = m02 * m10 - m00 * m12;
        float c22 = m00 * m11 - m01 * m10;
        float nx = c00 * normal.x() + c01 * normal.y() + c02 * normal.z();
        float ny = c10 * normal.x() + c11 * normal.y() + c12 * normal.z();
        float nz = c20 * normal.x() + c21 * normal.y() + c22 * normal.z();
        return new Vector3f(nx, ny, nz).normalize();
    }

    public static Vector3D transformNormal(Matrix3d mat, Vector3D normal) {
        double m00 = mat.m00(), m01 = mat.m01(), m02 = mat.m02();
        double m10 = mat.m10(), m11 = mat.m11(), m12 = mat.m12();
        double m20 = mat.m20(), m21 = mat.m21(), m22 = mat.m22();
        double c00 = m11 * m22 - m12 * m21;
        double c01 = m12 * m20 - m10 * m22;
        double c02 = m10 * m21 - m11 * m20;
        double c10 = m02 * m21 - m01 * m22;
        double c11 = m00 * m22 - m02 * m20;
        double c12 = m01 * m20 - m00 * m21;
        double c20 = m01 * m12 - m02 * m11;
        double c21 = m02 * m10 - m00 * m12;
        double c22 = m00 * m11 - m01 * m10;
        double tx = c00 * normal.x() + c01 * normal.y() + c02 * normal.z();
        double ty = c10 * normal.x() + c11 * normal.y() + c12 * normal.z();
        double tz = c20 * normal.x() + c21 * normal.y() + c22 * normal.z();
        return new Vector3D(tx, ty, tz).normalize();
    }
}

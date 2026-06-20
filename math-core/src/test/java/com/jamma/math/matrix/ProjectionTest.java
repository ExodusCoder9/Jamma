package com.jamma.math.matrix;

import com.jamma.math.Vector4d;
import com.jamma.math.Vector4f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectionTest {

    @Test
    public void testOpenGLPerspectiveD() {
        Matrix4d m = new Matrix4d().perspective(Math.toRadians(90.0), 1.0, 1.0, 100.0);

        // Near plane point: (0, 0, -1, 1)
        Vector4d pNear = new Vector4d(0.0, 0.0, -1.0, 1.0);
        Vector4d projNear = m.transform(pNear);
        // NDC = projNear / w
        double zNdcNear = projNear.z() / projNear.w();
        assertEquals(-1.0, zNdcNear, 1e-6);

        // Far plane point: (0, 0, -100, 1)
        Vector4d pFar = new Vector4d(0.0, 0.0, -100.0, 1.0);
        Vector4d projFar = m.transform(pFar);
        double zNdcFar = projFar.z() / projFar.w();
        assertEquals(1.0, zNdcFar, 1e-6);
    }

    @Test
    public void testVulkanPerspectiveD() {
        Matrix4d m = new Matrix4d().perspectiveVulkan(Math.toRadians(90.0), 1.0, 1.0, 100.0);

        // Y scaling should be negative due to Y-down clip space
        assertTrue(m.m11() < 0.0);

        // Near plane point: (0, 0, -1, 1)
        Vector4d pNear = new Vector4d(0.0, 0.0, -1.0, 1.0);
        Vector4d projNear = m.transform(pNear);
        double zNdcNear = projNear.z() / projNear.w();
        assertEquals(0.0, zNdcNear, 1e-6);

        // Far plane point: (0, 0, -100, 1)
        Vector4d pFar = new Vector4d(0.0, 0.0, -100.0, 1.0);
        Vector4d projFar = m.transform(pFar);
        double zNdcFar = projFar.z() / projFar.w();
        assertEquals(1.0, zNdcFar, 1e-6);
    }

    @Test
    public void testOpenGLPerspectiveF() {
        Matrix4f m = new Matrix4f().perspective((float) Math.toRadians(90.0), 1.0f, 1.0f, 100.0f);

        // Near plane point: (0, 0, -1, 1)
        Vector4f pNear = new Vector4f(0.0f, 0.0f, -1.0f, 1.0f);
        Vector4f projNear = m.transform(pNear);
        float zNdcNear = projNear.z() / projNear.w();
        assertEquals(-1.0f, zNdcNear, 1e-4f);

        // Far plane point: (0, 0, -100, 1)
        Vector4f pFar = new Vector4f(0.0f, 0.0f, -100.0f, 1.0f);
        Vector4f projFar = m.transform(pFar);
        float zNdcFar = projFar.z() / projFar.w();
        assertEquals(1.0f, zNdcFar, 1e-4f);
    }

    @Test
    public void testVulkanPerspectiveF() {
        Matrix4f m = new Matrix4f().perspectiveVulkan((float) Math.toRadians(90.0), 1.0f, 1.0f, 100.0f);

        assertTrue(m.m11() < 0.0f);

        Vector4f pNear = new Vector4f(0.0f, 0.0f, -1.0f, 1.0f);
        Vector4f projNear = m.transform(pNear);
        float zNdcNear = projNear.z() / projNear.w();
        assertEquals(0.0f, zNdcNear, 1e-4f);

        Vector4f pFar = new Vector4f(0.0f, 0.0f, -100.0f, 1.0f);
        Vector4f projFar = m.transform(pFar);
        float zNdcFar = projFar.z() / projFar.w();
        assertEquals(1.0f, zNdcFar, 1e-4f);
    }

    @Test
    public void testOpenGLOrthoD() {
        Matrix4d m = new Matrix4d().ortho(-1.0, 1.0, -1.0, 1.0, 1.0, 10.0);

        // Point at near plane: (0, 0, -1, 1)
        Vector4d pNear = new Vector4d(0.0, 0.0, -1.0, 1.0);
        Vector4d projNear = m.transform(pNear);
        assertEquals(-1.0, projNear.z(), 1e-6);

        // Point at far plane: (0, 0, -10, 1)
        Vector4d pFar = new Vector4d(0.0, 0.0, -10.0, 1.0);
        Vector4d projFar = m.transform(pFar);
        assertEquals(1.0, projFar.z(), 1e-6);
    }

    @Test
    public void testVulkanOrthoD() {
        Matrix4d m = new Matrix4d().orthoVulkan(-1.0, 1.0, -1.0, 1.0, 1.0, 10.0);

        // Point at near plane: (0, 0, -1, 1)
        Vector4d pNear = new Vector4d(0.0, 0.0, -1.0, 1.0);
        Vector4d projNear = m.transform(pNear);
        assertEquals(0.0, projNear.z(), 1e-6);

        // Point at far plane: (0, 0, -10, 1)
        Vector4d pFar = new Vector4d(0.0, 0.0, -10.0, 1.0);
        Vector4d projFar = m.transform(pFar);
        assertEquals(1.0, projFar.z(), 1e-6);
    }
}

package com.jamma.graphics;

import com.jamma.math.Vector3f;
import com.jamma.math.geometry.AABBf;
import com.jamma.math.geometry.Rayf;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphicsTest {

    static final float F = 1e-4f;

    // ── Color ─────────────────────────────────────────────────────────────

    @Test void colorConstants() {
        assertEquals(1, Color.WHITE.r(), F);
        assertEquals(1, Color.WHITE.g(), F);
        assertEquals(1, Color.WHITE.b(), F);
        assertEquals(1, Color.WHITE.a(), F);
        assertEquals(0, Color.BLACK.r(), F);
        assertEquals(1, Color.RED.r(), F);
        assertEquals(1, Color.GREEN.g(), F);
        assertEquals(0, Color.GREEN.r(), F);
    }

    @Test void colorRgbaPackUnpack() {
        Color c = new Color(0.5f, 0.75f, 1.0f, 0.25f);
        int packed = c.toRgba();
        Color unpacked = new Color(packed);
        assertEquals(c.r(), unpacked.r(), 1f/255);
        assertEquals(c.g(), unpacked.g(), 1f/255);
        assertEquals(c.b(), unpacked.b(), 1f/255);
        assertEquals(c.a(), unpacked.a(), 1f/255);
    }

    @Test void colorRgbPack() {
        Color c = new Color(1, 0, 0);
        assertEquals(0xFF0000, c.toRgb());
    }

    @Test void colorToLinearSrgb() {
        Color srgb = new Color(0.5f, 0.5f, 0.5f);
        Color linear = srgb.toLinear();
        assertTrue(linear.r() < 0.5f);
        Color back = linear.toSrgb();
        assertEquals(0.5f, back.r(), 1e-3f);
    }

    @Test void colorBlend() {
        Color a = Color.RED;
        Color b = Color.BLUE;
        Color m = a.blend(b, 0.5f);
        assertEquals(0.5f, m.r(), F);
        assertEquals(0, m.g(), F);
        assertEquals(0.5f, m.b(), F);
        assertEquals(1, m.a(), F);
    }

    @Test void colorMultiply() {
        Color a = new Color(0.5f, 1, 1);
        Color b = new Color(1, 0.5f, 1);
        Color m = a.multiply(b);
        assertEquals(0.5f, m.r(), F);
        assertEquals(0.5f, m.g(), F);
        assertEquals(1, m.b(), F);
    }

    @Test void colorWithAlpha() {
        Color c = Color.RED.withAlpha(0.5f);
        assertEquals(1, c.r(), F);
        assertEquals(0.5f, c.a(), F);
    }

    @Test void colorHexRoundTrip() {
        Color c = new Color(0x80FF4080);
        String hex = c.toHex();
        Color back = Color.fromHex(hex);
        assertEquals(c.r(), back.r(), 1f/255);
        assertEquals(c.g(), back.g(), 1f/255);
        assertEquals(c.b(), back.b(), 1f/255);
        assertEquals(c.a(), back.a(), 1f/255);
    }

    @Test void colorFromHex3() {
        Color c = Color.fromHex("#F0A");
        assertEquals(1, c.r(), F);
        assertEquals(0, c.g(), F);
        assertEquals(0.667f, c.b(), 1e-2f);
    }

    @Test void colorHsv() {
        float[] hsv = Color.RED.hsv();
        assertEquals(0, hsv[0], F);
        assertEquals(1, hsv[1], F);
        assertEquals(1, hsv[2], F);

        Color back = Color.fromHsv(hsv[0], hsv[1], hsv[2]);
        assertEquals(Color.RED.r(), back.r(), F);
        assertEquals(Color.RED.g(), back.g(), F);
        assertEquals(Color.RED.b(), back.b(), F);
    }

    @Test void colorTemperature() {
        Color warm = Color.fromTemperature(3200);
        assertTrue(warm.r() > warm.b(), () -> "warm light r=" + warm.r() + " > b=" + warm.b() + " at 3200K");
        Color cool = Color.fromTemperature(10000);
        assertTrue(cool.b() > cool.r(), () -> "cool light b=" + cool.b() + " > r=" + cool.r() + " at 10000K");
    }

    // ── Transform ──────────────────────────────────────────────────────────

    @Test void transformIdentity() {
        var t = new Transform();
        assertEquals(Vector3f.ZERO, t.position());
        assertEquals(1, t.scale().x(), F);
        assertEquals(1, t.scale().y(), F);
        assertEquals(1, t.scale().z(), F);
    }

    @Test void transformToMatrix() {
        var t = new Transform(
            new Vector3f(1, 2, 3),
            new Quaternionf(0, 0, 0, 1),
            new Vector3f(2, 2, 2)
        );
        Matrix4f m = t.toMatrix();
        // translation
        assertEquals(1, m.m30(), F);
        assertEquals(2, m.m31(), F);
        assertEquals(3, m.m32(), F);
        // uniform scale 2
        assertEquals(2, m.m00(), F);
        assertEquals(2, m.m11(), F);
        assertEquals(2, m.m22(), F);
    }

    @Test void transformFromMatrix() {
        var m = new Matrix4f().translate(5, 10, 15).rotateX(1.57f).scale(3);
        var t = Transform.fromMatrix(m);
        assertEquals(5, t.position().x(), 1e-3f);
        assertEquals(10, t.position().y(), 1e-3f);
        assertEquals(15, t.position().z(), 1e-3f);
        assertEquals(3, t.scale().x(), 1e-3f);
        assertEquals(3, t.scale().y(), 1e-3f);
        assertEquals(3, t.scale().z(), 1e-3f);
    }

    @Test void transformApply() {
        var t = new Transform(
            new Vector3f(1, 0, 0),
            Quaternionf.IDENTITY,
            new Vector3f(2, 2, 2)
        );
        Vector3f v = t.apply(new Vector3f(1, 1, 1));
        assertEquals(3, v.x(), F);
        assertEquals(2, v.y(), F);
        assertEquals(2, v.z(), F);
    }

    @Test void transformTranslate() {
        var t = new Transform().translate(1, 2, 3);
        assertEquals(1, t.position().x(), F);
        assertEquals(2, t.position().y(), F);
        assertEquals(3, t.position().z(), F);
    }

    @Test void transformLerp() {
        var a = new Transform(Vector3f.ZERO, Quaternionf.IDENTITY, new Vector3f(1, 1, 1));
        var b = new Transform(new Vector3f(10, 0, 0), Quaternionf.IDENTITY, new Vector3f(2, 2, 2));
        var r = a.lerp(b, 0.5f);
        assertEquals(5, r.position().x(), F);
        assertEquals(1.5f, r.scale().x(), F);
    }

    @Test void transformCompose() {
        var child = new Transform(new Vector3f(1, 0, 0));
        var parent = new Transform(new Vector3f(10, 0, 0));
        var world = child.compose(parent);
        assertEquals(11, world.position().x(), F);
    }

    // ── Camera ─────────────────────────────────────────────────────────────

    @Test void cameraDefaults() {
        var cam = Camera.createPerspective(1.57f, 16f/9f, 0.1f, 100f);
        Vector3f dir = new Vector3f(
            cam.center().x() - cam.eye().x(),
            cam.center().y() - cam.eye().y(),
            cam.center().z() - cam.eye().z()
        );
        assertEquals(1, dir.length(), F);
    }

    @Test void cameraViewMatrix() {
        var cam = new Camera(Vector3f.ZERO, new Vector3f(0, 0, -1), new Vector3f(0, 1, 0),
            1.57f, 1.6f, 0.1f, 100f);
        Matrix4f view = cam.viewMatrix();
        // looking down -Z → Z axis should be (0, 0, 1) in view space
        float zx = view.m20(), zy = view.m21(), zz = view.m22();
        assertEquals(0, zx, F);
        assertEquals(0, zy, F);
        assertEquals(1, zz, F);
    }

    @Test void cameraProjectionMatrix() {
        var cam = Camera.createPerspective(1.57f, 1.6f, 0.1f, 100f);
        Matrix4f proj = cam.projectionMatrix();
        assertNotEquals(0, proj.m00());
        assertNotEquals(0, proj.m11());
    }

    @Test void cameraViewProjection() {
        var cam = Camera.createPerspective(1.57f, 16f/9f, 0.1f, 100f);
        Matrix4f vp = cam.viewProjectionMatrix();
        assertNotNull(vp);
    }

    @Test void cameraFrustum() {
        var cam = Camera.createPerspective(1.57f, 16f/9f, 0.1f, 100f);
        Frustum f = cam.frustum();
        assertNotNull(f);
        // center should be inside
        assertEquals(Frustum.INSIDE, f.testPoint(cam.center()));
    }

    @Test void cameraWith() {
        var cam = Camera.createPerspective(1.57f, 16f/9f, 0.1f, 100f);
        var moved = cam.withEye(new Vector3f(5, 0, 0));
        assertEquals(5, moved.eye().x(), F);
    }

    @Test void cameraForward() {
        var cam = Camera.createPerspective(1.57f, 16f/9f, 0.1f, 100f);
        var fwd = cam.forward(5);
        assertEquals(-5, fwd.eye().z() - cam.eye().z(), 1e-3f);
    }

    @Test void cameraOrbit() {
        var cam = new Camera(
            new Vector3f(10, 0, 0), Vector3f.ZERO, new Vector3f(0, 1, 0),
            1.57f, 1.6f, 0.1f, 100f
        );
        var orbited = cam.orbit((float) Math.PI / 2, 0);
        // after 90° yaw around Y, eye should be near (0, 0, -10)
        assertEquals(0, orbited.eye().x(), 1e-2f);
        assertEquals(-10, orbited.eye().z(), 1e-0f);
    }

    // ── Viewport ───────────────────────────────────────────────────────────

    @Test void viewportAspect() {
        var vp = new Viewport(0, 0, 1920, 1080);
        assertEquals(16f/9f, vp.aspect(), 1e-4f);
    }

    @Test void viewportNormalizedToScreen() {
        var vp = new Viewport(100, 100, 800, 600);
        float sx = vp.normalizedToScreenX(0);
        float sy = vp.normalizedToScreenY(0);
        assertEquals(500, sx, F);
        assertEquals(400, sy, F);
    }

    @Test void viewportScreenToNormalized() {
        var vp = new Viewport(0, 0, 1920, 1080);
        float nx = vp.screenToNormalizedX(960);
        float ny = vp.screenToNormalizedY(540);
        assertEquals(0, nx, F);
        assertEquals(0, ny, F);
    }

    @Test void viewportRoundTrip() {
        var vp = new Viewport(0, 0, 1920, 1080);
        float[] sc = vp.normalizedToScreen(0.5f, -0.5f);
        float[] nd = vp.screenToNormalized(sc[0], sc[1]);
        assertEquals(0.5f, nd[0], F);
        assertEquals(-0.5f, nd[1], F);
    }

    // ── Frustum ────────────────────────────────────────────────────────────

    @Test void frustumFromMatrix() {
        Matrix4f vp = new Matrix4f().perspective(1.57f, 1.6f, 0.1f, 100f)
            .multiply(new Matrix4f().lookAt(
                new Vector3f(0, 0, 5), Vector3f.ZERO, new Vector3f(0, 1, 0)));
        Frustum f = Frustum.from(vp);
        assertNotNull(f);
    }

    @Test void frustumAABBTest() {
        var cam = new Camera(
            new Vector3f(0, 0, 5), Vector3f.ZERO, new Vector3f(0, 1, 0),
            1.57f, 1.6f, 0.1f, 100f
        );
        Frustum f = cam.frustum();
        // box at origin should be inside
        assertEquals(Frustum.INSIDE, f.testAABB(new AABBf(-1, -1, -1, 1, 1, 1)));
        // box far beyond far plane should be outside
        assertEquals(Frustum.OUTSIDE, f.testAABB(new AABBf(-1, -1, -200, 1, 1, -150)));
    }

    @Test void frustumSphereTest() {
        var cam = new Camera(
            new Vector3f(0, 0, 5), Vector3f.ZERO, new Vector3f(0, 1, 0),
            1.57f, 1.6f, 0.1f, 100f
        );
        Frustum f = cam.frustum();
        assertEquals(Frustum.INSIDE, f.testSphere(Vector3f.ZERO, 1));
        assertEquals(Frustum.OUTSIDE, f.testSphere(new Vector3f(0, 0, -200), 1));
    }

    @Test void frustumPointTest() {
        var cam = new Camera(
            new Vector3f(0, 0, 5), Vector3f.ZERO, new Vector3f(0, 1, 0),
            1.57f, 1.6f, 0.1f, 100f
        );
        Frustum f = cam.frustum();
        assertEquals(Frustum.INSIDE, f.testPoint(Vector3f.ZERO));
        assertEquals(Frustum.OUTSIDE, f.testPoint(new Vector3f(0, 0, -200)));
    }

    @Test void frustumCorners() {
        var cam = new Camera(
            Vector3f.ZERO, new Vector3f(0, 0, -1), new Vector3f(0, 1, 0),
            1.57f, 1.0f, 1, 10
        );
        Frustum f = cam.frustum();
        Vector3f[] corners = f.corners();
        assertEquals(8, corners.length);
        // near corners should be closer than far corners
        float nearZ = (corners[0].z() + corners[1].z() + corners[2].z() + corners[3].z()) / 4;
        float farZ = (corners[4].z() + corners[5].z() + corners[6].z() + corners[7].z()) / 4;
        assertTrue(nearZ > farZ, "near plane should be closer to camera than far plane");
    }

    // ── RayBuilder ─────────────────────────────────────────────────────────

    @Test void rayBuilderFromCamera() {
        var cam = new Camera(
            Vector3f.ZERO, new Vector3f(0, 0, -1), new Vector3f(0, 1, 0),
            1.57f, 1.6f, 0.1f, 100f
        );
        Rayf ray = RayBuilder.fromCamera(cam, 0, 0);
        assertNotNull(ray);
        assertEquals(1, ray.direction.length(), 1e-4f);
        // center ray should point toward center
        assertTrue(ray.direction.z() < 0);
    }

    // ── Integration ────────────────────────────────────────────────────────

    @Test void cameraTransformViewportIntegration() {
        var cam = Camera.createPerspective(1.57f, 16f/9f, 0.1f, 100f);
        var vp = new Viewport(0, 0, 1920, 1080);
        var ray = RayBuilder.fromCamera(cam, vp.screenToNormalizedX(960), vp.screenToNormalizedY(540));
        assertNotNull(ray);
        assertEquals(1, ray.direction.length(), 1e-4f);
    }
}

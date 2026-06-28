package com.jamma.graphics;

import com.jamma.math.Vector3f;
import com.jamma.math.matrix.Matrix4f;
import com.jamma.math.quaternion.Quaternionf;

public record Camera(Vector3f eye, Vector3f center, Vector3f up, float fovY, float aspect, float zNear, float zFar) {

    public static Camera createPerspective(float fovY, float aspect, float zNear, float zFar) {
        return new Camera(
            new Vector3f(0, 0, 1),
            Vector3f.ZERO,
            new Vector3f(0, 1, 0),
            fovY, aspect, zNear, zFar
        );
    }

    public Matrix4f viewMatrix() {
        return new Matrix4f().lookAt(eye, center, up);
    }

    public Matrix4f projectionMatrix() {
        return new Matrix4f().perspective(fovY, aspect, zNear, zFar);
    }

    public Matrix4f viewProjectionMatrix() {
        return projectionMatrix().multiply(viewMatrix());
    }

    public Frustum frustum() {
        var vp = viewProjectionMatrix();
        return Frustum.from(vp);
    }

    public Camera withEye(Vector3f eye) {
        return new Camera(eye, center, up, fovY, aspect, zNear, zFar);
    }

    public Camera withCenter(Vector3f center) {
        return new Camera(eye, center, up, fovY, aspect, zNear, zFar);
    }

    public Camera withUp(Vector3f up) {
        return new Camera(eye, center, up, fovY, aspect, zNear, zFar);
    }

    public Camera withFov(float fovY) {
        return new Camera(eye, center, up, fovY, aspect, zNear, zFar);
    }

    public Camera withAspect(float aspect) {
        return new Camera(eye, center, up, fovY, aspect, zNear, zFar);
    }

    public Camera withNear(float zNear) {
        return new Camera(eye, center, up, fovY, aspect, zNear, zFar);
    }

    public Camera withFar(float zFar) {
        return new Camera(eye, center, up, fovY, aspect, zNear, zFar);
    }

    public Camera forward(float amount) {
        Vector3f dir = new Vector3f(
            center.x() - eye.x(),
            center.y() - eye.y(),
            center.z() - eye.z()
        ).normalize();
        return new Camera(
            new Vector3f(eye.x() + dir.x() * amount, eye.y() + dir.y() * amount, eye.z() + dir.z() * amount),
            new Vector3f(center.x() + dir.x() * amount, center.y() + dir.y() * amount, center.z() + dir.z() * amount),
            up, fovY, aspect, zNear, zFar
        );
    }

    public Camera orbit(float yaw, float pitch) {
        Vector3f dir = new Vector3f(
            eye.x() - center.x(),
            eye.y() - center.y(),
            eye.z() - center.z()
        );
        float radius = dir.length();
        dir = dir.normalize();

        Vector3f right = up.cross(dir).normalize();

        var yawQ = Quaternionf.fromAxisAngle(up.x(), up.y(), up.z(), yaw);
        var pitchQ = Quaternionf.fromAxisAngle(right.x(), right.y(), right.z(), pitch);

        dir = pitchQ.transform(yawQ.transform(dir));

        return new Camera(
            new Vector3f(center.x() + dir.x() * radius, center.y() + dir.y() * radius, center.z() + dir.z() * radius),
            center, up, fovY, aspect, zNear, zFar
        );
    }
}

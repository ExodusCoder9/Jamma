package com.jamma.graphics;

import com.jamma.math.Vector3f;
import com.jamma.math.geometry.Rayf;
import com.jamma.math.quaternion.Quaternionf;

public final class RayBuilder {

    private RayBuilder() {}

    public static Rayf fromCamera(Camera camera, float ndcX, float ndcY) {
        var invVp = camera.viewProjectionMatrix().invert();
        var near = invVp.transformProject(new Vector3f(ndcX, ndcY, -1));
        var far = invVp.transformProject(new Vector3f(ndcX, ndcY, 1));
        return new Rayf(
            near.x(), near.y(), near.z(),
            far.x() - near.x(), far.y() - near.y(), far.z() - near.z()
        ).normalize();
    }
}

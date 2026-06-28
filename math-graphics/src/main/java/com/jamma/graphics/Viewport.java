package com.jamma.graphics;

public record Viewport(int x, int y, int width, int height) {

    public float aspect() {
        return (float) width / height;
    }

    public float normalizedToScreenX(float nx) {
        return x + (nx + 1) * 0.5f * width;
    }

    public float normalizedToScreenY(float ny) {
        return y + (1 - ny) * 0.5f * height;
    }

    public float screenToNormalizedX(float sx) {
        return (sx - x) / width * 2 - 1;
    }

    public float screenToNormalizedY(float sy) {
        return 1 - (sy - y) / height * 2;
    }

    public float[] normalizedToScreen(float nx, float ny) {
        return new float[]{normalizedToScreenX(nx), normalizedToScreenY(ny)};
    }

    public float[] screenToNormalized(float sx, float sy) {
        return new float[]{screenToNormalizedX(sx), screenToNormalizedY(sy)};
    }
}

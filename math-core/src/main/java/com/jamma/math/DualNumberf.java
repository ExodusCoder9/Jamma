package com.jamma.math;

import java.io.Serial;
import java.io.Serializable;

public record DualNumberf(float value, float derivative) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final DualNumberf ZERO = new DualNumberf(0.0f, 0.0f);
    public static final DualNumberf ONE = new DualNumberf(1.0f, 0.0f);

    public DualNumberf(float value) {
        this(value, 0.0f);
    }

    public DualNumberf add(DualNumberf other) {
        return new DualNumberf(value + other.value, derivative + other.derivative);
    }

    public DualNumberf sub(DualNumberf other) {
        return new DualNumberf(value - other.value, derivative - other.derivative);
    }

    public DualNumberf mul(DualNumberf other) {
        return new DualNumberf(value * other.value, Math.fma(value, other.derivative, derivative * other.value));
    }

    public DualNumberf div(DualNumberf other) {
        float inv = 1.0f / other.value;
        return new DualNumberf(value * inv, (derivative - value * inv * other.derivative) * inv);
    }

    public DualNumberf negate() {
        return new DualNumberf(-value, -derivative);
    }

    public DualNumberf add(float s) {
        return new DualNumberf(value + s, derivative);
    }

    public DualNumberf sub(float s) {
        return new DualNumberf(value - s, derivative);
    }

    public DualNumberf mul(float s) {
        return new DualNumberf(value * s, derivative * s);
    }

    public DualNumberf div(float s) {
        float inv = 1.0f / s;
        return new DualNumberf(value * inv, derivative * inv);
    }

    public DualNumberf sin() {
        float s = (float) Math.sin(value);
        return new DualNumberf(s, derivative * (float) Math.cos(value));
    }

    public DualNumberf cos() {
        float c = (float) Math.cos(value);
        return new DualNumberf(c, -derivative * (float) Math.sin(value));
    }

    public DualNumberf tan() {
        float t = (float) Math.tan(value);
        return new DualNumberf(t, derivative * (1.0f + t * t));
    }

    public DualNumberf exp() {
        float e = (float) Math.exp(value);
        return new DualNumberf(e, derivative * e);
    }

    public DualNumberf log() {
        return new DualNumberf((float) Math.log(value), derivative / value);
    }

    public DualNumberf sqrt() {
        float s = (float) Math.sqrt(value);
        return new DualNumberf(s, derivative / (2.0f * s));
    }

    public DualNumberf pow(float n) {
        return new DualNumberf((float) Math.pow(value, n), derivative * n * (float) Math.pow(value, n - 1.0f));
    }

    public DualNumberf pow(DualNumberf other) {
        float v = (float) Math.pow(value, other.value);
        return new DualNumberf(v, v * (derivative * other.value / value + other.derivative * (float) Math.log(value)));
    }

    public DualNumberd toDouble() {
        return new DualNumberd(value, derivative);
    }

    @Override
    public String toString() {
        return "DualNumberf[" + value + ", " + derivative + "]";
    }
}

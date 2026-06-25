package com.jamma.math;

import java.io.Serial;
import java.io.Serializable;

public record DualNumberd(double value, double derivative) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final DualNumberd ZERO = new DualNumberd(0.0, 0.0);
    public static final DualNumberd ONE = new DualNumberd(1.0, 0.0);

    public DualNumberd(double value) {
        this(value, 0.0);
    }

    public DualNumberd add(DualNumberd other) {
        return new DualNumberd(value + other.value, derivative + other.derivative);
    }

    public DualNumberd sub(DualNumberd other) {
        return new DualNumberd(value - other.value, derivative - other.derivative);
    }

    public DualNumberd mul(DualNumberd other) {
        return new DualNumberd(value * other.value, Math.fma(value, other.derivative, derivative * other.value));
    }

    public DualNumberd div(DualNumberd other) {
        double inv = 1.0 / other.value;
        return new DualNumberd(value * inv, (derivative - value * inv * other.derivative) * inv);
    }

    public DualNumberd negate() {
        return new DualNumberd(-value, -derivative);
    }

    public DualNumberd add(double s) {
        return new DualNumberd(value + s, derivative);
    }

    public DualNumberd sub(double s) {
        return new DualNumberd(value - s, derivative);
    }

    public DualNumberd mul(double s) {
        return new DualNumberd(value * s, derivative * s);
    }

    public DualNumberd div(double s) {
        double inv = 1.0 / s;
        return new DualNumberd(value * inv, derivative * inv);
    }

    public DualNumberd sin() {
        double s = Math.sin(value);
        return new DualNumberd(s, derivative * Math.cos(value));
    }

    public DualNumberd cos() {
        double c = Math.cos(value);
        return new DualNumberd(c, -derivative * Math.sin(value));
    }

    public DualNumberd tan() {
        double t = Math.tan(value);
        return new DualNumberd(t, derivative * (1.0 + t * t));
    }

    public DualNumberd exp() {
        double e = Math.exp(value);
        return new DualNumberd(e, derivative * e);
    }

    public DualNumberd log() {
        return new DualNumberd(Math.log(value), derivative / value);
    }

    public DualNumberd sqrt() {
        double s = Math.sqrt(value);
        return new DualNumberd(s, derivative / (2.0 * s));
    }

    public DualNumberd pow(double n) {
        return new DualNumberd(Math.pow(value, n), derivative * n * Math.pow(value, n - 1.0));
    }

    public DualNumberd pow(DualNumberd other) {
        double v = Math.pow(value, other.value);
        return new DualNumberd(v, v * (derivative * other.value / value + other.derivative * Math.log(value)));
    }

    public DualNumberf toFloat() {
        return new DualNumberf((float) value, (float) derivative);
    }

    @Override
    public String toString() {
        return "DualNumberd[" + value + ", " + derivative + "]";
    }
}

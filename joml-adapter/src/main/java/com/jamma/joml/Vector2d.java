package com.jamma.joml;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vector2d {

    public double x;
    public double y;

    public Vector2d() {
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2d(double[] d) {
        this.x = d[0];
        this.y = d[1];
    }

    public Vector2d(com.jamma.math.Vector2d jamma) {
        this.x = jamma.x();
        this.y = jamma.y();
    }

    public com.jamma.math.Vector2d toJamma() {
        return new com.jamma.math.Vector2d(x, y);
    }

    public static Vector2d fromJamma(com.jamma.math.Vector2d jamma) {
        return new Vector2d(jamma.x(), jamma.y());
    }

    public Vector2d set(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2d set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2d set(double[] d) {
        this.x = d[0];
        this.y = d[1];
        return this;
    }

    public Vector2d add(Vector2d v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vector2d add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2d sub(Vector2d v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector2d sub(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2d mul(Vector2d v) {
        x *= v.x;
        y *= v.y;
        return this;
    }

    public Vector2d mul(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vector2d mul(double x, double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2d div(Vector2d v) {
        x /= v.x;
        y /= v.y;
        return this;
    }

    public Vector2d div(double scalar) {
        x /= scalar;
        y /= scalar;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public Vector2d normalize() {
        double inv = 1.0 / length();
        x *= inv;
        y *= inv;
        return this;
    }

    public Vector2d negate() {
        x = -x;
        y = -y;
        return this;
    }

    public double dot(Vector2d v) {
        return Math.fma(x, v.x, y * v.y);
    }

    public double distance(Vector2d v) {
        double dx = x - v.x;
        double dy = y - v.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceSquared(Vector2d v) {
        double dx = x - v.x;
        double dy = y - v.y;
        return dx * dx + dy * dy;
    }

    public double cross(Vector2d v) {
        return x * v.y - y * v.x;
    }

    public double angle(Vector2d v) {
        double d = dot(v) / (length() * v.length());
        return Math.acos(Math.clamp(d, -1.0, 1.0));
    }

    public Vector2d lerp(Vector2d other, double t) {
        x = Math.fma(t, other.x - x, x);
        y = Math.fma(t, other.y - y, y);
        return this;
    }

    public Vector2d rotate(double ang) {
        double c = Math.cos(ang);
        double s = Math.sin(ang);
        double tx = x * c - y * s;
        double ty = x * s + y * c;
        x = tx;
        y = ty;
        return this;
    }

    public Vector2d perpendicular() {
        double tx = -y;
        y = x;
        x = tx;
        return this;
    }

    public Vector2d zero() {
        x = y = 0.0;
        return this;
    }

    public Vector2d abs() {
        x = Math.abs(x);
        y = Math.abs(y);
        return this;
    }

    public double get(int component) {
        return switch (component) {
            case 0 -> x;
            case 1 -> y;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public double[] get(double[] dest) {
        dest[0] = x;
        dest[1] = y;
        return dest;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2d v)) return false;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(v.x)
            && Double.doubleToLongBits(y) == Double.doubleToLongBits(v.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

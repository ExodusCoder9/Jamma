package com.jamma.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MathLibTest {

    @Test void sin() { assertEquals(0.0, MathLib.sin(0.0), 1e-15); assertEquals(1.0, MathLib.sin(Math.PI / 2.0), 1e-15); }
    @Test void cos() { assertEquals(1.0, MathLib.cos(0.0), 1e-15); assertEquals(0.0, MathLib.cos(Math.PI / 2.0), 1e-15); }
    @Test void tan() { assertEquals(0.0, MathLib.tan(0.0), 1e-15); assertEquals(1.0, MathLib.tan(Math.PI / 4.0), 1e-7); }
    @Test void sqrt() { assertEquals(2.0, MathLib.sqrt(4.0), 1e-15); assertEquals(0.0, MathLib.sqrt(0.0), 1e-15); }
    @Test void invSqrt() { assertEquals(0.5, MathLib.invSqrt(4.0), 1e-15); }
    @Test void sec() { assertEquals(1.0, MathLib.sec(0.0), 1e-15); }
    @Test void csc() { assertEquals(1.0, MathLib.csc(Math.PI / 2.0), 1e-15); }
    @Test void cot() { assertEquals(0.0, MathLib.cot(Math.PI / 2.0), 1e-15); }
    @Test void asinh() { assertEquals(0.0, MathLib.asinh(0.0), 1e-15); }
    @Test void acosh() { assertEquals(0.0, MathLib.acosh(1.0), 1e-15); }
    @Test void atanh() { assertEquals(0.0, MathLib.atanh(0.0), 1e-15); }

    @Test void sinc() { assertEquals(1.0, MathLib.sinc(0.0), 1e-15); assertEquals(0.0, MathLib.sinc(Math.PI), 1e-10); }

    @Test void clamp() {
        assertEquals(5.0, MathLib.clamp(10.0, 0.0, 5.0), 1e-15);
        assertEquals(0.0, MathLib.clamp(-1.0, 0.0, 5.0), 1e-15);
        assertEquals(3, MathLib.clamp(10, 1, 3));
    }
    @Test void saturate() { assertEquals(0.5, MathLib.saturate(0.5), 1e-15); assertEquals(1.0, MathLib.saturate(2.0), 1e-15); }
    @Test void lerp() { assertEquals(5.0, MathLib.lerp(0.0, 10.0, 0.5), 1e-15); }
    @Test void map() { assertEquals(50.0, MathLib.map(0.5, 0.0, 1.0, 0.0, 100.0), 1e-15); }
    @Test void smoothstep() { assertEquals(0.5, MathLib.smoothstep(0.0, 1.0, 0.5), 1e-10); }
    @Test void smootherstep() { assertEquals(0.5, MathLib.smootherstep(0.0, 1.0, 0.5), 1e-10); }
    @Test void cosineInterpolation() { assertEquals(0.5, MathLib.cosineInterpolation(0.0, 1.0, 0.5), 1e-10); }
    @Test void wrap() { assertEquals(0.2, MathLib.wrap(1.2, 0.0, 1.0), 1e-15); }
    @Test void pingPong() { assertEquals(0.5, MathLib.pingPong(0.5, 1.0), 1e-15); assertEquals(0.5, MathLib.pingPong(1.5, 1.0), 1e-15); }
    @Test void sawtooth() { assertEquals(0.0, MathLib.sawtooth(0.0), 1e-15); }
    @Test void triangle() { assertEquals(-1.0, MathLib.triangle(0.0), 1e-15); }
    @Test void square() { assertEquals(1.0, MathLib.square(0.0), 1e-15); }

    @Test void relu() { assertEquals(5.0, MathLib.relu(5.0), 1e-15); assertEquals(0.0, MathLib.relu(-3.0), 1e-15); }
    @Test void gelu() { assertEquals(0.0, MathLib.gelu(-1e10), 1e-10); }
    @Test void logistic() { assertEquals(0.5, MathLib.logistic(0.0), 1e-15); }
    @Test void softsign() { assertEquals(0.5, MathLib.softsign(1.0), 1e-15); }
    @Test void swish() { assertEquals(0.0, MathLib.swish(0.0), 1e-15); }
    @Test void hardSigmoid() { assertEquals(0.5, MathLib.hardSigmoid(0.0), 1e-15); }

    @Test void erf() { assertEquals(0.0, MathLib.erf(0.0), 1e-15); assertEquals(0.8427, MathLib.erf(1.0), 1e-3); }
    @Test void gamma() { assertEquals(1.0, MathLib.gamma(1.0), 1e-10); assertEquals(1.0, MathLib.gamma(2.0), 1e-10); assertEquals(2.0, MathLib.gamma(3.0), 1e-10); }
    @Test void beta() { assertEquals(1.0, MathLib.beta(1.0, 1.0), 1e-10); }

    @Test void factorial() { assertEquals(6.0, MathLib.factorial(3), 1e-15); assertEquals(120.0, MathLib.factorial(5), 1e-15); }
    @Test void binomial() { assertEquals(10.0, MathLib.binomial(5, 2), 1e-15); }
    @Test void permutations() { assertEquals(60, MathLib.permutations(5, 3)); }

    @Test void stirling() {
        assertEquals(1.0, MathLib.stirlingFirstKind(0, 0), 1e-15);
        assertEquals(1.0, MathLib.stirlingSecondKind(3, 3), 1e-15);
        assertEquals(3.0, MathLib.stirlingSecondKind(3, 2), 1e-15);
    }

    @Test void gcd() { assertEquals(6, MathLib.gcd(12, 18)); }
    @Test void lcm() { assertEquals(36, MathLib.lcm(12, 18)); }
    @Test void isPrime() { assertTrue(MathLib.isPrime(7)); assertFalse(MathLib.isPrime(1)); assertFalse(MathLib.isPrime(4)); }

    @Test void lerpAngle() {
        assertEquals(Math.PI / 2.0, MathLib.lerpAngle(0.0, Math.PI, 0.5), 1e-10);
    }

    @Test void statistics() {
        double[] a = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertEquals(15.0, MathLib.sum(a), 1e-15);
        assertEquals(3.0, MathLib.mean(a), 1e-15);
        assertEquals(1.5811388300841898, MathLib.stddev(a), 1e-10);
        assertEquals(1.0, MathLib.arrayMin(a), 1e-15);
        assertEquals(5.0, MathLib.arrayMax(a), 1e-15);
        assertEquals(3.0, MathLib.median(a), 1e-15);
    }

    @Test void covariance() {
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, 5.0, 6.0};
        assertEquals(1.0, MathLib.covariance(a, b), 1e-10);
    }

    @Test void correlation() {
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, 5.0, 6.0};
        assertEquals(1.0, MathLib.correlation(a, b), 1e-10);
    }

    @Test void bilinear() {
        assertEquals(2.5, MathLib.bilinear(1.0, 2.0, 3.0, 4.0, 0.5, 0.5), 1e-15);
    }

    @Test void trunc() { assertEquals(3.0, MathLib.trunc(3.7), 1e-15); assertEquals(-3.0, MathLib.trunc(-3.7), 1e-15); }
    @Test void frac() { assertEquals(0.7, MathLib.frac(3.7), 1e-15); }
    @Test void copySign() { assertEquals(-5.0, MathLib.copySign(5.0, -1.0), 1e-15); }
    @Test void constants() { assertEquals(Math.PI, MathLib.PI, 1e-15); }

    @Test void expm1() { assertEquals(0.0, MathLib.expm1(0.0), 1e-15); }
    @Test void log1p() { assertEquals(0.0, MathLib.log1p(0.0), 1e-15); }
    @Test void log2() { assertEquals(1.0, MathLib.log2(2.0), 1e-15); }
    @Test void exp2() { assertEquals(4.0, MathLib.exp2(2.0), 1e-15); }

    @Test void catmullRomScalar() { assertEquals(1.0, MathLib.catmullRom(3.0, 1.0, 2.0, 5.0, 0.0), 1e-15); }
    @Test void hermiteScalar() { assertEquals(0.0, MathLib.hermite(0.0, 1.0, 0.0, 0.0, 0.0), 1e-15); }
    @Test void bezierScalar() { assertEquals(0.5, MathLib.bezier(0.0, 0.0, 1.0, 1.0, 0.5), 1e-15); }
    @Test void step() { assertEquals(1.0, MathLib.step(0.5, 1.0), 1e-15); }
    @Test void pulse() { assertEquals(1.0, MathLib.pulse(0.0, 1.0, 0.5), 1e-15); }
    @Test void ramp() { assertEquals(5.0, MathLib.ramp(5.0), 1e-15); assertEquals(0.0, MathLib.ramp(-3.0), 1e-15); }
    @Test void normalizeAngle() { assertEquals(0.0, MathLib.normalizeAngle(MathLib.TAU), 1e-15); }
    @Test void gaussian() { assertTrue(MathLib.gaussian(0.0, 0.0, 1.0) > 0.3); }
}

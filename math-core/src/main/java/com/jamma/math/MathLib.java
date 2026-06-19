package com.jamma.math;

import com.jamma.math.scalar.ScalarMath;

public final class MathLib {

    private MathLib() {
    }

    public static final double PI = ScalarMath.PI;
    public static final double E = ScalarMath.E;
    public static final double TAU = ScalarMath.TAU;
    public static final double PHI = ScalarMath.PHI;
    public static final double LN2 = ScalarMath.LN2;
    public static final double LN10 = ScalarMath.LN10;
    public static final double SQRT2 = ScalarMath.SQRT2;
    public static final double SQRT3 = ScalarMath.SQRT3;
    public static final double EPSILON = ScalarMath.EPSILON;
    public static final double FLT_EPSILON = ScalarMath.FLT_EPSILON;

    public static double sin(double x) { return ScalarMath.sin(x); }
    public static double cos(double x) { return ScalarMath.cos(x); }
    public static double tan(double x) { return ScalarMath.tan(x); }
    public static double asin(double x) { return ScalarMath.asin(x); }
    public static double acos(double x) { return ScalarMath.acos(x); }
    public static double atan(double x) { return ScalarMath.atan(x); }
    public static double atan2(double y, double x) { return ScalarMath.atan2(y, x); }
    public static double sec(double x) { return ScalarMath.sec(x); }
    public static double csc(double x) { return ScalarMath.csc(x); }
    public static double cot(double x) { return ScalarMath.cot(x); }
    public static double asec(double x) { return ScalarMath.asec(x); }
    public static double acsc(double x) { return ScalarMath.acsc(x); }
    public static double acot(double x) { return ScalarMath.acot(x); }
    public static double versin(double x) { return ScalarMath.versin(x); }
    public static double coversin(double x) { return ScalarMath.coversin(x); }
    public static double haversin(double x) { return ScalarMath.haversin(x); }
    public static double exsec(double x) { return ScalarMath.exsec(x); }
    public static double sinc(double x) { return ScalarMath.sinc(x); }

    public static double sinh(double x) { return ScalarMath.sinh(x); }
    public static double cosh(double x) { return ScalarMath.cosh(x); }
    public static double tanh(double x) { return ScalarMath.tanh(x); }
    public static double sech(double x) { return ScalarMath.sech(x); }
    public static double csch(double x) { return ScalarMath.csch(x); }
    public static double coth(double x) { return ScalarMath.coth(x); }
    public static double asinh(double x) { return ScalarMath.asinh(x); }
    public static double acosh(double x) { return ScalarMath.acosh(x); }
    public static double atanh(double x) { return ScalarMath.atanh(x); }

    public static double sqrt(double x) { return ScalarMath.sqrt(x); }
    public static double invSqrt(double x) { return ScalarMath.invSqrt(x); }
    public static double cbrt(double x) { return ScalarMath.cbrt(x); }
    public static double pow(double a, double b) { return ScalarMath.pow(a, b); }
    public static double exp(double x) { return ScalarMath.exp(x); }
    public static double exp2(double x) { return ScalarMath.exp2(x); }
    public static double expm1(double x) { return ScalarMath.expm1(x); }
    public static double log(double x) { return ScalarMath.log(x); }
    public static double log10(double x) { return ScalarMath.log10(x); }
    public static double log2(double x) { return ScalarMath.log2(x); }
    public static double log1p(double x) { return ScalarMath.log1p(x); }
    public static double logb(double x) { return ScalarMath.logb(x); }

    public static double abs(double x) { return ScalarMath.abs(x); }
    public static float abs(float x) { return ScalarMath.abs(x); }
    public static int abs(int x) { return ScalarMath.abs(x); }
    public static long abs(long x) { return ScalarMath.abs(x); }
    public static double min(double a, double b) { return ScalarMath.min(a, b); }
    public static double max(double a, double b) { return ScalarMath.max(a, b); }
    public static int min(int a, int b) { return ScalarMath.min(a, b); }
    public static int max(int a, int b) { return ScalarMath.max(a, b); }
    public static double clamp(double value, double min, double max) { return ScalarMath.clamp(value, min, max); }
    public static int clamp(int value, int min, int max) { return ScalarMath.clamp(value, min, max); }
    public static double saturate(double value) { return ScalarMath.saturate(value); }
    public static double floor(double x) { return ScalarMath.floor(x); }
    public static double ceil(double x) { return ScalarMath.ceil(x); }
    public static double round(double x) { return ScalarMath.round(x); }
    public static double trunc(double x) { return ScalarMath.trunc(x); }
    public static double frac(double x) { return ScalarMath.frac(x); }
    public static double signum(double x) { return ScalarMath.signum(x); }
    public static double copySign(double magnitude, double sign) { return ScalarMath.copySign(magnitude, sign); }
    public static double nextUp(double x) { return ScalarMath.nextUp(x); }
    public static double nextDown(double x) { return ScalarMath.nextDown(x); }
    public static double ulp(double x) { return ScalarMath.ulp(x); }
    public static double mod(double x, double y) { return ScalarMath.mod(x, y); }
    public static double wrap(double value, double min, double max) { return ScalarMath.wrap(value, min, max); }
    public static double pingPong(double t, double length) { return ScalarMath.pingPong(t, length); }
    public static double sawtooth(double t) { return ScalarMath.sawtooth(t); }
    public static double triangle(double t) { return ScalarMath.triangle(t); }
    public static double square(double t) { return ScalarMath.square(t); }

    public static double lerp(double a, double b, double t) { return ScalarMath.lerp(a, b, t); }
    public static double lerpAngle(double a, double b, double t) { return ScalarMath.lerpAngle(a, b, t); }
    public static double bilinear(double tl, double tr, double bl, double br, double tx, double ty) { return ScalarMath.bilinear(tl, tr, bl, br, tx, ty); }
    public static double inverseLerp(double a, double b, double value) { return ScalarMath.inverseLerp(a, b, value); }
    public static double map(double value, double inLow, double inHigh, double outLow, double outHigh) { return ScalarMath.map(value, inLow, inHigh, outLow, outHigh); }
    public static double smoothstep(double edge0, double edge1, double x) { return ScalarMath.smoothstep(edge0, edge1, x); }
    public static double smootherstep(double edge0, double edge1, double x) { return ScalarMath.smootherstep(edge0, edge1, x); }
    public static double cosineInterpolation(double a, double b, double t) { return ScalarMath.cosineInterpolation(a, b, t); }
    public static double catmullRom(double a, double b, double c, double d, double t) { return ScalarMath.catmullRom(a, b, c, d, t); }
    public static double hermite(double a, double b, double t, double tension, double bias) { return ScalarMath.hermite(a, b, t, tension, bias); }
    public static double bezier(double a, double b, double c, double d, double t) { return ScalarMath.bezier(a, b, c, d, t); }
    public static double step(double edge, double x) { return ScalarMath.step(edge, x); }
    public static double pulse(double start, double end, double x) { return ScalarMath.pulse(start, end, x); }
    public static double ramp(double x) { return ScalarMath.ramp(x); }
    public static double fma(double a, double b, double c) { return ScalarMath.fma(a, b, c); }
    public static double toRadians(double degrees) { return ScalarMath.toRadians(degrees); }
    public static double toDegrees(double radians) { return ScalarMath.toDegrees(radians); }
    public static double normalizeAngle(double angle) { return ScalarMath.normalizeAngle(angle); }
    public static double degrees(double x) { return ScalarMath.degrees(x); }
    public static double radians(double x) { return ScalarMath.radians(x); }

    public static double gaussian(double x, double mean, double sigma) { return ScalarMath.gaussian(x, mean, sigma); }
    public static double logistic(double x) { return ScalarMath.logistic(x); }
    public static double logit(double p) { return ScalarMath.logit(p); }
    public static double softplus(double x) { return ScalarMath.softplus(x); }
    public static double relu(double x) { return ScalarMath.relu(x); }
    public static double leakyRelu(double x, double alpha) { return ScalarMath.leakyRelu(x, alpha); }
    public static double elu(double x, double alpha) { return ScalarMath.elu(x, alpha); }
    public static double gelu(double x) { return ScalarMath.gelu(x); }
    public static double softsign(double x) { return ScalarMath.softsign(x); }
    public static double swish(double x) { return ScalarMath.swish(x); }
    public static double hardSigmoid(double x) { return ScalarMath.hardSigmoid(x); }
    public static double selu(double x) { return ScalarMath.selu(x); }
    public static double erf(double x) { return ScalarMath.erf(x); }
    public static double erfc(double x) { return ScalarMath.erfc(x); }
    public static double beta(double a, double b) { return ScalarMath.beta(a, b); }
    public static double gamma(double x) { return ScalarMath.gamma(x); }
    public static double lgamma(double x) { return ScalarMath.lgamma(x); }
    public static double factorial(int n) { return ScalarMath.factorial(n); }
    public static long factorialLong(int n) { return ScalarMath.factorialLong(n); }
    public static double binomial(int n, int k) { return ScalarMath.binomial(n, k); }
    public static long permutations(int n, int k) { return ScalarMath.permutations(n, k); }
    public static double stirlingFirstKind(int n, int k) { return ScalarMath.stirlingFirstKind(n, k); }
    public static double stirlingSecondKind(int n, int k) { return ScalarMath.stirlingSecondKind(n, k); }
    public static int gcd(int a, int b) { return ScalarMath.gcd(a, b); }
    public static int lcm(int a, int b) { return ScalarMath.lcm(a, b); }
    public static boolean isPrime(int n) { return ScalarMath.isPrime(n); }

    public static double sum(double[] a) { return ScalarMath.sum(a); }
    public static double product(double[] a) { return ScalarMath.product(a); }
    public static double mean(double[] a) { return ScalarMath.mean(a); }
    public static double median(double[] a) { return ScalarMath.median(a); }
    public static double percentile(double[] a, double p) { return ScalarMath.percentile(a, p); }
    public static double covariance(double[] a, double[] b) { return ScalarMath.covariance(a, b); }
    public static double correlation(double[] a, double[] b) { return ScalarMath.correlation(a, b); }
    public static double variance(double[] a) { return ScalarMath.variance(a); }
    public static double populationVariance(double[] a) { return ScalarMath.populationVariance(a); }
    public static double stddev(double[] a) { return ScalarMath.stddev(a); }
    public static double populationStddev(double[] a) { return ScalarMath.populationStddev(a); }
    public static double arrayMin(double[] a) { return ScalarMath.arrayMin(a); }
    public static double arrayMax(double[] a) { return ScalarMath.arrayMax(a); }
    public static int argmin(double[] a) { return ScalarMath.argmin(a); }
    public static int argmax(double[] a) { return ScalarMath.argmax(a); }

    public static boolean isFinite(double x) { return ScalarMath.isFinite(x); }
    public static boolean isNaN(double x) { return ScalarMath.isNaN(x); }
    public static boolean isInfinite(double x) { return ScalarMath.isInfinite(x); }
}

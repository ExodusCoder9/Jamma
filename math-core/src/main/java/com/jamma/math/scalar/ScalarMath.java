package com.jamma.math.scalar;

/**
 * Static math utilities for scalar (non-vector) double-precision operations.
 * <p>
 * Provides constants, special functions (gamma, beta, erf, stirling), and
 * common helpers (clamp, lerp, smoothstep, rounding, statistics).
 * <p>
 * All methods operate on primitive {@code double} values and return
 * primitive results — no allocations.
 */
public final class ScalarMath {

    private ScalarMath() {
    }

    public static final double PI = Math.PI;
    public static final double E = Math.E;
    public static final double TAU = 2.0 * Math.PI;
    public static final double PHI = 1.6180339887498948482;
    public static final double LN2 = Math.log(2.0);
    public static final double LN10 = Math.log(10.0);
    public static final double LOG2E = 1.0 / LN2;
    public static final double LOG10E = 1.0 / LN10;
    public static final double SQRT2 = Math.sqrt(2.0);
    public static final double SQRT3 = Math.sqrt(3.0);
    public static final double EPSILON = 2.220446049250313E-16;
    public static final double FLT_EPSILON = 1.1920929E-7f;

    private static final double DEG_TO_RAD = PI / 180.0;
    private static final double RAD_TO_DEG = 180.0 / PI;

    private static final double ERF_A1 = 0.254829592;
    private static final double ERF_A2 = -0.284496736;
    private static final double ERF_A3 = 1.421413741;
    private static final double ERF_A4 = -1.453152027;
    private static final double ERF_A5 = 1.061405429;
    private static final double ERF_P = 0.3275911;

    private static final int GAMMA_N = 9;
    private static final double[] GAMMA_P = {
        0.99999999999980993, 676.5203681218851, -1259.1392167224028,
        771.32342877765313, -176.61502916214059, 12.507343278686905,
        -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7
    };

    public static double sin(double x) { return Math.sin(x); }
    public static double cos(double x) { return Math.cos(x); }
    public static double tan(double x) { return Math.tan(x); }
    public static double asin(double x) { return Math.asin(x); }
    public static double acos(double x) { return Math.acos(x); }
    public static double atan(double x) { return Math.atan(x); }
    public static double atan2(double y, double x) { return Math.atan2(y, x); }

    public static double sec(double x) { return 1.0 / Math.cos(x); }
    public static double csc(double x) { return 1.0 / Math.sin(x); }
    public static double cot(double x) { return Math.cos(x) / Math.sin(x); }
    public static double asec(double x) { return Math.acos(1.0 / x); }
    public static double acsc(double x) { return Math.asin(1.0 / x); }
    public static double acot(double x) { return Math.atan2(1.0, x); }

    public static double versin(double x) { return 1.0 - Math.cos(x); }
    public static double coversin(double x) { return 1.0 - Math.sin(x); }
    public static double haversin(double x) { return (1.0 - Math.cos(x)) / 2.0; }
    public static double exsec(double x) { return sec(x) - 1.0; }
    public static double excsc(double x) { return csc(x) - 1.0; }
    public static double vercosin(double x) { return 1.0 + Math.cos(x); }
    public static double covercosin(double x) { return 1.0 + Math.sin(x); }
    public static double havercosin(double x) { return (1.0 + Math.cos(x)) / 2.0; }
    public static double hacoversin(double x) { return (1.0 - Math.sin(x)) / 2.0; }
    public static double hacovercosin(double x) { return (1.0 + Math.sin(x)) / 2.0; }
    public static double chord(double x) { return 2.0 * Math.sin(x / 2.0); }
    public static double sinc(double x) {
        if (x == 0.0) return 1.0;
        return Math.sin(x) / x;
    }

    public static double hypot(double a, double b) { return Math.hypot(a, b); }
    public static double hypot3(double a, double b, double c) { return Math.sqrt(a * a + b * b + c * c); }

    public static double roundTo(double value, double multiple) { return Math.round(value / multiple) * multiple; }
    public static double floorTo(double value, double multiple) { return Math.floor(value / multiple) * multiple; }
    public static double ceilTo(double value, double multiple) { return Math.ceil(value / multiple) * multiple; }
    public static double snap(double value, double grid) { return Math.round(value / grid) * grid; }

    public static double sinh(double x) { return Math.sinh(x); }
    public static double cosh(double x) { return Math.cosh(x); }
    public static double tanh(double x) { return Math.tanh(x); }
    public static double sech(double x) { return 1.0 / Math.cosh(x); }
    public static double csch(double x) { return 1.0 / Math.sinh(x); }
    public static double coth(double x) { return Math.cosh(x) / Math.sinh(x); }
    public static double asinh(double x) { return Math.log(x + Math.sqrt(x * x + 1.0)); }
    public static double acosh(double x) { return Math.log(x + Math.sqrt(x * x - 1.0)); }
    public static double atanh(double x) { return 0.5 * Math.log((1.0 + x) / (1.0 - x)); }

    public static double sqrt(double x) { return Math.sqrt(x); }
    public static double invSqrt(double x) { return 1.0 / Math.sqrt(x); }
    public static double cbrt(double x) { return Math.cbrt(x); }
    public static double pow(double a, double b) { return Math.pow(a, b); }
    public static double exp(double x) { return Math.exp(x); }
    public static double exp2(double x) { return Math.pow(2.0, x); }
    public static double expm1(double x) { return Math.expm1(x); }
    public static double log(double x) { return Math.log(x); }
    public static double log10(double x) { return Math.log10(x); }
    public static double log2(double x) { return Math.log(x) / LN2; }
    public static double log1p(double x) { return Math.log1p(x); }
    public static double logb(double x) { return Math.getExponent(x); }

    public static double abs(double x) { return Math.abs(x); }
    public static float abs(float x) { return Math.abs(x); }
    public static int abs(int x) { return Math.abs(x); }
    public static long abs(long x) { return Math.abs(x); }

    public static double min(double a, double b) { return Math.min(a, b); }
    public static double max(double a, double b) { return Math.max(a, b); }
    public static int min(int a, int b) { return Math.min(a, b); }
    public static int max(int a, int b) { return Math.max(a, b); }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
    public static double saturate(double value) { return clamp(value, 0.0, 1.0); }

    public static double floor(double x) { return Math.floor(x); }
    public static double ceil(double x) { return Math.ceil(x); }
    public static double round(double x) { return Math.round(x); }
    public static double trunc(double x) { return x < 0.0 ? Math.ceil(x) : Math.floor(x); }
    public static double frac(double x) { return x - trunc(x); }

    public static double signum(double x) { return Math.signum(x); }
    public static double copySign(double magnitude, double sign) { return Math.copySign(magnitude, sign); }
    public static double nextUp(double x) { return Math.nextUp(x); }
    public static double nextDown(double x) { return Math.nextDown(x); }
    public static double ulp(double x) { return Math.ulp(x); }

    public static double mod(double x, double y) { return x % y; }
    public static double wrap(double value, double min, double max) {
        double range = max - min;
        return min + (((value - min) % range) + range) % range;
    }
    public static double pingPong(double t, double length) {
        t = mod(t, length * 2.0);
        return length - Math.abs(t - length);
    }

    public static double sawtooth(double t) { return 2.0 * (t - Math.floor(t + 0.5)); }
    public static double triangle(double t) { return 2.0 * Math.abs(2.0 * (t - Math.floor(t + 0.5))) - 1.0; }
    public static double square(double t) { return Math.sin(t) >= 0.0 ? 1.0 : -1.0; }

    public static double lerp(double a, double b, double t) {
        return Math.fma(t, b - a, a);
    }
    public static double inverseLerp(double a, double b, double value) {
        return (value - a) / (b - a);
    }
    public static double map(double value, double inLow, double inHigh, double outLow, double outHigh) {
        return lerp(outLow, outHigh, inverseLerp(inLow, inHigh, value));
    }
    public static double lerpAngle(double a, double b, double t) {
        double diff = b - a;
        diff %= TAU;
        if (diff > PI) diff -= TAU;
        else if (diff < -PI) diff += TAU;
        return a + diff * t;
    }

    public static double smoothstep(double edge0, double edge1, double x) {
        double t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }
    public static double smootherstep(double edge0, double edge1, double x) {
        double t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }
    public static double cosineInterpolation(double a, double b, double t) {
        double ct = (1.0 - Math.cos(t * PI)) / 2.0;
        return lerp(a, b, ct);
    }
    public static double catmullRom(double a, double b, double c, double d, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        return 0.5 * ((2.0 * b) + (-a + c) * t + (2.0 * a - 5.0 * b + 4.0 * c - d) * t2 + (-a + 3.0 * b - 3.0 * c + d) * t3);
    }
    public static double hermite(double a, double b, double t, double tension, double bias) {
        double t2 = t * t;
        double t3 = t2 * t;
        double m0 = (b - a) * (1.0 + tension) * (1.0 - bias);
        double m1 = (b - a) * (1.0 + tension) * bias;
        return (2.0 * t3 - 3.0 * t2 + 1.0) * a + (t3 - 2.0 * t2 + t) * m0 + (-2.0 * t3 + 3.0 * t2) * b + (t3 - t2) * m1;
    }
    public static double bezier(double a, double b, double c, double d, double t) {
        double u = 1.0 - t;
        double u2 = u * u;
        double u3 = u2 * u;
        double t2 = t * t;
        double t3 = t2 * t;
        return u3 * a + 3.0 * u2 * t * b + 3.0 * u * t2 * c + t3 * d;
    }

    public static double bilinear(double tl, double tr, double bl, double br, double tx, double ty) {
        double top = lerp(tl, tr, tx);
        double bottom = lerp(bl, br, tx);
        return lerp(top, bottom, ty);
    }

    public static double step(double edge, double x) { return x < edge ? 0.0 : 1.0; }
    public static double pulse(double start, double end, double x) {
        return step(start, x) - step(end, x);
    }
    public static double ramp(double x) { return Math.max(0.0, x); }

    public static double fma(double a, double b, double c) { return Math.fma(a, b, c); }
    public static double toRadians(double degrees) { return degrees * DEG_TO_RAD; }
    public static double toDegrees(double radians) { return radians * RAD_TO_DEG; }
    public static double normalizeAngle(double angle) { return mod(angle, TAU); }
    public static double degrees(double x) { return x * RAD_TO_DEG; }
    public static double radians(double x) { return x * DEG_TO_RAD; }

    public static double gaussian(double x, double mean, double sigma) {
        double d = (x - mean) / sigma;
        return Math.exp(-0.5 * d * d) / (sigma * Math.sqrt(TAU));
    }
    public static double logistic(double x) { return 1.0 / (1.0 + Math.exp(-x)); }
    public static double logit(double p) { return Math.log(p / (1.0 - p)); }
    public static double softplus(double x) { return Math.log1p(Math.exp(x)); }
    public static double relu(double x) { return Math.max(0.0, x); }
    public static double leakyRelu(double x, double alpha) { return x < 0.0 ? alpha * x : x; }
    public static double elu(double x, double alpha) { return x < 0.0 ? alpha * (Math.expm1(x)) : x; }
    public static double selu(double x) {
        double alpha = 1.6732632423543772848170429916717;
        double scale = 1.0507009873554804934193349852946;
        return scale * (x >= 0.0 ? x : alpha * (Math.expm1(x)));
    }
    public static double gelu(double x) { return 0.5 * x * (1.0 + erf(x / SQRT2)); }
    public static double softsign(double x) { return x / (1.0 + Math.abs(x)); }
    public static double swish(double x) { return x * logistic(x); }
    public static double hardSigmoid(double x) { return clamp(x * 0.2 + 0.5, 0.0, 1.0); }

    public static double erf(double x) {
        if (x == 0.0) return 0.0;
        boolean neg = x < 0.0;
        if (neg) x = -x;
        double t = 1.0 / (1.0 + ERF_P * x);
        double y = 1.0 - (((((ERF_A5 * t + ERF_A4) * t) + ERF_A3) * t + ERF_A2) * t + ERF_A1) * t * Math.exp(-x * x);
        return neg ? -y : y;
    }
    public static double erfc(double x) { return 1.0 - erf(x); }

    public static double gamma(double x) {
        if (x <= 0.0 && x == Math.floor(x)) {
            throw new ArithmeticException("Gamma pole at negative integer: " + (int) x);
        }
        if (x < 0.5) {
            double sinTerm = Math.sin(PI * x);
            if (sinTerm == 0.0) throw new ArithmeticException("Gamma pole at " + (int) x);
            return PI / (sinTerm * gamma(1.0 - x));
        }
        x -= 1.0;
        double y = GAMMA_P[0];
        for (int i = 1; i < GAMMA_N; i++) {
            y += GAMMA_P[i] / (x + i);
        }
        double t = x + GAMMA_N - 1.5;
        return Math.sqrt(TAU) * Math.pow(t, x + 0.5) * Math.exp(-t) * y;
    }
    public static double lgamma(double x) { return Math.log(gamma(x)); }

    public static double beta(double a, double b) { return gamma(a) * gamma(b) / gamma(a + b); }

    public static double factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative factorial");
        double r = 1.0;
        for (int i = 2; i <= n; i++) r *= i;
        return r;
    }
    public static long factorialLong(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative factorial");
        if (n > 20) throw new ArithmeticException("Factorial overflow for long");
        long r = 1;
        for (int i = 2; i <= n; i++) r *= i;
        return r;
    }
    public static double binomial(int n, int k) {
        if (k < 0 || k > n) return 0.0;
        if (k == 0 || k == n) return 1.0;
        k = Math.min(k, n - k);
        double r = 1.0;
        for (int i = 1; i <= k; i++) r = r * (n - k + i) / i;
        return r;
    }
    public static long permutations(int n, int k) {
        if (k < 0 || k > n) return 0;
        long r = 1;
        for (int i = n; i > n - k; i--) r *= i;
        return r;
    }
    public static double stirlingFirstKind(int n, int k) {
        if (k < 0 || k > n) return 0.0;
        if (k == 0) return n == 0 ? 1.0 : 0.0;
        if (n == k) return 1.0;
        double[][] dp = new double[n + 1][k + 1];
        dp[0][0] = 1.0;
        for (int i = 1; i <= n; i++) {
            int maxJ = Math.min(i, k);
            for (int j = 1; j <= maxJ; j++) {
                dp[i][j] = dp[i - 1][j - 1] - (i - 1) * dp[i - 1][j];
            }
        }
        return dp[n][k];
    }
    public static double stirlingSecondKind(int n, int k) {
        if (k < 0 || k > n) return 0.0;
        if (k == 0) return n == 0 ? 1.0 : 0.0;
        if (n == k) return 1.0;
        double[] prev = new double[k + 1];
        double[] curr = new double[k + 1];
        prev[0] = 1.0;
        for (int i = 1; i <= n; i++) {
            int maxJ = Math.min(i, k);
            curr[0] = 0.0;
            for (int j = 1; j <= maxJ; j++) {
                curr[j] = prev[j - 1] + j * prev[j];
            }
            double[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[k];
    }

    public static long fibonacci(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative fibonacci");
        if (n == 0) return 0;
        if (n == 1) return 1;
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) { long t = a + b; a = b; b = t; }
        return b;
    }
    public static long lucas(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative lucas");
        if (n == 0) return 2;
        if (n == 1) return 1;
        long a = 2, b = 1;
        for (int i = 2; i <= n; i++) { long t = a + b; a = b; b = t; }
        return b;
    }
    public static long catalan(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative catalan");
        return Math.round(binomial(2 * n, n) / (n + 1.0));
    }
    public static long bellNumber(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative bell");
        long[][] bell = new long[n + 1][n + 1];
        bell[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            bell[i][0] = bell[i - 1][i - 1];
            for (int j = 1; j <= i; j++) bell[i][j] = bell[i - 1][j - 1] + bell[i][j - 1];
        }
        return bell[n][0];
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return Math.abs(a);
    }
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a) / gcd(a, b) * Math.abs(b);
    }
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n < 4) return true;
        if (n % 2 == 0) return false;
        int sqrt = (int) Math.sqrt(n);
        for (int i = 3; i <= sqrt; i += 2) if (n % i == 0) return false;
        return true;
    }
    public static boolean isCoprime(int a, int b) { return gcd(a, b) == 1; }
    public static int nextPrime(int n) {
        if (n < 2) return 2;
        int c = n + 1;
        while (!isPrime(c)) c++;
        return c;
    }
    public static int totient(int n) {
        int r = n;
        for (int p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                while (n % p == 0) n /= p;
                r -= r / p;
            }
        }
        if (n > 1) r -= r / n;
        return r;
    }
    public static int radical(int n) {
        int r = 1;
        for (int p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                r *= p;
                while (n % p == 0) n /= p;
            }
        }
        if (n > 1) r *= n;
        return r;
    }

    public static boolean isPowerOfTwo(int n) { return n > 0 && (n & (n - 1)) == 0; }
    public static int nextPowerOfTwo(int n) {
        if (n <= 1) return 1;
        n--;
        n |= n >> 1; n |= n >> 2; n |= n >> 4; n |= n >> 8; n |= n >> 16;
        return n + 1;
    }
    public static int prevPowerOfTwo(int n) {
        if (n < 1) return 0;
        n |= n >> 1; n |= n >> 2; n |= n >> 4; n |= n >> 8; n |= n >> 16;
        return n - (n >> 1);
    }
    public static int roundUpToPowerOfTwo(int n) {
        if (n < 1) return 1;
        return nextPowerOfTwo(n);
    }

    public static double sum(double[] a) {
        double s = 0.0;
        for (double v : a) s += v;
        return s;
    }
    public static double product(double[] a) {
        double p = 1.0;
        for (double v : a) p *= v;
        return p;
    }
    public static double mean(double[] a) { return sum(a) / a.length; }
    public static double median(double[] a) {
        double[] s = a.clone();
        java.util.Arrays.sort(s);
        int m = s.length / 2;
        return s.length % 2 == 0 ? (s[m - 1] + s[m]) / 2.0 : s[m];
    }
    public static double variance(double[] a) {
        double m = mean(a);
        double s = 0.0;
        for (double v : a) s += (v - m) * (v - m);
        return s / (a.length - 1);
    }
    public static double populationVariance(double[] a) {
        double m = mean(a);
        double s = 0.0;
        for (double v : a) s += (v - m) * (v - m);
        return s / a.length;
    }
    public static double stddev(double[] a) { return Math.sqrt(variance(a)); }
    public static double populationStddev(double[] a) { return Math.sqrt(populationVariance(a)); }
    public static double percentile(double[] a, double p) {
        double[] s = a.clone();
        java.util.Arrays.sort(s);
        double idx = p / 100.0 * (s.length - 1);
        int lo = (int) Math.floor(idx);
        int hi = (int) Math.ceil(idx);
        return lo == hi ? s[lo] : s[lo] + (s[hi] - s[lo]) * (idx - lo);
    }
    public static double covariance(double[] a, double[] b) {
        double ma = mean(a), mb = mean(b);
        double s = 0.0;
        for (int i = 0; i < a.length; i++) s += (a[i] - ma) * (b[i] - mb);
        return s / (a.length - 1);
    }
    public static double correlation(double[] a, double[] b) {
        return covariance(a, b) / (stddev(a) * stddev(b));
    }
    public static double geometricMean(double[] a) {
        double logSum = 0.0;
        for (double v : a) logSum += Math.log(v);
        return Math.exp(logSum / a.length);
    }
    public static double harmonicMean(double[] a) {
        double s = 0.0;
        for (double v : a) s += 1.0 / v;
        return a.length / s;
    }
    public static double quadraticMean(double[] a) {
        double s = 0.0;
        for (double v : a) s += v * v;
        return Math.sqrt(s / a.length);
    }
    public static double weightedMean(double[] values, double[] weights) {
        double sumW = 0.0, sumVW = 0.0;
        for (int i = 0; i < values.length; i++) { sumVW += values[i] * weights[i]; sumW += weights[i]; }
        return sumVW / sumW;
    }
    public static double trimmedMean(double[] a, double trim) {
        double[] s = a.clone();
        java.util.Arrays.sort(s);
        int k = (int) Math.floor(s.length * trim);
        double sum = 0.0;
        for (int i = k; i < s.length - k; i++) sum += s[i];
        return sum / (s.length - 2 * k);
    }
    public static double[] mode(double[] a) {
        java.util.Map<Double, Integer> freq = new java.util.LinkedHashMap<>();
        for (double v : a) freq.merge(v, 1, Integer::sum);
        int max = freq.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        return freq.entrySet().stream().filter(e -> e.getValue() == max).mapToDouble(e -> e.getKey()).toArray();
    }
    public static double skewness(double[] a) {
        double m = mean(a);
        double s = 0.0, n = a.length;
        for (double v : a) { double d = v - m; s += d * d * d; }
        return s / (n * Math.pow(stddev(a), 3));
    }
    public static double kurtosis(double[] a) {
        double m = mean(a);
        double s = 0.0, n = a.length;
        double sd = stddev(a);
        for (double v : a) { double d = v - m; s += d * d * d * d; }
        return s / (n * sd * sd * sd * sd) - 3.0;
    }
    public static double standardError(double[] a) { return stddev(a) / Math.sqrt(a.length); }
    public static double zScore(double value, double mean, double stddev) { return (value - mean) / stddev; }
    public static double entropy(double[] probabilities) {
        double e = 0.0;
        for (double p : probabilities) { if (p > 0) e -= p * Math.log(p); }
        return e;
    }
    public static double distanceManhattan(double[] a, double[] b) {
        double s = 0.0;
        for (int i = 0; i < a.length; i++) s += Math.abs(a[i] - b[i]);
        return s;
    }
    public static double distanceChebyshev(double[] a, double[] b) {
        double m = 0.0;
        for (int i = 0; i < a.length; i++) m = Math.max(m, Math.abs(a[i] - b[i]));
        return m;
    }
    public static double distanceMinkowski(double[] a, double[] b, double p) {
        double s = 0.0;
        for (int i = 0; i < a.length; i++) s += Math.pow(Math.abs(a[i] - b[i]), p);
        return Math.pow(s, 1.0 / p);
    }
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2, double radius) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
        return radius * 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
    }
    public static double areaTriangleHeron(double a, double b, double c) {
        double s = (a + b + c) / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    public static double signedArea2D(double x1, double y1, double x2, double y2, double x3, double y3) {
        return ((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)) / 2.0;
    }

    public static double arrayMin(double[] a) {
        double m = a[0];
        for (int i = 1; i < a.length; i++) if (a[i] < m) m = a[i];
        return m;
    }
    public static double arrayMax(double[] a) {
        double m = a[0];
        for (int i = 1; i < a.length; i++) if (a[i] > m) m = a[i];
        return m;
    }
    public static int argmin(double[] a) {
        int idx = 0;
        for (int i = 1; i < a.length; i++) if (a[i] < a[idx]) idx = i;
        return idx;
    }
    public static int argmax(double[] a) {
        int idx = 0;
        for (int i = 1; i < a.length; i++) if (a[i] > a[idx]) idx = i;
        return idx;
    }

    public static double[] movingAverage(double[] data, int window) {
        double[] r = new double[data.length - window + 1];
        double sum = 0.0;
        for (int i = 0; i < window; i++) sum += data[i];
        r[0] = sum / window;
        for (int i = window; i < data.length; i++) { sum += data[i] - data[i - window]; r[i - window + 1] = sum / window; }
        return r;
    }
    public static double[] exponentialMovingAverage(double[] data, double alpha) {
        double[] r = new double[data.length];
        r[0] = data[0];
        for (int i = 1; i < data.length; i++) r[i] = alpha * data[i] + (1.0 - alpha) * r[i - 1];
        return r;
    }
    public static double[] normalizeData(double[] data) {
        double min = arrayMin(data), max = arrayMax(data), range = max - min;
        double[] r = new double[data.length];
        for (int i = 0; i < data.length; i++) r[i] = (data[i] - min) / range;
        return r;
    }
    public static double[] standardize(double[] data) {
        double m = mean(data), sd = stddev(data);
        double[] r = new double[data.length];
        for (int i = 0; i < data.length; i++) r[i] = (data[i] - m) / sd;
        return r;
    }

    public static double invErf(double x) {
        if (x < -1.0 || x > 1.0) throw new IllegalArgumentException("invErf domain [-1, 1]");
        if (x == 0.0) return 0.0;
        double y = x;
        for (int i = 0; i < 100; i++) {
            double e = erf(y) - x;
            if (Math.abs(e) < 1e-15) break;
            y -= e * Math.sqrt(PI) * 0.5 / Math.exp(-y * y);
        }
        return y;
    }
    public static double lambertW0(double x) {
        if (x < -1.0 / E) throw new IllegalArgumentException("lambertW0 domain x >= -1/e");
        double w = x < 0.0 ? 0.0 : Math.log(1.0 + x);
        for (int i = 0; i < 20; i++) {
            double ew = Math.exp(w);
            double dw = (w * ew - x) / (ew * (w + 1.0) - (w + 2.0) * (w * ew - x) / (2.0 * w + 2.0));
            w -= dw;
            if (Math.abs(dw) < 1e-14) break;
        }
        return w;
    }
    public static double riemannZeta(double s) {
        if (s == 1.0) throw new IllegalArgumentException("Zeta pole at s=1");
        double sum = 0.0;
        for (int n = 1; n < 20000; n++) sum += 1.0 / Math.pow(n, s);
        return sum;
    }
    public static double fresnelC(double x) {
        double sum = 0.0, term = x;
        for (int n = 0; n < 20; n++) {
            sum += term / (4.0 * n + 1.0);
            term *= -(x * x * x * x) * (4.0 * n + 1.0) / (4.0 * n + 5.0);
        }
        return sum;
    }
    public static double fresnelS(double x) {
        double sum = 0.0, term = x * x * x / 3.0;
        for (int n = 0; n < 20; n++) {
            sum += term / (4.0 * n + 3.0);
            term *= -(x * x * x * x) * (4.0 * n + 3.0) / (4.0 * n + 7.0);
        }
        return sum;
    }

    public static boolean isFinite(double x) { return Double.isFinite(x); }
    public static boolean isNaN(double x) { return Double.isNaN(x); }
    public static boolean isInfinite(double x) { return Double.isInfinite(x); }
}

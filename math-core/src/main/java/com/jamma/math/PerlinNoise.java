package com.jamma.math;

public final class PerlinNoise {

    private PerlinNoise() {}

    private static final int[] p = {
        151,160,137,91,90,15,131,13,201,95,96,53,194,233,7,225,
        140,36,103,30,69,142,8,99,37,240,21,10,23,190,6,148,
        247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,
        57,177,33,88,237,149,56,87,174,20,125,136,171,168,68,175,
        74,165,71,134,139,48,27,166,77,146,158,231,83,111,229,122,
        60,211,133,230,220,105,92,41,55,46,245,40,244,102,143,54,
        65,25,63,161,1,216,80,73,209,76,132,187,208,89,18,169,
        200,196,135,130,116,188,159,86,164,100,109,198,173,186,3,64,
        52,217,226,250,124,123,5,202,38,147,118,126,255,82,85,212,
        207,206,59,227,47,16,58,17,182,189,28,42,223,183,170,213,
        119,248,152,2,44,154,163,70,221,153,101,155,167,43,172,9,
        129,22,39,253,19,98,108,110,79,113,224,232,178,185,112,104,
        218,246,97,228,251,34,242,193,238,210,144,12,191,179,162,241,
        81,51,145,235,249,14,239,107,49,192,214,31,181,199,106,157,
        184,84,204,176,115,121,50,45,127,4,150,254,138,236,205,93,
        222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };

    private static final int[] perm = new int[512];

    static {
        for (int i = 0; i < 512; i++) {
            perm[i] = p[i & 255];
        }
    }

    private static int fastFloor(double x) {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x) {
        return (hash & 1) == 0 ? x : -x;
    }

    private static double grad(int hash, double x, double y) {
        int h = hash & 3;
        double u = h < 2 ? x : y;
        double v = h < 2 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    private static double grad(int hash, double x, double y, double z) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : (h == 12 || h == 14 ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    private static int[] seedPerm(int seed) {
        int[] perm = new int[512];
        System.arraycopy(p, 0, perm, 0, 256);
        long r = seed;
        for (int i = 255; i > 0; i--) {
            r = r * 1103515245L + 12345L;
            int j = (int)((r >>> 16) % (i + 1));
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }
        for (int i = 0; i < 256; i++) {
            perm[i + 256] = perm[i];
        }
        return perm;
    }

    public static double noise(double x) {
        int xi = fastFloor(x);
        double xf = x - xi;
        double u = fade(xf);
        int a = perm[xi & 255];
        int b = perm[(xi + 1) & 255];
        return lerp(u, grad(a, xf), grad(b, xf - 1.0));
    }

    public static double noise(double x, double y) {
        int ix = fastFloor(x);
        int iy = fastFloor(y);
        int X = ix & 255;
        int Y = iy & 255;
        double xf = x - ix;
        double yf = y - iy;
        double u = fade(xf);
        double v = fade(yf);
        int a = perm[X] + Y;
        int aa = perm[a];
        int ab = perm[a + 1];
        int b = perm[X + 1] + Y;
        int ba = perm[b];
        int bb = perm[b + 1];
        double x1 = lerp(u, grad(aa, xf, yf), grad(ba, xf - 1.0, yf));
        double x2 = lerp(u, grad(ab, xf, yf - 1.0), grad(bb, xf - 1.0, yf - 1.0));
        return lerp(v, x1, x2);
    }

    public static double noise(double x, double y, double z) {
        int ix = fastFloor(x);
        int iy = fastFloor(y);
        int iz = fastFloor(z);
        int X = ix & 255;
        int Y = iy & 255;
        int Z = iz & 255;
        double xf = x - ix;
        double yf = y - iy;
        double zf = z - iz;
        double u = fade(xf);
        double v = fade(yf);
        double w = fade(zf);
        int a = perm[X] + Y;
        int aa = perm[a] + Z;
        int ab = perm[a + 1] + Z;
        int b = perm[X + 1] + Y;
        int ba = perm[b] + Z;
        int bb = perm[b + 1] + Z;
        double x1 = lerp(u, grad(perm[aa], xf, yf, zf), grad(perm[ba], xf - 1.0, yf, zf));
        double x2 = lerp(u, grad(perm[ab], xf, yf - 1.0, zf), grad(perm[bb], xf - 1.0, yf - 1.0, zf));
        double x3 = lerp(u, grad(perm[aa + 1], xf, yf, zf - 1.0), grad(perm[ba + 1], xf - 1.0, yf, zf - 1.0));
        double x4 = lerp(u, grad(perm[ab + 1], xf, yf - 1.0, zf - 1.0), grad(perm[bb + 1], xf - 1.0, yf - 1.0, zf - 1.0));
        double y1 = lerp(v, x1, x2);
        double y2 = lerp(v, x3, x4);
        return lerp(w, y1, y2);
    }

    public static double noise(double x, int seed) {
        int[] sp = seedPerm(seed);
        int xi = fastFloor(x);
        double xf = x - xi;
        double u = fade(xf);
        int a = sp[xi & 255];
        int b = sp[(xi + 1) & 255];
        return lerp(u, grad(a, xf), grad(b, xf - 1.0));
    }

    public static double noise(double x, double y, int seed) {
        int[] sp = seedPerm(seed);
        int ix = fastFloor(x);
        int iy = fastFloor(y);
        int X = ix & 255;
        int Y = iy & 255;
        double xf = x - ix;
        double yf = y - iy;
        double u = fade(xf);
        double v = fade(yf);
        int a = sp[X] + Y;
        int aa = sp[a];
        int ab = sp[a + 1];
        int b = sp[X + 1] + Y;
        int ba = sp[b];
        int bb = sp[b + 1];
        double x1 = lerp(u, grad(aa, xf, yf), grad(ba, xf - 1.0, yf));
        double x2 = lerp(u, grad(ab, xf, yf - 1.0), grad(bb, xf - 1.0, yf - 1.0));
        return lerp(v, x1, x2);
    }

    public static double noise(double x, double y, double z, int seed) {
        int[] sp = seedPerm(seed);
        int ix = fastFloor(x);
        int iy = fastFloor(y);
        int iz = fastFloor(z);
        int X = ix & 255;
        int Y = iy & 255;
        int Z = iz & 255;
        double xf = x - ix;
        double yf = y - iy;
        double zf = z - iz;
        double u = fade(xf);
        double v = fade(yf);
        double w = fade(zf);
        int a = sp[X] + Y;
        int aa = sp[a] + Z;
        int ab = sp[a + 1] + Z;
        int b = sp[X + 1] + Y;
        int ba = sp[b] + Z;
        int bb = sp[b + 1] + Z;
        double x1 = lerp(u, grad(sp[aa], xf, yf, zf), grad(sp[ba], xf - 1.0, yf, zf));
        double x2 = lerp(u, grad(sp[ab], xf, yf - 1.0, zf), grad(sp[bb], xf - 1.0, yf - 1.0, zf));
        double x3 = lerp(u, grad(sp[aa + 1], xf, yf, zf - 1.0), grad(sp[ba + 1], xf - 1.0, yf, zf - 1.0));
        double x4 = lerp(u, grad(sp[ab + 1], xf, yf - 1.0, zf - 1.0), grad(sp[bb + 1], xf - 1.0, yf - 1.0, zf - 1.0));
        double y1 = lerp(v, x1, x2);
        double y2 = lerp(v, x3, x4);
        return lerp(w, y1, y2);
    }

    public static float noise(float x) {
        return (float) noise((double) x);
    }

    public static float noise(float x, float y) {
        return (float) noise((double) x, (double) y);
    }

    public static float noise(float x, float y, float z) {
        return (float) noise((double) x, (double) y, (double) z);
    }

    public static double fBm(double x, int octaves, double lacunarity, double gain) {
        double value = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        double max = 0.0;
        for (int i = 0; i < octaves; i++) {
            value += amplitude * noise(x * frequency);
            max += amplitude;
            amplitude *= gain;
            frequency *= lacunarity;
        }
        return value / max;
    }

    public static double fBm(double x, double y, int octaves, double lacunarity, double gain) {
        double value = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        double max = 0.0;
        for (int i = 0; i < octaves; i++) {
            value += amplitude * noise(x * frequency, y * frequency);
            max += amplitude;
            amplitude *= gain;
            frequency *= lacunarity;
        }
        return value / max;
    }

    public static double fBm(double x, double y, double z, int octaves, double lacunarity, double gain) {
        double value = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        double max = 0.0;
        for (int i = 0; i < octaves; i++) {
            value += amplitude * noise(x * frequency, y * frequency, z * frequency);
            max += amplitude;
            amplitude *= gain;
            frequency *= lacunarity;
        }
        return value / max;
    }
}

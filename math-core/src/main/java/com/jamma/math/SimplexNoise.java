package com.jamma.math;

public final class SimplexNoise {

    private SimplexNoise() {}

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

    private static final int[][] grad2 = {
        {1,0},{-1,0},{0,1},{0,-1},
        {1,1},{-1,1},{1,-1},{-1,-1}
    };

    private static final int[][] grad3 = {
        {1,1,0},{-1,1,0},{1,-1,0},{-1,-1,0},
        {1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1},
        {0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1}
    };

    private static final int[][] grad4 = {
        {0,1,1,1},{0,1,1,-1},{0,1,-1,1},{0,1,-1,-1},
        {0,-1,1,1},{0,-1,1,-1},{0,-1,-1,1},{0,-1,-1,-1},
        {1,0,1,1},{1,0,1,-1},{1,0,-1,1},{1,0,-1,-1},
        {-1,0,1,1},{-1,0,1,-1},{-1,0,-1,1},{-1,0,-1,-1},
        {1,1,0,1},{1,1,0,-1},{1,-1,0,1},{1,-1,0,-1},
        {-1,1,0,1},{-1,1,0,-1},{-1,-1,0,1},{-1,-1,0,-1},
        {1,1,1,0},{1,1,-1,0},{1,-1,1,0},{1,-1,-1,0},
        {-1,1,1,0},{-1,1,-1,0},{-1,-1,1,0},{-1,-1,-1,0}
    };

    private static int fastFloor(double x) {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    private static double dot(int[] g, double x, double y) {
        return g[0] * x + g[1] * y;
    }

    private static double dot(int[] g, double x, double y, double z) {
        return g[0] * x + g[1] * y + g[2] * z;
    }

    private static double dot(int[] g, double x, double y, double z, double w) {
        return g[0] * x + g[1] * y + g[2] * z + g[3] * w;
    }

    private static double noise2D(double x, double y, int[] p) {
        double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
        double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;

        double s = (x + y) * F2;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);

        double t = (i + j) * G2;
        double X0 = i - t;
        double Y0 = j - t;
        double x0 = x - X0;
        double y0 = y - Y0;

        int i1, j1;
        if (x0 > y0) { i1 = 1; j1 = 0; }
        else { i1 = 0; j1 = 1; }

        double x1 = x0 - i1 + G2;
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2;
        double y2 = y0 - 1.0 + 2.0 * G2;

        int ii = i & 255;
        int jj = j & 255;

        int gi0 = p[ii + p[jj]] & 7;
        int gi1 = p[ii + i1 + p[jj + j1]] & 7;
        int gi2 = p[ii + 1 + p[jj + 1]] & 7;

        double n0, n1, n2;

        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0) n0 = 0;
        else { t0 *= t0; n0 = t0 * t0 * dot(grad2[gi0], x0, y0); }

        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0) n1 = 0;
        else { t1 *= t1; n1 = t1 * t1 * dot(grad2[gi1], x1, y1); }

        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0) n2 = 0;
        else { t2 *= t2; n2 = t2 * t2 * dot(grad2[gi2], x2, y2); }

        return 70.0 * (n0 + n1 + n2);
    }

    private static double noise3D(double x, double y, double z, int[] p) {
        double F3 = 1.0 / 3.0;
        double G3 = 1.0 / 6.0;

        double s = (x + y + z) * F3;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        int k = fastFloor(z + s);

        double t = (i + j + k) * G3;
        double X0 = i - t;
        double Y0 = j - t;
        double Z0 = k - t;
        double x0 = x - X0;
        double y0 = y - Y0;
        double z0 = z - Z0;

        int i1, j1, k1;
        int i2, j2, k2;
        if (x0 >= y0) {
            if (y0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 1; k2 = 0; }
            else if (x0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 0; k2 = 1; }
            else { i1 = 0; j1 = 0; k1 = 1; i2 = 1; j2 = 0; k2 = 1; }
        } else {
            if (y0 < z0) { i1 = 0; j1 = 0; k1 = 1; i2 = 0; j2 = 1; k2 = 1; }
            else if (x0 < z0) { i1 = 0; j1 = 1; k1 = 0; i2 = 0; j2 = 1; k2 = 1; }
            else { i1 = 0; j1 = 1; k1 = 0; i2 = 1; j2 = 1; k2 = 0; }
        }

        double x1 = x0 - i1 + G3;
        double y1 = y0 - j1 + G3;
        double z1 = z0 - k1 + G3;
        double x2 = x0 - i2 + 2.0 * G3;
        double y2 = y0 - j2 + 2.0 * G3;
        double z2 = z0 - k2 + 2.0 * G3;
        double x3 = x0 - 1.0 + 3.0 * G3;
        double y3 = y0 - 1.0 + 3.0 * G3;
        double z3 = z0 - 1.0 + 3.0 * G3;

        int ii = i & 255;
        int jj = j & 255;
        int kk = k & 255;

        int gi0 = p[ii + p[jj + p[kk]]] % 12;
        int gi1 = p[ii + i1 + p[jj + j1 + p[kk + k1]]] % 12;
        int gi2 = p[ii + i2 + p[jj + j2 + p[kk + k2]]] % 12;
        int gi3 = p[ii + 1 + p[jj + 1 + p[kk + 1]]] % 12;

        double n0, n1, n2, n3;

        double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
        if (t0 < 0) n0 = 0;
        else { t0 *= t0; n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0); }

        double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
        if (t1 < 0) n1 = 0;
        else { t1 *= t1; n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1); }

        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
        if (t2 < 0) n2 = 0;
        else { t2 *= t2; n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2); }

        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
        if (t3 < 0) n3 = 0;
        else { t3 *= t3; n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3); }

        return 32.0 * (n0 + n1 + n2 + n3);
    }

    private static double noise4D(double x, double y, double z, double w, int[] p) {
        double F4 = (Math.sqrt(5.0) - 1.0) / 4.0;
        double G4 = (5.0 - Math.sqrt(5.0)) / 20.0;

        double s = (x + y + z + w) * F4;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        int k = fastFloor(z + s);
        int l = fastFloor(w + s);

        double t = (i + j + k + l) * G4;
        double X0 = i - t;
        double Y0 = j - t;
        double Z0 = k - t;
        double W0 = l - t;
        double x0 = x - X0;
        double y0 = y - Y0;
        double z0 = z - Z0;
        double w0 = w - W0;

        int rankx = 0, ranky = 0, rankz = 0, rankw = 0;
        if (x0 > y0) rankx++; else ranky++;
        if (x0 > z0) rankx++; else rankz++;
        if (x0 > w0) rankx++; else rankw++;
        if (y0 > z0) ranky++; else rankz++;
        if (y0 > w0) ranky++; else rankw++;
        if (z0 > w0) rankz++; else rankw++;

        int i1 = rankx >= 3 ? 1 : 0;
        int j1 = ranky >= 3 ? 1 : 0;
        int k1 = rankz >= 3 ? 1 : 0;
        int l1 = rankw >= 3 ? 1 : 0;
        int i2 = rankx >= 2 ? 1 : 0;
        int j2 = ranky >= 2 ? 1 : 0;
        int k2 = rankz >= 2 ? 1 : 0;
        int l2 = rankw >= 2 ? 1 : 0;
        int i3 = rankx >= 1 ? 1 : 0;
        int j3 = ranky >= 1 ? 1 : 0;
        int k3 = rankz >= 1 ? 1 : 0;
        int l3 = rankw >= 1 ? 1 : 0;

        double x1 = x0 - i1 + G4;
        double y1 = y0 - j1 + G4;
        double z1 = z0 - k1 + G4;
        double w1 = w0 - l1 + G4;
        double x2 = x0 - i2 + 2.0 * G4;
        double y2 = y0 - j2 + 2.0 * G4;
        double z2 = z0 - k2 + 2.0 * G4;
        double w2 = w0 - l2 + 2.0 * G4;
        double x3 = x0 - i3 + 3.0 * G4;
        double y3 = y0 - j3 + 3.0 * G4;
        double z3 = z0 - k3 + 3.0 * G4;
        double w3 = w0 - l3 + 3.0 * G4;
        double x4 = x0 - 1.0 + 4.0 * G4;
        double y4 = y0 - 1.0 + 4.0 * G4;
        double z4 = z0 - 1.0 + 4.0 * G4;
        double w4 = w0 - 1.0 + 4.0 * G4;

        int ii = i & 255;
        int jj = j & 255;
        int kk = k & 255;
        int ll = l & 255;

        int gi0 = p[ii + p[jj + p[kk + p[ll]]]] & 31;
        int gi1 = p[ii + i1 + p[jj + j1 + p[kk + k1 + p[ll + l1]]]] & 31;
        int gi2 = p[ii + i2 + p[jj + j2 + p[kk + k2 + p[ll + l2]]]] & 31;
        int gi3 = p[ii + i3 + p[jj + j3 + p[kk + k3 + p[ll + l3]]]] & 31;
        int gi4 = p[ii + 1 + p[jj + 1 + p[kk + 1 + p[ll + 1]]]] & 31;

        double n0, n1, n2, n3, n4;

        double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
        if (t0 < 0) n0 = 0;
        else { t0 *= t0; n0 = t0 * t0 * dot(grad4[gi0], x0, y0, z0, w0); }

        double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
        if (t1 < 0) n1 = 0;
        else { t1 *= t1; n1 = t1 * t1 * dot(grad4[gi1], x1, y1, z1, w1); }

        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
        if (t2 < 0) n2 = 0;
        else { t2 *= t2; n2 = t2 * t2 * dot(grad4[gi2], x2, y2, z2, w2); }

        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
        if (t3 < 0) n3 = 0;
        else { t3 *= t3; n3 = t3 * t3 * dot(grad4[gi3], x3, y3, z3, w3); }

        double t4 = 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
        if (t4 < 0) n4 = 0;
        else { t4 *= t4; n4 = t4 * t4 * dot(grad4[gi4], x4, y4, z4, w4); }

        return 27.0 * (n0 + n1 + n2 + n3 + n4);
    }

    private static int[] seedPerm(int seed) {
        int[] perm = new int[512];
        for (int i = 0; i < 256; i++) {
            perm[i] = p[i];
        }
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

    public static double noise(double x, double y) {
        return noise2D(x, y, perm);
    }

    public static double noise(double x, double y, double z) {
        return noise3D(x, y, z, perm);
    }

    public static double noise(double x, double y, double z, double w) {
        return noise4D(x, y, z, w, perm);
    }

    public static double noise(double x, double y, int seed) {
        return noise2D(x, y, seedPerm(seed));
    }

    public static double noise(double x, double y, double z, int seed) {
        return noise3D(x, y, z, seedPerm(seed));
    }

    public static double noise(double x, double y, double z, double w, int seed) {
        return noise4D(x, y, z, w, seedPerm(seed));
    }

    public static float noise(float x, float y) {
        return (float) noise((double) x, (double) y);
    }

    public static float noise(float x, float y, float z) {
        return (float) noise((double) x, (double) y, (double) z);
    }
}

package com.jamma.math.incubator;

import com.jamma.math.Vector3d;
import com.jamma.math.Vector4d;
import com.jamma.math.matrix.Matrix4d;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

@SuppressWarnings("preview")
public final class ParallelOps {

    private static final int PARALLEL_THRESHOLD = 1 << 12;

    private ParallelOps() {
    }

    private static int availableCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    // ── Vector3d batch ops ──────────────────────────────────────────────

    public static Vector3d[] batchAddParallel(Vector3d[] a, Vector3d[] b) {
        return batchOp(a, b, VectorMath::add, PARALLEL_THRESHOLD);
    }

    public static Vector3d[] batchSubParallel(Vector3d[] a, Vector3d[] b) {
        return batchOp(a, b, VectorMath::sub, PARALLEL_THRESHOLD);
    }

    public static Vector3d[] batchMulParallel(Vector3d[] a, Vector3d[] b) {
        return batchOp(a, b, VectorMath::mul, PARALLEL_THRESHOLD);
    }

    public static double[] batchDotParallel(Vector3d[] a, Vector3d[] b) {
        int n = a.length;
        double[] result = new double[n];
        if (n <= PARALLEL_THRESHOLD) {
            for (int i = 0; i < n; i++) result[i] = VectorMath.dot(a[i], b[i]);
            return result;
        }
        int cores = availableCores();
        int chunkSize = (n + cores - 1) / cores;
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) result[i] = VectorMath.dot(a[i], b[i]);
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch dot failed", e);
        }
        return result;
    }

    public static Vector3d[] batchCrossParallel(Vector3d[] a, Vector3d[] b) {
        return batchOp(a, b, VectorMath::cross, PARALLEL_THRESHOLD);
    }

    public static Vector3d[] batchNormalizeParallel(Vector3d[] v) {
        int n = v.length;
        Vector3d[] result = new Vector3d[n];
        if (n <= PARALLEL_THRESHOLD) {
            for (int i = 0; i < n; i++) result[i] = VectorMath.normalize(v[i]);
            return result;
        }
        int cores = availableCores();
        int chunkSize = (n + cores - 1) / cores;
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) result[i] = VectorMath.normalize(v[i]);
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch normalize failed", e);
        }
        return result;
    }

    public static Vector3d[] batchScaleParallel(Vector3d[] v, double s) {
        int n = v.length;
        Vector3d[] result = new Vector3d[n];
        if (n <= PARALLEL_THRESHOLD) {
            for (int i = 0; i < n; i++) result[i] = VectorMath.scale(v[i], s);
            return result;
        }
        int cores = availableCores();
        int chunkSize = (n + cores - 1) / cores;
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                final double scalar = s;
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) result[i] = VectorMath.scale(v[i], scalar);
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch scale failed", e);
        }
        return result;
    }

    public static Vector3d[] batchTransformParallel(Matrix4d m, Vector3d[] v) {
        int n = v.length;
        Vector3d[] result = new Vector3d[n];
        if (n <= PARALLEL_THRESHOLD) {
            for (int i = 0; i < n; i++) result[i] = transformPos(m, v[i]);
            return result;
        }
        int cores = availableCores();
        int chunkSize = (n + cores - 1) / cores;
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) result[i] = transformPos(m, v[i]);
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch transform failed", e);
        }
        return result;
    }

    // ── Vector4d batch ops ──────────────────────────────────────────────

    public static Vector4d[] batchAddParallel(Vector4d[] a, Vector4d[] b) {
        int n = a.length;
        Vector4d[] result = new Vector4d[n];
        if (n <= PARALLEL_THRESHOLD) {
            for (int i = 0; i < n; i++) result[i] = a[i].add(b[i]);
            return result;
        }
        int cores = availableCores();
        int chunkSize = (n + cores - 1) / cores;
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) result[i] = a[i].add(b[i]);
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch add (Vec4) failed", e);
        }
        return result;
    }

    public static Vector4d[] batchTransformParallel(Matrix4d m, Vector4d[] v) {
        int n = v.length;
        Vector4d[] result = new Vector4d[n];
        if (n <= PARALLEL_THRESHOLD) {
            for (int i = 0; i < n; i++) result[i] = m.transform(v[i]);
            return result;
        }
        int cores = availableCores();
        int chunkSize = (n + cores - 1) / cores;
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) result[i] = m.transform(v[i]);
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch transform (Vec4) failed", e);
        }
        return result;
    }

    // ── Private helpers ─────────────────────────────────────────────────

    private static Vector3d[] batchOp(Vector3d[] a, Vector3d[] b, BinaryOperator<Vector3d> op, int threshold) {
        int n = a.length;
        Vector3d[] result = new Vector3d[n];
        if (n <= threshold) {
            for (int i = 0; i < n; i++) result[i] = op.apply(a[i], b[i]);
            return result;
        }
        int cores = availableCores();
        int chunkSize = (n + cores - 1) / cores;
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) result[i] = op.apply(a[i], b[i]);
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch op failed", e);
        }
        return result;
    }

    private static Vector3d transformPos(Matrix4d m, Vector3d v) {
        double w = Math.fma(m.m30, v.x(), Math.fma(m.m31, v.y(), m.m32 * v.z())) + m.m33;
        double invW = 1.0 / w;
        return new Vector3d(
            (Math.fma(m.m00, v.x(), Math.fma(m.m01, v.y(), m.m02 * v.z())) + m.m03) * invW,
            (Math.fma(m.m10, v.x(), Math.fma(m.m11, v.y(), m.m12 * v.z())) + m.m13) * invW,
            (Math.fma(m.m20, v.x(), Math.fma(m.m21, v.y(), m.m22 * v.z())) + m.m23) * invW
        );
    }
}

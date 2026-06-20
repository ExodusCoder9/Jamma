package com.jamma.math.incubator;

import com.jamma.math.Vector3D;
import java.util.concurrent.StructuredTaskScope;

@SuppressWarnings("preview")
public final class ParallelOps {

    private static final int PARALLEL_THRESHOLD = 1 << 12;

    private ParallelOps() {
    }

    public static Vector3D[] batchAddParallel(Vector3D[] a, Vector3D[] b) {
        int n = a.length;
        Vector3D[] result = new Vector3D[n];
        if (n <= PARALLEL_THRESHOLD) {
            for (int i = 0; i < n; i++) {
                result[i] = VectorMath.add(a[i], b[i]);
            }
            return result;
        }

        int cores = Runtime.getRuntime().availableProcessors();
        int chunkSize = (n + cores - 1) / cores;

        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
            for (int c = 0; c < cores; c++) {
                final int start = c * chunkSize;
                final int end = Math.min(start + chunkSize, n);
                if (start < end) {
                    scope.fork(() -> {
                        for (int i = start; i < end; i++) {
                            result[i] = VectorMath.add(a[i], b[i]);
                        }
                        return null;
                    });
                }
            }
            scope.join();
        } catch (Throwable e) {
            throw new RuntimeException("Parallel batch add failed", e);
        }
        return result;
    }
}


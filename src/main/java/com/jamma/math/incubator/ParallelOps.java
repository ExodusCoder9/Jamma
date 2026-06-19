package com.jamma.math.incubator;

import com.jamma.math.Vector3D;

import java.util.concurrent.StructuredTaskScope;

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
        int mid = n / 2;
        try (var scope = StructuredTaskScope.open()) {
            var leftFuture = scope.fork(() -> {
                Vector3D[] left = new Vector3D[mid];
                for (int i = 0; i < mid; i++) {
                    left[i] = VectorMath.add(a[i], b[i]);
                }
                return left;
            });
            var rightFuture = scope.fork(() -> {
                Vector3D[] right = new Vector3D[n - mid];
                for (int i = mid; i < n; i++) {
                    right[i - mid] = VectorMath.add(a[i], b[i]);
                }
                return right;
            });
            scope.join();
            Vector3D[] left = leftFuture.get();
            Vector3D[] right = rightFuture.get();
            System.arraycopy(left, 0, result, 0, left.length);
            System.arraycopy(right, 0, result, mid, right.length);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Parallel batch add interrupted", e);
        }
        return result;
    }
}

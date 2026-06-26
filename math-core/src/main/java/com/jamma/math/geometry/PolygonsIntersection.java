package com.jamma.math.geometry;

import java.util.BitSet;

/**
 * Fast point-in-polygon test using an interval tree over polygon edges.
 * <p>
 * Preprocesses polygon vertices into a bounding sphere, AABB, and Y-interval tree
 * in the constructor. {@link #testPoint(float, float)} is then O(log n + k) where
 * k is the number of polygon edges crossing the Y-coordinate of the query point.
 * <p>
 * Thread-safe after construction (all testPoint methods are read-only).
 */
public class PolygonsIntersection {

    private final float[] verticesXY;
    private final int[] polygons;
    private final float centerX, centerY, radiusSquared;
    private final float minX, minY, maxX, maxY;
    private final IntervalTreeNode root;

    /**
     * @param verticesXY flat float array of interleaved x,y vertex coordinates
     * @param polygons   start index into verticesXY for each sub-polygon (holes via nested polygons)
     * @param count      number of vertices to read from verticesXY (count*2 floats)
     */
    public PolygonsIntersection(float[] verticesXY, int[] polygons, int count) {
        this.verticesXY = verticesXY;
        this.polygons = polygons;
        int vertexCount = Math.min(count, verticesXY.length / 2);

        // Compute AABB and bounding sphere center
        float cx = 0, cy = 0;
        float mnX = Float.POSITIVE_INFINITY, mnY = Float.POSITIVE_INFINITY;
        float mxX = Float.NEGATIVE_INFINITY, mxY = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < vertexCount; i++) {
            float x = verticesXY[i * 2];
            float y = verticesXY[i * 2 + 1];
            cx += x;
            cy += y;
            mnX = Math.min(mnX, x);
            mnY = Math.min(mnY, y);
            mxX = Math.max(mxX, x);
            mxY = Math.max(mxY, y);
        }
        cx /= vertexCount;
        cy /= vertexCount;
        this.centerX = cx;
        this.centerY = cy;
        this.minX = mnX;
        this.minY = mnY;
        this.maxX = mxX;
        this.maxY = mxY;

        // Compute bounding sphere radius squared
        float maxRsq = 0;
        for (int i = 0; i < vertexCount; i++) {
            float dx = verticesXY[i * 2] - cx;
            float dy = verticesXY[i * 2 + 1] - cy;
            float rsq = dx * dx + dy * dy;
            if (rsq > maxRsq) maxRsq = rsq;
        }
        this.radiusSquared = maxRsq;

        // Build interval tree from edges
        int edgeCount = 0;
        for (int p = 0; p < polygons.length; p++) {
            int start = polygons[p];
            int end = (p + 1 < polygons.length) ? polygons[p + 1] : vertexCount;
            edgeCount += end - start;
        }
        Interval[] intervals = new Interval[edgeCount];
        int idx = 0;
        for (int p = 0; p < polygons.length; p++) {
            int start = polygons[p];
            int end = (p + 1 < polygons.length) ? polygons[p + 1] : vertexCount;
            for (int i = start; i < end; i++) {
                int j = (i + 1 < end) ? i + 1 : start;
                float yi = verticesXY[i * 2 + 1];
                float yj = verticesXY[j * 2 + 1];
                intervals[idx++] = new Interval(Math.min(yi, yj), Math.max(yi, yj), i, j, p);
            }
        }
        this.root = buildTree(intervals, 0, intervals.length);
    }

    private static IntervalTreeNode buildTree(Interval[] intervals, int l, int r) {
        if (l >= r) return null;
        int mid = (l + r) >>> 1;
        // Find median Y (sort by mid)
        java.util.Arrays.sort(intervals, l, r, (a, b) -> Float.compare(a.start, b.start));
        // Actually, median-of-medians approach: just use mid after sort
        float splitY = intervals[mid].start;
        // Partition into left (entirely below splitY), center (crossing splitY), right (entirely above splitY)
        int ci = l, li = l, ri = r;
        int n = r - l;
        Interval[] left = new Interval[n];
        Interval[] center = new Interval[n];
        Interval[] right = new Interval[n];
        int lc = 0, cc = 0, rc = 0;
        for (int i = l; i < r; i++) {
            Interval iv = intervals[i];
            if (iv.end < splitY) {
                left[lc++] = iv;
            } else if (iv.start > splitY) {
                right[rc++] = iv;
            } else {
                center[cc++] = iv;
            }
        }
        IntervalTreeNode node = new IntervalTreeNode(splitY, java.util.Arrays.copyOf(center, cc));
        System.arraycopy(left, 0, intervals, l, lc);
        System.arraycopy(right, 0, intervals, l + lc, rc);
        node.left = buildTree(intervals, l, l + lc);
        node.right = buildTree(intervals, l + lc, l + lc + rc);
        return node;
    }

    /**
     * Test if a point is inside any of the polygons.
     */
    public boolean testPoint(float x, float y) {
        return testPoint(x, y, null);
    }

    /**
     * Test if a point is inside any of the polygons.
     * If {@code inPolys} is non-null, the i-th bit is set for each containing polygon.
     * @return true if the point is inside at least one polygon (and not in a hole)
     */
    public boolean testPoint(float x, float y, BitSet inPolys) {
        // Quick rejection: bounding sphere
        float dx = x - centerX;
        float dy = y - centerY;
        if (dx * dx + dy * dy > radiusSquared) return false;
        // Quick rejection: AABB
        if (x < minX || x > maxX || y < minY || y > maxY) return false;

        // Even-odd ray casting via interval tree
        boolean inside = false;
        if (root != null) {
            inside = traverse(x, y, root, inPolys);
        }
        return inside;
    }

    private boolean traverse(float x, float y, IntervalTreeNode node, BitSet inPolys) {
        boolean inside = false;
        // Test edges that cross the split Y
        for (Interval iv : node.center) {
            float xi = verticesXY[iv.i * 2];
            float yi = verticesXY[iv.i * 2 + 1];
            float xj = verticesXY[iv.j * 2];
            float yj = verticesXY[iv.j * 2 + 1];
            // Check if ray from (x,y) toward negative X crosses this edge
            if ((yi > y) != (yj > y)) {
                float xDist = xi + (y - yi) / (yj - yi) * (xj - xi) - x;
                if (xDist < 0) {
                    inside = !inside;
                    if (inPolys != null) {
                        inPolys.flip(iv.polyIndex);
                    }
                }
            }
        }
        // Recurse left/right
        if (y < node.splitY && node.left != null) {
            inside ^= traverse(x, y, node.left, inPolys);
        }
        if (y >= node.splitY && node.right != null) {
            inside ^= traverse(x, y, node.right, inPolys);
        }
        return inside;
    }

    private record Interval(float start, float end, int i, int j, int polyIndex) {}
    private static class IntervalTreeNode {
        final float splitY;
        final Interval[] center;
        IntervalTreeNode left, right;
        IntervalTreeNode(float splitY, Interval[] center) {
            this.splitY = splitY;
            this.center = center;
        }
    }
}

package com.rectangles.service;

import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.AdjacencyResult.Type;
import com.rectangles.domain.Rectangle;
import org.springframework.stereotype.Service;

/**
 * Determines whether two rectangles are adjacent and classifies the type
 * of adjacency.
 *
 * <h2>Algorithm</h2>
 * <p>Two axis-aligned rectangles can only be adjacent along a vertical or
 * horizontal shared boundary. We check all four combinations:</p>
 * <ul>
 *   <li>A's right edge (x = A.maxX) touching B's left edge (x = B.minX)</li>
 *   <li>A's left edge (x = A.minX) touching B's right edge (x = B.maxX)</li>
 *   <li>A's top edge (y = A.maxY) touching B's bottom edge (y = B.minY)</li>
 *   <li>A's bottom edge (y = A.minY) touching B's top edge (y = B.maxY)</li>
 * </ul>
 *
 * <p>For each candidate shared boundary, we project the two rectangles onto
 * the perpendicular axis to find the overlapping segment. That overlap
 * determines the adjacency type:</p>
 * <ul>
 *   <li><b>PROPER</b>    — the shared segment equals both sides in full length</li>
 *   <li><b>SUB_LINE</b>  — one side is wholly inside the other (but shorter)</li>
 *   <li><b>PARTIAL</b>   — the sides partially overlap</li>
 *   <li><b>NOT_ADJACENT</b> — no shared boundary found</li>
 * </ul>
 */
@Service
public class AdjacencyAnalyzer {

    /**
     * Analyzes the adjacency relationship between two rectangles.
     *
     * @param a the first rectangle
     * @param b the second rectangle
     * @return an {@link AdjacencyResult} describing the relationship
     */
    public AdjacencyResult analyze(Rectangle a, Rectangle b) {

        // Check vertical shared boundaries (left/right edges)
        if (a.maxX == b.minX || a.minX == b.maxX) {
            // Project both rectangles onto the Y axis
            Type type = classifySegmentOverlap(a.minY, a.maxY, b.minY, b.maxY);
            if (type != Type.NOT_ADJACENT) return new AdjacencyResult(type);
        }

        // Check horizontal shared boundaries (top/bottom edges)
        if (a.maxY == b.minY || a.minY == b.maxY) {
            // Project both rectangles onto the X axis
            Type type = classifySegmentOverlap(a.minX, a.maxX, b.minX, b.maxX);
            if (type != Type.NOT_ADJACENT) return new AdjacencyResult(type);
        }

        return new AdjacencyResult(Type.NOT_ADJACENT);
    }

    /**
     * Given two 1D segments [aMin, aMax] and [bMin, bMax] on a shared axis,
     * determines what kind of overlap (adjacency type) they have.
     *
     * <p>The segments represent the projections of the two rectangles' sides
     * onto the axis perpendicular to their shared boundary.</p>
     *
     * @param aMin start of segment A
     * @param aMax end of segment A
     * @param bMin start of segment B
     * @param bMax end of segment B
     * @return the {@link Type} of adjacency, or NOT_ADJACENT if no overlap
     */
    private Type classifySegmentOverlap(double aMin, double aMax,
                                         double bMin, double bMax) {
        // Find the overlapping segment
        double overlapMin = Math.max(aMin, bMin);
        double overlapMax = Math.min(aMax, bMax);

        // No overlap (or only touching at a single point, which is not a segment)
        if (overlapMax <= overlapMin) {
            return Type.NOT_ADJACENT;
        }

        double overlapLen = overlapMax - overlapMin;
        double aLen = aMax - aMin;
        double bLen = bMax - bMin;

        // PROPER: the overlap spans both segments entirely
        if (overlapLen == aLen && overlapLen == bLen) {
            return Type.PROPER;
        }

        // SUB_LINE: one segment is wholly inside the other
        // A inside B: aMin >= bMin && aMax <= bMax  →  overlap == aLen
        // B inside A: bMin >= aMin && bMax <= aMax  →  overlap == bLen
        if (overlapLen == aLen || overlapLen == bLen) {
            return Type.SUB_LINE;
        }

        // PARTIAL: segments partially overlap but neither contains the other
        return Type.PARTIAL;
    }
}
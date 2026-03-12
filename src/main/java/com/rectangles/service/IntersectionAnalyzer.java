package com.rectangles.service;

import com.rectangles.domain.Point;
import com.rectangles.domain.Rectangle;
import com.rectangles.domain.IntersectionResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Determines whether the <em>edges</em> of two rectangles intersect and
 * identifies the points of intersection.
 *
 * <h2>Algorithm</h2>
 * <p>Each axis-aligned rectangle has four edges:</p>
 * <ul>
 *   <li>Top    — horizontal segment at y = maxY, from minX to maxX</li>
 *   <li>Bottom — horizontal segment at y = minY, from minX to maxX</li>
 *   <li>Left   — vertical segment at x = minX, from minY to maxY</li>
 *   <li>Right  — vertical segment at x = maxX, from minY to maxY</li>
 * </ul>
 *
 * <p>We test all 16 pairs (4 edges of A × 4 edges of B) for intersection.
 * Each pair is either:</p>
 * <ul>
 *   <li>Horizontal vs. Vertical — can produce at most one crossing point</li>
 *   <li>Horizontal vs. Horizontal — parallel, cannot cross (collinear overlap
 *       is treated as edge-touching, not as an intersection point)</li>
 *   <li>Vertical vs. Vertical — same as above</li>
 * </ul>
 *
 * <p>A containment relationship (one rectangle inside the other) does NOT
 * produce intersection points because the edges never cross.</p>
 *
 * <h2>Edge representation</h2>
 * <p>A horizontal edge is stored as {@code (x1, y, x2, y)} and a vertical
 * edge as {@code (x, y1, x, y2)}.</p>
 */
@Service
public class IntersectionAnalyzer {

    /**
     * Analyzes the edge intersection between two rectangles.
     *
     * @param a the first rectangle
     * @param b the second rectangle
     * @return an {@link IntersectionResult} with a flag and list of crossing points
     */
    public IntersectionResult analyze(Rectangle a, Rectangle b) {

        List<double[]> edgesA = getEdges(a);
        List<double[]> edgesB = getEdges(b);
        List<Point> points = new ArrayList<>();

        for (double[] edgeA : edgesA) {
            for (double[] edgeB : edgesB) {
                Point p = computeIntersection(edgeA, edgeB);
                if (p != null) {
                    points.add(p);
                }
            }
        }

        return new IntersectionResult(!points.isEmpty(), points);
    }

    /**
     * Returns the four edges of a rectangle as a list of segments.
     * Each segment is represented as a double array: [x1, y1, x2, y2].
     */
    private List<double[]> getEdges(Rectangle r) {
        List<double[]> edges = new ArrayList<>();
        edges.add(new double[]{r.minX, r.maxY, r.maxX, r.maxY}); // top
        edges.add(new double[]{r.minX, r.minY, r.maxX, r.minY}); // bottom
        edges.add(new double[]{r.minX, r.minY, r.minX, r.maxY}); // left
        edges.add(new double[]{r.maxX, r.minY, r.maxX, r.maxY}); // right
        return edges;
    }

    /**
     * Computes the intersection point (if any) between two line segments.
     *
     * <p>Since all edges are axis-aligned, every pair is either:</p>
     * <ul>
     *   <li>horizontal vs vertical → at most one crossing point</li>
     *   <li>horizontal vs horizontal → parallel, no crossing</li>
     *   <li>vertical vs vertical → parallel, no crossing</li>
     * </ul>
     *
     * @param s1 first segment as [x1, y1, x2, y2]
     * @param s2 second segment as [x1, y1, x2, y2]
     * @return the intersection Point, or null if none exists
     */
    private Point computeIntersection(double[] s1, double[] s2) {
        boolean s1Horizontal = (s1[1] == s1[3]);
        boolean s2Horizontal = (s2[1] == s2[3]);

        // Both horizontal or both vertical — parallel (or coincident), no crossing point
        if (s1Horizontal == s2Horizontal) return null;

        // Also skip if the two segments share an endpoint coordinate on the crossing axis
        // (i.e. they meet exactly at a corner — touching, not crossing)
        // This is handled naturally by the strict-boundary check below.

        // Assign horizontal and vertical segments
        double[] horiz = s1Horizontal ? s1 : s2;
        double[] vert  = s1Horizontal ? s2 : s1;

        // Horizontal segment: y is constant (horiz[1] == horiz[3])
        // Vertical segment:   x is constant (vert[0]  == vert[2])
        double horizY  = horiz[1];
        double horizX1 = Math.min(horiz[0], horiz[2]);
        double horizX2 = Math.max(horiz[0], horiz[2]);

        double vertX  = vert[0];
        double vertY1 = Math.min(vert[1], vert[3]);
        double vertY2 = Math.max(vert[1], vert[3]);

        // The crossing exists if vertX is within [horizX1, horizX2]
        // and horizY is within [vertY1, vertY2].
        // We use strict inequalities on at least one end so that corner-touching
        // points (shared corners) are counted once rather than twice.
        boolean xWithin = horizX1 < vertX && vertX < horizX2;
        boolean yWithin = vertY1  < horizY && horizY < vertY2;

        if (xWithin && yWithin) {
            return new Point(vertX, horizY);
        }
        return null;
    }
}
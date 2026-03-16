package com.rectangles.service.strategy;

import com.rectangles.domain.Point;
import com.rectangles.domain.Rectangle;
import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Segment;
import com.rectangles.dto.IntersectionRequest;
import com.rectangles.dto.Request;
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
 * <p>Each edge is represented as a {@link com.rectangles.domain.Segment} composed
 * of two {@link com.rectangles.domain.Point} objects (start and end).</p>
 */
@Service
public class IntersectionStrategy implements AnalyzerStrategy {

    /**
     * Analyzes the edge intersection between two rectangles.
     *
     * @param request the request containing the 2 rectangles
     * @return an {@link IntersectionResult} with a flag and list of crossing points
     */
    @Override
    public IntersectionResult analyze(Request request) {

        IntersectionRequest intersectionRequest = (IntersectionRequest) request;

        List<Segment> segmentsA = getSegments(intersectionRequest.getRectangleA());
        List<Segment> segmentsB = getSegments(intersectionRequest.getRectangleB());
        List<Point> points = new ArrayList<>();

        for (Segment segmentA : segmentsA) {
            for (Segment segmentB : segmentsB) {
                Point p = computeIntersection(segmentA, segmentB);
                if (p != null) {
                    points.add(p);
                }
            }
        }

        return new IntersectionResult(!points.isEmpty(), points);
    }

    /**
     * Returns the four edges of a rectangle as a list of {@link com.rectangles.domain.Segment} objects.
     */
    private List<Segment> getSegments(Rectangle r) {
        List<Segment> edges = new ArrayList<>();
        edges.add(new Segment(r.topLeft(), r.topRight())); // top
        edges.add(new Segment(r.bottomLeft(), r.bottomRight())); // bottom
        edges.add(new Segment(r.bottomLeft(), r.topLeft())); // left
        edges.add(new Segment(r.bottomRight(), r.topRight())); // right
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
     * @param s1 first segment
     * @param s2 second segment
     * @return the intersection Point, or null if none exists
     */
    private Point computeIntersection(Segment s1, Segment s2) {

        if (s1.isHorizontal() == s2.isHorizontal()) return null;

        Segment horiz = s1.isHorizontal() ? s1 : s2;
        Segment vert  = s1.isHorizontal() ? s2 : s1;

        boolean xWithin = horiz.getMinX() < vert.getConstantX() && vert.getConstantX() < horiz.getMaxX();
        boolean yWithin = vert.getMinY()  < horiz.getConstantY() && horiz.getConstantY() < vert.getMaxY();

        if (xWithin && yWithin) {
            return new Point(vert.getConstantX(), horiz.getConstantY());
        }
        return null;
    }
}
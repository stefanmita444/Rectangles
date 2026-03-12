package com.rectangles.domain;

import java.util.Collections;
import java.util.List;

/**
 * Represents the result of an intersection check between two rectangles.
 *
 * <p>Intersection here means that the <em>edges</em> of the two rectangles
 * cross each other. A rectangle wholly contained inside another does NOT
 * produce an intersection result.</p>
 */
public record IntersectionResult(boolean hasIntersection, List<Point> intersectionPoints) {

    /** Wraps the point list in an unmodifiable view on construction. */
    public IntersectionResult {
        intersectionPoints = Collections.unmodifiableList(intersectionPoints);
    }

    @Override
    public String toString() {
        return "IntersectionResult{hasIntersection=" + hasIntersection
            + ", points=" + intersectionPoints + "}";
    }
}

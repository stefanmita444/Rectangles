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

    public IntersectionResult(boolean hasIntersection, List<Point> intersectionPoints) {
        this.hasIntersection = hasIntersection;
        this.intersectionPoints = Collections.unmodifiableList(intersectionPoints);
    }

    /**
     * Returns true if the edges of the two rectangles intersect.
     */
    @Override
    public boolean hasIntersection() {
        return hasIntersection;
    }

    /**
     * Returns the list of points where the edges of the two rectangles cross.
     * Returns an empty list if there is no intersection.
     */
    @Override
    public List<Point> intersectionPoints() {
        return intersectionPoints;
    }

    @Override
    public String toString() {
        return "IntersectionResult{hasIntersection=" + hasIntersection
            + ", points=" + intersectionPoints + "}";
    }
}
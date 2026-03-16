package com.rectangles.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * Represents the result of an intersection check between two rectangles.
 *
 * <p>Intersection here means that the <em>edges</em> of the two rectangles
 * cross each other. A rectangle wholly contained inside another does NOT
 * produce an intersection result.</p>
 */
@Getter
@ToString
@AllArgsConstructor
public class IntersectionResult extends Result {

    private final boolean hasIntersection;
    private final List<Point> intersectionPoints;

    /** Wraps the point list in an unmodifiable view on construction. */
    public List<Point> intersectionPoints() {
        return Collections.unmodifiableList(this.intersectionPoints);
    }

    public boolean hasIntersection() {
        return this.hasIntersection;
    }
}

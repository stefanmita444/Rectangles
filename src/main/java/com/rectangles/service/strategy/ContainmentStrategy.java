package com.rectangles.service.strategy;

import com.rectangles.domain.Rectangle;
import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.Status;
import com.rectangles.dto.ContainmentRequest;
import com.rectangles.dto.Request;
import org.springframework.stereotype.Service;

/**
 * Determines whether one rectangle is wholly contained within another.
 *
 * <h2>Algorithm</h2>
 * <p>Rectangle B is contained within Rectangle A if and only if all four of
 * B's corners lie inside (or on the boundary of) A. In coordinate terms:</p>
 * <pre>
 *   A.minX <= B.minX  AND  B.maxX <= A.maxX
 *   A.minY <= B.minY  AND  B.maxY <= A.maxY
 * </pre>
 *
 * <p>If they are not in a containment relationship, we check whether they
 * overlap at all (intersection without containment), or are fully separate.</p>
 */
@Service
public class ContainmentStrategy implements AnalyzerStrategy {

    /**
     * Checks whether rectangle {@code inner} is wholly contained within
     * rectangle {@code outer}.
     *
     * <p>The check is directional: it asks "is {@code inner} inside
     * {@code outer}?". To check the reverse, swap the arguments.</p>
     *
     * @param request the request containing the 2 rectangles
     * @return a {@link ContainmentResult} describing the relationship
     */
    @Override
    public ContainmentResult analyze(Request request) {
        ContainmentRequest containmentRequest = (ContainmentRequest) request;

        if (isContained(containmentRequest.getRectangleA(), containmentRequest.getRectangleB())) {
            return new ContainmentResult(Status.CONTAINED);
        }
        if (isContained(containmentRequest.getRectangleB(), containmentRequest.getRectangleA())) {
            return new ContainmentResult(Status.CONTAINED);
        }
        if (overlaps(containmentRequest.getRectangleA(), containmentRequest.getRectangleB())) {
            return new ContainmentResult(Status.INTERSECTION_NO_CONTAINMENT);
        }
        return new ContainmentResult(Status.NO_CONTAINMENT);
    }

    /**
     * Returns true if all four corners of {@code inner} lie within
     * (or on the boundary of) {@code outer}.
     */
    private boolean isContained(Rectangle outer, Rectangle inner) {
        return outer.minX <= inner.minX
            && inner.maxX <= outer.maxX
            && outer.minY <= inner.minY
            && inner.maxY <= outer.maxY;
    }

    /**
     * Returns true if the two rectangles overlap at all (share any area).
     * Two axis-aligned rectangles do NOT overlap when one is fully to the
     * left, right, above, or below the other.
     */
    private boolean overlaps(Rectangle a, Rectangle b) {
        return !(a.maxX <= b.minX   // a is fully left of b
              || b.maxX <= a.minX   // b is fully left of a
              || a.maxY <= b.minY   // a is fully below b
              || b.maxY <= a.minY); // b is fully below a
    }
}
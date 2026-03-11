package com.rectangles.domain;

/**
 * Represents the result of a containment check between two rectangles.
 */
public record ContainmentResult(com.rectangles.domain.ContainmentResult.Status status) {

    public enum Status {
        /**
         * Rectangle B is wholly inside Rectangle A.
         */
        CONTAINED,

        /**
         * The rectangles overlap but neither is wholly inside the other.
         */
        INTERSECTION_NO_CONTAINMENT,

        /**
         * The rectangles do not overlap at all.
         */
        NO_CONTAINMENT
    }

    public boolean isContained() {
        return status == Status.CONTAINED;
    }

    @Override
    public String toString() {
        return "ContainmentResult{status=" + status + "}";
    }
}
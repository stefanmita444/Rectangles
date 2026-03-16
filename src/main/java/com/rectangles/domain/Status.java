package com.rectangles.domain;

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

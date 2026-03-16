package com.rectangles.domain;

import lombok.Getter;

@Getter
public enum Type {
    /**
     * One full side of rectangle A exactly matches one full side of rectangle B.
     * Example: A's right edge and B's left edge are the same segment.
     */
    PROPER,

    /**
     * One side of rectangle A is wholly contained within a side of rectangle B
     * (but does not span the full length of B's side).
     */
    SUB_LINE,

    /**
     * A portion of a side of rectangle A overlaps with a portion of a side
     * of rectangle B, but neither is fully contained in the other.
     */
    PARTIAL,

    /**
     * The rectangles do not share any side or side segment.
     */
    NOT_ADJACENT
}

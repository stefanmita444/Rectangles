package com.rectangles.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Represents the result of a containment check between two rectangles.
 */
@ToString
@AllArgsConstructor
public class ContainmentResult extends Result {

    private final Status status;

    public boolean isContained() {
        return status == Status.CONTAINED;
    }

    public Status status() {
        return this.status;
    }
}
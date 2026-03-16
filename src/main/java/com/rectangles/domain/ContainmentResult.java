package com.rectangles.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents the result of a containment check between two rectangles.
 */
@Getter
@ToString
@AllArgsConstructor
public class ContainmentResult extends Result {

    private final Status status;

    @JsonIgnore
    public boolean isContained() {
        return status == Status.CONTAINED;
    }
}
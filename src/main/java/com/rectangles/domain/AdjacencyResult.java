package com.rectangles.domain;

import lombok.AllArgsConstructor;

/**
 * Represents the result of an adjacency check between two rectangles.
 */
@AllArgsConstructor
public class AdjacencyResult extends Result {

    private final Type type;

    public boolean isAdjacent() {
        return this.type != Type.NOT_ADJACENT;
    }

    @Override
    public String toString() {
        return "AdjacencyResult{type=" + type + "}";
    }

    public Type type() {
        return this.type;
    }
}
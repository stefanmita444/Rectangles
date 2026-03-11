package com.rectangles.domain;

/**
 * Represents a 2D point with x and y coordinates.
 */
public record Point(double x, double y) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point other)) return false;
        return Double.compare(this.x, other.x) == 0
            && Double.compare(this.y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
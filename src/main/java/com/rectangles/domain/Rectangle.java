package com.rectangles.domain;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an axis-aligned rectangle defined by two corner points.
 *
 * <p>The rectangle is defined by a bottom-left corner (x1, y1) and a
 * top-right corner (x2, y2). The constructor normalizes the corners so that
 * x1 <= x2 and y1 <= y2, regardless of how they are passed in.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *   Rectangle r = new Rectangle(1, 1, 4, 3);
 *   // minX=1, minY=1, maxX=4, maxY=3
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class Rectangle {

    /** Left edge x-coordinate */
    public double minX;

    /** Bottom edge y-coordinate */
    public double minY;

    /** Right edge x-coordinate */
    public double maxX;

    /** Top edge y-coordinate */
    public double maxY;

    /**
     * Constructs a rectangle from two corner points.
     * Coordinates are normalized so minX <= maxX and minY <= maxY.
     *
     * @param minX x-coordinate of the first corner
     * @param minY y-coordinate of the first corner
     * @param maxX x-coordinate of the second corner
     * @param maxY y-coordinate of the second corner
     * @throws IllegalArgumentException if the rectangle has zero width or height
     */
    public Rectangle(double minX, double minY, double maxX, double maxY) {
        if (minX == maxX) throw new IllegalArgumentException("Rectangle cannot have zero width.");
        if (minY == maxY) throw new IllegalArgumentException("Rectangle cannot have zero height.");

        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
    }

    @AssertTrue(message = "Rectangle cannot have zero width")
    public boolean isWidthValid() { return minX != maxX; }

    @AssertTrue(message = "Rectangle cannot have zero height")
    public boolean isHeightValid() { return minY != maxY; }

    /** Returns the top-left corner of the rectangle. */
    public Point topLeft()     { return new Point(minX, maxY); }

    /** Returns the top-right corner of the rectangle. */
    public Point topRight()    { return new Point(maxX, maxY); }

    /** Returns the bottom-left corner of the rectangle. */
    public Point bottomLeft()  { return new Point(minX, minY); }

    /** Returns the bottom-right corner of the rectangle. */
    public Point bottomRight() { return new Point(maxX, minY); }

    @Override
    public String toString() {
        return "Rectangle[(" + minX + "," + minY + ") -> (" + maxX + "," + maxY + ")]";
    }
}
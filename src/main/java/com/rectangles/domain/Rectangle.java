package com.rectangles.domain;

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
public class Rectangle {

    /** Left edge x-coordinate */
    public final double minX;

    /** Bottom edge y-coordinate */
    public final double minY;

    /** Right edge x-coordinate */
    public final double maxX;

    /** Top edge y-coordinate */
    public final double maxY;

    /**
     * Constructs a rectangle from two corner points.
     * Coordinates are normalized so minX <= maxX and minY <= maxY.
     *
     * @param x1 x-coordinate of the first corner
     * @param y1 y-coordinate of the first corner
     * @param x2 x-coordinate of the second corner
     * @param y2 y-coordinate of the second corner
     * @throws IllegalArgumentException if the rectangle has zero width or height
     */
    public Rectangle(double x1, double y1, double x2, double y2) {
        if (x1 == x2) throw new IllegalArgumentException("Rectangle cannot have zero width.");
        if (y1 == y2) throw new IllegalArgumentException("Rectangle cannot have zero height.");

        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
    }

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
package com.rectangles.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {
    @Test
    @DisplayName("Constructor normalizes coordinates")
    void testNormalization() {
        Rectangle r = new Rectangle(5, 8, 1, 2); // passed in reverse order
        assertEquals(1, r.minX);
        assertEquals(2, r.minY);
        assertEquals(5, r.maxX);
        assertEquals(8, r.maxY);
    }

    @Test
    @DisplayName("Zero width rectangle throws exception")
    void testZeroWidth() {
        assertThrows(IllegalArgumentException.class,
            () -> new Rectangle(3, 0, 3, 5));
    }

    @Test
    @DisplayName("Zero height rectangle throws exception")
    void testZeroHeight() {
        assertThrows(IllegalArgumentException.class,
            () -> new Rectangle(0, 3, 5, 3));
    }

    @Test
    @DisplayName("Rectangle with negative coordinates normalizes correctly")
    void testNegativeCoordinates() {
        Rectangle r = new Rectangle(-5, -3, -1, -1);
        assertEquals(-5, r.minX);
        assertEquals(-3, r.minY);
        assertEquals(-1, r.maxX);
        assertEquals(-1, r.maxY);
    }

    @Test
    @DisplayName("Rectangle spanning negative to positive coordinates")
    void testCrossOrigin() {
        Rectangle r = new Rectangle(-4, -4, 4, 4);
        assertEquals(-4, r.minX);
        assertEquals(-4, r.minY);
        assertEquals(4,  r.maxX);
        assertEquals(4,  r.maxY);
    }

    @Test
    @DisplayName("Corner accessors return correct points")
    void testCornerAccessors() {
        Rectangle r = new Rectangle(1, 2, 5, 6);
        assertEquals(new Point(1, 6), r.topLeft());
        assertEquals(new Point(5, 6), r.topRight());
        assertEquals(new Point(1, 2), r.bottomLeft());
        assertEquals(new Point(5, 2), r.bottomRight());
    }
}
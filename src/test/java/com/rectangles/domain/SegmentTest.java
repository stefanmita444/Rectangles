package com.rectangles.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTest {

    @Test
    @DisplayName("Horizontal segment is detected correctly")
    void testIsHorizontal() {
        Segment s = new Segment(new Point(0, 3), new Point(5, 3));
        assertTrue(s.isHorizontal());
    }

    @Test
    @DisplayName("Vertical segment is not horizontal")
    void testIsNotHorizontal() {
        Segment s = new Segment(new Point(3, 0), new Point(3, 5));
        assertFalse(s.isHorizontal());
    }

    @Test
    @DisplayName("getConstantY returns the y value of a horizontal segment")
    void testGetConstantY() {
        Segment s = new Segment(new Point(1, 4), new Point(7, 4));
        assertEquals(4.0, s.getConstantY());
    }

    @Test
    @DisplayName("getConstantX returns the x value of a vertical segment")
    void testGetConstantX() {
        Segment s = new Segment(new Point(3, 1), new Point(3, 8));
        assertEquals(3.0, s.getConstantX());
    }

    @Test
    @DisplayName("getMinX and getMaxX return correct bounds regardless of point order")
    void testGetMinMaxX() {
        Segment s = new Segment(new Point(6, 2), new Point(2, 2));
        assertEquals(2.0, s.getMinX());
        assertEquals(6.0, s.getMaxX());
    }

    @Test
    @DisplayName("getMinY and getMaxY return correct bounds regardless of point order")
    void testGetMinMaxY() {
        Segment s = new Segment(new Point(3, 8), new Point(3, 1));
        assertEquals(1.0, s.getMinY());
        assertEquals(8.0, s.getMaxY());
    }
}

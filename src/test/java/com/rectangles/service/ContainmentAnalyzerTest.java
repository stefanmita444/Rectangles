package com.rectangles.service;

import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContainmentAnalyzer.class})
public class ContainmentAnalyzerTest {

    @Autowired
    private ContainmentAnalyzer analyzer;

    @BeforeEach
    void setup() {
        analyzer = new ContainmentAnalyzer();
    }

    @Test
    @DisplayName("Inner rectangle fully contained within outer")
    void testFullyContained() {
        Rectangle outer = new Rectangle(0, 0, 10, 10);
        Rectangle inner = new Rectangle(2, 2, 7, 7);

        ContainmentResult result = analyzer.analyze(outer, inner);

        assertEquals(ContainmentResult.Status.CONTAINED, result.status());
        assertTrue(result.isContained());
    }

    @Test
    @DisplayName("Inner rectangle touching the boundary is still contained")
    void testContainedOnBoundary() {
        Rectangle outer = new Rectangle(0, 0, 10, 10);
        Rectangle inner = new Rectangle(0, 0, 5, 5); // shares bottom-left corner

        ContainmentResult result = analyzer.analyze(outer, inner);

        assertEquals(ContainmentResult.Status.CONTAINED, result.status());
    }

    @Test
    @DisplayName("Rectangles are completely separate - no containment")
    void testNoContainment() {
        Rectangle a = new Rectangle(0, 0, 3, 3);
        Rectangle b = new Rectangle(5, 5, 9, 9);

        ContainmentResult result = analyzer.analyze(a, b);

        assertEquals(ContainmentResult.Status.NO_CONTAINMENT, result.status());
        assertFalse(result.isContained());
    }

    @Test
    @DisplayName("Rectangles overlap but neither contains the other")
    void testIntersectionNoContainment() {
        Rectangle a = new Rectangle(0, 0, 5, 5);
        Rectangle b = new Rectangle(3, 3, 8, 8);

        ContainmentResult result = analyzer.analyze(a, b);

        assertEquals(ContainmentResult.Status.INTERSECTION_NO_CONTAINMENT, result.status());
        assertFalse(result.isContained());
    }

    @Test
    @DisplayName("Identical rectangles - one contains the other")
    void testIdenticalRectangles() {
        Rectangle a = new Rectangle(0, 0, 5, 5);
        Rectangle b = new Rectangle(0, 0, 5, 5);

        ContainmentResult result = analyzer.analyze(a, b);

        assertEquals(ContainmentResult.Status.CONTAINED, result.status());
    }

    @Test
    @DisplayName("Small outer cannot contain larger inner")
    void testReversedContainment() {
        Rectangle outer = new Rectangle(2, 2, 5, 5);
        Rectangle inner = new Rectangle(0, 0, 10, 10); // inner is actually larger

        ContainmentResult result = analyzer.analyze(outer, inner);

        // 'inner' is not contained in 'outer'; they overlap
        assertEquals(ContainmentResult.Status.INTERSECTION_NO_CONTAINMENT, result.status());
    }

}
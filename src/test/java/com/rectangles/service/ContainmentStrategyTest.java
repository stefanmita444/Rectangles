package com.rectangles.service;

import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.Rectangle;
import com.rectangles.domain.Status;
import com.rectangles.dto.ContainmentRequest;
import com.rectangles.service.strategy.ContainmentStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContainmentStrategy.class})
public class ContainmentStrategyTest {

    @Autowired
    private ContainmentStrategy analyzer;

    @Test
    @DisplayName("b rectangle fully contained within a")
    void testFullyContained() {
        Rectangle a = new Rectangle(0, 0, 10, 10);
        Rectangle b = new Rectangle(2, 2, 7, 7);

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.CONTAINED, result.status());
        assertTrue(result.isContained());
    }

    @Test
    @DisplayName("b rectangle touching the boundary is still contained")
    void testContainedOnBoundary() {
        Rectangle a = new Rectangle(0, 0, 10, 10);
        Rectangle b = new Rectangle(0, 0, 5, 5); // shares bottom-left corner

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.CONTAINED, result.status());
    }

    @Test
    @DisplayName("Rectangles are completely separate - no containment")
    void testNoContainment() {
        Rectangle a = new Rectangle(0, 0, 3, 3);
        Rectangle b = new Rectangle(5, 5, 9, 9);

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.NO_CONTAINMENT, result.status());
        assertFalse(result.isContained());
    }

    @Test
    @DisplayName("Rectangles overlap but neither contains the other")
    void testIntersectionNoContainment() {
        Rectangle a = new Rectangle(0, 0, 5, 5);
        Rectangle b = new Rectangle(3, 3, 8, 8);

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.INTERSECTION_NO_CONTAINMENT, result.status());
        assertFalse(result.isContained());
    }

    @Test
    @DisplayName("Identical rectangles - one contains the other")
    void testIdenticalRectangles() {
        Rectangle a = new Rectangle(0, 0, 5, 5);
        Rectangle b = new Rectangle(0, 0, 5, 5);

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.CONTAINED, result.status());
    }

    @Test
    @DisplayName("b rectangle fully contained reversed")
    void testFullyContainedba() {
        Rectangle a = new Rectangle(0, 0, 10, 10);
        Rectangle b = new Rectangle(2, 2, 7, 7);

        ContainmentRequest containmentRequest = new ContainmentRequest(b, a);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.CONTAINED, result.status());
        assertTrue(result.isContained());
    }

    @Test
    @DisplayName("A fully below B - no containment (hits a.maxY <= b.minY branch)")
    void testNoContainmentABelowB() {
        // X ranges overlap but A is entirely below B
        Rectangle a = new Rectangle(0, 0, 4, 2);
        Rectangle b = new Rectangle(0, 3, 4, 6);

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.NO_CONTAINMENT, result.status());
    }

    @Test
    @DisplayName("B fully to the left of A - no containment (hits b.maxX <= a.minX branch)")
    void testNoContainmentBLeftOfA() {
        Rectangle a = new Rectangle(5, 0, 9, 4);
        Rectangle b = new Rectangle(0, 0, 3, 4);

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.NO_CONTAINMENT, result.status());
    }

    @Test
    @DisplayName("B fully below A - no containment (hits b.maxY <= a.minY branch)")
    void testNoContainmentBBelowA() {
        Rectangle a = new Rectangle(0, 5, 4, 9);
        Rectangle b = new Rectangle(0, 0, 4, 3);

        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
        ContainmentResult result = analyzer.analyze(containmentRequest);

        assertEquals(Status.NO_CONTAINMENT, result.status());
    }

}
package com.rectangles.service;

import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Point;
import com.rectangles.domain.Rectangle;
import com.rectangles.dto.IntersectionRequest;
import com.rectangles.service.strategy.IntersectionStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntersectionStrategy.class})
public class IntersectionStrategyTest {

    @Autowired
    private IntersectionStrategy analyzer;

    @Test
    @DisplayName("Overlapping rectangles - edges cross at 2 points")
    void testOverlappingRectangles() {
        // A: (0,0)-(4,4)  B: (2,2)-(6,6)
        // A's right edge (x=4) crosses B's bottom edge (y=2) at (4,2)
        // A's top edge (y=4)   crosses B's left edge (x=2)  at (2,4)
        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(2, 2, 6, 6);

        IntersectionRequest intersectionRequest = new IntersectionRequest(a, b);
        IntersectionResult result = analyzer.analyze(intersectionRequest);

        assertTrue(result.hasIntersection());
        assertEquals(2, result.intersectionPoints().size());
        assertTrue(result.intersectionPoints().contains(new Point(4, 2)));
        assertTrue(result.intersectionPoints().contains(new Point(2, 4)));
    }

    @Test
    @DisplayName("Separate rectangles - no intersection")
    void testSeparateRectangles() {
        Rectangle a = new Rectangle(0, 0, 3, 3);
        Rectangle b = new Rectangle(5, 5, 8, 8);

        IntersectionRequest intersectionRequest = new IntersectionRequest(a, b);
        IntersectionResult result = analyzer.analyze(intersectionRequest);

        assertFalse(result.hasIntersection());
        assertTrue(result.intersectionPoints().isEmpty());
    }

    @Test
    @DisplayName("Contained rectangle - edges do not cross")
    void testContainedRectangle() {
        // B is fully inside A — edges never cross
        Rectangle a = new Rectangle(0, 0, 10, 10);
        Rectangle b = new Rectangle(2, 2, 5, 5);

        IntersectionRequest intersectionRequest = new IntersectionRequest(a, b);
        IntersectionResult result = analyzer.analyze(intersectionRequest);

        assertFalse(result.hasIntersection());
    }

    @Test
    @DisplayName("Cross-shape rectangles - edges cross at 4 points")
    void testCrossShapeIntersection() {
        // A is wide and short (y=2..5, x=0..8), B is narrow and tall (x=3..6, y=0..8)
        // A's top edge (y=5) crosses B's left (x=3) at (3,5) and B's right (x=6) at (6,5)
        // A's bottom edge (y=2) crosses B's left (x=3) at (3,2) and B's right (x=6) at (6,2)
        Rectangle a = new Rectangle(0, 2, 8, 5);
        Rectangle b = new Rectangle(3, 0, 6, 8);

        IntersectionRequest intersectionRequest = new IntersectionRequest(a, b);
        IntersectionResult result = analyzer.analyze(intersectionRequest);

        assertTrue(result.hasIntersection());
        assertEquals(4, result.intersectionPoints().size());
        assertTrue(result.intersectionPoints().contains(new Point(3, 5)));
        assertTrue(result.intersectionPoints().contains(new Point(6, 5)));
        assertTrue(result.intersectionPoints().contains(new Point(3, 2)));
        assertTrue(result.intersectionPoints().contains(new Point(6, 2)));
    }

    @Test
    @DisplayName("Rectangles touching at a single edge - intersection points at corners")
    void testTouchingEdge() {
        // A's right edge touches B's left edge exactly — they share a full side
        // This is adjacency, not edge crossing. No interior crossing points.
        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(4, 0, 8, 4);

        IntersectionRequest intersectionRequest = new IntersectionRequest(a, b);
        IntersectionResult result = analyzer.analyze(intersectionRequest);

        // Edges coincide but don't properly cross — no unique interior crossing points
        assertFalse(result.hasIntersection());
    }

    @Test
    @DisplayName("Rectangles overlapping on one side only - 2 intersection points")
    void testPartialOverlapOnOneSide() {
        // A: (0,0)-(4,4)  B: (2,-2)-(6,2)
        // A's bottom edge (y=0) crossed by B's left edge (x=2) at (2,0)
        // A's right edge (x=4) crossed by B's top edge (y=2)  at (4,2)
        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(2, -2, 6, 2);

        IntersectionRequest intersectionRequest = new IntersectionRequest(a, b);
        IntersectionResult result = analyzer.analyze(intersectionRequest);

        assertTrue(result.hasIntersection());
        assertEquals(2, result.intersectionPoints().size());
        assertTrue(result.intersectionPoints().contains(new Point(2, 0)));
        assertTrue(result.intersectionPoints().contains(new Point(4, 2)));
    }
}
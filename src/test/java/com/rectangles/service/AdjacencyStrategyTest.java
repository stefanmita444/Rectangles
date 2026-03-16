package com.rectangles.service;

import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.Rectangle;
import com.rectangles.domain.Type;
import com.rectangles.dto.AdjacencyRequest;
import com.rectangles.service.strategy.AdjacencyStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AdjacencyStrategy.class})
public class AdjacencyStrategyTest {

    @Autowired
    private AdjacencyStrategy analyzer;

    @Test
    @DisplayName("PROPER adjacency - full side shared horizontally")
    void testProperAdjacencyHorizontal() {
        // A's right edge and B's left edge are both from y=0 to y=4
        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(4, 0, 8, 4);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.PROPER, result.getType());
        assertTrue(result.isAdjacent());
    }

    @Test
    @DisplayName("PROPER adjacency - full side shared vertically")
    void testProperAdjacencyVertical() {
        // A's top edge and B's bottom edge are both from x=0 to x=4
        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(0, 4, 4, 8);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.PROPER, result.getType());
    }

    @Test
    @DisplayName("SUB_LINE adjacency - A's side is inside B's longer side")
    void testSubLineAdjacencyAInsideB() {
        // B's left edge runs from y=0 to y=8; A's right edge runs y=2 to y=6
        // A's side is wholly contained within B's longer side
        Rectangle a = new Rectangle(0, 2, 4, 6);
        Rectangle b = new Rectangle(4, 0, 8, 8);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.SUB_LINE, result.getType());
    }

    @Test
    @DisplayName("SUB_LINE adjacency - B's side is inside A's longer side")
    void testSubLineAdjacencyBInsideA() {
        // A's right edge runs y=0 to y=8; B's left edge runs y=2 to y=5
        Rectangle a = new Rectangle(0, 0, 4, 8);
        Rectangle b = new Rectangle(4, 2, 8, 5);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.SUB_LINE, result.getType());
    }

    @Test
    @DisplayName("PARTIAL adjacency - sides partially overlap")
    void testPartialAdjacency() {
        // A's right edge: y=0 to y=5   B's left edge: y=3 to y=8
        // Overlap from y=3 to y=5 — partial
        Rectangle a = new Rectangle(0, 0, 4, 5);
        Rectangle b = new Rectangle(4, 3, 8, 8);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.PARTIAL, result.getType());
    }

    @Test
    @DisplayName("NOT adjacent - gap between rectangles")
    void testNotAdjacent() {
        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(5, 0, 9, 4); // gap of 1 unit

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.NOT_ADJACENT, result.getType());
        assertFalse(result.isAdjacent());
    }

    @Test
    @DisplayName("NOT adjacent - rectangles are diagonal to each other")
    void testNotAdjacentDiagonal() {
        Rectangle a = new Rectangle(0, 0, 3, 3);
        Rectangle b = new Rectangle(5, 5, 8, 8);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.NOT_ADJACENT, result.getType());
    }

    @Test
    @DisplayName("NOT adjacent - sides on same line but with a gap")
    void testSameLineButGap() {
        // A's right edge at x=4, y=0 to y=2
        // B's left edge at x=4, y=5 to y=8  → same x, but y ranges don't touch
        Rectangle a = new Rectangle(0, 0, 4, 2);
        Rectangle b = new Rectangle(4, 5, 8, 8);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.NOT_ADJACENT, result.getType());
    }

    @Test
    @DisplayName("Overlapping rectangles are NOT adjacent")
    void testOverlappingNotAdjacent() {
        Rectangle a = new Rectangle(0, 0, 5, 5);
        Rectangle b = new Rectangle(3, 3, 8, 8);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.NOT_ADJACENT, result.getType());
    }

    @Test
    @DisplayName("PROPER adjacency - B is to the left of A (hits a.minX == b.maxX branch)")
    void testProperAdjacencyBLeftOfA() {
        // B's right edge and A's left edge both run from y=0 to y=4
        Rectangle a = new Rectangle(4, 0, 8, 4);
        Rectangle b = new Rectangle(0, 0, 4, 4);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.PROPER, result.getType());
        assertTrue(result.isAdjacent());
    }

    @Test
    @DisplayName("NOT adjacent - horizontal boundary found but X ranges have a gap (hits false branch of horizontal type != NOT_ADJACENT)")
    void testHorizontalBoundaryFoundButXGap() {
        // A's top edge (y=4) == B's bottom edge (y=4), but A is x=0..2 and B is x=5..9 — no X overlap
        Rectangle a = new Rectangle(0, 0, 2, 4);
        Rectangle b = new Rectangle(5, 4, 9, 8);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.NOT_ADJACENT, result.getType());
    }

    @Test
    @DisplayName("NOT adjacent - rectangles touch only at a single corner point")
    void testCornerTouchNotAdjacent() {
        // A's top-right corner and B's bottom-left corner meet at (3,3) — a single point, not a segment
        Rectangle a = new Rectangle(0, 0, 3, 3);
        Rectangle b = new Rectangle(3, 3, 6, 6);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.NOT_ADJACENT, result.getType());
    }

    @Test
    @DisplayName("PROPER adjacency - B is below A (hits a.minY == b.maxY branch)")
    void testProperAdjacencyBBelowA() {
        // A's bottom edge and B's top edge both run from x=0 to x=4
        Rectangle a = new Rectangle(0, 4, 4, 8);
        Rectangle b = new Rectangle(0, 0, 4, 4);

        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);

        AdjacencyResult result = analyzer.analyze(adjacencyRequest);

        assertEquals(Type.PROPER, result.getType());
        assertTrue(result.isAdjacent());
    }

}
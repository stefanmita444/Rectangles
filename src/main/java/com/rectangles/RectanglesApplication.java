package com.rectangles;

import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Rectangle;
import com.rectangles.service.AdjacencyAnalyzer;
import com.rectangles.service.ContainmentAnalyzer;
import com.rectangles.service.IntersectionAnalyzer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Facade that exposes all three rectangle analysis operations.
 *
 * <p>Usage example:</p>
 * <pre>
 *   RectanglesApplication analyzer = new RectanglesApplication();
 *   Rectangle a = new Rectangle(0, 0, 4, 4);
 *   Rectangle b = new Rectangle(2, 2, 6, 6);
 *
 *   IntersectionResult  ir = analyzer.intersection(a, b);
 *   ContainmentResult   cr = analyzer.containment(a, b);
 *   AdjacencyResult     ar = analyzer.adjacency(a, b);
 * </pre>
 */
@SpringBootApplication
public class RectanglesApplication {

    private final IntersectionAnalyzer intersectionAnalyzer  = new IntersectionAnalyzer();
    private final ContainmentAnalyzer containmentAnalyzer   = new ContainmentAnalyzer();
    private final AdjacencyAnalyzer adjacencyAnalyzer     = new AdjacencyAnalyzer();

    /**
     * Determines whether the edges of the two rectangles cross each other
     * and returns the crossing points.
     *
     * @param a first rectangle
     * @param b second rectangle
     * @return intersection result with crossing points
     */
    public IntersectionResult intersection(Rectangle a, Rectangle b) {
        return intersectionAnalyzer.analyze(a, b);
    }

    /**
     * Determines whether rectangle {@code inner} is wholly contained
     * within rectangle {@code outer}.
     *
     * @param outer the candidate outer rectangle
     * @param inner the candidate inner rectangle
     * @return containment result
     */
    public ContainmentResult containment(Rectangle outer, Rectangle inner) {
        return containmentAnalyzer.analyze(outer, inner);
    }

    /**
     * Determines whether two rectangles are adjacent (share a side or
     * part of a side) and classifies the type of adjacency.
     *
     * @param a first rectangle
     * @param b second rectangle
     * @return adjacency result
     */
    public AdjacencyResult adjacency(Rectangle a, Rectangle b) {
        return adjacencyAnalyzer.analyze(a, b);
    }

    // -----------------------------------------------------------------------
    // Quick demo
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        RectanglesApplication analyzer = new RectanglesApplication();

        System.out.println("=== Intersection Demo ===");
        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(2, 2, 6, 6);
        IntersectionResult ir = analyzer.intersection(a, b);
        System.out.println("A=" + a + "  B=" + b);
        System.out.println("Result: " + ir);

        System.out.println("\n=== Containment Demo ===");
        Rectangle outer = new Rectangle(0, 0, 10, 10);
        Rectangle inner = new Rectangle(2, 2, 5, 5);
        ContainmentResult cr = analyzer.containment(outer, inner);
        System.out.println("Outer=" + outer + "  Inner=" + inner);
        System.out.println("Result: " + cr);

        System.out.println("\n=== Adjacency Demo ===");
        Rectangle left  = new Rectangle(0, 0, 4, 4);
        Rectangle right = new Rectangle(4, 0, 8, 4);
        AdjacencyResult ar = analyzer.adjacency(left, right);
        System.out.println("Left=" + left + "  Right=" + right);
        System.out.println("Result: " + ar);
    }
}

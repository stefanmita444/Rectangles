package com.rectangles;

import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Rectangle;
import com.rectangles.service.AdjacencyAnalyzer;
import com.rectangles.service.ContainmentAnalyzer;
import com.rectangles.service.IntersectionAnalyzer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * Facade that exposes all three rectangle analysis operations.
 *
 * <p>Spring Boot entry point. The application starts via {@link #main}, which boots
 * the Spring context. Once ready, {@link #run} executes a quick demo.
 * The three analyzer beans are injected via constructor injection.</p>
 */
@SpringBootApplication
public class RectanglesApplication implements CommandLineRunner {

    private final IntersectionAnalyzer intersectionAnalyzer;
    private final ContainmentAnalyzer containmentAnalyzer;
    private final AdjacencyAnalyzer adjacencyAnalyzer;

    public RectanglesApplication(IntersectionAnalyzer intersectionAnalyzer,
                                  ContainmentAnalyzer containmentAnalyzer,
                                  AdjacencyAnalyzer adjacencyAnalyzer) {
        this.intersectionAnalyzer = intersectionAnalyzer;
        this.containmentAnalyzer  = containmentAnalyzer;
        this.adjacencyAnalyzer    = adjacencyAnalyzer;
    }

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
    // Entry point
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        SpringApplication.run(RectanglesApplication.class, args);
    }

    // -----------------------------------------------------------------------
    // Interactive CLI — runs after Spring context is ready
    // -----------------------------------------------------------------------

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("╔══════════════════════════════╗");
            System.out.println("║     Rectangle Analyzer CLI   ║");
            System.out.println("╚══════════════════════════════╝");

            boolean running = true;
            while (running) {
                System.out.println("\nChoose an operation:");
                System.out.println("  1 - Intersection");
                System.out.println("  2 - Containment");
                System.out.println("  3 - Adjacency");
                System.out.println("  0 - Exit");
                System.out.print("Option: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> {
                        System.out.println("\n-- Intersection --");
                        Rectangle a = promptRectangle(scanner, "Rectangle A");
                        Rectangle b = promptRectangle(scanner, "Rectangle B");
                        if (a == null || b == null) break;
                        IntersectionResult ir = intersection(a, b);
                        System.out.println("Result: " + ir);
                    }
                    case "2" -> {
                        System.out.println("\n-- Containment --");
                        Rectangle outer = promptRectangle(scanner, "Outer rectangle");
                        Rectangle inner = promptRectangle(scanner, "Inner rectangle");
                        if (outer == null || inner == null) break;
                        ContainmentResult cr = containment(outer, inner);
                        System.out.println("Result: " + cr);
                    }
                    case "3" -> {
                        System.out.println("\n-- Adjacency --");
                        Rectangle a = promptRectangle(scanner, "Rectangle A");
                        Rectangle b = promptRectangle(scanner, "Rectangle B");
                        if (a == null || b == null) break;
                        AdjacencyResult ar = adjacency(a, b);
                        System.out.println("Result: " + ar);
                    }
                    case "0" -> {
                        System.out.println("Goodbye.");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please enter 0, 1, 2 or 3.");
                }
            }
        }
    }

    /**
     * Prompts the user to enter coordinates for a rectangle.
     * Returns null if the input is invalid, allowing the loop to continue.
     */
    private Rectangle promptRectangle(Scanner scanner, String label) {
        try {
            System.out.println(label + " — enter two corner points:");
            System.out.print("  x1: "); double x1 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("  y1: "); double y1 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("  x2: "); double x2 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("  y2: "); double y2 = Double.parseDouble(scanner.nextLine().trim());
            return new Rectangle(x1, y1, x2, y2);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number — please try again.");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rectangle: " + e.getMessage());
            return null;
        }
    }
}

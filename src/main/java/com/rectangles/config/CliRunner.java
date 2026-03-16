package com.rectangles.config;

import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Rectangle;
import com.rectangles.dto.AdjacencyRequest;
import com.rectangles.dto.ContainmentRequest;
import com.rectangles.dto.IntersectionRequest;
import com.rectangles.service.AnalyzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Profile("cli")
@RequiredArgsConstructor
public class CliRunner implements CommandLineRunner {

  private final AnalyzerService analyzerService;

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
                        IntersectionRequest intersectionRequest = new IntersectionRequest(a, b);
                        IntersectionResult ir = (IntersectionResult) analyzerService.analyze(intersectionRequest);
                        System.out.println("Result: " + ir);
                    }
                    case "2" -> {
                        System.out.println("\n-- Containment --");
                        Rectangle a = promptRectangle(scanner, "Rectangle A");
                        Rectangle b = promptRectangle(scanner, "Rectangle B");
                        if (a == null || b == null) break;
                        ContainmentRequest containmentRequest = new ContainmentRequest(a, b);
                        ContainmentResult cr = (ContainmentResult) analyzerService.analyze(containmentRequest);
                        System.out.println("Result: " + cr);
                    }
                    case "3" -> {
                        System.out.println("\n-- Adjacency --");
                        Rectangle a = promptRectangle(scanner, "Rectangle A");
                        Rectangle b = promptRectangle(scanner, "Rectangle B");
                        if (a == null || b == null) break;
                        AdjacencyRequest adjacencyRequest = new AdjacencyRequest(a, b);
                        AdjacencyResult ar = (AdjacencyResult) analyzerService.analyze(adjacencyRequest);
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

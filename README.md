# Rectangle Analyzer

A Spring Boot application implementing three geometric algorithms for analyzing axis-aligned rectangles.
Given any two rectangles — each defined by two corner points `(x1, y1)` and `(x2, y2)` — the application determines:

- **Intersection** — whether the edges of the rectangles cross, and at which exact points
- **Containment** — whether one rectangle is wholly inside the other
- **Adjacency** — whether the rectangles share a side or part of a side, and what kind

> All rectangles are axis-aligned. Sides are always parallel to the X and Y axes.

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 17 or higher |
| Gradle | 9.x via wrapper (no install needed) |
| Git | Any recent version |

Verify Java:
```bash
java -version
```

---

## Project Structure

```
src/
├── main/java/com/rectangles/
│   ├── RectanglesApplication.java        # Entry point and facade
│   ├── domain/
│   │   ├── Rectangle.java                # Rectangle model (two corners)
│   │   ├── Point.java                    # 2D point record
│   │   ├── IntersectionResult.java       # Result object for intersection
│   │   ├── ContainmentResult.java        # Result object for containment
│   │   └── AdjacencyResult.java          # Result object for adjacency
│   └── service/
│       ├── IntersectionAnalyzer.java     # @Service — intersection logic
│       ├── ContainmentAnalyzer.java      # @Service — containment logic
│       └── AdjacencyAnalyzer.java        # @Service — adjacency logic
└── test/java/com/rectangles/
    ├── domain/
    │   └── RectangleTest.java            # Model validation tests
    └── service/
        ├── IntersectionAnalyzerTest.java # Intersection tests
        ├── ContainmentAnalyzerTest.java  # Containment tests
        └── AdjacencyAnalyzerTest.java    # Adjacency tests
```

---

## Build & Run

### Clean build (compile + test + JAR)
```bash
./gradlew clean build
```

### Run tests only
```bash
./gradlew clean test
```

Force re-run even if nothing has changed (Gradle caches results by default):
```bash
./gradlew clean test --rerun-tasks
```

### Run the application
```bash
./gradlew bootRun
```

Or build and run the JAR directly:
```bash
./gradlew bootJar
java -jar build/libs/Rectangles-0.0.1-SNAPSHOT.jar
```

### View test report
After running tests, open the HTML report in your browser:
```
build/reports/tests/test/index.html
```

### Linux
```bash
chmod +x gradlew
./gradlew clean build
```

---

## Dependencies

All dependencies are pulled automatically from Maven Central on first build.

| Dependency | Scope | Purpose |
|---|---|---|
| `spring-boot-starter` | implementation | Core Spring Boot framework |
| `spring-boot-starter-test` | testImplementation | JUnit 5, AssertJ, Mockito |
| `junit-platform-launcher` | testRuntimeOnly | Required to run JUnit 5 tests with Gradle |

---

## The Algorithms

### Rectangle Model
Defined by two corners `(x1, y1)` and `(x2, y2)`. The constructor normalizes input so `minX <= maxX` and `minY <= maxY` regardless of which corner is passed first. Zero-width or zero-height rectangles throw `IllegalArgumentException`.

---

### Intersection
Checks whether the **edges** of two rectangles physically cross and returns the exact crossing points.

Each rectangle has 4 edges (top, bottom, left, right). All 16 pairs are tested:
- Same-direction pairs (both horizontal or both vertical) are parallel — skipped
- One horizontal + one vertical — crossing point is `(verticalEdge.x, horizontalEdge.y)`, counted only if it falls **strictly inside** both segments

Strict inequalities (`<` not `<=`) ensure rectangles that merely touch along a shared edge are not counted as intersecting — keeping Intersection and Adjacency cleanly separated.

| Scenario | Result |
|---|---|
| Edges cross diagonally | 2 intersection points |
| One rectangle fully inside the other | No intersection (edges never cross) |
| Fully separate | No intersection |
| Touching on a shared edge only | No intersection |
| Cross shape (one wide, one tall) | 4 intersection points |

---

### Containment
Checks whether one rectangle is wholly inside another.

| Status | Meaning |
|---|---|
| `CONTAINED` | All corners of the inner rectangle lie within (or on the boundary of) the outer |
| `INTERSECTION_NO_CONTAINMENT` | Rectangles overlap but neither is fully inside the other |
| `NO_CONTAINMENT` | Rectangles are completely separate |

---

### Adjacency
Checks whether two rectangles share a side or part of a side. First confirms they touch on the same boundary line, then classifies the type of overlap.

| Type | Meaning |
|---|---|
| `PROPER` | Both full sides match exactly — same length and position |
| `SUB_LINE` | One side fits entirely within the other's longer side |
| `PARTIAL` | Sides partially overlap but neither contains the other |
| `NOT_ADJACENT` | No shared boundary (including same-line cases with a gap) |

---

## Test Suite

Tests use JUnit 5. Service tests use `@ExtendWith(SpringExtension.class)` and `@ContextConfiguration` to verify Spring bean wiring alongside algorithm correctness.

| Test Class | Tests | Coverage |
|---|---|---|
| `RectangleTest` | 4 | Normalization, zero-dimension validation, corner accessors |
| `IntersectionAnalyzerTest` | 6 | Overlapping, separate, contained, cross-shape, adjacent (no crossing), partial overlap |
| `ContainmentAnalyzerTest` | 6 | Fully contained, boundary, separate, overlap, identical, reversed sizes |
| `AdjacencyAnalyzerTest` | 8 | Proper (H+V), sub-line (both directions), partial, not adjacent (gap, diagonal, y-gap, overlapping) |

---

## Result Objects

Each algorithm returns a dedicated result record carrying both a classification and any associated data.

| Class | Fields | Values |
|---|---|---|
| `IntersectionResult` | `hasIntersection`, `intersectionPoints` | `boolean` + `List<Point>` of crossing coordinates |
| `ContainmentResult` | `status` | `CONTAINED`, `INTERSECTION_NO_CONTAINMENT`, `NO_CONTAINMENT` |
| `AdjacencyResult` | `type` | `PROPER`, `SUB_LINE`, `PARTIAL`, `NOT_ADJACENT` |
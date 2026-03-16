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
│   ├── RectanglesApplication.java        # Spring Boot entry point
│   ├── config/
│   │   └── CliRunner.java                # Interactive CLI (active on 'cli' profile only)
│   ├── controller/
│   │   └── AnalyzerController.java       # REST endpoints (/analyze/*)
│   ├── service/
│   │   ├── AnalyzerService.java          # Routes requests to the correct strategy
│   │   └── strategy/
│   │       ├── AnalyzerStrategy.java     # Strategy interface
│   │       ├── IntersectionStrategy.java # Intersection logic
│   │       ├── ContainmentStrategy.java  # Containment logic
│   │       └── AdjacencyStrategy.java    # Adjacency logic
│   ├── dto/
│   │   ├── Request.java                  # Abstract base request
│   │   ├── IntersectionRequest.java      # Request for intersection
│   │   ├── ContainmentRequest.java       # Request for containment
│   │   └── AdjacencyRequest.java         # Request for adjacency
│   └── domain/
│       ├── Rectangle.java                # Rectangle model (two corners)
│       ├── Point.java                    # 2D point
│       ├── Segment.java                  # Line segment (start + end Point)
│       ├── Result.java                   # Abstract base result
│       ├── IntersectionResult.java       # Result for intersection
│       ├── ContainmentResult.java        # Result for containment
│       ├── AdjacencyResult.java          # Result for adjacency
│       ├── Status.java                   # Enum — containment status
│       └── Type.java                     # Enum — adjacency type
└── test/java/com/rectangles/
    ├── domain/
    │   ├── RectangleTest.java
    │   └── SegmentTest.java
    ├── service/
    │   ├── AnalyzerServiceTest.java
    │   ├── IntersectionStrategyTest.java
    │   ├── ContainmentStrategyTest.java
    │   └── AdjacencyStrategyTest.java
    └── controller/
        └── AnalyzerControllerTest.java
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

### Run the application (REST API)
```bash
./gradlew bootRun
```

### Run the application (CLI mode)
```bash
./gradlew bootRun --args='--spring.profiles.active=cli'
```

Or build and run the JAR directly:
```bash
./gradlew bootJar

# REST API
java -jar build/libs/Rectangles-0.0.1-SNAPSHOT.jar

# CLI mode
java -jar build/libs/Rectangles-0.0.1-SNAPSHOT.jar --spring.profiles.active=cli
```

### View test report
After running tests, open the HTML report in your browser:
```
build/reports/tests/test/index.html
```

### View coverage report
JaCoCo runs automatically after every test run. Open the HTML report in your browser:
```
build/reports/jacoco/test/html/index.html
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
| `spring-boot-starter-web` | implementation | Spring MVC + embedded Tomcat for REST API |
| `spring-boot-starter` | implementation | Core Spring Boot framework |
| `lombok` | implementation | Boilerplate reduction (`@Getter`, `@Setter`, etc.) |
| `spring-boot-starter-test` | testImplementation | JUnit 5, AssertJ, Mockito |
| `junit-platform-launcher` | testRuntimeOnly | Required to run JUnit 5 tests with Gradle |

---

## REST API

The application exposes a REST API under `/analyze`. All endpoints accept a JSON body with `rectangleA` and `rectangleB`.

### Request body format

```json
{
  "rectangleA": { "minX": 0, "minY": 0, "maxX": 4, "maxY": 4 },
  "rectangleB": { "minX": 2, "minY": 2, "maxX": 6, "maxY": 6 }
}
```

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/analyze/intersection` | Check if edges cross and return crossing points |
| `GET` | `/analyze/containment` | Check if one rectangle is inside the other |
| `GET` | `/analyze/adjacency` | Check if rectangles share a boundary |

### Example — Intersection

**Request**
```
GET /analyze/intersection
```
```json
{
  "rectangleA": { "minX": 0, "minY": 0, "maxX": 4, "maxY": 4 },
  "rectangleB": { "minX": 2, "minY": 2, "maxX": 6, "maxY": 6 }
}
```

**Response**
```json
{
  "hasIntersection": true,
  "intersectionPoints": [
    { "x": 4.0, "y": 2.0 },
    { "x": 2.0, "y": 4.0 }
  ]
}
```

---

## Using the CLI

The CLI is inactive by default. Start the application with the `cli` profile to enable it:

```bash
./gradlew bootRun --args='--spring.profiles.active=cli'
```

You will be presented with an interactive menu:

```
╔══════════════════════════════╗
║     Rectangle Analyzer CLI   ║
╚══════════════════════════════╝

Choose an operation:
  1 - Intersection
  2 - Containment
  3 - Adjacency
  0 - Exit
Option:
```

Select an operation, then enter the two corner points of each rectangle when prompted:

```
Rectangle A — enter two corner points:
  x1: 0
  y1: 0
  x2: 4
  y2: 4
```

You are entering **point 1** as `(x1, y1)` and **point 2** as `(x2, y2)`. Any two opposite corners work — the constructor normalizes them automatically. The only constraint is that the two points cannot share an X or Y value (that would produce a zero-width or zero-height rectangle).

---

## Architecture

Requests flow through three layers:

```
AnalyzerController  →  AnalyzerService  →  AnalyzerStrategy (IntersectionStrategy | ContainmentStrategy | AdjacencyStrategy)
```

- **`AnalyzerController`** — receives HTTP requests and maps them to typed `Request` DTOs
- **`AnalyzerService`** — inspects the request type and delegates to the matching strategy
- **`AnalyzerStrategy`** — interface with a single `analyze(Request) → Result` method; each implementation casts the request to its specific subtype and executes the algorithm

---

## The Algorithms

### Rectangle Model
Defined by two corners `(x1, y1)` and `(x2, y2)`. The constructor normalizes input so `minX <= maxX` and `minY <= maxY` regardless of which corner is passed first. Zero-width or zero-height rectangles throw `IllegalArgumentException`.

---

### Intersection
Checks whether the **edges** of two rectangles physically cross and returns the exact crossing points.

Each rectangle has 4 edges represented as `Segment` objects (top, bottom, left, right). All 16 pairs are tested:
- Same-direction pairs (both horizontal or both vertical) are parallel — skipped
- One horizontal + one vertical — crossing point is `(vert.constantX, horiz.constantY)`, counted only if it falls **strictly inside** both segments

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

Tests use JUnit 5. Strategy tests use `@ExtendWith(SpringExtension.class)` and `@ContextConfiguration` to verify Spring bean wiring alongside algorithm correctness. The service test uses Mockito to verify routing logic. The controller test uses `@WebMvcTest` with `MockMvc`.

| Test Class | Tests | Coverage |
|---|---|---|
| `RectangleTest` | 6 | Normalization, zero-dimension validation, corner accessors, negative coordinates, cross-origin |
| `SegmentTest` | 6 | isHorizontal, constantX/Y, minX/maxX/minY/maxY — including reversed point order |
| `IntersectionStrategyTest` | 6 | Overlapping, separate, contained, cross-shape (4 points verified), touching edge, partial overlap |
| `ContainmentStrategyTest` | 9 | Fully contained, boundary, separate, overlap, identical, reversed inputs, A below B, B left of A, B below A |
| `AdjacencyStrategyTest` | 13 | Proper (all 4 directions), sub-line (both directions), partial, not adjacent (gap, diagonal, x-gap, y-gap, overlapping, corner-touch) |
| `AnalyzerServiceTest` | 4 | Routing to each strategy, null request handling |
| `AnalyzerControllerTest` | 3 | 200 response for each endpoint |

---

## Result Objects

Each algorithm returns a dedicated result object extending the `Result` base class.

| Class | Fields | Values |
|---|---|---|
| `IntersectionResult` | `hasIntersection`, `intersectionPoints` | `boolean` + `List<Point>` of crossing coordinates |
| `ContainmentResult` | `status` | `CONTAINED`, `INTERSECTION_NO_CONTAINMENT`, `NO_CONTAINMENT` |
| `AdjacencyResult` | `type` | `PROPER`, `SUB_LINE`, `PARTIAL`, `NOT_ADJACENT` |

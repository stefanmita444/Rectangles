package com.rectangles.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Represents a 2D point with x and y coordinates.
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    @NotNull
    private double x;
    @NotNull
    private double y;
}
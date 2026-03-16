package com.rectangles.dto;

import com.rectangles.domain.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdjacencyRequest extends Request {
    private Rectangle rectangleA;
    private Rectangle rectangleB;
}

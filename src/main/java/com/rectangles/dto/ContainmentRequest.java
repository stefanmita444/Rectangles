package com.rectangles.dto;

import com.rectangles.domain.Rectangle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContainmentRequest extends Request {
    @Valid @NotNull
    private Rectangle rectangleA;
    @Valid @NotNull
    private Rectangle rectangleB;
}

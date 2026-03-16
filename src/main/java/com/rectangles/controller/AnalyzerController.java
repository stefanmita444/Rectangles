package com.rectangles.controller;

import com.rectangles.domain.Result;
import com.rectangles.dto.AdjacencyRequest;
import com.rectangles.dto.ContainmentRequest;
import com.rectangles.dto.IntersectionRequest;
import com.rectangles.service.AnalyzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analyze")
public class AnalyzerController {
    private final AnalyzerService analyzerService;

    @GetMapping("/adjacency")
    public Result getAdjacency(@RequestBody AdjacencyRequest request) {
        return analyzerService.analyze(request);
    }

    @GetMapping("/intersection")
    public Result getIntersection(@RequestBody IntersectionRequest request) {
        return analyzerService.analyze(request);
    }

    @GetMapping("/containment")
    public Result getContainment(@RequestBody ContainmentRequest request) {
        return analyzerService.analyze(request);
    }
}

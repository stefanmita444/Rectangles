package com.rectangles.service;

import com.rectangles.domain.Result;
import com.rectangles.dto.AdjacencyRequest;
import com.rectangles.dto.ContainmentRequest;
import com.rectangles.dto.IntersectionRequest;
import com.rectangles.dto.Request;
import com.rectangles.service.strategy.AdjacencyStrategy;
import com.rectangles.service.strategy.AnalyzerStrategy;
import com.rectangles.service.strategy.ContainmentStrategy;
import com.rectangles.service.strategy.IntersectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzerService {
    private final AdjacencyStrategy adjacencyStrategy;
    private final ContainmentStrategy containmentStrategy;
    private final IntersectionStrategy intersectionStrategy;

    public Result analyze(Request request) {
        if (request == null) {
            return null;
        }

        AnalyzerStrategy analyzerStrategy = null;

        if (request instanceof IntersectionRequest) {
            analyzerStrategy = intersectionStrategy;
        }

        if (request instanceof ContainmentRequest) {
            analyzerStrategy = containmentStrategy;
        }

        if (request instanceof AdjacencyRequest) {
            analyzerStrategy = adjacencyStrategy;
        }

        if (analyzerStrategy == null) {
            return null;
        }

        return analyzerStrategy.analyze(request);
    }
}

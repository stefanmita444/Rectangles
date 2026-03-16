package com.rectangles.service.strategy;

import com.rectangles.domain.Result;
import com.rectangles.dto.Request;

public interface AnalyzerStrategy {
    Result analyze(Request request);
}

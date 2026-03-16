package com.rectangles.service;

import com.rectangles.domain.Result;
import com.rectangles.dto.Request;
import com.rectangles.service.strategy.AnalyzerStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyzerService {

    private final Map<Class<?>, AnalyzerStrategy<?>> strategyMap;

    public AnalyzerService(List<AnalyzerStrategy<?>> strategies) {
        this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(AnalyzerStrategy::getSupportedRequestType, s -> s));
    }

    @SuppressWarnings("unchecked")
    public Result analyze(Request request) {
        if (request == null) return null;
        AnalyzerStrategy<Request> strategy = (AnalyzerStrategy<Request>) strategyMap.get(request.getClass());
        return strategy != null ? strategy.analyze(request) : null;
    }
}
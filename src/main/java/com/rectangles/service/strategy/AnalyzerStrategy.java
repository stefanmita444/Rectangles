package com.rectangles.service.strategy;

import com.rectangles.domain.Result;
import com.rectangles.dto.Request;

/**
 * Strategy interface for rectangle analysis operations.
 *
 * <p>Each implementation handles exactly one request type, declared via
 * {@link #getSupportedRequestType()}. {@link com.rectangles.service.AnalyzerService}
 * uses this to build a dispatch map at startup — no {@code instanceof} checks needed.</p>
 *
 * @param <T> the concrete {@link Request} subtype this strategy handles
 */
public interface AnalyzerStrategy<T extends Request> {

    /**
     * Executes the analysis for the given request.
     *
     * @param request the typed request containing the two rectangles
     * @return a {@link Result} describing the relationship between the rectangles
     */
    Result analyze(T request);

    /**
     * Returns the exact {@link Request} subtype this strategy handles.
     * Used by {@link com.rectangles.service.AnalyzerService} to register
     * this strategy in the dispatch map at startup.
     *
     * @return the class object for {@code T}
     */
    Class<T> getSupportedRequestType();
}
